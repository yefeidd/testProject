package cn.zn.com.zn_android.model;

import android.content.Context;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.TaOperateDetailViewHolder;
import cn.zn.com.zn_android.model.bean.OperateDetailBean;

/**
 * 操作明细
 * Created by Jolly on 2016/9/14 0014.
 */
public class OperateDetailModel extends ListviewItemModel {
    private OperateDetailBean bean;

    public OperateDetailModel(OperateDetailBean bean) {
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        TaOperateDetailViewHolder holder = (TaOperateDetailViewHolder) viewHolder;
        holder.mTvName.setText(bean.getCode_name());
        holder.mTvCode.setText(bean.getCode_id());
        holder.mTvNum.setText(bean.getPosi_num());
        holder.mTvPrice.setText(bean.getCost_price());
        holder.mTvCost.setText(bean.getPosi_cost());
    }
}
