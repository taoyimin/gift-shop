package com.taozi.gifts.modules.user.activity;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.transition.Transition;
import android.transition.TransitionSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.taozi.gifts.R;
import com.taozi.gifts.activity.BaseActivity;
import com.taozi.gifts.transition.ChangeColor;
import com.taozi.gifts.transition.ChangePosition;
import com.taozi.gifts.transition.ShareElemEnterRevealTransition;
import com.taozi.gifts.transition.ShareElemReturnChangePosition;
import com.taozi.gifts.transition.ShareElemReturnRevealTransition;

/**
 * Created by Tao Yimin on 2016/10/31.
 */
public class UserActivity extends BaseActivity{
    private ViewGroup mUserLayout;
    private ImageView mHeadImage;

    @Override
    protected int setContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_user;
    }

    @Override
    protected void findViews() {
        mUserLayout= (ViewGroup) findViewById(R.id.user_layout);
        mHeadImage= (ImageView) findViewById(R.id.user_head);
    }

    @Override
    protected void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //getWindow().setExitTransition(new Explode());
            //getWindow().setEnterTransition(new Explode());
            getWindow().setSharedElementEnterTransition(buildShareElemEnterSet());
            getWindow().setSharedElementReturnTransition(buildShareElemReturnSet());
        }
    }

    @Override
    protected void initEvent() {
    }

    @Override
    protected void init() {
        //模拟读取用户头像
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
        changePos.addTarget(R.id.user_layout);
        transitionSet.addTransition(changePos);

        Transition revealTransition = new ShareElemEnterRevealTransition(mUserLayout);
        revealTransition.addTarget(R.id.user_layout);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        transitionSet.addTransition(revealTransition);

        ChangeColor changeColor = new ChangeColor(getResources().getColor(R.color.colorAccent), Color.WHITE);
        changeColor.addTarget(R.id.user_layout);
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
        changePos.addTarget(R.id.user_layout);
        transitionSet.addTransition(changePos);

        ChangeColor changeColor = new ChangeColor(Color.WHITE, getResources().getColor(R.color.colorAccent));
        changeColor.addTarget(R.id.user_layout);
        transitionSet.addTransition(changeColor);

        Transition revealTransition = new ShareElemReturnRevealTransition(mUserLayout);
        revealTransition.addTarget(R.id.user_layout);
        revealTransition.setInterpolator(new FastOutSlowInInterpolator());
        transitionSet.addTransition(revealTransition);

        transitionSet.setDuration(300);

        return transitionSet;
    }
}
