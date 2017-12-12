package com.example.sajidsalman75.nearby.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Users;
import com.example.sajidsalman75.nearby.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by sajidsalman75 on 11/16/2017.
 */

public class EditProfile extends AppCompatActivity {

    EditText etFname, etLname, etEmail, etPassword;
    Button btnUpdate;
    DatabaseHelper db;
    Users user;
    Intent i;
    String id;
    SharedPreferences prefs;

    @Override
    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            i = new Intent(EditProfile.this, Login.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        collectData();
        db = new DatabaseHelper(this);
        etFname = findViewById(R.id.etfname);
        etFname.setText(user.getFIRSTNAME());
        etLname = findViewById(R.id.etlname);
        etLname.setText(user.getLASTNAME());
        etEmail = findViewById(R.id.etEmail);
        etEmail.setText(user.getEMAIL());
        etPassword = findViewById(R.id.etPassword);
        etPassword.setText(user.getPASSWORD());
        btnUpdate = findViewById(R.id.btnUpdate);
    }

    private void collectData(){
        prefs = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
        user = new Users(prefs.getString("fname", "none"),
                prefs.getString("lname", "none"),
                prefs.getString("email", "none"),
                prefs.getString("password", "none"),
                prefs.getString("role", "none"));
        id = prefs.getString("id", "none");
    }

    public void updateButtonClicked(View view){
        boolean flag = true;
        if(isEmpty(etEmail.getText().toString())){
            flag = false;
            showEmptyError(etEmail);
        }
        if (isEmpty(etFname.getText().toString())){
            flag = false;
            showEmptyError(etFname);
        }
        if (isEmpty(etLname.getText().toString())){
            flag = false;
            showEmptyError(etLname);
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
            user.setFIRSTNAME(etFname.getText().toString());
            user.setLASTNAME(etLname.getText().toString());
            user.setEMAIL(etEmail.getText().toString());
            user.setPASSWORD(etPassword.getText().toString());
            boolean result = db.updateUserInfo(id, user);
            if (result){
                Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getApplicationContext(), "Not Updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void showEmptyError(EditText et){
        et.setError("Please Fill this");
    }

    private static boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(EditProfile.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditProfile.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }
}