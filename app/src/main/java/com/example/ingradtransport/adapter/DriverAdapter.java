package com.example.ingradtransport.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.ingradtransport.model.User;

import java.util.List;

public class DriverAdapter extends ArrayAdapter<User> {

    private Context context;
    private List<User> drivers;
    private Typeface typeface;
    private float textSize;

    public DriverAdapter(@NonNull Context context, List<User> drivers, float textSize) {
        super(context, 0, drivers);
        this.context = context;
        this.drivers = drivers;
        //this.typeface = typeface;
        this.textSize = textSize;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_spinner_item, parent, false);
        }

        TextView textView = convertView.findViewById(android.R.id.text1);
        User driver = drivers.get(position);
        textView.setText(driver.getLastname() + " " + driver.getName() + " " + driver.getPatronymic());
        //textView.setTypeface(typeface);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return getView(position, convertView, parent);
    }

}
