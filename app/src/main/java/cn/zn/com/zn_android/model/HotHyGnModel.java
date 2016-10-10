package cn.zn.com.zn_android.model;

import android.content.Context;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.IndustryAdapter;

/**
 * Created by Jolly on 2016/8/15 0015.
 */
public class HotHyGnModel {

    private String name;
    private String average;
    private String ticName;
    private String change;
    private String changeRate;
    private String codeId;

    public HotHyGnModel(String name, String average, String ticName, String change,
                        String changeRate, String codeId) {
        this.name = name;
        this.average = average;
        this.ticName = ticName;
        this.change = change;
        this.changeRate = changeRate;
        this.codeId = codeId;
    }

    public void showHotUi(IndustryAdapter.ViewHolder viewHolder, Context context) {

        if (null == average) {
            average = "--";
        }
        if (null == change) {
            change = "--";
        }
        if (null == changeRate) {
            changeRate = "--";
        }

        viewHolder.mTitle.setText(name);

        if (average.startsWith("-")) {
            viewHolder.mTvUpDown.setTextColor(context.getResources().getColor(R.color.green_down));
            viewHolder.mTvUpDown.setText(average);
        } else {
            viewHolder.mTvUpDown.setTextColor(context.getResources().getColor(R.color.app_bar_color));
            viewHolder.mTvUpDown.setText("+" + average);
        }

        if (change.startsWith("-")) {
            viewHolder.mTvPrice.setText(change);
        } else {
            viewHolder.mTvPrice.setText("+" + change);
        }

        if (changeRate.startsWith("-")) {
            viewHolder.mTvRate.setText(changeRate);
        } else {
            viewHolder.mTvRate.setText("+" + changeRate);
        }

        viewHolder.mTvName.setText(ticName);

    }

    public String getCodeId() {
        return codeId;
    }

    public String getName() {
        return name;
    }

}
