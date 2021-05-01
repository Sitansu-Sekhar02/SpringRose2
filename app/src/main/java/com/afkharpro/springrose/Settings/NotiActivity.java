package com.afkharpro.springrose.Settings;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Models.Noti;
import com.afkharpro.springrose.R;

public class NotiActivity extends AppCompatActivity {
    App app = App.getInstance();
    Context context;
    ToggleButton toggleButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_noti);
        context = getApplicationContext();
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);

        Noti noti = Noti.findById(Noti.class, (long) 1);
        toggleButton.setChecked(noti.turnedon);


        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b){
                    Noti noti = Noti.findById(Noti.class, (long) 1);
                    noti.turnedon = true;
                    noti.save();
                }else{
                    Noti noti = Noti.findById(Noti.class, (long) 1);
                    noti.turnedon = false;
                    noti.save();
                }
            }
        });

        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.Notifications));
        TextView tv = new TextView(getApplicationContext());

        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
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
    }
}
