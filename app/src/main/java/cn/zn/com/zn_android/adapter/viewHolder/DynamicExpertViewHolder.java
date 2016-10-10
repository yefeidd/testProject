package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/9/18 0018.
 */
public class DynamicExpertViewHolder extends BaseViewHolder {
    @Bind(R.id.sdv_avatar)
    public SimpleDraweeView mSdvAvatar;
    @Bind(R.id.tv_name)
    public TextView mTvName;
    @Bind(R.id.tv_time)
    public TextView mTvTime;
    @Bind(R.id.tv_win_rate)
    public TextView mTvWinRate;
    @Bind(R.id.tv_sum_profit)
    public TextView mTvSumProfit;
    @Bind(R.id.tv_month_profit)
    public TextView mTvMonthProfit;
    @Bind(R.id.tv_week_profit)
    public TextView mTvWeekProfit;
    @Bind(R.id.tv_add_focus)
    public TextView mTvAddFocus;
    @Bind(R.id.rl_dynamic)
    public RelativeLayout mRlDynamic;

    DynamicExpertViewHolder(View view) {
        super(view);
    }
}
