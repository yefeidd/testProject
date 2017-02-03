package cn.zn.com.zn_android.model;

import android.content.Context;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.BillHolder;
import cn.zn.com.zn_android.model.bean.BillBean;

/**
 * Created by Jolly on 2016/12/7.
 */
public class BillModel extends ListviewItemModel {
    //    private String time;
//    private String price;
//    private String type;
    private BillBean.DataBean bean;

    public BillModel(BillBean.DataBean bean) {
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        BillHolder holder = (BillHolder) viewHolder;
        String times = bean.getWeek() + "\n" + bean.getTimes();
        holder.mTvTime.setText(times);
        holder.mTvPrice.setText(bean.getWealth() + "币");
        holder.mTvType.setText(bean.getWay());
    }



    public BillBean.DataBean getBean() {
        return bean;
    }
}
