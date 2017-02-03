package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.ContestNoticeListHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ContestDynamicBean;
import cn.zn.com.zn_android.uiclass.activity.ContestNoticeActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/9/18 0018.
 * email: m15267280642@163.com
 * explain:
 */
public class ContestNoticeModel extends ListviewItemModel {

    private Activity mActivity;
    private ContestDynamicBean bean;

    public ContestNoticeModel(Activity activity, ContestDynamicBean bean) {
        this.mActivity = activity;
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        ContestNoticeListHolder holder = (ContestNoticeListHolder) viewHolder;
        holder.mTvTitle.setText(bean.getTitle());
        holder.mTvDate.setText(bean.getTime());
        holder.mRlItem.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType(bean.getUrl()));
            mActivity.startActivity(new Intent(mActivity, ContestNoticeActivity.class));
        });

    }
}
