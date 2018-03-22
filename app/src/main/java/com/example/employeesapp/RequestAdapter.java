package com.example.employeesapp;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter<Request> {

public RequestAdapter(Context context, ArrayList<Request> request) {
        super(context, 0, request);
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Request request = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
        convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_request, parent, false);
        }
        // Lookup view for data population
        TextView roomnum = (TextView) convertView.findViewById(R.id.roomNumberTv);
        TextView req = (TextView) convertView.findViewById(R.id.requestTv);
        TextView dep = (TextView) convertView.findViewById(R.id.depTv);
        TextView time = (TextView) convertView.findViewById(R.id.timeTv);
        // Populate the data into the template view using the data object
        roomnum.setText(request.roomNum);
        req.setText(request.service);
        dep.setText(request.department);
        time.setText(request.time);
        // Return the completed view to render on screen
        return convertView;
        }
}