package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
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

public class WishlistAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;


    JSONObject property;

    public WishlistAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
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
        View view = inflater.inflate(R.layout.simple_list_item_3, null);




        ImageView SourceImageMain = (ImageView) view.findViewById(R.id.imageView2);
        TextView coderef = (TextView) view.findViewById(R.id.coderef);
        TextView price = (TextView) view.findViewById(R.id.price);
        TextView stock = (TextView) view.findViewById(R.id.stock);
        Button delete = (Button) view.findViewById(R.id.remove);



        try {
            Picasso.get().load(property.getString("thumb")).placeholder(R.drawable.loading_icon).error(R.drawable.loading_icon).into(SourceImageMain);
            coderef.setText(property.getString("name"));
            price.setText(property.getString("price"));
            stock.setText(property.getString("stock"));

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag("remove");
                    ((ListView) parent).performItemClick(view, position, 0);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


//

view.setTag("view");
        return view;
    }
}
