package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.HotStockListHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotTicBean;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import cn.zn.com.zn_android.utils.UnitUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class HotStockListModel extends ListviewItemModel {
    private HotTicBean bean;
    private int index;
    private Activity mActivity;

    public HotStockListModel setIndex(int index) {
        this.index = index;
        return this;
    }

    public HotStockListModel(Activity activity, HotTicBean bean) {
        this.mActivity = activity;
        this.bean = bean;
    }


    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        HotStockListHolder hotStockListHolder = (HotStockListHolder) viewHolder;
        hotStockListHolder.mTvName.setText(bean.getCode_name());
        hotStockListHolder.mTvProfit.setText(bean.getCode_id());
        hotStockListHolder.mTvTotal.setText(clacUnit(bean.getPrice()));
        hotStockListHolder.mTvAction.setText(bean.getChange_rage() + "%");
        if (index > 2) {
            hotStockListHolder.mTvRank.setBackgroundResource(R.color.bar_bg_gray);
            hotStockListHolder.mTvRank.setTextColor(context.getResources().getColor(R.color.bar_text_title_gray));
        } else {
            hotStockListHolder.mTvRank.setBackgroundResource(R.color.text_red);
            hotStockListHolder.mTvRank.setTextColor(context.getResources().getColor(R.color.white));
        }
        hotStockListHolder.mTvRank.setText(String.valueOf(index + 1));
        hotStockListHolder.mLlItem.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType(bean));
            mActivity.startActivity(new Intent(mActivity, MarketDetailActivity.class));
        });


    }

    private String clacUnit(String str) {
        try {
            Float num = Float.valueOf(str);
            StringBuilder sb = new StringBuilder();
            String unit = UnitUtils.getVolUnit1(num);
            float vol = UnitUtils.getVol(num);
            sb.append(vol).append(unit);
            return sb.toString();
        } catch (Exception e) {
            return str;
        }
    }
}
