package com.afkharpro.springrose.OrderFlow;

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
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.ShippingAddressAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.MenuActivities.AccountActivity;
import com.afkharpro.springrose.R;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONObject;

import java.util.ArrayList;

public class GuestFlowActivity extends AppCompatActivity {
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    Button guest,join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_flow);
        context = getApplicationContext();
        Typeface face = Typeface.createFromAsset(getAssets(),"tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("" + context.getString(R.string.JoinUs));
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


        guest = (Button)findViewById(R.id.guest);
        join = (Button)findViewById(R.id.join);

        guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guest.setEnabled(false);
                Intent intent  = new Intent(getApplicationContext(),GuestRegisterActivity.class);
                startActivity(intent);
                finish();
                guest.setEnabled(true);

            }
        });

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                join.setEnabled(false);
                Intent intent  = new Intent(getApplicationContext(),AccountActivity.class);
                startActivity(intent);
                finish();
                join.setEnabled(true);
            }
        });


    }
}
