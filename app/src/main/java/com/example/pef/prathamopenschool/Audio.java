package com.example.pef.prathamopenschool;

import android.media.MediaRecorder;
import android.util.Log;

public class Audio extends Thread
{
    private boolean stopped = false;
    MediaRecorder mediaRecorder;
    private boolean isRecording=false;


    /**
     * Give the thread high priority so that it's not canceled unexpectedly, and start it
     */
    public Audio()
    {
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);
    }

    @Override
    public void run()
    {
        try
        {

            try {
                mediaRecorder = new MediaRecorder();
                mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
                mediaRecorder.setAudioEncodingBitRate(96000);
                mediaRecorder.setAudioSamplingRate(44100);
                mediaRecorder.setOutputFile("/storage/sdcard0/recordGame.mp4");


                mediaRecorder.prepare();
            } catch (Exception e) {
                e.printStackTrace();
            }

            mediaRecorder.start();
            isRecording=true;

        }
        catch(Throwable x)
        {
             Log.w("Audio", "Error reading voice audio", x);
        }
    }

    /**
     * Called from outside of the thread in order to stop the recording/playback loop
     */
    public void close()
    {
        //stopped = false;
        try{
            if (isRecording)
            {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                isRecording = false;
            }
        }catch (Exception e){
            }


    }

}