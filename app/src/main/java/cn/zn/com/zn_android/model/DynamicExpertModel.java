package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.DynamicExpertViewHolder;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.DynamicExpertBean;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.TaActivity;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/9/18 0018.
 */
public class DynamicExpertModel extends ListviewItemModel {
    private static final String TAG = "DynamicExpertModel";
    private DynamicExpertBean bean;
    private Activity mContext;
    private int position1 = 0;
    private boolean isShortTerm = false; // 是否首页短线牛人

    public void setShortTerm(boolean shortTerm) {
        isShortTerm = shortTerm;
    }

    public DynamicExpertBean getBean() {
        return bean;
    }

    private FocusChangeListner mListner;

    public interface FocusChangeListner {
        void focusChange(boolean focus, String userId, int position1);
    }

    public DynamicExpertModel(Activity context, DynamicExpertBean bean, int position, FocusChangeListner mListner) {
        this.bean = bean;
        this.mContext = context;
        this.position1 = position;
        this.mListner = mListner;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        DynamicExpertViewHolder holder = (DynamicExpertViewHolder) viewHolder;
        holder.mTvName.setText(bean.getNickname());
        holder.mTvTime.setText(String.format(mContext.getString(R.string.buy_in_time),
                bean.getFirst_time()));

        if (!isShortTerm) {
            String profit = String.format(mContext.getString(R.string.sum_profit), bean.getProfit()) + "%";
            SpannableString ssProfit = new SpannableString(profit);
            if (bean.getProfit().startsWith("-")) {
                ssProfit.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.green_down)),
                        3, ssProfit.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                ssProfit.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_bar_color)),
                        3, ssProfit.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.mTvSumProfit.setText(ssProfit);
            holder.mTvWeekProfit.setText(String.format(mContext.getString(R.string.week_profit), bean.getWeek_income()) + "%");
        } else {
            String profit = String.format(mContext.getString(R.string.week_profit1), bean.getWeek_income()) + "%";
            SpannableString ssProfit = new SpannableString(profit);
            if (bean.getWeek_income().startsWith("-")) {
                ssProfit.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.green_down)),
                        3, ssProfit.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                ssProfit.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_bar_color)),
                        3, ssProfit.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.mTvSumProfit.setText(ssProfit);
            holder.mTvWeekProfit.setText(String.format(mContext.getString(R.string.sum_profit1), bean.getProfit()) + "%");
        }

        String winRate = String.format(mContext.getString(R.string.win_rate), bean.getWin_rate()) + "%";
        SpannableString ssWinRate = new SpannableString(winRate);
        if (bean.getWin_rate().startsWith("-")) {
            ssWinRate.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.green_down)),
                    3, ssWinRate.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            ssWinRate.setSpan(new ForegroundColorSpan(mContext.getResources().getColor(R.color.app_bar_color)),
                    3, ssWinRate.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        holder.mTvWinRate.setText(ssWinRate);

        holder.mTvMonthProfit.setText(String.format(mContext.getString(R.string.month_profit), bean.getMon_income()) + "%");
        holder.mSdvAvatar.setImageURI(Uri.parse(bean.getAvatars()));

        holder.mRlDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType().setStockCode(bean.getUser_id()));
                mContext.startActivity(new Intent(mContext, TaActivity.class));
            }
        });
        holder.mTvAddFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RnApplication.getInstance().getUserInfo().getIsLogin() == 1) {
                    if (holder.mTvAddFocus.getText().equals(mContext.getString(R.string.add_focus))) {
//                        attentionOther(bean.getUser_id(), holder);
                        mListner.focusChange(true, bean.getUser_id(), position1);
                    } else {
//                        unsetConcern(bean.getUser_id(), holder);
                        mListner.focusChange(false, bean.getUser_id(), position1);
                    }
                } else {
//                    EventBus.getDefault().postSticky(new AnyEventType(bean));
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });

        if (null != bean.getAttentionType()) {
            if (bean.getAttentionType().equals("1") || bean.getAttentionType().equals("2")) {
                holder.mTvAddFocus.setText(mContext.getString(R.string.finish_focus));
            } else {
                holder.mTvAddFocus.setText(mContext.getString(R.string.add_focus));
            }
        } else {
            holder.mTvAddFocus.setText(mContext.getString(R.string.add_focus));
        }
        Log.d(TAG, "showItem: " + bean.getAttentionType());
    }


}
