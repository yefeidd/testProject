package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.QADetailType;
import cn.zn.com.zn_android.viewfeatures.QuestionAnswerDetailView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/12/2.
 */

public class QuestionAnswerDetailPresenter extends BasePresenter<QuestionAnswerDetailView> {
    private Activity mContext;
    private ApiManager mApiManager;

    public QuestionAnswerDetailPresenter(Activity mContext) {
        this.mContext = mContext;
        this.mApiManager = ApiManager.getInstance();
    }

    public void queryQADetail(String sessionId, String id) {
        mApiManager.getService().queryQADetail(sessionId, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mView.onSuccess(QADetailType.QA_DETAIL, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryQADetail: ", throwable);
                    mView.onError(QADetailType.QA_DETAIL, throwable);
                });

//        AppObservable.bindActivity(mContext, mApiManager.getService().queryQADetail(sessionId, id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mView.onSuccess(QADetailType.QA_DETAIL, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryQADetail: ", throwable);
//                    mView.onError(QADetailType.QA_DETAIL, throwable);
//                });
    }

    public void postEvaluate(String sessionId, String id, String score) {
        mApiManager.getService().postEvaluate(sessionId, id, score)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mView.onSuccess(QADetailType.POST_EVALUATE, retValue);
                }, throwable -> {
                    Log.e(TAG, "postEvaluate: ", throwable);
                    mView.onError(QADetailType.POST_EVALUATE, throwable);
                });

//        AppObservable.bindActivity(mContext, mApiManager.getService().postEvaluate(sessionId, id, score))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mView.onSuccess(QADetailType.POST_EVALUATE, retValue);
//                }, throwable -> {
//                    Log.e(TAG, "postEvaluate: ", throwable);
//                    mView.onError(QADetailType.POST_EVALUATE, throwable);
//                });
    }
}
