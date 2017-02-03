package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/28 0028.
 * email: m15267280642@163.com
 * explain:
 */

public class PresentScorePresenter {
    private static final String TAG = "PresentScorePresenter";
    private ApiManager _apiManager = ApiManager.getInstance();
    private RnApplication _mApplication = RnApplication.getInstance();
    private Activity mActivity;

    public PresentScorePresenter(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 报名赠送积分
     */
    public void presentScore() {
        _apiManager.getService().presentSignScore(_mApplication.getUserInfo().getSessionID(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reurnValue -> {
                    if (reurnValue != null && reurnValue.getMsg().equals("success")) {
                        Log.i(TAG, "presentScore: " + "赠送成功");
                    }
                }, Throwable -> {
                    Throwable.printStackTrace();
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().presentSignScore(_mApplication.getUserInfo().getSessionID(), ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(reurnValue -> {
//                    if (reurnValue != null && reurnValue.getMsg().equals("success")) {
//                        Log.i(TAG, "presentScore: " + "赠送成功");
//                    }
//                }, Throwable -> {
//                    Throwable.printStackTrace();
//                });
    }

    /**
     * 分享送积分接口
     */
    public void sharePresentScore() {
        _apiManager.getService().presentScore(_mApplication.getUserInfo().getSessionID(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(reurnValue -> {
                    if (reurnValue != null && reurnValue.getMsg().equals("success")) {
                        Log.i(TAG, "presentScore: " + "赠送成功");
                    }
                }, Throwable -> {
                    Throwable.printStackTrace();
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().presentScore(_mApplication.getUserInfo().getSessionID(), ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(reurnValue -> {
//                    if (reurnValue != null && reurnValue.getMsg().equals("success")) {
//                        Log.i(TAG, "presentScore: " + "赠送成功");
//                    }
//                }, Throwable -> {
//                    Throwable.printStackTrace();
//                });
    }
}
