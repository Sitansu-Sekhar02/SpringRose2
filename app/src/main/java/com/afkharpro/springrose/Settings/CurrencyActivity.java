package com.afkharpro.springrose.Settings;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.MainActivity;
import com.afkharpro.springrose.Models.Currency;
import com.afkharpro.springrose.ProductActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.SplashActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CurrencyActivity extends AppCompatActivity {

    ImageView SAR, USD, EUR, GBP;
    App app = App.getInstance();
    Context context;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        SAR = (ImageView) findViewById(R.id.sar);
        USD = (ImageView) findViewById(R.id.usd);
        EUR = (ImageView) findViewById(R.id.eur);
        GBP = (ImageView) findViewById(R.id.gbp);
        context = getApplicationContext();
        //getSupportActionBar().setTitle("Currency");

        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.Currency));
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(face);
        tv.setTextSize((float)24);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);

        SAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               showmsg(context.getString(R.string.CurrencysettoSAR));
                app.setCurrency("SAR");
                Currency curr = Currency.findById(Currency.class, (long)1);
                if (curr==null){
                    Currency currnew = new Currency("SAR");
                    currnew.save();
                }else{
                    curr.currency="SAR";
                    curr.save();
                }
                mFirebaseAnalytics.setUserProperty("Currency", app.getCurrency());
                clearcats();

            }
        });


        USD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmsg(context.getString(R.string.CurrencysettoUSD));
                app.setCurrency("USD");

                Currency curr = Currency.findById(Currency.class, (long)1);
                if (curr==null){
                    Currency currnew = new Currency("USD");
                    currnew.save();
                }else{
                    curr.currency="USD";
                    curr.save();
                }
                mFirebaseAnalytics.setUserProperty("Currency", app.getCurrency());
                clearcats();
            }
        });

        EUR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmsg(context.getString(R.string.CurrencysettoEUR));
                app.setCurrency("EUR");

                Currency curr = Currency.findById(Currency.class, (long)1);
                if (curr==null){
                    Currency currnew = new Currency("EUR");
                    currnew.save();
                }else{
                    curr.currency="EUR";
                    curr.save();
                }
                mFirebaseAnalytics.setUserProperty("Currency", app.getCurrency());
                clearcats();
            }
        });


        GBP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmsg(context.getString(R.string.CurrencysettoGBP));
                app.setCurrency("GBP");
                Currency curr = Currency.findById(Currency.class, (long)1);
                if (curr==null){
                    Currency currnew = new Currency("GBP");
                    currnew.save();
                }else{
                    curr.currency="GBP";
                    curr.save();
                }
                mFirebaseAnalytics.setUserProperty("Currency", app.getCurrency());
                clearcats();
            }
        });


    }


    public void showmsg(String msg) {
        new SweetAlertDialog(CurrencyActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText("Currency Selected")
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sDialog) {
                sDialog.dismissWithAnimation();
                Intent intent = new Intent(CurrencyActivity.this, SplashActivity.class);
                startActivity(intent);
                finishAffinity();
                     }
                     })
                .show();
    }

    public void clearcats(){
        app.getCoporate().clear();
        app.getEid().clear();
        app.getBirthday().clear();
        app.getGraduation().clear();
        app.getMother().clear();
        app.getArrangments().clear();
        app.getArtificial().clear();
        app.getBestsellers().clear();
        app.getHand().clear();
        app.getLonglife().clear();
        app.getViewed().clear();
        app.getNewborn().clear();
        app.getLove().clear();
        app.getNewproducts().clear();
    }


}
