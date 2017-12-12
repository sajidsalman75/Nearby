package com.example.sajidsalman75.nearby.Controller;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.example.sajidsalman75.nearby.R;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import static android.text.TextUtils.isEmpty;

/**
 * Created by sajidsalman75 on 11/10/2017.
 */

public class AddPlace extends AppCompatActivity{

    final int REQUEST_CODE_GALLERY = 999;
    ImageView iv, iv2, iv3, iv4, iv5, selected;
    SharedPreferences prefs;
    long result = -1;
    boolean isInserted = false;
    Button btnAdd;
    EditText etName, etAddress;
    DatabaseHelper db;
    Places place;
    String itemCity = "", itemCategory = "", itemSubcategory = "", itemTown = "";
    TimePicker openingTime, closingTime;
    Spinner spCity, spTown, spCategory, spSubcategory;
    Cursor res;
    int positionCity, positionTown, positionCategory, positionSubCategory;
    City[] cities;
    String[] list;
    Category[] cateogries;
    Subcategory[] subCategories;
    Town[] towns;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(result == -1){
                showToast("Not Added");
            }
            else{
                showToast("Place Added");
            }
        }
    };

    @Override
    protected void onStart(){
        super.onStart();
        prefs = PreferenceManager.getDefaultSharedPreferences(AddPlace.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (!isLogin){
            Intent i = new Intent(AddPlace.this, Login.class);
            startActivity(i);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_place);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        db = new DatabaseHelper(this);
        etName = findViewById(R.id.etName);
        etAddress = findViewById(R.id.etAddress);
        btnAdd = findViewById(R.id.btnAdd);
        openingTime = findViewById(R.id.timePicker);
        openingTime.setIs24HourView(true);
        closingTime = findViewById(R.id.timePicker1);
        closingTime.setIs24HourView(true);
        spTown = findViewById(R.id.spTown);
        spSubcategory = findViewById(R.id.spSubCategory);
        iv = findViewById(R.id.iv);
        iv2 = findViewById(R.id.iv2);
        iv3 = findViewById(R.id.iv3);
        iv4 = findViewById(R.id.iv4);
        iv5 = findViewById(R.id.iv5);
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = findViewById(R.id.iv);
                ActivityCompat.requestPermissions(
                        AddPlace.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = findViewById(R.id.iv2);
                ActivityCompat.requestPermissions(
                        AddPlace.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = findViewById(R.id.iv3);
                ActivityCompat.requestPermissions(
                        AddPlace.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = findViewById(R.id.iv4);
                ActivityCompat.requestPermissions(
                        AddPlace.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = findViewById(R.id.iv5);
                ActivityCompat.requestPermissions(
                        AddPlace.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

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

        spCity = findViewById(R.id.spCity);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCity.setAdapter(adapter);

        //list = null;
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
                ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(AddPlace.this,
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

        //list = null;
        res = db.getAllCategories();
        res.moveToNext();
        list = new String[res.getCount()];
        cateogries = new Category[res.getCount()];
        for (int i = 0 ; i < res.getCount() ; i++){
            cateogries[i] = new Category(res.getString(1));
            cateogries[i].setID(res.getInt(0));
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

                res = db.getAllSubcategories(cateogries[positionCategory].getID());
                res.moveToNext();
                list = new String[res.getCount()];
                subCategories = new Subcategory[res.getCount()];
                for (int i = 0 ; i < res.getCount() ; i++){
                    subCategories[i] = new Subcategory(res.getString(1));
                    subCategories[i].setID(res.getInt(0));
                    list[i] = res.getString(1);
                    res.moveToNext();
                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(AddPlace.this,
                            android.R.layout.simple_spinner_item, list);
                    adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spSubcategory.setAdapter(adapter4);
                    spSubcategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            itemSubcategory = parent.getItemAtPosition(position).toString();
                            positionSubCategory = position;
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

    public void AddButtonClicked(View view){

        boolean flag = true;
        if(isEmpty(etName.getText().toString())){
            flag = false;
            showEmptyError(etName);
        }
        if (itemCity.equals("")){
            flag = false;
        }
        if (itemCategory.equals("")){
            flag = false;
        }
        if (isEmpty(etAddress.getText().toString())){
            flag = false;
            showEmptyError(etAddress);
        }

        if (flag){
            prefs = PreferenceManager.getDefaultSharedPreferences(AddPlace.this);
            String id1 = prefs.getString("id", "none");
            int id = Integer.parseInt(id1);
            Time time1 = new Time();
            time1.hourToSec(openingTime.getCurrentHour(), openingTime.getCurrentMinute());
            place = new Places(etName.getText().toString(), cities[positionCity].getID(), towns[positionTown].getID(),
                    cateogries[positionCategory].getID(),subCategories[positionSubCategory].getID(),
                    etAddress.getText().toString(), id);
            place.setOPENINGTIME(time1.getSec());
            time1.hourToSec(closingTime.getCurrentHour(), closingTime.getCurrentMinute());
            place.setCLOSINGTIME(time1.getSec());
            boolean count = db.searchIfExistPlace(place, cities[positionCity].getNAME(), cateogries[positionCategory].getNAME());
            if (count){
                flag = false;
                Toast.makeText(getApplicationContext(), "Place Already Exist", Toast.LENGTH_SHORT).show();
            }
        }
        if (flag){
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    result = db.insertPlace(place);
                    if (result != -1){
                        if (iv.getDrawable() != null){
                            db.insertImage(imageViewToByte(iv), result);
                        }
                        if (iv2.getDrawable() != null){
                            db.insertImage(imageViewToByte(iv), result);
                        }
                        if (iv3.getDrawable() != null){
                            db.insertImage(imageViewToByte(iv), result);
                        }
                        if (iv4.getDrawable() != null){
                            db.insertImage(imageViewToByte(iv), result);
                        }
                        if (iv5.getDrawable() != null){
                            db.insertImage(imageViewToByte(iv), result);
                        }
                    }
                    handler.sendEmptyMessage(0);
                }

            };

            Thread t1 = new Thread(r);
            t1.start();
        }
    }

    public void showToast(String message){
        Toast t1 = Toast.makeText(AddPlace.this, message, Toast.LENGTH_SHORT);
        t1.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(AddPlace.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(AddPlace.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddPlace.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }

    private void showEmptyError(EditText et){
        et.setError("Please Fill this");
    }

    public static byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable)image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == REQUEST_CODE_GALLERY){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            }
            else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                selected.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

}