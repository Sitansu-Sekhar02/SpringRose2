package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
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
import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 16-May-17.
 */

public class MyOrderMoreAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;
    App app = App.getInstance();

    JSONObject property;

    public MyOrderMoreAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.rentalProperties = objects;
    }

    //called when rendering the list
    public View getView(final int position, View convertView, final ViewGroup parent) {

        //get the property we are displaying
        property = rentalProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.myordermore_item, null);

        ImageView SourceImageMain = (ImageView) view.findViewById(R.id.productpic);

        TextView code = (TextView) view.findViewById(R.id.productcode);
        TextView price = (TextView) view.findViewById(R.id.price);
      //  TextView quan = (TextView) view.findViewById(R.id.quantity);
        TextView del = (TextView) view.findViewById(R.id.del);
        TextView msg = (TextView) view.findViewById(R.id.msg);


        LinearLayout ball = (LinearLayout)view.findViewById(R.id.ball);
        LinearLayout choc = (LinearLayout)view.findViewById(R.id.choc);
        TextView balltext = (TextView) view.findViewById(R.id.balltext);
        TextView choctext= (TextView) view.findViewById(R.id.choctext);

        LinearLayout linmsg = (LinearLayout) view.findViewById(R.id.linmsg);
        LinearLayout lintime = (LinearLayout) view.findViewById(R.id.lintime);
        TextView timetext = (TextView) view.findViewById(R.id.time);


        try {
            Picasso.get().load("https://springrose.com.sa/image/"+property.getString("image")).placeholder(R.mipmap.ic_launcher).into(SourceImageMain);

        } catch (Exception e) {
            e.printStackTrace();
        }

        try{

                code.setText( property.getString("model") );

            JSONArray arr = property.getJSONArray("option");

            del.setText(context.getResources().getString(R.string.DeliveryDate)+" : " + arr.getJSONObject(0).getString("value"));
            timetext.setText(context.getResources().getString(R.string.DeliveryDetails)+" : " + arr.getJSONObject(1).getString("value"));

            price.setText(property.getString("price"));
          //  quan.setText(property.getString("quantity"));







            //del.setText(arr.getJSONObject(0).getString("name")+" : "+arr.getJSONObject(0).getString("value"));
            msg.setText(context.getResources().getString(R.string.MessageCard)+" : "+arr.getJSONObject(3).getString("value"));
           choctext.setText(context.getString(R.string.Quantity)+" : "+property.getString("quantity"));
//            if (arr.length()>3){
//
//                try{
//                    if (arr.length()==4){
//                        choc.setVisibility(View.VISIBLE);
//                        choctext.setText(arr.getJSONObject(3).getString("name"));
//                    }else if (arr.length()==5){
//                        choc.setVisibility(View.VISIBLE);
//                        ball.setVisibility(View.VISIBLE);
//                        choctext.setText(arr.getJSONObject(3).getString("name"));
//                        balltext.setText(arr.getJSONObject(4).getString("name"));
//                    }
//
//                }catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }else {
//                ball.setVisibility(View.GONE);
//                choc.setVisibility(View.GONE);
//            }
            ball.setVisibility(View.GONE);

        }catch (Exception e) {
            e.printStackTrace();
        }




        return view;
    }
}





