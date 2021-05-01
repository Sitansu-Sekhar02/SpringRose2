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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.MainActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.ShopActivity;
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

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class FinalActivity extends AppCompatActivity {
    ImageView propic;
    TextView title, desc;
    Button checkout;
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    int pos;
    String ordernum;

    @Override
    public void onBackPressed() {
        checkout.setEnabled(false);
        Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
        startActivity(intent);
        finishAffinity();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        propic = (ImageView) findViewById(R.id.logo);
        title = (TextView) findViewById(R.id.t1);
        desc = (TextView) findViewById(R.id.desc);
        checkout = (Button) findViewById(R.id.checkout);
        context = getApplicationContext();
        Typeface face = Typeface.createFromAsset(getAssets(),"tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("" + context.getString(R.string.Complete));
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
        ordernum="";

        Intent i = getIntent();
        ordernum = i.getStringExtra("ordernum");


        title.setText(context.getString(R.string.Yourshipmentisonitsway)+" "+ordernum);

            Picasso.get().load(R.drawable.lastdone).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(propic);
            desc.setText(context.getString(R.string.Youcanfindoutmoreaboutthisorderfrom));



        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkout.setEnabled(false);
                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });


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