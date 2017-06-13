package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.net.Uri;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;

public class JSInterface extends Activity {
    static Context mContext;
    String path ,mediaPath;
    //    TextToSpeech tts;
    Context context;
    static Context staticContext;
    //    DBHelper db;
//    Utility Util;
//    Audio recordAudio;
    public String SessionId;
    public static int groupId = 0;
    public int UserId = 1;
    MediaRecorder myAudioRecorder;
    public static MediaPlayer mp, mp2 = null;
    int presentStudents[];
    //    public static videoTracking videoTracking;
//    ShowAlert showAlert;
    WebView w;
    RadioGroup radioGroup;
    public static int flag = 0, VideoFlag = 0;
    static Boolean MediaFlag = false, pdfFlag = false, audioFlag = false, trailerFlag = false;

    //   static org.apache.log4j.Logger log = WriteLog.getLogger("JSInterface");

    JSInterface(Context c, WebView w) {
        mContext = c;
        context = c;
        staticContext = c;
        //       db = new DBHelper(c);
        //       Util = new Utility();
//        this.presentStudents = present;
        this.SessionId = SessionId;
        mp = new MediaPlayer();
//        showAlert = new ShowAlert();
        this.w = w;
        VideoFlag = 0;
    }

    @JavascriptInterface
    public String getPath(String gameFolder){
        mediaPath=splashScreenVideo.fpath+"Media/"+gameFolder+"/";
        return mediaPath;
/*        w.post(new Runnable() {
            public void run() {
                String jsString = "javascript:Utils.setPath('" + mediaPath + "')";
                w.loadUrl(jsString);
            }
        });
*/    }

    @JavascriptInterface
    public void getNavPath(String resFolder){
        path=splashScreenVideo.fpath+resFolder+"/";
        w.post(new Runnable() {
            public void run() {
                String jsString = "javascript:Utils.setNavPath('" + path + "')";
                w.loadUrl(jsString);
            }
        });
    }

    @JavascriptInterface
    public void getGamePath(String resFolder){
        path=splashScreenVideo.fpath+resFolder+"/";
        w.post(new Runnable() {
            public void run() {
                String jsString = "javascript:Utils.setGamePath('" + path + "')";
                w.loadUrl(jsString);
            }
        });
    }


    @JavascriptInterface
    public String getMediaPath() {
        VideoFlag = 1;
        String path;
        path = splashScreenVideo.fpath;
        return path;
    }

