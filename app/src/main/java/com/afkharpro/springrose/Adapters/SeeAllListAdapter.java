package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 23-May-17.
 */

public class SeeAllListAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;
    App app = App.getInstance();
    boolean odd = false;
    JSONObject property;



    public SeeAllListAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.rentalProperties = objects;
    }

    //called when rendering the list
    public View getView(final int position, View convertView, final ViewGroup parent) {


        //get the property we are displaying

        property = rentalProperties.get(position);



       ViewHolder holder;

        if(convertView==null) {

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.simple_list_item_1, null);
            holder = new ViewHolder();
            holder.SourceImageMain = (ImageView) convertView.findViewById(R.id.imageView2);

            holder.code = (TextView) convertView.findViewById(R.id.coderef);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.discount = (TextView) convertView.findViewById(R.id.discountprice);
            holder.oriprice = (TextView) convertView.findViewById(R.id.oriprice);

            holder.ribbon = (ImageView) convertView.findViewById(R.id.ribbon);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //get the inflater and inflate the XML layout for each item





        try {
            Picasso.get().load(property.getString("image")).placeholder(R.drawable.loading_icon).into(holder.SourceImageMain);
            holder.code.setText(property.getString("name"));
            holder.price.setText(property.getString("price_formated"));

            holder.ribbon.setVisibility(View.GONE);

            String price = property.getString("price_formated");
            String originalprice = property.getString("price_formated");
            if (!property.getString("special_formated").equals("false")){
                price = property.getString("special_formated");
            }else{
                price = property.getString("price_formated");
            }
            String quantity = property.getString("quantity");

            if (!price.equals(originalprice)){
                if (app.getLanguage().contains("en")){
                    Picasso.get().load(R.drawable.discount_ribbon_en).error(R.drawable.loading_icon).into(holder.ribbon);
                }else{
                    Picasso.get().load(R.drawable.discount_ribbon_ar).error(R.drawable.loading_icon).into(holder.ribbon);
                }

                double p = Double.parseDouble(price.replace("SAR","").replace(",","").trim());
                double or = Double.parseDouble(originalprice.replace("SAR","").replace(",","").trim());

                float percentage = (float)((p / or) * 100);
                String per =percentage+"";
                int spacePos = per.indexOf(".");
                String youString= per.substring(0, spacePos);
                int finalper = 100 - Integer.parseInt(youString);

                holder.discount.setText(finalper+"%");
                RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(context, R.anim.myanim);
                ranim.setFillAfter(true); //For the textview to remain at the same place after the rotation
                holder.discount.setAnimation(ranim);
                holder.ribbon.setVisibility(View.VISIBLE);


                holder.oriprice.setVisibility(View.VISIBLE);
                holder.price.setText(price);
                holder.oriprice.setText(originalprice);
                holder.oriprice.setPaintFlags(holder.oriprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                holder.oriprice.setText("");

                holder.ribbon.setVisibility(View.GONE);

                if (quantity.equals("0")){
                    if (app.getLanguage().contains("en")){
                        Picasso.get().load(R.drawable.ribbonsold_en).error(R.drawable.loading_icon).into(holder.ribbon);
                    }else{
                        Picasso.get().load(R.drawable.ribbonsold_ar).error(R.drawable.loading_icon).into(holder.ribbon);
                    }

                    holder.ribbon.setVisibility(View.VISIBLE);
                }
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class ViewHolder {
        TextView oriprice ;
        TextView discount ;
        ImageView SourceImageMain;
        ImageView ribbon;
        TextView code;
        TextView price;


    }
}
