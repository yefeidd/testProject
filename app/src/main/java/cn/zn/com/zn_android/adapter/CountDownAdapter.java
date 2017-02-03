package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.WaitAnswerBean;
import cn.zn.com.zn_android.uiclass.activity.QuestionDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherAnswerDetailActivity;
import cn.zn.com.zn_android.uiclass.customerview.CountDown.CountDownTask;
import cn.zn.com.zn_android.uiclass.customerview.CountDown.CountDownTimers;
import cn.zn.com.zn_android.utils.DateUtils;

/**
 * @author WhatsAndroid, zjs
 */
public class CountDownAdapter extends ArrayAdapter<WaitAnswerBean> {
    private static final String TAG = "CountDownAdapter";
    private int rsID;
    private CountDownTask mCountDownTask;
    private ViewHolder viewHolder;

    public CountDownAdapter(Context context, List<WaitAnswerBean> objects, int rsID) {
        super(context, 0, objects);
        this.rsID = rsID;
    }

    public void setCountDownTask(CountDownTask countDownTask) {
        if (!Objects.equals(mCountDownTask, countDownTask)) {
            mCountDownTask = countDownTask;
            notifyDataSetChanged();
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(rsID, parent, false);
//        }
        final WaitAnswerBean countDownInfo = getItem(position);
        if (countDownInfo.millis > 0) {
            startCountDown(position, countDownInfo, convertView);
        } else {
            cancelCountDown(position, countDownInfo, convertView);
        }
        viewHolder = new ViewHolder(convertView);
//
        setStarLevel(countDownInfo.getQuestionBean().getStart(), viewHolder.mIvStar);
//        preAnswerHolder.mIvStar.setImageResource(R.drawable.pre_star4);
        String str = countDownInfo.getQuestionBean().getAsk();
        if (str.length() >= 30) {
            str = str.substring(0, 30);
            str = str + "...";
        }
        viewHolder.mTvContent.setText(str);
        viewHolder.mLlContent.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), QuestionDetailActivity.class);
            intent.putExtra("id", countDownInfo.getQuestionBean().getId());
            intent.putExtra("type","WaitAnswer");
            getContext().startActivity(intent);
        });
        viewHolder.mTvAnswer.setText(getContext().getResources().getString(R.string.wait_answer));
        viewHolder.mTvTime.setVisibility(View.VISIBLE);
        if (position == 0) {
            viewHolder.mTvTime.setTextColor(getContext().getResources().getColor(R.color.text_red));
        } else {
            viewHolder.mTvTime.setTextColor(getContext().getResources().getColor(R.color.text_gray));
        }
        viewHolder.mTvAnswer.setOnClickListener(v -> {
            //跳转到回答页面
            skipAnswerDetailActivity(countDownInfo.getQuestionBean().getId());
        });


        return convertView;
    }

    private void startCountDown(final int position, final WaitAnswerBean countDownInfo, View convertView) {
        if (mCountDownTask != null) {
            mCountDownTask.until(convertView, countDownInfo.millis,
                    countDownInfo.countDownInterval, new CountDownTimers.OnCountDownListener() {
                        @Override
                        public void onTick(View view, long millisUntilFinished) {
                            doOnTick(position, view, millisUntilFinished, countDownInfo.countDownInterval);
                        }

                        @Override
                        public void onFinish(View view) {
                            doOnFinish(position, view);
                        }
                    });
        }
    }

    private void doOnTick(int position, View view, long millisUntilFinished, long countDownInterval) {
        TextView textView = (TextView) view.findViewById(R.id.tv_time);
        Long[] times = DateUtils.second2HourStr1(millisUntilFinished/countDownInterval);
        String time = String.format(getContext().getResources().getString(R.string.leave_times), times[0], times[1], times[2]);
        textView.setText(time);
    }

    private void doOnFinish(int position, View view) {
        TextView textView = (TextView) view.findViewById(R.id.tv_time);
        Long[] times = DateUtils.second2HourStr1(0L);
        String time = String.format(getContext().getResources().getString(R.string.leave_times), times[0], times[1], times[2]);
        textView.setText(time);
    }

    private void cancelCountDown(int position, WaitAnswerBean countDownInfo, View view) {
        if (mCountDownTask != null) {
            mCountDownTask.cancel(view);
        }

        TextView textView = (TextView) view.findViewById(R.id.tv_time);
        Long[] times = DateUtils.second2HourStr1(0L);
        String time = String.format(getContext().getResources().getString(R.string.leave_times), times[0], times[1], times[2]);
        textView.setText(time);
    }


    static class ViewHolder {
        @Bind(R.id.iv_star)
        ImageView mIvStar;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_answer)
        TextView mTvAnswer;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.ll_content)
        LinearLayout mLlContent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 设置星星
     *
     * @param star
     * @param view
     */
    private void setStarLevel(String star, ImageView view) {
        switch (star) {
            case "1":
                view.setImageResource(R.drawable.pre_star1);
                break;
            case "2":
                view.setImageResource(R.drawable.pre_star2);

                break;
            case "3":
                view.setImageResource(R.drawable.pre_star3);

                break;
            case "4":
                view.setImageResource(R.drawable.pre_star4);

                break;
            case "5":
                view.setImageResource(R.drawable.pre_star5);

                break;
        }
    }


    public void skipAnswerDetailActivity(String id) {
        Intent intent = new Intent(getContext(), TeacherAnswerDetailActivity.class);
        intent.putExtra("id", id);
        getContext().startActivity(intent);
    }
}
