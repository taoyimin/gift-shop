package com.taozi.gifts.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.View;
import android.view.ViewAnimationUtils;

import com.se7en.utils.DeviceUtils;

/**
 * Created by Tao Yimin on 2016/10/28.
 */
public class MyAnimationUtil {

    /**
     * 以控件中心为原点开启圆形揭露动画
     *
     * @param view   需要做动画的View
     * @param isShow 是显示还是隐藏
     */
    public static void startCenterCircularReveal(final View view, final boolean isShow) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;
        int finalRadius = (int)Math.sqrt(Math.pow(view.getWidth()/2,2)+Math.pow(view.getHeight()/2,2));
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (isShow) {
                view.setVisibility(View.VISIBLE);
                anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);
            } else {
                anim = ViewAnimationUtils.createCircularReveal(view, cx, cy, finalRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        view.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
        anim.start();
    }

    /**
     * 旋转90度动画
     * @param view 需要做动画的控件
     * @param isLeft 是向左旋转还是向右旋转
     */
    public static void rotation90(final View view, final boolean isLeft) {
        ValueAnimator animator = ValueAnimator.ofInt(0, 90).setDuration(300);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int progress = (int) animation.getAnimatedValue();
                if(isLeft){
                    view.setRotation(progress);
                }else{
                    view.setRotation(90-progress);
                }
            }
        });
        animator.start();
    }

    /**
     * item从底部滑入
     * @param distance 滑动距离
     */
    public static void itemBottomIn(final View view, final int distance){
        ValueAnimator animator=ValueAnimator.ofFloat(0,1).setDuration(500);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress=animation.getAnimatedFraction();
                view.setTranslationY(distance-distance*progress);
                view.setAlpha(progress);
            }
        });
        animator.start();
    }

    public static void startFragmentCircularReveal(final View view, final View fragmentLayout, final boolean isShow) {
        int cx = (int) (view.getX()+view.getWidth() / 2);
        int cy = (int) (view.getY()+view.getHeight() / 2);
        int finalRadius = (int) Math.sqrt(Math.pow(DeviceUtils.getScreenHeight(),2)+Math.pow(DeviceUtils.getScreenWdith(),2));
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            if (isShow) {
                fragmentLayout.setVisibility(View.VISIBLE);
                anim = ViewAnimationUtils.createCircularReveal(fragmentLayout, cx, cy, 0, finalRadius);
            } else {
                anim = ViewAnimationUtils.createCircularReveal(fragmentLayout, cx, cy, finalRadius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        fragmentLayout.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }
        anim.start();
    }
}
