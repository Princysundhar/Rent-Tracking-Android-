package com.example.rent_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class custom_view_store extends BaseAdapter {
    String[]sid,store_name,latitude,longitude,email,contact;
    private Context context;
    SharedPreferences sh;
    String url;


    public custom_view_store(Context applicationContext, String[] sid, String[] store_name, String[] latitude, String[] longitude, String[] email, String[] contact) {
        this.context = applicationContext;
        this.sid =sid;
        this.store_name = store_name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.email = email;
        this.contact = contact;

    }

    @Override
    public int getCount() {
        return store_name.length;
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
            gridView=inflator.inflate(R.layout.activity_custom_view_store,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView30);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView32);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView34);
        Button b1 = (Button)gridView.findViewById(R.id.button7);            // Locate
        Button b2 = (Button)gridView.findViewById(R.id.button8);            // Rating
        Button b3 = (Button)gridView.findViewById(R.id.button12);            // Product

        b1.setTag(i);
        b2.setTag(i);
        b3.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               int ik = (int)view.getTag();
               String url = "http://maps.google.com/?q=" + latitude[ik] + "," + longitude[ik];
               Intent i = new Intent(android.content.Intent.ACTION_VIEW,Uri.parse(url));
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
               context.startActivity(i);

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                int pos = (int)view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("sid",sid[pos]);
                ed.commit();
                Intent i = new Intent(context,view_rating.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos=(int) view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("sid",sid[pos]);
                ed.commit();
                Intent i = new Intent(context,view_product.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);


        tv1.setText(store_name[i]);
        tv2.setText(email[i]);
        tv3.setText(contact[i]);



        return gridView;
    }
}