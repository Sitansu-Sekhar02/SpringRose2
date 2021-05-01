package com.afkharpro.springrose.Fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by murdi on 03-May-17.
 */

public class PaintView extends View {
    private  String Text = "";
    private Path myArc;
    private Paint mPaintText;

    public PaintView(Context context,String flower) {
        super(context);
        //create Path object
        myArc = new Path();
        RectF oval = new RectF();


        //create paint object
        mPaintText = new Paint(Paint.ANTI_ALIAS_FLAG);
        //set style
        mPaintText.setStyle(Paint.Style.FILL_AND_STROKE);
        //set color
        mPaintText.setColor(Color.WHITE);
        //kids
     //   RectF oval = new RectF(50,75,680,250);
        if (flower.equals("Kids Parties & Balloons")){
            oval = new RectF(50,80,680,250);
            mPaintText.setTextSize(50f);
        }
      //  mPaintText.setTextSize(50f);


//natural
        if (flower.equals("Natural Flower")){
            oval = new RectF(160,80,590,150);
            mPaintText.setTextSize(50f);
        }
        //  RectF oval = new RectF(160,75,590,150);

        //artificial
        if (flower.equals("Artificial Flower")){
            oval = new RectF(160,80,590,150);
            mPaintText.setTextSize(50f);
        }
        //  RectF oval = new RectF(160,75,590,150);

        //wedding
        if (flower.equals("Wedding & Events")){
            oval = new RectF(120,80,620,250);
            mPaintText.setTextSize(50f);
        }
       //   RectF oval = new RectF(120,75,650,250);

        //fiber

        if (flower.equals("Fiberglass & Garden Products")){
            oval = new RectF(40,80,700,250);
            mPaintText.setTextSize(40f);
        }




        //set text Size
      //  mPaintText.setTextSize(40f);

        myArc.addArc(oval, -500, 300);

        Text = flower+"";

    }

    @Override
    protected void onDraw(Canvas canvas) {
        //Draw Text on Canvas
        canvas.drawTextOnPath(Text, myArc, 0, 0, mPaintText);
        invalidate();
    }
}
