package com.example.pef.prathamopenschool;

import android.content.Context;
import android.database.Cursor;

/**
 * Created by PEF-2 on 30/05/2016.
 */
public class StatusDBHelper extends DBHelper {
    Context c;
    final String TABLENAME = "Status";
    final String ERRORTABLENAME = "Logs";
    public static int newGroupCount = 1;

    StatusDBHelper(Context c) {
        super(c);
        this.c = c;
    }

    public boolean Update(String keyName, String value) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("UPDATE " + TABLENAME + " SET value = ? where key = ? ", new String[]{value, keyName});
            //long resultCount = database.update(TABLENAME, contentValues, keyName +" = ?", new String[]{value});
            cursor.moveToFirst();

            if (cursor.getCount() >= 0)
                return true;
            else return false;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "Update-StatusDBHelper");
            return false;
        }
    }

    public String getValue(String key) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("Select value from " + TABLENAME + " where key = ?", new String[]{key});
            //long resultCount = database.update(TABLENAME, contentValues, keyName +" = ?", new String[]{value});
            cursor.moveToFirst();
            return cursor.getString(0);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "getValue-StatusDBHelper");
            return null;
        }
    }

    public String[] getGroupIDs() {     /*function for getting All Group IDs */
        String groupIDs[],key="0";
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("Select value from " + TABLENAME + " where key != ?", new String[]{key});
            cursor.moveToFirst();
            groupIDs=new String[cursor.getCount()];
            for (int i=0;i<cursor.getCount();i++){
                groupIDs[i]=new String(cursor.getString(i));
            }
            return groupIDs;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "getGroupIDs-StatusDBHelper");
            return null;
        }
    }

    public boolean checkGroupIdExists(String grpId) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("Select * from " + TABLENAME + " where value = ?", new String[]{String.valueOf(grpId)});
            //long resultCount = database.update(TABLENAME, contentValues, keyName +" = ?", new String[]{value});
            cursor.moveToFirst();
            if (cursor.getCount() > 0) {
                return true;
            } else return false;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "checkGroupIdExists-StatusDBHelper");
            return false;
        }
    }

    public int getTrailerCount(String grpId) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("Select trailerCount from " + TABLENAME + " where value = ?", new String[]{String.valueOf(grpId)});
            //long resultCount = database.update(TABLENAME, contentValues, keyName +" = ?", new String[]{value});
            cursor.moveToFirst();

            //return cursor.getString(0);
            return cursor.getInt(0);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "getTrailerDate-StatusDBHelper");
            return 0;
        }
    }

    public int getOldTrailerCount(String grpId) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("Select oldTrailerCount from " + TABLENAME + " where value = ?", new String[]{String.valueOf(grpId)});
            //long resultCount = database.update(TABLENAME, contentValues, keyName +" = ?", new String[]{value});
            cursor.moveToFirst();

            //return cursor.getString(0);
            return cursor.getInt(0);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "getOldTrailerCount-StatusDBHelper");
            return 0;
        }
    }

    public boolean updateTrailerCount(int count, String grpId) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("Update " + TABLENAME + " set trailerCount = ? where value = ?", new String[]{String.valueOf(count), String.valueOf(grpId)});
            //long resultCount = database.update(TABLENAME, contentValues, keyName +" = ?", new String[]{value});
            cursor.moveToFirst();
            if (cursor.getCount() >= 0)
                return true;
            else return false;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "updateTrailerDate-StatusDBHelper");
            return false;
        }
    }

    public boolean updateOldTrailerCount(int count, String grpId) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("Update " + TABLENAME + " set oldTrailerCount = ? where value = ?", new String[]{String.valueOf(count), String.valueOf(grpId)});
            //long resultCount = database.update(TABLENAME, contentValues, keyName +" = ?", new String[]{value});
            cursor.moveToFirst();
            if (cursor.getCount() >= 0)
                return true;
            else return false;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "updateOldTrailerCount-StatusDBHelper");
            return false;
        }
    }

    public boolean addToStatusTable(String key, String value, int count, int oldTrailerCount) {
        try {
            database = getWritableDatabase();
            // Toast.makeText(grpContext,"newGroupCount"+newGroupCount,Toast.LENGTH_LONG).show();
            Cursor cursor = database.rawQuery("Insert into " + TABLENAME + " values(?,?,?,?)", new String[]{key, String.valueOf(value), String.valueOf(count), String.valueOf(oldTrailerCount)});
            cursor.moveToFirst();
            if (cursor.getCount() >= 0) {

                return true;
            } else return false;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "addToStatusTable-StatusDBHelper");
            return false;
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
        BackupDatabase.backup(c);
    }
}
