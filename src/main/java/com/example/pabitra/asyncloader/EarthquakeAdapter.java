package com.example.pabitra.asyncloader;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Created by PABITRA on 11/14/2016.
 */
public class EarthquakeAdapter extends ArrayAdapter<Earthquake> {

    private Activity activity;
    private ArrayList<Earthquake> arrayList;

    public EarthquakeAdapter(Context context, ArrayList<Earthquake> arrayLists){
        super(context, R.layout.earthquake_list_item, arrayLists);
        activity = (Activity)context;
        arrayList = arrayLists;
    }

    private class ViewHolder {
        private TextView magnitude;
        private TextView place;
        private TextView time;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        Earthquake earthquake = arrayList.get(position);

        if (convertView == null){
           convertView = activity.getLayoutInflater().inflate(R.layout.earthquake_list_item, null);
            holder = new ViewHolder();
            holder.magnitude = (TextView)convertView.findViewById(R.id.magnitude);
            holder.place = (TextView)convertView.findViewById(R.id.place);
            holder.time = (TextView)convertView.findViewById(R.id.time);

            convertView.setTag(holder);
        } else {
          holder = (ViewHolder)convertView.getTag();
        }

        holder.magnitude.setText(earthquake.getMagnitude());
        holder.place.setText(earthquake.getPlace());
        holder.time.setText(earthquake.getDate());
        GradientDrawable gradientDrawable = (GradientDrawable)holder.magnitude.getBackground();
        gradientDrawable.setColor(getDrwableColor(Double.parseDouble(earthquake.getMagnitude())));

        return convertView;
    }

    public int getDrwableColor(Double magnitude) {
        int mag = (int)Math.floor(magnitude);
        int colorResourceID = 0;
        switch (mag){
            case 1:
            case 2:
            case 3:
                colorResourceID = R.color.magnitude_5;
                break;

            case 4:
                colorResourceID = R.color.magnitude_4;
                break;

            case 5:
                colorResourceID = R.color.magnitude_3;
                break;

            case 6:
                colorResourceID = R.color.magnitude_2;
                break;

            case 7:
            case 8:
            case 9:
                colorResourceID = R.color.magnitude_1;
                break;
        }

        return ContextCompat.getColor(activity, colorResourceID);
    }
}
