package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 09-May-17.
 */

public class AddonHozitemJSONAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;


    JSONObject property;

    public AddonHozitemJSONAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
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
        View view = inflater.inflate(R.layout.simple_list_item_addon, null);




        ImageView SourceImageMain = (ImageView) view.findViewById(R.id.imageView2);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView name = (TextView) view.findViewById(R.id.name);


        try {
            Picasso.get().load(property.getString("image")).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(SourceImageMain);
            price.setText(property.getString("price_formated"));
            name.setText(property.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }


//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((ListView) parent).performItemClick(view, position, 0);
//            }
//        });


        //set address and description


        return view;
    }
}
