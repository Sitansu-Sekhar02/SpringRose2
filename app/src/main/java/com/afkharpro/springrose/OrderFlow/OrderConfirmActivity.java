package com.afkharpro.springrose.OrderFlow;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.TermsActivity;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class OrderConfirmActivity extends AppCompatActivity {

    ImageView propic;
    TextView title, desc;
    Button checkout;
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    int pos;
    CheckBox agree;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirm);
        propic = (ImageView) findViewById(R.id.logo);
        title = (TextView) findViewById(R.id.t1);
        desc = (TextView) findViewById(R.id.desc);
        checkout = (Button) findViewById(R.id.checkout);
        context = getApplicationContext();
        Typeface face = Typeface.createFromAsset(getAssets(),"tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("" + context.getString(R.string.OrderConfirm));
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(face);
        tv.setTextSize((float) 24);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);
        agree = (CheckBox)findViewById(R.id.checkBox);
        TextView tc = (TextView)findViewById(R.id.tc);
        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TermsActivity.class);
                startActivity(intent);

            }
        });


        Intent i = getIntent();
        pos = i.getIntExtra("pos", 0);
        title.setText(i.getStringExtra("paystyle"));

        if (pos == 0) {
            Picasso.get().load(R.drawable.bank).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(propic);
            desc.setText(context.getString(R.string.MakePaymenttothebelowbankdetails)+"\n" +
                    "Inma  Bank / بنك الانماء\n" +
                    "Account : 68200215222018\n" +
                    "IBAN : SA4005000068200215222018\n" +
                    "\n"+
                    "Riyadh Bank / بنك الرياض\n"+
                    "Account : 2361663729940\n" +
                    "IBAN : SA3920000002361663729940\n" +
                    "\n" +
                    "Al Rajhi Bank / مصرف الراجحي\n"+
                    "Account : 582608010123483\n" +
                    "IBAN : SA4680000582608010123483\n" +
                    "\n" +
                    "Ahli Bank / البنك الاهلي\n"+
                    "Account : 62517667000210\n" +
                    "IBAN : SA3610000062517667000210"



            );
        } else if (pos == 1) {
            Picasso.get().load(R.drawable.credit).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(propic);
            desc.setText(context.getString(R.string.MakePaymentusingyourvisaormastercard));
        } else if (pos == 2) {
            Picasso.get().load(R.drawable.payfort).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(propic);
            desc.setText(context.getString(R.string.MakePaymentusingSADAD));
        } else if (pos == 3) {
            Picasso.get().load(R.drawable.cod).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(propic);
            desc.setText(context.getString(R.string.MakePaymentondeliveryincash));
        }


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    checkout.setEnabled(false);
                    doconfirm();


            }
        });


    }

    public void doconfirm() {




        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/confirm/confirm", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {


                                try{
                                    Intent i = new Intent(context, PaymentActivity.class);
                                    i.putExtra("pos",pos);
                                    i.putExtra("orderid",response.getJSONObject("data").optLong("order_id"));
                                    startActivity(i);
                                }catch (Exception e){

                                }

                                checkout.setEnabled(true);

                            }


                        } catch (Exception e) {
                            checkout.setEnabled(true);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        checkout.setEnabled(true);
                        if (volleyError instanceof NetworkError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ServerError) {
                            message(context, context.getString(R.string.Theservercouldnotbefound), jsObjRequest);
                        } else if (volleyError instanceof AuthFailureError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof ParseError) {
                            message(context, context.getString(R.string.Parsingerror), jsObjRequest);
                        } else if (volleyError instanceof NoConnectionError) {
                            message(context, context.getString(R.string.CannotconnecttoInternet), jsObjRequest);
                        } else if (volleyError instanceof TimeoutError) {
                            message(context, context.getString(R.string.ConnectionTimeOut), jsObjRequest);
                        }

                    }


                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(       30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void message(final Context context,String msg, final JsonObjectRequest jor) {

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText(msg);
        pDialog.setCancelable(true);

        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                MySingleton.getInstance(context).addToRequestQueue(jor);
            }
        })
                .show();

    }
}
