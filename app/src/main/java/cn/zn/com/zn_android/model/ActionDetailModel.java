package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.zn.com.zn_android.adapter.viewHolder.ActionDetailViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OperateDetailBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class ActionDetailModel extends ListviewItemModel {
    private OperateDetailBean bean;
    private Activity mActivity;

    public ActionDetailModel(Activity activity, OperateDetailBean bean) {
        this.bean = bean;
        this.mActivity = activity;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        ActionDetailViewHolder actionDetailHolder = (ActionDetailViewHolder) viewHolder;
        actionDetailHolder.mTvCode.setText(bean.getCode_id());
        actionDetailHolder.mTvName.setText(bean.getCode_name());
        actionDetailHolder.mTvNumber.setText(bean.getPosi_num());
        actionDetailHolder.mTvBasePrice.setText(bean.getCost_price());
        actionDetailHolder.mTvPositionCost.setText(bean.getPosi_cost());
        actionDetailHolder.mLlItem.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType().setObject(new OptionalStockBean().setName(bean.getCode_name()).setCode(bean.getCode_id())));
            mActivity.startActivity(new Intent(mActivity, MarketDetailActivity.class));
        });
    }
}
