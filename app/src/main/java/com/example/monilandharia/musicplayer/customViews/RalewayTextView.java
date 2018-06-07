package com.example.monilandharia.musicplayer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.example.monilandharia.musicplayer.R;

public class RalewayTextView extends AppCompatTextView{
    public RalewayTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public RalewayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RalewayTextView(Context context) {
        super(context);
    }


    public void setTypeface(Typeface tf, int style) {
        if (style == Typeface.BOLD) {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Bold.ttf")/*, -1*/);
        } else {
            super.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/Raleway-Regular.ttf")/*, -1*/);
        }
    }
}
