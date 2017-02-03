package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;
import cn.zn.com.zn_android.viewfeatures.TransactionDetailView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/21 0021.
 */
public class TransactionDetalPresenter extends BasePresenter<TransactionDetailView> {
    private Activity mActivity;
    private ApiManager _apiManager;
    private TransactionDetailView detailView;

    public TransactionDetalPresenter(Activity activity, TransactionDetailView detailView) {
        this.mActivity = activity;
        this.detailView = detailView;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 成交明细--当前持仓2
     *
     * @param code_id
     */
    public void queryHold(String sessionId, String code_id) {
        _apiManager.getService().queryHold(sessionId, code_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    detailView.onSuccess(SimulativeBoardType.QUERY_HOLD, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryHold: " + throwable);
                    detailView.onError(SimulativeBoardType.QUERY_HOLD, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().queryHold(sessionId, code_id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    detailView.onSuccess(SimulativeBoardType.QUERY_HOLD, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryHold: " + throwable);
//                    detailView.onError(SimulativeBoardType.QUERY_HOLD, throwable);
//                });
    }

    /**
     * 成交明细--当前持仓2
     *
     * @param code_id
     */
    public void queryHoldList(String sessionId, String code_id, int page, int pageSize) {
        _apiManager.getService().queryHoldList(sessionId, code_id, page, pageSize)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    detailView.onSuccess(SimulativeBoardType.QUERY_HOLD_LIST, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryHoldList: " + throwable);
                    detailView.onError(SimulativeBoardType.QUERY_HOLD_LIST, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().queryHoldList(sessionId, code_id, page, pageSize))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    detailView.onSuccess(SimulativeBoardType.QUERY_HOLD_LIST, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryHoldList: " + throwable);
//                    detailView.onError(SimulativeBoardType.QUERY_HOLD_LIST, throwable);
//                });
    }

    /**
     * 历史交易--成交明细 头部接口
     *
     * @param code_id 股票代码
     * @return
     */
    public void queryChangeList(String sessionId, String code_id) {
        _apiManager.getService().queryChangeList(sessionId, code_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    detailView.onSuccess(SimulativeBoardType.QUERY_CHANGE_LIST, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryChangeList: " + throwable);
                    detailView.onError(SimulativeBoardType.QUERY_CHANGE_LIST, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().queryChangeList(sessionId, code_id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    detailView.onSuccess(SimulativeBoardType.QUERY_CHANGE_LIST, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryChangeList: " + throwable);
//                    detailView.onError(SimulativeBoardType.QUERY_CHANGE_LIST, throwable);
//                });
    }

    /**
     * 成交明细--当前持仓2
     *
     * @param code_id 股票代码
     * @return
     */
    public void queryChangeCodeList(String sessionId, String code_id, int page, int page_size) {
        _apiManager.getService().queryChangeCodeList(sessionId, code_id, page, page_size)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    detailView.onSuccess(SimulativeBoardType.QUERY_CHANGE_CODE_LIST, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryChangeCodeList: " + throwable);
                    detailView.onError(SimulativeBoardType.QUERY_CHANGE_CODE_LIST, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().queryChangeCodeList(sessionId, code_id, page, page_size))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    detailView.onSuccess(SimulativeBoardType.QUERY_CHANGE_CODE_LIST, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryChangeCodeList: " + throwable);
//                    detailView.onError(SimulativeBoardType.QUERY_CHANGE_CODE_LIST, throwable);
//                });
    }

}
