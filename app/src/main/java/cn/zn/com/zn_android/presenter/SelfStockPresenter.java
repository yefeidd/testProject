package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.viewfeatures.SelfStockView;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/8/1 0001.
 */
public class SelfStockPresenter extends BasePresenter<SelfStockPresenter> {

    private SelfStockView selfStockView;
    private Activity _activity;
    private ApiManager _apiManager;

    public SelfStockPresenter(SelfStockView selfStockView, Context mContext) {
        this.selfStockView = selfStockView;
        this._activity = (Activity) mContext;
        this._apiManager = ApiManager.getInstance();
    }

    public void querySelfStock(String sessionId) {
        AppObservable.bindActivity(_activity, _apiManager.getService().querySelfStock(sessionId, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        selfStockView.onSuccess(StockRequestType.QUERY_SELF_SELECT, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "querySelfStock: ", throwable);
                    selfStockView.onError(StockRequestType.QUERY_SELF_SELECT, throwable);
                });
    }
}
