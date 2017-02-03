package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.viewfeatures.OptionalStockView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/6/28 0028.
 */
public class OptionalStockPresenter extends BasePresenter<OptionalStockView> {
    private OptionalStockView optionalStockView;
    private Activity _activity;
    private ApiManager _apiManager;

    public OptionalStockPresenter(OptionalStockView view, Activity activity) {
        this.optionalStockView = view;
        this._activity = activity;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 用户删除自选股票
     *
     * @param sessionId
     * @param id
     */
    public void delSelfStock(String sessionId, String id) {
        _apiManager.getService().delSelfStock(sessionId, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        optionalStockView.onSuccess(StockRequestType.DEL_SELF_SELECT, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "delSelfStock: ", throwable);
                    optionalStockView.onError(StockRequestType.DEL_SELF_SELECT, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().delSelfStock(sessionId, id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        optionalStockView.onSuccess(StockRequestType.DEL_SELF_SELECT, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "delSelfStock: ", throwable);
//                    optionalStockView.onError(StockRequestType.DEL_SELF_SELECT, throwable);
//                });
    }

}
