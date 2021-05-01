package com.afkharpro.springrose.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afkharpro.springrose.Base.App;
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.R;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 16-May-17.
 */

public class ShippingAddressAdapter extends ArrayAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> rentalProperties;
    App app = App.getInstance();
    TextView quan;
    JSONObject property;
    ImageView SourceImageMain;
    Users users;

    public ShippingAddressAdapter(Context context, int resource, ArrayList<JSONObject> objects) {
        super(context, resource, objects);

        this.context = context;
        this.rentalProperties = objects;
        users = Users.findById(Users.class, (long) 1);
    }

    //called when rendering the list
    public View getView(final int position, View convertView, final ViewGroup parent) {

        //get the property we are displaying
        property = rentalProperties.get(position);

        //get the inflater and inflate the XML layout for each item
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.shipping_address_list_item, null);

        SourceImageMain = (ImageView) view.findViewById(R.id.approved);


        TextView add1 = (TextView) view.findViewById(R.id.text1);
        TextView add2 = (TextView) view.findViewById(R.id.text2);
        TextView city1 = (TextView) view.findViewById(R.id.text3);
        TextView city2 = (TextView) view.findViewById(R.id.text4);


        try {
            add1.setText(property.getString("firstname"));
            add2.setText(property.getString("address_1"));
            city1.setText(property.getString("zone"));
            city2.setText(property.getString("lastname"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (shippingclicked){
//                    SourceImageMain.setVisibility(View.INVISIBLE);
//                    shippingclicked=false;
//                }else{
//                    SourceImageMain.setVisibility(View.VISIBLE);
//                    shippingclicked=true;
//                }
                view.setTag("approved");
                ((ListView) parent).performItemClick(view, position, 0);
            }
        });


        return view;
    }
}





