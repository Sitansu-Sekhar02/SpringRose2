package com.afkharpro.springrose.Fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.afkharpro.springrose.Adapters.HozitemAdapter;
import com.afkharpro.springrose.Adapters.ShoppingCartAdapter;
import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Base.MySingleton;
import com.afkharpro.springrose.OrderFlow.CartActivity;
import com.afkharpro.springrose.ProductActivity;
import com.afkharpro.springrose.R;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by murdi on 09-May-17.
 */

public class SilderFragment extends Fragment {
    JsonObjectRequest jsObjRequest;
    App app = App.getInstance();

    private int identifier;
    Context context;
    View rowView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        context = getContext();
        identifier = args.getInt("identifier");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {



        ViewHolder holder;


        if(rowView==null) {

            rowView = inflater.inflate(R.layout.slider, container, false);

            holder = new ViewHolder();
            holder.im = (ImageView) rowView.findViewById(R.id.sliderimg);

            rowView.setTag(holder);
        }else{
            holder = (ViewHolder) rowView.getTag();
        }


//        for (int i = 0; i < app.getSlidersarr().size(); i++) {
//            if (identifier == i) {
//                try {
//                    Picasso.with(context).load(app.getSlidersarr().get(i).getString("image")).into(im);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        holder.im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(context, ProductActivity.class);
//                try {
//                    if (!TextUtils.isEmpty(app.getSlidersarr().get(identifier).getString("product_id"))) {
//                        intent.putExtra("id", Integer.parseInt(app.getSlidersarr().get(identifier).getString("product_id")));
//
//                        startActivity(intent);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }



               // v.setTag("image");




            }
        });

        if (identifier == 0) {

            try {
                Picasso.get().load(app.getSlidersarr().get(0).getString("image")).into(holder.im);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (identifier == 1) {
            try {
                Picasso.get().load(app.getSlidersarr().get(1).getString("image")).into(holder.im);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (identifier == 2) {

            try {
                Picasso.get().load(app.getSlidersarr().get(2).getString("image")).into(holder.im);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (identifier == 3) {

            try {
                Picasso.get().load(app.getSlidersarr().get(3).getString("image")).into(holder.im);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (identifier == 4) {

            try {
                Picasso.get().load(app.getSlidersarr().get(4).getString("image")).into(holder.im);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (identifier == 5) {

            try {
                Picasso.get().load(app.getSlidersarr().get(5).getString("image")).into(holder.im);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else if (identifier == 6) {

            try {
                Picasso.get().load(app.getSlidersarr().get(6).getString("image")).into(holder.im);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }





        return rowView;
    }


    static class ViewHolder {

        ImageView im;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("dummy", true);
    }


}
