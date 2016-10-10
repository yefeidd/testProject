package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.GiftBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/3/29 0029.
 */
public class GiftRVAdapter extends RecyclerView.Adapter {
    private Context mContext;
    private List<GiftBean> giftBeans = new ArrayList<>();
    private int beforeSelect = -1;

    public GiftRVAdapter setItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        return this;
    }

    private OnItemClickListener itemClickListener;
    public interface OnItemClickListener {
        void onClick(View itemView, int position);
    }

    public GiftRVAdapter(Context context, List<GiftBean> giftBeans) {
//        super();
        this.mContext = context;
        this.giftBeans = giftBeans;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        GiftViewHolder holder = new GiftViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_gift, parent, false));
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        GiftViewHolder giftViewHolder = (GiftViewHolder) holder;

        giftViewHolder.mIvGiftImg.setImageResource(giftBeans.get(position).getImgRes());
        giftViewHolder.mIvGiftName.setText(giftBeans.get(position).getGiftName());
        giftViewHolder.mIvGiftWealth.setText(giftBeans.get(position).getGiftWealth() + "个财富币");
        if (giftBeans.get(position).isSelected()) {
            giftViewHolder.mLlGift.setBackgroundColor(mContext.getResources().getColor(R.color.register_content_color));
        } else {
            giftViewHolder.mLlGift.setBackgroundColor(mContext.getResources().getColor(R.color.white));

        }

        if (itemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (beforeSelect >= 0) {
                        giftBeans.get(beforeSelect).setIsSelected(!giftBeans.get(beforeSelect).isSelected());
                    }
                    beforeSelect = position;
                    giftBeans.get(position).setIsSelected(!giftBeans.get(position).isSelected());
                    itemClickListener.onClick(v, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return giftBeans.size();
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'item_gift.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Avast Developers (http://github.com/avast)
     */
    class GiftViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.iv_gift_img)
        ImageView mIvGiftImg;
        @Bind(R.id.iv_gift_name)
        TextView mIvGiftName;
        @Bind(R.id.iv_gift_wealth)
        TextView mIvGiftWealth;
        @Bind(R.id.ll_gift)
        LinearLayout mLlGift;

        public GiftViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
