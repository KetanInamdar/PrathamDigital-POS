package com.example.pef.prathamopenschool;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;

public class splashScreenVideo extends AppCompatActivity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {
    VideoView splashVideo;
    public static String appname = "";
    public static String fpath;
    Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen_video);

        getSdCardPath();

        context = this;

        CrlDBHelper db = new CrlDBHelper(context);
        VillageDBHelper vdb = new VillageDBHelper(context);

        // Check initial DB Entry emptiness for populating data
        Boolean crlResult = db.checkTableEmptyness();
        Boolean villageResult = vdb.checkTableEmptyness();

        if (crlResult == false || villageResult == false) {
            try {
                SetInitialValues();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {

        }


        splashVideo = (VideoView) findViewById(R.id.videoView2);

        Play(Uri.parse(fpath + "/Media/splashVideo.mp4"));
        splashVideo.setOnPreparedListener(this);
        splashVideo.setOnCompletionListener(this);


        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void Play(Uri path) {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(splashVideo);
        try {
            splashVideo.setVideoURI(path);
        } catch (Exception e) {
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(getApplicationContext());
            syncActivityLogs.addToDB("play-videoPlay", e, "Error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        splashVideo.setMediaController(mediaController);
        splashVideo.requestFocus();

    }


    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();

        this.overridePendingTransition(R.anim.lefttoright, R.anim.lefttoright);
    }

    public void getSdCardPath() {
        CharSequence c = "";

        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
        try {
            c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
            appname = c.toString();
            Log.w("LABEL", c.toString());
        } catch (Exception e) {//Name Not FOund Exception
        }

        if (appname.equals("Pratham Digital Optimized")) {
            if ((new File("/storage/extSdCard/.PrathamOpenSchool/HLearning/").exists()) && (new File("/storage/extSdCard/.PrathamOpenSchool/KhelBadi/").exists()) && (new File("/storage/extSdCard/.PrathamOpenSchool/KhelPuri/").exists()) && (new File("/storage/extSdCard/.PrathamOpenSchool/Media/").exists()) && (new File("/storage/extSdCard/.PrathamOpenSchool/AddNewVideos/").exists())) {
                fpath = "/storage/extSdCard/";
            } else if ((new File("/storage/sdcard1/.PrathamOpenSchool/HLearning/").exists()) && (new File("/storage/sdcard1/.PrathamOpenSchool/KhelBadi/").exists()) && (new File("/storage/sdcard1/.PrathamOpenSchool/KhelPuri/").exists()) && (new File("/storage/sdcard1/.PrathamOpenSchool/Media/").exists()) && (new File("/storage/sdcard1/.PrathamOpenSchool/AddNewVideos/").exists())) {
                fpath = "/storage/sdcard1/";
            } else if ((new File("/storage/usbcard1/.PrathamOpenSchool/HLearning/").exists()) && (new File("/storage/usbcard1/.PrathamOpenSchool/KhelBadi/").exists()) && (new File("/storage/usbcard1/.PrathamOpenSchool/KhelPuri/").exists()) && (new File("/storage/usbcard1/.PrathamOpenSchool/Media/").exists()) && (new File("/storage/usbcard1/.PrathamOpenSchool/AddNewVideos/").exists())) {
                fpath = "/storage/usbcard1/";

            } else if ((new File("/storage/sdcard0/.PrathamOpenSchool/HLearning/").exists()) && (new File("/storage/sdcard0/.PrathamOpenSchool/KhelBadi/").exists()) && (new File("/storage/sdcard0/.PrathamOpenSchool/KhelPuri/").exists()) && (new File("/storage/sdcard0/.PrathamOpenSchool/Media/").exists()) && (new File("/storage/sdcard0/.PrathamOpenSchool/AddNewVideos/").exists())) {
                fpath = "/storage/sdcard0/";

            } else if ((new File("/storage/emulated/0/.PrathamOpenSchool/HLearning/").exists()) && (new File("/storage/emulated/0/.PrathamOpenSchool/KhelBadi/").exists()) && (new File("/storage/emulated/0/.PrathamOpenSchool/KhelPuri/").exists()) && (new File("/storage/emulated/0/.PrathamOpenSchool/Media/").exists()) && (new File("/storage/emulated/0/.PrathamOpenSchool/AddNewVideos/").exists())) {
                fpath = "/storage/emulated/0/";
            }
            fpath = fpath + ".PrathamOpenSchool/";
        }
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        splashVideo.start();
    }


    void SetInitialValues() throws JSONException {

        // insert your code to run only when application is started first time here
        context = this;

        //CRL Initial DB Process
        CrlDBHelper db = new CrlDBHelper(context);
        // For Loading CRL Json From External Storage (Assets)
        JSONArray crlJsonArray = new JSONArray(loadCrlJSONFromAsset());
        for (int i = 0; i < crlJsonArray.length(); i++) {

            JSONObject clrJsonObject = crlJsonArray.getJSONObject(i);
            Crl crlobj = new Crl();
            crlobj.CRLId = clrJsonObject.getString("CRLId");
            crlobj.FirstName = clrJsonObject.getString("FirstName");
            crlobj.LastName = clrJsonObject.getString("LastName");
            crlobj.UserName = clrJsonObject.getString("UserName");
            crlobj.Password = clrJsonObject.getString("Password");
            crlobj.ProgramId = clrJsonObject.getInt("ProgramId");
            crlobj.Mobile = clrJsonObject.getString("Mobile");
            crlobj.State = clrJsonObject.getString("State");
            crlobj.Email = clrJsonObject.getString("Email");

            db.insertData(crlobj);
        }

        //Villages Initial DB Process
        VillageDBHelper database = new VillageDBHelper(context);
        // For Loading Villages Json From External Storage (Assets)
        JSONArray villagesJsonArray = new JSONArray(loadVillageJSONFromAsset());

        for (int j = 0; j < villagesJsonArray.length(); j++) {
            JSONObject villagesJsonObject = villagesJsonArray.getJSONObject(j);

            Village villageobj = new Village();
            villageobj.VillageID = villagesJsonObject.getInt("VillageId");
            villageobj.VillageCode = villagesJsonObject.getString("VillageCode");
            villageobj.VillageName = villagesJsonObject.getString("VillageName");
            villageobj.Block = villagesJsonObject.getString("Block");
            villageobj.District = villagesJsonObject.getString("District");
            villageobj.State = villagesJsonObject.getString("State");
            villageobj.CRLID = villagesJsonObject.getString("CRLId");

            database.insertData(villageobj);
        }

    }

    // Reading CRL Json From SDCard
    public String loadCrlJSONFromAsset() {
        String crlJsonStr = null;

        try {
            File crlJsonSDCard = new File(fpath + "Json/Crl.json");
            FileInputStream stream = new FileInputStream(crlJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                crlJsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return crlJsonStr;
    }

    // Reading Village Json From SDCard
    public String loadVillageJSONFromAsset() {
        String villageJson = null;
        try {
            File villageJsonSDCard = new File(fpath + "Json/Village.json");
            FileInputStream stream = new FileInputStream(villageJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                villageJson = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return villageJson;

    }

}