    /*********************************recording code******************/
    /*@JavascriptInterface
    public void startRecording() throws IOException {
        Toast.makeText(mContext,"Entered in startRecording function ",Toast.LENGTH_LONG).show();
        myAudioRecorder = new MediaRecorder();

        myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.AAC_ADTS);
        myAudioRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        myAudioRecorder.setOutputFile("/storage/sdcard1/storyGame");

        myAudioRecorder.prepare();
        myAudioRecorder.start();

    }*/
    public void showDialogue(String msg) {
        Activity act = (Activity) mContext;
        AlertDialog alertDialog = new AlertDialog.Builder(act).create();
        alertDialog.setTitle("Alert");
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
        alertDialog.show();
    }

/*
    JavascriptInterface
    public void whoseGuest() {
        Activity act = (Activity) mContext;
        StatusDBHelper statusDBHelper = new StatusDBHelper(context);

        if (statusDBHelper.getValue("pullFlag").equals("0"))
            showDialogue("Data is not available. Please first LogIn as admin user.");
        else if (statusDBHelper.getValue("group1").equals("0") && statusDBHelper.getValue("group2").equals("0"))
            showDialogue("Assign groups to this tablet. Please LogIn as admin user.");
        else {
            AlertDialog.Builder alert = new AlertDialog.Builder(act);
            if (splashScreenVideo.appname.contains("Marathi")) {
                alert.setTitle(" तुम्ही  कोणत्या group चे guest आहात ?  ");
            } else if (splashScreenVideo.appname.contains("Hindi")) {
                alert.setTitle(" आप कौनसे group के guest हो ?  ");
            }

            String group1 = (statusDBHelper.getValue("group1"));
            String group2 = (statusDBHelper.getValue("group2"));

            GroupDBHelper groupDBHelper = new GroupDBHelper(context);
            String groupName1 = groupDBHelper.getGroupById(group1);
            String groupName2 = groupDBHelper.getGroupById(group2);

            final LinearLayout layout = new LinearLayout(act, null);
            layout.setOrientation(LinearLayout.VERTICAL);


            TableLayout.LayoutParams lp = new TableLayout.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);


            radioGroup = new RadioGroup(act);
            RadioButton radioButton = new RadioButton(act);
            radioButton.setId(0);
            radioButton.setTag(group1);
            radioButton.setText(groupName1);
            radioButton.setTextSize(25);
            radioGroup.addView(radioButton, lp);

            RadioButton radioButton1 = new RadioButton(act);
            radioButton1.setId(Integer.parseInt("1"));
            radioButton.setTag(group2);
            radioButton1.setText(groupName2);
            radioButton1.setTextSize(25);
            radioGroup.addView(radioButton1, lp);

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
                    int count = 0;

                    int selectedId = radioGroup.getCheckedRadioButtonId();
                    if (selectedId != -1) {
                        RadioButton radioButton = (RadioButton) layout.findViewById(selectedId);
                        groupId = radioButton.getId();

                        //Toast.makeText(context,"groupid: "+groupId,Toast.LENGTH_LONG).show();
                    } else {
                        doNotClose = true;
                        Toast toast = Toast.makeText(context, "Please select group.", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.CENTER, 0, 0);
                        toast.show();
                    }
                    if (!doNotClose) {
                        dialog.dismiss();

                        //got groupId of logIn group so check if showTrailer date is present or not?
                        StatusDBHelper statusDBHelper1 = new StatusDBHelper(mContext);
                        count = statusDBHelper1.getTrailerCount(String.valueOf(groupId));

                        if (count == 0) {
                            //if condition satisfied that means this grp has logged In for first time so play trailer
                            //and generate date for future trailer display
                            Random r = new Random();
                            int duration = r.nextInt(18 - 12) + 12;
                            //Toast.makeText(mContext,"duartion"+duration,Toast.LENGTH_LONG).show();

                            //String dayAfterDate = new Utility().GetCurrentDate();
                            //Toast.makeText(mContext,"date"+dayAfterDate,Toast.LENGTH_LONG).show();

                            File file = new File(splashScreenVideo.fpath + "trailer.mp4");

                            if (file.exists()) {
                                Uri path = Uri.fromFile(file);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setDataAndType(path, "video/mp4");

                                try {
                                    trailerFlag = true;

                                    StatusDBHelper statusDBHelper2 = new StatusDBHelper(mContext);
                                    boolean res = statusDBHelper2.updateTrailerCount(duration, String.valueOf(groupId));
                                    BackupDatabase.backup(mContext);
                                    ((Activity) mContext).startActivityForResult(intent, 1);
                                } catch (Exception e) {
                                    e.getStackTrace();
                                    showAlert.showDialogue(mContext, "No Application Available to View Video");
                                    SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
                                    syncActivityLogs.addToDB("whoseGuest-JSInterface", e, "Error");
                                }
                            } else {
                                showAlert.showDialogue(mContext, "Video file is not available.");
                            }
                        } else {
                            count--;
                            StatusDBHelper statusDBHelper2 = new StatusDBHelper(mContext);
                            boolean res = statusDBHelper2.updateTrailerCount(count, String.valueOf(groupId));
                            BackupDatabase.backup(mContext);

                            w.post(new Runnable() {
                                public void run() {
                                    w.loadUrl("file:///android_asset/www/HomePage.html");
                                }
                            });
                        }


                    }
                }
            });
        }
    }
*/

