package com.example.sajidsalman75.nearby.Controller;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by sajidsalman75 on 11/10/2017.
 */

public class Login extends AppCompatActivity {
    DatabaseHelper db;
    EditText etEmail, etPassword;
    Cursor res;
    boolean flag;
    ProgressDialog progress;
    Button btnLogin, btnSearch;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            progress.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        btnSearch = findViewById(R.id.btnSearch);
        btnLogin = findViewById(R.id.btnLogin);
        db = new DatabaseHelper(this);
        login();
    }

    public void login(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etEmail = findViewById(R.id.etEmail);
                etPassword = findViewById(R.id.etPassword);
                boolean flag = true;

                if (isEmpty(etEmail.getText().toString())){
                    showEmptyError(etEmail);
                    flag = false;
                }
                if (isEmpty(etPassword.getText().toString())){
                    showEmptyError(etPassword);
                    flag = false;
                }

                if (flag){
                        res = db.login(etEmail.getText().toString(), etPassword.getText().toString());
                        if(res.getCount() == 0) {
                            // show message
                            showToast("Wrong Email or Password");
                        }
                        else {
                            progress = new ProgressDialog(Login.this);
                            progress.setTitle("Logging In");
                            progress.setMessage("Wait while logging in...");
                            progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
                            progress.show();
                            res.moveToNext();

                            Runnable r = new Runnable() {
                                @Override
                                public void run() {
                                    if (res.getString(5).equals("superadmin")) {
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                        prefs.edit().putBoolean("Islogin", true).commit();
                                        Intent i = new Intent(Login.this, SuperAdminPanel.class);

                                        prefs.edit().putString("id", res.getString(0)).commit();
                                        prefs.edit().putString("fname", res.getString(1)).commit();
                                        prefs.edit().putString("lname", res.getString(2)).commit();
                                        prefs.edit().putString("email", res.getString(3)).commit();
                                        prefs.edit().putString("password", res.getString(4)).commit();
                                        prefs.edit().putString("role", res.getString(5)).commit();
                                        startActivity(i);
                                    }
                                    else if (res.getString(5).equals("user")){
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                        prefs.edit().putBoolean("Islogin", true).commit();
                                        Intent i = new Intent(Login.this, UserPanel.class);
                                        prefs.edit().putString("id", res.getString(0)).commit();
                                        prefs.edit().putString("fname", res.getString(1)).commit();
                                        prefs.edit().putString("lname", res.getString(2)).commit();
                                        prefs.edit().putString("email", res.getString(3)).commit();
                                        prefs.edit().putString("password", res.getString(4)).commit();
                                        prefs.edit().putString("role", res.getString(5)).commit();
                                        startActivity(i);
                                    }
                                    else if (res.getString(5).equals("admin")){
                                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Login.this);
                                        prefs.edit().putBoolean("Islogin", true).commit();
                                        Intent i = new Intent(Login.this, AdminPanel.class);
                                        prefs.edit().putString("id", res.getString(0)).commit();
                                        prefs.edit().putString("fname", res.getString(1)).commit();
                                        prefs.edit().putString("lname", res.getString(2)).commit();
                                        prefs.edit().putString("email", res.getString(3)).commit();
                                        prefs.edit().putString("password", res.getString(4)).commit();
                                        prefs.edit().putString("role", res.getString(5)).commit();
                                        startActivity(i);
                                    }
                                    else if(res.getString(5).equals("disable")){
                                    }
                                    handler.sendEmptyMessage(0);
                                }
                            };

                            Thread t1 = new Thread(r);
                            t1.start();
                        }
                }
            }
        });
    }

    public void showToast(String message){
        Toast t1 = Toast.makeText(Login.this, message, Toast.LENGTH_SHORT);
        t1.show();
    }
    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }
    private void showEmptyError(EditText et){
        et.setError("Please Fill this");
    }

    public void signupButtonClicked(View view){
        Intent i = new Intent(Login.this, Signup.class);
        startActivity(i);
    }

    public void onBackPressed(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to exit?")
                .setTitle("Exit Application");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory( Intent.CATEGORY_HOME );
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.simple_action_bar, menu);
        return true;
    }

    public void btnSearchClicked(View view){
        Intent i = new Intent(Login.this, Search.class);
        startActivity(i);
    }
}