package com.smalltown.rainsimpleexample.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;

/**
 * Created by zh on 2015/10/14.
 */
public class DrawableUtil {
    /**
     * 生成自定义Shape
     * @param radius
     * @param argb
     * @return
     */
    public static GradientDrawable generateDrawable(float radius, int argb){
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius(radius);
        drawable.setColor(argb);
        return drawable;
    }

    public static StateListDrawable generateSeleter(Drawable press,Drawable normal){
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed},press);
        drawable.addState(new int[]{},normal);
        return drawable;
    }
}
