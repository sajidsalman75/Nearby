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
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Category;
import com.example.sajidsalman75.nearby.Model.City;
import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.Model.Subcategory;
import com.example.sajidsalman75.nearby.Model.Town;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/10/2017.
 */

public class Search extends AppCompatActivity{

    String itemCity, itemCategory, name, itemTown, itemSubcategory;
    DatabaseHelper db;
    EditText etName;
    ListView lv;
    int positionCity, positionTown, positionCategory, positionSubcategory;
    MyAdapter adapter;
    boolean flag = false;
    Cursor res = null, categoriesRes, citiesRes, subcategoriesRes, townsRes;
    String[] list, citiesArray, categoriesArray, subcategoriesArray, townsArray;
    Places[] places;
    ProgressDialog progress;
    Spinner spTown, spCategory, spSubcategory, spCity;
    SharedPreferences prefs;
    City[] cities;
    Category[] categories;
    Subcategory[] subcategories;
    Town[] towns;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(res !=  null){
                if(res.getCount() == 0) {
                    // show message
                    showMessage("Sorry","Nothing found");
                    lv.setAdapter(null);
                }
                else{

                    int i=0;
                    places = new Places[res.getCount()];
                    citiesArray = new String[res.getCount()];
                    categoriesArray = new String[res.getCount()];
                    subcategoriesArray = new String[res.getCount()];
                    townsArray = new String[res.getCount()];
                    while (res.moveToNext()){
                        places[i] = new Places(res.getString(1), res.getInt(2),
                                res.getInt(3), res.getInt(4),
                                res.getInt(5), res.getString(6), res.getInt(9));
                        places[i].setID(res.getInt(0));
                        places[i].setOPENINGTIME(res.getInt(7));
                        places[i].setCLOSINGTIME(res.getInt(8));

                        categoriesRes = db.getCategoryById(places[i].getCATID());
                        categoriesRes.moveToNext();
                        categoriesArray[i] = categoriesRes.getString(0);

                        citiesRes = db.getCityById(places[i].getCITYID());
                        citiesRes.moveToNext();
                        citiesArray[i] = citiesRes.getString(0);

                        subcategoriesRes = db.getSubcategoryByid(places[i].getSUBCATID());
                        subcategoriesRes.moveToNext();
                        subcategoriesArray[i] = subcategoriesRes.getString(0);

                        townsRes = db.getTownById(places[i].getTOWNID());
                        townsRes.moveToNext();
                        townsArray[i] = townsRes.getString(0);

                        i++;
                    }
                    adapter = new MyAdapter(Search.this,places, citiesArray, categoriesArray);
                    lv.setAdapter(adapter);
                    lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            prefs = PreferenceManager.getDefaultSharedPreferences(Search.this);
                            prefs.edit().putString("name", places[position].getNAME()).commit();
                            prefs.edit().putInt("cityid", places[position].getCITYID()).commit();
                            prefs.edit().putInt("townid", places[position].getTOWNID()).commit();
                            prefs.edit().putInt("subcategoryid", places[position].getSUBCATID()).commit();
                            prefs.edit().putInt("categoryid", places[position].getCATID()).commit();
                            prefs.edit().putString("address", places[position].getADDRESS()).commit();
                            prefs.edit().putInt("placeid", places[position].getID()).commit();
                            prefs.edit().putInt("closingtime", places[position].getCLOSINGTIME()).commit();
                            prefs.edit().putInt("openingtime", places[position].getOPENINGTIME()).commit();
                            prefs.edit().putString("city", citiesArray[position]);
                            Log.i("mine", citiesArray[position]);
                            prefs.edit().putString("category", categoriesArray[position]);
                            prefs.edit().putString("subcategory", subcategoriesArray[position]);
                            prefs.edit().putString("town", townsArray[position]);
                            Intent i = new Intent(Search.this, ShowPlaceInfo.class);
                            flag = true;
                            i.putExtra("city", citiesArray[position]);
                            i.putExtra("category", categoriesArray[position]);
                            i.putExtra("town", townsArray[position]);
                            i.putExtra("subcategory", subcategoriesArray[position]);
                            startActivity(i);
                        }
                    });
                }
            }
            progress.dismiss();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        etName = findViewById(R.id.etName);
        db = new DatabaseHelper(this);
        lv = findViewById(R.id.listview);
        spTown = findViewById(R.id.spTown);
        spSubcategory = findViewById(R.id.spSubCategory);
        spCity = findViewById(R.id.spCity);
        spCategory = findViewById(R.id.spCategory);

        res = db.getAllCitites();
        cities = new City[res.getCount()];
        list = new String[res.getCount()];

        res.moveToNext();

        for (int i = 0 ; i < res.getCount() ; i++){
            cities[i] = new City(res.getString(1));
            cities[i].setID(res.getInt(0));
            list[i] = res.getString(1);
            res.moveToNext();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);

        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemCity = parent.getItemAtPosition(position).toString();
                positionCity = position;

                res = db.getAllTown(cities[positionCity].getID());
                res.moveToNext();
                list = new String[res.getCount()];
                towns = new Town[res.getCount()];
                for (int i = 0 ; i < res.getCount() ; i++){
                    towns[i] = new Town(res.getString(1));
                    towns[i].setID(res.getInt(0));
                    list[i] = res.getString(1);
                    res.moveToNext();
                }
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(Search.this,
                        android.R.layout.simple_spinner_item, list);
                adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spTown.setAdapter(adapter3);
                spTown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        itemTown = parent.getItemAtPosition(position).toString();
                        positionTown = position;
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        res = db.getAllCategories();
        res.moveToNext();
        list = new String[res.getCount()];
        categories = new Category[res.getCount()];
        for (int i = 0 ; i < res.getCount() ; i++){
            categories[i] = new Category(res.getString(1));
            categories[i].setID(res.getInt(0));
            list[i] = res.getString(1);
            res.moveToNext();
        }

        spCategory = findViewById(R.id.spCategory);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter1);
        //list = null;
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemCategory = parent.getItemAtPosition(position).toString();
                positionCategory = position;

                res = db.getAllSubcategories(categories[positionCategory].getID());
                res.moveToNext();
                list = new String[res.getCount()];
                subcategories = new Subcategory[res.getCount()];
                for (int i = 0 ; i < res.getCount() ; i++){
                    subcategories[i] = new Subcategory(res.getString(1));
                    subcategories[i].setID(res.getInt(0));
                    list[i] = res.getString(1);
                    res.moveToNext();
                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(Search.this,
                            android.R.layout.simple_spinner_item, list);
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSubcategory.setAdapter(adapter4);
                    spSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            itemSubcategory = parent.getItemAtPosition(position).toString();
                            positionSubcategory = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onBackPressed(){
        prefs = PreferenceManager.getDefaultSharedPreferences(Search.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
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
        else{
            super.onBackPressed();
        }
    }

    public void searchButtonClicked(View view){
        name = etName.getText().toString();

        progress = new ProgressDialog(Search.this);
        progress.setTitle("Loading");
        progress.setMessage("Wait while loading...");
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                res = db.search(etName.getText().toString(), cities[positionCity].getID(), categories[positionCategory].getID(),
                        towns[positionTown].getID(), subcategories[positionSubcategory].getID());
                /*if (item.contains("-") && itemCategory.contains("-") && name.equals("")){
                    res = db.getAllPlaces();
                }
                else if (item.contains("-") && name.equals("") && !itemCategory.contains("-")){
                    res = db.showPlaces(null, null, itemCategory);
                }
                else if (item.contains("-") && !name.equals("") && itemCategory.contains("-")){
                    res = db.showPlaces(name, null, null);
                }
                else if (item.contains("-") && !name.equals("") && !itemCategory.contains("-")){
                    res = db.showPlaces(name, null, itemCategory);
                }
                else if (!item.contains("-") && name.equals("") && itemCategory.contains("-")){
                    res = db.showPlaces(null, item, null);
                }
                else if (!item.contains("-") && name.equals("") && !itemCategory.contains("-")){
                    res = db.showPlaces(null, item, itemCategory);
                }
                else if (!item.contains("-") && !name.equals("") && itemCategory.contains("-")){
                    res = db.showPlaces(name, item, null);
                }
                else if (!item.contains("-") && !name.equals("") && !itemCategory.contains("-")){
                    res = db.showPlaces(name, item, itemCategory);
                }*/
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
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(Search.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            item.setTitle("Login");
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
                else{
                    Intent i = new Intent(Search.this, Login.class);
                    //item.setTitle("Login");
                    startActivity(i);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(Search.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        MenuItem item;
        inflater.inflate(R.menu.action_bar, menu);
        item = menu.findItem(R.id.action_logout);
        if (isLogin){
            item.setTitle("Logout");
        }
        else{
            item.setTitle("Login");
        }
        return true;
    }
}