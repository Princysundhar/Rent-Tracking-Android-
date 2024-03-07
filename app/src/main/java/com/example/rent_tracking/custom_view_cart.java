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
import android.widget.ListView;
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

public class custom_view_cart extends BaseAdapter {
    String[]cart_id,product_name,date,image,category,amount,store_info,count;
    private Context context;
    SharedPreferences sh;
    String url;

    public custom_view_cart(Context applicationContext, String[] cart_id, String[] product_name, String[] date,String[]image, String[] category, String[] amount, String[] store_info, String[] count) {
        this.context = applicationContext;
        this.cart_id = cart_id;
        this.product_name = product_name;
        this.date = date;
        this.image = image;
        this.category = category;
        this.amount = amount;
        this.store_info = store_info;
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
        LayoutInflater inflator=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if(view==null)
        {
            gridView=new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView=inflator.inflate(R.layout.activity_custom_view_cart,null);

        }
        else
        {
            gridView=(View)view;

        }
        TextView tv1 = (TextView)gridView.findViewById(R.id.textView52);
        TextView tv2 = (TextView)gridView.findViewById(R.id.textView54);
        TextView tv3 = (TextView)gridView.findViewById(R.id.textView56);
        TextView tv4 = (TextView)gridView.findViewById(R.id.textView58);
        TextView tv5 = (TextView)gridView.findViewById(R.id.textView60);
        TextView tv6 = (TextView)gridView.findViewById(R.id.textView62);
        ImageView img = (ImageView) gridView.findViewById(R.id.imageView6);
        Button b1 = (Button)gridView.findViewById(R.id.button15);
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos = (int)view.getTag();

                sh = PreferenceManager.getDefaultSharedPreferences(context);
                sh.getString("ipaddress","");
                url = sh.getString("url","") + "android_cancel_order";

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                // response
                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(context, "Order cancelled", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context.getApplicationContext(),view_cart.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(i);

                                    }


                                    // }
                                    else {
                                        Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
                                    }

                                }    catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();

//                        String id=sh.getString("uid","");
                        params.put("cart_id",cart_id[pos]);
//                params.put("mac",maclis);

                        return params;
                    }
                };

                int MY_SOCKET_TIMEOUT_MS=100000;

                postRequest.setRetryPolicy(new DefaultRetryPolicy(
                        MY_SOCKET_TIMEOUT_MS,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(postRequest);


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
        tv5.setText(store_info[i]);
        tv6.setText(count[i]);


        SharedPreferences sh= PreferenceManager.getDefaultSharedPreferences(context);
        String ipaddress = sh.getString("ipaddress","");

        String url="http://" + ipaddress + ":3000"+image[i];


        Picasso.with(context).load(url).transform(new CircleTransform()). into(img);

        return gridView;
    }
}