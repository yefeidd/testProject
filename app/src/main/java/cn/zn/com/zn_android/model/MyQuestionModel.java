package cn.zn.com.zn_android.model;

import android.content.Context;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.MyQuestionHolder;
import cn.zn.com.zn_android.model.bean.MyQuestionBean;

/**
 * Created by Jolly on 2016/12/7.
 */

public class MyQuestionModel extends ListviewItemModel {
    private MyQuestionBean bean;

    public MyQuestionModel(MyQuestionBean bean) {
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        MyQuestionHolder mHolder = (MyQuestionHolder) viewHolder;
//        String title = StringUtil.cutOUtStr(bean.getQuestion(), "...", 30);
        mHolder.mTvTitle.setText(bean.getQuestion());
        mHolder.mTvPrice.setText("￥" + bean.getMoney());
        mHolder.mTvTime.setText("等待时间: " + bean.getTime());
    }
}
