package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.OrderConfirmType;
import cn.zn.com.zn_android.viewfeatures.OrderConfirmView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/11/28.
 */

public class OrderConfirmPresenter extends BasePresenter<OrderConfirmView> {
    private Activity mActivity;
    private ApiManager mApiManager;

    public OrderConfirmPresenter(Activity activity) {
        this.mActivity = activity;
        this.mApiManager = ApiManager.getInstance();
    }

    public void queryOrderInfo(String sessinId, String orderNum) {
        mApiManager.getService().queryOrderInfo(sessinId, orderNum)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue -> {
                    if (retValue == null || retValue.getData() == null) {
                    } else {
                        mView.onSuccess(OrderConfirmType.QUERY_ORDER_INFO, retValue.getData());
                    }
                }, throwable -> {
                    Log.e(TAG, "queryOrderInfo: ", throwable);
                    mView.onError(OrderConfirmType.QUERY_ORDER_INFO, throwable);
                });

//        AppObservable.bindActivity(mActivity, mApiManager.getService().queryOrderInfo(sessinId, orderNum))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue -> {
//                    if (retValue == null || retValue.getData() == null) {
//                    } else {
//                        mView.onSuccess(OrderConfirmType.QUERY_ORDER_INFO, retValue.getData());
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "queryOrderInfo: ", throwable);
//                    mView.onError(OrderConfirmType.QUERY_ORDER_INFO, throwable);
//                });
    }

    public void payOrder(String sessinId, String order_num, String zg_tic, String platform) {
        mApiManager.getService().payOrder(sessinId, order_num, zg_tic, platform)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(retValue -> {
                    mView.onSuccess(OrderConfirmType.PAY_ORDER, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "payOrder: ", throwable);
                    mView.onError(OrderConfirmType.PAY_ORDER, throwable);
                });

//        AppObservable.bindActivity(mActivity, mApiManager.getService().payOrder(sessinId, order_num, zg_tic, platform))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(retValue -> {
//                    mView.onSuccess(OrderConfirmType.PAY_ORDER, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "payOrder: ", throwable);
//                    mView.onError(OrderConfirmType.PAY_ORDER, throwable);
//                });
    }
}
