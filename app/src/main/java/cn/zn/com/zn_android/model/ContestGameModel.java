package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.ContestGameHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ContestDynamicBean;
import cn.zn.com.zn_android.uiclass.activity.SignUpActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/9/18 0018.
 * email: m15267280642@163.com
 * explain:
 */
public class ContestGameModel extends ListviewItemModel {
    private Activity mActivity;
    private ContestDynamicBean bean;

    public ContestGameModel(Activity activity, ContestDynamicBean bean) {
        this.mActivity = activity;
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        ContestGameHolder holder = (ContestGameHolder) viewHolder;
        holder.mSlvIcon.setImageURI(Uri.parse(bean.getImg()));
        holder.mTvTitle.setText(bean.getName());
        holder.mTvDate.setText(bean.getStart_time() + "-" + bean.getEnd_time());
        holder.mTvAward.setText(bean.getReward());
        holder.mRlItem.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType(bean.getUrl()));
            mActivity.startActivity(new Intent(mActivity, SignUpActivity.class));
        });


    }
}
