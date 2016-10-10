package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.TextView;

import cn.zn.com.zn_android.R;

import butterknife.Bind;

/**
 * ta  操作明细
 * Created by Jolly on 2016/9/14 0014.
 */
public class TaOperateDetailViewHolder extends BaseViewHolder {
    @Bind(R.id.tv_code)
    public TextView mTvCode;
    @Bind(R.id.tv_name)
    public TextView mTvName;
    @Bind(R.id.tv_num)
    public TextView mTvNum;
    @Bind(R.id.tv_price)
    public TextView mTvPrice;
    @Bind(R.id.tv_cost)
    public TextView mTvCost;

    TaOperateDetailViewHolder(View view) {
        super(view);
    }
}
