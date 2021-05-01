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
 * Created by murdi on 29-May-17.
 */

public class SearchAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;


    JSONObject property;

    public SearchAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
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
        View view = inflater.inflate(R.layout.simple_list_item_1, null);




        ImageView SourceImageMain = (ImageView) view.findViewById(R.id.imageView2);
        TextView coderef = (TextView) view.findViewById(R.id.coderef);
        TextView price = (TextView) view.findViewById(R.id.price);



        try {
            Picasso.get().load(property.getString("image")).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(SourceImageMain);
            coderef.setText(property.getString("model"));
            price.setText(property.getString("price_formated"));

        } catch (JSONException e) {
            e.printStackTrace();
        }


//


        return view;
    }
}
