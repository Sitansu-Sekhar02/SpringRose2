package com.afkharpro.springrose.MenuActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.http.SslError;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afkharpro.springrose.R;
import com.afkharpro.springrose.WebActivity;

public class OrderStatusActivity extends AppCompatActivity {
    WebView webview;
    ProgressBar loading;
    Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_status);
        webview = (WebView) findViewById(R.id.webview);
        loading = (ProgressBar) findViewById(R.id.progressBar);
        Intent intent = getIntent();

        Typeface face = Typeface.createFromAsset(getAssets(), "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(getApplicationContext().getString(R.string.nav_orderStatus));
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
        webview.setWebViewClient(new OrderStatusActivity.MyBrowser());
        webview.getSettings().setJavaScriptEnabled(true);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
            }
        }, 3000);

        webview.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {


            }
        });
        webview.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });

        //  webview.loadUrl("http://developer.android.com/");


        webview.loadUrl("https://springrose.com.sa/ot");

        // webview.loadUrl("https://google.com");
        checkurl(webview.getUrl());
    }

    public void checkurl(String url) {
        try {
            if (url.contains("formResponse")) {
                onBackPressed();
            } else {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        checkurl(webview.getUrl());
                    }
                }, 6000);

            }
        } catch (Exception e) {

        }

    }

    private class MyBrowser extends WebViewClient {
        @JavascriptInterface
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            return super.shouldOverrideUrlLoading(view, request);
        }


    }
}

