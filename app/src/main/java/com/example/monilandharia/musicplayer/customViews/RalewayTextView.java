package com.example.monilandharia.musicplayer.customViews;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.util.Log;

import com.example.monilandharia.musicplayer.R;

public class RalewayTextView extends AppCompatTextView{
    private static final String TAG = "TextView";
    public RalewayTextView(Context context){
        super(context);
    }
    public RalewayTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }
    public RalewayTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }
    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.RalewayTextView);
        String customFont = a.getString(R.styleable.RalewayTextView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }
    public boolean setCustomFont(Context ctx, String asset) {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: "+e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}
