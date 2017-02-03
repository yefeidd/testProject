package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.viewfeatures.UpDownRankingView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/8/16 0016.
 */
public class UpDownRankingPresenter extends BasePresenter<UpDownRankingView> {
    private UpDownRankingView upDownRankingView;
    private Activity _activity;
    private ApiManager _apiManager;

    public UpDownRankingPresenter(UpDownRankingView upDownRankingView, Activity _activity) {
        this.upDownRankingView = upDownRankingView;
        this._activity = _activity;
        this._apiManager = ApiManager.getInstance();
    }

    public void queryZfbList() {
        _apiManager.getService().queryZfbList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_ZFB_LIST, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryZfbList: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_ZFB_LIST, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryZfbList(""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_ZFB_LIST, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryZfbList: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_ZFB_LIST, throwable);
//                });
    }

    public void queryDfbList() {
        _apiManager.getService().queryDfbList("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_DFB_LIST, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryDfbList: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_DFB_LIST, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryDfbList(""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_DFB_LIST, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryDfbList: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_DFB_LIST, throwable);
//                });
    }

    public void queryHslList(String type) {
        _apiManager.getService().queryHslList(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_HSL_LIST, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHslList: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_HSL_LIST, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHslList(type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_HSL_LIST, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHslList: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_HSL_LIST, throwable);
//                });
    }

    public void queryZfList(String type) {
        _apiManager.getService().queryZfList(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_ZF_LIST, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryZfList: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_ZF_LIST, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryZfList(type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_ZF_LIST, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryZfList: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_ZF_LIST, throwable);
//                });
    }

    public void queryGjgbList(String type) {
        _apiManager.getService().queryGjbList(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        if ("1".equals(type)) {
                            upDownRankingView.onSuccess(StockRequestType.QUERY_DJGB_LIST, retValue.getData());
                        } else {
                            upDownRankingView.onSuccess(StockRequestType.QUERY_GJGB_LIST, retValue.getData());
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "queryGjgbList: ", throwable);
                    if ("1".equals(type)) {
                        upDownRankingView.onError(StockRequestType.QUERY_DJGB_LIST, throwable);
                    } else {
                        upDownRankingView.onError(StockRequestType.QUERY_GJGB_LIST, throwable);
                    }
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryGjbList(type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        if ("1".equals(type)) {
//                            upDownRankingView.onSuccess(StockRequestType.QUERY_DJGB_LIST, retValue.getData());
//                        } else {
//                            upDownRankingView.onSuccess(StockRequestType.QUERY_GJGB_LIST, retValue.getData());
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryGjgbList: ", throwable);
//                    if ("1".equals(type)) {
//                        upDownRankingView.onError(StockRequestType.QUERY_DJGB_LIST, throwable);
//                    } else {
//                        upDownRankingView.onError(StockRequestType.QUERY_GJGB_LIST, throwable);
//                    }
//                });
    }

    public void queryHotHyList(String codeId, String type) {
        _apiManager.getService().queryHotHyList(codeId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_HY_LIST, retValue.getData());

                    }
                }, throwable -> {
                    Log.e(TAG, "queryHotHyList: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_HY_LIST, throwable);

                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHotHyList(codeId, type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_HY_LIST, retValue.getData());
//
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHotHyList: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_HY_LIST, throwable);
//
//                });
    }

    public void queryHotGnList(String codeId, String type) {
        _apiManager.getService().queryHotGnList(codeId, type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_GN_LIST, retValue.getData());

                    }
                }, throwable -> {
                    Log.e(TAG, "queryHotGnList: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_GN_LIST, throwable);

                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHotGnList(codeId, type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_GN_LIST, retValue.getData());
//
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHotGnList: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_GN_LIST, throwable);
//
//                });
    }

    public void queryMainUpdown(String type) {
        _apiManager.getService().queryMainUpdown(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        if (type.equals("1")) {
                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_MAIN_UP, retValue.getData());
                        } else {
                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_MAIN_DOWN, retValue.getData());
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "queryMainUpdown: ", throwable);
                    if (type.equals("1")) {
                        upDownRankingView.onError(StockRequestType.QUERY_HK_MAIN_UP, throwable);
                    } else {
                        upDownRankingView.onError(StockRequestType.QUERY_HK_MAIN_DOWN, throwable);
                    }

                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryMainUpdown(type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        if (type.equals("1")) {
//                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_MAIN_UP, retValue.getData());
//                        } else {
//                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_MAIN_DOWN, retValue.getData());
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryMainUpdown: ", throwable);
//                    if (type.equals("1")) {
//                        upDownRankingView.onError(StockRequestType.QUERY_HK_MAIN_UP, throwable);
//                    } else {
//                    }
//
//                });
    }

    public void queryNewUpdown(String type) {
        _apiManager.getService().queryNewUpdown(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        if (type.equals("1")) {
                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_NEW_UP, retValue.getData());
                        } else {
                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_NEW_DOWN, retValue.getData());
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "queryNewUpdown: ", throwable);
                    if (type.equals("1")) {
                        upDownRankingView.onError(StockRequestType.QUERY_HK_NEW_UP, throwable);
                    } else {
                        upDownRankingView.onError(StockRequestType.QUERY_HK_NEW_DOWN, throwable);
                    }

                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryNewUpdown(type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        if (type.equals("1")) {
//                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_NEW_UP, retValue.getData());
//                        } else {
//                            upDownRankingView.onSuccess(StockRequestType.QUERY_HK_NEW_DOWN, retValue.getData());
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryNewUpdown: ", throwable);
//                    if (type.equals("1")) {
//                        upDownRankingView.onError(StockRequestType.QUERY_HK_NEW_UP, throwable);
//                    } else {
//                        upDownRankingView.onError(StockRequestType.QUERY_HK_NEW_DOWN, throwable);
//                    }
//
//                });
    }

    public void queryMainMoney(String type) {
        _apiManager.getService().queryMainMoney(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_HK_MAIN_MONEY, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryMainMoney: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_HK_MAIN_MONEY, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryMainMoney(type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_HK_MAIN_MONEY, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryMainMoney: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_HK_MAIN_MONEY, throwable);
//                });
    }

    public void queryNewMoney(String type) {
        _apiManager.getService().queryNewMoney(type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        upDownRankingView.onSuccess(StockRequestType.QUERY_HK_NEW_MONEY, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryNewMoney: ", throwable);
                    upDownRankingView.onError(StockRequestType.QUERY_HK_NEW_MONEY, throwable);

                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryNewMoney(type))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        upDownRankingView.onSuccess(StockRequestType.QUERY_HK_NEW_MONEY, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryNewMoney: ", throwable);
//                    upDownRankingView.onError(StockRequestType.QUERY_HK_NEW_MONEY, throwable);
//
//                });
    }

}
