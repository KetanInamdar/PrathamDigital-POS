package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
    }

    public void goToAdminPage(View view) {
        Intent intent =new Intent(WelcomeActivity.this,AdminActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }


    public void goToStudentLogin(View view) {
        Intent intent =new Intent(WelcomeActivity.this,StartingActivity.class);
        WelcomeActivity.this.startActivity(intent);
    }
}
