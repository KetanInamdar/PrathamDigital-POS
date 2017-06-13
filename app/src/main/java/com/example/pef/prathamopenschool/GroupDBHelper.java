package com.example.pef.prathamopenschool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;


public class GroupDBHelper extends DBHelper {
    final String TABLENAME = "Groups";
    final String ERRORTABLENAME = "Logs";
    Context c;

    public GroupDBHelper(Context context) {
        super(context);
        c=context;
        database = getWritableDatabase();

    }

    public void insertData(Group obj) {

        database = getWritableDatabase();

        contentValues.put("GroupID", obj.GroupID);
        contentValues.put("GroupCode", obj.GroupCode);
        contentValues.put("GroupName", obj.GroupName);
        contentValues.put("UnitNumber", obj.UnitNumber);
        contentValues.put("DeviceID", obj.DeviceID);
        contentValues.put("Responsible", obj.Responsible);
        contentValues.put("ResponsibleMobile", obj.ResponsibleMobile);
        contentValues.put("VillageID", obj.VillageID);
        contentValues.put("ProgramId", obj.ProgramID);
        database.insert("Groups", null, contentValues);

        database.close();

    }

    public boolean Add(Group group,SQLiteDatabase database1) {
        try {
            _PopulateContentValues(group);
            long resultCount = database1.insert(TABLENAME, null, contentValues);
            database1.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"Add-group");

            return false;
        }
    }

    public boolean Update(Group group) {
        try {
            database = getWritableDatabase();
            _PopulateContentValues(group);
            long resultCount = database.update(TABLENAME, contentValues, "GroupID = ?", new String[]{((String) group.GroupID).toString()});
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"Update-group");
            return false;
        }
    }

    public boolean Delete(String groupID) {
        try {
            database = getWritableDatabase();
            long resultCount = database.delete(TABLENAME, "GroupID = ?", new String[]{groupID.toString()});
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"Delete-group");
            return false;
        }
    }

    public boolean DeleteAll() {
        try {
            database = getWritableDatabase();
            long resultCount = database.delete(TABLENAME, null, null);
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"DeleteAll-group");
            return false;
        }
    }

    public Group Get(String groupID) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from " + TABLENAME + " where GroupID='" + groupID + "'", null);
            return _PopulateObjectFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex,"Get-group");
            return null;
        }
    }

    public String getGroupById(String id){
        try{
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select GroupName from "+TABLENAME+" where GroupID="+id,null);
            cursor.moveToFirst();
            database.close();
            return cursor.getString(0);
        }
        catch (Exception ex){
            return null;
        }
    }


    public List<Group> GetAll() {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from " + TABLENAME + "", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex,"GetAll-group");
            return null;
        }
    }


    public List<GroupList> GetGroups(int villageId) {
        try{
            database = getWritableDatabase();
            List<GroupList> list = new ArrayList<GroupList>();
            if(villageId==0){
                list.add(new GroupList("0","--Select Group--"));
            }
            else{
                Cursor cursor = database.rawQuery("SELECT GroupID,GroupName FROM "+TABLENAME+" WHERE VillageID = ? ORDER BY GroupName ASC",new String[]{String.valueOf(villageId)});
                list.add(new GroupList("0","--Select Group--"));
                cursor.moveToFirst();
                while(cursor.isAfterLast() == false){

                    Log.i("GroupDBHelper",cursor.getString(cursor.getColumnIndex("GroupName"))+ ":" + cursor.getInt(cursor.getColumnIndex("GroupID")));
                    list.add(new GroupList(cursor.getString(cursor.getColumnIndex("GroupID")), cursor.getString(cursor.getColumnIndex("GroupName"))));

                    cursor.moveToNext();
                }
                database.close();

            }
            return list;

        }
        catch (Exception ex)
        {
            ex.getStackTrace();
            _PopulateLogValues(ex,"GetGroups-group");
            return null;
        }
    }

    private void _PopulateContentValues(Group group) {
        contentValues.put("GroupID", group.GroupID);
        contentValues.put("GroupName", group.GroupName);
        contentValues.put("UnitNumber", group.UnitNumber);
        contentValues.put("DeviceID", group.DeviceID);
        contentValues.put("Responsible", group.Responsible);
        contentValues.put("ResponsibleMobile", group.ResponsibleMobile);
        contentValues.put("VillageID", group.VillageID);
        contentValues.put("GroupCode", group.GroupCode);
    }

    private Group _PopulateObjectFromCursor(Cursor cursor) {
        try {
            database = getWritableDatabase();
            Group group = new Group();
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                group.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                group.GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
                group.UnitNumber = cursor.getString((cursor.getColumnIndex("UnitNumber")));
                group.DeviceID = cursor.getString((cursor.getColumnIndex("DeviceID")));
                group.VillageID = cursor.getInt((cursor.getColumnIndex("VillageID")));
                group.Responsible = cursor.getString((cursor.getColumnIndex("Responsible")));
                group.ResponsibleMobile = cursor.getString(cursor.getColumnIndex("ResponsibleMobile"));
                group.GroupCode = cursor.getString((cursor.getColumnIndex("GroupCode")));
                cursor.moveToNext();
            }
            cursor.close();database.close();
            return group;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"PopulateObjectFromCursor-group");
            return null;
        }
    }

    private List<Group> _PopulateListFromCursor(Cursor cursor) {
        try {
            database = getWritableDatabase();
            List<Group> groups = new ArrayList<Group>();
            Group group = new Group();
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                group.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                group.GroupName = cursor.getString(cursor.getColumnIndex("GroupName"));
                group.UnitNumber = cursor.getString((cursor.getColumnIndex("UnitNumber")));
                group.DeviceID = cursor.getString((cursor.getColumnIndex("DeviceID")));
                group.VillageID = cursor.getInt((cursor.getColumnIndex("VillageID")));
                group.GroupCode = cursor.getString((cursor.getColumnIndex("GroupCode")));
                group.ResponsibleMobile = cursor.getString(cursor.getColumnIndex("ResponsibleMobile"));
                group.Responsible = cursor.getString((cursor.getColumnIndex("Responsible")));

                groups.add(group);
                cursor.moveToNext();
            }
            cursor.close();database.close();
            return groups;
        } catch (Exception ex) {
            _PopulateLogValues(ex,"PopulateListFromCursor-group");
            return null;
        }
    }

    private void _PopulateLogValues(Exception ex,String method) {
        database = getWritableDatabase();
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
        contentValues.put("GroupId", logs.groupId);
        contentValues.put("DeviceId",logs.deviceId);
        contentValues.put("LogDetail","StudentLog");
        database.insert(ERRORTABLENAME, null, contentValues);
        database.close();
        BackupDatabase.backup(c);
    }
}