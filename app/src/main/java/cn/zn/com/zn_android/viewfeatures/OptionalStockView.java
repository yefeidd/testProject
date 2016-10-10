package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.StockRequestType;

/**
 * Created by Jolly on 2016/6/28 0028.
 */
public interface OptionalStockView extends BaseView {
    void onSuccess(StockRequestType requestType, Object object);

    void onError(StockRequestType requestType, Throwable t);

}
