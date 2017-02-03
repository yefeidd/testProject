package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.IndexRequestType;
import cn.zn.com.zn_android.viewfeatures.SSView;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 沪深
 *
 * Created by Jolly on 2016/7/27 0027.
 */
public class SSPresenter extends BasePresenter<SSView> {
    private SSView SSView;
    private Context context;
    private Activity _activity;
    private ApiManager _apiManager;


    public SSPresenter(SSView SSView, Context context) {
        this.SSView = SSView;
        this.context = context;
        this._activity = (Activity) context;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 查询沪深指数
     */
    public void queryShIndex() {
        _apiManager.getService().queryShIndex("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        SSView.onSuccess(IndexRequestType.SH_INDEX, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryShIndex: ", throwable);
                    SSView.onError(IndexRequestType.SH_INDEX, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryShIndex(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        SSView.onSuccess(IndexRequestType.SH_INDEX, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryShIndex: ", throwable);
//                    SSView.onError(IndexRequestType.SH_INDEX, throwable);
//                });
    }

    /**
     * 查询深证指数
     */
    public void querySzIndex() {
        _apiManager.getService().querySzIndex("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        SSView.onSuccess(IndexRequestType.SZ_INDEX, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "querySzIndex: ", throwable);
                    SSView.onError(IndexRequestType.SZ_INDEX, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().querySzIndex(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        SSView.onSuccess(IndexRequestType.SZ_INDEX, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "querySzIndex: ", throwable);
//                    SSView.onError(IndexRequestType.SZ_INDEX, throwable);
//                });
    }

    /**
     * 查询创业板指
     */
    public void queryCyIndex() {
        _apiManager.getService().queryCyIndex("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(returnValue -> {
                    if (null != returnValue) {
                        SSView.onSuccess(IndexRequestType.CY_INDEX, returnValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryCyIndex: ", throwable);
                    SSView.onError(IndexRequestType.CY_INDEX, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryCyIndex(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnValue -> {
//                    if (null != returnValue) {
//                        SSView.onSuccess(IndexRequestType.CY_INDEX, returnValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryCyIndex: ", throwable);
//                    SSView.onError(IndexRequestType.CY_INDEX, throwable);
//                });
    }

//    /**
//     * 获取沪深股票涨跌幅排行
//     *
//     * @param field 可选参数，用,分隔,默认为空，返回全部字段，不选即为默认值。返回字段见下方
//     * @param exchangeCD 交易所代码；不输入返回沪深，输入XSHG只返回沪市，输入XSHE只返回深市
//     * @param pagesize
//     * @param pagenum
//     * @param desc 是否是跌幅排行；不输入返回涨幅排行；输入1返回跌幅排行
//     * @return
//     */
//    public void getEquRTRank(String field, String exchangeCD, String pagesize,
//                             String pagenum, String desc) {
//        AppObservable.bindActivity(_activity, _stockManager.getService().getEquRTRank(
//                Constants.STOCK_TOKEN, field, exchangeCD, pagesize, pagenum, desc))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue -> {
//                    if ("".equals(desc)) {
//                        SSView.onSuccess(IndexRequestType.UP_RT_RATE, retValue.getData());
//                    } else {
//                        SSView.onSuccess(IndexRequestType.DOWN_RT_RATE, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "getEquRTRank: ", throwable);
//                    if ("".equals(desc)) {
//                        SSView.onError(IndexRequestType.UP_RT_RATE, throwable);
//                    } else {
//                        SSView.onError(IndexRequestType.DOWN_RT_RATE, throwable);
//                    }
//                });
//    }

    /**
     * 行情首页各列表数据
     */
    public void querySS() {
        _apiManager.getService().querySS("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        SSView.onSuccess(IndexRequestType.SS_LIST, retValue.getData());
                    } else {
                        Log.e(TAG, "querySS: null");
                    }
                }, throwable -> {
                    Log.e(TAG, "querySS: ", throwable);
                    SSView.onError(IndexRequestType.SS_LIST, throwable);
                });

//        AppObservable.bindActivity(_activity, _apiManager.getService().querySS(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        SSView.onSuccess(IndexRequestType.SS_LIST, retValue.getData());
//                    } else {
//                        Log.e(TAG, "querySS: null");
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "querySS: ", throwable);
//                    SSView.onError(IndexRequestType.SS_LIST, throwable);
//                });
    }
}
