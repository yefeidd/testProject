package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;

import butterknife.Bind;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class HotStockListHolder extends BaseViewHolder {
    @Bind(R.id.ll_item)
    public LinearLayout mLlItem;
    @Bind(R.id.tv_rank)
    public TextView mTvRank;
    @Bind(R.id.tv_name)
    public TextView mTvName;
    @Bind(R.id.tv_profit)
    public TextView mTvProfit;
    @Bind(R.id.tv_total)
    public TextView mTvTotal;
    @Bind(R.id.tv_action)
    public TextView mTvAction;

    HotStockListHolder(View view) {
        super(view);
    }

}
