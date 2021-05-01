package com.afkharpro.springrose.PopUps;

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
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AddShippingAddressActivity extends Activity {
    String shipping;
    JSONObject ship;
    EditText shippingaddress;
    Button add;
    App app = App.getInstance();
    JsonObjectRequest jsObjRequest;
    Context context;
    boolean ispayment,isuser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_add_shipping_address);
        context = getApplicationContext();
        shippingaddress = (EditText) findViewById(R.id.shipping);
        add = (Button) findViewById(R.id.addship);


        Intent intent = getIntent();
        shipping = intent.getStringExtra("shipping");
        ispayment = intent.getBooleanExtra("ispayment", false);
        isuser = intent.getBooleanExtra("isuser", false);
        if (ispayment) {
            shippingaddress.setHint(context.getString(R.string.PaymentAddress));
        }

        if (isuser) {
            shippingaddress.setHint(context.getString(R.string.Address));
        }
        try {
            ship = new JSONObject(shipping);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!shippingaddress.getText().toString().equals("")) {
                    if (isuser){
                        douseraddress();
                    }else{
                        if (ispayment) {
                            dopaymentaddress();
                        } else {
                            doshippingaddress();
                        }
                    }



                }
            }
        });


    }

    public void douseraddress() {


        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", ship.getString("firstname"));
            usercre.put("lastname", ship.getString("lastname"));
            usercre.put("city", ship.getString("city"));
            usercre.put("address_1", shippingaddress.getText().toString());
            usercre.put("country_id", ship.getString("country_id"));
            usercre.put("postcode", ship.getString("postcode"));
            usercre.put("zone_id", Integer.parseInt(ship.getString("zone_id")));



        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/account/address", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                finish();

                            } else {

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

    public void doshippingaddress() {


        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", ship.getString("firstname"));
            usercre.put("lastname", ship.getString("lastname"));
            usercre.put("city", ship.getString("city"));
            usercre.put("address_1", shippingaddress.getText().toString());
            usercre.put("country_id", ship.getString("country_id"));
            usercre.put("postcode", ship.getString("postcode"));
            usercre.put("zone_id", Integer.parseInt(ship.getString("zone_id")));
            usercre.put("shipping_address", "new");


        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/shipping_address/shippingaddress", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                finish();

                            } else {

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

    public void dopaymentaddress() {


        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", ship.getString("firstname"));
            usercre.put("lastname", ship.getString("lastname"));
            usercre.put("city", ship.getString("city"));
            usercre.put("address_1", shippingaddress.getText().toString());
            usercre.put("country_id", ship.getString("country_id"));
            usercre.put("postcode", ship.getString("postcode"));
            usercre.put("zone_id", Integer.parseInt(ship.getString("zone_id")));


        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/payment_address/paymentaddress", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                finish();

                            } else {

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
