package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/12/7.
 */

public class MyQuestionHolder extends BaseViewHolder {
    @Bind(R.id.tv_wait)
    public TextView mTvWait;
    @Bind(R.id.tv_title)
    public TextView mTvTitle;
    @Bind(R.id.tv_tip)
    public TextView mTvTip;
    @Bind(R.id.ll_time)
    public LinearLayout mLlTime;
    @Bind(R.id.tv_price)
    public TextView mTvPrice;
    @Bind(R.id.tv_time)
    public TextView mTvTime;

    MyQuestionHolder(View view) {
        super(view);
    }
}
