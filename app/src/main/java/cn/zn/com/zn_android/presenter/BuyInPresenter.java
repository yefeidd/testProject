package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.model.entity.RetValue;
import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;
import cn.zn.com.zn_android.viewfeatures.BuyInView;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/12 0012.
 */
public class BuyInPresenter extends BasePresenter<BuyInView> {
    private Activity mActivity;
    private ApiManager _apiManager;
    private BuyInView buyInView;

    public BuyInPresenter(Activity activity, BuyInView buyInView) {
        this.mActivity = activity;
        this.buyInView = buyInView;
        this._apiManager = ApiManager.getInstance();
    }

    public void buySellStock(String sessionId, String code_id, String type) {
        AppObservable.bindActivity(mActivity, _apiManager.getService().buySellStock(sessionId, code_id, type))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    buyInView.onSuccess(SimulativeBoardType.BUY_STOCK, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "buySellStock: " + throwable);
                    buyInView.onError(SimulativeBoardType.BUY_STOCK, throwable);
                });
    }

    /**
     *
     * @param code_id
     * @param code_num
     * @param type
     * @param code_name
     * @param code_price
     * @param now
     */
    public void tradeStock(String sessionId, String code_id, String code_num, String type,
                           String code_name, String code_price, String now) {
        AppObservable.bindActivity(mActivity, _apiManager.getService().tradeStock(sessionId, code_id,
                code_num, type, code_name, code_price, now))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    buyInView.onSuccess(SimulativeBoardType.TRADE_STOCK, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "tradeStock: " + throwable);
                    buyInView.onError(SimulativeBoardType.TRADE_STOCK, throwable);
                });

    }
}
