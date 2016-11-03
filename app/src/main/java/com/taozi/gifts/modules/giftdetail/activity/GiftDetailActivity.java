package com.taozi.gifts.modules.giftdetail.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.transition.Fade;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.se7en.utils.DeviceUtils;
import com.taozi.gifts.R;
import com.taozi.gifts.activity.BaseActivity;
import com.taozi.gifts.i.AppBarStateChangeListener;
import com.taozi.gifts.modules.shopcar.activity.ShopcarActivity;
import com.taozi.gifts.util.ImageLoaderUtil;
import com.taozi.gifts.util.MyAnimationUtil;

/**
 * Created by Tao Yimin on 2016/10/29.
 */
public class GiftDetailActivity extends BaseActivity {
    private DrawerLayout mDrawerLayout;
    private NestedScrollView mDrawer;
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private ImageView mGiftImage;
    private FloatingActionButton mFab;
    private ViewGroup mPriceNotify,mGiftParameter;
    private TextView mTitleText, mPriceText,mMarketPriceText,mGiftNotify;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    MyAnimationUtil.itemBottomIn(mTitleText, 100);
                    break;
                case 1:
                    MyAnimationUtil.itemBottomIn(mPriceNotify, 100);
                    break;
                case 2:
                    MyAnimationUtil.itemBottomIn(mGiftParameter, 100);
                    break;
            }
        }
    };

    @Override
    protected int setContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_gift_detail;
    }

    @Override
    protected void findViews() {
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawer = (NestedScrollView) findViewById(R.id.drawer_view);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mGiftImage = (ImageView) findViewById(R.id.gift_image);
        mFab = (FloatingActionButton) findViewById(R.id.shop_car_fab);
        mPriceNotify = (ViewGroup) findViewById(R.id.layout_price_notify);
        mTitleText = (TextView) findViewById(R.id.content_gift_title);
        mPriceText = (TextView) findViewById(R.id.content_gift_price);
        mMarketPriceText = (TextView) findViewById(R.id.content_market_price);
        mGiftParameter = (ViewGroup) findViewById(R.id.content_gift_parameter);
        mGiftNotify= (TextView) findViewById(R.id.content_gift_notify);
    }

    @Override
    protected void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }
    }

    @Override
    protected void initEvent() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GiftDetailActivity.this, ShopcarActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(GiftDetailActivity.this, Pair.create(findViewById(R.id.shop_car_fab), "share_shop_car"));
                    startActivity(intent, transitionActivityOptions.toBundle());
                }
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int i) {
                ViewGroup.LayoutParams vl = mDrawer.getLayoutParams();
                if (state == State.EXPANDED) {
                    //展开状态
                    vl.height = DeviceUtils.getScreenHeight() - DeviceUtils.dip2px(256);
                    mDrawer.setTranslationY(DeviceUtils.dip2px(256));
                    mDrawer.setLayoutParams(vl);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    vl.height = DeviceUtils.getScreenHeight() - DeviceUtils.dip2px(56) - DeviceUtils.getStatuBarHeight();
                    mDrawer.setTranslationY(DeviceUtils.dip2px(56) + DeviceUtils.getStatuBarHeight());
                    mDrawer.setLayoutParams(vl);
                } else {
                    //中间状态
                }
            }
        });
        mGiftParameter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawer);
            }
        });
        mGiftNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v,"功能待实现",Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void init() {
        //填充一部分数据
        String imageUrl = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        String price = getIntent().getStringExtra("price");
        String marketPrice = getIntent().getStringExtra("marketPrice");
        ImageLoader.getInstance().displayImage(imageUrl, mGiftImage, ImageLoaderUtil.getDefaultOption());
        mCollapsingToolbarLayout.setTitle(title);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00FD0478"));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mMarketPriceText.setText("原价:"+marketPrice);
        //底部滑入动画
        mPriceNotify.setAlpha(0);
        mTitleText.setAlpha(0);
        mGiftParameter.setAlpha(0);
        mHandler.sendEmptyMessageDelayed(0,200);
        mHandler.sendEmptyMessageDelayed(1,500);
        mHandler.sendEmptyMessageDelayed(2,800);
        mTitleText.setText(title);
        mPriceText.setText(price);
    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
