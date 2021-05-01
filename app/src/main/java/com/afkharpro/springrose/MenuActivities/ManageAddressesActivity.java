package com.afkharpro.springrose.MenuActivities;

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
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.ShippingAddressAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.MainActivity;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.OrderFlow.SetAddressesActivity;
import com.afkharpro.springrose.PopUps.AddShippingAddressActivity;
import com.afkharpro.springrose.PopUps.DeliveryAddressActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.ShopActivity;
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

public class ManageAddressesActivity extends AppCompatActivity {
    Button add;
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    ShippingAddressAdapter adapter;
    ProgressBar spinner;
    ListView list;
    ArrayList<JSONObject> productsarr;
    int pos;
    public static boolean isback;
    String defaultaddressid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_left_in, R.anim.hold);
        setContentView(R.layout.activity_manage_addresses);
        add = (Button) findViewById(R.id.add);
        context = getApplicationContext();
        list = (ListView) findViewById(R.id.listView2);
        spinner = (ProgressBar) findViewById(R.id.progressBar);
        Typeface face = Typeface.createFromAsset(getAssets(), "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.ManageAddresses));
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
       // renewtoken();
        getShippingaddress();
        getdefaultaddress();


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pos = i;
                try {
                    if (productsarr.get(pos).getString("address_id").equals(defaultaddressid)) {
                        new SweetAlertDialog(ManageAddressesActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.Alert))
                                .setContentText(context.getString(R.string.Youcannotdeleteyourdefaultaddress))
                                .show();
                    }else {

                        new SweetAlertDialog(ManageAddressesActivity.this, SweetAlertDialog.WARNING_TYPE)
                                .setTitleText(context.getString(R.string.DeleteAddress))
                                .showCancelButton(true)
                                .setConfirmText(context.getString(R.string.Yes))
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismissWithAnimation();
                                        try {
                                            if (productsarr.size() <= 1) {

                                                new SweetAlertDialog(ManageAddressesActivity.this, SweetAlertDialog.WARNING_TYPE)
                                                        .setTitleText(context.getString(R.string.Musthaveatleastoneaddress))
                                                        .show();


                                            } else {
                                                try {
                                                    deleteAddress(productsarr.get(pos).getString("address_id"));
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                                productsarr.remove(pos);
                                                adapter.notifyDataSetChanged();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    }
                                })
                                .show();
                    }

                }catch (Exception e){

                }

            }
        });


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setEnabled(false);
                try {
                    Intent i = new Intent(context, DeliveryAddressActivity.class);
                    i.putExtra("isuser", true);
                    i.putExtra("shipping", productsarr.get(0).toString());
                    startActivity(i);
                    add.setEnabled(true);
                } catch (Exception e) {
                    add.setEnabled(true);
                }


//                Intent i = new Intent(context,AddShippingAddressActivity.class);
//                i.putExtra("isuser",true);
//                i.putExtra("shipping",productsarr.get(0).toString());
//                startActivity(i);
            }
        });
    }

    @Override
    protected void onPostResume() {
        if (isback) {
            getShippingaddress();
            getdefaultaddress();
            isback = false;
            add.setEnabled(true);
        }
        super.onPostResume();
    }


    public void deleteAddress(String addid) {


        jsObjRequest = new JsonObjectRequest
                (Request.Method.DELETE, "https://springrose.com.sa/index.php?route=rest/account/address&id=" + addid, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {


                                spinner.setVisibility(View.GONE);
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

    public void getShippingaddress() {
        productsarr = new ArrayList<>();
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/shipping_address/shippingaddress", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                JSONArray arr = response.getJSONObject("data").getJSONArray("addresses");
                              //  defaultaddressid = response.getJSONObject("data").getString("address_id");
                                for (int i = 0; i < arr.length(); i++) {
                                    productsarr.add(arr.getJSONObject(i));

                                }

                                adapter = new ShippingAddressAdapter(context, 0, productsarr);
                                list.setAdapter(adapter);
                                spinner.setVisibility(View.GONE);
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
                            getShippingaddress();
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

    public void message(final Context context, String msg, final JsonObjectRequest jor) {

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


    public synchronized void getdefaultaddress() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/account/account", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                defaultaddressid = response.getJSONObject("data").getString("address_id");
                                spinner.setVisibility(View.GONE);
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
}
