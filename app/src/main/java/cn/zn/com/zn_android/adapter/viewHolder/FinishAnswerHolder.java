package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by zjs on 2016/11/30 0030.
 * email: m15267280642@163.com
 * explain:
 */

public class FinishAnswerHolder extends BaseViewHolder {


    @Bind(R.id.iv_star)
    public ImageView mIvStar;
    @Bind(R.id.tv_content)
    public TextView mTvContent;
    @Bind(R.id.tv_time)
    public TextView mTvTime;
    @Bind(R.id.tv_set_cancel)
    public TextView mTvSetCancel;

    FinishAnswerHolder(View view) {
        super(view);
    }
}
