package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/12/7.
 */

public class OrderAllHolder extends BaseViewHolder {
    @Bind(R.id.tv_price)
    public TextView mTvPrice;
    @Bind(R.id.tv_question)
    public TextView mTvQuestion;
    @Bind(R.id.tv_type)
    public TextView mTvType;
    @Bind(R.id.tv_time)
    public TextView mTvTime;
    @Bind(R.id.ib_pay)
    public ImageButton mIbPay;

    OrderAllHolder(View view) {
        super(view);
    }
}
