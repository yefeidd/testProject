package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.IndexRequestType;
import cn.zn.com.zn_android.viewfeatures.HKView;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/7/27 0027.
 */
public class HKPresenter extends BasePresenter<HKView> {
    private HKView hkView;
    private Context context;
    private Activity _activity;
    private ApiManager _apiManager;

    public HKPresenter(HKView hkView, Context context) {
        this.hkView = hkView;
        this.context = context;
        this._activity = (Activity) context;
        this._apiManager = ApiManager.getInstance();
    }

    public void queryHsIndex() {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryHsIndex(""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        hkView.onSuccess(IndexRequestType.HS_INDEX, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryShIndex: ", throwable);
                    hkView.onError(IndexRequestType.HS_INDEX, throwable);
                });
    }

    public void queryGqIndex() {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryGqIndex(""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        hkView.onSuccess(IndexRequestType.GQ_INDEX, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryShIndex: ", throwable);
                    hkView.onError(IndexRequestType.GQ_INDEX, throwable);
                });
    }

    public void queryHcIndex() {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryHcIndex(""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        hkView.onSuccess(IndexRequestType.HC_INDEX, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryShIndex: ", throwable);
                    hkView.onError(IndexRequestType.HC_INDEX, throwable);
                });
    }

    public void queryHK() {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryHK(""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        hkView.onSuccess(IndexRequestType.HK_LIST, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHK: ", throwable);
                    hkView.onError(IndexRequestType.HK_LIST, throwable);
                });
    }
}
