package com.afkharpro.springrose;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.HozitemAdapter;
import com.afkharpro.springrose.Adapters.HozitemJSONAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Models.AccessToken;
import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.PopUps.ImageActivity;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class ProductActivity extends AppCompatActivity {
    int id;
    JsonObjectRequest jsObjRequest;
    Context context;
    App app = App.getInstance();
    String url = "https://springrose.com.sa/index.php?route=feed/rest_api/products&id=";
    ImageView productpic;
    TextView coderef, price;
    ArrayList<JSONObject> items;
    Button addtocart;
    String cardid;
    EditText desc;

    String MORNING = "826";
    String AFTERNOON = "827";
    String EVENING = "828";
    String imagelink;
    JSONArray optionarr;
    String discript;
    FirebaseRemoteConfig mFirebaseRemoteConfig;
    String share_message_en;
    String share_message_ar;
    ImageView ribbon;
    ActionBar ab;
    Typeface face;
    TextView height, length, width;
    String productcode;
    Handler mHandler;
    SweetAlertDialog sw,loading;
    Bundle bundle;
    private FirebaseAnalytics mFirebaseAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        bundle = new Bundle();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        final Intent intent = getIntent();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        context = getApplicationContext();
        ribbon = (ImageView) findViewById(R.id.ribbon);
        face = Typeface.createFromAsset(getAssets(), "tajawalregular.ttf");
        mHandler = new Handler();
        //getSupportActionBar().setTitle("Product Details");
        loading = new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        loading.setTitleText(context.getString(R.string.Loading));
        loading.show();
        ab = getSupportActionBar();

//        if (app.getLanguage().equals("ar")){
//            ab.setTitle(context.getString(R.string.Product));
//        }else{
//            ab.setTitle("               "+context.getString(R.string.Product));
//        }
        ab.setTitle("");


        mFirebaseRemoteConfig.setDefaults(R.xml.remote_config_defaults);
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                .setDeveloperModeEnabled(false)
                .build();
        mFirebaseRemoteConfig.setConfigSettings(configSettings);
        long cacheExpiration = 3600;
        if (mFirebaseRemoteConfig.getInfo().getConfigSettings().isDeveloperModeEnabled()) {
            cacheExpiration = 0;
        }
        mFirebaseRemoteConfig.fetch(cacheExpiration)
                .addOnCompleteListener(ProductActivity.this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mFirebaseRemoteConfig.activateFetched();
                        } else {

                        }
                        share_message_en = mFirebaseRemoteConfig.getString("product_share_message_en");
                        share_message_ar = mFirebaseRemoteConfig.getString("product_share_message_ar");

                    }
                });


        productpic = (ImageView) findViewById(R.id.productpic);
        //coderef = (TextView) findViewById(R.id.coderef);
        height = (TextView) findViewById(R.id.height);
        length = (TextView) findViewById(R.id.length);
        width = (TextView) findViewById(R.id.width);
        price = (TextView) findViewById(R.id.price);
        addtocart = (Button) findViewById(R.id.addtocart);
        desc = (EditText) findViewById(R.id.msg);
        id = intent.getIntExtra("id", 0);
        context = getApplicationContext();
        renewtoken2();


        productpic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(context, ImageActivity.class);
                intent1.putExtra("imagelink", imagelink);
                intent1.putExtra("desc", discript);
                startActivity(intent1);

            }
        });

        addtocart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(cardid)&&TextUtils.isEmpty(desc.getText().toString())) {
                    new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.EnterCardMessage))
                            .show();

                } else if (TextUtils.isEmpty(cardid)&&!TextUtils.isEmpty(desc.getText().toString())) {
                    new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText(context.getString(R.string.SelectCard))
                            .show();
                }

                else if (TextUtils.isEmpty(desc.getText().toString())&&TextUtils.isEmpty(cardid)) {
                    Intent intent1 = new Intent(context, SubOptionProductActivity.class);
                    intent1.putExtra("id", id);
                    intent1.putExtra("cardid", "");
                    intent1.putExtra("desc", "");
                    intent1.putExtra("options", String.valueOf(optionarr));
                    startActivity(intent1);
                } else{
                    Intent intent1 = new Intent(context, SubOptionProductActivity.class);
                    intent1.putExtra("id", id);
                    intent1.putExtra("cardid", cardid);
                    intent1.putExtra("desc", desc.getText().toString());
                    intent1.putExtra("options", String.valueOf(optionarr));
                    startActivity(intent1);
                }






            }
        });






    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.product, menu);

        MenuItem share = menu.findItem(R.id.action_share);
        MenuItem wish = menu.findItem(R.id.action_wish);


        share.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                share_message_en =  share_message_en.replaceAll("#", productcode);
                share_message_ar = share_message_ar.replaceAll("#", productcode);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                if (app.getLanguage().equals("ar")) {
                    sendIntent.putExtra(Intent.EXTRA_TEXT, share_message_ar);
                } else {
                    sendIntent.putExtra(Intent.EXTRA_TEXT, share_message_en);
                }

                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return false;
            }
        });


        wish.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.NORMAL_TYPE)
                        .setTitleText(context.getString(R.string.AddtoWishList))
                        .setContentText(context.getString(R.string.Areyousureyouwanttoadd))
                        .setConfirmText(context.getString(R.string.Yes))
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismissWithAnimation();
                                addtowish();
                            }
                        })
                        .show();
                return false;
            }
        });


        return super.onCreateOptionsMenu(menu);
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
                            addtowish();
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

    public void renewtoken2() {
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
                            getdata();
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
    public void addtowish() {
        //   renewtoken();

        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/wishlist/wishlist&id=" + id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            if (response.getBoolean("success")) {
                                sw =    new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                                sw.setTitleText(context.getString(R.string.AddedtoWishList))
                                .show();

                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (sw.isShowing()){
                                            sw.dismissWithAnimation();
                                        }
                                    }
                                },1000);




                            } else {
                                sw =  new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.ERROR_TYPE);
                                        sw.setTitleText(context.getString(R.string.Tryagainlater))
                                        .show();

                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (sw.isShowing()){
                                            sw.dismissWithAnimation();
                                        }
                                    }
                                },1000);
                            }


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
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());
                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    public void getdata() {
        //   renewtoken();
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url + id, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String arti = response.getJSONObject("data").getJSONArray("category").getJSONObject(0).getString("id");

                            String q = response.getJSONObject("data").getString("quantity");



                            if (arti.equals("72")) {
                                if (app.getLanguage().contains("en")){
                                    Drawable d = getResources().getDrawable(R.drawable.ribbon_en);
                                    ribbon.setBackground(d);
                                }else{
                                    Drawable d = getResources().getDrawable(R.drawable.ribbon_ar);
                                    ribbon.setBackground(d);
                                }

                                ribbon.setVisibility(View.VISIBLE);
                            } else {
                                ribbon.setVisibility(View.GONE);
                            }



                            if (q.equals("0")){
                                if (app.getLanguage().contains("en")){
                                    Drawable d = getResources().getDrawable(R.drawable.ribbonsold_en);
                                    ribbon.setBackground(d);
                                }else{
                                    Drawable d = getResources().getDrawable(R.drawable.ribbonsold_ar);
                                    ribbon.setBackground(d);
                                }
                                ribbon.setVisibility(View.VISIBLE);
                            } else {
                                ribbon.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {

                        }
                        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(id));


                        try {

                            JSONObject data = response.getJSONObject("data");
                            imagelink = data.getString("original_image");
                            discript = data.getString("description");
                            productcode = data.getString("name");
                            Picasso.get()
                                    .load(data.getString("image"))
                                    .placeholder(R.drawable.loading_icon)
                                    .error(R.drawable.loading_icon)
                                    .fit().centerCrop()
                                    .into(productpic);
                            String len = data.getString("length").substring(0, 2);
                            String wid = data.getString("width").substring(0, 2);
                            String heig = data.getString("height").substring(0, 2);
                            if (app.getLanguage().equals("ar")) {

                                length.setText("الارتفاع: " + len);
                                width.setText("العرض: " + wid);
                                height.setText("الطول: " + heig);
                                ab.setTitle(data.getString("name") );
                                //   coderef.setText("رمز المنتج : " + data.getString("name"));
                            } else {
                                //  coderef.setText("Product Code : " + data.getString("name"));
                                length.setText("L: " + len);
                                width.setText("W: " + wid);
                                height.setText("H: " + heig);
                                ab.setTitle(  data.getString("name"));
                            }

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

                            price.setText(data.getString("price_formated"));

                            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, productcode);

                            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);

                            optionarr = data.getJSONArray("options");
                            JSONObject overallcardsarr = optionarr.getJSONObject(optionarr.length() - 2);
                            final JSONArray cardsarr = overallcardsarr.getJSONArray("option_value");
                            items = new ArrayList<JSONObject>();
                            for (int i = 0; i < cardsarr.length(); i++) {

                                items.add(cardsarr.getJSONObject(i));
                            }


                            HozitemJSONAdapter adapter = new HozitemJSONAdapter(context, 0, items);
                            TwoWayView lvTest = (TwoWayView) findViewById(R.id.lvItems);
                            lvTest.setAdapter(adapter);

                            lvTest.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                                    new SweetAlertDialog(ProductActivity.this, SweetAlertDialog.SUCCESS_TYPE)
//                                            .setTitleText(context.getString(R.string.CardSelected))
//                                            .setContentText(context.getString(R.string.YouclickedCard) + (i + 1))
//                                            .show();
                                    try {
                                        cardid = items.get(i).getString("product_option_value_id");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

loading.dismissWithAnimation();
                        } catch (JSONException e) {
                            loading.dismissWithAnimation();
                            finish();
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
