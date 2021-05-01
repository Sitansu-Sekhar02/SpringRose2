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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.PopUps.DeliveryAddressActivity;
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
import fr.ganfra.materialspinner.MaterialSpinner;

public class GuestRegisterActivity extends AppCompatActivity {
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    Context context;
    AutoCompleteTextView mEmailView, firstView, lastView, teleView, cityView, shippingView, postcodeView;
    MaterialSpinner spinner;
    MaterialSpinner spinnercountry;
    Button mEmailSignInButton;
    CheckBox agree, whats, phone;
    ArrayList<String> zones;
    ArrayList<String> country;
    CheckBox iam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guest_register);
        context = getApplicationContext();
        Typeface face = Typeface.createFromAsset(getAssets(), "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle("" + context.getString(R.string.GuestRegistration));
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

        iam = (CheckBox)findViewById(R.id.iam);


        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        zones = new ArrayList<String>();
        for (int i = 0; i < app.getZonesArrayList().size(); i++) {
            if (app.getCountryArrayList().get(0).getID() == Integer.parseInt(app.getZonesArrayList().get(i).getCountryID())) {
                zones.add(app.getZonesArrayList().get(i).getName());
            }
        }

        spinnercountry = (MaterialSpinner) findViewById(R.id.spinnercountry);
        country = new ArrayList<String>();


        for (int i = 0; i < app.getCountryArrayList().size(); i++) {
            country.add(app.getCountryArrayList().get(i).getName());
        }


       /* ArrayAdapter<CharSequence> adapter = new ArrayAdapter(context,
                R.layout.custom_spinner,zones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setPopupBackgroundResource(R.drawable.spinner);
        spinner.setAdapter(adapter);*/

        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, R.layout.spinner_item, country);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnercountry = (MaterialSpinner) findViewById(R.id.spinnercountry);
        spinnercountry.setAdapter(adapter2);

        spinnercountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                zones = new ArrayList<String>();

                for (int i = 0; i < app.getZonesArrayList().size(); i++) {
                    if (app.getCountryArrayList().get(position).getID() == Integer.parseInt(app.getZonesArrayList().get(i).getCountryID())) {
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

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, zones);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner = (MaterialSpinner) findViewById(R.id.spinner);
        spinner.setAdapter(adapter);


        mEmailSignInButton = (Button) findViewById(R.id.reg);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        firstView = (AutoCompleteTextView) findViewById(R.id.first);
        lastView = (AutoCompleteTextView) findViewById(R.id.last);
        teleView = (AutoCompleteTextView) findViewById(R.id.tele);
        whats = (CheckBox) findViewById(R.id.whats);
        phone = (CheckBox) findViewById(R.id.phone);

        cityView = (AutoCompleteTextView) findViewById(R.id.city);
        shippingView = (AutoCompleteTextView) findViewById(R.id.shipping);
        postcodeView = (AutoCompleteTextView) findViewById(R.id.Postcode);
        agree = (CheckBox) findViewById(R.id.checkBox);
        TextView tc = (TextView) findViewById(R.id.tc);

        whats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                phone.setChecked(false);
            }
        });

        phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whats.setChecked(false);
            }
        });

        tc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, TermsActivity.class);
                startActivity(intent);
            }
        });


        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                attemptRegister();
            }
        });


    }

    private void attemptRegister() {


        String email = mEmailView.getText().toString();
        String first = firstView.getText().toString();
        String last = "";
        String tele = teleView.getText().toString();

        String city = "";
        String shipping = shippingView.getText().toString();
        String postcode = "";


        boolean cancel = false;
        View focusView = null;


        if (!agree.isChecked()) {
            agree.setError(context.getString(R.string.Pleasecheck));
            focusView = agree;
            cancel = true;
        }


        if (!whats.isChecked() && !phone.isChecked()) {
            whats.setError("Please check");
            focusView = whats;
            cancel = true;
        }

        if (TextUtils.isEmpty(first)) {
            firstView.setError(context.getString(R.string.Invalid));
            focusView = firstView;
            cancel = true;
        }

        if (TextUtils.isEmpty(postcode)) {
//            postcodeView.setError(context.getString(R.string.Invalid));
//            focusView = postcodeView;
//            cancel = true;
            postcodeView.setText(".");
            postcode =".";
        }

        if (TextUtils.isEmpty(shipping)) {
            shippingView.setError(context.getString(R.string.Invalid));
            focusView = shippingView;
            cancel = true;
        }


        if (TextUtils.isEmpty(tele)) {
            teleView.setError(context.getString(R.string.Invalid));
            focusView = teleView;
            cancel = true;
        }

        if (TextUtils.isEmpty(last)) {
//            lastView.setError(context.getString(R.string.Invalid));
//            focusView = lastView;
//            cancel = true;
            lastView.setText(".");
            last =".";
        }


        if (TextUtils.isEmpty(city)) {
//            cityView.setError(context.getString(R.string.Invalid));
//            focusView = cityView;
//            cancel = true;
            cityView.setText(".");
            city = ".";
        }


        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }



        if (cancel) {
            focusView.requestFocus();
        } else {


            doregister(first, last, email, tele, city, shipping, postcode);

        }
    }

    public void doregister(String first, String last, String email, String tele, String city, String shipping, String postcode) {
        // renewtoken();
        mEmailSignInButton.setEnabled(false);
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


        JSONObject Custom = new JSONObject();
        JSONObject Contact = new JSONObject();
        try {
            if (whats.isChecked()) {
                Contact.put("2", "2");
            } else {
                Contact.put("2", "1");
            }
            Custom.put("Preferred Contact", Contact);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", first);
            usercre.put("lastname", ".");
            usercre.put("email", email);
            usercre.put("telephone", tele);
            usercre.put("city", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getName().toString());
            usercre.put("address_1", shipping);
            usercre.put("country_id", app.getCountryArrayList().get(spinnercountry.getSelectedItemPosition()).getID());
            usercre.put("postcode", postcode);
            usercre.put("zone_id", zoneid);
            usercre.put("custom_field", Custom);
            //usercre.put("agree", 1);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/guest/guest", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                doshipping();


//                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
//                                        .setTitleText("Registered")
//                                        .setContentText("Welcome "+first)
//                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                            @Override
//                                            public void onClick(SweetAlertDialog sDialog) {
//                                                sDialog.dismissWithAnimation();
//                                                finish();
//                                            }
//                                        })
//                                        .show();

                            } else {
                                mEmailSignInButton.setEnabled(true);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            mEmailSignInButton.setEnabled(true);
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mEmailSignInButton.setEnabled(true);
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

    public void doshipping() {
        // renewtoken();
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


        String first = firstView.getText().toString();
        String tele = teleView.getText().toString();


        String shipping = shippingView.getText().toString();


        JSONObject usercre = new JSONObject();
        try {
            usercre.put("firstname", first);
            usercre.put("lastname", tele);
            usercre.put("city", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getName().toString());
            usercre.put("address_1", shipping);
            usercre.put("country_id", app.getCountryArrayList().get(spinnercountry.getSelectedItemPosition()).getID());
            usercre.put("postcode", ".");
            usercre.put("zone_id", zoneid);
            //usercre.put("agree", 1);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/guest_shipping/guestshipping", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {


                                new SweetAlertDialog(GuestRegisterActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(context.getString(R.string.Registered))
                                        .setContentText(context.getString(R.string.Welcome) + firstView.getText().toString())
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();

                                                if (iam.isChecked()) {
                                                    Intent i = new Intent(context, SetMethodsActivity.class);
                                                    startActivity(i);
                                                    finish();
                                                } else {
                                                    Intent i = new Intent(context, DeliveryAddressActivity.class);
                                                    i.putExtra("isuser", true);
                                                    startActivity(i);
                                                    finish();
                                                }

                                            }
                                        })
                                        .show();

                            } else {

                            }


                        } catch (Exception e) {
                            mEmailSignInButton.setEnabled(true);
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        mEmailSignInButton.setEnabled(true);
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

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
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

}
