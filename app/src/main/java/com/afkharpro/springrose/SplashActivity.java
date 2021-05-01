package com.afkharpro.springrose;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import com.rampo.updatechecker.UpdateChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.afkharpro.springrose.Adapters.HozitemAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.MenuActivities.AccountActivity;
import com.afkharpro.springrose.MenuActivities.ProfileActivity;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.Models.Country;
import com.afkharpro.springrose.Models.Currency;

import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.Models.Language;
import com.afkharpro.springrose.Models.Noti;
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.Models.Zones;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;
import com.rampo.updatechecker.notice.Notice;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SplashActivity extends Activity {
    JsonObjectRequest jsObjRequest;
    String url = "https://springrose.com.sa/index.php?route=feed/rest_api/gettoken&grant_type=client_credentials";
    App app = App.getInstance();
    Context context;
    Users users;
    boolean english;
    boolean loaded =true;
    int x,y;
    int oid =0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        context = getApplicationContext();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        Currency curr = Currency.findById(Currency.class, (long) 1);
        if (curr == null) {
            app.setCurrency("SAR");
        } else {
            app.setCurrency(curr.currency);
        }


        Noti noti = Noti.findById(Noti.class, (long) 1);
        if (noti == null) {
            Noti notinew = new Noti(true);
            notinew.turnedon = true;
            notinew.save();
        }


        app.getArrangments().clear();
        app.getHand().clear();
        app.getArtificial().clear();
        app.getSpecialities().clear();
        app.getLove().clear();
        app.getMother().clear();
        app.getGraduation().clear();
        app.getNewborn().clear();
        app.getBirthday().clear();


//

        Language lang = Language.findById(Language.class, (long) 1);
        if (lang == null) {
            app.setLanguage("ar");
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            conf.setLocale(new Locale("ar-rSA"));
            res.updateConfiguration(conf, dm);
        } else {
            app.setLanguage(lang.lang);
            Resources res = context.getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            android.content.res.Configuration conf = res.getConfiguration();
            if (app.getLanguage().contains("en")){
                conf.setLocale(new Locale("en-rGB"));
            }else{
                conf.setLocale(new Locale(app.getLanguage()));
            }

            res.updateConfiguration(conf, dm);
        }



        getToken();

    }


    public void getToken() {

        AccessToken ac = AccessToken.findById(AccessToken.class, (long) 1);
        if (ac != null) {
            app.setAccess_token(ac.token);
        }
        JSONObject obj = new JSONObject();
        try {
            obj.put("old_token", app.getAccess_token());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, url, obj, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            app.setAccess_token(response.getString("access_token"));
                            AccessToken curr = AccessToken.findById(AccessToken.class, (long) 1);

                            if (curr == null) {
                                AccessToken currnew = new AccessToken(response.getString("access_token"));
                                currnew.save();
                            } else {
                                curr.token = app.getAccess_token();
                                curr.save();
                            }

                            getsilders();


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("#########SS", e.getMessage());
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

    public void getsilders() {
        app.getSlidersarr().clear();
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=feed/rest_api/slideshows", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                JSONArray productslist = new JSONArray();
                                productslist = response.getJSONArray("data").getJSONObject(0).getJSONArray("banners");
                                for (int i = 0; i < productslist.length(); i++) {
                                    app.getSlidersarr().add(productslist.getJSONObject(i));
                                }


                            } else {


                            }

                            Language lang = Language.findById(Language.class, (long) 1);
                            if (lang != null) {
                                if (lang.lang.equals("en-GB")) {
                                    english = true;
                                    setlanguage("en-GB");
                                } else {
                                    english = false;
                                    setlanguage("ar");
                                }
                            } else {
                                setlanguage("ar");
                                english = false;
                            }
                            getCountries();


//                            try {
//                                users = Users.findById(Users.class, (long) 1);
//                                if (users != null || !users.userid.equals("0")) {
//                                    logout();
//                                   dologin(users.email,users.password);
//                                }else{
//                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                    startActivity(intent);
//                                    finishAffinity();
//                                }
//                            } catch (Exception e) {
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                                finishAffinity();
//                            }

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

    public void callbigDATA() {


  //  cathozilist(59);
        domainstuff();
        Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
        intent.putExtra("lang", english);
        startActivity(intent);
        finishAffinity();


//        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//        intent.putExtra("lang", english);
//        startActivity(intent);
//        finishAffinity();



    }

   public void domainstuff(){
       Intent i = getIntent();
       english = i.getBooleanExtra("lang", false);

       oid = i.getIntExtra("oid",0);


       Language lang = Language.findById(Language.class, (long) 1);
       if (lang != null) {
           if (lang.lang.equals("en-gb")) {
               english = true;
               setlanguage("en-gb");
           } else {
               english = false;
               setlanguage("ar");
           }
       } else {
           String s = Locale.getDefault().getDisplayLanguage();
           if (s.contains("En")){
               setlanguage("en-rGB");
               english = true;
               Language lannew = new Language("en-gb");
               lannew.save();
           }else{
               setlanguage("ar");
               english = false;
               Language lannew = new Language("ar");
               lannew.save();
           }

       }



       if (ContextCompat.checkSelfPermission(SplashActivity.this,
               android.Manifest.permission.ACCESS_FINE_LOCATION)
               != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(SplashActivity.this,
                   new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                   1001);
       }

       if (ContextCompat.checkSelfPermission(SplashActivity.this,
               android.Manifest.permission.CALL_PHONE)
               != PackageManager.PERMISSION_GRANTED) {
           ActivityCompat.requestPermissions(SplashActivity.this,
                   new String[]{Manifest.permission.CALL_PHONE},
                   1002);
       }


       final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

       if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
           buildAlertMessageNoGps();

       }

       UpdateChecker checker = new UpdateChecker(this);
       checker.setNotice(Notice.DIALOG);
       checker.start();
       getcart();


   }

    public synchronized void  getZONES(int cid) {

        if (cid==0){
           // callbigDATA();
        }

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=feed/rest_api/countries&id="+cid, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Zones zo;
                                JSONArray productslist = new JSONArray();
                                productslist = response.getJSONObject("data").getJSONArray("zone");
                                for (int i = productslist.length()-1; i >= 0; i--) {
                                    zo = new Zones();
                                    zo.setID(Integer.parseInt(productslist.getJSONObject(i).getString("zone_id")));
                                    zo.setName(productslist.getJSONObject(i).getString("name"));
                                    zo.setCountryID(productslist.getJSONObject(i).getString("country_id"));
                                    app.getZonesArrayList().add(zo);
                                }

                                y++;
                                if (y==x){
                                    callbigDATA();
                                }
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

    public void getCountries() {
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=feed/rest_api/countries", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {
                                Country zo;
                                JSONArray productslist = new JSONArray();
                                productslist = response.getJSONArray("data");
                               x = productslist.length();
                                for (int i = productslist.length()-1; i >= 0; i--) {
                                    zo = new Country();
                                    zo.setID(Integer.parseInt(productslist.getJSONObject(i).getString("country_id")));
                                    zo.setName(productslist.getJSONObject(i).getString("name"));
                                    getZONES(zo.getID());
                                    app.getCountryArrayList().add(zo);

                                }


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

    public void setlanguage(String l) {
        app.setLanguage(l);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        android.content.res.Configuration conf = res.getConfiguration();
        if (l.contains("en")){
            conf.setLocale(new Locale("en-rGB"));
        }else{
            conf.setLocale(new Locale(l));
        }

        res.updateConfiguration(conf, dm);

    }

    public void getcart() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/cart/cart", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                JSONArray productslist = new JSONArray();
                                app.setItemInCart(true);
                            } else {
                                app.setItemInCart(false);

                            }

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


    private void askForPermission(String permission, Integer requestCode) {
        if (ContextCompat.checkSelfPermission(SplashActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(SplashActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            //  Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
    }




    public void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.YourGPSseemstobedisabled))
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.Yes), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton(getResources().getString(R.string.No), new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();

    }
}
