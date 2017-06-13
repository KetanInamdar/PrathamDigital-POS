package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class AddNewCrl extends Activity {


    EditText edt_Crlid, edt_Fname, edt_Lname, edt_Username, edt_Password, edt_ProgramId, edt_Mobile, edt_Email;
    Spinner spinner_State;
    Button btn_Submit, btn_Clear;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_crl);

        context = this;

        edt_Crlid = (EditText) findViewById(R.id.edt_CrlID);
        edt_Fname = (EditText) findViewById(R.id.edt_Fname);
        edt_Lname = (EditText) findViewById(R.id.edt_Lname);
        edt_Username = (EditText) findViewById(R.id.edt_Username);
        edt_Password = (EditText) findViewById(R.id.edt_Password);
        edt_ProgramId = (EditText) findViewById(R.id.edt_ProgramId);
        edt_Mobile = (EditText) findViewById(R.id.edt_Mobile);
        edt_Email = (EditText) findViewById(R.id.edt_Email);

        btn_Submit = (Button) findViewById(R.id.btn_Submit);
        btn_Clear = (Button) findViewById(R.id.btn_Clear);

        spinner_State = (Spinner) findViewById(R.id.spinner_State);

        //Get Villages Data for States AllSpinners
        VillageDBHelper database = new VillageDBHelper(context);
        List<String> States = database.GetState();

        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> StateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, States);

        // Hint for AllSpinners
        spinner_State.setPrompt("Select State");
        spinner_State.setAdapter(StateAdapter);

        btn_Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //CRL Initial DB Process
                CrlDBHelper db = new CrlDBHelper(context);

                // Check existing CRLID Entry
                Boolean result = db.GetCrlUserName(edt_Username.getText().toString());
                if (result == false) {

                    Crl crlobj = new Crl();

                    crlobj.CRLId = edt_Crlid.getText().toString();
                    crlobj.FirstName = edt_Fname.getText().toString();
                    crlobj.LastName = edt_Lname.getText().toString();
                    crlobj.UserName = edt_Username.getText().toString();
                    crlobj.Password = edt_Password.getText().toString();
                    crlobj.ProgramId = Integer.parseInt(String.valueOf(edt_ProgramId.getText()));
                    crlobj.Mobile = edt_Mobile.getText().toString();
                    crlobj.State = spinner_State.getSelectedItem().toString(); // get Selected Item
                    crlobj.Email = edt_Email.getText().toString();

                    // Check AllSpinners Emptyness
                    int SpinnerValue = spinner_State.getSelectedItemPosition();
                    if (SpinnerValue > 0) {

                        db.insertData(crlobj);
                        Toast.makeText(AddNewCrl.this, "Record Inserted Successfully !!!", Toast.LENGTH_SHORT).show();
                        FormReset();
                    } else {
                        Toast.makeText(AddNewCrl.this, "Please Select the State !!!", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(AddNewCrl.this, "CRL UserName already present !!! Please choose another CRLID !!!", Toast.LENGTH_SHORT).show();
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

    public void FormReset() {

        edt_Crlid.getText().clear();
        edt_Fname.getText().clear();
        edt_Lname.getText().clear();
        edt_Username.getText().clear();
        edt_Password.getText().clear();
        edt_ProgramId.getText().clear();
        edt_Mobile.getText().clear();
        edt_Email.getText().clear();

        spinner_State.setSelection(0);
    }
}
