package com.afkharpro.springrose;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import butterknife.*;
import butterknife.ButterKnife;
import butterknife.OnClick;

import com.afkharpro.springrose.Adapters.HozitemAdapter;
import com.afkharpro.springrose.Adapters.SimpleImageAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.ImageData;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Base.SwipeListener;

import com.afkharpro.springrose.Fragments.RotateAnimator;
import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.Models.Language;
import com.afkharpro.springrose.OrderFlow.CartActivity;
import com.afkharpro.springrose.Settings.ForgotActivity;
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
import com.antonyt.infiniteviewpager.InfinitePagerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.lukedeighton.wheelview.WheelView;
import com.lukedeighton.wheelview.adapter.WheelAdapter;
import com.rampo.updatechecker.UpdateChecker;
import com.rampo.updatechecker.notice.Notice;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.lucasr.twowayview.TwoWayView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;

import cn.pedant.SweetAlert.SweetAlertDialog;
import github.hellocsl.cursorwheel.CursorWheelLayout;


public class MainActivity extends FragmentActivity {
    private FirebaseAnalytics mFirebaseAnalytics;
    Button shop;
    ViewPager viewPager;
    Context context;
    JsonObjectRequest jsObjRequest;
    String url = "https://springrose.com.sa/index.php?route=feed/rest_api/gettoken&grant_type=client_credentials";
    App app = App.getInstance();
    TextView en, ar;
    boolean english;
    static final Integer LOCATION = 0x1;
    ImageView im;
    ViewFlipper simpleViewFlipper;
    int wheelpos = 0;


    Button mMainButtonRadonSelected;
    Random mRandom = new Random();

    CursorWheelLayout mTestCircleMenuTop;
    GestureDetector gestureDetector;
    WheelView wheelView;
    LinearLayout thelogo;
    boolean survey;
    int oid =0 ;
    FirebaseRemoteConfig mFirebaseRemoteConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);



        Intent i = getIntent();
        english = i.getBooleanExtra("lang", false);

        shop = (Button) findViewById(R.id.btnreal);
        context = getApplicationContext();
        mFirebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        wheelView = (WheelView) findViewById(R.id.wheelview);
        im = (ImageView) findViewById(R.id.logoimage);
        en = (TextView) findViewById(R.id.en);
        thelogo = (LinearLayout) findViewById(R.id.thelogo);
        survey = i.getBooleanExtra("survey",false);





        oid = i.getIntExtra("oid",0);


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

        if (english) {
            en.setText("العربية");
        } else {
            en.setText("English");
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    1001);
        }

        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.CALL_PHONE},
                    1002);
        }


        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }


        if (survey){
            survey=false;
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
                    .addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mFirebaseRemoteConfig.activateFetched();
                            } else {

                            }


                            app.setSurlink(mFirebaseRemoteConfig.getString("survey_link"));
                            Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                            intent.putExtra("title","Survey");
                            intent.putExtra("link",app.getSurlink()+oid);
                            startActivity(intent);
                        }
                    });


        }
        UpdateChecker checker = new UpdateChecker(this);
        checker.setNotice(Notice.DIALOG);
        checker.start();

//       simpleViewFlipper = (ViewFlipper) findViewById(R.id.simpleViewFlipper);
//
//        //simpleViewFlipper.setInAnimation(this,R.anim.slide_left_in_new);
//       // simpleViewFlipper.setOutAnimation(this,R.anim.slide_left_out_new);
//
//
//
//
//
//        simpleViewFlipper.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//
//                            simpleViewFlipper.showNext();
//
//                return false;
//            }
//        });

//        mTestCircleMenuTop = (CursorWheelLayout) findViewById(R.id.test_circle_menu_top);
//        initData();


        wheelView.setAdapter(new WheelAdapter() {
            @Override
            public Drawable getDrawable(int position) {
                if (position == 0) {

                } else if (position == 1) {

                } else if (position == 2) {

                } else if (position == 3) {


                } else if (position == 4) {

                } else if (position == 5) {

                }
                return null;
            }

            @Override
            public int getCount() {
                return 6;
            }
        });

        final Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.shake);




