package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
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
public class ContestGameHolder extends BaseViewHolder {

    @Bind(R.id.rl_item)
    public RelativeLayout mRlItem;
    @Bind(R.id.slv_icon)
    public SimpleDraweeView mSlvIcon;
    @Bind(R.id.tv_title)
    public TextView mTvTitle;
    @Bind(R.id.tv_date)
    public TextView mTvDate;
    @Bind(R.id.tv_ranking)
    public TextView mTvRanking;
    @Bind(R.id.tv_award)
    public TextView mTvAward;
    @Bind(R.id.iv_detail)
    public ImageView mIvDetail;

    ContestGameHolder(View view) {
        super(view);
    }

}
