package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 16-May-17.
 */

public class MyOrdersAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;
    App app = App.getInstance();
    TextView quan;
    JSONObject property;


    public MyOrdersAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
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
        View view = inflater.inflate(R.layout.my_order_list_item, null);




        TextView add1 = (TextView) view.findViewById(R.id.text1);
        TextView add2 = (TextView) view.findViewById(R.id.text2);
        TextView add3 = (TextView) view.findViewById(R.id.text3);
        TextView add4 = (TextView) view.findViewById(R.id.text4);
        TextView id = (TextView) view.findViewById(R.id.txtid);


        try {
            id.setText(property.getString("order_id"));
            add1.setText(property.getString("status"));
            add2.setText(property.getString("name"));
            add3.setText(property.getString("date_added"));
            add4.setText(property.getString("total"));

        } catch (Exception e) {
            e.printStackTrace();
        }




        return view;
    }
}





