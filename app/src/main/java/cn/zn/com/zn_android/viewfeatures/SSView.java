package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.IndexRequestType;

/**
 * Created by Jolly on 2016/7/27 0027.
 */
public interface SSView extends BaseView {
    void onSuccess(IndexRequestType requestType, Object object);

    void onError(IndexRequestType requestType, Throwable t);
}
