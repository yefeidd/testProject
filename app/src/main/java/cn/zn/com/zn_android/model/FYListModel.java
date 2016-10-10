package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.DynamicExpertViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.FYListHolder;
import cn.zn.com.zn_android.helper.SpfHelper;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.FyRankingBean;
import cn.zn.com.zn_android.model.bean.TrackRankingBean;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.OperatingDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.TaActivity;
import cn.zn.com.zn_android.uiclass.customerview.ShareDialogBuilder;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UnitUtils;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class FYListModel extends ListviewItemModel {
    private Activity _mActivity;
    private Dialog dialog;
    private Dialog actionDetailDialog;
    private ShareDialogBuilder builder;
    private boolean flag = true;
    private int type = 0;
    private SpfHelper spHelper;
    private final String STATUS_KEY = "share_status";
    private String curDate;
    private String saveDate;
    private Object object;
    private FyRankingBean fyBean;
    private TrackRankingBean trackBean;
    private int index;
    private PresentScorePresenter sharepresenter;


    public FYListModel setIndex(int index) {
        this.index = index;
        return this;
    }

    public FYListModel(Activity activity, int type) {
        this._mActivity = activity;
        builder = ShareDialogBuilder.getInstance();
        this.type = type;
        this.spHelper = RnApplication.getInstance().getSpfHelper();
        sharepresenter = new PresentScorePresenter(activity);
    }

    public FYListModel setBean(Object object) {
        this.object = object;
        return this;
    }


    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        FYListHolder fyListHolder = (FYListHolder) viewHolder;
        if (index > 2) {
            fyListHolder.mTvRank.setBackgroundResource(R.color.bar_bg_gray);
            fyListHolder.mTvRank.setTextColor(context.getResources().getColor(R.color.bar_text_title_gray));
        } else {
            fyListHolder.mTvRank.setBackgroundResource(R.color.text_red);
            fyListHolder.mTvRank.setTextColor(context.getResources().getColor(R.color.white));
        }
        if (0 == type) {
            fyListHolder.mTvAttention.setVisibility(View.GONE);
            fyListHolder.mTvAction.setVisibility(View.VISIBLE);
        } else {
            fyListHolder.mTvAction.setVisibility(View.GONE);
            fyListHolder.mTvAttention.setVisibility(View.VISIBLE);
        }
        fyListHolder.mTvRank.setText(String.valueOf(index + 1));

        switch (type) {
            case 0:
                fyBean = (FyRankingBean) object;
                fyListHolder.mTvName.setText(fyBean.getNickname());
                fyListHolder.mTvProfit.setText(fyBean.getProfit() + "%");
                fyListHolder.mTvTotal.setText(clacUnit(fyBean.getTotalmoney()));
                fyListHolder.mLlItem.setOnClickListener(v -> {
                    EventBus.getDefault().postSticky(new AnyEventType(fyBean.getUser_id()));
                    _mActivity.startActivity(new Intent(_mActivity, TaActivity.class));
                });
                fyListHolder.mTvAction.setOnClickListener(v -> {
                    curDate = DateUtils.getStringDate(System.currentTimeMillis(), "yyyy-MM-dd");
                    saveDate = spHelper.getData(STATUS_KEY);
                    if (curDate.equals(saveDate)) {
                        EventBus.getDefault().postSticky(new AnyEventType(fyBean.getUser_id()));
                        _mActivity.startActivity(new Intent(_mActivity, OperatingDetailActivity.class));
                    } else {
                        showShareDialog();
                    }
                });
                break;
            case 1:
                trackBean = (TrackRankingBean) object;
                fyListHolder.mTvName.setText(trackBean.getNickname());
                fyListHolder.mTvProfit.setText(trackBean.getProfit() + "%");
                fyListHolder.mTvTotal.setText(clacUnit(trackBean.getTotalmoney()));
                fyListHolder.mLlItem.setOnClickListener(v -> {
                    EventBus.getDefault().postSticky(new AnyEventType(trackBean.getUser_id()));
                    _mActivity.startActivity(new Intent(_mActivity, TaActivity.class));
                });
                break;
        }
        fyListHolder.mTvAttention.setOnClickListener(v -> {
            if (RnApplication.getInstance().getUserInfo().getIsLogin() == 1) {
                if (fyListHolder.mTvAttention.getText().equals(_mActivity.getString(R.string.add_focus))) {
                    attentionOther(trackBean.getUser_id(), fyListHolder);
                } else {
                    unsetConcern(trackBean.getUser_id(), fyListHolder);
                }
            } else {
                _mActivity.startActivity(new Intent(_mActivity, LoginActivity.class));
            }
        });

        if (null != trackBean) {
            if (trackBean.getAttentionType() == 1) {
                fyListHolder.mTvAttention.setText(_mActivity.getString(R.string.finish_focus));
            } else {
                fyListHolder.mTvAttention.setText(_mActivity.getString(R.string.add_focus));
            }
        }
    }


    /**
     * 分享的时候的dialog
     */
    private void showShareDialog() {
        builder.setActivity(_mActivity);
        builder.setUmShareListener(shareListener);
        dialog = builder.getDialog();
//        builder.setIsWidthFull(true);
        dialog.show();
    }

    private String clacUnit(String str) {
        try {
            Float num = Float.valueOf(str);
            StringBuilder sb = new StringBuilder();
            String unit = UnitUtils.getVolUnit1(num);
            float vol = UnitUtils.getVol(num);
            sb.append(vol).append(unit);
            return sb.toString();
        } catch (Exception e) {
            return str;
        }
    }


    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(_mActivity, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            flag = false;
            dialog.dismiss();
            curDate = DateUtils.getStringDate(System.currentTimeMillis(), "yyyy-MM-dd");
            spHelper.saveData(STATUS_KEY, curDate);
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(_mActivity, platform + " 分享失败", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (flag) {
                Toast.makeText(_mActivity, platform + " 分享取消", Toast.LENGTH_SHORT).show();
            } else {
                flag = true;
            }
            dialog.dismiss();
        }
    };

    private String TAG = "FYListModel";

    public void attentionOther(String userId, FYListHolder holder) {
        AppObservable.bindActivity(_mActivity, ApiManager.getInstance().getService().attentionOther(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(_mActivity, retValue.getData().getMessage());
                        holder.mTvAttention.setText(_mActivity.getString(R.string.finish_focus));
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });
    }

    public void unsetConcern(String userId, FYListHolder holder) {
        AppObservable.bindActivity(_mActivity, ApiManager.getInstance().getService().unsetConcern(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(_mActivity, retValue.getData().getMessage());
                        holder.mTvAttention.setText(_mActivity.getString(R.string.add_focus));
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });
    }

}
