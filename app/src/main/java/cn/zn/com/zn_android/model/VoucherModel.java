package cn.zn.com.zn_android.model;

import android.content.Context;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.VoucherViewHolder;

/**
 * Created by Jolly on 2016/9/28 0028.
 */

public class VoucherModel extends ListviewItemModel {

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        VoucherViewHolder holder = (VoucherViewHolder) viewHolder;
        holder.mIvVoucher.setImageResource(R.drawable.quan);
    }
}
