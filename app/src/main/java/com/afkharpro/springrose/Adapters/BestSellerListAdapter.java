package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class BestSellerListAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;
    App app = App.getInstance();
    TextView desc;
    JSONObject property;

    public BestSellerListAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
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
        View view = inflater.inflate(R.layout.simple_list_item_2, null);

        ImageView SourceImageMain = (ImageView) view.findViewById(R.id.imageView2);

        TextView code = (TextView) view.findViewById(R.id.coderef);
        TextView price = (TextView) view.findViewById(R.id.price);
        desc = (TextView) view.findViewById(R.id.description);




        try {
            Picasso.get().load(property.getString("thumb")).placeholder(R.drawable.loading_icon).into(SourceImageMain);
            code.setText(property.getString("name"));
            price.setText(property.getString("price_formated"));
            desc.setText(Html.fromHtml(property.getString("description")));





        } catch (JSONException e) {
            e.printStackTrace();
        }


        return convertView;
    }

    static class ViewHolder {
        TextView coderef;
        TextView price ;
        ImageView SourceImageMain;
    }
}
