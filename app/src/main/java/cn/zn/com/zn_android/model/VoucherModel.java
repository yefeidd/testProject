package cn.zn.com.zn_android.model;

import android.content.Context;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.VoucherViewHolder;
import cn.zn.com.zn_android.model.bean.ShareTicBean;

/**
 * Created by Jolly on 2016/9/28 0028.
 */

public class VoucherModel extends ListviewItemModel {
    private ShareTicBean bean;

    public VoucherModel(ShareTicBean bean) {
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        VoucherViewHolder holder = (VoucherViewHolder) viewHolder;
        holder.mCbUseZnb.setVisibility(View.GONE);
        holder.mDivider.setVisibility(View.GONE);
        holder.mTvNum.setVisibility(View.VISIBLE);
        holder.mTvValue.setText(bean.getFace_value());
        holder.mTvNum.setText("x " + bean.getNum());
    }
}
