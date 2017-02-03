package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.SignRequestType;
import cn.zn.com.zn_android.viewfeatures.SignView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/26 0026.
 */

public class SignPresenter extends BasePresenter<SignView> {
    private SignView signView;
    private Activity _activity;
    private ApiManager _apiManager;

    public SignPresenter(SignView signView, Activity activity) {
        this.signView = signView;
        this._activity = activity;
        this._apiManager = ApiManager.getInstance();
    }

    public void queryUserSign(String sessionId) {
        _apiManager.getService().queryUserSign(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    signView.onSuccess(SignRequestType.SIGN_INFO, retValue.getData());
                }, throwable -> {
                    signView.onError(SignRequestType.SIGN_INFO, throwable);
                    Log.e(TAG, "queryUserSign: ", throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryUserSign(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    signView.onSuccess(SignRequestType.SIGN_INFO, retValue.getData());
//                }, throwable -> {
//                    signView.onError(SignRequestType.SIGN_INFO, throwable);
//                    Log.e(TAG, "queryUserSign: ", throwable);
//                });
    }

    public void userSign(String sessionId) {
        _apiManager.getService().userSign(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    signView.onSuccess(SignRequestType.USER_SIGN, retValue.getData());
                }, throwable -> {
                    signView.onError(SignRequestType.USER_SIGN, throwable);
                    Log.e(TAG, "userSign: ", throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().userSign(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    signView.onSuccess(SignRequestType.USER_SIGN, retValue.getData());
//                }, throwable -> {
//                    signView.onError(SignRequestType.USER_SIGN, throwable);
//                    Log.e(TAG, "userSign: ", throwable);
//                });
    }

}
