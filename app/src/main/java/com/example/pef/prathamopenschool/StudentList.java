package com.example.pef.prathamopenschool;

/**
 * Created by PEF-2 on 27/10/2015.
 */
public class StudentList {
    String studentid;
    String studentName;

    StudentList(String id,String name){
        studentid = id;
        studentName = name;
    }

    public String getStudentid() {
        return studentid;
    }

    public String getStudentName() {
        return studentName;
    }
}
