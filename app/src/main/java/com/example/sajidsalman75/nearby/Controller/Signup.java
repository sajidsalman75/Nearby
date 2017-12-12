package com.example.sajidsalman75.nearby.Controller;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import com.example.sajidsalman75.nearby.Model.Users;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/10/2017.
 */

public class Signup extends AppCompatActivity {

    int i=0;
    DatabaseHelper db;
    EditText etFirstname, etLastname, etEmail, etPassword;
    Button btnSignup, btnLogin;
    Users user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        db = new DatabaseHelper(this);
        btnSignup = findViewById(R.id.btnSignup);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etFirstname = findViewById(R.id.etfname);
        etLastname = findViewById(R.id.etlname);
        btnLogin = findViewById(R.id.btnLogin);
        signup();
    }



    public void signup(){
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = true;
                if(isEmpty(etEmail.getText().toString())){
                    flag = false;
                    showEmptyError(etEmail);
                }
                if (isEmpty(etFirstname.getText().toString())){
                    flag = false;
                    showEmptyError(etFirstname);
                }
                if (isEmpty(etLastname.getText().toString())){
                    flag = false;
                    showEmptyError(etLastname);
                }
                if (isEmpty(etPassword.getText().toString())){
                    flag = false;
                    showEmptyError(etPassword);
                }
                if (!isValidEmail(etEmail.getText().toString())){
                    flag = false;
                    etEmail.setError("Please use a valid email");
                }
                if(etPassword.getText().length() < 6){
                    flag = false;
                    etPassword.setError("Please enter at least six characters");
                }

                if (flag){
                    user = new Users(etFirstname.getText().toString(), etLastname.getText().toString(),
                            etEmail.getText().toString(), etPassword.getText().toString(), "user");
                    boolean isInserted = db.insertUser(user);
                    if(!isInserted){
                        showToast("Email Already Exists");
                    }
                    else{
                        showToast("User Created");
                        Intent i = new Intent(Signup.this, Login.class);
                        startActivity(i);
                    }
                }
                /*user = new Users("Salman", "Sajid", "sajidsalman75@gmail.com", "123456", "superadmin");
                db.insertUser(user);*/
            }
        });
    }

    public void loginButtonClicked(View view){
        Intent i = new Intent(Signup.this, Login.class);
        startActivity(i);
    }

    public void showToast(String message){
        Toast t1 = Toast.makeText(Signup.this, message, Toast.LENGTH_SHORT);
        t1.show();
    }
    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    private static boolean isEmpty(String array){
        return TextUtils.isEmpty(array);
    }
    private void showEmptyError(EditText et){
        et.setError("Please Fill this");
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
}