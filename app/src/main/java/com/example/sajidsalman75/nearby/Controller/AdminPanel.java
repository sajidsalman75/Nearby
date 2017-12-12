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

import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/15/2017.
 */

public class AdminPanel extends AppCompatActivity {

    Button btnAdd, btnEdit, btnSearch, btnEditProfile;
    Intent i;
    SharedPreferences prefs;

    @Override
    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(AdminPanel.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            i = new Intent(AdminPanel.this, Search.class);
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
        setContentView(R.layout.admin_panel);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        btnAdd = findViewById(R.id.btnAddPlace);
        btnEdit = findViewById(R.id.btnEditPlace);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnSearch = findViewById(R.id.btnSearch);
    }

    public void btnSearchClicked(View view){
        i = new Intent(AdminPanel.this, Search.class);
        startActivity(i);
    }
    public void btnEditProfileClicked(View view){
        i = new Intent(AdminPanel.this, EditProfile.class);
        startActivity(i);
    }
    public void btnEditPlaceInformationClicked(View view){
        i = new Intent(AdminPanel.this, EditPlace.class);
        startActivity(i);
    }
    public void btnAddPlaceClicked(View view){
        i = new Intent(AdminPanel.this, AddPlace.class);
        startActivity(i);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(AdminPanel.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(AdminPanel.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AdminPanel.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }
}
