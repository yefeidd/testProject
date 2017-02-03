package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ImitateFryAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ImitateFryItemBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.uiclass.activity.BuyInActivity;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.TransactionDetailActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/9/8 0008.
 */
public class ImitateFryModel {
    private Context mContext;
    private ImitateFryItemBean bean;

    public ImitateFryModel(Context context, ImitateFryItemBean bean) {
        this.mContext = context;
        this.bean = bean;
    }

    public void updateUi(ImitateFryAdapter.ViewHolder viewHolder) {
        viewHolder.mTvHead.setText(bean.getCode_name() + "  （" + bean.getCode_id() + "）");
        viewHolder.mTvHead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setCode(bean.getCode_id());
                stockBean.setName(bean.getCode_name());
                EventBus.getDefault().postSticky(new AnyEventType(stockBean));
                mContext.startActivity(new Intent(mContext, MarketDetailActivity.class));
            }
        });

        viewHolder.mTvCurrentPrice.setText(bean.getNow_price());
        if (bean.getProfit().startsWith("-")) {
            viewHolder.mTvGains.setText(bean.getProfit() + "%");
            viewHolder.mTvGains.setTextColor(mContext.getResources().getColor(R.color.green_down));
            viewHolder.mTvCurrentPrice.setTextColor(mContext.getResources().getColor(R.color.green_down));
            viewHolder.mTvAvarageCost.setTextColor(mContext.getResources().getColor(R.color.green_down));
        } else {
            viewHolder.mTvGains.setText("+" + bean.getProfit() + "%");
            viewHolder.mTvGains.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
            viewHolder.mTvCurrentPrice.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
            viewHolder.mTvAvarageCost.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
        }
        viewHolder.mTvLastMarketValue.setText(bean.getNew_worth());
        viewHolder.mTvAvarageCost.setText(bean.getAvage_price());
        viewHolder.mTvPositionNum.setText(bean.getPosition_num());
        viewHolder.mTvSellNum.setText(bean.getTo_sell_num() + "");

        viewHolder.mTvTransactionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(
                        bean.getCode_id()).setTid(mContext.getString(R.string.mnpdqcc)).setStockCode(bean.getCode_name()));
                mContext.startActivity(new Intent(mContext, TransactionDetailActivity.class));
            }
        });

        viewHolder.mTvBuyIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(bean.getCode_id())
                        .setTid(mContext.getString(R.string.buy_in)));
                mContext.startActivity(new Intent(mContext, BuyInActivity.class));
            }
        });

        viewHolder.mTvSellOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(bean.getCode_id())
                        .setTid(mContext.getString(R.string.sell_out)));
                mContext.startActivity(new Intent(mContext, BuyInActivity.class));
            }
        });
    }
}
