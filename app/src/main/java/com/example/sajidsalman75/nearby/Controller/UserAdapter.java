package com.example.sajidsalman75.nearby.Controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.sajidsalman75.nearby.Model.Places;
import com.example.sajidsalman75.nearby.Model.Users;
import com.example.sajidsalman75.nearby.R;

/**
 * Created by sajidsalman75 on 11/21/2017.
 */

public class UserAdapter extends ArrayAdapter<Users>{
    private final Context context;
    private final Users[] values;

    public UserAdapter(Context context, Users[] values){
        super(context,-1, values);
        this.context = context;
        this.values = values;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_admin_layout, parent, false);
        TextView tvName = rowView.findViewById(R.id.tvName);
        TextView tvEmail = rowView.findViewById(R.id.tvEmail);
        tvName.setText(values[position].getFIRSTNAME() + " " + values[position].getLASTNAME());
        tvEmail.setText(values[position].getEMAIL());
        return rowView;
    }
}
