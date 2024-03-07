package com.example.rent_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class edit_profile extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5, e6;
    ImageView img;
    Button b1;
    SharedPreferences sh;
    String url;
    String flg = "no";
    Bitmap bitmap = null;
    ProgressDialog pd;
    String MobilePattern = "[6-9][0-9]{9}";
    String PinPattern = "[6-9][0-9]{5}";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        e1 = findViewById(R.id.editTextTextPersonName6);
        e2 = findViewById(R.id.editTextTextPersonName7);
        e3 = findViewById(R.id.editTextTextPersonName8);
        e4 = findViewById(R.id.editTextNumber2);
        e5 = findViewById(R.id.editTextTextEmailAddress2);
        e5.setFocusable(false);             // Email should be read only format
        e6 = findViewById(R.id.editTextNumber3);
        b1 = findViewById(R.id.button6);
        img = findViewById(R.id.imageView4);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ipaddress", "");
        url = sh.getString("url", "") + "android_edit_profile";

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
                                e1.setText(jsonObj.getString("name"));
                                e2.setText(jsonObj.getString("place"));
                                e3.setText(jsonObj.getString("post"));
                                e4.setText(jsonObj.getString("pin"));
                                e5.setText(jsonObj.getString("email"));
                                e6.setText(jsonObj.getString("contact"));

                                String image = jsonObj.getString("image");
                                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                String ipaddress = sh.getString("ipaddress", "");
                                String url = "http://" + ipaddress + ":1234" + image;
                                Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).into(img);

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
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
                flg = "yes";
            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                sh.getString("ipaddress", "");
                url = sh.getString("url", "") + "android_edit_profiles";

                String name = e1.getText().toString();
                String place = e2.getText().toString();
                String post = e3.getText().toString();
                String pin = e4.getText().toString();
                String email = e5.getText().toString();
                String contact = e6.getText().toString();
                int flag=0;
                if(name.equalsIgnoreCase("")){
                    e1.setError("null");
                    flag++;
                }
                if(place.equalsIgnoreCase("")){
                    e2.setError("null");
                    flag++;
                }
                if(post.equalsIgnoreCase("")){
                    e3.setError("null");
                    flag++;
                }
                if(!pin.matches(PinPattern)){
                    e4.setError("null");
                    flag++;
                }
                if(!email.matches(emailPattern)){
                    e5.setError("null");
                    flag++;
                } if(!contact.matches(MobilePattern)){
                    e6.setError("null");
                    flag++;
                }




                if (flg.equalsIgnoreCase("yes")) {
                    Toast.makeText(getApplicationContext(), "YEs", Toast.LENGTH_SHORT).show();
                    uploadBitmap(name, place, post, pin, email, contact);
                } else {
                    Toast.makeText(edit_profile.this, "", Toast.LENGTH_SHORT).show();
                    uploadBitmap2(name, email, post, pin, email, contact);
                }


            }
        });
    }

    private void uploadBitmap2(String name, String place, String post, String pin, String email, String contact) {

        pd = new ProgressDialog(edit_profile.this);
        pd.setMessage("Uploading....");
        pd.show();
        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            pd.dismiss();


                            JSONObject obj = new JSONObject(new String(response.data));

                            if (obj.getString("status").equals("ok")) {
                                Toast.makeText(getApplicationContext(), "update success", Toast.LENGTH_SHORT).show();
                                android.content.Intent i = new Intent(getApplicationContext(), view_profile.class);
                                startActivity(i);
                            } else {
                                Toast.makeText(getApplicationContext(), "update failed", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {


            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                params.put("name", name);//passing to python
                params.put("place", place);//passing to python
                params.put("post", post);//passing to python
                params.put("pin", pin);//passing to python
                params.put("email", email);//passing to python
                params.put("contact", contact);//passing to python
//                    params.put("photo", photo);//passing to python

                params.put("lid", sh.getString("lid", ""));//passing to python
                return params;
            }


        };

        int MY_SOCKET_TIMEOUT_MS = 100000;

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                MY_SOCKET_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(this).add(volleyMultipartRequest);


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                img.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //converting to bitarray
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(String name, String place, String post, String pin, String email, String contact) {
        {


            pd = new ProgressDialog(edit_profile.this);
            pd.setMessage("Uploading....");
            pd.show();
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                pd.dismiss();


                                JSONObject obj = new JSONObject(new String(response.data));

                                if (obj.getString("status").equals("ok")) {
                                    Toast.makeText(getApplicationContext(), "Update success", Toast.LENGTH_SHORT).show();
                                    android.content.Intent i = new Intent(getApplicationContext(), view_profile.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), " failed", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    params.put("name", name);//passing to python
                    params.put("place", place);//passing to python
                    params.put("post", post);//passing to python
                    params.put("pin", pin);//passing to python
                    params.put("email", email);//passing to python
                    params.put("contact", contact);//passing to python
//                        params.put("photo", );//passing to python

                    params.put("lid", sh.getString("lid", ""));//passing to python
                    return params;
                }

                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    return params;
                }
            };
            int MY_SOCKET_TIMEOUT_MS = 100000;

            volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                    MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            Volley.newRequestQueue(this).add(volleyMultipartRequest);


        }

    }


}