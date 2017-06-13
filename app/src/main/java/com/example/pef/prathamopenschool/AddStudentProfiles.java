package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.UUID;

public class AddStudentProfiles extends Activity {

    Spinner states_spinner, blocks_spinner, villages_spinner, groups_spinner, existingStudent_Spinner;
    EditText edt_Fname, edt_Mname, edt_Lname, edt_Age, edt_Class, edt_ProgramID;
    RadioGroup rg_Gender;
    RadioButton selectedGender;
    Button btn_Submit, btn_Clear, btn_Capture;
    VillageDBHelper database;
    GroupDBHelper gdb;
    StudentDBHelper sdb;
    List<String> Blocks;
    int vilID;
    Context villageContext, grpContext, stdContext;
    Utility Util;
    String gender;
    List<String> ExistingStudents;
    String StudentID, FirstName, MiddleName, LastName, Age, Class, UpdatedDate, Gender;
    String randomUUIDStudent, randomUUIDGroup;
    private static final int TAKE_Thumbnail = 1;
    ImageView imgView;
    private static String TAG = "PermissionDemo";
    private static final int REQUEST_WRITE_STORAGE = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student_profiles);


        villageContext = this;
        database = new VillageDBHelper(villageContext);

        grpContext = this;

        stdContext = this;
        sdb = new StudentDBHelper(stdContext);

        // Unique ID For StudentID
        UUID uuStdid = UUID.randomUUID();
        randomUUIDStudent = uuStdid.toString();

        // Unique ID For GroupID
        UUID uuGrpid = UUID.randomUUID();
        randomUUIDGroup = uuGrpid.toString();


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


        // Program ID (1=HLearning, 2=ReadIndia(KP))
        edt_ProgramID = (EditText) findViewById(R.id.edt_ProgramId);
        edt_ProgramID.setText("1");


        edt_Fname = (EditText) findViewById(R.id.edt_FirstName);
        edt_Mname = (EditText) findViewById(R.id.edt_MiddleName);
        edt_Lname = (EditText) findViewById(R.id.edt_LastName);
        edt_Age = (EditText) findViewById(R.id.edt_Age);
        edt_Class = (EditText) findViewById(R.id.edt_Class);

        rg_Gender = (RadioGroup) findViewById(R.id.rg_Gender);

        // get selected radio button from radioGroup
        int selectedId = rg_Gender.getCheckedRadioButtonId();
        // find the radio button by returned id
        selectedGender = (RadioButton) findViewById(selectedId);
        gender = selectedGender.getText().toString();


        // Take Photo Functionality
        btn_Capture = (Button) findViewById(R.id.btn_Capture);
        imgView = (ImageView) findViewById(R.id.imageView);


        btn_Capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(MediaStore.ACTION_IMAGE_CAPTURE),
                        TAKE_Thumbnail);
            }
        });


        btn_Submit = (Button) findViewById(R.id.btn_Submit);
        btn_Clear = (Button) findViewById(R.id.btn_Clear);

        Util = new Utility();


        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check AllSpinners Emptyness
                int StatesSpinnerValue = states_spinner.getSelectedItemPosition();
                int BlocksSpinnerValue = blocks_spinner.getSelectedItemPosition();
                int VillagesSpinnerValue = villages_spinner.getSelectedItemPosition();
                int GroupsSpinnerValue = groups_spinner.getSelectedItemPosition();


                if (StatesSpinnerValue > 0 && BlocksSpinnerValue > 0 && VillagesSpinnerValue > 0 && GroupsSpinnerValue > 0 && selectedGender.isChecked()) {


                    Student stdObj = new Student();

                    stdObj.StudentID = randomUUIDStudent;
                    stdObj.FirstName = edt_Fname.getText().toString();
                    stdObj.MiddleName = edt_Mname.getText().toString();
                    stdObj.LastName = edt_Lname.getText().toString();
                    stdObj.Age = Integer.parseInt(String.valueOf(edt_Age.getText()));
                    stdObj.Class = Integer.parseInt(String.valueOf(edt_Class.getText()));
                    stdObj.UpdatedDate = Util.GetCurrentDateTime();
                    stdObj.Gender = gender;
                    stdObj.GroupID = randomUUIDGroup;

                    sdb.insertData(stdObj);

                    Toast.makeText(AddStudentProfiles.this, "Record Inserted Successfully !!!", Toast.LENGTH_SHORT).show();
                    FormReset();

                } else {
                    Toast.makeText(AddStudentProfiles.this, "Please Select Fill all fields !!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

        btn_Clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FormReset();
            }
        });


    }


       /* rg_Gender = (RadioGroup) findViewById(R.id.rg_Gender);
        rb_Male = (RadioButton) findViewById(R.id.rb_Male);
        rb_Female = (RadioButton) findViewById(R.id.rb_Female);

*/


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
                populateGroups(vilID);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void populateGroups(int villageID) {
        groups_spinner = (Spinner) findViewById(R.id.spinner_SelectGroups);
        //Get Groups Data for Villages filtered by Villages for Spinners
        grpContext = this;
        gdb = new GroupDBHelper(grpContext);
        List<GroupList> GroupsVillages = gdb.GetGroups(villageID);
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<GroupList> GroupsAdapter = new ArrayAdapter<GroupList>(this, android.R.layout.simple_spinner_item, GroupsVillages);
        // Hint for AllSpinners
        groups_spinner.setPrompt("Select Group");
        groups_spinner.setAdapter(GroupsAdapter);
        groups_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateExistingStudents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void populateExistingStudents() {

        existingStudent_Spinner = (Spinner) findViewById(R.id.spinner_existingStudent);

        /*ExistingStudents = sdb.fetchData(randomUUIDGroup);

        StudentID = ExistingStudents.get(0);
        FirstName = ExistingStudents.get(1);
        MiddleName = ExistingStudents.get(2);
        LastName = ExistingStudents.get(3);
        Age = ExistingStudents.get(4);
        Class = ExistingStudents.get(5);
        UpdatedDate = ExistingStudents.get(6);
        Gender = ExistingStudents.get(7);

        List<Student> StudentFName = FirstName;
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<Student> GroupsAdapter = new ArrayAdapter<GroupList>(this, android.R.layout.simple_spinner_item, StudentFName);
        // Hint for AllSpinners
        groups_spinner.setPrompt("Select Group");
        groups_spinner.setAdapter(GroupsAdapter);
        groups_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                populateExistingStudents();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        edt_Fname.setText(FirstName);
        edt_Mname.setText(MiddleName);
        edt_Lname.setText(LastName);
        edt_Age.setText(Age);
        edt_Class.setText(Class);
        if (Gender.equals("Male")) {
            selectedGender.setChecked(true);
        } else {
            selectedGender.setChecked(true);
        }*/
    }


    public void FormReset() {

        states_spinner.setSelection(0);
        blocks_spinner.setSelection(0);
        villages_spinner.setSelection(0);
        groups_spinner.setSelection(0);
        existingStudent_Spinner.setSelection(0);

        edt_Fname.getText().clear();
        edt_Mname.getText().clear();
        edt_Lname.getText().clear();
        edt_Age.getText().clear();
        edt_Class.getText().clear();

        rg_Gender.clearCheck();

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == TAKE_Thumbnail) {
            if (data != null) {
                if (data.hasExtra("data")) {
                    Bitmap thumbnail1 = data.getParcelableExtra("data");
                    imgView.setImageBitmap(thumbnail1);
                    try {

                        Context cnt;
                        cnt = this;
                        File folder = new File(Environment.getExternalStorageDirectory() + "/.StudentPictures");
                        boolean success = true;
                        if (!folder.exists()) {
                            success = folder.mkdir();
                        }
                        if (success) {
                            // Do something on success
                            File outputFile = new File(folder, randomUUIDStudent + ".jpg");
                            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                            thumbnail1.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                            fileOutputStream.flush();
                            fileOutputStream.close();
                            data = null;
                            thumbnail1 = null;
                        } else {
                            // Do something else on failure
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}