package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.MarketDetailType;
import cn.zn.com.zn_android.viewfeatures.MarketDetailView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

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
        this.msubscription = new CompositeSubscription();
    }

    /**
     * 沪深股票股东，简况，财务信息
     * @param ticCode
     */
    public void queryTicInfo(String ticCode) {
        Subscription sub = _apiManager.getService().queryTicInfo(ticCode)
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
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryTicInfo(ticCode))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        detailView.onSuccess(MarketDetailType.STOCK_INFO_INTRU, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.d(TAG, "queryTicInfo: ", throwable);
//                    detailView.onError(MarketDetailType.STOCK_INFO_INTRU, throwable);
//                });
    }

    /**
     * 用户添加自选股票
     * <p>
     *
     * @param ticker 股票代码
     */
    public void addSelfStock(String sessionId, String ticker) {
        Subscription sub = _apiManager.getService().addSelfStock(sessionId, ticker)
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
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().addSelfStock(sessionId, ticker))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        detailView.onSuccess(MarketDetailType.ADD_SELF_SELECT, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "addSelfStock: ", throwable);
//                    detailView.onError(MarketDetailType.ADD_SELF_SELECT, throwable);
//                });
    }

    /**
     * 用户删除自选股票
     *
     * @param sessionId
     * @param id
     */
    public void delSelfStock(String sessionId, String id) {
        Subscription sub = _apiManager.getService().delSelfStock(sessionId, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        detailView.onSuccess(MarketDetailType.DEL_SELF_SELECT, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "delSelfStock: ", throwable);
                    detailView.onError(MarketDetailType.DEL_SELF_SELECT, throwable);
                });
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().delSelfStock(sessionId, id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        detailView.onSuccess(MarketDetailType.DEL_SELF_SELECT, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "delSelfStock: ", throwable);
//                    detailView.onError(MarketDetailType.DEL_SELF_SELECT, throwable);
//                });
    }


    /**
     * 查询股票详情
     */
    public void queryMarketDetail(String sessionID, String ticker) {
        Subscription sub = _apiManager.getService().queryMarketDetail(sessionID, ticker)
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
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryMarketDetail(sessionID, ticker))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        detailView.onSuccess(MarketDetailType.QUERY_MARKET_DETAIL, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "addSelfStock: ", throwable);
//                    detailView.onError(MarketDetailType.QUERY_MARKET_DETAIL, throwable);
//                });
    }

    public void queryHsNewsList(String type, String code, String page, String pageSize) {
        Subscription sub = _apiManager.getService().queryHsNewsList(type, code, page, pageSize)
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
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHsNewsList(type, code, page, pageSize))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        Log.d(TAG, "queryHsNewsList: " + returnValue.getData().size());
//                        detailView.onSuccess(MarketDetailType.QUERY_HS_NEWS_LIST, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHsNewsList: ", throwable);
//                    detailView.onError(MarketDetailType.QUERY_HS_NEWS_LIST, throwable);
//                });
    }

    public void queryHkNewsList(String type, String code, String page, String pageSize) {
        Subscription sub = _apiManager.getService().queryHkNewsList(type, code, page, pageSize)
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
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkNewsList(type, code, page, pageSize))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        Log.d(TAG, "queryHkNewsList: " + returnValue.getData().size());
//                        detailView.onSuccess(MarketDetailType.QUERY_HK_NEWS_LIST, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHkNewsList: ", throwable);
//                    detailView.onError(MarketDetailType.QUERY_HK_NEWS_LIST, throwable);
//                });
    }

    /**
     * 沪深股票股东，简况，财务信息
     *
     * @param ticCode
     */
    public void queryHkTicInfo(String ticCode) {
        Subscription sub = _apiManager.getService().queryHkTicInfo(ticCode)
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
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkTicInfo(ticCode))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        detailView.onSuccess(MarketDetailType.HK_STOCK_INFO_INTRU, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.d(TAG, "queryHkTicInfo: ", throwable);
//                    detailView.onError(MarketDetailType.HK_STOCK_INFO_INTRU, throwable);
//                });
    }

}
