package com.taozi.gifts.transition;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.os.Build;
import android.transition.Transition;
import android.transition.TransitionValues;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Tao Yimin on 2016/10/28.
 */
@TargetApi(Build.VERSION_CODES.KITKAT)
public class ChangeColor extends Transition {
    private static final String TAG = "ChangeColor";

    private static final String PROPNAME_BACKGROUND = "customtransition:change_color:backgroundcolor";


    int mStartColor;
    int mEndColor;

    public ChangeColor(int startColor, int endColor) {
        this.mStartColor = startColor;
        this.mEndColor = endColor;
    }

    private void captureValues(TransitionValues values) {
        values.values.put(PROPNAME_BACKGROUND, values.view.getBackground());
    }

    @Override
    public void captureStartValues(TransitionValues transitionValues) {
        captureValues(transitionValues);
        transitionValues.values.put(PROPNAME_BACKGROUND, mStartColor);
    }

    @Override
    public void captureEndValues(TransitionValues transitionValues) {
        transitionValues.values.put(PROPNAME_BACKGROUND, mEndColor);
    }

    @Override
    public Animator createAnimator(ViewGroup sceneRoot,
                                   TransitionValues startValues, TransitionValues endValues) {
        if (null == startValues || null == endValues) {
            return null;
        }
        final View view = endValues.view;

        int startColor = (int) startValues.values.get(PROPNAME_BACKGROUND);
        int endColor = (int) endValues.values.get(PROPNAME_BACKGROUND);

        if (startColor != endColor) {
            ValueAnimator animator = ValueAnimator.ofObject(new ArgbEvaluator(), startColor, endColor);
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    Object value = animation.getAnimatedValue();
                    if (null != value) {
                        view.setBackgroundColor((Integer) value);
                    }
                }
            });
            return animator;
        }
        return null;
    }
}
