package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by PEF-2 on 12/10/2015.
 */
public class PushSyncClient extends AsyncTask<ArrayList<String>, Void, ArrayList<String>> {
    private Context mContext;
    ProgressDialog progressDialog;
    public TextView v;
    public Activity act;

    PushSyncClient(){

    }

    PushSyncClient(Context c,TextView v){
        //act = grpContext;
        mContext=c;
        this.v=v;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = ProgressDialog.show(mContext, "PLEASE WAIT", "Data is being pushed to server.");
    }

    protected ArrayList<String> doInBackground(ArrayList<String>... urls) {

        ArrayList<String> result=new ArrayList<String>();

        Iterator<String> iterator=urls[0].iterator();
        String url1=iterator.next();//logs
        //String url2=iterator.next();//scores

        JSONArray _jsonArray = null;
        ConnectivityManager check=(ConnectivityManager)
                this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = check.getAllNetworkInfo();

        int i;
        for ( i = 0; i<info.length; i++){
            if (info[i].getState() == NetworkInfo.State.CONNECTED){
                break;
            }
        }

        if(i<info.length){
            String resultFromServerForLogs,resultFromServer;

            resultFromServer = this.PostDataToServer(Utility.getProperty("pushToServerURL",mContext), url1);

            if(resultFromServer.equals("Success"))
                result.add("Success");
            else result.add(resultFromServer);

            return result;
          }
        else{
            result.add("no internet connection.Please check your network connection.");
            return result;
        }
    }

    protected void onPostExecute(ArrayList<String> result) {
        Iterator<String> iterator=result.iterator();
        String actualResult=iterator.next();
        //String resOfLogs=iterator.next();
        progressDialog.dismiss();

        if(actualResult.equals("Success")) {
            if(this.ClearDatabaseEntriesDuringFetchData("Attendance", mContext) && this.ClearDatabaseEntriesDuringFetchData("Scores", mContext) && this.ClearDatabaseEntriesDuringFetchData("Logs", mContext)){
                BackupDatabase.backup(mContext);
                v.setTextColor(Color.WHITE);

                v.setText("Data is pushed to the server.");

                Animation animation1 = AnimationUtils.loadAnimation(mContext, R.anim.blink);
                v.startAnimation(animation1);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        v.clearAnimation();
                        v.setText("");
                    }
                }, 30000);
            }
            else{
                v.setTextColor(Color.RED);
                v.setText("Data deletion from database failed.");
            }
        }
        else{
            BackupDatabase.backup(mContext);
            v.setTextColor(Color.RED);
            v.setText("Data push failed due to "+actualResult);

        }

    }


    Boolean ClearDatabaseEntriesDuringFetchData(String className, Context mContext) {

        if(className.equals("Scores")){
            ScoreDBHelper scoreDBHelper=new ScoreDBHelper(mContext);
            return scoreDBHelper.DeleteAll();
        }
        else if(className.equals("Logs")){
            LogsDBHelper logsDBHelper=new LogsDBHelper(mContext);
            return logsDBHelper.DeleteAll();
        }
        else if(className.equals("Attendance")){
            AttendanceDBHelper attendanceDBHelper = new AttendanceDBHelper(mContext);
            return attendanceDBHelper.DeleteAll();
        }
        return false;
    }

    String PostDataToServer(String ServiceURL, String data)
    {
        String _output = "";
        try
        {

            HttpParams httpParameters = new BasicHttpParams();
            int timeoutConnection = 60000;
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
            int timeoutSocket = 900000;
            HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
            HttpClient client = new DefaultHttpClient(httpParameters);

            HttpPost post = new HttpPost(ServiceURL);
            StringEntity input = new StringEntity(data);
            input.setContentType("application/json");
            post.setEntity(input);
            HttpResponse response = client.execute(post);

            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                _output += line;
            }
            JSONObject obj = new JSONObject(_output);
            if(obj.getString("ErrorId").equals("1"))
                return "Success";
            else return obj.getString("ErrorDesc");
            // return true;
        }
        catch(Exception ex){
         
            SyncActivityLogs syncActivityLogs=new SyncActivityLogs(mContext);
            syncActivityLogs.addToDB("doInBackground-PushSyncClient",ex,"Error");
            BackupDatabase.backup(mContext);
            return ex.getMessage();
        }
    }
}
