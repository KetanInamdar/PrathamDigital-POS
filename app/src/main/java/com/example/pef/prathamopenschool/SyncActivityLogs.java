package com.example.pef.prathamopenschool;

import android.content.Context;
import android.telephony.TelephonyManager;

public class SyncActivityLogs extends DBHelper {

    Utility Util;
    final String ERRORTABLENAME = "Logs";
    Context c;

    SyncActivityLogs(Context c) {
        super(c);
        this.c = c;
        database = getWritableDatabase();
        Util = new Utility();
    }

    public void addToDB(String method, Exception e, String type) {
        String deviceId = ((TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        contentValues.put("CurrentDateTime", Util.GetCurrentDateTime());
        contentValues.put("ExceptionMsg", e.getMessage());
        contentValues.put("ExceptionStackTrace", e.getStackTrace().toString());
        contentValues.put("MethodName", method);
        contentValues.put("Type", type);
        contentValues.put("GroupId", LogInPage.groupId);
        contentValues.put("DeviceId", deviceId);
        contentValues.put("LogDetail", "StudentLog");
        database.insert(ERRORTABLENAME, null, contentValues);

    }

    public void addLog(String method, String type) {
        String deviceId = ((TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        Logs logs = new Logs();
        logs.currentDateTime = Util.GetCurrentDateTime();
        logs.errorType = type;
        logs.exceptionMessage = "";
        logs.exceptionStackTrace = "";
        logs.methodName = method;
        logs.groupId = LogInPage.groupId;

        contentValues.put("CurrentDateTime", logs.currentDateTime);
        contentValues.put("ExceptionMsg", logs.exceptionMessage);
        contentValues.put("ExceptionStackTrace", logs.exceptionStackTrace);
        contentValues.put("MethodName", logs.methodName);
        contentValues.put("Type", logs.errorType);
        contentValues.put("GroupId", LogInPage.groupId);
        contentValues.put("DeviceId", deviceId);
        contentValues.put("LogDetail", "StudentLog");
        database.insert(ERRORTABLENAME, null, contentValues);


    }

    public void addLog(String method, String type, String msg) {
        String deviceId = ((TelephonyManager) c
                .getSystemService(Context.TELEPHONY_SERVICE))
                .getDeviceId();
        Logs logs = new Logs();
        logs.currentDateTime = Util.GetCurrentDateTime();
        logs.errorType = type;
        logs.exceptionMessage = msg;
        logs.exceptionStackTrace = "";
        logs.methodName = method;
        logs.groupId = LogInPage.groupId;

        contentValues.put("CurrentDateTime", logs.currentDateTime);
        contentValues.put("ExceptionMsg", logs.exceptionMessage);
        contentValues.put("ExceptionStackTrace", logs.exceptionStackTrace);
        contentValues.put("MethodName", logs.methodName);
        contentValues.put("Type", logs.errorType);
        contentValues.put("GroupId", LogInPage.groupId);
        contentValues.put("DeviceId", deviceId);
        contentValues.put("LogDetail", "StudentLog");
        database.insert(ERRORTABLENAME, null, contentValues);


    }
}