    @JavascriptInterface
    public void audioPlayerLoop(String filename) {
        String path;
        if (filename.charAt(0) == '/') {
            path = filename;//check for recording game and then change
        } else {
            //path="/storage/sdcard1/.prathamMarathi/"+filename;
            path = splashScreenVideo.fpath + filename;
        }
        mp2 = new MediaPlayer();

        try {
            mp2.setDataSource(path);
            mp2.prepare();
            mp2.setLooping(true);
            mp2.start();
        } catch (Exception e) {
/*            showAlert.showDialogue(mContext, "Problem occurred in audio player. Please contact your administrator.");
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
            syncActivityLogs.addToDB("audioPlayer-JSInterface", e, "Error");
            BackupDatabase.backup(mContext);
*/            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void stopAudioPlayerloop() {
        try {
            if (mp2 != null) {
                mp2.stop();
                mp2.reset();
                //mp = null;
            }
        } catch (Exception e) {
/*
            showAlert.showDialogue(mContext, "Problem occurred in audio player. Please contact your administrator.");
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
            syncActivityLogs.addToDB("stopAudioPlayer-JSInterface", e, "Error");
            BackupDatabase.backup(mContext);
*/
        }
    }

/*
    @JavascriptInterface
    public void startRecording(String recName) {
        try {
            recordAudio = new Audio(recName);
            recordAudio.start();
        }
        catch (Exception e){
            log.error("Exception occurred at : "+e.getMessage());
            showAlert.showDialogue(mContext,"Problem occurred in audio recorder. Please contact your administrator.");
            SyncActivityLogs syncActivityLogs=new SyncActivityLogs(mContext);
            syncActivityLogs.addToDB("startRecording-JSInterface",e,"Error");
            BackupDatabase.backup(mContext);
        }
    }
*/

/*
    @JavascriptInterface
    public void sendBackTojavascript() {
        w.post(new Runnable() {
            public void run() {
                String str1 = splashScreenVideo.jsonstrOfNewVideos;
                String jsString = "javascript:loadNewJson('" + str1 + "')";
                w.loadUrl(jsString);
            }
        });

    }

    @JavascriptInterface
    public void getNewVideos() {
        final String str1 = splashScreenVideo.newVideos;
        w.post(new Runnable() {
            public void run() {
                String jsString = "javascript:showNewlyAddedVideos('" + str1 + "')";
                w.loadUrl(jsString);
            }
        });

    }
*/

    @JavascriptInterface
    public void showMsgInAndroid(String str) {
        Toast.makeText(mContext, "length:" + str, Toast.LENGTH_LONG).show();
    }

/*
    @JavascriptInterface
    public void stopRecording() {
        audioFlag = false;
        try {
            recordAudio.close();
        } catch (Exception e) {
            log.error("Exception occurred at : " + e.getMessage());
            showAlert.showDialogue(mContext, "Problem occurred in audio recorder. Please contact your administrator.");
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
            syncActivityLogs.addToDB("stopRecording-JSInterface", e, "Error");
            BackupDatabase.backup(mContext);
        }
    }
*/

/*
    @JavascriptInterface
    public void showPdf(String filename, String resId) {
        try {
            File file = new File(splashScreenVideo.fpathMedia+filename);

            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(path, "application/pdf");
                try {
                    pdfFlag = true;
                    videoTracking = new videoTracking(mContext, presentStudents, SessionId, resId);
                    videoTracking.calculateStartTime();
                    ((Activity) mContext).startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    log.error("Exception occurred at : " + e.getMessage());
                    showAlert.showDialogue(mContext, "No Application Available to View PDF");
                    SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
                    syncActivityLogs.addToDB("showPdf-JSInterface", e, "Error");
                }
            } else {
                showAlert.showDialogue(mContext, "PDF file is not available.");
            }
        } catch (Exception e) {
            log.error("Exception occurred at : " + e.getMessage());
        }
    }

    @JavascriptInterface
    public void audioPlayerForStory(String filename, String storyName) {
        try {

            String path = "";
            audioFlag = true;
            flag=0;
            String mp3File;


            //  path="/storage/sdcard1/.PrathamHindi/salana baal-katai divas/";
            try {
                if(storyName != null) {
                    mp3File = "storyGame/Raw/" + storyName + "/" + filename;
                }
                else{
                    mp3File = "/storage/sdcard0/recordings"+ filename;
                }
                if(filename.charAt(0)=='/'){
                    path="/storage/sdcard0/recordings"+filename;//check for recording game and then change
                    mp.setDataSource(path);
                }
                else{


                    path = splashScreenVideo.fpathMedia+mp3File;

                    mp.setDataSource(path);
                    //mp.setDataSource(this,Uri.parse(path));

                }
                if(mp.isPlaying())
                    mp.stop();

                mp.prepare();
                mp.start();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        audioFlag = false;
                        try {
                            //webViewActivity.loadUrl("javascript:temp()");
                            w.post(new Runnable() {
                                @Override
                                public void run() {

                                    w.loadUrl("javascript:temp()");
                                }
                            });
                        } catch (Exception e) {
                            log.error("Exception occurred at : " + e.getMessage());
                            e.printStackTrace();
                        }

                    }
                });

            } catch (Exception e) {
                log.error("Exception occurred at : " + e.getMessage());
                showAlert.showDialogue(mContext, "Problem occurred in audio player. Please contact your administrator.");

                SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
                syncActivityLogs.addToDB("audioPlayerForStory-JSInterface", e, "Error");
                BackupDatabase.backup(mContext);

                e.printStackTrace();
            }
        } catch (Exception e) {
            log.error("Exception occurred at : " + e.getMessage());
        }
    }
*/


    @JavascriptInterface
    public void audioPlayer(String filename) {
        try {
            String path;
            if (filename.charAt(0) == '/') {
                path = filename;//check for recording game and then change
            } else {
                //path="/storage/sdcard1/.prathamMarathi/"+filename;
                path = splashScreenVideo.fpath + filename;
            }
            mp = new MediaPlayer();

            try {
                mp.setDataSource(path);
                mp.prepare();
                mp.start();

                mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        mp.stop();
                    }
                });
            } catch (Exception e) {
/*
                log.error("Exception occurred at : " + e.getMessage());
                showAlert.showDialogue(mContext, "Problem occurred in audio player. Please contact your administrator.");
                SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
                syncActivityLogs.addToDB("audioPlayer-JSInterface", e, "Error");
                BackupDatabase.backup(mContext);
*/
                e.printStackTrace();
            }
        } catch (Exception e) {
/*
            log.error("Exception occurred at : " + e.getMessage());
*/
        }
    }

    @JavascriptInterface
    public void stopAudioPlayer() {
        try {
            if (mp != null) {
                mp.stop();
                mp.reset();
                //mp = null;
            }
        } catch (Exception e) {
/*
            log.error("Exception occurred at : " + e.getMessage());
            showAlert.showDialogue(mContext, "Problem occurred in audio player. Please contact your administrator.");
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
            syncActivityLogs.addToDB("stopAudioPlayer-JSInterface", e, "Error");
            BackupDatabase.backup(mContext);
*/
        }
    }

    @JavascriptInterface
    public void showVideo(String filename, String resId) {

        try {
            File file = new File(splashScreenVideo.fpath+"Media/"+ filename);

            if (file.exists()) {
                Uri path = Uri.fromFile(file);
                Intent intent = new Intent(mContext, PlayVideo.class);
                intent.putExtra("path", path.toString());
//                videoTracking = new videoTracking(mContext, presentStudents, SessionId, resId);
//                videoTracking.calculateStartTime();
                MediaFlag = true;
                ((Activity) mContext).startActivityForResult(intent, 1);
            } else {
//                showAlert.showDialogue(mContext, "Video file is not available.");
            }
        } catch (Exception e) {
//            log.error("Exception occurred at : " + e.getMessage());
        }

    }

