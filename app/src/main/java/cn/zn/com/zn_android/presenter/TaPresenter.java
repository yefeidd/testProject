package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.DynamicExpertViewHolder;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.ExchangeRecordModel;
import cn.zn.com.zn_android.model.bean.ExchangeRecordBean;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.BaseMvpView;

import java.util.ArrayList;
import java.util.List;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/13 0013.
 */
public class TaPresenter extends BasePresenter<BaseMvpView> {
    private Activity mActivity;
    private ApiManager _apiManager;
    private final static int head = 0;
    private final static int detail = 1;
    private final static int history = 2;
    private final static int focus = 3;
    private final static int unfocus = 4;

    public TaPresenter(Activity mActivity) {
        this.mActivity = mActivity;
        this._apiManager = ApiManager.getInstance();
    }


    @Override
    public void attach(BaseMvpView view) {
        super.attach(view);
    }


    public void requestHeadDate(String sessionId, String userId) {
        AppObservable.bindActivity(mActivity, _apiManager.getService().queryOtherMsg(sessionId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        mView.onSuccess(head, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryMainMoney: ", throwable);
                    mView.onError(head, throwable);
                });
    }


    public void requestHistoryDate(String userId, int page, int pageSize) {
        AppObservable.bindActivity(mActivity, _apiManager.getService().queryOtherHistory(userId, page, pageSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        List<ExchangeRecordModel> historyModels = new ArrayList<>();
                        List<ExchangeRecordBean> historyList = retValue.getData();
                        for (ExchangeRecordBean bean : historyList) {
                            ExchangeRecordModel model = new ExchangeRecordModel(mActivity, bean);
                            historyModels.add(model);
                        }
                        mView.onSuccess(history, historyModels);
                    }
                }, throwable -> {
                    Log.e(TAG, "queryMainMoney: ", throwable);
                    mView.onError(history, throwable);
                });
    }

    public void attentionOther(String sessionId, String userId) {
        AppObservable.bindActivity(mActivity, _apiManager.getService().attentionOther(sessionId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        mView.onSuccess(focus, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                    mView.onError(focus, throwable);
                });
    }

    public void unsetConcern(String sessionId, String userId) {
        AppObservable.bindActivity(mActivity, ApiManager.getInstance().getService().unsetConcern(
                sessionId, userId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        mView.onSuccess(unfocus, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });
    }

}
