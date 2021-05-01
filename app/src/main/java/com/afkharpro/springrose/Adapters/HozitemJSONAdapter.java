package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.afkharpro.springrose.Models.Hozitem;
import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 09-May-17.
 */

public class HozitemJSONAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;


    JSONObject property;

    public HozitemJSONAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
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
        View view = inflater.inflate(R.layout.simple_list_item_card, null);




        ImageView SourceImageMain = (ImageView) view.findViewById(R.id.imageViewcard);


        try {
            Picasso.get().load(property.getString("image")).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(SourceImageMain);
        } catch (JSONException e) {
            e.printStackTrace();
        }





        //set address and description


        return view;
    }
}
