package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.ExcellentAnswerHolder;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.uiclass.activity.QuestionAnswerDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherLiveActivity;
import cn.zn.com.zn_android.utils.DensityUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/11/24.
 */

public class ExcellentAnswerModel extends ListviewItemModel {
    private String title;
    private String detail;
    private String url;
    private String name;
    private String time;
    private boolean isComment = false;
    private String id;
    private String tid;
    private int type = 0; // // 0:不显示评价, 1:未评价，2：已评价，3：已退还

    public ExcellentAnswerModel(String title, String detail, String url, String name, String time, String id, String tid) {
        this.title = title;
        this.detail = detail;
        this.url = url;
        this.name = name;
        this.time = time;
        this.id = id;
        this.tid = tid;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        ExcellentAnswerHolder holder = (ExcellentAnswerHolder) viewHolder;
        holder.tvQuestion.setText(title);

        holder.tvDetail.setText(detail);
        holder.tvDetail.setLetterSpacing(DensityUtil.dip2px(context, 0.8f));
        if (null == detail || TextUtils.isEmpty(detail)) {
            holder.tvDetail.setVisibility(View.GONE);
        } else {
            holder.tvDetail.setVisibility(View.VISIBLE);
        }

        holder.tvName.setText(name);
        holder.tvTime.setText(time);
        holder.sdvAvatar.setImageURI(Uri.parse(url));

        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType().setState(true).setTid(id).setType(Constants.COMMENT_UN));
                context.startActivity(new Intent(context, QuestionAnswerDetailActivity.class));
            }
        });

        holder.sdvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotLiveBean hotLiveBean = new HotLiveBean();
                hotLiveBean.setTid(tid);
//                hotLiveBean.setOrdurl(roomBeanList.get(position - 1).getOrdurl());
                hotLiveBean.setAvatars(url);
                hotLiveBean.setTitle(name);
                hotLiveBean.setRoom_number("");
                hotLiveBean.setCollect("");
                hotLiveBean.setClick("");
                hotLiveBean.setPlacard("");
                hotLiveBean.setCurrentItem(2);
                EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
                context.startActivity(new Intent(context, TeacherLiveActivity.class));
            }
        });
        holder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotLiveBean hotLiveBean = new HotLiveBean();
                hotLiveBean.setTid(tid);
//                hotLiveBean.setOrdurl(roomBeanList.get(position - 1).getOrdurl());
                hotLiveBean.setAvatars(url);
                hotLiveBean.setTitle(name);
                hotLiveBean.setRoom_number("");
                hotLiveBean.setCollect("");
                hotLiveBean.setClick("");
                hotLiveBean.setPlacard("");
                hotLiveBean.setCurrentItem(2);
                EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
                context.startActivity(new Intent(context, TeacherLiveActivity.class));
            }
        });

        if (isComment) {
            holder.mRlComment.setVisibility(View.VISIBLE);
            switch (type) {
                case 1:
                    holder.tvComment.setText(context.getString(R.string.comment));
                    holder.tvComment.setTextColor(context.getResources().getColor(R.color.market_code_color));
                    holder.tvComment.setCompoundDrawablesWithIntrinsicBounds(context.getResources().getDrawable(R.mipmap.ic_comment), null, null, null);
                    holder.mLlItem.setClickable(false);
                    holder.tvComment.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().postSticky(new AnyEventType().setState(false).setTid(id).setType(Constants.COMMENT_UN));
                            context.startActivity(new Intent(context, QuestionAnswerDetailActivity.class));
                        }
                    });

                    holder.mLlItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().postSticky(new AnyEventType().setState(false).setTid(id).setType(Constants.COMMENT_UN));
                            context.startActivity(new Intent(context, QuestionAnswerDetailActivity.class));
                        }
                    });

                    holder.tvName.setVisibility(View.VISIBLE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.sdvAvatar.setVisibility(View.VISIBLE);

                    break;
                case 2:
                    holder.tvComment.setText(context.getString(R.string.had_comment));
                    holder.tvComment.setTextColor(context.getResources().getColor(R.color.market_code_color));
                    holder.tvComment.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    holder.tvComment.setClickable(false);

                    holder.mLlItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().postSticky(new AnyEventType().setState(true).setTid(id).setType(Constants.COMMENT_UN));
                            context.startActivity(new Intent(context, QuestionAnswerDetailActivity.class));
                        }
                    });

                    holder.tvName.setVisibility(View.VISIBLE);
                    holder.tvTime.setVisibility(View.VISIBLE);
                    holder.sdvAvatar.setVisibility(View.VISIBLE);

                    break;
                case 3:
                    holder.tvComment.setText(context.getString(R.string.had_back));
                    holder.tvComment.setTextColor(context.getResources().getColor(R.color.app_bar_color));
                    holder.tvComment.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                    holder.tvComment.setClickable(false);

                    holder.mLlItem.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            EventBus.getDefault().postSticky(new AnyEventType().setState(true).setTid(id).setType(Constants.IS_BACK));
                            context.startActivity(new Intent(context, QuestionAnswerDetailActivity.class));
                        }
                    });

                    holder.tvName.setVisibility(View.GONE);
                    holder.tvTime.setVisibility(View.GONE);
                    holder.sdvAvatar.setVisibility(View.GONE);

                    break;
            }

            holder.tvTime.setGravity(Gravity.LEFT);
        } else {
            holder.mRlComment.setVisibility(View.GONE);
            holder.tvTime.setGravity(Gravity.RIGHT);
        }

    }
}
