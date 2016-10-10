package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/9/14 0014.
 */
public class TaRecordViewHolder extends BaseViewHolder {
    @Bind(R.id.sdv_avatar)
    public SimpleDraweeView mSdvAvatar;
    @Bind(R.id.tv_name)
    public TextView mTvName;
    @Bind(R.id.tv_day)
    public TextView mTvDay;
    @Bind(R.id.tv_time)
    public TextView mTvTime;
    @Bind(R.id.tv_sell_buy)
    public TextView mTvSellBuy;
    @Bind(R.id.tv_stock_name)
    public TextView mTvStockName;
    @Bind(R.id.tv_operate)
    public TextView mTvOperate;
    @Bind(R.id.tv_buy_in)
    public TextView mTvBuyIn;

    TaRecordViewHolder(View view) {
        super(view);
    }
}
