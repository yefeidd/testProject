package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by zjs on 2016/11/28 0028.
 * email: m15267280642@163.com
 * explain:
 */

public class PreAnswerHolder extends BaseViewHolder {

    @Bind(R.id.iv_star)
    public ImageView mIvStar;
    @Bind(R.id.tv_content)
    public TextView mTvContent;
    @Bind(R.id.tv_answer)
    public TextView mTvAnswer;
    @Bind(R.id.tv_time)
    public TextView mTVTime;
    @Bind(R.id.ll_content)
    public LinearLayout mLlContent;

    PreAnswerHolder(View view) {
        super(view);
    }

}
