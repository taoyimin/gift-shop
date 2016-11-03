package com.taozi.gifts.activity;

import android.Manifest;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.se7en.utils.DeviceUtils;
import com.taozi.gifts.R;
import com.taozi.gifts.adapter.CategoryRecyclerAdapter;
import com.taozi.gifts.bean.GiftCategory;
import com.taozi.gifts.dao.GiftCategoryDao;
import com.taozi.gifts.i.AppBarStateChangeListener;
import com.taozi.gifts.i.BaseCallBack;
import com.taozi.gifts.i.MyItemTouchCallback;
import com.taozi.gifts.i.OnRecyclerItemClickListener;
import com.taozi.gifts.modules.category.activity.CategoryActivity;
import com.taozi.gifts.util.ImageLoaderUtil;
import com.taozi.gifts.util.MyAnimationUtil;
import com.taozi.gifts.widget.BannerLayout;
import com.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements MyItemTouchCallback.OnDragListener {
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private ViewGroup mLocationLayout,mShopCollectLayout;
    private ImageView mHeadImage, mQrCode,mQrCodeHide, mShopCollect,mShopCollcetHide;
    private RecyclerView mRecyclerView;
    private CategoryRecyclerAdapter mAdapter;
    private List<GiftCategory> mList;
    private ItemTouchHelper mItemTouchHelper;
    private View mView;
    private boolean isHide;
    private boolean isExit=true;
    private Handler mHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    mQrCodeHide.setX(mQrCode.getX());
                    mQrCodeHide.setY(mQrCode.getY());
                    break;
                case 1:
                    mShopCollcetHide.setX(mShopCollect.getX());
                    mShopCollcetHide.setY(mShopCollect.getY());
                    break;
            }
        }
    };

    private BannerLayout mBannerLayout;

    private List<BannerLayout.BannerEntity> urls = new ArrayList<>();

    @Override
    protected int setContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_main;
    }

    @Override
    protected void findViews() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mLocationLayout = (ViewGroup) findViewById(R.id.location_layout);
        mShopCollectLayout= (ViewGroup) findViewById(R.id.shop_collect_layout);
        mHeadImage = (ImageView) findViewById(R.id.head_image);
        mQrCode = (ImageView) findViewById(R.id.two_code);
        mQrCodeHide= (ImageView) findViewById(R.id.two_code_hide);
        mShopCollect = (ImageView) findViewById(R.id.shop_collect);
        mShopCollcetHide= (ImageView) findViewById(R.id.shop_collect_hide);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mBannerLayout = (BannerLayout) findViewById(R.id.banner_layout);
        mHeadImage.setPivotX(0);
        mHeadImage.setPivotY(DeviceUtils.dip2px(57));
        mHandler.sendEmptyMessageDelayed(0,500);
        mHandler.sendEmptyMessageDelayed(1,500);
    }

    @Override
    protected void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
        }
    }

    @Override
    protected void initEvent() {
        mList = new ArrayList<>();
        mAdapter = new CategoryRecyclerAdapter(this, mList);
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            // private int flags = 0;
            @Override
            public void onClick(View v) {
/*                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    Path path = new Path();
                    if(flags==0){
                        path.moveTo(v.getX(), v.getY());
                        path.quadTo(v.getX()+500, v.getY(), v.getX()+500, v.getY()-500);
                    }else{
                        path.moveTo(v.getX(), v.getY());
                        path.quadTo(v.getX(), v.getY()+500, v.getX()-500, v.getY()+500);
                    }
                    ObjectAnimator mAnimator = null;
                    mAnimator = ObjectAnimator.ofFloat(v, View.X, View.Y, path);
                    Path p = new Path();
                    if(flags==0){
                        p.lineTo(1f, 1f);
                        flags=1;
                    }else{
                        p.lineTo(1f, 1f);
                        flags=0;
                    }
                    mAnimator.setInterpolator(new PathInterpolator(p));
                    mAnimator.setInterpolator(new OvershootInterpolator());
                    mAnimator.setDuration(300);
                    mAnimator.start();
                }*/
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int i) {

                if (state == State.EXPANDED) {
                    //展开状态
                    mQrCodeHide.setClickable(false);
                    mShopCollcetHide.setClickable(false);
                } else if (state == State.COLLAPSED) {
                    //折叠状态
                    mQrCodeHide.setClickable(true);
                    mShopCollcetHide.setClickable(true);
                } else {
                    //中间状态
                }
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float progress = (float) -verticalOffset / appBarLayout.getTotalScrollRange();
                //头像缩小
                mHeadImage.setScaleX(1 - progress / 3);
                mHeadImage.setScaleY(1 - progress / 3);
                //mShopCar,mShopCollect,mLocationLayout保持原地不动
                mQrCode.setTranslationY(-verticalOffset);
                mShopCollect.setTranslationY(-verticalOffset);
                mLocationLayout.setTranslationY(-verticalOffset);
                //隐藏和显示mShopCar,mShopCollect
                if (progress == 1 && isHide) {
                    MyAnimationUtil.startCenterCircularReveal(mQrCode, isHide);
                    MyAnimationUtil.startCenterCircularReveal(mShopCollect, isHide);
                    MyAnimationUtil.startCenterCircularReveal(mLocationLayout, isHide);
                    isHide = false;
                }
                if (progress != 1 && !isHide) {
                    MyAnimationUtil.startCenterCircularReveal(mQrCode, isHide);
                    MyAnimationUtil.startCenterCircularReveal(mShopCollect, isHide);
                    MyAnimationUtil.startCenterCircularReveal(mLocationLayout, isHide);
                    isHide = true;
                }
            }
        });
        mBannerLayout.setOnPagerClickListener(new BannerLayout.OnPagerClickListener() {
            @Override
            public void onClick(BannerLayout.BannerEntity entity) {
                Snackbar.make(mRecyclerView, entity.getUrl(), Snackbar.LENGTH_SHORT).show();
            }
        });
        mHeadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
      /*          Intent intent = new Intent(MainActivity.this, UserActivity.class);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, mHeadImage, "share_user_layout");
                    startActivity(intent, options.toBundle());
                }*/
            }
        });
        mQrCodeHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    applyPermission();
                } else {
                    startQrcode();
                }
            }
        });
        mShopCollcetHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyAnimationUtil.startFragmentCircularReveal(mShopCollcetHide,mShopCollectLayout,true);
                isExit=false;
            }
        });
    }

    @Override
    protected void init() {
        update(mBannerLayout);
        //模拟读取用户名
        mCollapsingToolbarLayout.setTitle("taoyimin");
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#FD0478"));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        //模拟读取头像
        ImageLoader.getInstance().displayImage("http://d.hiphotos.baidu.com/zhidao/wh%3D450%2C600/sign=2509c457fbfaaf5184b689bbb964b8d8/f7246b600c33874400ea430d570fd9f9d72aa090.jpg", mHeadImage, ImageLoaderUtil.getCircleBitmapOption(Color.WHITE, 8));
        mItemTouchHelper = new ItemTouchHelper(new MyItemTouchCallback(mAdapter).setOnDragListener(this));
        //将mItemTouchHelper和mRecyclerView进行关联
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        //添加监听
        mRecyclerView.addOnItemTouchListener(new OnRecyclerItemClickListener(mRecyclerView) {
            @Override
            public void onLongClick(RecyclerView.ViewHolder vh) {
                mItemTouchHelper.startDrag(vh);
                mView = vh.itemView;
                mView.setSelected(true);
            }

            @Override
            public void onItemClick(RecyclerView.ViewHolder vh) {
                startClickAnimation(vh.itemView);
                int position = vh.getLayoutPosition();
                Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
                intent.putExtra("image", mList.get(position).getImage());
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("cid", mList.get(position).getId() + "");
                ActivityOptions options = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this, Pair.create(vh.itemView.findViewById(R.id.category_card), "share_category_card"), Pair.create(vh.itemView.findViewById(R.id.category_image), "share_category_image"));
                    startActivity(intent, options.toBundle());
                }
            }
        });
    }

    /**
     * 拖拽完成后的回掉
     */
    @Override
    public void onFinishDrag() {
        mView.setSelected(false);
        mView = null;
    }

    private void startClickAnimation(final View view) {
        ValueAnimator animatorUP = ValueAnimator.ofFloat(0f, 1f).setDuration(150);
        animatorUP.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = animation.getAnimatedFraction();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setTranslationZ(DeviceUtils.dip2px(15) * progress);
                }
            }
        });
        animatorUP.start();
        ValueAnimator animatorDown = ValueAnimator.ofFloat(0f, 1f).setDuration(300);
        animatorDown.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float progress = animation.getAnimatedFraction();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.setTranslationZ(DeviceUtils.dip2px(15) * (1 - progress));
                }
            }
        });
        animatorDown.setStartDelay(150);
        animatorDown.start();
    }

    @Override
    protected void loadData() {
        GiftCategoryDao.getGiftCategoryList(new BaseCallBack() {
            @Override
            public void success(Object data) {
                mList.addAll((List<GiftCategory>) data);
                if (mList != null) {
                    mAdapter.notifyItemRangeInserted(0, mList.size());
                }
            }

            @Override
            public void failed(int errorCode, Object data) {
                Snackbar.make(mRecyclerView, "请检查网络连接", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void update(View v) {
        urls.clear();
        //模拟读取轮播图片
        urls.add(new Entity("http://www.songliapp.com/file/images/110278.jpg"));
        urls.add(new Entity("http://www.songliapp.com/file/images/122111.jpg"));
        urls.add(new Entity("http://www.songliapp.com/file/images/110275.jpg"));
        mBannerLayout.setDatas(urls);
    }

    private class Entity implements BannerLayout.BannerEntity {

        String url;

        public Entity(String url) {
            this.url = url;
        }

        @Override
        public String getUrl() {
            return url;
        }
    }


    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    /**
     * 申请相机权限
     */
    @TargetApi(Build.VERSION_CODES.M)
    public void applyPermission() {
        int hasWriteContactsPermission = checkSelfPermission(Manifest.permission.CAMERA);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        startQrcode();
    }

    /**
     * 申请结果的回掉
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    startQrcode();
                } else {
                    // Permission Denied
                    Snackbar.make(mAppBarLayout, "拒绝授权可能会影响您的体验", Snackbar.LENGTH_SHORT).show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    final private int REQUEST_CODE_START_QRCODE=0;
    /**
     * 跳转到二维码扫描界面
     */
    public void startQrcode() {
        Intent openCameraIntent = new Intent(this, CaptureActivity.class);
        startActivityForResult(openCameraIntent, REQUEST_CODE_START_QRCODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_START_QRCODE) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("result");
            Snackbar.make(mAppBarLayout, scanResult, Snackbar.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        if(!isExit){
            MyAnimationUtil.startFragmentCircularReveal(mShopCollcetHide,mShopCollectLayout,false);
            isExit=true;
            return;
        }
        super.onBackPressed();
    }
}
