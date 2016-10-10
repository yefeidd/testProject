package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;

import butterknife.Bind;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class ContestNoticeListHolder extends BaseViewHolder {


    @Bind(R.id.tv_title)
    public TextView mTvTitle;
    @Bind(R.id.rl_item)
    public RelativeLayout mRlItem;
    @Bind(R.id.tv_date)
    public TextView mTvDate;

    ContestNoticeListHolder(View view) {
        super(view);
    }

}
