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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.Model.Users;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/21/2017.
 */

public class GetAllAdmins extends AppCompatActivity{

    ListView lv;
    DatabaseHelper db;
    UserAdapter adapter;
    Cursor res;
    boolean flag = false;
    Users user;
    Users[] list;
    ProgressDialog progress;
    SharedPreferences prefs;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(res.getCount() == 0) {
                // show message
                showMessage("Sorry","Nothing found");
                lv.setAdapter(null);
            }
            else{
                list = new Users[res.getCount()];
                int i=0;
                while (res.moveToNext()){
                    list[i] = new Users(res.getString(1), res.getString(2),
                            res.getString(3), res.getString(4), res.getString(5));
                    list[i].setID(res.getInt(0));
                    i++;
                }
                adapter = new UserAdapter(GetAllAdmins.this,list);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GetAllAdmins.this);
                        prefs.edit().putString("firstname", list[position].getFIRSTNAME()).commit();
                        prefs.edit().putString("lastname", list[position].getLASTNAME()).commit();
                        prefs.edit().putString("email1", list[position].getEMAIL()).commit();
                        prefs.edit().putString("password1", list[position].getPASSWORD()).commit();
                        prefs.edit().putString("role", list[position].getROLE()).commit();
                        prefs.edit().putInt("userid", list[position].getID()).commit();
                        Intent i = new Intent(GetAllAdmins.this, AdminInfo.class);
                        flag = true;
                        startActivity(i);
                    }
                });
            }
            progress.dismiss();
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(GetAllAdmins.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            Intent i = new Intent(GetAllAdmins.this, Login.class);
            startActivity(i);
        }
        if (flag){
            res = db.getAllAdmins();
            if(res.getCount() == 0) {
                // show message
                showMessage("Sorry","Nothing found");
                lv.setAdapter(null);
                return;
            }
            else{
                list = new Users[res.getCount()];
                int i=0;
                while (res.moveToNext()){
                    list[i] = new Users(res.getString(1), res.getString(2),
                            res.getString(3), res.getString(4), res.getString(5));
                    list[i].setID(res.getInt(0));
                    i++;
                }
                adapter = new UserAdapter(this,list);
                lv.setAdapter(adapter);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.get_all_admins);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.listAdmins);

        progress = new ProgressDialog(GetAllAdmins.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                res = db.getAllAdmins();
                handler.sendEmptyMessage(0);
            }
        };

        Thread t1 = new Thread(r);
        t1.start();
    }

    public void showMessage(String title,String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(GetAllAdmins.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(GetAllAdmins.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(GetAllAdmins.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }
}