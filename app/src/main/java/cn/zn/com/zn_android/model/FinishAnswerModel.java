package cn.zn.com.zn_android.model;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.FinishAnswerHolder;
import cn.zn.com.zn_android.model.bean.QuestionBean;

/**
 * Created by zjs on 2016/11/30 0030.
 * email: m15267280642@163.com
 * explain:
 */

public class FinishAnswerModel extends ListviewItemModel {
    private int type = 0; //0 老师抢答 1 学员主动 2 精彩回答
    private QuestionBean bean;
    private FinishAnswerHolder finishAnswerHolder;
    private OnPerfectListener listener;

    public QuestionBean getBean() {
        return bean;
    }

    public FinishAnswerModel() {

    }



    public FinishAnswerModel setType(int type) {
        this.type = type;
        return this;
    }

    public FinishAnswerModel setBean(QuestionBean bean) {
        this.bean = bean;
        return this;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        finishAnswerHolder = (FinishAnswerHolder) viewHolder;
        setStarLevel(bean.getStart(), finishAnswerHolder.mIvStar);
        String str = bean.getAsk();  //测试使用
        if (str.length() >= 30) str = str.substring(0, 30);
        str = str + "...";
        finishAnswerHolder.mTvContent.setText(str);
        if (type == 2) {
            finishAnswerHolder.mTvSetCancel.setText("取消精彩");
        } else {
            finishAnswerHolder.mTvSetCancel.setText("设为精彩");
        }
//        String ss = "设为精彩";
        finishAnswerHolder.mTvSetCancel.setOnClickListener(v -> {
            if (listener != null) {
                listener.onclick(type, finishAnswerHolder.mTvSetCancel, position, bean.getId());
            }
//            if (ss.equals(finishAnswerHolder.mTvSetCancel.getText().toString())) {
//                ToastUtil.show(context, "设置成功", Toast.LENGTH_SHORT);
//                finishAnswerHolder.mTvSetCancel.setText(context.getString(R.string.cancel_perfect));
//            } else {
//                ToastUtil.show(context, "取消成功", Toast.LENGTH_SHORT);
//                finishAnswerHolder.mTvSetCancel.setText(context.getString(R.string.set_perfect));
//            }
        });
        finishAnswerHolder.mTvTime.setText("回答时间: " + bean.getHtime());
    }


    /**
     * 更新条目状态(设为精彩以及取消精彩)
     */

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

    public FinishAnswerModel setOnPerfectListener(OnPerfectListener listener) {
        this.listener = listener;
        return this;
    }

    public interface OnPerfectListener {
        /**
         * @param type     //0 老师抢答 1 学员主动 2 精彩回答
         * @param view     //设置精彩的按钮
         * @param position //具体哪一个条目点击了设置精彩
         */
        void onclick(int type, TextView view, int position, String id);
    }




}
