package com.example.pef.prathamopenschool;

import android.content.Context;
import android.widget.Toast;

public class UserSessionDBHelper extends DBHelper {
    final String TABLENAME = "Session";
    final String ERRORTABLENAME = "Logs";
    public UserSessionDBHelper(Context context) {
        super(context); database = getWritableDatabase();
    }

    public boolean Add(UserSession sessionObj) {
        try {

            _PopulateContentValues(sessionObj);

            long resultCount = database.insert(TABLENAME, null, contentValues);
            Toast.makeText(c, "resultCount from session" + resultCount,
                    Toast.LENGTH_SHORT).show();
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"Add-UserSession");
            return false;
        }
    }

    private void _PopulateContentValues(UserSession session) {
        try {
            contentValues.put("SessionID",session.UserSessionID);
            contentValues.put("UserId",session.UserID);
        } catch (Exception ex) {
        }
    }
    private void _PopulateLogValues(Exception ex,String method) {
        Logs logs=new Logs();
        logs.currentDateTime=Util.GetCurrentDateTime();
        logs.errorType="Error";
        logs.exceptionMessage=ex.getMessage();
        logs.exceptionStackTrace=ex.getStackTrace().toString();
        logs.methodName=method;
        logs.groupId=LogInPage.groupId;
        logs.deviceId=LogInPage.deviceId;

        contentValues.put("CurrentDateTime",logs.currentDateTime);
        contentValues.put("ExceptionMsg",logs.exceptionMessage);
        contentValues.put("ExceptionStackTrace",logs.exceptionStackTrace);
        contentValues.put("MethodName",logs.methodName);
        contentValues.put("Type",logs.errorType);
        contentValues.put("GroupId", logs.groupId);
        contentValues.put("DeviceId",logs.deviceId);
        database.insert(ERRORTABLENAME, null, contentValues);
        database.close();
        BackupDatabase.backup(c);
    }
}
