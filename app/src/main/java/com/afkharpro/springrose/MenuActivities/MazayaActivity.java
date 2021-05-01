package com.afkharpro.springrose.MenuActivities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.Models.Currency;
import com.afkharpro.springrose.Models.Mazaya;
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.OrderFlow.CartActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.ShopActivity;
import com.afkharpro.springrose.WebActivity;
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

public class MazayaActivity extends AppCompatActivity {
    Context context;
    TextView visit;
    AutoCompleteTextView memberID;
    Button link;
    Mazaya curr;
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mazaya);
        context = getApplicationContext();
        visit = (TextView)findViewById(R.id.visit) ;
        memberID = (AutoCompleteTextView) findViewById(R.id.memberID);
        link = (Button) findViewById(R.id.link);

//        curr = Mazaya.findById(Mazaya.class, (long)1);
//        if (curr !=null){
//            memberID.setText(curr.MID);
//        }
        getmazaya();
        link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(memberID.getText().toString())){
                    memberID.setError(getApplicationContext().getString(R.string.Invalid));
                }else{
                    link.setClickable(false);
                    link.setAlpha((float)0.5);
                    linktheid();
                }

            }
        });

        Typeface face = Typeface.createFromAsset(getAssets(),                 "tajawalregular.ttf");
        ActionBar ab = getSupportActionBar();
        ab.setTitle(context.getString(R.string.Mazaya));
        TextView tv = new TextView(getApplicationContext());
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT, // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT); // Height of TextView
        tv.setLayoutParams(lp);
        tv.setText(ab.getTitle());
        tv.setTextColor(Color.WHITE);
        tv.setGravity(Gravity.CENTER);
        tv.setTypeface(face);
        tv.setTextSize((float)24);
        ab.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        ab.setCustomView(tv);


        visit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), WebActivity.class);
                intent.putExtra("title",context.getString(R.string.Mazaya));
                intent.putExtra("link","http://mymazaya.com/en/User/Login");
                startActivity(intent);
            }
        });
    }

    public void linktheid(){



        JSONObject usercre = new JSONObject();
        try {
            usercre.put("3", memberID.getText().toString());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsObjRequest = new JsonObjectRequest
                (Request.Method.POST, "https://springrose.com.sa/index.php?route=rest/account/mazaya", usercre, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            link.setClickable(true);
                            link.setAlpha((float)1);
                            if (response.getBoolean("success")){
//                                curr = Mazaya.findById(Mazaya.class, (long)1);
//                                if (curr==null){
//                                    Mazaya currnew = new Mazaya(memberID.getText().toString());
//                                    currnew.MID=memberID.getText().toString();
//                                    currnew.save();
//                                }else{
//                                    curr.MID=memberID.getText().toString();
//                                    curr.save();
//                                }

                                new SweetAlertDialog(MazayaActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(context.getString(R.string.MazayaLoyaltyProgram))
                                        .setContentText(context.getString(R.string.YourMemberIDLinkedSuccessfully))
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismissWithAnimation();
                                                finish();
                                            }
                                        })
                                        .show();


                            }else{
                                JSONObject err = response.getJSONObject("error");
                                memberID.setError(err.getString("mazayaid"));

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

    public void getmazaya() {

        jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, "https://springrose.com.sa/index.php?route=rest/account/account", null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String s = response.toString();
                            Bundle bundle;
                            if (response.getBoolean("success")) {
                                String memid = "";
                                try {
                                   memid = response.getJSONObject("data").getJSONObject("account_custom_field").getString("3");
                                }catch (Exception e){

                                }



                                memberID.setText(memid);


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
