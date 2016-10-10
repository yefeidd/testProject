package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.ContestRankingListHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ContestDynamicBean;
import cn.zn.com.zn_android.uiclass.activity.TaActivity;
import cn.zn.com.zn_android.utils.UnitUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/9/18 0018.
 * email: m15267280642@163.com
 * explain:
 */
public class ContestRankingListModel extends ListviewItemModel {
    private Activity mActivity;
    private ContestDynamicBean bean;

    public ContestRankingListModel(Activity activity, ContestDynamicBean bean) {
        this.mActivity = activity;
        this.bean = bean;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        ContestRankingListHolder holder = (ContestRankingListHolder) viewHolder;
        int index = 1;
        try {
            index = Integer.valueOf(bean.getRanking());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (index <= 3) {
            holder.mTvRank.setBackgroundResource(R.color.text_red);
            holder.mTvRank.setTextColor(mActivity.getResources().getColor(R.color.white));
        } else {
            holder.mTvRank.setBackgroundResource(R.color.bar_bg_gray);
            holder.mTvRank.setTextColor(mActivity.getResources().getColor(R.color.bar_text_title_gray));
        }
        holder.mTvRank.setText(bean.getRanking());
        holder.mTvName.setText(bean.getNickname());
        holder.mTvProfit.setText(bean.getProfit() + "%");
        if (bean.getProfit().contains("-")) {
            holder.mTvProfit.setTextColor(mActivity.getResources().getColor(R.color.market_green));
        } else {
            holder.mTvProfit.setTextColor(mActivity.getResources().getColor(R.color.market_red));
        }
        holder.mTvTotal.setText(UnitUtils.clacUnit(bean.getTotalmoney()));
        holder.mTvAction.setText(bean.getNow_position() + "%");
        holder.mLlItem.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType(bean.getUser_id()));
            mActivity.startActivity(new Intent(mActivity, TaActivity.class));
        });
    }
}
