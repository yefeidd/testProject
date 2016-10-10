package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.SignRequestType;

/**
 * Created by Jolly on 2016/9/26 0026.
 */

public interface SignView extends BaseView {
    void onSuccess(SignRequestType requestType, Object object);

    void onError(SignRequestType requestType, Throwable t);
}
