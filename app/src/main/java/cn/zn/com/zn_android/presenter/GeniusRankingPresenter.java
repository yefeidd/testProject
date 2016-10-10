package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.os.Message;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.FYListModel;
import cn.zn.com.zn_android.model.bean.FyRankingBean;
import cn.zn.com.zn_android.uiclass.activity.EarningsRankingActivity;
import cn.zn.com.zn_android.viewfeatures.GeniusRankingView;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 人气牛人、最赚钱牛人、短线牛人
 * Created by Jolly on 2016/9/28 0028.
 */

public class GeniusRankingPresenter extends BasePresenter<GeniusRankingView> {
    public static final int RQNR = 0X01; // 人气牛人
    public static final int ZZQNR = 0X02; // 最赚钱牛人
    public static final int DXNR = 0X03; // 短线牛人

    private Activity mActivity;
    private GeniusRankingView geniusRankingView;
    private ApiManager _apiManager;

    public GeniusRankingPresenter(Activity mActivity, GeniusRankingView geniusRankingView) {
        this.mActivity = mActivity;
        this.geniusRankingView = geniusRankingView;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 短线牛人列表
     */
    public void queryWeekRankingList() {
        AppObservable.bindActivity(mActivity, _apiManager.getService().queryWeekRankingList(RnApplication.getInstance().getUserInfo().getSessionID(), ""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        geniusRankingView.onSuccess(DXNR, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryWeekRankingList: ", throwable);
                    geniusRankingView.onError(DXNR, throwable);
                });
    }

    public void queryHotWarrenList(int page, int num) {
        AppObservable.bindActivity(mActivity, _apiManager.getService().queryHotWarrenList(RnApplication.getInstance().getUserInfo().getSessionID(), page, num))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    geniusRankingView.onSuccess(RQNR, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryHotWarrenList: ", throwable);
                    geniusRankingView.onError(RQNR, throwable);
                });
    }

    public void queryFyRanking(int page, int num) {
        AppObservable.bindActivity(mActivity, _apiManager.getService().queryFyRanking(RnApplication.getInstance().getUserInfo().getSessionID(), String.valueOf(page), String.valueOf(num)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    geniusRankingView.onSuccess(ZZQNR, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryFyRanking: ", throwable);
                    geniusRankingView.onError(ZZQNR, throwable);
                });
    }

}
