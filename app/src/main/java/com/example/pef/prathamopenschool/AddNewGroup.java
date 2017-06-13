package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;
import java.util.UUID;

public class AddNewGroup extends Activity {

    Spinner states_spinner, blocks_spinner, villages_spinner;
    EditText edt_NewGroupName, edt_ProgramID;
    Button btn_Submit, btn_Clear;
    VillageDBHelper database;
    GroupDBHelper gdb;
    List<String> Blocks;
    int vilID;
    String deviceID="";
    String deviceIMEI="";

    Context villageContext, grpContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_group);

        villageContext = this;
        database = new VillageDBHelper(villageContext);

        grpContext = this;

        // Unique ID For GroupID
        UUID uuid = UUID.randomUUID();
        final String randomUUIDGroup = uuid.toString();

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


        edt_NewGroupName = (EditText) findViewById(R.id.edt_NewGroupName);

        // Generate Unique Device ID
        deviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(),Settings.Secure.ANDROID_ID);
        /*//Device ID from Assign Groups
        TelephonyManager tManager = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        deviceIMEI = tManager.getDeviceId();
        final String devID = deviceIMEI;
*/
        btn_Submit = (Button) findViewById(R.id.btn_Submit);
        btn_Clear = (Button) findViewById(R.id.btn_Clear);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Check AllSpinners Emptyness
                int StatesSpinnerValue = states_spinner.getSelectedItemPosition();
                int BlocksSpinnerValue = blocks_spinner.getSelectedItemPosition();
                int VillagesSpinnerValue = villages_spinner.getSelectedItemPosition();
                gdb = new GroupDBHelper(grpContext);

                if (StatesSpinnerValue > 0 && BlocksSpinnerValue > 0 && VillagesSpinnerValue > 0) {


                    Group grpobj = new Group();

                    grpobj.GroupID = randomUUIDGroup;
                    grpobj.GroupCode = "";
                    grpobj.GroupName = edt_NewGroupName.getText().toString();
                    grpobj.UnitNumber = "";
                    grpobj.DeviceID = deviceID;
                    grpobj.Responsible = "";
                    grpobj.ResponsibleMobile = "";
                    grpobj.VillageID = Integer.parseInt(String.valueOf(vilID));
                    grpobj.ProgramID = Integer.parseInt(String.valueOf(edt_ProgramID.getText()));

                    gdb.insertData(grpobj);
                    Toast.makeText(AddNewGroup.this, "Record Inserted Successfully !!!", Toast.LENGTH_SHORT).show();
                    FormReset();

                } else {
                    Toast.makeText(AddNewGroup.this, "Please Select Fill all fields !!!", Toast.LENGTH_SHORT).show();
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
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void FormReset() {

        states_spinner.setSelection(0);
        blocks_spinner.setSelection(0);
        villages_spinner.setSelection(0);
        edt_NewGroupName.getText().clear();
    }


}