package com.example.sajidsalman75.nearby.Controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Users;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/21/2017.
 */

public class AddAdmin extends AppCompatActivity {

    EditText etFname, etLname, etEmail, etPassword;
    Button btnRemoveAdmin;
    DatabaseHelper db;
    Users user;
    int id, result;
    SharedPreferences prefs;
    ProgressDialog progress;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progress.dismiss();
            if (result == 1){
                Toast.makeText(getApplicationContext(), "Admin Added", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Not Added", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(AddAdmin.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            Intent i = new Intent(AddAdmin.this, Login.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_admin);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        db = new DatabaseHelper(this);
        collectData();
        etFname = findViewById(R.id.etfname);
        etLname = findViewById(R.id.etlname);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        btnRemoveAdmin = findViewById(R.id.btnRemoveAdmin);
        etFname.setText(user.getFIRSTNAME());
        etLname.setText(user.getLASTNAME());
        etEmail.setText(user.getEMAIL());
        etPassword.setText(user.getPASSWORD());
    }

    private void collectData(){
        prefs = PreferenceManager.getDefaultSharedPreferences(AddAdmin.this);
        user = new Users(prefs.getString("firstname", "none"),
                prefs.getString("lastname", "none"),
                prefs.getString("email1", "none"),
                prefs.getString("password1", "none"),
                prefs.getString("role1", "none"));
        id = prefs.getInt("userid", 0);
    }

    public void btnAddAdminClicked(View view){

        Runnable r = new Runnable() {
            @Override
            public void run() {
                result = db.createAdmin(Integer.toString(id));
                handler.sendEmptyMessage(0);
            }
        };

        Thread t1 = new Thread(r);
        progress = new ProgressDialog(AddAdmin.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        t1.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(AddAdmin.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(AddAdmin.this, Login.class);
                            startActivity(i);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User cancelled the dialog
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        MenuItem item;
        inflater.inflate(R.menu.action_bar, menu);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddAdmin.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }

    public void btnDisableClicked(View view){
        int id = prefs.getInt("userid",0);
        db.disableUser(Integer.toString(id));
    }
}