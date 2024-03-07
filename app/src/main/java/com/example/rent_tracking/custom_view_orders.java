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

public class custom_view_orders extends BaseAdapter {
    String[]order_id,date,status,amount,store_name,payment_status;
    private Context context;
    SharedPreferences sh;
    String url;


    public custom_view_orders(Context applicationContext, String[] order_id, String[] date, String[] status, String[] amount, String[] store_name, String[] payment_status) {
        this.context = applicationContext;
        this.order_id = order_id;
        this.status = status;
        this.amount = amount;
        this.store_name = store_name;
        this.date = store_name;
        this.payment_status = payment_status;

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
            gridView=inflator.inflate(R.layout.activity_custom_view_orders,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1=(TextView)gridView.findViewById(R.id.textView65);
        TextView tv2=(TextView)gridView.findViewById(R.id.textView67);
        TextView tv3=(TextView)gridView.findViewById(R.id.textView69);
        TextView tv4=(TextView)gridView.findViewById(R.id.textView71);
        TextView tv5=(TextView)gridView.findViewById(R.id.textView75);
        Button b1 = (Button) gridView.findViewById(R.id.button18);
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pos = (int) view.getTag();
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed = sh.edit();
                ed.putString("order_id",order_id[pos]);
                ed.putString("oamount",amount[pos]);
                ed.commit();
                Intent i = new Intent(context,Make_payment.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);
            }
        });


        tv1.setTextColor(Color.BLACK);
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);



        tv1.setText(date[i]);
        tv2.setText(status[i]);
        tv3.setText(amount[i]);
        tv4.setText(store_name[i]);
        tv5.setText(payment_status[i]);





        return gridView;
    }
}