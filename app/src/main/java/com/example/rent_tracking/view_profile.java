package com.example.rent_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class view_profile extends AppCompatActivity {
    TextView tv1, tv2, tv3, tv4, tv5, tv6;
    Button b1;
    ImageView img;
    SharedPreferences sh;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        tv1 = findViewById(R.id.textView6);
        tv2 = findViewById(R.id.textView8);
        tv3 = findViewById(R.id.textView10);
        tv4 = findViewById(R.id.textView12);
        tv5 = findViewById(R.id.textView14);
        tv6 = findViewById(R.id.textView16);
        img = findViewById(R.id.imageView3);
        b1 = findViewById(R.id.button5);


        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ipaddress", "");
        url = sh.getString("url", "") + "android_view_profile";

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                        // response
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                tv1.setText(jsonObj.getString("name"));
                                tv2.setText(jsonObj.getString("place"));
                                tv3.setText(jsonObj.getString("post"));
                                tv4.setText(jsonObj.getString("pin"));
                                tv5.setText(jsonObj.getString("email"));
                                tv6.setText(jsonObj.getString("contact"));



                                String image = jsonObj.getString("photo");
                                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String ipaddress = sh.getString("ipaddress", "");
                                String url = "http://" + ipaddress + ":1234" + image;
                                Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).into(img);//circle
                                img.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                        String ipaddress = sh.getString("ipaddress", "");
                                        String url = "http://" + ipaddress + ":1234" + image;
                                        openFile(url);

                                    }

                                    public void openFile(String url) {

                                        Uri uri = Uri.parse(url);

                                        Intent intent = new Intent(Intent.ACTION_VIEW);
                                        if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                                            // Word document
                                            intent.setDataAndType(uri, "application/msword");
                                        } else if (url.toString().contains(".pdf")) {
                                            // PDF file
                                            intent.setDataAndType(uri, "application/pdf");
                                        } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                                            // Powerpoint file
                                            intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
                                        } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                                            // Excel file
                                            intent.setDataAndType(uri, "application/vnd.ms-excel");
                                        } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                                            // WAV audio file
                                            intent.setDataAndType(uri, "application/x-wav");
                                        } else if (url.toString().contains(".rtf")) {
                                            // RTF file
                                            intent.setDataAndType(uri, "application/rtf");
                                        } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                                            // WAV audio file
                                            intent.setDataAndType(uri, "audio/x-wav");
                                        } else if (url.toString().contains(".gif")) {
                                            // GIF file
                                            intent.setDataAndType(uri, "image/gif");
                                        } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                                            // JPG file
                                            intent.setDataAndType(uri, "image/jpeg");
                                        } else if (url.toString().contains(".txt")) {
                                            // Text file
                                            intent.setDataAndType(uri, "text/plain");
                                        } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                                            // Video files
                                            intent.setDataAndType(uri, "video/*");
                                        } else {
                                            //if you want you can also define the intent type for any other file
                                            //additionally use else clause below, to manage other unknown extensions
                                            //in this case, Android will show all applications installed on the device
                                            //so you can choose which application to use
                                            intent.setDataAndType(uri, "*/*");
                                        }
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);

                                    }
                                });

                            }


                            // }
                            else {
                                Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                            }

                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                Map<String, String> params = new HashMap<String, String>();


                params.put("lid", sh.getString("lid", ""));


                return params;
            }
        };

        int MY_SOCKET_TIMEOUT_MS = 100000;

        postRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(postRequest);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), edit_profile.class);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), Home.class);
        startActivity(i);

    }

}