//        wheelView.setOnWheelItemSelectedListener(new WheelView.OnWheelItemSelectListener() {
//            @Override
//            public void onWheelItemSelected(WheelView parent, Drawable itemDrawable, int position) {
//                wheelpos = position;
//
//                if (position == 0) {
//                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
//                    startActivity(intent);
//                } else if (position == 1) {
//                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
//                    startActivity(intent);
//                } else if (position == 2) {
//                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
//                    startActivity(intent);
//                } else if (position == 3) {
//                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
//                    startActivity(intent);
//                } else if (position == 4) {
//                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
//                    startActivity(intent);
//                } else if (position == 5) {
//                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
//                    startActivity(intent);
//                }
//            }
//
//
//        });


        // ar = (TextView)findViewById(R.id.ar);
//        if (app.getSlidersarr().size()==0){
//            getsilders();
//        }


        en.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (english) {
                    en.setText("English");
                    Language lang = Language.findById(Language.class, (long) 1);
                    if (lang == null) {
                        Language lannew = new Language("ar");
                        lannew.save();

                    } else {
                        lang.lang = "ar";
                        lang.save();

                    }

                    setlanguage("ar");
                    english = false;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("lang", english);
                    startActivity(intent);
                    finish();
                } else {
                    en.setText("العربية");
                    Language lang = Language.findById(Language.class, (long) 1);
                    if (lang == null) {
                        Language lannew = new Language("en-GB");
                        lannew.save();

                    } else {
                        lang.lang = "en-GB";
                        lang.save();

                    }
                    setlanguage("en-GB");
                    english = true;
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("lang", english);
                    startActivity(intent);
                    finish();
                }


            }
        });

//        ar.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ar.setAlpha((float)1);
//                en.setAlpha((float)0.5);
//
//
//
//            }
//        });




//        PagerAdapter wrappedAdapter = new InfinitePagerAdapter(adapter);
//
//        // actually an InfiniteViewPager
//        viewPager = (ViewPager) findViewById(R.id.pager);
//        viewPager.setAdapter(wrappedAdapter);
//        viewPager.setOffscreenPageLimit(1);
//        viewPager.setClipToPadding(false);
//        viewPager.setDrawingCacheEnabled(true);
//        viewPager.buildDrawingCache(false);
//
//
//        viewPager.setPageTransformer(true, new RotateAnimator(90));
//        //viewPager.setPadding(24,0,24,0);
//        // viewPager.setPageMargin(-300);
//
//
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                int currentitem = viewPager.getCurrentItem();
//                if (currentitem == 0) {
//                    shop.setText(context.getString(R.string.SHOPNOW));
//                } else if (currentitem == 1) {
//                    shop.setText(context.getString(R.string.VISITNOW));
//                } else if (currentitem == 2) {
//                    shop.setText(context.getString(R.string.VISITNOW));
//                } else if (currentitem == 3) {
//                    shop.setText(context.getString(R.string.VISITNOW));
//                } else if (currentitem == 4) {
//                    shop.setText(context.getString(R.string.VISITNOW));
//                }
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });


        //viewPager.setPageMargin(-400);

        shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shop.setEnabled(false);
                shop.setAlpha((float) 0.6);
                Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                startActivity(intent);

            }
        });

        getcart();





//        if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            askForPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, LOCATION);
//            return;
//        }


    }

    @Override
    protected void onResume() {
        shop.setEnabled(true);
        shop.setAlpha((float) 1);
        super.onResume();
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
        mFirebaseAnalytics.setUserProperty("Language", app.getLanguage());
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
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {

                //This is called if user has denied the permission before
                //In this case I am just asking the permission again
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);

            } else {

                ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
            }
        } else {
            //  Toast.makeText(this, "" + permission + " is already granted.", Toast.LENGTH_SHORT).show();
        }
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

    public void seeall(int id, String title) {
        Intent i = new Intent(context, SeeAllActivity.class);
        i.putExtra("id", id);
        i.putExtra("title", title);
        startActivity(i);

    }

}



