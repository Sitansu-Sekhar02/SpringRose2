package com.afkharpro.springrose.Base;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by murdi on 23-May-17.
 */

public class FlatMedium extends android.support.v7.widget.AppCompatTextView {

    public FlatMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setType(context);
    }

    public FlatMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        setType(context);
    }

    public FlatMedium(Context context) {
        super(context);
        setType(context);
    }

    private void setType(Context context){
        this.setTypeface(Typeface.createFromAsset(context.getAssets(),
                "tajawalmedium.ttf"));

        // this.setShadowLayer(1.5f, 5, 5, getContext().getResources().getColor(R.color.black_shadow));
    }
}
