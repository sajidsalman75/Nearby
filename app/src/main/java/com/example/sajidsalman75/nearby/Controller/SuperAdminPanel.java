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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/15/2017.
 */

public class SuperAdminPanel extends AppCompatActivity {
    EditText etEmail;
    DatabaseHelper db;
    SharedPreferences prefs;

    @Override
    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(SuperAdminPanel.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            Intent i = new Intent(SuperAdminPanel.this, Search.class);
            startActivity(i);
        }
    }

    @Override
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.super_admin_panel);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        db = new DatabaseHelper(this);
        etEmail = findViewById(R.id.etEmail);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(SuperAdminPanel.this);
                if (item.getTitle().equals("Logout")){

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(SuperAdminPanel.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SuperAdminPanel.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }

    public void btnGetAllAdminClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, GetAllAdmins.class);
        startActivity(i);
    }

    public void btnGetAllUserClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, GetAllUsers.class);
        startActivity(i);
    }

    public void btnSearchClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, Search.class);
        startActivity(i);
    }

    public void btnEditProfileClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, EditProfile.class);
        startActivity(i);
    }

    public void btnEditPlaceClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, EditPlace.class);
        startActivity(i);
    }

    public void btnAddPlaceClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, AddPlace.class);
        startActivity(i);
    }

    public void btnAddCityClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, AddCity.class);
        startActivity(i);
    }

    public void btnAddTownClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, AddTown.class);
        startActivity(i);
    }

    public void btnAddCategoryClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, AddCategory.class);
        startActivity(i);
    }

    public void btnAddSubcategoryClicked(View view){
        Intent i = new Intent(SuperAdminPanel.this, AddSubcategory.class);
        startActivity(i);
    }
}