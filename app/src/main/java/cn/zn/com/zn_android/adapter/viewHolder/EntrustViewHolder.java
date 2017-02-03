package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/10/20 0020.
 */

public class EntrustViewHolder extends BaseViewHolder {

    @Bind(R.id.tv_time)
    public TextView mTvTime;  // 名称代码
    @Bind(R.id.tv_trade_type)
    public TextView mTvTradeType; // 委托状态
    @Bind(R.id.tv_deal_price)
    public TextView mTvDealPrice; // 委托价格
    @Bind(R.id.tv_deal_num)
    public TextView mTvDealNum; // 委托数量
    @Bind(R.id.tv_deal_amount)
    public TextView mTvDealAmount; // 委托时间
    @Bind(R.id.tv_exchange_fee)
    public TextView mTvExchangeFee; // 冻结资金
    @Bind(R.id.tv_cancel_trade)
    public TextView mTvCancelTrade;

    EntrustViewHolder(View view) {
        super(view);
//        R.layout.item_entrust_trade
    }
}
