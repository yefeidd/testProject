package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;

import butterknife.Bind;

/**
 * Created by zjs on 2016/9/19 0019.
 * email: m15267280642@163.com
 * explain:
 */
public class ActionDetailViewHolder extends BaseViewHolder {

    @Bind(R.id.ll_item)
    public LinearLayout mLlItem;
    @Bind(R.id.tv_code)
    public TextView mTvCode;
    @Bind(R.id.tv_name)
    public TextView mTvName;
    @Bind(R.id.tv_number)
    public TextView mTvNumber;
    @Bind(R.id.tv_base_price)
    public TextView mTvBasePrice;
    @Bind(R.id.tv_position_cost)
    public TextView mTvPositionCost;

    ActionDetailViewHolder(View view) {
        super(view);
    }
}
