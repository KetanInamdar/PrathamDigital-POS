package com.example.pef.prathamopenschool;

import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PEF on 05/06/2017.
 */

public class CrlDBHelper extends DBHelper {


    Cursor cursor;

    public CrlDBHelper(Context context) {
        super(context);
        // db = this.getWritableDatabase();
        database = getWritableDatabase();
    }

    public boolean checkTableEmptyness() {

        database = getReadableDatabase();

        String count = "SELECT count(*) FROM CRL";
        Cursor mcursor = database.rawQuery(count, null);
        mcursor.moveToFirst();
        int icount = mcursor.getInt(0);
        if (icount > 0) {
            return true;
        }
        else {
            return false;
        }
    }

    // Executed when Pull Data will be called
    public void updateJsonData(Crl obj) {

        database = getWritableDatabase();

        contentValues.put("CRLID", obj.CRLId);
        contentValues.put("FirstName", obj.FirstName);
        contentValues.put("LastName", obj.LastName);
        contentValues.put("UserName", obj.UserName);
        contentValues.put("Password", obj.Password);
        contentValues.put("ProgramId", obj.ProgramId);
        contentValues.put("Mobile", obj.Mobile);
        contentValues.put("State", obj.State);
        contentValues.put("Email", obj.Email);

        database.replace("CRL", null, contentValues);
        database.close();
    }



    public void insertData(Crl obj) {

        database = getWritableDatabase();

        contentValues.put("CRLID", obj.CRLId);
        contentValues.put("FirstName", obj.FirstName);
        contentValues.put("LastName", obj.LastName);
        contentValues.put("UserName", obj.UserName);
        contentValues.put("Password", obj.Password);
        contentValues.put("ProgramId", obj.ProgramId);
        contentValues.put("Mobile", obj.Mobile);
        contentValues.put("State", obj.State);
        contentValues.put("Email", obj.Email);

        database.insert("CRL", null, contentValues);
        database.close();
    }


    public boolean GetCrlUserName(String username) {

        database = getWritableDatabase();

        cursor = database.rawQuery("SELECT UserName FROM CRL WHERE UserName = ? ", new String[]{String.valueOf(username)});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.close();
                database.close();
                return true;
            }
        }
        return false;
    }

    public boolean CrlLogin(String uname, String pass) {

        database = getWritableDatabase();

        cursor = database.rawQuery("SELECT * FROM CRL WHERE UserName=? AND PassWord =?", new String[]{uname, pass});
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                cursor.close();
                database.close();
                return true;
            }
        }
        return false;
    }

    public List<Crl> GetAll() {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from CRL ", null);
            database.close();
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {

            return null;
        }
    }

    private List<Crl> _PopulateListFromCursor(Cursor cursor) {
        try {
            database = getWritableDatabase();
            List<Crl> crl_list = new ArrayList<Crl>();
            Crl crlObject = new Crl();
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                crlObject.CRLId = cursor.getString(cursor.getColumnIndex("CRLID"));
                crlObject.FirstName = cursor.getString(cursor.getColumnIndex("FirstName"));
                crlObject.LastName = cursor.getString(cursor.getColumnIndex("LaseName"));
                crlObject.UserName = cursor.getString((cursor.getColumnIndex("UserName")));
                crlObject.Password = cursor.getString((cursor.getColumnIndex("PassWord")));
                crlObject.State = cursor.getString((cursor.getColumnIndex("State")));
                crlObject.ProgramId = cursor.getInt(cursor.getColumnIndex("ProgramId"));
                crlObject.Mobile = cursor.getString((cursor.getColumnIndex("Mobile")));
                crlObject.State = cursor.getString((cursor.getColumnIndex("State")));
                crlObject.Email = cursor.getString((cursor.getColumnIndex("Email")));

                crl_list.add(crlObject);
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            return crl_list;
        } catch (Exception ex) {

            return null;
        }
    }
}
