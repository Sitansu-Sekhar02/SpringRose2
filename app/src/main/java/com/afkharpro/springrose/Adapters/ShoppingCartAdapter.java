package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Models.ShoppingCartItem;
import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 16-May-17.
 */

public class ShoppingCartAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;
    App app = App.getInstance();
    TextView quan;
    JSONObject property;
    Button plus, minus;
    Handler mHandler;

    public ShoppingCartAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.rentalProperties = objects;
        mHandler = new Handler();
    }

    //called when rendering the list
    public View getView(final int position, View convertView, final ViewGroup parent) {

        //get the property we are displaying
        property = rentalProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.shoppingcart_item, null);

        ImageView SourceImageMain = (ImageView) view.findViewById(R.id.productpic);
        ImageView close = (ImageView) view.findViewById(R.id.close);
        TextView code = (TextView) view.findViewById(R.id.productcode);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView del = (TextView) view.findViewById(R.id.del);
        TextView msg = (TextView) view.findViewById(R.id.msg);
        quan = (TextView) view.findViewById(R.id.quantity);
        plus = (Button) view.findViewById(R.id.plus);
        minus = (Button) view.findViewById(R.id.minus);


        LinearLayout ball = (LinearLayout) view.findViewById(R.id.ball);
        LinearLayout choc = (LinearLayout) view.findViewById(R.id.choc);
        LinearLayout linmsg = (LinearLayout) view.findViewById(R.id.linmsg);
        LinearLayout lintime = (LinearLayout) view.findViewById(R.id.lintime);
        TextView balltext = (TextView) view.findViewById(R.id.balltext);
        TextView choctext = (TextView) view.findViewById(R.id.choctext);
        TextView timetext = (TextView) view.findViewById(R.id.time);


        try {
            Picasso.get().load(property.getString("thumb")).placeholder(R.drawable.loading_icon).into(SourceImageMain);

            code.setText(property.getString("name"));
            JSONArray arr = property.getJSONArray("option");

            del.setText(context.getResources().getString(R.string.DeliveryDate)+" : " + arr.getJSONObject(0).getString("value"));
            timetext.setText(context.getResources().getString(R.string.DeliveryDetails)+" : " + arr.getJSONObject(1).getString("value"));

            price.setText(property.getString("price"));
            quan.setText(property.getString("quantity"));

            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag("close");

                    ((ListView) parent).performItemClick(view, position, 0);
                }
            });


            plus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag("plus");
                    setbtn();
                    ((ListView) parent).performItemClick(view, position, 0);
                }
            });

            minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setbtn();
                    view.setTag("minus");
                    ((ListView) parent).performItemClick(view, position, 0);
                }
            });

             if (arr.length()>2) {
                 if (arr.getJSONObject(2).getString("name").equals("Choose a Card")||arr.getJSONObject(2).getString("name").equals("اختر البطاقة")) {
                     msg.setText(context.getResources().getString(R.string.MessageCard) + " : " + arr.getJSONObject(3).getString("value"));
                     if (arr.length() > 4) {
                         try {
                             if (arr.length() == 5) {
                                 choc.setVisibility(View.VISIBLE);
                                 choctext.setText(context.getResources().getString(R.string.Chocolate) + " : " + arr.getJSONObject(4).getString("value"));
                             } else if (arr.length() == 6) {
                                 choc.setVisibility(View.VISIBLE);
                                 ball.setVisibility(View.VISIBLE);
                                 choctext.setText(context.getResources().getString(R.string.Chocolate) + " : " + arr.getJSONObject(4).getString("value"));
                                 balltext.setText(context.getResources().getString(R.string.Balloons) + " : " + arr.getJSONObject(5).getString("value"));
                             }

                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     } else {
                         ball.setVisibility(View.GONE);
                         choc.setVisibility(View.GONE);
                     }
                 } else {
                     linmsg.setVisibility(View.GONE);
                     if (arr.length() > 2) {

                         try {
                             if (arr.length() == 3) {
                                 choc.setVisibility(View.VISIBLE);
                                 choctext.setText(context.getResources().getString(R.string.Chocolate) + " : " + arr.getJSONObject(2).getString("value"));
                             } else if (arr.length() == 4) {
                                 choc.setVisibility(View.VISIBLE);
                                 ball.setVisibility(View.VISIBLE);
                                 choctext.setText(context.getResources().getString(R.string.Chocolate) + " : " + arr.getJSONObject(2).getString("value"));
                                 balltext.setText(context.getResources().getString(R.string.Balloons) + " : " + arr.getJSONObject(3).getString("value"));
                             }

                         } catch (Exception e) {
                             e.printStackTrace();
                         }
                     }
                 }
             }else{
                 ball.setVisibility(View.GONE);
                 choc.setVisibility(View.GONE);
                 linmsg.setVisibility(View.GONE);
             }



        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    public void setbtn() {
        plus.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                plus.setEnabled(true);
            }
        }, 1000);
        minus.setEnabled(false);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                minus.setEnabled(true);
            }
        }, 1000);
    }
}





