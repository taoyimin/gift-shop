package com.taozi.gifts.modules.category.activity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.se7en.utils.DeviceUtils;
import com.taozi.gifts.R;
import com.taozi.gifts.activity.BaseActivity;
import com.taozi.gifts.i.BaseCallBack;
import com.taozi.gifts.modules.category.adatper.GiftRecyclerAdapter;
import com.taozi.gifts.modules.category.bean.Gift;
import com.taozi.gifts.modules.category.dao.GiftDao;
import com.taozi.gifts.modules.giftdetail.activity.GiftDetailActivity;
import com.taozi.gifts.modules.shopcar.activity.ShopcarActivity;
import com.taozi.gifts.util.ImageLoaderUtil;
import com.taozi.gifts.util.MyAnimationUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/10/28.
 */
public class CategoryActivity extends BaseActivity {
    private AppBarLayout mAppBarLayout;
    private CollapsingToolbarLayout mCollapsingToolbarLayout;
    private Toolbar mToolbar;
    private RecyclerView mRecyclerView;
    private GiftRecyclerAdapter mAdapter;
    private ImageView mCategoryImage, mBackImage;
    private List<Gift> mList;
    private boolean isHide;
    private int totalDistance;
    private boolean isMoveTop;
    private FloatingActionButton mFab;
    private SwipeRefreshLayout mCategoryRefresh;
    private boolean isRefresh = false;

    @Override
    protected int setContentViewId() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        return R.layout.activity_categoy;
    }

    @Override
    protected void findViews() {
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        mCollapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        mToolbar = (Toolbar) findViewById(R.id.tool_bar);
        mCategoryImage = (ImageView) findViewById(R.id.category_image);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mBackImage = (ImageView) findViewById(R.id.back);
        mFab = (FloatingActionButton) findViewById(R.id.shop_car_fab);
        mCategoryRefresh = (SwipeRefreshLayout) findViewById(R.id.category_refresh);
        ViewGroup.LayoutParams params = mCategoryImage.getLayoutParams();
        params.width = DeviceUtils.getScreenWdith();
        mCategoryImage.setLayoutParams(params);
    }

    @Override
    protected void initTransition() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setExitTransition(new Explode());
            getWindow().setEnterTransition(new Explode());
        }
    }

    @Override
    protected void initEvent() {
        mList = new ArrayList<>();
        mAdapter = new GiftRecyclerAdapter(this, mList);
        mAdapter.setOnItemClickListener(new GiftRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(GiftRecyclerAdapter.MyViewHolder holder, int position) {
                Intent intent = new Intent(CategoryActivity.this, GiftDetailActivity.class);
                intent.putExtra("image", mList.get(position).getImage1());
                intent.putExtra("title", mList.get(position).getTitle());
                intent.putExtra("id", mList.get(position).getId());
                intent.putExtra("praise", mList.get(position).getPraiseCount());
                intent.putExtra("price", mList.get(position).getPrice());
                intent.putExtra("marketPrice", mList.get(position).getMarketPrice());
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(CategoryActivity.this, Pair.create((View) holder.mGiftImage, "share_gift_image"), Pair.create((View) holder.mCardView, "share_detail_card"), Pair.create(findViewById(R.id.shop_car_fab), "share_shop_car"));
                    startActivity(intent, options.toBundle());
                }
            }

            @Override
            public void onItemLongClick(GiftRecyclerAdapter.MyViewHolder holder, int position) {

            }
        });
        mBackImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isMoveTop) {
                    mRecyclerView.smoothScrollToPosition(0);
                } else {
                    onBackPressed();
                }
            }
        });
        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                float progress = (float) -verticalOffset / appBarLayout.getTotalScrollRange();
                //隐藏和显示mBackImage
                if (progress == 1 && isHide) {
                    MyAnimationUtil.startCenterCircularReveal(mBackImage, isHide);
                    isHide = false;
                }
                if (progress != 1 && !isHide) {
                    MyAnimationUtil.startCenterCircularReveal(mBackImage, isHide);
                    isHide = true;
                }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        mFab.setClickable(true);
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        mFab.setClickable(false);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        mFab.setClickable(false);
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                totalDistance += dy;
                if (totalDistance >= 1000 && !isMoveTop) {
                    MyAnimationUtil.rotation90(mBackImage, true);
                    isMoveTop = true;
                } else if (totalDistance < 1000 && isMoveTop) {
                    MyAnimationUtil.rotation90(mBackImage, false);
                    isMoveTop = false;
                }
            }
        });
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoryActivity.this, ShopcarActivity.class);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(CategoryActivity.this, Pair.create(findViewById(R.id.shop_car_fab), "share_shop_car"));
                    startActivity(intent, transitionActivityOptions.toBundle());
                }
            }
        });
        mCategoryRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                isRefresh = true;
                loadData();
            }
        });
    }

    @Override
    protected void init() {
        String imageUrl = getIntent().getStringExtra("image");
        String title = getIntent().getStringExtra("title");
        ImageLoader.getInstance().displayImage(imageUrl, mCategoryImage, ImageLoaderUtil.getDefaultOption());
        mCollapsingToolbarLayout.setTitle(title);
        mCollapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00FD0478"));
        mCollapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mCategoryRefresh.setColorSchemeResources(R.color.colorAccent);
    }

    @Override
    protected void loadData() {
        mCategoryRefresh.setRefreshing(true);
        GiftDao.getGiftList(getIntent().getStringExtra("cid"), new BaseCallBack() {
            @Override
            public void success(Object data) {
                mCategoryRefresh.setRefreshing(false);
                if (data != null) {
                    if (isRefresh) {
                        mList.clear();
                        mList.addAll((List<Gift>) data);
                        mAdapter.notifyItemRangeChanged(0, mList.size());
                        return;
                    }
                    mList.addAll((List<Gift>) data);
                    mAdapter.notifyItemRangeInserted(0, mList.size());
                }
            }

            @Override
            public void failed(int errorCode, Object data) {
                isRefresh = false;
                mCategoryRefresh.setRefreshing(false);
                Snackbar.make(mCollapsingToolbarLayout, "请检查网络连接", Snackbar.LENGTH_SHORT).show();
            }
        });
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
}
