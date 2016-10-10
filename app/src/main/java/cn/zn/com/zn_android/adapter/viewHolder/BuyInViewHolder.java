package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/9/12 0012.
 */
public class BuyInViewHolder extends BaseViewHolder {
    @Bind(R.id.tv_mark)
    public TextView mTvMark;
    @Bind(R.id.tv_price)
    public TextView mTvPrice;
    @Bind(R.id.tv_num)
    public TextView mTvNum;
    @Bind(R.id.rl_buy_sell)
    public LinearLayout mRlBuySell;

    BuyInViewHolder(View view) {
        super(view);
    }
}
