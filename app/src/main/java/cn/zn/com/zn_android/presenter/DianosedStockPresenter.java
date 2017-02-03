package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/11/24.
 */

public class DianosedStockPresenter extends BasePresenter<DiagnosedStockView> {
    private Activity mActivity;
    private DiagnosedStockView mDSView;
    private ApiManager mApiManager;

    public DianosedStockPresenter(Activity activity, DiagnosedStockView mDSView) {
        this.mActivity = activity;
        this.mDSView = mDSView;
        this.mApiManager = ApiManager.getInstance();
    }

    /**
     * 首页精彩回答
     *
     * @param sessionId
     * @param page
     * @param num
     */
    public void excellentAnswerList(String sessionId, int page, String num) {
        mApiManager.getService().excellentAnswerList(sessionId, page + "", num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mView.onSuccess(DiagnosedType.USER_EXCELLENT_ANSWER_LIST, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "excellentAnswerList: ", throwable);
                    mView.onError(DiagnosedType.USER_EXCELLENT_ANSWER_LIST, throwable);
                });

//        AppObservable.bindActivity(mActivity, mApiManager.getService().excellentAnswerList(sessionId, page + "", num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mView.onSuccess(DiagnosedType.USER_EXCELLENT_ANSWER_LIST, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "excellentAnswerList: ", throwable);
//                    mView.onError(DiagnosedType.USER_EXCELLENT_ANSWER_LIST, throwable);
//                });
    }

    /**
     * 诊股大厅用户首页接口
     */
    public void diagnoseIndex() {
        mApiManager.getService().diagnoseIndex("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mView.onSuccess(DiagnosedType.USER_DIAGNOSE_INDEX, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "diagnoseIndex: ", throwable);
                    mView.onError(DiagnosedType.USER_DIAGNOSE_INDEX, throwable);
                });

//        AppObservable.bindActivity(mActivity, mApiManager.getService().diagnoseIndex(""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mView.onSuccess(DiagnosedType.USER_DIAGNOSE_INDEX, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "diagnoseIndex: ", throwable);
//                    mView.onError(DiagnosedType.USER_DIAGNOSE_INDEX, throwable);
//                });
    }

}
