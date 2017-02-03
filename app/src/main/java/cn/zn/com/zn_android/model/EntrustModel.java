package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.EntrustViewHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.EntrustBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/10/20 0020.
 */

public class EntrustModel extends ListviewItemModel {
    private EntrustBean bean;
    public interface CancelTradeListener {
        void cancelTrade(String id);
    }
    private CancelTradeListener mListener;

    public EntrustModel(EntrustBean bean, CancelTradeListener mListener) {
        this.bean = bean;
        this.mListener = mListener;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        EntrustViewHolder holder = (EntrustViewHolder) viewHolder;
        holder.mTvTradeType.setVisibility(View.VISIBLE);

        holder.mTvTime.setText(bean.getCode_name() + " （" + bean.getCode_id() + "）");
        holder.mTvTime.setTextColor(context.getResources().getColor(R.color.sell_out_blue));
        if (null != bean.getTrade_type() && bean.getTrade_type().equals("1")) {
            holder.mTvTime.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_buy_in), null);
        } else if (null != bean.getTrade_type() && bean.getTrade_type().equals("2")){
            holder.mTvTime.setCompoundDrawablesWithIntrinsicBounds(null, null, context.getResources().getDrawable(R.mipmap.icon_sell_out), null);
        }
        holder.mTvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setCode(bean.getCode_id());
                stockBean.setName(bean.getCode_name());
                EventBus.getDefault().postSticky(new AnyEventType(stockBean));
                context.startActivity(new Intent(context, MarketDetailActivity.class));
            }
        });
        holder.mTvDealPrice.setText(bean.getCode_price());
        holder.mTvDealNum.setText(bean.getCode_num());
        holder.mTvDealAmount.setText(bean.getEntrust_time());
        holder.mTvExchangeFee.setText(bean.getFrozen_money());
        holder.mTvCancelTrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.cancelTrade(bean.getId());
            }
        });
    }
}
