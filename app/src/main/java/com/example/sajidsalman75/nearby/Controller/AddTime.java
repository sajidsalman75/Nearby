package com.example.sajidsalman75.nearby.Controller;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
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
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.sajidsalman75.nearby.DBHandler.DatabaseHelper;
import com.example.sajidsalman75.nearby.Model.Time;
import com.example.sajidsalman75.nearby.R;

import java.util.Calendar;

/**
 * Created by sajidsalman75 on 12/12/2017.
 */

public class AddTime extends AppCompatActivity {

    Time time;
    DatabaseHelper db;
    SharedPreferences prefs;
    TextView etClosingTime, etOpeningTime;
    Calendar currentTime;
    int hour, min, openingTime, closingTime;
    String format;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_new_place_time);
        init();
        prefs = PreferenceManager.getDefaultSharedPreferences(AddTime.this);
        currentTime = Calendar.getInstance();
        hour = currentTime.get(Calendar.HOUR);
        min = currentTime.get(Calendar.MINUTE);

        selectedFormat(hour);
    }

    public int selectedFormat(int hour){
        if (hour == 0){
            hour += 12;
            format = "AM";
        }
        else if(hour == 12){
            format = "PM";
        }
        else if (hour < 12){
            format = "AM";
        }
        else{
            hour -= 12;
            format = "PM";
        }
        return hour;
    }

    public void etOpeningTimeClicked(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = selectedFormat(hourOfDay);
                time.hourToSec(hour, minute);
                prefs.edit().putInt("openingtime", time.getSec());
                etOpeningTime.setText(hourOfDay + " : " + minute + " " + format);
            }
        }, hour, min, true);
        timePickerDialog.show();
    }

    public void etClosingTimeClicked(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hour = selectedFormat(hourOfDay);
                time.hourToSec(hour, minute);
                prefs.edit().putInt("closingtime", time.getSec());
                etClosingTime.setText(hourOfDay + " : " + minute + " " + format);
            }
        }, hour, min, true);
        timePickerDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                prefs = PreferenceManager.getDefaultSharedPreferences(AddTime.this);
                if (item.getTitle().equals("Logout")){
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(getResources().getString(R.string.logoutString))
                            .setTitle(getResources().getString(R.string.logout));

                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // User clicked OK button
                            prefs.edit().putBoolean("Islogin", false).commit();
                            prefs.edit().putString("role", "").commit();
                            Intent i = new Intent(AddTime.this, Login.class);
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(AddTime.this);
        boolean isLogin = prefs.getBoolean("Islogin", false);
        if (isLogin){
            item = menu.findItem(R.id.action_logout);
            item.setTitle("Logout");
        }
        return true;
    }

    private void init(){
        etOpeningTime = findViewById(R.id.openingTime);
        etClosingTime = findViewById(R.id.closingTime);
        db = new DatabaseHelper(this);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        time = new Time();
    }
}