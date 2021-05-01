package com.afkharpro.springrose.PopUps;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.MenuActivities.ManageAddressesActivity;
import com.afkharpro.springrose.OrderFlow.SetAddressesActivity;
import com.afkharpro.springrose.OrderFlow.SetMethodsActivity;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

public class DeliveryAddressActivity extends AppCompatActivity {
    String shipping;
    JSONObject ship;
    AutoCompleteTextView shippingaddress,firstname,lastname,telephone;
    Button add;
    App app = App.getInstance();
    JsonObjectRequest jsObjRequest;
    Context context;
    boolean isuser;
    boolean isguest;


    MaterialSpinner spinner;
    ArrayList<String> zones;
    LinearLayout checklin;
    CheckBox defaultbox;
    MaterialSpinner spinnercountry;
    CheckBox iam;
    ArrayList<String> country;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_address);

        context = getApplicationContext();
        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.AddressDetails));
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



      checklin = (LinearLayout)findViewById(R.id.lin6) ;
        defaultbox = (CheckBox)findViewById(R.id.checkBox2);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
         zones = new ArrayList<String>();
        for (int i = 0 ; i<app.getZonesArrayList().size();i++){
            zones.add(app.getZonesArrayList().get(i).getName());
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, zones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);


        spinnercountry = (MaterialSpinner) findViewById(R.id.spinnercountry);
        country = new ArrayList<String>();


        for (int i = 0 ; i<app.getCountryArrayList().size();i++){
            country.add(app.getCountryArrayList().get(i).getName());
        }
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, R.layout.spinner_item, country);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercountry = (MaterialSpinner) findViewById(R.id.spinnercountry);
        spinnercountry.setAdapter(adapter2);

        spinnercountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zones = new ArrayList<String>();

                for (int i = 0 ; i<app.getZonesArrayList().size();i++){
                    if (app.getCountryArrayList().get(position).getID()==Integer.parseInt(app.getZonesArrayList().get(i).getCountryID())){
                        zones.add(app.getZonesArrayList().get(i).getName());
                    }

                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item, zones);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner = (MaterialSpinner) findViewById(R.id.spinner);
                spinner.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        firstname = (AutoCompleteTextView) findViewById(R.id.first);
        lastname = (AutoCompleteTextView) findViewById(R.id.last);
        telephone= (AutoCompleteTextView) findViewById(R.id.tele);



        shippingaddress = (AutoCompleteTextView) findViewById(R.id.shipping);
        add = (Button) findViewById(R.id.addship);


        Intent intent = getIntent();
        shipping = intent.getStringExtra("shipping");
        isuser = intent.getBooleanExtra("isuser", false);
        isguest = intent.getBooleanExtra("isguest", false);

        try {
            ship = new JSONObject(shipping);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (isuser){
            checklin.setVisibility(View.VISIBLE);
        }else{
            checklin.setVisibility(View.GONE);
        }


        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                add.setEnabled(false);
                attempt();
            }

        });
    }

    public void attempt(){
        boolean cancel = false;
        View focusView = null;





        if (TextUtils.isEmpty(shippingaddress.getText().toString())) {
            shippingaddress.setError("Invalid");
            focusView = shippingaddress;
            cancel = true;
        }

        if (TextUtils.isEmpty(firstname.getText().toString())) {
            firstname.setError("Invalid");
            focusView = firstname;
            cancel = true;
        }

        if (TextUtils.isEmpty(telephone.getText().toString())) {
            telephone.setError("Invalid");
            focusView = telephone;
            cancel = true;
        }

        if (telephone.getText().toString().length()<9) {
            telephone.setError("Invalid");
            focusView = telephone;
            cancel = true;
        }

        if (cancel) {
            add.setEnabled(true);
            focusView.requestFocus();
        } else {
            if (isuser){
                douseraddress();
            }else{
                if (isguest){
                    doguestaddress();
                }else {
                    doshippingaddress();
                }

            }
        }

    }


    public void doguestaddress() {


        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", firstname.getText().toString());
            usercre.put("lastname", telephone.getText().toString());
            usercre.put("city", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getName().toString());
            usercre.put("address_1", shippingaddress.getText().toString());
            usercre.put("country_id", app.getCountryArrayList().get(spinnercountry.getSelectedItemPosition()).getID());
            usercre.put("postcode", ".");
            usercre.put("zone_id", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getID());




        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/guest_shipping/guestshipping", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {


                                Intent i = new Intent(context, SetMethodsActivity.class);
                                startActivity(i);
                                finish();



                            } else {
                                add.setEnabled(true);
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

    public void douseraddress() {
        int zoneid = 0;
        if (spinner.getSelectedItemPosition() == 0) {
            zoneid = 2879;
        } else if (spinner.getSelectedItemPosition() == 1) {
            zoneid = 2880;
        } else if (spinner.getSelectedItemPosition() == 2) {
            zoneid = 4237;
        } else if (spinner.getSelectedItemPosition() == 3) {
            zoneid = 4236;
        }

        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", firstname.getText().toString());
            usercre.put("lastname", telephone.getText().toString());
            usercre.put("city", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getName().toString());
            usercre.put("address_1", shippingaddress.getText().toString());
            usercre.put("country_id", app.getCountryArrayList().get(spinnercountry.getSelectedItemPosition()).getID());
            usercre.put("postcode", ".");
            usercre.put("zone_id", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getID());
            if (defaultbox.isChecked()){
                usercre.put("default", 1);
            }else{
                usercre.put("default", 0);
            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/account/address", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                ManageAddressesActivity.isback=true;

                                finish();

                            } else {
                                add.setEnabled(true);
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
        int zoneid = 0;
        if (spinner.getSelectedItemPosition() == 0) {
            zoneid = 2879;
        } else if (spinner.getSelectedItemPosition() == 1) {
            zoneid = 2880;
        } else if (spinner.getSelectedItemPosition() == 2) {
            zoneid = 4237;
        } else if (spinner.getSelectedItemPosition() == 3) {
            zoneid = 4236;
        }


        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", firstname.getText().toString());
            usercre.put("lastname", telephone.getText().toString());
            usercre.put("city", zones.get(spinner.getSelectedItemPosition()).toString());
            usercre.put("address_1", shippingaddress.getText().toString());
            usercre.put("country_id", app.getCountryArrayList().get(spinnercountry.getSelectedItemPosition()).getID());
            usercre.put("postcode", ".");
            usercre.put("zone_id", zoneid);

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
                                SetAddressesActivity.isback=true;
                                finish();

                            } else {
                                add.setEnabled(true);
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
