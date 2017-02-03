package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.viewfeatures.StockIndicesView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/8/25 0025.
 */
public class StockIndicesPresenter extends BasePresenter<StockIndicesView> {
    private StockIndicesView indicesView;
    private Activity _activity;
    private ApiManager _apiManager;

    public StockIndicesPresenter(StockIndicesView indicesView, Activity _activity) {
        this.indicesView = indicesView;
        this._activity = _activity;
        this._apiManager = ApiManager.getInstance();
    }

    public void queryShDetail() {
        _apiManager.getService().queryShDetail("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        indicesView.onSuccess(StockRequestType.QUERY_SH_DETAIL, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryShDetail: ", throwable);
                    indicesView.onError(StockRequestType.QUERY_SH_DETAIL, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryShDetail(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        indicesView.onSuccess(StockRequestType.QUERY_SH_DETAIL, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryShDetail: ", throwable);
//                    indicesView.onError(StockRequestType.QUERY_SH_DETAIL, throwable);
//                });
    }

    public void querySzDetail(String type) {
        _apiManager.getService().querySzDetail(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "querySzDetail: ", throwable);
                    indicesView.onError(StockRequestType.QUERY_SZ_DETAIL, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().querySzDetail(type))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "querySzDetail: ", throwable);
//                    indicesView.onError(StockRequestType.QUERY_SZ_DETAIL, throwable);
//                });
    }

    public void queryHkUpdownHs(String type) {
        _apiManager.getService().queryHkUpdownHs(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        if (type.equals("1")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HS_UP, returnValue.getData());
                        } else if (type.equals("2")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HS_DOWN, returnValue.getData());
                        } else if (type.equals("3")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HS_CHANGE, returnValue.getData());
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHkUpdownHs: ", throwable);
                    if (type.equals("1")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HS_UP, throwable);
                    } else if (type.equals("2")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HS_DOWN, throwable);
                    } else if (type.equals("3")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HS_CHANGE, throwable);
                    }
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkUpdownHs(type))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        if (type.equals("1")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HS_UP, returnValue.getData());
//                        } else if (type.equals("2")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HS_DOWN, returnValue.getData());
//                        } else if (type.equals("3")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HS_CHANGE, returnValue.getData());
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHkUpdownHs: ", throwable);
//                    if (type.equals("1")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HS_UP, throwable);
//                    } else if (type.equals("2")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HS_DOWN, throwable);
//                    } else if (type.equals("3")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HS_CHANGE, throwable);
//                    }
//                });
    }

    public void queryHkUpdownGq(String type) {
        _apiManager.getService().queryHkUpdownGq(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        if (type.equals("1")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_GQ_UP, returnValue.getData());
                        } else if (type.equals("2")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_GQ_DOWN, returnValue.getData());
                        } else if (type.equals("3")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_GQ_CHANGE, returnValue.getData());
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHkUpdownGq: ", throwable);
                    if (type.equals("1")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_GQ_UP, throwable);
                    } else if (type.equals("2")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_GQ_DOWN, throwable);
                    } else if (type.equals("3")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_GQ_CHANGE, throwable);
                    }
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkUpdownGq(type))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        if (type.equals("1")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_GQ_UP, returnValue.getData());
//                        } else if (type.equals("2")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_GQ_DOWN, returnValue.getData());
//                        } else if (type.equals("3")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_GQ_CHANGE, returnValue.getData());
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHkUpdownGq: ", throwable);
//                    if (type.equals("1")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_GQ_UP, throwable);
//                    } else if (type.equals("2")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_GQ_DOWN, throwable);
//                    } else if (type.equals("3")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_GQ_CHANGE, throwable);
//                    }
//                });
    }

    public void queryHkUpdownHc(String type) {
        _apiManager.getService().queryHkUpdownHc(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        if (type.equals("1")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HC_UP, returnValue.getData());
                        } else if (type.equals("2")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HC_DOWN, returnValue.getData());
                        } else if (type.equals("3")) {
                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HC_CHANGE, returnValue.getData());
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHkUpdownHc: ", throwable);
                    if (type.equals("1")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HC_UP, throwable);
                    } else if (type.equals("2")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HC_DOWN, throwable);
                    } else if (type.equals("3")) {
                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HC_CHANGE, throwable);
                    }
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkUpdownHc(type))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        if (type.equals("1")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HC_UP, returnValue.getData());
//                        } else if (type.equals("2")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HC_DOWN, returnValue.getData());
//                        } else if (type.equals("3")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_HK_UPDOWN_HC_CHANGE, returnValue.getData());
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHkUpdownHc: ", throwable);
//                    if (type.equals("1")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HC_UP, throwable);
//                    } else if (type.equals("2")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HC_DOWN, throwable);
//                    } else if (type.equals("3")) {
//                        indicesView.onError(StockRequestType.QUERY_HK_UPDOWN_HC_CHANGE, throwable);
//                    }
//                });
    }

    public void queryShCFG(String type) {
        _apiManager.getService().queryShCFG(type)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue-> {
                    if (null != retValue) {
                        if (type.equals("1")) {
                            indicesView.onSuccess(StockRequestType.QUERY_SH_UPDOWN_SSE, retValue.getData());
                        } else if (type.equals("2")) {
                            indicesView.onSuccess(StockRequestType.QUERY_SH_UPDOWN_SZ, retValue.getData());
                        } else if (type.equals("3")) {
                            indicesView.onSuccess(StockRequestType.QUERY_SH_UPDOWN_REM, retValue.getData());
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "queryHkUpdownGq: ", throwable);
                    if (type.equals("1")) {
                        indicesView.onError(StockRequestType.QUERY_SH_UPDOWN_SSE, throwable);
                    } else if (type.equals("2")) {
                        indicesView.onError(StockRequestType.QUERY_SH_UPDOWN_SZ, throwable);
                    } else if (type.equals("3")) {
                        indicesView.onError(StockRequestType.QUERY_SH_UPDOWN_REM, throwable);
                    }
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryShCFG(type))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue-> {
//                    if (null != retValue) {
//                        if (type.equals("1")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_SH_UPDOWN_SSE, retValue.getData());
//                        } else if (type.equals("2")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_SH_UPDOWN_SZ, retValue.getData());
//                        } else if (type.equals("3")) {
//                            indicesView.onSuccess(StockRequestType.QUERY_SH_UPDOWN_REM, retValue.getData());
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryHkUpdownGq: ", throwable);
//                    if (type.equals("1")) {
//                        indicesView.onError(StockRequestType.QUERY_SH_UPDOWN_SSE, throwable);
//                    } else if (type.equals("2")) {
//                        indicesView.onError(StockRequestType.QUERY_SH_UPDOWN_SZ, throwable);
//                    } else if (type.equals("3")) {
//                        indicesView.onError(StockRequestType.QUERY_SH_UPDOWN_REM, throwable);
//                    }
//                });
    }

    public void queryHkHsDetail() {
        _apiManager.getService().queryHkHsDetail("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue-> {
                    if (null != retValue) {
                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, retValue.getData());
                    }
                }, throwable -> {
                    indicesView.onError(StockRequestType.QUERY_HK_DETAIL_HS, throwable);
                    Log.e(TAG, "queryHkHsDetail: ", throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkHsDetail(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue-> {
//                    if (null != retValue) {
//                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, retValue.getData());
//                    }
//                }, throwable -> {
//                    indicesView.onError(StockRequestType.QUERY_HK_DETAIL_HS, throwable);
//                    Log.e(TAG, "queryHkHsDetail: ", throwable);
//                });
    }

    public void queryHkGqDetail() {
        _apiManager.getService().queryHkGqDetail("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue-> {
                    if (null != retValue) {
                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, retValue.getData());
                    }
                }, throwable -> {
                    indicesView.onError(StockRequestType.QUERY_HK_DETAIL_GQ, throwable);
                    Log.e(TAG, "queryHkGqDetail: ", throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkGqDetail(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue-> {
//                    if (null != retValue) {
//                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, retValue.getData());
//                    }
//                }, throwable -> {
//                    indicesView.onError(StockRequestType.QUERY_HK_DETAIL_GQ, throwable);
//                    Log.e(TAG, "queryHkGqDetail: ", throwable);
//                });
    }

    public void queryHkHcDetail() {
        _apiManager.getService().queryHkHcDetail("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue-> {
                    if (null != retValue) {
                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, retValue.getData());
                    }
                }, throwable -> {
                    indicesView.onError(StockRequestType.QUERY_HK_DETAIL_HC, throwable);
                    Log.e(TAG, "queryHkHcDetail: ", throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryHkHcDetail(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue-> {
//                    if (null != retValue) {
//                        indicesView.onSuccess(StockRequestType.QUERY_SZ_DETAIL, retValue.getData());
//                    }
//                }, throwable -> {
//                    indicesView.onError(StockRequestType.QUERY_HK_DETAIL_HC, throwable);
//                    Log.e(TAG, "queryHkHcDetail: ", throwable);
//                });
    }

}
