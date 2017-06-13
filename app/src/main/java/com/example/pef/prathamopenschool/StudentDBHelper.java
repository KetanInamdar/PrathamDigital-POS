package com.example.pef.prathamopenschool;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class StudentDBHelper extends DBHelper {
    final String TABLENAME = "Student";
    final String ERRORTABLENAME = "Logs";
    Context c;

    public StudentDBHelper(Context context) {
        super(context);
        c = context;
        database = getWritableDatabase();

    }

    public void replaceData(Student obj) {

        database = getWritableDatabase();

        contentValues.put("StudentID", obj.StudentID);
        contentValues.put("FirstName", obj.FirstName);
        contentValues.put("MiddleName", obj.MiddleName);
        contentValues.put("LastName", obj.LastName);
        contentValues.put("Age", obj.Age);
        contentValues.put("Class", obj.Class);
        contentValues.put("UpdatedDate", obj.UpdatedDate);
        contentValues.put("Gender", obj.Gender);
        contentValues.put("GroupID", obj.GroupID);
        database.replace("Student", null, contentValues);

        database.close();

    }


    public void insertData(Student obj) {

        database = getWritableDatabase();

        contentValues.put("StudentID", obj.StudentID);
        contentValues.put("FirstName", obj.FirstName);
        contentValues.put("MiddleName", obj.MiddleName);
        contentValues.put("LastName", obj.LastName);
        contentValues.put("Age", obj.Age);
        contentValues.put("Class", obj.Class);
        contentValues.put("UpdatedDate", obj.UpdatedDate);
        contentValues.put("Gender", obj.Gender);
        contentValues.put("GroupID", obj.GroupID);

        database.insert("Student", null, contentValues);
        database.close();

    }


    public boolean Add(Student student, SQLiteDatabase database1) {
        try {
            _PopulateContentValues(student);
            long resultCount = database1.insert(TABLENAME, null, contentValues);
            database1.close();
            return true;
            //return resultCount;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "Add-Student");
            //return 0;
            return false;
        }
    }

    public boolean Update(Student student) {
        try {
            database = getWritableDatabase();
            _PopulateContentValues(student);
            long resultCount = database.update(TABLENAME, contentValues, "StudentID = ?", new String[]{(student.StudentID)});
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "Update-Student");
            return false;
        }
    }

    public boolean Delete(String studentID) {
        try {
            database = getWritableDatabase();
            long resultCount = database.delete(TABLENAME, "StudentID = ?", new String[]{studentID});
            database.close();
            return true;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "Delete-Student");

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
            _PopulateLogValues(ex, "DeleteAll-Student");
            return false;
        }
    }

    public Student Get(String studentID) {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from " + TABLENAME + " where StudentID='" + studentID + "'", null);
            return _PopulateObjectFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "Get-Student");
            return null;
        }
    }

    public List<Student> GetAll() {
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("select * from " + TABLENAME + "", null);
            return _PopulateListFromCursor(cursor);
        } catch (Exception ex) {
            _PopulateLogValues(ex, "GetAll-Student");
            return null;
        }
    }

    private void _PopulateContentValues(Student student) {

        contentValues.put("StudentID", student.StudentID);
        contentValues.put("FirstName", student.FirstName);
        contentValues.put("LastName", student.LastName);
        contentValues.put("Gender", student.Gender);
        contentValues.put("GroupID", student.GroupID);
        contentValues.put("MiddleName", student.MiddleName);
        contentValues.put("Age", student.Age);
        contentValues.put("Class", student.Class);
        contentValues.put("UpdatedDate", student.UpdatedDate);
    }

    private Student _PopulateObjectFromCursor(Cursor cursor) {
        try {
            database = getWritableDatabase();
            Student student = new Student();
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                student.StudentID = cursor.getString(cursor.getColumnIndex("StudentID"));
                student.FirstName = cursor.getString(cursor.getColumnIndex("FirstName"));
                student.LastName = cursor.getString((cursor.getColumnIndex("LastName")));
                student.Gender = cursor.getString((cursor.getColumnIndex("Gender")));
                student.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                student.MiddleName = cursor.getString(cursor.getColumnIndex("MiddleName"));
                student.Age = cursor.getInt(cursor.getColumnIndex("Age"));
                student.Class = cursor.getInt(cursor.getColumnIndex("Class"));
                student.UpdatedDate = cursor.getString(cursor.getColumnIndex("UpdatedDate"));
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            return student;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "PopulateObjectFromCursor-Student");

            return null;
        }
    }

    private List<Student> _PopulateListFromCursor(Cursor cursor) {
        try {
            database = getWritableDatabase();
            List<Student> students = new ArrayList<Student>();
            Student student = new Student();
            cursor.moveToFirst();

            while (cursor.isAfterLast() == false) {
                student.StudentID = cursor.getString(cursor.getColumnIndex("StudentID"));
                student.FirstName = cursor.getString(cursor.getColumnIndex("FirstName"));
                student.LastName = cursor.getString(cursor.getColumnIndex("LastName"));
                student.Gender = cursor.getString(cursor.getColumnIndex("Gender"));
                student.GroupID = cursor.getString(cursor.getColumnIndex("GroupID"));
                student.MiddleName = cursor.getString(cursor.getColumnIndex("MiddleName"));
                student.Age = cursor.getInt(cursor.getColumnIndex("Age"));
                student.Class = cursor.getInt(cursor.getColumnIndex("Class"));
                student.UpdatedDate = cursor.getString(cursor.getColumnIndex("UpdatedDate"));
                students.add(student);
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
            return students;
        } catch (Exception ex) {
            _PopulateLogValues(ex, "PopulateListFromCursor-Student");

            return null;
        }
    }

    public JSONArray getStudentsList(String groupId) {
        List<StudentList> list = new ArrayList<StudentList>();
        JSONArray arr = new JSONArray();
        try {
            database = getWritableDatabase();
            Cursor cursor = database.rawQuery("SELECT StudentID,FirstName || \" \" ||LastName as LastName FROM " + TABLENAME + " WHERE GroupID = ? ORDER BY LastName ASC", new String[]{String.valueOf(groupId)});

            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                JSONObject obj = new JSONObject();
                obj.put("studentId", cursor.getString(cursor.getColumnIndex("StudentID")));
                obj.put("studentName", cursor.getString(cursor.getColumnIndex("LastName")));
                obj.put("groupId", cursor.getString(cursor.getColumnIndex("GroupID")));
                arr.put(obj);
                cursor.moveToNext();
            }
            database.close();
        } catch (Exception ex) {
            ex.getStackTrace();
            _PopulateLogValues(ex, "getStudetntsList-Student");
            return null;
        }
        return arr;
    }

    private void _PopulateLogValues(Exception ex, String method) {
        database = getWritableDatabase();
        Logs logs = new Logs();
        logs.currentDateTime = Util.GetCurrentDateTime();
        logs.errorType = "Error";
        logs.exceptionMessage = ex.getMessage();
        logs.exceptionStackTrace = ex.getStackTrace().toString();
        logs.methodName = method;

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