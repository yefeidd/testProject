package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class HotStockGodViewHolder extends BaseViewHolder {

    @Bind(R.id.iv_head_avatars)
    public SimpleDraweeView mIvHeadAvatars;
    @Bind(R.id.rl_door)
    public RelativeLayout mRlDoor;
    @Bind(R.id.tv_name)
    public TextView mTvName;
    @Bind(R.id.tv_but_time)
    public TextView mTvButTime;
    @Bind(R.id.tv_month_yield)
    public TextView mTvMonthYield;
    @Bind(R.id.tv_week_yield)
    public TextView mTvWeekYield;
    @Bind(R.id.tv_yield)
    public TextView mTvYield;
    @Bind(R.id.tv_win_rate)
    public TextView mTvWinRate;
    @Bind(R.id.btn_focus)
    public Button mBtnFocus;

    HotStockGodViewHolder(View view) {
        super(view);
    }

}
