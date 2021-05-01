package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 09-May-17.
 */

public class ArtHozitemAdapter extends ArrayAdapter<Hozitem> {

    private Context context;
    private List<Hozitem> rentalProperties;


    Hozitem property;

    public ArtHozitemAdapter(Context context, int resource, ArrayList<Hozitem> objects) {
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
            convertView = inflater.inflate(R.layout.artsimple_list_item_1, null);
            holder = new ViewHolder();
            holder.coderef = (TextView) convertView.findViewById(R.id.coderef);
            holder.price = (TextView) convertView.findViewById(R.id.price);
            holder.SourceImageMain = (ImageView) convertView.findViewById(R.id.imageView2);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        //get the inflater and inflate the XML layout for each item





        holder.coderef.setText(property.getCoderef());
        holder.price.setText(property.getPrice());


        Picasso.get().load(property.getImage()).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into( holder.SourceImageMain);





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
        ImageView SourceImageMain;
    }
}
