package cn.zn.com.zn_android.model;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.OnlineTeacherRankingHolder;
import cn.zn.com.zn_android.model.bean.OnlineTeacherBean;

/**
 *
 * Created by Jolly on 2016/11/29.
 */

public class OnlineTeacherModel extends ListviewItemModel {
    private OnlineTeacherBean bean;
    private int ranking;
    private int type; //1:综合排序2:数量排序3:好评排序

    public OnlineTeacherBean getBean() {
        return bean;
    }

    public OnlineTeacherModel(OnlineTeacherBean bean, int ranking, int type) {
        this.bean = bean;
        this.ranking = ranking;
        this.type = type;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        OnlineTeacherRankingHolder holder = (OnlineTeacherRankingHolder) viewHolder;
        if (ranking < 4) {
            holder.mTvRanking.setTextColor(context.getResources().getColor(R.color.yellow_efb11d));
        } else {
            holder.mTvRanking.setTextColor(context.getResources().getColor(R.color.font_value));
        }
        holder.mTvRanking.setText(ranking + "");
        holder.mTvName.setText(bean.getNickname());
        holder.mTvDetail.setText(bean.getRemark());
        holder.mSdvAvatar.setImageURI(Uri.parse(bean.getHeadimg()));

        String resName = "ic_teacher_star" + bean.getStar_num();
        holder.mIvStar.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                context.getResources().getIdentifier(resName, "drawable", context.getPackageName())));

        String typeStr = "";
        switch (type) {
            case 1:
                typeStr = "综合评分";
                break;
            case 2:
                typeStr = "数量";
                break;
            case 3:
                typeStr = "好评";
                break;
        }
        holder.mTvType.setText(typeStr);
        holder.mTvScore.setText(bean.getNum());
    }

}
