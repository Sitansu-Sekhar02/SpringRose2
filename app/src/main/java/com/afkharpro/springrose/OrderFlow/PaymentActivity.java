package com.afkharpro.springrose.OrderFlow;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PaymentActivity extends AppCompatActivity {
    WebView webview;
    ProgressBar loading;

    App app = App.getInstance();
    int pos;
    Context context;
    JsonObjectRequest jsObjRequest;
    Handler mHandler;
    Map<String, String> params;
    String TAG = "Payment Page";
    double orderid=0;

    @JavascriptInterface
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        webview = (WebView) findViewById(R.id.webview);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        Intent intent = getIntent();
        pos = intent.getIntExtra("pos", 0);
        orderid = intent.getLongExtra("orderid",0);
        context = getApplicationContext();
        mHandler = new Handler();

        params = new HashMap<String, String>();
        params.put("Authorization", "Bearer " + app.getAccess_token());

        Log.v(TAG, "Authorization :" + "Bearer " + app.getAccess_token());
        params.put("X-Oc-Currency", app.getCurrency());
        Log.v(TAG, "X-Oc-Currency : " + app.getCurrency());
        params.put("X-Oc-Merchant-Language", app.getLanguage());
        Log.v(TAG, "X-Oc-Merchant-Language : " + app.getLanguage());
        params.put("Content-Type", "application/javascript");
        Log.v(TAG, "Content-Type : " + "application/javascript");
        params.put("content-encoding", "utf-8");
        Log.v(TAG, "content-encoding : " + "utf-8");

        Typeface face = Typeface.createFromAsset(getAssets(), "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.Payment));
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


        mHandler = new Handler();
        webview.setWebViewClient(new MyBrowser());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadsImagesAutomatically(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setAllowFileAccessFromFileURLs(true);
        webview.getSettings().setBlockNetworkImage(false);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36");
        //webview.addJavascriptInterface(this,"$");

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
            }
        }, 8000);

//        webview.setWebChromeClient(new WebChromeClient() {
//            @JavascriptInterface
//            public void onProgressChanged(WebView view, int progress) {
//
//
//            }
//        });
//        webview.setWebViewClient(new WebViewClient() {
//            @JavascriptInterface
//            public void onPageFinished(WebView view, String url){
//                view.getUrl();
//                loading.setVisibility(View.GONE);
//            }
//            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//
//            }
//        });


        if (pos == 1 || pos == 2) {


            webview.loadUrl("https://springrose.com.sa/index.php?route=rest/confirm/confirm&page=pay", params);

           checkfinish();

            //gettofinish();
        } else {
            gettofinish();
        }

    }

    private class MyBrowser extends WebViewClient {
        @JavascriptInterface
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.getSettings().setJavaScriptEnabled(true);
            view.getSettings().setLoadsImagesAutomatically(true);
            view.getSettings().setAllowContentAccess(true);
            view.getSettings().setAllowFileAccess(true);
            view.getSettings().setDomStorageEnabled(true);
            view.getSettings().setAllowContentAccess(true);
            view.getSettings().setAllowFileAccessFromFileURLs(true);
            view.getSettings().setBlockNetworkImage(false);
           // view.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
            view.getSettings().setUserAgentString("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36");
            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }


    }


    public synchronized void checkfinish() {
        if (webview.getUrl().contains("success")) {
            doconfirm();
        }else {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkfinish();
                }
            }, 8000);
        }



    }

    public void gettofinish() {
        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/confirm/confirm&page=pay", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            if (response.getBoolean("success")) {
                                doconfirm();

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


                // params.put("Authorization", "Bearer ee5375799c60356b872be97edaf836b8c91d3134" );
                params.put("Authorization", "Bearer " + app.getAccess_token());
                params.put("X-Oc-Currency", app.getCurrency());
                params.put("X-Oc-Merchant-Language", app.getLanguage());

                return params;
            }
        };


         jsObjRequest.setRetryPolicy(new DefaultRetryPolicy(                30000,                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }


    public void doconfirm() {



        jsObjRequest = new JsonObjectRequest
                (Request.Method.PUT, "https://springrose.com.sa/index.php?route=rest/confirm/confirm", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getBoolean("success")) {

                                Intent i = new Intent(context, FinalActivity.class);
                                String ordernum = String.valueOf(orderid).replace("E7","").replace(".","") +"";
                                i.putExtra("ordernum",ordernum);

                                startActivity(i);
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
