package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.viewfeatures.GeniusRankingView;
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
    public void queryWeekRankingList(int page, int num) {
        _apiManager.getService().querySortStockGenius(RnApplication.getInstance().getUserInfo().getSessionID(), page, num)
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

//        AppObservable.bindActivity(mActivity, _apiManager.getService().querySortStockGenius(RnApplication.getInstance().getUserInfo().getSessionID(), page, num))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        geniusRankingView.onSuccess(DXNR, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryWeekRankingList: ", throwable);
//                    geniusRankingView.onError(DXNR, throwable);
//                });
    }

    public void queryHotWarrenList(int page, int num) {
        _apiManager.getService().queryHotStockGenius(RnApplication.getInstance().getUserInfo().getSessionID(), page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    geniusRankingView.onSuccess(RQNR, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryHotWarrenList: ", throwable);
                    geniusRankingView.onError(RQNR, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().queryHotStockGenius(RnApplication.getInstance().getUserInfo().getSessionID(), page, num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    geniusRankingView.onSuccess(RQNR, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryHotWarrenList: ", throwable);
//                    geniusRankingView.onError(RQNR, throwable);
//                });
    }

    public void queryFyRanking(int page, int num) {
        _apiManager.getService().queryMoneyStockGenius(RnApplication.getInstance().getUserInfo().getSessionID(), page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    geniusRankingView.onSuccess(ZZQNR, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryFyRanking: ", throwable);
                    geniusRankingView.onError(ZZQNR, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().queryMoneyStockGenius(RnApplication.getInstance().getUserInfo().getSessionID(), page, num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    geniusRankingView.onSuccess(ZZQNR, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryFyRanking: ", throwable);
//                    geniusRankingView.onError(ZZQNR, throwable);
//                });
    }

}
