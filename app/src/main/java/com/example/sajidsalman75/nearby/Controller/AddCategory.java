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
 * Created by sajidsalman75 on 11/29/2017.
 */

public class AddCategory extends AppCompatActivity {
    SharedPreferences prefs;
    DatabaseHelper db;
    EditText etCategory;
    Button btnAddCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_category);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        db = new DatabaseHelper(this);
        etCategory = findViewById(R.id.etAddCategory);
        btnAddCategory = findViewById(R.id.btnAddCategory);
    }

    public void btnAddCategoryClicked(View view){
        if (etCategory.getText().toString().equals("")){
            etCategory.setError("Required!");
        }
        else{
            boolean flag = db.addCategory(etCategory.getText().toString());
            if (flag){
                Toast.makeText(getApplicationContext(), "Added", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Not Added", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(AddCategory.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(AddCategory.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddCategory.this);
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