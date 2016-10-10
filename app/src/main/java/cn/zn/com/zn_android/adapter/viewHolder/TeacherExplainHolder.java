package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;

/**
 * Created by zjs on 2016/9/14 0014.
 * email: m15267280642@163.com
 * explain:
 */
public class TeacherExplainHolder extends BaseViewHolder {
    @Bind(R.id.sdv_explain)
    public SimpleDraweeView mSdvExplain;
    @Bind(R.id.tv_title)
    public TextView mTvTitle;
    @Bind(R.id.tv_number)
    public TextView mTvNumber;
    @Bind(R.id.rl_item)
    public RelativeLayout mRlItem;

    TeacherExplainHolder(View view) {
        super(view);
    }
}
