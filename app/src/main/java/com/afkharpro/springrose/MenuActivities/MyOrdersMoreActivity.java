package com.afkharpro.springrose.MenuActivities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.MyOrderMoreAdapter;
import com.afkharpro.springrose.Adapters.MyOrdersAdapter;
import com.afkharpro.springrose.Adapters.ShippingAddressAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Models.AccessToken;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MyOrdersMoreActivity extends AppCompatActivity {
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    ProgressBar spinner;
    String id,state;
    JSONObject data;
    MyOrderMoreAdapter adapter;
    ArrayList<JSONObject> productsarr;
    ListView list;
    TextView date,status,shipping,payment,quantity,total,address,orderdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left_in, R.anim.hold);
        setContentView(R.layout.activity_my_orders_more);
        context = getApplicationContext();

        list = (ListView) findViewById(R.id.listView2);
        //Invoiceid = (TextView)findViewById(R.id.invoiceid) ;
        date = (TextView)findViewById(R.id.date) ;
        status = (TextView)findViewById(R.id.status) ;
        shipping = (TextView)findViewById(R.id.shippingmethod) ;
        payment = (TextView)findViewById(R.id.paymentmethod) ;
        quantity = (TextView)findViewById(R.id.subtotal) ;
        total = (TextView)findViewById(R.id.total) ;
        address = (TextView)findViewById(R.id.address) ;
        orderdate = (TextView)findViewById(R.id.orderdate) ;

        Intent intent = getIntent();
       id = intent.getStringExtra("id");
        state = intent.getStringExtra("state");
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.OrderNo)+ id);
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
        renewtoken();


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
                            getorders();
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

    public void getorders() {
        productsarr = new ArrayList<>();
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/order/orders&id="+id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                data = response.getJSONObject("data");
                                JSONArray products = data.getJSONArray("products");
                                for (int i = 0; i < products.length(); i++) {
                                    productsarr.add(products.getJSONObject(i));

                                }

                                adapter = new MyOrderMoreAdapter(context, 0, productsarr);
                                list.setAdapter(adapter);


                                updateUI();

                                spinner.setVisibility(View.GONE);
                            } else {
                                new SweetAlertDialog(MyOrdersMoreActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(context.getString(R.string.Youdonthaveanyorders))
                                        .show();
                                spinner.setVisibility(View.GONE);
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

    public void updateUI(){
        try {
          //  Invoiceid.setText(data.getString("invoice_prefix")+data.getString("invoice_no"));
            date.setText(data.getString("date_added"));
            status.setText(state);
            shipping.setText(data.getString("shipping_method"));
            payment.setText(data.getString("payment_method"));
            String tots = data.getString("total");
            tots = tots.substring(0, tots.indexOf("."));
            total.setText(tots+" "+data.getString("currency_code").replaceAll("A",""));
            quantity.setText(""+data.getJSONArray("products").length());
            address.setText(context.getString(R.string.ShippingAddress)+": "+data.getString("shipping_firstname")+", "+data.getString("shipping_address_1")+", "+data.getString("shipping_zone")+", "+data.getString("telephone"));
orderdate.setText(context.getString(R.string.OrderDate));




        } catch (JSONException e) {
            e.printStackTrace();
        }
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
