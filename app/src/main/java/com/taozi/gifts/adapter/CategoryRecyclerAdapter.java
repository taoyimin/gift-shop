package com.taozi.gifts.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taozi.gifts.R;
import com.taozi.gifts.bean.GiftCategory;
import com.taozi.gifts.i.MyItemTouchCallback;
import com.taozi.gifts.util.ImageLoaderUtil;

import java.util.Collections;
import java.util.List;

/**
 * Created by Tao Yimin on 2016/9/30.
 * 测试用的RecyclerView适配器,可以参考
 */
public class CategoryRecyclerAdapter extends RecyclerView.Adapter<CategoryRecyclerAdapter.MyViewHolder> implements MyItemTouchCallback.ItemTouchAdapter{
    private Context context;
    private List<GiftCategory> list;

    /**
     * 构造方法
     *
     * @param context
     * @param list
     */
    public CategoryRecyclerAdapter(Context context, List<GiftCategory> list) {
        this.context = context;
        this.list = list;
    }

    /**
     * 创建itemView和ViewHolder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_categroy_item, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * 填充界面
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ImageLoader.getInstance().displayImage(list.get(position).getImage(), holder.mImageView, ImageLoaderUtil.getDefaultOption());
        setAnimation(holder.mCardView);
    }

    /**
     * 返回需要显示的行数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * item滑出屏幕时清空动画
     *
     * @param holder
     */
    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.mCardView.clearAnimation();
    }

    /**
     * item入场动画
     *
     * @param viewToAnimate
     */
    private void setAnimation(View viewToAnimate) {
        Animation animation = AnimationUtils.loadAnimation(viewToAnimate.getContext(), R.anim.item_bottom_in);
        viewToAnimate.startAnimation(animation);
    }

    /**
     * 在拖拽过程中会不停回掉该方法
     * @param fromPosition
     * @param toPosition
     */
    @Override
    public void onMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(list, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(list, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onSwiped(int position) {
        list.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 自定义ViewHolder内部类
     */
    class MyViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private ImageView mImageView;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.category_card);
            mImageView = (ImageView) itemView.findViewById(R.id.category_image);
        }
    }
}
