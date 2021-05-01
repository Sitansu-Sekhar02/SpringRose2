package com.afkharpro.springrose.Fragments;

/**
 * Created by murdi on 03-May-17.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.afkharpro.springrose.MainActivity;
import com.afkharpro.springrose.R;
import com.afkharpro.springrose.ShopActivity;
import com.afkharpro.springrose.WebActivity;
import com.squareup.picasso.Picasso;

import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Example Fragment class that shows an identifier inside a TextView.
 */
public class ColourFragment extends Fragment {

    private int identifier;
    Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        context = getContext();
        identifier = args.getInt("identifier");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View rowView = inflater.inflate(R.layout.frament_1, container, false);
       /* TextView v = (TextView) rowView.findViewById(R.id.flower);
        ImageView im = (ImageView) rowView.findViewById(R.id.logoimage);

        RelativeLayout rlMain=(RelativeLayout)rowView.findViewById(R.id.rlMain);

      //  rlMain.addView(pV);

        v.setTextSize(20);
        v.setTextColor(Color.WHITE);

        if (identifier==0){
           // v.setText("Natural Flower");
           // PaintView pV=new PaintView(context,"Natural Flower");
           // rlMain.addView(pV);
            rlMain.setBackgroundResource(R.drawable.naturalflower);
            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,ShopActivity.class);
                    startActivity(intent);
                }
            });
            Picasso.with(context).load(R.drawable.naturalflowerlogo).into(im);
        }else if (identifier==1){
            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(context.getString(R.string.Comingsoon))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                }
                            })
                            .show();
                }
            });
            rlMain.setBackgroundResource(R.drawable.artificialflower);
            Picasso.with(context).load(R.drawable.springrose).into(im);
        }else if (identifier==2){
            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,WebActivity.class);
                    intent.putExtra("title",context.getString(R.string.WeddingEvents));
                    intent.putExtra("link","https://www.instagram.com/springrose_wedding");
                    startActivity(intent);
                }
            });
            rlMain.setBackgroundResource(R.drawable.weddingevents);
            Picasso.with(context).load(R.drawable.wedding).into(im);
        }else if (identifier==3){
            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context,WebActivity.class);
                    intent.putExtra("title",context.getString(R.string.KidsPartiesBalloons));
                    intent.putExtra("link","https://www.instagram.com/springrose_balloons");
                    startActivity(intent);
                }
            });
            rlMain.setBackgroundResource(R.drawable.kids);
            Picasso.with(context).load(R.drawable.kidslogo).into(im);
         //   v.setTextSize(16);
        }else if (identifier==4){
            rlMain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new SweetAlertDialog(context, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText(context.getString(R.string.Comingsoon))
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();

                                }
                            })
                            .show();
                }
            });
            rlMain.setBackgroundResource(R.drawable.fiberglass);
            Picasso.with(context).load(R.drawable.garden).into(im);
          //  v.setTextSize(13);
        }

*/
        return rowView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("dummy", true);
    }
}