/*
    @JavascriptInterface
    public void showNewVideos(String filename, String resId) {

        try {
            File file = new File(AddNewVideo.sdCardPath + filename);

            if (file.exists()) {
                Uri path = Uri.fromFile(file);

                Intent intent = new Intent(mContext, VideoPlay.class);
                intent.putExtra("path", path.toString());

            */
/*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(path, "video/mp4");*//*


                try {
                    videoTracking = new videoTracking(mContext, presentStudents, SessionId, resId);
                    videoTracking.calculateStartTime();
                    MediaFlag = true;
                    ((Activity) mContext).startActivityForResult(intent, 1);
                } catch (Exception e) {
                    log.error("Exception occurred at : " + e.getMessage());
                    e.getStackTrace();
                    showAlert.showDialogue(mContext, "No Application Available to View Video");
                    SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
                    syncActivityLogs.addToDB("showNewVideos-JSInterface", e, "Error");
                }
            } else {
                showAlert.showDialogue(mContext, "Video file is not available.");
            }
        } catch (Exception e) {
            log.error("Exception occurred at : " + e.getMessage());
        }


    }

    public static void returnFunction() {
        try {
            pdfFlag = false;
            MediaFlag = false;
            videoTracking.calculateEndTime();
        } catch (Exception e) {
            log.error("Exception occurred at : " + e.getMessage());
            e.printStackTrace();
        }
    }

    @JavascriptInterface
    public void addScore(String resId, int questionId, int scorefromGame, int totalMarks, int level, String startTime) {
        boolean _wasSuccessful = false;
        //put try catch block for error handling
        try {

            if (resId.equals(null) || startTime.equals("undefined")) {
                SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
                if (resId.equals(null))
                    syncActivityLogs.addLog("addScore-JSInterface", "Error", "resource id is null");
                else if (startTime.equals("undefined"))
                    syncActivityLogs.addLog("addScore-JSInterface", "Error", "startTime is undefined");
                BackupDatabase.backup(mContext);
            } else {
                StatusDBHelper statusDBHelper = new StatusDBHelper(context);
                ScoreDBHelper scoreDBHelper = new ScoreDBHelper(context);
                Score score = new Score();
                score.SessionID = SessionId;
//                score.PlayerID =0;// playerId as 0 for guest users
                score.ResourceID = resId;
                score.QuestionId = questionId;
                score.ScoredMarks = scorefromGame;
                score.TotalMarks = totalMarks;
                score.StartTime = startTime;
                score.GroupID = splashScreenVideo.groupId;
                String deviceId = statusDBHelper.getValue("deviceId");
                if (deviceId.equals("") || deviceId.contains("111111111111111")) {
                    deviceId = Build.SERIAL;
                    if (deviceId.equals("") || deviceId == null)
                        showAlert.showDialogue(mContext, "Problem occurred in tracking deviceID.Immediately contacct your administrator.");
                    else {
                        score.DeviceID = deviceId;
                        score.EndTime = Util.GetCurrentDateTime();
                        score.Level = level;
                        _wasSuccessful = scoreDBHelper.Add(score);

                        if (!_wasSuccessful) {
                            showAlert.showDialogue(mContext, "Problem occurred in score tracking functionality. Please contact your administrator.");
                        }
                    }
                } else {
                    score.DeviceID = deviceId;
                    score.EndTime = Util.GetCurrentDateTime();
                    score.Level = level;
                    _wasSuccessful = scoreDBHelper.Add(score);

                    if (!_wasSuccessful) {
                        showAlert.showDialogue(mContext, "Problem occurred in score tracking functionality. Please contact your administrator.");
                    }
                }
                //score.DeviceID = StartingActivity.deviceId;

                */
