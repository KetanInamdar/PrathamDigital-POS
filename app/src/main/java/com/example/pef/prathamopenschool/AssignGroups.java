package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


public class AssignGroups extends Activity {

    Spinner states_spinner, blocks_spinner, villages_spinner;
    String deviceIMEI = "";
    public String checkBoxIds[], checkBoxIds1[], group1 = "0", group2 = "0", group3 = "0", group4 = "0", group5 = "0";
    public boolean grpFound = false;
    int cnt = 0;
    VillageDBHelper database;
    StatusDBHelper sdb;
    GroupDBHelper gdb;
    Button btnAssign;
    StudentDBHelper stdDB;
    List<String> Blocks;
    List<Group> listJsonGrp;
    List<Student> jsonStdList;
    List<GroupList> dbgroupList;
    int vilID;
    Context context, villageContext, grpcontext, sdhContext, stdContext;
    JSONArray grpJsonArray, stdJsonArray;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assign_groups);


        btnAssign = (Button) findViewById(R.id.allocateGroups);

        context = this;

        // Generate Device ID
        deviceIMEI = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

        stdContext = this;
        stdDB = new StudentDBHelper(stdContext);
        gdb = new GroupDBHelper(context);

      /*  sdhContext = this;

        grpcontext = this;

        // Memory Allocation
        villageContext = this;*/
        database = new VillageDBHelper(context);

        states_spinner = (Spinner) findViewById(R.id.spinner_SelectState);
        //Get Villages Data for States AllSpinners
        List<String> States = database.GetState();
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> StateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, States);
        // Hint for AllSpinners
        states_spinner.setPrompt("Select State");
        states_spinner.setAdapter(StateAdapter);

        states_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedState = states_spinner.getSelectedItem().toString();
                populateBlock(selectedState);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btnAssign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    group1 = group2 = group3 = group4 = group5 = "0";
                    cnt = 0;
                    for (int i = 0; i < checkBoxIds.length; i++) {
                        CheckBox checkBox = (CheckBox) findViewById(i);

                        if (checkBox.isChecked() && group1 == "0") {
                            group1 = (String) checkBox.getTag();
                            cnt++;
                        } else if (checkBox.isChecked() && group2 == "0") {
                            cnt++;
                            group2 = (String) checkBox.getTag();
                        } else if (checkBox.isChecked() && group3 == "0") {
                            cnt++;
                            group3 = (String) checkBox.getTag();
                        } else if (checkBox.isChecked() && group4 == "0") {
                            cnt++;
                            group4 = (String) checkBox.getTag();
                        } else if (checkBox.isChecked() && group5 == "0") {
                            cnt++;
                            group5 = (String) checkBox.getTag();
                        }

                    }

                    if (cnt < 1) {
                        Toast.makeText(AssignGroups.this, "Please Select atleast one Group !!!", Toast.LENGTH_SHORT).show();
                        // showDialogue("please select atleast two groups for tablet");
                    } else if (cnt >= 1 && cnt <= 5) {
                        try {

                            if (!checkGroupInDB(group1) && group1 != "") {
                                Group obj = getGroupInfo(group1);
                                //   obj.GroupCode = "";
                                gdb.insertData(obj);   // Debugging till here
                            }
                            if (!checkGroupInDB(group2) && group2 != "") {
                                Group obj = getGroupInfo(group2);
                                //   obj.GroupCode = "";
                                gdb.insertData(obj);
                            }
                            if (!checkGroupInDB(group3) && group3 != "") {
                                Group obj = getGroupInfo(group3);
                                obj.GroupCode = "";
                                gdb.insertData(obj);
                            }
                            if (!checkGroupInDB(group4) && group4 != "") {
                                Group obj = getGroupInfo(group4);
                                //   obj.GroupCode = "";
                                gdb.insertData(obj);
                            }
                            if (!checkGroupInDB(group5) && group5 != "") {
                                Group obj = getGroupInfo(group5);
                                //      obj.GroupCode = "";
                                gdb.insertData(obj);
                            }

                            List<Student> stdList = PopulateStudentsFromJson();

                            for (int i = 0; i < stdList.size(); i++) {
                                Student sobj = stdList.get(i);
                                stdDB.replaceData(sobj);
                            }


                            Toast.makeText(AssignGroups.this, "Button Pressed !!!", Toast.LENGTH_SHORT).show();

                            StatusDBHelper statusDBHelper = new StatusDBHelper(context);

                            statusDBHelper.Update("group1", (group1));
                            statusDBHelper.Update("group2", (group2));
                            statusDBHelper.Update("group3", (group3));
                            statusDBHelper.Update("group4", (group4));
                            statusDBHelper.Update("group5", (group5));
                            statusDBHelper.Update("village", Integer.toString(vilID));
                            statusDBHelper.Update("deviceId", deviceIMEI);
                            statusDBHelper.Update("ActivatedDate", new Utility().GetCurrentDateTime());
                            statusDBHelper.Update("ActivatedForGroups", group1 + "," + group2 + "," + group3 + "," + group4 + "," + group5);
                            StatusDBHelper statusDBHelper2 = new StatusDBHelper(context);
                            boolean res = statusDBHelper2.updateTrailerCount(0, group1);
                            res = statusDBHelper2.updateTrailerCount(0, group2);

                            //BackupDatabase.backup(getApplicationContext());
                            //showDialogue(grp1 + " and " + grp2 + " groups are assigned to this tablet.");
                        } catch (Exception e) {

                        }

                        //Toast.makeText(getApplicationContext(),"grp1 : "+group1+"grp2 : "+group2,Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(AssignGroups.this, " You can select Maximum 5 Groups !!! ", Toast.LENGTH_SHORT).show();
                    }
                    //showDialogue("please select two groups for each tablet");
                } catch (Exception e) {

                }
            }


        });


    }//onCreate


    public boolean checkGroupInDB(String grpID) {

        boolean flag = false;

        for (int k = 0; k < dbgroupList.size(); k++) {
            if (dbgroupList.get(k).getGroupId() == grpID) {
                flag = true;
                return true;
            }
        }
        return flag;
    }


    public Group getGroupInfo(String grpID) {

        Group obj = new Group();

        for (int k = 0; k < listJsonGrp.size(); k++) {
            if (listJsonGrp.get(k).GroupID == grpID) {
                return listJsonGrp.get(k);
            }

        }
        return obj;
    }




    public void populateBlock(String selectedState) {
        blocks_spinner = (Spinner) findViewById(R.id.spinner_SelectBlock);
        //Get Villages Data for Blocks AllSpinners
        Blocks = database.GetStatewiseBlock(selectedState);
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> BlockAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Blocks);
        // Hint for AllSpinners
        blocks_spinner.setPrompt("Select Block");
        blocks_spinner.setAdapter(BlockAdapter);

        blocks_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedBlock = blocks_spinner.getSelectedItem().toString();
                populateVillage(selectedBlock);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


    public void populateVillage(String selectedBlock) {
        villages_spinner = (Spinner) findViewById(R.id.spinner_selectVillage);
        //Get Villages Data for Villages filtered by block for Spinners
        List<VillageList> BlocksVillages = database.GetVillages(selectedBlock);
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<VillageList> VillagesAdapter = new ArrayAdapter<VillageList>(this, android.R.layout.simple_spinner_item, BlocksVillages);
        // Hint for AllSpinners
        villages_spinner.setPrompt("Select Village");
        villages_spinner.setAdapter(VillagesAdapter);
        villages_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                VillageList village = (VillageList) parent.getItemAtPosition(position);
                vilID = village.getVillageId();
                try {
                    populateGroups(vilID);  //Populate groups According to JSON & DB in Checklist instead of using spinner
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void populateGroups(int vilID) throws JSONException {

        // Check Spinner Emptyness
        int VillagesSpinnerValue = villages_spinner.getSelectedItemPosition();

        if (VillagesSpinnerValue > 0) {

            // Showing Groups from Database
            checkBoxIds = null;
            GroupDBHelper groupDBHelper = new GroupDBHelper(context);
            dbgroupList = groupDBHelper.GetGroups(vilID);
            List<GroupList> groupList = new ArrayList<GroupList>(dbgroupList);


            // Pulling Group List from JSON
            try {
                listJsonGrp = PopulateGroupsFromJson(vilID);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int j = 0; j < listJsonGrp.size(); j++) {
                boolean flag = false;
                for (int k = 0; k < groupList.size(); k++) {
                    if (groupList.get(k).getGroupId() == listJsonGrp.get(j).GroupID) {
                        flag = true;
                    }

                }
                if (flag == false) {
                    groupList.add(new GroupList(listJsonGrp.get(j).GroupID, listJsonGrp.get(j).GroupName));
                }

            }

            groupList.remove(0);

            LinearLayout my_layout = (LinearLayout) findViewById(R.id.assignGroup1);
            LinearLayout my_layout1 = (LinearLayout) findViewById(R.id.assignGroup2);

            my_layout.removeAllViews();
            my_layout1.removeAllViews();

            checkBoxIds = new String[groupList.size()];
            int half = Math.round(groupList.size() / 2);

            for (int i = 0; i < groupList.size(); i++) {

                GroupList grp = groupList.get(i);
                String groupName = grp.getGroupName();
                String groupId = grp.getGroupId();

                TableRow row = new TableRow(AssignGroups.this);
                //row.setId(groupId);
                checkBoxIds[i] = groupId;

                //dynamically create checkboxes. i.e no. of students in group = no. of checkboxes
                row.setLayoutParams(new TableRow.LayoutParams(TableLayout.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT));
                CheckBox checkBox = new CheckBox(AssignGroups.this);

                try {
                    checkBox.setId(i);
                    checkBox.setTag(groupId);
                    checkBox.setText(groupName);
                } catch (Exception e) {

                }
                checkBox.setTextSize(20);
                checkBox.setTextColor(Color.BLACK);
                row.addView(checkBox);
                if (i >= half)
                    my_layout1.addView(row);
                else
                    my_layout.addView(row);
            }


            // Animation Effect on Groups populate
            LinearLayout image = (LinearLayout) findViewById(R.id.LinearLayoutGroups);
            Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide);
            image.startAnimation(animation1);

            Button btnAssign = (Button) findViewById(R.id.allocateGroups);
            btnAssign.setVisibility(View.VISIBLE);

        } else

        {
            LinearLayout my_layout = (LinearLayout) findViewById(R.id.assignGroup1);
            LinearLayout my_layout1 = (LinearLayout) findViewById(R.id.assignGroup2);
            my_layout.removeAllViews();
            my_layout1.removeAllViews();
        }

    }


    public List<Student> PopulateStudentsFromJson() throws JSONException {

        String tempGid;

        List<Student> jsonStdList = new ArrayList<Student>();

        // insert your code to run only when application is started first time here
        //stdContext = this;

        // For Loading Student Json From External Storage (Assets)
        stdJsonArray = new JSONArray(loadStudentJSONFromAsset());

        for (int i = 0; i < stdJsonArray.length(); i++) {


            JSONObject stdJsonObject = stdJsonArray.getJSONObject(i);

            //Check Specific GID
            tempGid = stdJsonObject.getString("GroupId");

            if ((group1.equals(tempGid)) || (group2.equals(tempGid)) || (group3.equals(tempGid)) || (group4.equals(tempGid)) || (group5.equals(tempGid))) {

                Student stdObj = new Student();

                stdObj.StudentID = stdJsonObject.getString("StudentId");
                stdObj.FirstName = stdJsonObject.getString("FirstName");
                stdObj.LastName = stdJsonObject.getString("LastName");
                stdObj.Age = stdJsonObject.getInt("Age");
                stdObj.Class = stdJsonObject.getInt("Class");
                stdObj.UpdatedDate = stdJsonObject.getString("UpdatedDate");
                stdObj.Gender = stdJsonObject.getString("Gender");
                stdObj.GroupID = stdJsonObject.getString("GroupId");

                jsonStdList.add(stdObj);

            }

            //db.insertData(grpobj);
        }

        return jsonStdList;
    }

    // Reading Student Json From SDCard
    public String loadStudentJSONFromAsset() {

        String stdJsonStr = null;

        try {
            File stdJsonSDCard = new File(splashScreenVideo.fpath + "Json/Student.json");
            FileInputStream stream = new FileInputStream(stdJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                stdJsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return stdJsonStr;
    }


    public List<Group> PopulateGroupsFromJson(int vilID) throws JSONException {

        int tempVid;

        List<Group> jsonGrpList = new ArrayList<Group>();

        // insert your code to run only when application is started first time here
        //grpcontext = this;

        // For Loading Group Json From External Storage (Assets)
        grpJsonArray = new JSONArray(loadGroupJSONFromAsset());

        for (int i = 0; i < grpJsonArray.length(); i++) {


            JSONObject grpJsonObject = grpJsonArray.getJSONObject(i);

            //Check Specific VID
            tempVid = grpJsonObject.getInt("VillageId");

            if (vilID == tempVid) {

                Group grpobj = new Group();

                grpobj.GroupID = grpJsonObject.getString("GroupId");
                grpobj.GroupName = grpJsonObject.getString("GroupName");
                grpobj.VillageID = grpJsonObject.getInt("VillageId");
                grpobj.ProgramID = grpJsonObject.getInt("ProgramId");

                jsonGrpList.add(grpobj);

            }

            //db.insertData(grpobj);
        }

        return jsonGrpList;
    }

    // Reading Group Json From SDCard
    public String loadGroupJSONFromAsset() {
        String grpJsonStr = null;

        try {
            File grpJsonSDCard = new File(splashScreenVideo.fpath + "Json/Group.json");
            FileInputStream stream = new FileInputStream(grpJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
                grpJsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return grpJsonStr;
    }

}