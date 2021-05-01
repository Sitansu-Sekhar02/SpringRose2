package com.afkharpro.springrose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.OrderFlow.CartActivity;
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
import fr.ganfra.materialspinner.MaterialSpinner;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SubProductActivity extends AppCompatActivity {
    int id;
    JsonObjectRequest jsObjRequest;
    Context context;
    App app = App.getInstance();
    String optionsarrstring;
    Button addtocart;
    String cardid, desc;
    String MORNING = "826";
    String AFTERNOON = "827";
    String EVENING = "828";
    String timeopt = "1";
    String dateStringopt = "1";
    String cardidopt = "1";
    String descopt = "1";
    String time,balloonid,chocid;
String ballopt,chocopt;
    CheckBox morn, after, even;
    DatePicker deliverydate;
    String dateString;
    ProgressBar spinner;
    JSONArray optionarr;
    JSONArray timingsARR;
    ArrayList<String> times;
    MaterialSpinner mspinner;
    ArrayAdapter<String> adapter;
    boolean nightTime = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_product);
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        cardid = intent.getStringExtra("cardid");
        desc = intent.getStringExtra("desc");
        balloonid = intent.getStringExtra("balloonid");
        chocid = intent.getStringExtra("chocid");
        optionsarrstring = intent.getStringExtra("options");
        try {
            optionarr = new JSONArray(optionsarrstring);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        context = getApplicationContext();
        deliverydate = (DatePicker) findViewById(R.id.datePicker2);
//        morn = (CheckBox) findViewById(R.id.morn);
//        after = (CheckBox) findViewById(R.id.after);
//        even = (CheckBox) findViewById(R.id.even);
        addtocart = (Button) findViewById(R.id.addtocart);



        try {
            timingsARR = optionarr.getJSONObject(1).getJSONArray("option_value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Calendar future = Calendar.getInstance();
        future.add(Calendar.DATE,150);
        deliverydate.setMinDate(Calendar.getInstance().getTimeInMillis());
        deliverydate.setMaxDate(future.getTimeInMillis());


        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        deliverydate.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), new DatePicker.OnDateChangedListener() {

            @Override
            public void onDateChanged(DatePicker datePicker, int year, int month, int dayOfMonth) {

                times = new ArrayList<String>();
                for (int i = 0; i < timingsARR.length(); i++) {


                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year, month , dayOfMonth);


                        Calendar today = Calendar.getInstance();
                    try {
                        String dsTime = timingsARR.getJSONObject(i).getString("name");
                        int dayOfMotnthDC = calendar.get(Calendar.DAY_OF_MONTH);
                        int dayOfMotnthT = today.get(Calendar.DAY_OF_MONTH);

                        int MotnthDC = calendar.get(Calendar.MONTH);
                        int MotnthT = today.get(Calendar.MONTH) ;

                        if (dayOfMotnthDC == dayOfMotnthT && MotnthDC == MotnthT) {

                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int dow = calendar.get(Calendar.DAY_OF_WEEK_IN_MONTH);
                            boolean isFriday = dow == Calendar.FRIDAY;
                            boolean isMorning = (dsTime.toLowerCase().contains("morning") || dsTime.contains("المساء"));
                            boolean isAfternoon = (dsTime.toLowerCase().contains("afternoon") || dsTime.contains("الصباح"));
                            boolean isEvening = (dsTime.toLowerCase().contains("evening") || dsTime.contains("الظهر"));

                            if (!isFriday) {
                                if (hour <= 10 && (isMorning || isAfternoon || isEvening) ) {
                                    times.add(dsTime);
                                }
                                if (hour > 10 && hour <= 15 && (isAfternoon || isEvening)) {
                                    times.add(dsTime);
                                }
                                if (hour > 15 && hour <= 21 && isEvening) {
                                    times.add(dsTime);
                                }
                                if (hour >= 21 && hour < 24) {
                                    nightTime = true;
                                } else {
                                    nightTime = false;
                                }
                            }else{
                                if (!isMorning && !isAfternoon) {
                                    times.add(dsTime);
                                }
                            }


                        }else{
                            nightTime = false;
                            int hour = calendar.get(Calendar.HOUR_OF_DAY);
                            int dow = calendar.get(Calendar.DAY_OF_WEEK);
                            boolean isFriday = dow == Calendar.FRIDAY;
                            boolean isMorning = (dsTime.toLowerCase().contains("morning") || dsTime.contains("المساء"));
                            boolean isAfternoon = (dsTime.toLowerCase().contains("afternoon") || dsTime.contains("الصباح"));
                            boolean isEvening = (dsTime.toLowerCase().contains("evening") || dsTime.contains("الظهر"));

                            if (!isFriday) {
                                times.add(dsTime);
                            }else{
                                if (!isMorning && !isAfternoon) {
                                    times.add(dsTime);
                                }
                            }

                        }
                    } catch(JSONException e){
                        e.printStackTrace();
                    }
            }

                adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, times);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                mspinner = (MaterialSpinner) findViewById(R.id.mspinner);
                mspinner.setAdapter(adapter);
            }
        });



        times = new ArrayList<String>();
        for (int i= 0 ; i<timingsARR.length();i++){
            try {
                String dsTime = timingsARR.getJSONObject(i).getString("name");

                    int hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                    int dow = Calendar.getInstance().get (Calendar.DAY_OF_WEEK);
                    boolean isFriday = dow == Calendar.FRIDAY;
                    boolean isMorning = (dsTime.toLowerCase().contains("morning") || dsTime.contains("المساء"));
                    boolean isAfternoon = (dsTime.toLowerCase().contains("afternoon") || dsTime.contains("الصباح"));
                    boolean isEvening = (dsTime.toLowerCase().contains("evening") || dsTime.contains("الظهر"));

                if (!isFriday) {
                    if (hour <= 10 && (isMorning || isAfternoon || isEvening) ) {
                        times.add(dsTime);
                    }
                    if (hour > 10 && hour <= 15 && (isAfternoon || isEvening)) {
                        times.add(dsTime);
                    }
                    if (hour > 15 && hour <= 21 && isEvening) {
                        times.add(dsTime);
                    }
                    if (hour >= 21 && hour < 24) {
                        nightTime = true;
                    } else {
                        nightTime = false;
                    }
                }else{
                    nightTime = false;
                    if (!isMorning) {
                        times.add(dsTime);
                    }
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, times);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mspinner = (MaterialSpinner) findViewById(R.id.mspinner);
        mspinner.setAdapter(adapter);

        mspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    time = timingsARR.getJSONObject(position).getString("product_option_value_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                try {
                    time = timingsARR.getJSONObject(0).getString("product_option_value_id");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


//        morn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setselected(morn);
//                try {
//                    time = optionarr.getJSONObject(1).getJSONArray("option_value").getJSONObject(1).getString("product_option_value_id");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//
//        after.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setselected(after);
//                try {
//                    time = optionarr.getJSONObject(1).getJSONArray("option_value").getJSONObject(0).getString("product_option_value_id");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        });
//
//        even.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                setselected(even);
//                try {
//                    time = optionarr.getJSONObject(1).getJSONArray("option_value").getJSONObject(2).getString("product_option_value_id");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });



        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.DeliveryDetails));
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


        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (nightTime) {
                    new SweetAlertDialog(SubProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.Times))
                            .show();
                }else{
                    int day = deliverydate.getDayOfMonth();
                    int month = deliverydate.getMonth();
                    int year = deliverydate.getYear();
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    dateString = sdf.format(calendar.getTime());


                    if (TextUtils.isEmpty(time)) {
                        new SweetAlertDialog(SubProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.SelectTime))
                                .show();
                    } else {


                        try {
                            dateStringopt = optionarr.getJSONObject(0).getString("product_option_id");
                            timeopt = optionarr.getJSONObject(1).getString("product_option_id");
                            cardidopt = optionarr.getJSONObject(4).getString("product_option_id");
                            descopt = optionarr.getJSONObject(5).getString("product_option_id");
                            if (!TextUtils.isEmpty(balloonid)){
                                ballopt = optionarr.getJSONObject(2).getString("product_option_id");
                            }
                            if (!TextUtils.isEmpty(chocid)){
                                chocopt = optionarr.getJSONObject(3).getString("product_option_id");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                        addcart();
                    }
                }




            }
        });


    }

    public void setselected(CheckBox checkBox) {
        morn.setChecked(false);
        after.setChecked(false);
        even.setChecked(false);
        checkBox.setChecked(true);
    }

    public void renewtoken() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("old_token", app.getAccess_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=feed/rest_api/gettoken&grant_type=client_credentials", obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            app.setAccess_token(response.getString("access_token"));
                            AccessToken curr = AccessToken.findById(AccessToken.class, (long) 1);
                            curr.token = app.getAccess_token();
                            curr.save();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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
                params.put("Authorization", "Basic bW9iaWxldXNlcjpnNUgzNDJeNw==");


                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void addcart() {
        //   renewtoken();
        JSONObject options = new JSONObject();
        JSONObject product = new JSONObject();
        try {
            ArrayList<String> list = new ArrayList<String>();
            list.add("1");

            ArrayList<String> blist = new ArrayList<String>();
            blist.add(balloonid);

            ArrayList<String> clist = new ArrayList<String>();
            clist.add(chocid);

            spinner.setVisibility(View.VISIBLE);
            JSONObject optquan = new JSONObject();



            options.put(dateStringopt, dateString);
            options.put(timeopt, time);
            if (!cardid.equals("")){
                options.put(cardidopt, cardid);
            }
            if (!cardid.equals("")&&!desc.equals("")){
                options.put(descopt, desc);
            }


            if (!TextUtils.isEmpty(balloonid)){
                options.put(ballopt, new JSONArray(blist));
                optquan.put(balloonid,new JSONArray(list));

            }
            if (!TextUtils.isEmpty(chocid)){
                options.put(chocopt, new JSONArray(clist));
               optquan.put(chocid,new JSONArray(list));
            }
            if (!TextUtils.isEmpty(chocid)||!TextUtils.isEmpty(balloonid)) {
                options.put("optionsQuantity", optquan);
            }


            product.put("product_id", String.valueOf(id));
            product.put("quantity", 1);
            product.put("option", options);
        } catch (JSONException e) {
            e.printStackTrace();
            spinner.setVisibility(View.GONE);
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/cart/cart", product, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            String x = "";
                            if (response.getBoolean("success")) {
                                spinner.setVisibility(View.GONE);
//                                new SweetAlertDialog(SubProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                        .setTitleText(context.getString(R.string.Added))
//                                        .setContentText(context.getString(R.string.Itemhasbeenaddedtoyourshoppingcart))
//                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sDialog) {
//                                                sDialog.dismissWithAnimation();
//                                                app.setItemInCart(true);
//                                                Intent i = new Intent(getApplicationContext(),CartActivity.class);
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        })
//                                        .show();
                                Intent i = new Intent(getApplicationContext(),CartActivity.class);
                                                startActivity(i);
                                                finish();
                            } else {
                                int num = 0;
                                JSONObject err = response.getJSONObject("error").getJSONObject("option");
                                Iterator<String> keys = err.keys();
                                while (keys.hasNext()) {
                                    num++;
                                    if (num == 1) {
                                        dateStringopt = (String) keys.next();
                                    } else if (num == 2) {
                                        timeopt = (String) keys.next();
                                    } else if (num == 3) {
                                        cardidopt = (String) keys.next();
                                    } else if (num == 4) {
                                        descopt = (String) keys.next();
                                    }
                                }
                                addcart();
                            }

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
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


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
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
