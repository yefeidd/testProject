package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;

import cn.zn.com.zn_android.adapter.viewHolder.ActionDetailViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.model.bean.OperateDetailBean;

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
    public void showItem(BaseViewHolder viewHolder, Context context) {
        ActionDetailViewHolder actionDetailHolder = (ActionDetailViewHolder) viewHolder;
    }
}
