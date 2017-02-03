package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.HotStockGodViewHolder;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.DynamicExpertBean;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.TaActivity;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class HotStockGodModel extends ListviewItemModel {
    private DynamicExpertBean bean;
    private Activity mContext;

    public HotStockGodModel(Activity activity, DynamicExpertBean bean) {
        mContext = activity;
        this.bean = bean;
    }


    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        HotStockGodViewHolder hotStockGodViewHolder = (HotStockGodViewHolder) viewHolder;
        hotStockGodViewHolder.mIvHeadAvatars.setImageURI(Uri.parse(bean.getAvatars()));
        hotStockGodViewHolder.mTvName.setText(bean.getNickname());
        hotStockGodViewHolder.mTvButTime.setText(bean.getFirst_time());
        hotStockGodViewHolder.mTvMonthYield.setText(bean.getMon_income() + "%");
        hotStockGodViewHolder.mTvWeekYield.setText(bean.getWeek_income() + "%");
        hotStockGodViewHolder.mTvWinRate.setText(bean.getWin_rate() + "%");
        hotStockGodViewHolder.mTvYield.setText(bean.getProfit() + "%");
        hotStockGodViewHolder.mRlDoor.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType().setStockCode(bean.getUser_id()));
            mContext.startActivity(new Intent(mContext, TaActivity.class));
        });
        hotStockGodViewHolder.mBtnFocus.setOnClickListener(v -> {
            if (RnApplication.getInstance().getUserInfo().getIsLogin() == 1) {
                if (hotStockGodViewHolder.mBtnFocus.getText().equals(mContext.getString(R.string.add_focus))) {
                    attentionOther(bean.getUser_id(), hotStockGodViewHolder);
                } else {
                    unsetConcern(bean.getUser_id(), hotStockGodViewHolder);
                }
            } else {
//                    EventBus.getDefault().postSticky(new AnyEventType(bean));
                mContext.startActivity(new Intent(mContext, LoginActivity.class));
            }
        });

        if (null != bean.getAttentionType()) {
            if (bean.getAttentionType().equals("1")) {
                hotStockGodViewHolder.mBtnFocus.setText(mContext.getString(R.string.finish_focus));
            } else {
                hotStockGodViewHolder.mBtnFocus.setText(mContext.getString(R.string.add_focus));
            }
        }
    }

    private String TAG = "HotStockGodModel";

    public void attentionOther(String userId, HotStockGodViewHolder holder) {
        ApiManager.getInstance().getService().attentionOther(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(mContext, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            holder.mBtnFocus.setText(mContext.getString(R.string.finish_focus));
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });

//        AppObservable.bindActivity(mContext, ApiManager.getInstance().getService().attentionOther(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(mContext, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            holder.mBtnFocus.setText(mContext.getString(R.string.finish_focus));
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "attentionOther: ", throwable);
//                });
    }

    public void unsetConcern(String userId, HotStockGodViewHolder holder) {
        ApiManager.getInstance().getService().unsetConcern(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(mContext, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            holder.mBtnFocus.setText(mContext.getString(R.string.add_focus));
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });

//        AppObservable.bindActivity(mContext, ApiManager.getInstance().getService().unsetConcern(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(mContext, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            holder.mBtnFocus.setText(mContext.getString(R.string.add_focus));
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "attentionOther: ", throwable);
//                });
    }

}