/*if(FirstActivity.KEY=="Guest"){



                }
                else{
                    for(int i=0;i<presentStudents.length;i++){

                        ScoreDBHelper scoreDBHelper = new ScoreDBHelper(context);
                        Score score = new Score();
                        score.SessionID = SessionId;
                        score.PlayerID =presentStudents[i];
                        score.ResourceID = resId;
                        score.QuestionId=questionId;
                        score.ScoredMarks = scorefromGame;
                        score.TotalMarks=totalMarks;
                        score.GroupID = SelectStudent.groupId;
                        score.DeviceID = StartingActivity.deviceId;
                        score.StartTime=startTime;
                        score.EndTime=Util.GetCurrentDateTime();
                        score.Level=level;
                        _wasSuccessful = scoreDBHelper.Add(score);

                        if(!_wasSuccessful){
                            showAlert.showDialogue(mContext,"Problem occurred in score tracking functionality. Please contact your administrator.");
                        }
                    }
                }*//*

            }
            BackupDatabase.backup(mContext);

        } catch (Exception e) {
            log.error("Exception occurred at : " + e.getMessage());
            showAlert.showDialogue(mContext, "Problem occurred in score tracking functionality. Please contact your administrator.");
            SyncActivityLogs syncActivityLogs = new SyncActivityLogs(mContext);
            syncActivityLogs.addToDB("addScore-JSInterface", e, "Error");
            BackupDatabase.backup(mContext);
        }

    }
*/

    @JavascriptInterface
    public String insertDetails(String name, String password) {
        return "";
    }

    /*
        @JavascriptInterface
        public void exitApplication() {
    
            */
/*if(FirstActivity.KEY=="Guest"){
            Intent intent=new Intent(mContext,splashScreenVideo.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            // this.finish();
            mContext.startActivity(intent);
        }
        else{*//*

        Intent intent = new Intent(mContext, StartingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // this.finish();
        mContext.startActivity(intent);
        //}

    }
*/
/*
    @JavascriptInterface
    public void playTts(String theWordWasAndYouSaid,String recordingPath) {
        tts = new TextToSp(mContext);
        tts.ttsFunction(theWordWasAndYouSaid);
        while (tts.t1.isSpeaking()){

        }
        playAudio(recordingPath);
    }
*/



}