package com.taozi.gifts.modules.shopcar.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;

import com.taozi.gifts.R;
import com.taozi.gifts.activity.BaseActivity;
import com.taozi.gifts.transition.ChangeColor;
import com.taozi.gifts.transition.ChangePosition;
import com.taozi.gifts.transition.ShareElemEnterRevealTransition;
import com.taozi.gifts.transition.ShareElemReturnChangePosition;
import com.taozi.gifts.transition.ShareElemReturnRevealTransition;

/**
 * Created by Tao Yimin on 2016/10/29.
 */
public class ShopcarActivity extends BaseActivity{
    private View mShopcarLayout;


    @Override
    protected int setContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_shop_car;
    }

    @Override
    protected void findViews() {
        mShopcarLayout=findViewById(R.id.shop_car_layout);
    }

    @Override
    protected void initTransition(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().setExitTransition(new Explode());
            getWindow().setSharedElementEnterTransition(buildShareElemEnterSet());
            getWindow().setSharedElementReturnTransition(buildShareElemReturnSet());
        }
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void init() {
    }

    @Override
    protected void loadData() {

    }

    /**
     * 分享 元素 进入动画
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private TransitionSet buildShareElemEnterSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition changePos = new ChangePosition();
        changePos.addTarget(R.id.shop_car_layout);
        transitionSet.addTransition(changePos);

        Transition revealTransition = new ShareElemEnterRevealTransition(mShopcarLayout);
        revealTransition.addTarget(R.id.shop_car_layout);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        transitionSet.addTransition(revealTransition);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.colorAccent), Color.WHITE);
        changeColor.addTarget(R.id.shop_car_layout);
        transitionSet.addTransition(changeColor);

        transitionSet.setDuration(300);

        return transitionSet;
    }

    /**
     * 分享元素返回动画
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    private TransitionSet buildShareElemReturnSet() {
        TransitionSet transitionSet = new TransitionSet();

        Transition changePos = new ShareElemReturnChangePosition();
        changePos.addTarget(R.id.shop_car_layout);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(Color.WHITE, getResources().getColor(R.color.colorAccent));
        changeColor.addTarget(R.id.shop_car_layout);
        transitionSet.addTransition(changeColor);

        Transition revealTransition = new ShareElemReturnRevealTransition(mShopcarLayout);
        revealTransition.addTarget(R.id.shop_car_layout);
        //revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        transitionSet.addTransition(revealTransition);

        transitionSet.setDuration(300);

        return transitionSet;
    }
}
