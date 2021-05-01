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
import com.afkharpro.springrose.Models.Users;
import com.afkharpro.springrose.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by murdi on 16-May-17.
 */

public class PaymentAddressAdapter extends ArrayAdapter<String> {

    private Context context;
    private List<String> rentalProperties;
    App app = App.getInstance();
    TextView quan;
    String property;
    ImageView SourceImageMain;
    Users users;

    public PaymentAddressAdapter(Context context, int resource, ArrayList<String> objects) {
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
        View view = inflater.inflate(R.layout.payment_address_list_item, null);




        TextView add1 = (TextView) view.findViewById(R.id.text1);



        try {
            add1.setText(property);


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





