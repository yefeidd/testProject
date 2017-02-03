package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.viewfeatures.StockView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 股票的增删改查业务
 * Created by Jolly on 2016/8/1 0001.
 */
public class StockPresenter extends BasePresenter<StockView> {
    private StockView stockView;
    private Activity _activity;
    private ApiManager _apiManager;

    public StockPresenter(StockView stockView, Activity mContext) {
        this.stockView = stockView;
        this._activity = mContext;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 用户添加自选股票
     * <p>
     *
     * @param ticker    股票代码
     */
    public void addSelfStock(String sessionId, String ticker) {
        _apiManager.getService().addSelfStock(sessionId, ticker)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        stockView.onSuccess(StockRequestType.ADD_SELF_SELECT, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "addSelfStock: ", throwable);
                    stockView.onError(StockRequestType.ADD_SELF_SELECT, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().addSelfStock(sessionId, ticker))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        stockView.onSuccess(StockRequestType.ADD_SELF_SELECT, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "addSelfStock: ", throwable);
//                    stockView.onError(StockRequestType.ADD_SELF_SELECT, throwable);
//                });
    }

    /**
     * 用户添加自选股票
     * <p>
     *
     * @param keyword   股票代码，股票拼音简称
     */
    public void searchStock(String keyword) {
        _apiManager.getService().searchStock(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        stockView.onSuccess(StockRequestType.SEARCH_STOCK, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "searchStock: ", throwable);
                    stockView.onError(StockRequestType.SEARCH_STOCK, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().searchStock(keyword))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        stockView.onSuccess(StockRequestType.SEARCH_STOCK, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "searchStock: ", throwable);
//                    stockView.onError(StockRequestType.SEARCH_STOCK, throwable);
//                });
    }

}
