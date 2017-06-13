package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.List;

public class PullData extends Activity {

    Spinner spinner_State;
    Button btn_pull;
    VillageDBHelper database;
    CrlDBHelper db;
    Context context;
    String selectedState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pull_data);

        context = this;
        database = new VillageDBHelper(context);

        spinner_State = (Spinner) findViewById(R.id.spinner_State);
        //Get Villages Data for States AllSpinners
        List<String> States = database.GetState();
        //Creating the ArrayAdapter instance having the Villages list
        ArrayAdapter<String> StateAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, States);
        // Hint for AllSpinners
        spinner_State.setPrompt("Select State");
        spinner_State.setAdapter(StateAdapter);

        spinner_State.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedState = spinner_State.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        btn_pull = (Button) findViewById(R.id.btn_PullData);

        btn_pull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                // Check Spineer's Emptyness
                int SpinnerValue = spinner_State.getSelectedItemPosition();

                if (SpinnerValue > 0) {

                    Toast.makeText(context, selectedState + " is Selected !!!", Toast.LENGTH_SHORT).show();

                    // Executed when Pull Data will be called for Offline Mode
                    try {
                        UpdateCrlAndVillageJson();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    Toast.makeText(context, "Data Updates Successfully !!!", Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(PullData.this, "Please Select the State !!!", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }


    public void UpdateCrlAndVillageJson() throws JSONException {

        // insert your code to run only when application is started first time here
        context = this;

        //CRL Initial DB Process
        db = new CrlDBHelper(context);
        // For Loading CRL Json From External Storage (Assets)
        JSONArray crlJsonArray = new JSONArray(loadCrlJSONFromAsset());
        for (int i = 0; i < crlJsonArray.length(); i++) {

            JSONObject clrJsonObject = crlJsonArray.getJSONObject(i);
            Crl crlobj = new Crl();
            crlobj.CRLId = clrJsonObject.getString("CRLId");
            crlobj.FirstName = clrJsonObject.getString("FirstName");
            crlobj.LastName = clrJsonObject.getString("LastName");
            crlobj.UserName = clrJsonObject.getString("UserName");
            crlobj.Password = clrJsonObject.getString("Password");
            crlobj.ProgramId = clrJsonObject.getInt("ProgramId");
            crlobj.Mobile = clrJsonObject.getString("Mobile");
            crlobj.State = clrJsonObject.getString("State");
            crlobj.Email = clrJsonObject.getString("Email");

            db.updateJsonData(crlobj);
        }


        //Villages Initial DB Process
        database = new VillageDBHelper(context);
        // For Loading Villages Json From External Storage (Assets)
        JSONArray villagesJsonArray = new JSONArray(loadVillageJSONFromAsset());

        for (int j = 0; j < villagesJsonArray.length(); j++) {
            JSONObject villagesJsonObject = villagesJsonArray.getJSONObject(j);

            Village villageobj = new Village();
            villageobj.VillageID = villagesJsonObject.getInt("VillageId");
            villageobj.VillageCode = villagesJsonObject.getString("VillageCode");
            villageobj.VillageName = villagesJsonObject.getString("VillageName");
            villageobj.Block = villagesJsonObject.getString("Block");
            villageobj.District = villagesJsonObject.getString("District");
            villageobj.State = villagesJsonObject.getString("State");
            villageobj.CRLID = villagesJsonObject.getString("CRLId");

            database.updateJsonData(villageobj);
        }

    }

    // Reading CRL Json From SDCard
    public String loadCrlJSONFromAsset() {
        String crlJsonStr = null;

        try {
            File crlJsonSDCard = new File(splashScreenVideo.fpath + "Json/Crl.json");
            FileInputStream stream = new FileInputStream(crlJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                crlJsonStr = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return crlJsonStr;
    }

    // Reading Village Json From SDCard
    public String loadVillageJSONFromAsset() {
        String villageJson = null;
        try {
            File villageJsonSDCard = new File(splashScreenVideo.fpath + "Json/Village.json");
            FileInputStream stream = new FileInputStream(villageJsonSDCard);
            try {
                FileChannel fc = stream.getChannel();
                MappedByteBuffer bb = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());

                villageJson = Charset.defaultCharset().decode(bb).toString();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                stream.close();
            }

        } catch (Exception e) {
        }

        return villageJson;

    }

}
