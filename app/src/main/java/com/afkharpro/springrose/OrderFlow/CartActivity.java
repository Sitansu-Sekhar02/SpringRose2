package com.afkharpro.springrose.OrderFlow;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.ShoppingCartAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.MenuActivities.MazayaActivity;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.PopUps.CouponActivity;
import com.afkharpro.springrose.ProductActivity;
import com.afkharpro.springrose.R;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CartActivity extends AppCompatActivity {
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    TextView test;
    ListView list;
    ShoppingCartAdapter adapter;
    ArrayList<JSONObject> productsarr;
    TextView subtotal, total,discount,vat;
    int subval = 0;
    int totalval = 0;
    ProgressBar spinner;
    Button checkout;
    EditText code;
    Button add;
    boolean redeemed;
    LinearLayout dislin;
    FirebaseAnalytics mFirebaseAnalytics;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String onlinecode= "null";
   // String onlinecode2= "null";
    boolean donefromremote = false;
    boolean donefrommazaya = false;
    TextView discounttext,vattext;
    LinearLayout dislayout;
    long cacheExpiration = 3600;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_cart);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        context = getApplicationContext();
        subtotal = (TextView) findViewById(R.id.subtotal);
        total = (TextView) findViewById(R.id.total);
        vat = (TextView) findViewById(R.id.vat);
        discount = (TextView) findViewById(R.id.dis);
        discounttext= (TextView) findViewById(R.id.discount);
        vattext= (TextView) findViewById(R.id.vattext);
        dislin = (LinearLayout)findViewById(R.id.dislin) ;
        checkout = (Button) findViewById(R.id.checkout);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        list = (ListView) findViewById(R.id.listView2);
        productsarr = new ArrayList<>();
        Intent intent = getIntent();
        redeemed = intent.getBooleanExtra("redeemed",false);
        donefromremote = intent.getBooleanExtra("donefromremote",false);
        donefrommazaya = intent.getBooleanExtra("donefrommazaya",false);
       // onlinecode2 = intent.getStringExtra("onlinecode");
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        dislayout = (LinearLayout)findViewById(R.id.linpro) ;
        //getSupportActionBar().setTitle("Shopping Cart");

        Typeface face = Typeface.createFromAsset(getAssets(),"tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();

           // ab.setTitle("        " + context.getString(R.string.ShoppingCart));
        ab.setTitle(context.getString(R.string.ShoppingCart));


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

        //renewtoken();
         getcart();
        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(false)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);

        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }

        checkremote();
       // getmazaya();



        code = (EditText) findViewById(R.id.code);
        add = (Button) findViewById(R.id.add);




        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(code.getText().toString())){
                    docoupon();
                }else{
                    code.setError(context.getString(R.string.Cannotbeempty));
                }
            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (productsarr.size()!=0){
                    checkout.setEnabled(false);
                    try{
                        Users user = Users.findById(Users.class, (long)1);
                        if (user!=null && !user.userid.equals("0")){
                            Intent i = new Intent(context,SetAddressesActivity.class);
                            startActivity(i);
                            checkout.setEnabled(true);
                        }else{
                            Intent i = new Intent(context,GuestFlowActivity.class);
                            startActivity(i);
                            checkout.setEnabled(true);
                        }

                    }catch (Exception e){

                        checkout.setEnabled(true);
//
                    }
                }else {
                    new SweetAlertDialog(CartActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.Cartisempty))
                            .show();
                }
            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (view.getTag().equals("plus")) {
                    String s = null;
                    try {
                        s = productsarr.get(i).getString("quantity");


                        int val = Integer.parseInt(s) + 1;


                        productsarr.get(i).put("quantity", String.valueOf(val));
                        updatequan(productsarr.get(i).getString("key"),String.valueOf(val));
                        adapter.notifyDataSetChanged();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                if (view.getTag().equals("minus")) {
                    String s = null;
                    try {
                        s = productsarr.get(i).getString("quantity");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    int val = 1;
                    if (Integer.parseInt(s) > 1) {
                        val = Integer.parseInt(s) - 1;
                    }
                    try {

                        updatequan(productsarr.get(i).getString("key"),String.valueOf(val));
                        productsarr.get(i).put("quantity", String.valueOf(val));
                        adapter.notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }

                if (view.getTag().equals("close")) {
                    try {
                        deleteitem(productsarr.get(i).getString("key"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    productsarr.remove(i);
                    adapter.notifyDataSetChanged();
                }


            }
        });


        if (redeemed){
            add.setEnabled(false);
            add.setFocusable(false);
            add.setAlpha((float)0.5);

            dislayout.setVisibility(View.GONE);

            if (donefrommazaya){
                if (app.getLanguage().equals("en-GB")){
                    discounttext.setText("Discount"+" ("+"Mazaya"+")");
                }else{
                    discounttext.setText("قسيمة التخفيض"+" ("+"Mazaya"+")");
                }
            }

            if (donefromremote){
                if (app.getLanguage().equals("en-GB")){
                    discounttext.setText("Discount"+" ("+onlinecode+")");
                }else{
                    discounttext.setText("قسيمة التخفيض"+" ("+onlinecode+")");
                }
            }



        }



    }

    public  void checkremote(){

        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(CartActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {

                        }
                        onlinecode = mFirebaseRemoteConfig.getString("coupon_discount");
                       // onlinecode2 = mFirebaseRemoteConfig.getString("coupon_discount2");

//                        if (!onlinecode.toLowerCase().equals("null")){
//
//                            if (onlinecode2.toLowerCase().equals("mazaya")){
//                                if (!donefrommazaya){
//                                    getmazaya();
//                                }
//                            }else {
//                                if (app.getLanguage().equals("en-GB")){
//                                    discounttext.setText("Discount"+" ("+onlinecode+")");
//                                }else{
//                                    discounttext.setText("قسيمة التخفيض"+" ("+onlinecode+")");
//                                }
//
//                                if (!donefromremote){
//                                    donefromremote=true;
//                                    docouponfromremote(onlinecode);
//                                }
//                            }


                            if (!onlinecode.toLowerCase().equals("null")){

                                if (onlinecode.toLowerCase().contains("mazaya")){
                                    if (!donefrommazaya){
                                     //  getmazaya();

                                    }
                                }else {
                                    if (app.getLanguage().equals("en-GB")){
                                        discounttext.setText("Discount"+" ("+onlinecode+")");
                                    }else{
                                        discounttext.setText("قسيمة التخفيض"+" ("+onlinecode+")");
                                    }

                                    if (!donefromremote){
                                        donefromremote=true;
                                        docouponfromremote(onlinecode);
                                    }
                                }




                        }


                    }
                });
    }




    public void showmazaya() {

        if (app.getLanguage().equals("en-GB")){
            discounttext.setText("Discount"+" ("+"Mazaya"+")");
        }else{
            discounttext.setText("قسيمة التخفيض"+" ("+"Mazaya"+")");
        }

        SweetAlertDialog pDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);

        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Mazaya");
        pDialog.setContentText(getApplicationContext().getString(R.string.showmazaya));
        pDialog.setCancelable(true);


        pDialog.show();

    }

    public void getmazaya() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/account/mazaya", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            Bundle bundle;
                            if (response.getBoolean("success")) {

                               if (response.getJSONObject("data").getString("card_type").equals("")){
                                  // donefrommazaya=true;



                                  /* final AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                   builder.setMessage(getResources().getString(R.string.mazmsg))
                                           .setCancelable(false)
                                           .setPositiveButton(getResources().getString(R.string.Linknow), new DialogInterface.OnClickListener() {
                                               public void onClick(final DialogInterface dialog, final int id) {
                                                   Intent intent = new Intent(CartActivity.this,MazayaActivity.class);
                                                   startActivity(intent);
                                                   finish();
                                                   dialog.cancel();
                                               }
                                           })
                                           .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                                               public void onClick(final DialogInterface dialog, final int id) {
                                                   checkremote();
                                                   dialog.cancel();
                                               }
                                           });
                                   final AlertDialog alert = builder.create();
                                   alert.show(); */
//                                   new SweetAlertDialog(CartActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                           // .setTitleText(context.getString(R.string.Success))
//                                           .setContentText(context.getString(R.string.YourMemberIDLinkedSuccessfully))
//                                           .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                               @Override
//                                               public void onClick(SweetAlertDialog sDialog) {
//                                                   sDialog.dismissWithAnimation();
//                                                   Intent intent = new Intent(CartActivity.this,MazayaActivity.class);
//                                                   startActivity(intent);
//                                               }
//                                           })
//                                           .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                               @Override
//                                               public void onClick(SweetAlertDialog sweetAlertDialog) {
//                                                   checkremote();
//                                               }
//                                           })
//                                           .show();


                               }else if (response.getJSONObject("data").getString("card_type").equals("Royal")){
                                   if (!donefrommazaya){
                                       redeemed=true;
                                       donefrommazaya=true;
                                       showmazaya();
                                      // docouponfromremote("mazaya15%");
                                   }
                               }else if (response.getJSONObject("data").getString("card_type").equals("Special")){
                                   if (!donefrommazaya){
                                       redeemed=true;
                                       donefrommazaya=true;
                                       showmazaya();
                                      // docouponfromremote("mazaya10%");
                                   }

                               }




                            } else {
                                checkremote();
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


                // params.put("Authorization", "Bearer ee5375799c60356b872be97edaf836b8c91d3134" );
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void getcart() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/cart/cart", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            Bundle bundle;
                            if (response.getBoolean("success")) {
                                JSONArray productslist = new JSONArray();
                                productslist = response.getJSONObject("data").getJSONArray("products");
                                for (int i = 0; i < productslist.length(); i++) {
                                    productsarr.add(productslist.getJSONObject(i));
                                    bundle = new Bundle();
                                    bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productslist.getJSONObject(i).getString("model"));
                                    mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.ADD_TO_CART, bundle);
                                }

                                    adapter = new ShoppingCartAdapter(CartActivity.this, 0, productsarr);
                                    list.setAdapter(adapter);

                                    if (response.getJSONObject("data").getString("coupon_name").contains("aza")){
                                        showmazaya();
                                    }



                                JSONObject sub = response.getJSONObject("data").getJSONArray("totals").getJSONObject(0);
                                JSONObject vatobj = response.getJSONObject("data").getJSONArray("totals").getJSONObject(1);
                                JSONObject tot = response.getJSONObject("data").getJSONArray("totals").getJSONObject(2);
                                subtotal.setText(sub.getString("text"));
                                if (vatobj.getString("text").contains("-")){
                                    add.setEnabled(false);
                                    add.setFocusable(false);
                                    add.setAlpha((float)0.5);
                                    dislayout.setVisibility(View.GONE);
                                    dislin.setVisibility(View.VISIBLE);
                                    subtotal.setText(response.getJSONObject("data").getJSONArray("totals").getJSONObject(0).getString("text"));
                                    discount.setText(response.getJSONObject("data").getJSONArray("totals").getJSONObject(1).getString("text"));
                                    vatobj = response.getJSONObject("data").getJSONArray("totals").getJSONObject(2);
                                    tot = response.getJSONObject("data").getJSONArray("totals").getJSONObject(3);
                                }
                                vat.setText(vatobj.getString("text"));
                                if (app.getLanguage().equals("en-GB")){
                                    vattext.setText("VAT ("+vatobj.getString("title")+")");
                                }else{
                                    vattext.setText("الضريبة "+"("+vatobj.getString("title")+")");
                                }


                                total.setText(tot.getString("text"));
                                app.setItemInCart(true);
                                spinner.setVisibility(View.GONE);



                            } else {
                                subtotal.setText("0");
                                total.setText("0");
                                vat.setText("0");
                                new SweetAlertDialog(CartActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(context.getString(R.string.Cartisempty))
                                        .show();
                                app.setItemInCart(false);
                                spinner.setVisibility(View.GONE);
                            }
                            //    adapter = new ShoppingCartAdapter(CartActivity.this,0 , canarray);

                            //  list.setAdapter(adapter);
                            String x = "";
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


                // params.put("Authorization", "Bearer ee5375799c60356b872be97edaf836b8c91d3134" );
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public synchronized void getcart2() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/cart/cart", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                JSONArray productslist = new JSONArray();
                                JSONObject sub = response.getJSONObject("data").getJSONArray("totals").getJSONObject(0);
                                JSONObject vatobj = response.getJSONObject("data").getJSONArray("totals").getJSONObject(1);
                                JSONObject tot = response.getJSONObject("data").getJSONArray("totals").getJSONObject(2);
                                subtotal.setText(sub.getString("text"));
                                if (vatobj.getString("text").contains("-")){
                                    add.setEnabled(false);
                                    add.setFocusable(false);
                                    add.setAlpha((float)0.5);
                                    dislayout.setVisibility(View.GONE);
                                    dislin.setVisibility(View.VISIBLE);
                                    discount.setText(response.getJSONObject("data").getJSONArray("totals").getJSONObject(1).getString("text"));
                                    subtotal.setText(response.getJSONObject("data").getJSONArray("totals").getJSONObject(0).getString("text"));
                                    vatobj = response.getJSONObject("data").getJSONArray("totals").getJSONObject(2);
                                    tot = response.getJSONObject("data").getJSONArray("totals").getJSONObject(3);
                                }
                                vat.setText(vatobj.getString("text"));
                                if (app.getLanguage().equals("en-GB")){
                                    vattext.setText("VAT ("+vatobj.getString("title")+")");
                                }else{
                                    vattext.setText("الضريبة "+"("+vatobj.getString("title")+")");
                                }
                                total.setText(tot.getString("text"));
                                app.setItemInCart(true);
                                spinner.setVisibility(View.GONE);
                            } else {
                                subtotal.setText("0");
                                total.setText("0");
//                                new SweetAlertDialog(CartActivity.this, SweetAlertDialog.WARNING_TYPE)
//                                        .setTitleText(context.getString(R.string.Cartisempty))
//                                        .show();
                                app.setItemInCart(false);
                                spinner.setVisibility(View.GONE);
                            }
                            //    adapter = new ShoppingCartAdapter(CartActivity.this,0 , canarray);

                            //  list.setAdapter(adapter);
                            String x = "";
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


                // params.put("Authorization", "Bearer ee5375799c60356b872be97edaf836b8c91d3134" );
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public synchronized void updatequan(final String pid, final String qun) {
        JSONObject q = new JSONObject();
        try {
            q.put("key",pid+"::");
            q.put("quantity",qun);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, "https://springrose.com.sa/index.php?route=rest/cart/updatecartv2", q, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                getcart2();
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


                // params.put("Authorization", "Bearer ee5375799c60356b872be97edaf836b8c91d3134" );
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public synchronized void deleteitem( String pid) {
        JSONObject quantity = new JSONObject();

        JSONObject q = new JSONObject();
        try {
            String key = pid+"::";
            q.put(key,"0");
            quantity.put("quantity",q);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, "https://springrose.com.sa/index.php?route=rest/cart/cart", quantity, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String s = response.toString();
                            getcart2();

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

    public void docoupon() {

        JSONObject usercre = new JSONObject();
        try {
            usercre.put("coupon", code.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/cart/coupon", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                intent.putExtra("redeemed",true);
                                startActivity(intent);
                                finish();

                            } else {
                                code.setError(context.getString(R.string.Incorrect));
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

    public synchronized void  docouponfromremote(final String onlinecode) {

        JSONObject usercre = new JSONObject();
        try {
            usercre.put("coupon", onlinecode);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/cart/coupon", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                Intent intent = new Intent(getApplicationContext(), CartActivity.class);
                                intent.putExtra("redeemed",true);
                                intent.putExtra("donefromremote",donefromremote);
                                intent.putExtra("donefrommazaya",donefrommazaya);
                                intent.putExtra("onlinecode",onlinecode);
                                startActivity(intent);
                                finish();

                            } else {
                                donefromremote=true;
                                //code.setError(context.getString(R.string.Incorrect));
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
}
