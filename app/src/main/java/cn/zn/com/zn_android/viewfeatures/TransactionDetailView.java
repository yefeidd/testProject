package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;

/**
 * Created by Jolly on 2016/9/21 0021.
 */
public interface TransactionDetailView extends BaseView {
    void onSuccess(SimulativeBoardType requestType, Object object);

    void onError(SimulativeBoardType requestType, Throwable t);
}
