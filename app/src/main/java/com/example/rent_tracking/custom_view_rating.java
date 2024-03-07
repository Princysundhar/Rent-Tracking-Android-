package com.example.rent_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class custom_view_rating extends BaseAdapter {
    String[]rid,rate,date,store_info;
    private Context context;


    public custom_view_rating(Context applicationContext, String[] rid, String[] rate, String[] date, String[] store_info) {
        this.context = applicationContext;
        this.rid = rid;
        this.rate = rate;
        this.date = date;
        this.store_info = store_info;

    }

    @Override
    public int getCount() {
        return store_info.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_view_rating,null);

        }
        else
        {
            gridView=(View)view;

        }
        RatingBar r1 = (RatingBar)gridView.findViewById(R.id.ratingBar);
        TextView tv1=(TextView)gridView.findViewById(R.id.textView36);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView38);


        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);


        tv1.setText(date[i]);
        tv2.setText(store_info[i]);
        r1.setRating(Float.parseFloat(rate[i]));
        r1.setEnabled(false);



        return gridView;
    }
}