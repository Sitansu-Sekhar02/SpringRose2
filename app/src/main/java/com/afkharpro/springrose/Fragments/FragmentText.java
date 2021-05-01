package com.afkharpro.springrose.Fragments;

/**
 * Created by murdi on 10-May-17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.MainActivity;
import com.afkharpro.springrose.MenuActivities.WishListActivity;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.Models.Currency;
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.OrderFlow.SetMethodsActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.Settings.ForgotActivity;
import com.afkharpro.springrose.ShopActivity;
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
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;
import fr.ganfra.materialspinner.MaterialSpinner;

/**
 * Created by neokree on 16/12/14.
 */
public class FragmentText extends Fragment {
    private int identifier;
    AutoCompleteTextView mEmailView, firstView, lastView, teleView, passView, conpassView, cityView, shippingView, postcodeView;
    MaterialSpinner spinner;
    MaterialSpinner spinnercountry;
    private EditText mPasswordView;
    Context context;
    Button mEmailSignInButton;
    View rowView;
    App app = App.getInstance();
    JsonObjectRequest jsObjRequest;
    SweetAlertDialog pDialog;

    CheckBox agree, whats, phone;
    ArrayList<String> zones;
    ArrayList<String> country;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        context = getContext();
        identifier = args.getInt("identifier");
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (identifier == 0) {
            rowView = inflater.inflate(R.layout.register, container, false);
            spinner = (MaterialSpinner) rowView.findViewById(R.id.spinner);
            zones = new ArrayList<String>();


            for (int i = 0 ; i<app.getZonesArrayList().size();i++){
                if (app.getCountryArrayList().get(0).getID()==Integer.parseInt(app.getZonesArrayList().get(i).getCountryID())){
                    zones.add(app.getZonesArrayList().get(i).getName());
                }
            }


            spinnercountry = (MaterialSpinner) rowView.findViewById(R.id.spinnercountry);
            country = new ArrayList<String>();


            for (int i = 0 ; i<app.getCountryArrayList().size();i++){
                country.add(app.getCountryArrayList().get(i).getName());
            }


            ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, zones);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner = (MaterialSpinner) rowView.findViewById(R.id.spinner);
            spinner.setAdapter(adapter);


            ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, country);
            adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnercountry = (MaterialSpinner) rowView.findViewById(R.id.spinnercountry);
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
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_item, zones);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinner = (MaterialSpinner) rowView.findViewById(R.id.spinner);
                    spinner.setAdapter(adapter);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });


            mEmailSignInButton = (Button) rowView.findViewById(R.id.reg);
            mEmailView = (AutoCompleteTextView) rowView.findViewById(R.id.email);
            firstView = (AutoCompleteTextView) rowView.findViewById(R.id.first);
            lastView = (AutoCompleteTextView) rowView.findViewById(R.id.last);
            teleView = (AutoCompleteTextView) rowView.findViewById(R.id.tele);
            passView = (AutoCompleteTextView) rowView.findViewById(R.id.Password);
            conpassView = (AutoCompleteTextView) rowView.findViewById(R.id.conPassword);
            cityView = (AutoCompleteTextView) rowView.findViewById(R.id.city);
            shippingView = (AutoCompleteTextView) rowView.findViewById(R.id.shipping);
            postcodeView = (AutoCompleteTextView) rowView.findViewById(R.id.Postcode);
            agree = (CheckBox) rowView.findViewById(R.id.checkBox);
            whats = (CheckBox) rowView.findViewById(R.id.whats);
            phone = (CheckBox) rowView.findViewById(R.id.phone);
            spinner.getSelectedItemPosition();
            firstView.setFocusable(true);
            TextView tc = (TextView) rowView.findViewById(R.id.tc);


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

            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptRegister();
                }
            });

            tc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, TermsActivity.class);
                    startActivity(intent);

                }
            });


            return rowView;
        } else if (identifier == 1) {
            rowView = inflater.inflate(R.layout.activity_login, container, false);
            mEmailSignInButton = (Button) rowView.findViewById(R.id.email_sign_in_button);
            mEmailView = (AutoCompleteTextView) rowView.findViewById(R.id.email);
            mPasswordView = (EditText) rowView.findViewById(R.id.password);
            TextView forgot = (TextView) rowView.findViewById(R.id.forgot);
            mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    attemptLogin();
                }
            });
            forgot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ForgotActivity.class);
                    startActivity(intent);
                }
            });

            mEmailView.setFocusable(true);
            return rowView;
        }


        return null;
    }


    private void attemptRegister() {


        String email = mEmailView.getText().toString();
        String first = firstView.getText().toString();
        String last = lastView.getText().toString();
        String tele = teleView.getText().toString();
        String pass = passView.getText().toString();
        String conpass = conpassView.getText().toString();
        String city = cityView.getText().toString();
        String shipping = shippingView.getText().toString();
        String postcode = postcodeView.getText().toString();


        boolean cancel = false;
        View focusView = null;


        if (!agree.isChecked()) {
            agree.setError("Please check");

            focusView = agree;
            cancel = true;
        }


        if (!whats.isChecked() && !phone.isChecked()) {
            whats.setError("Please check");
            focusView = whats;
            cancel = true;
        }

        if (TextUtils.isEmpty(first)) {
            firstView.setError("Invalid");
            focusView = firstView;
            cancel = true;
        }

        if (TextUtils.isEmpty(postcode)) {
//            postcodeView.setError("Invalid");
//            focusView = postcodeView;
//            cancel = true;
            postcodeView.setText(".");

        }

        if (TextUtils.isEmpty(shipping)) {
            shippingView.setError("Invalid");
            focusView = shippingView;
            cancel = true;
        }


        if (TextUtils.isEmpty(tele)) {
            teleView.setError("Invalid");
            focusView = teleView;
            cancel = true;
        }

        if (TextUtils.isEmpty(last)) {
//            lastView.setError("Invalid");
//            focusView = lastView;
//            cancel = true;
            lastView.setText(".");
        }


        if (TextUtils.isEmpty(city)) {
//            cityView.setError("Invalid");
//            focusView = cityView;
//            cancel = true;
            cityView.setText(".");
        }

        if (TextUtils.isEmpty(pass) || !isPasswordValid(pass)) {
            passView.setError(getString(R.string.error_invalid_password));
            focusView = passView;
            cancel = true;
        }

        if (TextUtils.isEmpty(conpass) || !isPasswordValid(conpass)) {
            conpassView.setError(getString(R.string.error_invalid_password));
            focusView = conpassView;
            cancel = true;
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


        email = mEmailView.getText().toString();
        first = firstView.getText().toString();
        last = lastView.getText().toString();
        tele = teleView.getText().toString();
        pass = passView.getText().toString();
        conpass = conpassView.getText().toString();
        city = cityView.getText().toString();
        shipping = shippingView.getText().toString();
        postcode = postcodeView.getText().toString();


        if (cancel) {
            focusView.requestFocus();
        } else {
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            doregister(first, last, email, tele, pass, conpass, city, shipping, postcode);

        }
    }


    private void attemptLogin() {

        mEmailView.setError(null);
        mPasswordView.setError(null);


        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
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
            pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
            pDialog.setTitleText("Loading");
            pDialog.setCancelable(false);
            pDialog.show();
            dologin(email, password);

        }
    }

    public void dologin(final String email, final String password) {
        //renewtoken();
        JSONObject usercre = new JSONObject();
        try {
            usercre.put("email", email);
            usercre.put("password", password);
            usercre.put("pns", FirebaseInstanceId.getInstance().getToken());
            usercre.put("phone_type", "Android");
            usercre.put("user_lng", app.getLanguage());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/login/login", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                String userid = response.getJSONObject("data").getString("customer_id");
                                String first = response.getJSONObject("data").getString("firstname");
                                String last = response.getJSONObject("data").getString("lastname");
                                String email = response.getJSONObject("data").getString("email");
                                String tele = response.getJSONObject("data").getString("telephone");


                                Users users = Users.findById(Users.class, (long) 1);
                                if (users == null) {
                                    users = new Users(userid, first, last, email, tele, password);
                                    users.save();
                                } else {
                                    users.userid = userid;
                                    users.first = first;
                                    users.last = last;
                                    users.email = email;
                                    users.tele = tele;
                                    users.password = password;
                                    users.save();
                                }


                                pDialog.dismissWithAnimation();
                                // renewtoken();

                                Bundle bundle = new Bundle();

                                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, users.first);

                                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.LOGIN, bundle);
                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(context.getString(R.string.Loggedin))
                                        .setContentText(context.getString(R.string.Welcome) + first)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                getActivity().finish();
                                                ShopActivity.isback = true;
                                            }
                                        })
                                        .show();


                            } else {
                                logout();
                                //dologin(email, password);
                                pDialog.dismissWithAnimation();
                                String errormsg = "";
                                try {
                                    pDialog.dismissWithAnimation();
                                    JSONObject err = response.getJSONObject("error");
                                   // errormsg = err.getString("warning");
                                    new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("")
                                            .setContentText(context.getString(R.string.LoginWrong))
                                            .show();
                                    pDialog.dismissWithAnimation();
                                } catch (Exception e) {


                                }


                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                            new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("")
                                    .setContentText(context.getString(R.string.LoginWrong))
                                    .show();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pDialog.dismissWithAnimation();
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


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);

    }


    public void doregister(String first, String last, String email, String tele, final String pass, String conpass, String city, String shipping, String postcode) {
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
            usercre.put("password", pass);
            usercre.put("confirm", conpass);
            usercre.put("telephone", tele);
            usercre.put("city", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getName().toString());
            usercre.put("address_1", shipping);
            usercre.put("country_id", app.getCountryArrayList().get(spinnercountry.getSelectedItemPosition()).getID());
            usercre.put("postcode", postcode);
            usercre.put("zone_id", app.getZonesArrayList().get(spinner.getSelectedItemPosition()).getID());
            usercre.put("agree", 1);
            usercre.put("pns", FirebaseInstanceId.getInstance().getToken());
            usercre.put("phone_type", "Android");
            usercre.put("user_lng", app.getLanguage());
            usercre.put("custom_field", Custom);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/register/register", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                String userid = response.getJSONObject("data").getString("customer_id");
                                String first = response.getJSONObject("data").getString("firstname");
                                String last = response.getJSONObject("data").getString("lastname");
                                String email = response.getJSONObject("data").getString("email");
                                String tele = response.getJSONObject("data").getString("telephone");
                                Users users = Users.findById(Users.class, (long) 1);
                                if (users == null) {
                                    users = new Users(userid, first, last, email, tele, pass);
                                    users.save();

                                } else {
                                    users.userid = userid;
                                    users.first = first;
                                    users.last = last;
                                    users.email = email;
                                    users.tele = tele;
                                    users.password = pass;
                                    users.save();
                                }

                                pDialog.dismissWithAnimation();
                                // renewtoken();


                                new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(context.getString(R.string.Registered))
                                        .setContentText(context.getString(R.string.Welcome) + " " + first)
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                getActivity().finish();
                                            }
                                        })
                                        .show();

                            } else {
                                pDialog.dismissWithAnimation();
                                JSONObject err = response.getJSONObject("error");
                                String errormsg = err.getString("warning");
                                new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE)
                                        .setTitleText(errormsg)
                                        .show();
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        pDialog.dismissWithAnimation();
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

    public void logout() {
        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/logout/logout", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            pDialog.dismissWithAnimation();
                            Users users = Users.findById(Users.class, (long) 1);
                            users.userid = "0";
                            users.save();


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


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    public void message(final Context context, String msg, final JsonObjectRequest jor) {

        SweetAlertDialog pDialog = new SweetAlertDialog((Activity) context, SweetAlertDialog.WARNING_TYPE);

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
