package com.example.pef.prathamopenschool;

import android.content.Context;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by PEF-2 on 23/06/2016.
 */
public class AttendanceDBHelper extends DBHelper {

    Context c;
    public static String TABLENAME = "Attendance";
    final String ERRORTABLENAME = "Logs";

    AttendanceDBHelper(Context c){
        super(c);
        this.c=c;
        database = this.getWritableDatabase();
    }

    public boolean Add(Attendance attendance) {
        try {
            _PopulateContentValues(attendance);
            long resultCount = database.insert(TABLENAME, null, contentValues);
            database.close();
            if(resultCount==-1)
                return false;
            else
                return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"Add-attendance");
            return false;
        }
    }


    public JSONArray GetAll(){
        JSONArray jsonArray = null;

        try {
            Cursor cursor = database.rawQuery("select * from " + TABLENAME + "", null);
            cursor.moveToFirst();
            jsonArray = new JSONArray();
            while (cursor.isAfterLast()==false){
                JSONObject obj = new JSONObject();
                obj.put("SessionID",cursor.getString(cursor.getColumnIndex("SessionID")));
                obj.put("PresentStudentIds",cursor.getString(cursor.getColumnIndex("PresentStudentIds")));
                obj.put("GroupID",cursor.getString(cursor.getColumnIndex("GroupID")));
                jsonArray.put(obj);
                cursor.moveToNext();
            }
        } catch (Exception ex) {
            _PopulateLogValues(ex,"GetAll-Attendance");
            return null;
        }
        return jsonArray;
    }

    public boolean DeleteAll() {
        try {
            // database = getWritableDatabase();
            long resultCount = database.delete(TABLENAME, null, null);
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"DeleteAll-Score");
            return false;
        }
    }

    private void _PopulateContentValues(Attendance attendance) {
        contentValues.put("SessionID", attendance.SessionID.toString());
        contentValues.put("GroupID",attendance.GroupID);
        contentValues.put("PresentStudentIds",attendance.PresentStudentIds);
    }

    private void _PopulateLogValues(Exception ex,String method) {
        Logs logs=new Logs();
        logs.currentDateTime=Util.GetCurrentDateTime();
        logs.errorType="Error";
        logs.exceptionMessage=ex.getMessage();
        logs.exceptionStackTrace=ex.getStackTrace().toString();
        logs.methodName=method;
        logs.groupId=LogInPage.groupId;
        logs.deviceId=StartingActivity.deviceId;

        contentValues.put("CurrentDateTime",logs.currentDateTime);
        contentValues.put("ExceptionMsg",logs.exceptionMessage);
        contentValues.put("ExceptionStackTrace",logs.exceptionStackTrace);
        contentValues.put("MethodName",logs.methodName);
        contentValues.put("Type",logs.errorType);
        contentValues.put("GroupId",logs.groupId);
        contentValues.put("DeviceId",logs.deviceId);
        contentValues.put("LogDetail","StudentLog");
        database.insert(ERRORTABLENAME, null, contentValues);
        database.close();
        BackupDatabase.backup(c);
    }

}
