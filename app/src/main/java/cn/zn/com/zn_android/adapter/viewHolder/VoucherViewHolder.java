package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;

import cn.zn.com.zn_android.R;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/9/28 0028.
 */

public class VoucherViewHolder extends BaseViewHolder {

    @Bind(R.id.iv_voucher)
    public ImageView mIvVoucher;

    public VoucherViewHolder(View view) {
        super(view);
    }
}
