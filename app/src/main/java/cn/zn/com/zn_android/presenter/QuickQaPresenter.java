package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.viewfeatures.QuickQaView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/11/25.
 */

public class QuickQaPresenter extends BasePresenter<QuickQaView> {
    public static final int STOCK_SEARCH = 0X010;
    public static final int REGULAR_STOCK= 0X011;

    private Activity mContext;
    private QuickQaView mView;
    private ApiManager _apiManager;

    public QuickQaPresenter(Activity mContext, QuickQaView mView) {
        this.mContext = mContext;
        this.mView = mView;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 搜索股票
     *
     * @param code    股票代码，拼音，中文
     * @param type    值为1显示5条，值为空10条
     * @param len     值为1,只显示沪深股票，为空都显示
     * @return
     */
    public void searchStock(String code, String type, String len) {
        _apiManager.getService().searchStock(code, type, len)
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue -> {
                    mView.onSuccess(STOCK_SEARCH, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "searchStock: ", throwable);
                    mView.onError(STOCK_SEARCH, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().searchStock(code, type, len))
//                .delay(200, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue -> {
//                    mView.onSuccess(STOCK_SEARCH, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "searchStock: ", throwable);
//                    mView.onError(STOCK_SEARCH, throwable);
//                });
    }

    public List<String> stockListFatory(List<OptionalStockBean> list) {
        List<String> data = new ArrayList<>();
        for (OptionalStockBean bean:list) {
            String sN = null;
            if (null != bean.getCode_name()) {
                sN = new StringBuilder(bean.getCode_name()).append("(").append(bean.getCode()).append(")").toString();
            } else if (null != bean.getName()) {
                sN = new StringBuilder(bean.getName()).append("(").append(bean.getCode()).append(")").toString();
            }
            data.add(sN);
        }
        return data;
    }

    public void isRegularStock(String tic_tit) {
        _apiManager.getService().regularStock(tic_tit)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue -> {
                    mView.onSuccess(REGULAR_STOCK, retValue);
                }, throwable -> {
                    Log.e(TAG, "isRegularStock: ", throwable);
                    mView.onError(REGULAR_STOCK, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().regularStock(tic_tit))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue -> {
//                    mView.onSuccess(REGULAR_STOCK, retValue);
//                }, throwable -> {
//                    Log.e(TAG, "isRegularStock: ", throwable);
//                    mView.onError(REGULAR_STOCK, throwable);
//                });
    }
}
