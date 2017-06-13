package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;


public class SyncActivity extends Activity {

    public String state;
    public static Boolean transferFlag = false;
    String packageName = null;
    static File file;
    String className = null;
    boolean found = false;
    static long time = 30000;
    static BluetoothAdapter btAdapter;
    Intent intent = null;
    int res;
    private static final int DISCOVER_DURATION = 3000;
    private static final int REQUEST_BLU = 1;
    String deviceId;
    public static ProgressDialog progress;
    public static JSONArray _array;
    RadioGroup radioGroup;
    public static boolean scoreNotAvailable = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sync);

        Intent i = getIntent();
        String id = i.getStringExtra("UserName");
        Toast.makeText(this, "Welcome " + id, Toast.LENGTH_SHORT).show();

        scoreNotAvailable = false;
        Button b1 = (Button) findViewById(R.id.pull);
        Button b2 = (Button) findViewById(R.id.push);
        Button b3 = (Button) findViewById(R.id.ReceiveProfile);
        Button b4 = (Button) findViewById(R.id.selectGroups);
        Button b6 = (Button) findViewById(R.id.pushDataToServer);

        TextView tv = (TextView) findViewById(R.id.message);
        tv.setText("");
    }

    public void AssignGroups(View v) {

        Intent intent = new Intent(SyncActivity.this, AssignGroups.class);
        startActivity(intent);

        // Old Code
        /*
        StatusDBHelper statusDBHelper = new StatusDBHelper(getApplicationContext());
        String pullFlag = statusDBHelper.getValue("pullFlag");
        //LogInPage.sharedPreferences=getApplicationContext().getSharedPreferences("SelectedValues",getApplicationContext().MODE_PRIVATE);
        if (pullFlag.equals("0")) {
            showDialogue("Please pull the data first, by selecting your state.");
        } else {
            Intent intent = new Intent(SyncActivity.this, AssignGroups.class);
            startActivity(intent);
        }*/
    }


    public void pullData(View v) {

        TextView tv = (TextView) findViewById(R.id.message);
        //tv.setText("");

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select the state and pull data...");

        final LinearLayout layout = new LinearLayout(this, null);
        layout.setOrientation(LinearLayout.VERTICAL);


        TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(5, 0, 0, 0);
        int i = 0;

        radioGroup = new RadioGroup(this);
        RadioButton radioButton = new RadioButton(this);
        radioButton.setId(i);
        i++;
        radioButton.setTextColor(Color.BLACK);
        radioButton.setText("Maharashtra");
        radioButton.setTextSize(25);
        radioGroup.addView(radioButton, lp);

        RadioButton radioButton1 = new RadioButton(this);
        radioButton1.setId(i);
        i++;
        radioButton1.setTextColor(Color.BLACK);
        radioButton1.setText("Rajasthan");
        radioButton1.setTextSize(25);
        radioGroup.addView(radioButton1, lp);

        RadioButton radioButton2 = new RadioButton(this);
        radioButton2.setId(i);
        radioButton2.setTextColor(Color.BLACK);
        radioButton2.setText("Uttar Pradesh");
        radioButton2.setTextSize(25);
        radioGroup.addView(radioButton2, lp);

        layout.addView(radioGroup);

        alert.setView(layout);

        alert.setCancelable(false);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        final AlertDialog dialog = alert.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean doNotClose = false;
                String state = null;
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if (selectedId != -1) {

                    RadioButton radioButton = (RadioButton) layout.findViewById(selectedId);
                    state = radioButton.getText().toString();
                    switch (state) {
                        case "Maharashtra":
                            state = "MH";
                            break;
                        case "Rajasthan":
                            state = "RJ";
                            break;
                        case "Uttar Pradesh":
                            state = "UP";
                            break;
                    }
                } else {
                    doNotClose = true;
                    Toast toast = Toast.makeText(getApplicationContext(), "Please select state.", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                }
                if (!doNotClose) {
                    dialog.dismiss();
                    TextView msg = (TextView) findViewById(R.id.message);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        SyncClient syncClient = new SyncClient(SyncActivity.this, state, msg);
                        syncClient.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
                    } else {
                        SyncClient syncClient = new SyncClient(SyncActivity.this, state, msg);
                        syncClient.execute("");
                    }
                }
            }
        });

    }


    public void WriteSettings(Context context, String data, String fName) {
        FileOutputStream fOut = null;
        OutputStreamWriter osw = null;
        deviceId = ((TelephonyManager) getApplicationContext()
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();

        try {
            File dir = Environment.getExternalStorageDirectory();
            File file = new File(dir, "." + fName + "_" + deviceId + ".json");
            try {
                fOut = new FileOutputStream(file);

                osw = new OutputStreamWriter(fOut);
                osw.write(data);
                osw.flush();
                osw.close();
                fOut.close();

            } catch (Exception e) {
                SyncActivityLogs syncActivityLogs = new SyncActivityLogs(getApplicationContext());
                syncActivityLogs.addToDB("WriteSettings-SyncActivity", e, "Error");
            } finally {

            }
        } catch (Exception e) {
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(getApplicationContext());
            syncActivityLogs.addToDB("WriteSettings-SyncActivity", e, "Error");
            e.printStackTrace();
            Toast.makeText(context, "Settings not saved", Toast.LENGTH_SHORT).show();
        }
    }

    public void test() {
        try {
            // Open your local db as the input stream
            InputStream myInput = getApplicationContext().getAssets().open("PrathamTabDB.db");

            // Path to the just created empty db
            String outFileName = "/data/data/com.example.pef.prathamopenschool/databases/PrathamTabDB.db";

            OutputStream myOutput = new FileOutputStream(outFileName);

            // transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            // Close the streams
            myOutput.flush();
            myOutput.close();
            myInput.close();
            BackupDatabase.backup(getApplicationContext());
        } catch (Exception e) {
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(getApplicationContext());
            syncActivityLogs.addToDB("test-SyncActivity", e, "Error");
            // BackupDatabase.backup(getApplicationContext());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, StartingActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("Exit me", true);
        startActivity(intent);
        finish();
    }

    public JSONArray readFromDatabase(int table) {

        JSONArray _array = new JSONArray();
        JSONArray _arrayLog = new JSONArray();
        String requestString = "";
        if (table == 1)//score transfer
        {
            try {
                List<Score> _scores = new ScoreDBHelper(this).GetAll();


                if (_scores == null) {
                } else {
                    for (int x = 0; x < _scores.size(); x++) {
                        JSONObject _obj = new JSONObject();
                        Score score = _scores.get(x);
                        _obj.put("PlayerId", score.PlayerID);
                        _obj.put("SessionId", score.SessionID);
                        _obj.put("GroupId", score.GroupID);
                        _obj.put("DeviceId", score.DeviceID);
                        _obj.put("ResourceId", score.ResourceID);
                        _obj.put("QuestionId", score.QuestionId);
                        _obj.put("ScoredMarks", score.ScoredMarks);
                        _obj.put("TotalMarks", score.TotalMarks);
                        _obj.put("StartDateTime", score.StartTime);
                        _obj.put("EndDateTime", score.EndTime);
                        _obj.put("Level", score.Level);

                        _array.put(_obj);
                    }
                }

            } catch (Exception ex) {
                SyncActivityLogs syncActivityLogs = new SyncActivityLogs(getApplicationContext());
                syncActivityLogs.addToDB("readFromDatabase-SyncActivity", ex, "Error");
                BackupDatabase.backup(getApplicationContext());
            }
            return _array;
        } else {//get logs from database
            try {
                List<Logs> _logs = new LogsDBHelper(this).GetAll();

                if (_logs == null) {
                } else {
                    for (int x = 0; x < _logs.size(); x++) {
                        JSONObject _obj = new JSONObject();
                        Logs logs = _logs.get(x);//Changed by Ameya on 24/11
                        _obj.put("LogID", logs.logId);
                        _obj.put("CurrentDateTime", logs.currentDateTime);
                        _obj.put("ExceptionMsg", logs.exceptionMessage);
                        _obj.put("ExceptionStackTrace", logs.exceptionStackTrace);
                        _obj.put("MethodName", logs.methodName);
                        _obj.put("Type", logs.errorType);
                        _obj.put("GroupId", logs.groupId);
                        _obj.put("DeviceId", logs.deviceId);
                        _obj.put("LogDetail", logs.LogDetail);
                        _arrayLog.put(_obj);

                    }


                }
            } catch (Exception e) {
            }
            return _arrayLog;
        }

    }


    public void transferData(View v) {
//************************** integrate push data code here********************/

        ArrayList<String> arrayList = new ArrayList<String>();
        TextView msg = (TextView) findViewById(R.id.message);
        _array = new JSONArray();
        TextView tv = (TextView) findViewById(R.id.message);
        tv.setText("");
        //  test();
        //test function is used only for reading database file from assets
        //Used when we want to push data from our side.

        //enableBlu();
        progress = new ProgressDialog(SyncActivity.this);
        progress.setMessage("Please Wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();


        ScoreDBHelper scoreDBHelper = new ScoreDBHelper(this);
        List<Score> scores = scoreDBHelper.GetAll();

        if (scores == null) {
        } else if (scores.size() == 0) {
            progress.dismiss();
        } else {
            try {

                JSONArray scoreData = new JSONArray(), logsData = new JSONArray(), attendanceData;

                for (int i = 0; i < scores.size(); i++) {
                    JSONObject _obj = new JSONObject();
                    Score _score = scores.get(i);

                    try {
                        _obj.put("SessionID", _score.SessionID);
                        // _obj.put("PlayerID",_score.PlayerID);
                        _obj.put("GroupID", _score.GroupID);
                        _obj.put("DeviceID", _score.DeviceID);
                        _obj.put("ResourceID", _score.ResourceID);
                        _obj.put("QuestionID", _score.QuestionId);
                        _obj.put("ScoredMarks", _score.ScoredMarks);
                        _obj.put("TotalMarks", _score.TotalMarks);
                        _obj.put("StartDateTime", _score.StartTime);
                        _obj.put("EndDateTime", _score.EndTime);
                        _obj.put("Level", _score.Level);

                        scoreData.put(_obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LogsDBHelper logsDBHelper = new LogsDBHelper(getApplicationContext());
                List<Logs> logs = logsDBHelper.GetAll();

                if (logs == null) {
                } else {
                    for (int x = 0; x < logs.size(); x++) {
                        JSONObject _obj = new JSONObject();
                        Logs _logs = logs.get(x);//Changed by Ameya on 24/11
                        try {
                            _obj.put("CurrentDateTime", _logs.currentDateTime);
                            _obj.put("ExceptionMsg", _logs.exceptionMessage);
                            _obj.put("ExceptionStackTrace", _logs.exceptionStackTrace);
                            _obj.put("MethodName", _logs.methodName);
                            _obj.put("Type", _logs.errorType);
                            _obj.put("GroupId", _logs.groupId);
                            _obj.put("DeviceId", _logs.deviceId);
                            _obj.put("LogDetail", _logs.LogDetail);
                            logsData.put(_obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    AttendanceDBHelper attendanceDBHelper1 = new AttendanceDBHelper(this);
                    attendanceData = attendanceDBHelper1.GetAll();

                    if (attendanceData == null) {
                    } else {
                        for (int i = 0; i < attendanceData.length(); i++) {
                            JSONObject jsonObject = attendanceData.getJSONObject(i);

                            String ids[] = jsonObject.getString("PresentStudentIds").split(",");
                            JSONArray presentStudents = new JSONArray();
                            for (int j = 0; j < ids.length; j++) {
                                JSONObject id = new JSONObject();
                                id.put("id", Integer.parseInt(ids[j]));

                                presentStudents.put(id);
                            }
                            jsonObject.remove("PresentStudentIds");
                            jsonObject.put("PresentStudentIds", presentStudents);
                        }

                        StatusDBHelper statusDBHelper = new StatusDBHelper(this);
                        JSONObject obj = new JSONObject();
                        obj.put("ScoreCount", scores.size());
                        obj.put("AttendanceCount", attendanceData.length());
                        obj.put("LogsCount", logs.size());
                        obj.put("TransId", new Utility().GetUniqueID());
                        obj.put("ActivatedDate", statusDBHelper.getValue("ActivatedDate"));
                        obj.put("ActivatedForGroups", statusDBHelper.getValue("ActivatedForGroups"));
                        obj.put("DeviceId", StartingActivity.deviceId);
                        obj.put("MobileNumber", "0");

                        String requestString = "{ \"metadata\": " + obj + ",\"scoreData\": " + scoreData + ", \"logsData\": " + logsData + ", \"attendanceData\": " + attendanceData + "}";

                        WriteSettings(getApplicationContext(), requestString, "Data");
                        TreansferFile("Data");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void TreansferFile(String filename) {

        progress.dismiss();
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter == null) {
            Toast.makeText(getApplicationContext(), "This device doesn't give bluetooth support.", Toast.LENGTH_LONG).show();
        } else {
            intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            intent.setType("text/plain");
            file = new File(Environment.getExternalStorageDirectory() + "/." + filename + "_" + deviceId + ".json");
            int x = 0;
            if (file.exists()) {

                PackageManager pm = getPackageManager();
                List<ResolveInfo> appsList = pm.queryIntentActivities(intent, 0);
                if (appsList.size() > 0) {

                    for (ResolveInfo info : appsList) {
                        packageName = info.activityInfo.packageName;
                        if (packageName.equals("com.android.bluetooth")) {
                            className = info.activityInfo.name;
                            found = true;
                            break;// found
                        }
                    }
                    if (!found) {
                        Toast.makeText(this, "Bluetooth not in list", Toast.LENGTH_SHORT).show();
                    } else {


                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                        intent.setClassName(packageName, className);
                        startActivityForResult(intent, 0);

                        //sendBroadcast(intent);
                    }
                }
            } else
                Toast.makeText(getApplicationContext(), "File not found", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_sync, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        res = resultCode;
        if (res == 0) {
            if (btAdapter.isEnabled()) {
                progress = new ProgressDialog(SyncActivity.this);
                progress.setTitle("Please Wait...");
                progress.setMessage("We are processing your request! Please check in notification bar.");
                progress.setCanceledOnTouchOutside(false);
                progress.setCancelable(false);
                progress.show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                    }
                }, 30000);
            }
        } else if (!(resultCode == DISCOVER_DURATION && requestCode == REQUEST_BLU)) {
            Toast.makeText(this, "BT cancelled", Toast.LENGTH_SHORT).show();
        }


    }

    public void enableBlu() {
        // enable device discovery - this will automatically enable Bluetooth
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (!btAdapter.isEnabled()) {

        }
    }


    public void transferLogs(View v) {

        ArrayList<String> arrayList = new ArrayList<String>();
        TextView msg = (TextView) findViewById(R.id.message);
        _array = new JSONArray();
        progress = new ProgressDialog(SyncActivity.this);
        progress.setMessage("Please Wait...");
        progress.setCanceledOnTouchOutside(false);
        progress.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                _array = readFromDatabase(0);
                if (_array.length() == 0) {
                    progress.dismiss();
                } else {
                    WriteSettings(getApplicationContext(), _array.toString(), "Logs");
                    TreansferFile("Logs");
                }

            }
        }, 3000);
    }


    public void pushDataToServer(View v) {
        //we will push logs and scores directly to the server

        TextView msg = (TextView) findViewById(R.id.message);
        msg.setText("");

        ScoreDBHelper scoreDBHelper = new ScoreDBHelper(this);
        List<Score> scores = scoreDBHelper.GetAll();

        if (scores == null) {
        } else if (scores.size() == 0) {
        } else {
            try {

                JSONArray scoreData = new JSONArray(), logsData = new JSONArray(), attendanceData;

                for (int i = 0; i < scores.size(); i++) {
                    JSONObject _obj = new JSONObject();
                    Score _score = scores.get(i);

                    try {
                        _obj.put("SessionID", _score.SessionID);
                        // _obj.put("PlayerID",_score.PlayerID);
                        _obj.put("GroupID", _score.GroupID);
                        _obj.put("DeviceID", _score.DeviceID);
                        _obj.put("ResourceID", _score.ResourceID);
                        _obj.put("QuestionID", _score.QuestionId);
                        _obj.put("ScoredMarks", _score.ScoredMarks);
                        _obj.put("TotalMarks", _score.TotalMarks);
                        _obj.put("StartDateTime", _score.StartTime);
                        _obj.put("EndDateTime", _score.EndTime);
                        _obj.put("Level", _score.Level);

                        scoreData.put(_obj);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LogsDBHelper logsDBHelper = new LogsDBHelper(getApplicationContext());
                List<Logs> logs = logsDBHelper.GetAll();

                if (logs == null) {
                } else {
                    for (int x = 0; x < logs.size(); x++) {
                        JSONObject _obj = new JSONObject();
                        Logs _logs = logs.get(x);//Changed by Ameya on 24/11
                        try {
                            _obj.put("CurrentDateTime", _logs.currentDateTime);
                            _obj.put("ExceptionMsg", _logs.exceptionMessage);
                            _obj.put("ExceptionStackTrace", _logs.exceptionStackTrace);
                            _obj.put("MethodName", _logs.methodName);
                            _obj.put("Type", _logs.errorType);
                            _obj.put("GroupId", _logs.groupId);
                            _obj.put("DeviceId", _logs.deviceId);
                            _obj.put("LogDetail", _logs.LogDetail);
                            logsData.put(_obj);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    AttendanceDBHelper attendanceDBHelper1 = new AttendanceDBHelper(this);
                    attendanceData = attendanceDBHelper1.GetAll();

                    if (attendanceData == null) {
                    } else {
                        for (int i = 0; i < attendanceData.length(); i++) {
                            JSONObject jsonObject = attendanceData.getJSONObject(i);

                            String ids[] = jsonObject.getString("PresentStudentIds").split(",");
                            JSONArray presentStudents = new JSONArray();
                            for (int j = 0; j < ids.length; j++) {
                                JSONObject id = new JSONObject();
                                id.put("id", Integer.parseInt(ids[j]));

                                presentStudents.put(id);
                            }
                            jsonObject.remove("PresentStudentIds");
                            jsonObject.put("PresentStudentIds", presentStudents);
                        }

                        StatusDBHelper statusDBHelper = new StatusDBHelper(this);
                        JSONObject obj = new JSONObject();
                        obj.put("ScoreCount", scores.size());
                        obj.put("AttendanceCount", attendanceData.length());
                        obj.put("LogsCount", logs.size());
                        obj.put("TransId", new Utility().GetUniqueID());
                        obj.put("DeviceId", StartingActivity.deviceId);
                        obj.put("MobileNumber", "0");
                        obj.put("ActivatedDate", statusDBHelper.getValue("ActivatedDate"));
                        obj.put("ActivatedForGroups", statusDBHelper.getValue("ActivatedForGroups"));
                        ArrayList<String> arrayList = new ArrayList<String>();
                        String requestString = "{ \"metadata\": " + obj + ", \"scoreData\": " + scoreData + ", \"logsData\": " + logsData + ", \"attendanceData\": " + attendanceData + "}";
                        arrayList.add(requestString);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                            new PushSyncClient(this, msg).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, arrayList);
                        else
                            new PushSyncClient(this, msg).execute(arrayList);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    static Boolean ClearDatabaseEntriesDuringFetchData(String className, Context mContext) {

        if (className.equals("Scores")) {
            ScoreDBHelper scoreDBHelper = new ScoreDBHelper(mContext);
            return scoreDBHelper.DeleteAll();
        } else if (className.equals("Logs")) {
            LogsDBHelper logsDBHelper = new LogsDBHelper(mContext);
            return logsDBHelper.DeleteAll();
        } else if (className.equals("Attendance")) {
            AttendanceDBHelper attendanceDBHelper = new AttendanceDBHelper(mContext);
            return attendanceDBHelper.DeleteAll();
        }
        return false;
    }


    public void goToStudentProfiles(View view) {
        Intent i = new Intent(SyncActivity.this, AddStudentProfiles.class);
        startActivity(i);
    }


    public void goToShareData(View view) {
        Intent goToShareD = new Intent(SyncActivity.this, ShareData.class);
        startActivity(goToShareD);
    }


    public void goToAddNewGroup(View view) {
        Intent goToAddNewGrp = new Intent(SyncActivity.this, AddNewGroup.class);
        startActivity(goToAddNewGrp);
    }


    public void goToAddNewCrl(View view) {

        Intent i = new Intent(SyncActivity.this, AddNewCrl.class);
        startActivity(i);

    }


    public void pullDataOffline(View view) {
        Intent goToPullDataOffline = new Intent(SyncActivity.this, PullData.class);
        startActivity(goToPullDataOffline);
    }

    public void ReceiveProfiles(View view) {
    }
}
