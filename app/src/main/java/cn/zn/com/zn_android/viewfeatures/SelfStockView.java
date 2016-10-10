package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.StockRequestType;

/**
 * Created by Jolly on 2016/8/1 0001.
 */
public interface SelfStockView extends BaseView {
    void onSuccess(StockRequestType requestType, Object object);

    void onError(StockRequestType requestType, Throwable t);
}
