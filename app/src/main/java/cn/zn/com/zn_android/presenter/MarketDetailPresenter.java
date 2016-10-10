package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.MarketDetailType;
import cn.zn.com.zn_android.viewfeatures.MarketDetailView;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class MarketDetailPresenter extends BasePresenter<MarketDetailView> {
    private MarketDetailView detailView;
    private Activity _activity;
    private ApiManager _apiManager;

    public MarketDetailPresenter(MarketDetailView detailView, Activity _activity) {
        this.detailView = detailView;
        this._activity = _activity;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 沪深股票股东，简况，财务信息
     * @param ticCode
     */
    public void queryTicInfo(String ticCode) {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryTicInfo(ticCode))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        detailView.onSuccess(MarketDetailType.STOCK_INFO_INTRU, retValue.getData());
                    }
                }, throwable -> {
                    Log.d(TAG, "queryTicInfo: ", throwable);
                    detailView.onError(MarketDetailType.STOCK_INFO_INTRU, throwable);
                });
    }

    /**
     * 用户添加自选股票
     * <p>
     *
     * @param ticker 股票代码
     */
    public void addSelfStock(String sessionId, String ticker) {
        AppObservable.bindActivity(_activity, _apiManager.getService().addSelfStock(sessionId, ticker))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        detailView.onSuccess(MarketDetailType.ADD_SELF_SELECT, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "addSelfStock: ", throwable);
                    detailView.onError(MarketDetailType.ADD_SELF_SELECT, throwable);
                });
    }


    /**
     * 查询股票详情
     */
    public void queryMarketDetail(String ticker) {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryMarketDetail(ticker))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        detailView.onSuccess(MarketDetailType.QUERY_MARKET_DETAIL, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "addSelfStock: ", throwable);
                    detailView.onError(MarketDetailType.QUERY_MARKET_DETAIL, throwable);
                });
    }

    public void queryHsNewsList(String type, String code, String page, String pageSize) {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryHsNewsList(type, code, page, pageSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        Log.d(TAG, "queryHsNewsList: " + returnValue.getData().size());
                        detailView.onSuccess(MarketDetailType.QUERY_HS_NEWS_LIST, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHsNewsList: ", throwable);
                    detailView.onError(MarketDetailType.QUERY_HS_NEWS_LIST, throwable);
                });
    }

    public void queryHkNewsList(String type, String code, String page, String pageSize) {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkNewsList(type, code, page, pageSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        Log.d(TAG, "queryHkNewsList: " + returnValue.getData().size());
                        detailView.onSuccess(MarketDetailType.QUERY_HK_NEWS_LIST, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHkNewsList: ", throwable);
                    detailView.onError(MarketDetailType.QUERY_HK_NEWS_LIST, throwable);
                });
    }

    /**
     * 沪深股票股东，简况，财务信息
     *
     * @param ticCode
     */
    public void queryHkTicInfo(String ticCode) {
        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkTicInfo(ticCode))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        detailView.onSuccess(MarketDetailType.HK_STOCK_INFO_INTRU, retValue.getData());
                    }
                }, throwable -> {
                    Log.d(TAG, "queryHkTicInfo: ", throwable);
                    detailView.onError(MarketDetailType.HK_STOCK_INFO_INTRU, throwable);
                });
    }

}
