package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;

/**
 * Created by zjs on 2016/9/21 0021.
 * email: m15267280642@163.com
 * explain:
 */
public class GeniusRankingViewHolder extends BaseViewHolder {
    @Bind(R.id.iv_head_avatars)
    SimpleDraweeView mIvHeadAvatars;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_but_time)
    TextView mTvButTime;
    @Bind(R.id.tv_month_yield)
    TextView mTvMonthYield;
    @Bind(R.id.tv_week_yield)
    TextView mTvWeekYield;
    @Bind(R.id.tv_yield)
    TextView mTvYield;
    @Bind(R.id.tv_win_rate)
    TextView mTvWinRate;
    @Bind(R.id.btn_focus)
    Button mBtnFocus;

    GeniusRankingViewHolder(View view) {
        super(view);
    }
}
