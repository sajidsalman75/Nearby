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
import android.widget.Toast;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/17/2017.
 */

public class EditPlace extends AppCompatActivity {
    ListView lv;
    DatabaseHelper db;
    MyAdapter adapter;
    Cursor res, categoriesRes, citiesRes;
    boolean flag = false;
    Places[] list;
    String[] cities, categories;
    Intent i;
    ProgressDialog progress;
    String id;
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
                list = new Places[res.getCount()];
                cities = new String[res.getCount()];
                categories = new String[res.getCount()];
                int i=0;
                while (res.moveToNext()){
                    list[i] = new Places(res.getString(1), res.getInt(2),
                            res.getInt(3), res.getInt(4),
                            res.getInt(5), res.getString(6), res.getInt(9));
                    list[i].setID(res.getInt(0));
                    list[i].setOPENINGTIME(res.getInt(7));
                    list[i].setCLOSINGTIME(res.getInt(8));
                    categoriesRes = db.getCategoryById(list[i].getCATID());
                    categoriesRes.moveToNext();
                    categories[i] = categoriesRes.getString(0);
                    citiesRes = db.getCityById(list[i].getCITYID());
                    citiesRes.moveToNext();
                    cities[i] = citiesRes.getString(0);
                    i++;
                }
                adapter = new MyAdapter(EditPlace.this,list, cities, categories);
                lv.setAdapter(adapter);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        prefs = PreferenceManager.getDefaultSharedPreferences(EditPlace.this);
                        prefs.edit().putString("name", list[position].getNAME()).commit();
                        prefs.edit().putInt("cityid", list[position].getCITYID()).commit();
                        prefs.edit().putInt("townid", list[position].getTOWNID()).commit();
                        prefs.edit().putInt("subcategoryid", list[position].getSUBCATID()).commit();
                        prefs.edit().putInt("categoryid", list[position].getCATID()).commit();
                        prefs.edit().putString("address", list[position].getADDRESS()).commit();
                        prefs.edit().putInt("placeid", list[position].getID()).commit();
                        prefs.edit().putInt("closingtime", list[position].getCLOSINGTIME()).commit();
                        prefs.edit().putInt("openingtime", list[position].getOPENINGTIME()).commit();
                        Intent i = new Intent(EditPlace.this, EditPlaceInformation.class);
                        flag = true;
                        startActivity(i);
                    }
                });
            }
            progress.dismiss();
        }
    };

    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(EditPlace.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            i = new Intent(EditPlace.this, Login.class);
            startActivity(i);
        }
        if (flag){
            if (prefs.getString("role", "none").equals("superadmin")){
                res = db.getAllPlaces();
            }
            else{
                res = db.adminWisePlaces(Integer.parseInt(id));
            }
            if(res.getCount() == 0) {
                // show message
                showMessage("Sorry","Nothing found");
                lv.setAdapter(null);
                return;
            }
            else{
                list = new Places[res.getCount()];
                cities = new String[res.getCount()];
                categories = new String[res.getCount()];
                int i=0;
                while (res.moveToNext()){
                    list[i] = new Places(res.getString(1), res.getInt(2),
                            res.getInt(3), res.getInt(4),
                            res.getInt(5), res.getString(6), res.getInt(9));
                    list[i].setID(res.getInt(0));
                    list[i].setOPENINGTIME(res.getInt(7));
                    list[i].setCLOSINGTIME(res.getInt(8));
                    categoriesRes = db.getCategoryById(list[i].getCATID());
                    categoriesRes.moveToNext();
                    categories[i] = categoriesRes.getString(0);
                    citiesRes = db.getCityById(list[i].getCITYID());
                    citiesRes.moveToNext();
                    cities[i] = citiesRes.getString(0);
                    i++;
                }
                adapter = new MyAdapter(this,list, cities, categories);
                lv.setAdapter(adapter);
            }
        }
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_place);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.listview);

        progress = new ProgressDialog(EditPlace.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();
        prefs = PreferenceManager.getDefaultSharedPreferences(EditPlace.this);
        id = prefs.getString("id", "none");
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (prefs.getString("role", "none").equals("superadmin")){
                    res = db.getAllPlaces();
                }
                else{
                    res = db.adminWisePlaces(Integer.parseInt(id));
                }
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
                prefs = PreferenceManager.getDefaultSharedPreferences(EditPlace.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(EditPlace.this, Login.class);
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
        prefs = PreferenceManager.getDefaultSharedPreferences(EditPlace.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }
}