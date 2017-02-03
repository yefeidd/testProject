package cn.zn.com.zn_android.model;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.AbsListView;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.BuyInViewHolder;
import cn.zn.com.zn_android.model.bean.BuyInBean;

/**
 * Created by Jolly on 2016/9/13 0013.
 */
public class BuyInModel extends ListviewItemModel {
    private BuyInBean bean;
    private int itemHeight;

    public BuyInModel(BuyInBean bean, int itemHeight) {
        this.bean = bean;
        this.itemHeight = itemHeight;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        BuyInViewHolder holder = (BuyInViewHolder) viewHolder;
        holder.mTvMark.setText(bean.getMark());
        holder.mTvPrice.setText(bean.getPrice());
        holder.mTvNum.setText(bean.getNum());
        AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        holder.mRlBuySell.setLayoutParams(layoutParams);

    }
}
