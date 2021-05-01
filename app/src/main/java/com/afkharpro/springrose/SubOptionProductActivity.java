package com.afkharpro.springrose;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.AddonHozitemJSONAdapter;
import com.afkharpro.springrose.Adapters.HozitemJSONAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SubOptionProductActivity extends AppCompatActivity {
    int id;
    JsonObjectRequest jsObjRequest;
    Context context;
    App app = App.getInstance();
    String optionsarrstring;
    Button addtocart;
    String cardid, desc, balloonid, chocid;
    String MORNING = "826";
    String AFTERNOON = "827";
    String EVENING = "828";
    String timeopt = "1";
    String dateStringopt = "1";
    String cardidopt = "1";
    String descopt = "1";
    String time;

    CheckBox morn, after, even;
    DatePicker deliverydate;
    String dateString;
    ProgressBar spinner;
    JSONArray optionarr;
    ArrayList<JSONObject> items;
    ArrayList<JSONObject> items2;
    Button skip;

    boolean clicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_option_product);
        context = getApplicationContext();
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        skip = (Button) findViewById(R.id.skip);
        addtocart = (Button) findViewById(R.id.addtocart);

        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.Addons));
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


        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        cardid = intent.getStringExtra("cardid");
        desc = intent.getStringExtra("desc");
        optionsarrstring = intent.getStringExtra("options");


        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent1 = new Intent(context, SubProductActivity.class);
                intent1.putExtra("id", id);
                intent1.putExtra("cardid", cardid);
                intent1.putExtra("desc", desc);
                intent1.putExtra("options", optionsarrstring);
                startActivity(intent1);
                finish();

            }
        });


        try {
            optionarr = new JSONArray(optionsarrstring);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        items = new ArrayList<JSONObject>();
        JSONObject overallcardsarr = null;
        try {
            overallcardsarr = optionarr.getJSONObject(optionarr.length() - 4);

            final JSONArray cardsarr;

            cardsarr = overallcardsarr.getJSONArray("option_value");

            for (int i = 0; i < cardsarr.length(); i++) {
                items.add(cardsarr.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AddonHozitemJSONAdapter adapter = new AddonHozitemJSONAdapter(context, 0, items);
        TwoWayView lvTest = (TwoWayView) findViewById(R.id.lvItems);
        lvTest.setAdapter(adapter);


        lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                new SweetAlertDialog(SubOptionProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText(context.getString(R.string.BalloonSelected))
//                        .setContentText(context.getString(R.string.YouclickedBalloon) + (i + 1))
//                        .show();
                try {
                    balloonid = items.get(i).getString("product_option_value_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                clicked = true;
            }
        });


        items2 = new ArrayList<JSONObject>();
        JSONObject overallcardsarr2 = null;
        try {
            overallcardsarr2 = optionarr.getJSONObject(optionarr.length() - 3);

            final JSONArray cardsarr;

            cardsarr = overallcardsarr2.getJSONArray("option_value");

            for (int i = 0; i < cardsarr.length(); i++) {
                items2.add(cardsarr.getJSONObject(i));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AddonHozitemJSONAdapter adapter2 = new AddonHozitemJSONAdapter(context, 0, items2);
        TwoWayView lvTest2 = (TwoWayView) findViewById(R.id.lvItems2);
        lvTest2.setAdapter(adapter2);


        lvTest2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                new SweetAlertDialog(SubOptionProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText(context.getString(R.string.ChocolateSelected))
//                        .setContentText(context.getString(R.string.YouclickedChocolate) + (i + 1))
//                        .show();
                try {
                    chocid = items2.get(i).getString("product_option_value_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                clicked = true;
            }
        });


        spinner.setVisibility(View.GONE);


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (clicked) {
                    Intent intent1 = new Intent(context, SubProductActivity.class);
                    intent1.putExtra("id", id);
                    intent1.putExtra("cardid", cardid);
                    intent1.putExtra("desc", desc);
                    intent1.putExtra("balloonid", balloonid);
                    intent1.putExtra("chocid", chocid);
                    intent1.putExtra("options", optionsarrstring);
                    startActivity(intent1);
                    finish();
                } else {
                    new SweetAlertDialog(SubOptionProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.Noaddonsselected))
                            .setContentText(context.getString(R.string.Areyousureyoudontwantany))
                            .setConfirmText(context.getString(R.string.Yes))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    Intent intent1 = new Intent(context, SubProductActivity.class);
                                    intent1.putExtra("id", id);
                                    intent1.putExtra("cardid", cardid);
                                    intent1.putExtra("desc", desc);
                                    intent1.putExtra("options", optionsarrstring);
                                    startActivity(intent1);
                                    finish();
                                }
                            })
                            .setCancelText(context.getString(R.string.No))
                            .show();
                }
            }
        });
    }
}
