package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.customerview.LetterSpacingTextView;

/**
 * Created by Jolly on 2016/11/24.
 */

public class ExcellentAnswerHolder extends BaseViewHolder {
    @Bind(R.id.ll_item)
    public LinearLayout mLlItem;
    @Bind(R.id.tv_question)
    public TextView tvQuestion;
    @Bind(R.id.tv_detail)
    public LetterSpacingTextView tvDetail;
    @Bind(R.id.sdv_avatar)
    public SimpleDraweeView sdvAvatar;
    @Bind(R.id.tv_name)
    public TextView tvName;
    @Bind(R.id.tv_time)
    public TextView tvTime;
    @Bind(R.id.tv_comment)
    public TextView tvComment;
    @Bind(R.id.rl_comment)
    public RelativeLayout mRlComment;

    ExcellentAnswerHolder(View view) {
//        R.layout.item_excellent_answer
        super(view);
    }
}
