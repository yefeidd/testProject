package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.MarketDetailType;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public interface MarketDetailView extends BaseView {
    void onSuccess(MarketDetailType requestType, Object object);

    void onError(MarketDetailType requestType, Throwable t);
}
