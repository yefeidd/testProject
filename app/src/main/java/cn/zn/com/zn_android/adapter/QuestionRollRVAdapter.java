package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.QustionRollBean;

/**
 * Created by Jolly on 2016/11/25.
 */

public class QuestionRollRVAdapter extends RecyclerView.Adapter<QuestionRollRVAdapter.ViewHolder> {
    private Context mContext;
    private int resId;
    private List<QustionRollBean> data = new ArrayList<>();

    public QuestionRollRVAdapter(int resId, List<QustionRollBean> data) {
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
        holder.mTvQuestion.setText(data.get(position).getSname() + "（" +
                data.get(position).getScode() + "）" + data.get(position).getPdescription());
        holder.mTvName.setText(data.get(position).getNickname());
        holder.mTvTime.setText(data.get(position).getTimes());

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
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_time)
        TextView mTvTime;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}

