package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.PreAnswerHolder;
import cn.zn.com.zn_android.model.bean.QuestionBean;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.activity.QuestionDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherAnswerDetailActivity;
import cn.zn.com.zn_android.uiclass.listener.CountDownListener;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;

/**
 * Created by zjs on 2016/11/28 0028.
 * email: m15267280642@163.com
 * explain:抢答/待回答诊股列表的model
 */

public class PreAnswerModel extends ListviewItemModel implements CountDownListener,DiagnosedStockView {
    private Activity mContext;
    private QuestionBean bean;
    private String type = "preAnswer";
    private DiagnosesSocketPresenter presenter;
    private PreAnswerHolder preAnswerHolder;

    public PreAnswerModel(Activity context, DiagnosesSocketPresenter presenter) {
        this.presenter = presenter;
        this.mContext = context;
    }

    public PreAnswerModel setBean(QuestionBean bean) {
        this.bean = bean;
        return this;
    }


    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        preAnswerHolder = (PreAnswerHolder) viewHolder;
        setStarLevel(bean.getStart(), preAnswerHolder.mIvStar);
//        preAnswerHolder.mIvStar.setImageResource(R.drawable.pre_star4);
        String str = bean.getAsk();
        if (str.length() >= 30) {
            str = str.substring(0, 30);
            str = str + "...";
        }
        preAnswerHolder.mTvContent.setText(str);
        preAnswerHolder.mLlContent.setOnClickListener(v -> {
            Intent intent = new Intent(context, QuestionDetailActivity.class);
            intent.putExtra("id", bean.getId());
            intent.putExtra("type","PreAnswer");
            context.startActivity(intent);
        });
        //preAnswer 表示抢答  waitAnser 表示待回答
        if (!"preAnswer".equals(type)) {
            preAnswerHolder.mTvAnswer.setText(context.getResources().getString(R.string.wait_answer));
            preAnswerHolder.mTVTime.setVisibility(View.VISIBLE);
            Long times;
            try {
                times = Long.valueOf(bean.getTime());
            } catch (Exception e) {
                times = 0L;
            }
            if (position == 0) {
                preAnswerHolder.mTVTime.setTextColor(mContext.getResources().getColor(R.color.text_red));
            } else {
                preAnswerHolder.mTVTime.setTextColor(mContext.getResources().getColor(R.color.text_gray));
            }
//            presenter.startCountDown(times, this);
            preAnswerHolder.mTvAnswer.setOnClickListener(v -> {
                //跳转到回答页面
                skipAnswerDetailActivity();
            });
        } else {
            preAnswerHolder.mTvAnswer.setText(context.getResources().getString(R.string.pre_answer));
            preAnswerHolder.mTVTime.setVisibility(View.GONE);
            preAnswerHolder.mTVTime.setText("剩余时间: 58分40秒");
            preAnswerHolder.mTvAnswer.setOnClickListener(v -> {
                presenter.rushAnswer(bean.getId(), this);
            });
        }


    }


    public void skipAnswerDetailActivity() {
        Intent intent = new Intent(mContext, TeacherAnswerDetailActivity.class);
        intent.putExtra("id", bean.getId());
        intent.putExtra("type","PreAnswer");
        mContext.startActivity(intent);
    }


    public PreAnswerModel setType(String type) {
        this.type = type;
        return this;
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

    @Override
    public void onStart(Object obj) {
        Long[] times = new Long[3];
        times = (Long[]) obj;
        String time = String.format(mContext.getResources().getString(R.string.leave_times), times[0], times[1], times[2]);
        preAnswerHolder.mTVTime.setText(time);
    }

    @Override
    public void onFail(Throwable t) {
        Log.e("preAnswerModel", "onError: " + "倒计时出错");
        String time = String.format(mContext.getResources().getString(R.string.leave_times), 0, 0, 0);
        preAnswerHolder.mTVTime.setText(time);
    }

    @Override
    public void onSuccess(DiagnosedType requestType, Object object) {
        switch (requestType) {
            case RESPONDER:
                skipAnswerDetailActivity();
                break;
        }
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
