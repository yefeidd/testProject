package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/11/29.
 */

public class OnlineTeacherRankingHolder extends BaseViewHolder {
    @Bind(R.id.tv_ranking)
    public TextView mTvRanking;
    @Bind(R.id.sdv_avatar)
    public SimpleDraweeView mSdvAvatar;
    @Bind(R.id.tv_name)
    public TextView mTvName;
    @Bind(R.id.tv_detail)
    public TextView mTvDetail;
    @Bind(R.id.tv_type)
    public TextView mTvType;
    @Bind(R.id.tv_score)
    public TextView mTvScore;
    @Bind(R.id.iv_star)
    public ImageView mIvStar;

    OnlineTeacherRankingHolder(View view) {
//        R.layout.item_online_teacher_ranking
        super(view);
    }
}
