package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/11/24.
 */

public class OnlineTeacherGVHolder extends BaseViewHolder {
    @Bind(R.id.sdv_avatar)
    public SimpleDraweeView sdvAvatar;
    @Bind(R.id.tv_name)
    public TextView tvName;
    @Bind(R.id.tv_answer_num)
    public TextView tvAnswerNum;
    @Bind(R.id.ib_ask_him)
    public ImageButton ibAskHim;
    @Bind(R.id.iv_star)
    public ImageView mIvStar;

    OnlineTeacherGVHolder(View view) {
//        R.layout.item_online_teacher_gv
        super(view);
    }

}
