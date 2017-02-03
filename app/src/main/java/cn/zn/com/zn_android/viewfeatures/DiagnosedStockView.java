package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;

/**
 * Created by Jolly on 2016/11/24.
 */

public interface DiagnosedStockView extends BaseView {
    void onSuccess(DiagnosedType requestType, Object object);

    void onError(DiagnosedType requestType, Throwable t);
}
