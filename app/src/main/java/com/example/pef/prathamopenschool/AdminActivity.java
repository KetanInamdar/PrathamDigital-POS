package com.example.pef.prathamopenschool;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AdminActivity extends Activity {

    Context context;
    EditText edtAdmin, edtPass;
    Button btn_Login;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        context = this;

        //Opening SQLite Pipeline
        final CrlDBHelper db = new CrlDBHelper(context);
        db.getReadableDatabase();

        edtAdmin = (EditText) findViewById(R.id.txtAdminUsername);
        edtPass = (EditText) findViewById(R.id.txtAdminPassword);
        btn_Login = (Button) findViewById(R.id.adminLogIn);


        btn_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String enteredUserName = edtAdmin.getText().toString();
                String enteredPassWord = edtPass.getText().toString();

                boolean result;
                result = db.CrlLogin(enteredUserName, enteredPassWord);

                if (result == true) {
                    Intent intent = new Intent(AdminActivity.this, SyncActivity.class);
                    intent.putExtra("UserName", enteredUserName);
                    startActivity(intent);
                    edtAdmin.setText("");
                    edtPass.setText("");
                } else {
                    Toast.makeText(AdminActivity.this, "Invalid Credentials !!!", Toast.LENGTH_SHORT).show();
                    edtAdmin.setText("");
                    edtPass.setText("");
                }

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();

    }
}
