package com.example.sajidsalman75.nearby.Controller;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.Model.Time;
import com.example.sajidsalman75.nearby.R;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by sajidsalman75 on 11/13/2017.
 */

public class MyAdapter extends ArrayAdapter<Places>{
    private final Context context;
    private final Places[] values;
    private String[] cities, categories;
    int position1;

    public MyAdapter(Context context, Places[] values, String[] cities, String[] categories){
        super(context,-1, values);
        this.context = context;
        this.values = values;
        this.categories = categories;
        this.cities = cities;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        position1 = position;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        TextView tvName = rowView.findViewById(R.id.tvName);
        TextView tvCity = rowView.findViewById(R.id.tvCity);
        TextView tvStatus = rowView.findViewById(R.id.tvStatus);
        TextView tvCategory = rowView.findViewById(R.id.tvCategory);
        if (getStatus()){
            tvStatus.setText("Open");
            tvStatus.setAllCaps(true);
            tvStatus.setTextColor(Color.GREEN);
        }
        else{
            tvStatus.setText("Close");
            tvStatus.setAllCaps(true);
            tvStatus.setTextColor(Color.RED);
        }
        tvName.setText(values[position].getNAME());
        tvCity.setText(cities[position]);
        tvCategory.setText(categories[position]);
        // Change the icon for Windows and iPhone
        return rowView;
    }

    public boolean getStatus(){
        boolean flag = false;
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        Time time = new Time();
        time.hourToSec(hour, minute);
        if (values[position1].getOPENINGTIME() < time.getSec() && time.getSec() < values[position1].getCLOSINGTIME()){
            flag = true;
        }
        return flag;
    }
}
