package com.example.sajidsalman75.nearby.Controller;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Category;
import com.example.sajidsalman75.nearby.Model.City;
import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.Model.Subcategory;
import com.example.sajidsalman75.nearby.Model.Time;
import com.example.sajidsalman75.nearby.Model.Town;
import com.example.sajidsalman75.nearby.Model.Users;
import com.example.sajidsalman75.nearby.R;

import static android.text.TextUtils.isEmpty;

/**
 * Created by sajidsalman75 on 11/17/2017.
 */

public class EditPlaceInformation extends AppCompatActivity{
    EditText etName, etAddress;
    Button btnUpdate;
    DatabaseHelper db;
    Time time;
    Places place;
    int id, i;
    TimePicker openingTime, closingTime;
    String itemCity, itemCategory, itemSubCategory, itemTown;
    SharedPreferences prefs;
    Cursor res;
    String[] list;
    City[] cities;
    Category[] categories;
    Subcategory[] subcategories;
    Town[] towns;
    Bitmap[] bitmaps;
    int positionCity, positionCategory, positionTown, positionSubcategory;


    @Override
    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(EditPlaceInformation.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            Intent i = new Intent(EditPlaceInformation.this, Login.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_place_information);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        btnUpdate = findViewById(R.id.btnUpdate);
        openingTime = findViewById(R.id.timePicker);
        openingTime.setIs24HourView(true);
        closingTime = findViewById(R.id.timePicker1);
        closingTime.setIs24HourView(true);
        ViewPager vp = findViewById(R.id.vp);
        db = new DatabaseHelper(this);
        collectData();
        Cursor imageRes = db.getImage(place.getID());
        int i = 0;
        bitmaps = new Bitmap[imageRes.getCount()];
        while (imageRes.moveToNext()){
            byte[] image = imageRes.getBlob(1);
            bitmaps[i] = BitmapFactory.decodeByteArray(image, 0, image.length);
            i++;
        }
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this, bitmaps);
        vp.setAdapter(viewPagerAdapter);
        time = new Time();
        time.secToHours(place.getOPENINGTIME());
        openingTime.setCurrentHour(time.getHour());
        openingTime.setCurrentMinute(time.getMin());
        time.secToHours(place.getCLOSINGTIME());
        closingTime.setCurrentMinute(time.getMin());
        closingTime.setCurrentHour(time.getHour());

        etName.setText(place.getNAME());
        etAddress.setText(place.getADDRESS());

        res = db.getAllCitites();
        res.moveToNext();
        cities = new City[res.getCount()];
        list = new String[res.getCount()];
        prefs = PreferenceManager.getDefaultSharedPreferences(EditPlaceInformation.this);
        for (i = 0 ; i < res.getCount() ; i++){
            cities[i] = new City(res.getString(1));
            cities[i].setID(res.getInt(0));
            list[i] = res.getString(1);
            if (res.getInt(0) == place.getCITYID()){
                positionCity = i;
            }
            res.moveToNext();
        }

        Spinner spCity = findViewById(R.id.spCity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemCity = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCity.setSelection(positionCity);

        res = db.getAllCategories();
        res.moveToNext();
        list = new String[res.getCount()];
        categories = new Category[res.getCount()];
        for (i = 0 ; i < res.getCount() ; i++){
            categories[i] = new Category(res.getString(1));
            categories[i].setID(res.getInt(0));
            list[i] = res.getString(1);
            if (res.getInt(0) == place.getCATID()){
                positionCategory = i;
            }
            res.moveToNext();
        }

        Spinner spCategory = findViewById(R.id.spCategory);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter1);
        spCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemCategory = parent.getItemAtPosition(position).toString();
                positionCategory = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spCategory.setSelection(positionCategory);


        res = db.getAllTown(place.getCITYID());
        res.moveToNext();
        list = new String[res.getCount()];
        towns = new Town[res.getCount()];
        for (i = 0 ; i < res.getCount() ; i++){
            towns[i] = new Town(res.getString(1));
            towns[i].setID(res.getInt(0));
            list[i] = res.getString(1);
            if (res.getInt(0) == place.getTOWNID()){
                positionTown = i;
            }
            res.moveToNext();
        }

        Spinner spTown = findViewById(R.id.spTown);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTown.setAdapter(adapter2);
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
        spTown.setSelection(positionTown);

        res = db.getAllSubcategories(place.getCATID());
        res.moveToNext();
        list = new String[res.getCount()];
        subcategories = new Subcategory[res.getCount()];
        for (i = 0 ; i < res.getCount() ; i++){
            subcategories[i] = new Subcategory(res.getString(1));
            subcategories[i].setID(res.getInt(0));
            list[i] = res.getString(1);
            if (res.getInt(0) == place.getSUBCATID()){
                positionSubcategory = i;
            }
            res.moveToNext();
        }

        Spinner spSubcategory = findViewById(R.id.spSubCategory);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spSubcategory.setAdapter(adapter3);
        spSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSubCategory = parent.getItemAtPosition(position).toString();
                positionSubcategory = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spSubcategory.setSelection(positionSubcategory);
    }

    private void collectData(){
        prefs = PreferenceManager.getDefaultSharedPreferences(EditPlaceInformation.this);
        place = new Places(prefs.getString("name", "none"),
                prefs.getInt("cityid", 0),
                prefs.getInt("townid", 0),
                prefs.getInt("categoryid", 0),
                prefs.getInt("subcategoryid", 0),
                prefs.getString("address", ""),
                Integer.parseInt(prefs.getString("id", "none")));
        id = prefs.getInt("placeid", 0);
        place.setCLOSINGTIME(prefs.getInt("closingtime", 0));
        place.setOPENINGTIME(prefs.getInt("openingtime", 0));
        place.setID(id);
    }

    public void updateButtonClicked(View view){
        boolean flag = true;
        if(isEmpty(etName.getText().toString())){
            flag = false;
            showEmptyError(etName);
        }
        if (isEmpty(etAddress.getText().toString())){
            flag = false;
            showEmptyError(etAddress);
        }
        if(itemCity.contains("-")){
            flag = false;
        }
        if (itemCategory.contains("-")){
            flag = false;
        }
        if (flag){
            place.setNAME(etName.getText().toString());
            place.setCITYID(cities[positionCity].getID());
            place.setCATID(categories[positionCategory].getID());
            place.setADDRESS(etAddress.getText().toString());
            place.setID(id);
            time.hourToSec(openingTime.getCurrentHour(), openingTime.getCurrentMinute());
            place.setOPENINGTIME(time.getSec());
            time.hourToSec(closingTime.getCurrentHour(), closingTime.getCurrentMinute());
            place.setCLOSINGTIME(time.getSec());
            place.setTOWNID(towns[positionTown].getID());
            place.setSUBCATID(subcategories[positionSubcategory].getID());
            boolean result = db.updatePlaceInfo(place);
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

    public void btnDeleteClicked(View view){
        int del = db.deletePlace(Integer.toString(id));
        if (del > 0){
            Toast.makeText(getApplicationContext(), "Deleted", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Not Deleted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(EditPlaceInformation.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(EditPlaceInformation.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(EditPlaceInformation.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }
}