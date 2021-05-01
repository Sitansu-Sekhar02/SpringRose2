package com.afkharpro.springrose.Settings;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.MainActivity;
import com.afkharpro.springrose.Models.Currency;
import com.afkharpro.springrose.Models.Language;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.SplashActivity;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class LanguageActivity extends AppCompatActivity {
    ImageView SAR, USD;
    App app = App.getInstance();
    Context context;
    private FirebaseAnalytics mFirebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language);
        context = getApplicationContext();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        SAR = (ImageView) findViewById(R.id.sar);
        USD = (ImageView) findViewById(R.id.usd);
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.Language));
        TextView tv = new TextView(getApplicationContext());

        Typeface face = Typeface.createFromAsset(getAssets(),                 "tahoma.ttf");
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
        //getSupportActionBar().setTitle("Language");

        SAR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmsg(context.getString(R.string.LanguagesettoArabic));
                app.setLanguage("ar");
                Language lang = Language.findById(Language.class, (long)1);
                if (lang==null){
                    Language lannew = new Language("ar");
                    lannew.save();
                    Resources res = context.getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    android.content.res.Configuration conf = res.getConfiguration();
                    conf.setLocale(new Locale("ar"));
                    res.updateConfiguration(conf, dm);

                }else{
                    lang.lang="ar";
                    lang.save();
                    Resources res = context.getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    android.content.res.Configuration conf = res.getConfiguration();
                    conf.setLocale(new Locale("ar"));
                    res.updateConfiguration(conf, dm);

                }
                mFirebaseAnalytics.setUserProperty("Language", app.getLanguage());

            }
        });


        USD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showmsg(context.getString(R.string.LanguagesettoEnglish));
                app.setLanguage("en-gb");
                Language lang = Language.findById(Language.class, (long)1);
                if (lang==null){
                    Language lannew = new Language("en-gb");
                    lannew.save();
                    Resources res = context.getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    android.content.res.Configuration conf = res.getConfiguration();
                    conf.setLocale(new Locale("en-rGB"));
                    res.updateConfiguration(conf, dm);
                }else{
                    lang.lang="en-gb";
                    lang.save();
                    Resources res = context.getResources();
                    DisplayMetrics dm = res.getDisplayMetrics();
                    android.content.res.Configuration conf = res.getConfiguration();

                    conf.setLocale(new Locale("en-rGB"));
                    res.updateConfiguration(conf, dm);
                }


                mFirebaseAnalytics.setUserProperty("Language", app.getLanguage());

            }
        });
    }

    public void showmsg(String msg) {
        new SweetAlertDialog(LanguageActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(context.getString(R.string.LanguageSelected))
                .setContentText(msg)
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                        Intent intent = new Intent(context, SplashActivity.class);
                        startActivity(intent);
                        finishAffinity();
                    }
                })
                .show();
    }


    public void setLang(String lang ){
        Resources res = getApplicationContext().getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        conf.setLocale(new Locale(lang));
        res.updateConfiguration(conf, dm);
    }


}
