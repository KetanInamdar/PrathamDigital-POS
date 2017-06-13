package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.VideoView;

public class PlayVideo extends Activity implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener {

    CountDownTimer cd;

    VideoView myVideoView;
    boolean timer;
    long duration;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        setContentView(R.layout.activity_play_video);

        myVideoView = (VideoView) findViewById(R.id.videoView1);
        String groupId=getIntent().getStringExtra("path");
        playVideo(Uri.parse(groupId));
        myVideoView.setOnPreparedListener(this);
        myVideoView.setOnCompletionListener(this);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }

    public void playVideo(Uri path)
    {
        MediaController mediaController = new MediaController(this);
        mediaController.setAnchorView(myVideoView);
        try {
            myVideoView.setVideoURI(path);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        myVideoView.setMediaController(mediaController);
        myVideoView.requestFocus();

    }

    @Override
    protected void onPause() {
        super.onPause();
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
                    //System.exit(0);
                    //app pn close kara
                }
            }
        }.start();
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
}

