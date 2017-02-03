package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.OnLineTeacherRankingType;
import cn.zn.com.zn_android.viewfeatures.OnlineTeacherRankingView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/11/29.
 */

public class OnlineTeacherRankingPresenter extends BasePresenter<OnlineTeacherRankingView> {
    private Activity mActivity;
    private ApiManager mApiManager;

    public OnlineTeacherRankingPresenter(Activity mActivity) {
        this.mActivity = mActivity;
        this.mApiManager = ApiManager.getInstance();
    }

    /**
     * 精英投顾排行榜
     *
     * @param type  1:综合排序2:数量排序3:好评排序
     * @param sort  1为升序2位降序
     * @param page  从0开始
     * @param num   一页数量
     */
    public void queryDiagnoseTeacherList(int type, String sort, int page, String num) {
        mView.showLoading();
        mApiManager.getService().queryDiagnoseTeacherList(type + "", sort, page + "", num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mView.hideLoading();
                    mView.onSuccess(OnLineTeacherRankingType.QUERY_TEACHER_LIST, retValue.getData());
                }, throwable -> {
                    mView.hideLoading();
                    Log.e(TAG, "queryDiagnoseTeacherList", throwable);
                });

//        AppObservable.bindActivity(mActivity, mApiManager.getService().queryDiagnoseTeacherList(type + "", sort, page + "", num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mView.hideLoading();
//                    mView.onSuccess(OnLineTeacherRankingType.QUERY_TEACHER_LIST, retValue.getData());
//                }, throwable -> {
//                    mView.hideLoading();
//                    Log.e(TAG, "queryDiagnoseTeacherList", throwable);
//                });
    }
}
