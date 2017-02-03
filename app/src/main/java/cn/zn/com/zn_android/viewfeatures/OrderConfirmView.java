package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.OrderConfirmType;

/**
 * Created by Jolly on 2016/11/28.
 */

public interface OrderConfirmView extends BaseView {
    void onSuccess(OrderConfirmType requestType, Object object);

    void onError(OrderConfirmType requestType, Throwable t);
}
