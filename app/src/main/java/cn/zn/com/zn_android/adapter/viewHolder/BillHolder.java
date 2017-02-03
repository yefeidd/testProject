package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/12/7.
 */

public class BillHolder extends BaseViewHolder {
    @Bind(R.id.tv_time)
    public TextView mTvTime;
    @Bind(R.id.iv_znb)
    public ImageView mIvZnb;
    @Bind(R.id.tv_price)
    public TextView mTvPrice;
    @Bind(R.id.tv_type)
    public TextView mTvType;

    BillHolder(View view) {
        super(view);
    }
}
