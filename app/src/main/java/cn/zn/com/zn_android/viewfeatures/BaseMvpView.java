package cn.zn.com.zn_android.viewfeatures;

/**
 * Created by Jolly on 2016/9/13 0013.
 */
public interface BaseMvpView extends BaseView {
    void onSuccess(int type, Object object);

    void onError(int Type, Throwable throwable);

}
