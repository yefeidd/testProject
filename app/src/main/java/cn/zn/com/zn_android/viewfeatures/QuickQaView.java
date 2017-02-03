package cn.zn.com.zn_android.viewfeatures;

/**
 * Created by Jolly on 2016/11/25.
 */

public interface QuickQaView extends BaseView {
    void onSuccess(int requestType, Object object);

    void onError(int requestType, Throwable t);
}
