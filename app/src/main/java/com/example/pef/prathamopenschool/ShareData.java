package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ShareData extends Activity {

    Button btnShareUsage, btnShareProfiles;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_data);

        btnShareProfiles = (Button) findViewById(R.id.btn_ShareStudentProfiles);
    }

}
