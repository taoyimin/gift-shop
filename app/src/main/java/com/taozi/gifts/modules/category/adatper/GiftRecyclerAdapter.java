package com.taozi.gifts.modules.category.adatper;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.taozi.gifts.R;
import com.taozi.gifts.modules.category.bean.Gift;
import com.taozi.gifts.util.ImageLoaderUtil;

import java.util.List;

/**
 * Created by Tao Yimin on 2016/9/30.
 * 测试用的RecyclerView适配器,可以参考
 */
public class GiftRecyclerAdapter extends RecyclerView.Adapter<GiftRecyclerAdapter.MyViewHolder> {
    private Context context;
    private List<Gift> list;
    private OnItemClickListener onItemClickListener;
    private int lastPosition=-1;

    /**
     * 构造方法
     *
     * @param context
     * @param list
     */
    public GiftRecyclerAdapter(Context context, List<Gift> list) {
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
        View itemView = LayoutInflater.from(context).inflate(R.layout.adapter_gift_item, parent, false);
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
        ImageLoader.getInstance().displayImage(list.get(position).getImage1(), holder.mGiftImage, ImageLoaderUtil.getDefaultOption());
        holder.mGiftTitle.setText(list.get(position).getTitle());
        holder.mGiftPraise.setText(list.get(position).getPraiseCount() + "");
        holder.mGiftPrice.setText(list.get(position).getPrice() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(holder, holder.getLayoutPosition());
                }
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemLongClick(holder, holder.getLayoutPosition());
                }
                return true;
            }
        });
        if(position>lastPosition){
            setAnimation(holder.mCardView);
            lastPosition++;
        }else{
            lastPosition--;
        }
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
     * 自定义ViewHolder内部类
     */
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public ImageView mGiftImage;
        public TextView mGiftPrice, mGiftTitle, mGiftPraise;

        public MyViewHolder(View itemView) {
            super(itemView);
            mCardView = (CardView) itemView.findViewById(R.id.gift_card);
            mGiftImage = (ImageView) itemView.findViewById(R.id.gift_image);
            mGiftPrice = (TextView) itemView.findViewById(R.id.gift_price);
            mGiftPraise = (TextView) itemView.findViewById(R.id.gift_praise);
            mGiftTitle = (TextView) itemView.findViewById(R.id.gift_title);
        }
    }

    /**
     * item的点击事件和长按事件回调接口
     */
    public interface OnItemClickListener {
        void onItemClick(MyViewHolder holder, int position);

        void onItemLongClick(MyViewHolder holder, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
