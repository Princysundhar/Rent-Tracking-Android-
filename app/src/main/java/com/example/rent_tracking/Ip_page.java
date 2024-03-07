package com.example.rent_tracking;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Ip_page extends AppCompatActivity {
    EditText e1;
    Button b1;
    SharedPreferences sh;
    String url;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        setContentView(R.layout.activity_ip_page);

        e1 = findViewById(R.id.editTextTextPersonName);
        b1 = findViewById(R.id.button);
        e1.setText(sh.getString("ipaddress",""));
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ipaddress = e1.getText().toString();
                int flag =0;
                if(ipaddress.equalsIgnoreCase("")){
                    e1.setError("Invalid");
                    flag++;
                }
                if(flag == 0) {
                    String url = "http://" + ipaddress + ":1234/";
                    SharedPreferences.Editor ed = sh.edit();
                    ed.putString("ipaddress", ipaddress);
                    ed.putString("url", url);
                    ed.commit();
                    Intent i = new Intent(getApplicationContext(), login.class);
                    startActivity(i);
                }
            }
        });
    }
}