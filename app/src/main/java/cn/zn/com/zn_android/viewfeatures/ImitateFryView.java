package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;

/**
 * 模拟炒股
 * Created by Jolly on 2016/9/8 0008.
 */
public interface ImitateFryView extends BaseView {
    void onSuccess(SimulativeBoardType requestType, Object object);

    void onError(SimulativeBoardType requestType, Throwable t);
}
