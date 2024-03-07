package com.example.rent_tracking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.razorpay.Checkout;
import com.razorpay.ExternalWalletListener;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import org.json.JSONException;
import org.json.JSONObject;
public class online_payment extends AppCompatActivity implements PaymentResultWithDataListener, ExternalWalletListener {
    private static final String TAG = online_payment.class.getSimpleName();
    private AlertDialog.Builder alertDialogBuilder;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_online_payment);
        Button button = (Button) findViewById(R.id.btn_pay);
        alertDialogBuilder = new AlertDialog.Builder(online_payment.this);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("Payment Result");
        alertDialogBuilder.setPositiveButton("Ok", (dialog, which) -> {
            //do nothing
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPayment();
            }
        });

        TextView privacyPolicy = (TextView) findViewById(R.id.txt_privacy_policy);

        privacyPolicy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent httpIntent = new Intent(Intent.ACTION_VIEW);
                httpIntent.setData(Uri.parse("https://razorpay.com/sample-application/"));
                startActivity(httpIntent);
            }
        });
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout co = new Checkout();

        EditText etApiKey = findViewById(R.id.et_api_key);
        if (!TextUtils.isEmpty(etApiKey.getText().toString())){
            co.setKeyID(etApiKey.getText().toString());
        }
        EditText etCustomOptions = findViewById(R.id.et_custom_options);
        if (!TextUtils.isEmpty(etCustomOptions.getText().toString())){
            try{
                JSONObject options = new JSONObject(etCustomOptions.getText().toString());
                co.open(activity, options);
            }catch (JSONException e){
                Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }
        }else{
            try {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                JSONObject options = new JSONObject();
                options.put("name", "Razorpay Corp");
                options.put("description", "Demoing Charges");
                options.put("send_sms_hash",true);
                options.put("allow_rotation", true);
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("currency", "INR");
                options.put("amount", sh.getString("oamount","")+"00");
                JSONObject preFill = new JSONObject();
                preFill.put("email", "riss.princytv@gmail.com");
                preFill.put("contact", "9895479752");
                options.put("prefill", preFill);
                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }
        }


    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */


    @Override
    public void onExternalWalletSelected(String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("External Wallet Selected:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onPaymentSuccess(String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("Payment Successful :\nPayment ID: "+s+"\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int i, String s, PaymentData paymentData) {
        try{
            alertDialogBuilder.setMessage("Payment Failed:\nPayment Data: "+paymentData.getData());
            alertDialogBuilder.show();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

