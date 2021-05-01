package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 09-May-17.
 */

public class HozitemAdapter extends ArrayAdapter<Hozitem> {

    private Context context;
    private List<Hozitem> rentalProperties;

App app = App.getInstance();
    Hozitem property;

    public HozitemAdapter(Context context, int resource, ArrayList<Hozitem> objects) {
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
            holder.coderef = (TextView) convertView.findViewById(R.id.coderef);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.discount = (TextView) convertView.findViewById(R.id.discountprice);
            holder.oriprice = (TextView) convertView.findViewById(R.id.oriprice);
            holder.SourceImageMain = (ImageView) convertView.findViewById(R.id.imageView2);
            holder.ribbon = (ImageView) convertView.findViewById(R.id.ribbon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.coderef.setText(property.getCoderef());
        holder.price.setText(property.getPrice());


        holder.ribbon.setVisibility(View.GONE);

        if (!property.getPrice().equals(property.getOriginalprice())){
            if (app.getLanguage().contains("en")){
                Picasso.get().load(R.drawable.discount_ribbon_en).error(R.drawable.loading_icon).into(holder.ribbon);
            }else{
                Picasso.get().load(R.drawable.discount_ribbon_ar).error(R.drawable.loading_icon).into(holder.ribbon);
            }

            double p = Double.parseDouble(property.getPrice().replace("SAR","").replace(",","").trim());
            double or = Double.parseDouble(property.getOriginalprice().replace("SAR","").replace(",","").trim());

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
            holder.price.setText(property.getPrice());
            holder.oriprice.setText(property.getOriginalprice());
            holder.oriprice.setPaintFlags(holder.oriprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }else{
            holder.oriprice.setText("");

            holder.ribbon.setVisibility(View.GONE);

            if (property.getQuantity().equals("0")){
                if (app.getLanguage().contains("en")){
                    Picasso.get().load(R.drawable.ribbonsold_en).error(R.drawable.loading_icon).into(holder.ribbon);
                }else{
                    Picasso.get().load(R.drawable.ribbonsold_ar).error(R.drawable.loading_icon).into(holder.ribbon);
                }

                holder.ribbon.setVisibility(View.VISIBLE);
            }
        }

//remove
       /* if (app.getLanguage().contains("en")){
            Picasso.get().load(R.drawable.discount_ribbon_en).error(R.drawable.loading_icon).into(holder.ribbon);
        }else{
            Picasso.get().load(R.drawable.discount_ribbon_ar).error(R.drawable.loading_icon).into(holder.ribbon);
        }

        double p = Double.parseDouble(property.getPrice().replace("SAR","").replace(",","").trim());
        double or = Double.parseDouble(property.getOriginalprice().replace("SAR","").replace(",","").trim());

        p = p-100;
        float percentage = (float)((p  / or) * 100);
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

        holder.price.setText("SAR "+p );
        holder.oriprice.setText(property.getOriginalprice());
        holder.oriprice.setPaintFlags(holder.oriprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);*/
//end


        Picasso.get().load(property.getImage()).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(holder.SourceImageMain);


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((ListView) parent).performItemClick(view, position, 0);
//            }
//        });


        //set address and description


        return convertView;
    }

    static class ViewHolder {
        TextView coderef;
        TextView price ;
        TextView oriprice ;
        TextView discount ;
        ImageView SourceImageMain;
        ImageView ribbon;
    }
}
