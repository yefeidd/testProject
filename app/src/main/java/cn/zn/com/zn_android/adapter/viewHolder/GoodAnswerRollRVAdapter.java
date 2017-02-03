package cn.zn.com.zn_android.adapter.viewHolder;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ExcellentAnswerBean;
import cn.zn.com.zn_android.uiclass.activity.QuestionAnswerDetailActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/12/27 0027.
 * email: m15267280642@163.com
 * explain:老师列表的列表滚动
 */

public class GoodAnswerRollRVAdapter extends RecyclerView.Adapter<GoodAnswerRollRVAdapter.ViewHolder> {
    private Context mContext;
    private int resId;
    private List<ExcellentAnswerBean> data = new ArrayList<>();

    public GoodAnswerRollRVAdapter(int resId, List<ExcellentAnswerBean> data) {
        this.resId = resId;
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(resId, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ExcellentAnswerBean bean = data.get(position);
        String question = bean.getSname() + "（" + bean.getScode() + "）" + bean.getPdescription();
        holder.mTvQuestion.setText(question);
        holder.mTvDetail.setText(bean.getAnswer_info());
        holder.mTvName.setText(bean.getNickname());
        holder.mTvTime.setText(bean.getHtime());
        holder.mSdvAvatar.setImageURI(Uri.parse(bean.getAvatars()));
        holder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType().setState(true).setTid(bean.getId()).setType(Constants.COMMENT_UN));
                mContext.startActivity(new Intent(mContext, QuestionAnswerDetailActivity.class));
            }
        });


//        holder.mTvQuestion.setText(data.get(position).getPdescription());
//        holder.mTvName.setText(data.get(position).getSname());
//        holder.mTvTime.setText(data.get(position).getTimes());

    }

    @Override
    public int getItemCount() {
//        if (data.size() > 5) {
//            return 4;
//        }
        if (data.size() > 0) {
            return data.size() - 1;
        }
        return 0;
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.tv_question)
        TextView mTvQuestion;
        @Bind(R.id.tv_detail)
        TextView mTvDetail;
        @Bind(R.id.sdv_avatar)
        SimpleDraweeView mSdvAvatar;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_comment)
        TextView mTvComment;
        @Bind(R.id.ll_item)
        LinearLayout mLlItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
