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
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/23/2017.
 */

public class ShowPlaceInfo extends AppCompatActivity {

    TextView tvName, tvCity, tvCategory, tvAddress, tvPlaceCity, tvPlaceCategory, tvPlaceAddress;
    int id;
    Places place;
    SharedPreferences prefs;
    String city, category, town, subcategory;
    Bitmap[] bitmaps;

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_place_info);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        DatabaseHelper db = new DatabaseHelper(this);
        TextView tvTown, tvPlaceTown, tvSubcategory, tvPlaceSubcategory;
        tvTown = findViewById(R.id.tvTown);
        tvPlaceTown = findViewById(R.id.tvPlaceTown);
        tvSubcategory = findViewById(R.id.tvSubcategory);
        tvPlaceSubcategory = findViewById(R.id.tvPlaceSubcategory);
        tvName = findViewById(R.id.tvName);
        tvCity = findViewById(R.id.tvCity);
        tvCategory = findViewById(R.id.tvCategory);
        tvAddress = findViewById(R.id.tvAddress);
        tvPlaceCity = findViewById(R.id.tvPlaceCity);
        tvPlaceCategory = findViewById(R.id.tvPlaceCategory);
        tvPlaceAddress = findViewById(R.id.tvPlaceAddress);
        ViewPager vp = findViewById(R.id.vp);
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
        tvPlaceTown.setText(town);
        tvPlaceSubcategory.setText(subcategory);
        tvName.setText(place.getNAME());
        tvPlaceCity.setText(city);
        tvPlaceCategory.setText(category);
        tvPlaceAddress.setText(place.getADDRESS());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(ShowPlaceInfo.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
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
                    Intent i = new Intent(ShowPlaceInfo.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ShowPlaceInfo.this);
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

    private void collectData(){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(ShowPlaceInfo.this);
        place = new Places(prefs.getString("name", "none"),
                prefs.getInt("cityid", 0),
                prefs.getInt("townid", 0),
                prefs.getInt("categoryid", 0),
                prefs.getInt("subcategoryid", 0),
                prefs.getString("address", "none"),
                Integer.parseInt(prefs.getString("id", "none")));
        Intent i = getIntent();
        city = i.getStringExtra("city");
        category = i.getStringExtra("category");
        town = i.getStringExtra("town");
        subcategory = i.getStringExtra("subcategory");
        id = prefs.getInt("placeid", 0);
        place.setID(prefs.getInt("placeid", 0));
    }
}