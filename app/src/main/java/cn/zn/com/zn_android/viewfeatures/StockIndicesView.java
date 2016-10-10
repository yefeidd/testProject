package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.StockRequestType;

/**
 * Created by Jolly on 2016/8/25 0025.
 */
public interface StockIndicesView extends BaseView {
    void onSuccess(StockRequestType requestType, Object object);

    void onError(StockRequestType requestType, Throwable t);
}
