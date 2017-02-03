package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.TaRecordViewHolder;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ExchangeRecordBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.uiclass.activity.BuyInActivity;

import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/9/14 0014.
 */
public class ExchangeRecordModel extends ListviewItemModel {
    private ExchangeRecordBean bean;
    private Context mContext;

    public ExchangeRecordModel(Context context, ExchangeRecordBean bean) {
        this.mContext = context;
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        TaRecordViewHolder holder = (TaRecordViewHolder) viewHolder;
        holder.mTvDay.setText(bean.getJoin_time());
        holder.mTvName.setText(bean.getUser_name());
        holder.mTvStockName.setText(bean.getCode_name() + "(" + bean.getCode_id() + ")");
        holder.mTvStockName.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType().setObject(new OptionalStockBean().setName(bean.getCode_name()).setCode(bean.getCode_id())));
            mContext.startActivity(new Intent(mContext, MarketDetailActivity.class));
        });
        holder.mTvTime.setText(bean.getMonth_ri());
        if (null != bean.getHead_img()) {
            holder.mSdvAvatar.setImageURI(Uri.parse(bean.getHead_img()));
        }
        String operate = "成交价" + bean.getPrice() + "元、股数" + bean.getCode_num();
        holder.mTvOperate.setText(operate);
        if ("1".equals(bean.getTrade_type())) {
            holder.mTvSellBuy.setText("买入");
            holder.mTvSellBuy.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
            holder.mTvBuyIn.setVisibility(View.VISIBLE);
        } else {
            holder.mTvSellBuy.setText("卖出");
            holder.mTvSellBuy.setTextColor(mContext.getResources().getColor(R.color.green_down));
            holder.mTvBuyIn.setVisibility(View.GONE);
        }

        holder.mTvBuyIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RnApplication.getInstance().getUserInfo().getIsLogin() == 1) {
                    EventBus.getDefault().postSticky(new AnyEventType(bean.getCode_id()).setTid(mContext.getString(R.string.buy_in)));
                    mContext.startActivity(new Intent(mContext, BuyInActivity.class));
                } else {
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });
    }
}
