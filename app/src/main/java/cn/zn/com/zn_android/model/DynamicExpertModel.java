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
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.DynamicExpertBean;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.TaActivity;
import cn.zn.com.zn_android.utils.ToastUtil;

import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/18 0018.
 */
public class DynamicExpertModel extends ListviewItemModel {
    private static final String TAG = "DynamicExpertModel";
    private DynamicExpertBean bean;
    private Activity mContext;

    public void setmListner(FocusChangeListner mListner) {
        this.mListner = mListner;
    }

    private FocusChangeListner mListner;

    public interface FocusChangeListner {
        void focusChange();
    }

    public DynamicExpertModel(Activity context, DynamicExpertBean bean) {
        this.bean = bean;
        this.mContext = context;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        DynamicExpertViewHolder holder = (DynamicExpertViewHolder) viewHolder;
        holder.mTvName.setText(bean.getNickname());
        holder.mTvTime.setText(String.format(mContext.getString(R.string.buy_in_time),
                bean.getFirst_time()));

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
        holder.mTvWeekProfit.setText(String.format(mContext.getString(R.string.week_profit), bean.getWeek_income()) + "%");
        holder.mSdvAvatar.setImageURI(Uri.parse(bean.getAvatars()));

        holder.mRlDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(bean.getUser_id()));
                mContext.startActivity(new Intent(mContext, TaActivity.class));
            }
        });
        holder.mTvAddFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (RnApplication.getInstance().getUserInfo().getIsLogin() == 1) {
                    if (holder.mTvAddFocus.getText().equals(mContext.getString(R.string.add_focus))) {
                        attentionOther(bean.getUser_id(), holder);
                    } else {
                        unsetConcern(bean.getUser_id(), holder);
                    }
                } else {
//                    EventBus.getDefault().postSticky(new AnyEventType(bean));
                    mContext.startActivity(new Intent(mContext, LoginActivity.class));
                }
            }
        });

        if (null != bean.getAttentionType()) {
            if (bean.getAttentionType().equals("1")) {
                holder.mTvAddFocus.setText(mContext.getString(R.string.finish_focus));
            } else {
                holder.mTvAddFocus.setText(mContext.getString(R.string.add_focus));
            }
        }
    }


    public void attentionOther(String userId, DynamicExpertViewHolder holder) {
        AppObservable.bindActivity(mContext, ApiManager.getInstance().getService().attentionOther(
                        RnApplication.getInstance().getUserInfo().getSessionID(), userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(mContext, retValue.getData().getMessage());
                        holder.mTvAddFocus.setText(mContext.getString(R.string.finish_focus));
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });
    }

    public void unsetConcern(String userId, DynamicExpertViewHolder holder) {
        AppObservable.bindActivity(mContext, ApiManager.getInstance().getService().unsetConcern(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(mContext, retValue.getData().getMessage());
                        holder.mTvAddFocus.setText(mContext.getString(R.string.add_focus));
                    }
                }, throwable -> {
                    Log.e(TAG, "unsetConcern: ", throwable);
                });
    }

}
