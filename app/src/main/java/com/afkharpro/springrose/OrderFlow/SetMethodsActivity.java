package com.afkharpro.springrose.OrderFlow;

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
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.PaymentAddressAdapter;
import com.afkharpro.springrose.Adapters.WishlistAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Models.AccessToken;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SetMethodsActivity extends AppCompatActivity {
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    TextView test;
    ListView shiplist;
    ListView paylist;
    WishlistAdapter adapter;
    ArrayList<JSONObject> productsarr;
    ArrayList<JSONObject> productsarr2;
    TextView subtotal, total;
    GridView gridView;
    ProgressBar spinner;
    JSONObject shipping;
    JSONObject payment;
    Button addshipping, addpayment, checkout;
    ArrayList<String> my_arrayship, my_arraypay;
    String paystyle;
    int pos;
    SweetAlertDialog loading;
    CheckBox agree;
    EditText msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        setContentView(R.layout.activity_set_methods);
        context = getApplicationContext();
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        shiplist = (ListView) findViewById(R.id.listView2);
        paylist = (ListView) findViewById(R.id.listView3);
        checkout = (Button) findViewById(R.id.checkout);
        msg = (EditText)findViewById(R.id.msg) ;
        Typeface face = Typeface.createFromAsset(getAssets(),"tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("" + context.getString(R.string.OrderMethods));
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
        renewtoken();


        shiplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                shipping = productsarr2.get(i);
                view.setSelected(true);
//                new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText(context.getString(R.string.ShippingMethodSelected))
//                        .setContentText(my_arraypay.get(i))
//                        .show();
            }
        });


        paylist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                payment = productsarr.get(i);
                paystyle = my_arrayship.get(i);
                pos = i;
                view.setSelected(true);
//                new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                        .setTitleText(context.getString(R.string.PaymentMethodSelected))
//                        .setContentText(my_arrayship.get(i))
//                        .show();
            }
        });


        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shipping == null) {
                    new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.SelectShippingMethod))
                            .show();
                } else if (payment == null) {
                    new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.SelectPaymentMethod))
                            .show();
                } else {
                    checkout.setEnabled(false);
                    if (!agree.isChecked()) {
                        checkout.setEnabled(true);
                        agree.setError("Please check");
                        loading = new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.WARNING_TYPE);
                        loading.setTitleText(context.getString(R.string.Terms));
                        loading.setContentText(context.getString(R.string.TermsMust));
                        loading.show();
                    }else {
                        loading = new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.PROGRESS_TYPE);
                        loading.setTitleText(context.getString(R.string.Loading));
                        loading.show();
                        checkout.setEnabled(false);
                        postpayment();
                    }



                }


            }
        });
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
                            getpaymentmethods();

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

    public void getpaymentmethods() {
        productsarr = new ArrayList<>();
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/payment_method/payments", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                my_arrayship = new ArrayList<>();
                                try {
                                    my_arrayship.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("bank_transfer").getString("title"));
                                    productsarr.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("bank_transfer"));
                                } catch (Exception e) {

                                }
                                try {
                                    my_arrayship.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("payfort_fort").getString("title"));
                                    productsarr.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("payfort_fort"));
                                } catch (Exception e) {

                                }
                                try {
                                    my_arrayship.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("payfort_fort_sadad").getString("title"));
                                    productsarr.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("payfort_fort_sadad"));
                                } catch (Exception e) {

                                }
                                try {
                                    my_arrayship.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("cod").getString("title"));
                                    productsarr.add(response.getJSONObject("data").getJSONObject("payment_methods").getJSONObject("cod"));
                                } catch (Exception e) {

                                }


//                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetMethodsActivity.this, android.R.layout.simple_list_item_1, my_arrayship);
//                                paylist.setAdapter(adapter);




                                ArrayAdapter<String> adapter = new PaymentAddressAdapter(context, 0, my_arrayship);
                                paylist.setAdapter(adapter);
                                spinner.setVisibility(View.GONE);
                                getShippingmethods();
                            } else {

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

    public void getShippingmethods() {
        productsarr2 = new ArrayList<>();
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/shipping_method/shippingmethods", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                my_arraypay = new ArrayList<>();
                                my_arraypay.add(response.getJSONObject("data").getJSONObject("shipping_methods").getJSONObject("free").getJSONObject("quote").getJSONObject("free").getString("title"));
                                my_arraypay.add(response.getJSONObject("data").getJSONObject("shipping_methods").getJSONObject("pickup").getJSONObject("quote").getJSONObject("pickup").getString("title"));
                                productsarr2.add(response.getJSONObject("data").getJSONObject("shipping_methods").getJSONObject("free").getJSONObject("quote").getJSONObject("free"));
                                productsarr2.add(response.getJSONObject("data").getJSONObject("shipping_methods").getJSONObject("pickup").getJSONObject("quote").getJSONObject("pickup"));
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(SetMethodsActivity.this, android.R.layout.simple_list_item_1, my_arraypay);
                                shiplist.setAdapter(adapter);
                                spinner.setVisibility(View.GONE);
                            } else {

                                spinner.setVisibility(View.GONE);
                            }

                            shipping = productsarr2.get(0);

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


    public void postshipping() {
        JSONObject usercre = new JSONObject();
        try {
            usercre.put("shipping_method", shipping.getString("code"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/shipping_method/shippingmethods", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {



                                    loading.dismissWithAnimation();
                                    if (pos ==0){
                                        Intent i = new Intent(context, OrderConfirmActivity.class);
                                        i.putExtra("paystyle",paystyle);
                                        i.putExtra("pos",pos);
                                        startActivity(i);
                                        checkout.setEnabled(true);
                                    }else{
                                        doconfirm();
                                    }







                            } else {
                                loading.dismissWithAnimation();
                                new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText("Shipping fail error")
                                        .setContentText(shipping.getString("code"))
                                        .show();
//                                Intent i = new Intent(context, OrderConfirmActivity.class);
//                                i.putExtra("paystyle",paystyle);
//                                i.putExtra("pos",pos);
//
//                                startActivity(i);
//                                finish();
                            }


                        } catch (Exception e) {
                            checkout.setEnabled(true);
                            e.printStackTrace();
                            loading.dismissWithAnimation();
                            new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("Shipping exception error")
                                    .show();
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


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }



    public void postpayment() {
        JSONObject usercre = new JSONObject();
        try {

            usercre.put("payment_method", payment.getString("code"));
            usercre.put("agree", 1);
            String themsg ="";
            themsg =msg.getText().toString();
            usercre.put("comment", themsg);


            int x=1;





        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/payment_method/payments", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                postshipping();
                            } else {
                                loading.dismissWithAnimation();
                                new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(context.getString(R.string.paymentfailerror))
                                        .show();
                            }


                        } catch (Exception e) {
                            checkout.setEnabled(true);
                            e.printStackTrace();
                            loading.dismissWithAnimation();
                            new SweetAlertDialog(SetMethodsActivity.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("payment exception error")
                                    .show();
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


    public void doconfirm() {




        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/confirm/confirm", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                Intent i = new Intent(context, PaymentActivity.class);
                                i.putExtra("pos",pos);
                                try{
                                    i.putExtra("orderid",response.getJSONObject("data").getDouble("order_id"));
                                }catch (Exception e){

                                }

                                startActivity(i);
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


        jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }
}
