package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * Created by PEF on 22/01/2016.
 */
public class VideoPlay extends Activity implements OnCompletionListener, OnPreparedListener {

    CountDownTimer cd;
    VideoView myVideoView;
    boolean timer;
    long duration;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        try{
            setContentView(R.layout.videoplay);
            myVideoView = (VideoView) findViewById(R.id.videoView1);
            JSInterface.MediaFlag = true;
            String groupId=getIntent().getStringExtra("path");
            Play(Uri.parse(groupId));
            myVideoView.setOnPreparedListener(this);
            myVideoView.setOnCompletionListener(this);

            if(JSInterface.VideoFlag == 1)
                myVideoView.setMediaController(null);

            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        catch (Exception e){
        }
    }

    public void Play(Uri path)
    {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(myVideoView);
        try {
            myVideoView.setVideoURI(path);
        } catch (Exception e) {
            SyncActivityLogs syncActivityLogs=new SyncActivityLogs(getApplicationContext());
            syncActivityLogs.addToDB("play-videoPlay",e, "Error");
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.setMediaController(mediaController);
        myVideoView.requestFocus();

    }

    @Override
    protected void onPause() {
        super.onPause();
        try{
            myVideoView.pause();
            cd = new CountDownTimer(duration, 1000) {
                //cd = new CountDownTimer(duration, 1000) {
                @Override
                public void onTick(long millisUntilFinished)
                {
                    duration = millisUntilFinished;
                    timer = true;
                }
                @Override
                public void onFinish() {
                    timer = false;
                    PowerManager powermanager = (PowerManager)getSystemService(POWER_SERVICE);
                    boolean screen = powermanager.isScreenOn();
                    if(!screen)
                    {
                        finish();
                    }
                }
            }.start();
        }
        catch (Exception e){
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("REMAINING TIME FOR VIDEO IS :"+duration);
        if(timer == true)
        {
            cd.cancel();
            myVideoView.start();
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        this.finish();
        //JSInterface.MediaFlag=false;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        myVideoView.start();
        duration = myVideoView.getDuration();
    }

    @Override
    public void onBackPressed() {
        if(JSInterface.VideoFlag == 1){
            JSInterface.VideoFlag = 0;
            finish();
           // MainActivity.webView.goBack();
        }
        else finish();
    }
}