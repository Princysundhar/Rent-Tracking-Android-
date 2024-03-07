package com.example.rent_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Custom_view_product extends BaseAdapter {
    String[] pid, product_name, date, image, category, amount, store_name, count;
    private Context context;

    public Custom_view_product(Context applicationContext, String[] pid, String[] product_name, String[] date, String[] image, String[] category, String[] amount, String[] store_name, String[] count) {
        this.context = applicationContext;
        this.pid = pid;
        this.product_name = product_name;
        this.date = date;
        this.image = image;
        this.category = category;
        this.amount = amount;
        this.store_name = store_name;
        this.count = count;

    }


    @Override
    public int getCount() {
        return product_name.length;
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
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_view_product, null);

        } else {
            gridView = (View) view;

        }
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView18);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView20);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView22);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView24);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView26);
        TextView tv6 = (TextView) gridView.findViewById(R.id.textView28);
        ImageView im = (ImageView) gridView.findViewById(R.id.imageView5);
        Button b1 = (Button) gridView.findViewById(R.id.button13);
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int)view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("pid",pid[pos]);
                ed.commit();
                Intent i =new Intent(context,add_quantity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });

        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);
        tv6.setTextColor(Color.BLACK);


        tv1.setText(product_name[i]);
        tv2.setText(date[i]);
        tv3.setText(category[i]);
        tv4.setText(amount[i]);
        tv5.setText(store_name[i]);
        tv6.setText(count[i]);


        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ipaddress = sh.getString("ipaddress", "");

        String url = "http://" + ipaddress + ":3000/static/Images/" + image[i] + ".jpg";


        Picasso.with(context).load(url).transform(new CircleTransform()).into(im);

        return gridView;
    }
}