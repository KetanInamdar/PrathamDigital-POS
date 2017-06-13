package com.example.pef.prathamopenschool;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class ScoreDBHelper extends DBHelper {
    final String TABLENAME = "Scores";
    final String ERRORTABLENAME = "Logs";

    Context contexter;

    public ScoreDBHelper(Context context) {
        super(context);
        contexter = context;
        database = this.getWritableDatabase();
    }

    public boolean Add(Score score) {
        try {
            _PopulateContentValues(score);
            long resultCount = database.insert(TABLENAME, null, contentValues);
            //Toast.makeText(grpContext, "resultCount from add score" + resultCount,
            //      Toast.LENGTH_SHORT).show();
            database.close();
            if (resultCount == -1)
                return false;
            else
                return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "Add-Score");
            return false;
        }
    }

    public boolean DeleteAll() {
        try {
            // database = getWritableDatabase();
            long resultCount = database.delete(TABLENAME, null, null);
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "DeleteAll-Score");
            return false;
        }
    }

    public boolean Update(Score score) {
        return false;
    }

    public boolean Delete(int scoreID) {
        return true;
    }

    public Score Get(int scoreID) {
        Score score = new Score();
        return score;
    }

    public List<Score> GetAll() {
        try {
            Cursor cursor = database.rawQuery("select * from " + TABLENAME + "", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetAll-Score");
            return null;
        }
    }

    private void _PopulateContentValues(Score score) {

        contentValues.put("SessionID", score.SessionID.toString());
        //contentValues.put("PlayerID", score.PlayerID);
        contentValues.put("GroupID", score.GroupID);
        contentValues.put("DeviceID", score.DeviceID);
        contentValues.put("QuestionID", score.QuestionId);
        contentValues.put("ResourceID", score.ResourceID);
        contentValues.put("ScoredMarks", score.ScoredMarks);
        contentValues.put("TotalMarks", score.TotalMarks);
        contentValues.put("StartDateTime", score.StartTime);
        contentValues.put("EndDateTime", score.EndTime);
        contentValues.put("Level", score.Level);
    }

    private List<Score> _PopulateListFromCursor(Cursor cursor) {
        try {
            List<Score> scoreList = new ArrayList<Score>();
            Score score;
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                score = new Score();
                //score.PlayerID=cursor.getInt(cursor.getColumnIndex("PlayerID"));
                score.SessionID = cursor.getString(cursor.getColumnIndex("SessionID"));
                score.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                score.DeviceID = cursor.getString(cursor.getColumnIndex("DeviceID"));
                score.ResourceID = cursor.getString(cursor.getColumnIndex("ResourceID"));
                score.QuestionId = cursor.getInt(cursor.getColumnIndex("QuestionID"));
                score.ScoredMarks = cursor.getInt(cursor.getColumnIndex("ScoredMarks"));
                score.TotalMarks = cursor.getInt(cursor.getColumnIndex("TotalMarks"));
                score.Level = cursor.getInt(cursor.getColumnIndex("Level"));
                score.StartTime = cursor.getString(cursor.getColumnIndex("StartDateTime"));
                score.EndTime = cursor.getString(cursor.getColumnIndex("EndDateTime"));

                scoreList.add(score);
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            return scoreList;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "PopulateListFromCursor-Score");
            return null;
        }
    }

    private void _PopulateLogValues(Exception ex, String method) {
        Logs logs = new Logs();
        logs.currentDateTime = Util.GetCurrentDateTime();
        logs.errorType = "Error";
        logs.exceptionMessage = ex.getMessage();
        logs.exceptionStackTrace = ex.getStackTrace().toString();
        logs.methodName = method;
        logs.groupId = LogInPage.groupId;
        logs.deviceId = StartingActivity.deviceId;

        contentValues.put("CurrentDateTime", logs.currentDateTime);
        contentValues.put("ExceptionMsg", logs.exceptionMessage);
        contentValues.put("ExceptionStackTrace", logs.exceptionStackTrace);
        contentValues.put("MethodName", logs.methodName);
        contentValues.put("Type", logs.errorType);
        contentValues.put("GroupId", logs.groupId);
        contentValues.put("DeviceId", logs.deviceId);
        contentValues.put("LogDetail", "StudentLog");
        database.insert(ERRORTABLENAME, null, contentValues);
        database.close();
        BackupDatabase.backup(contexter);
    }
}
