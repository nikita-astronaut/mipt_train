package ru.rkarasev.miptrain.gui;

import java.util.ArrayList;

import ru.rkarasev.miptrain.R;
import ru.rkarasev.miptrain.utils.FinalData;
import ru.rkarasev.miptrain.utils.Utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils.TruncateAt;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomArrayAdapter extends ArrayAdapter<FinalData> {
    private final LayoutInflater mInflater;
 
    public CustomArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_2);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
 
    public void setData(ArrayList<FinalData> data) {
        clear();
        if (data != null) {
            for (FinalData appEntry : data) {
                add(appEntry);
            }
        }
    }
    
    @Override 
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
 
        if (convertView == null) {
            view = mInflater.inflate(R.layout.single_item, parent, false);
        } else {
            view = convertView;
        }

        FinalData item = getItem(position);
        if (position % 2 == 0) {
        	((TextView)view.findViewById(R.id.train_info)).setEllipsize(TruncateAt.MARQUEE);
        	((TextView)view.findViewById(R.id.train_info)).setSelected(true);
        	((TextView)view.findViewById(R.id.stations_info)).setEllipsize(TruncateAt.MARQUEE);
        	((TextView)view.findViewById(R.id.stations_info)).setSelected(true);
        	((TextView)view.findViewById(R.id.stations_info)).setText(item.getFullData());
        	((AutoResizeTextView)view.findViewById(R.id.time_info)).setText(Utils.remainingTimeFormat(item.getShortData()));
        	((TextView)view.findViewById(R.id.train_info)).setText(item.getTimeData());
        	
        	((TextView)view.findViewById(R.id.train_info)).setBackgroundColor(Color.parseColor("#8ad5f0"));
        	((TextView)view.findViewById(R.id.stations_info)).setBackgroundColor(Color.parseColor("#c5eaf8"));
        	((AutoResizeTextView)view.findViewById(R.id.time_info)).setBackgroundColor(Color.parseColor("#8ad5f0"));
        } else {
        	((TextView)view.findViewById(R.id.train_info)).setEllipsize(TruncateAt.MARQUEE);
        	((TextView)view.findViewById(R.id.train_info)).setSelected(true);
        	((TextView)view.findViewById(R.id.stations_info)).setEllipsize(TruncateAt.MARQUEE);
        	((TextView)view.findViewById(R.id.stations_info)).setSelected(true);
        	((TextView)view.findViewById(R.id.stations_info)).setText(item.getFullData());
        	((AutoResizeTextView)view.findViewById(R.id.time_info)).setText(Utils.remainingTimeFormat(item.getShortData()));
        	((TextView)view.findViewById(R.id.train_info)).setText(item.getTimeData());
        	
        	((TextView)view.findViewById(R.id.train_info)).setBackgroundColor(Color.parseColor("#c5e26d"));
        	((TextView)view.findViewById(R.id.stations_info)).setBackgroundColor(Color.parseColor("#e2f0b6"));
        	((AutoResizeTextView)view.findViewById(R.id.time_info)).setBackgroundColor(Color.parseColor("#c5e26d"));
        }
        return view;
    }
} 
