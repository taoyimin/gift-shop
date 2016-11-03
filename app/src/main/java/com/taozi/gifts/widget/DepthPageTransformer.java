package com.taozi.gifts.widget;

import android.os.Build;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by Tao Yimin on 2016/9/25.
 */
public class DepthPageTransformer implements ViewPager.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float position) {
        int pageWidth = view.getWidth();

        if (position < -1) {
            view.setAlpha(0);

        } else if (position <= 0) { // [-1,0]
            view.setAlpha(1);
            view.setTranslationX(0);
            view.setScaleX(1);
            view.setScaleY(1);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(10);
                view.setTranslationZ(100);
            }

        } else if (position <= 1) {
            view.setAlpha(1 - position);
            view.setTranslationX(pageWidth * -position);
            float scaleFactor = MIN_SCALE
                    + (1 - MIN_SCALE) * (1 - Math.abs(position));
            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.setElevation(10);
                view.setTranslationZ(100 * (1 - position));
            }

        } else {
            view.setAlpha(0);
        }
    }
}

