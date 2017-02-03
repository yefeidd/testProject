package cn.zn.com.zn_android.adapter.viewHolder;

import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/9/28 0028.
 */

public class VoucherViewHolder extends BaseViewHolder {

    @Bind(R.id.tv_value)
    public TextView mTvValue;
    @Bind(R.id.cb_use_znb)
    public AppCompatCheckBox mCbUseZnb;
    @Bind(R.id.tv_num)
    public TextView mTvNum;
    @Bind(R.id.v_divider)
    public View mDivider;

    public VoucherViewHolder(View view) {
        super(view);
    }
}
