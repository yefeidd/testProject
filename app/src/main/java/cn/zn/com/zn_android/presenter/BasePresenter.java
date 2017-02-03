package cn.zn.com.zn_android.presenter;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by Jolly on 2016/5/27 0027.
 */
public abstract class BasePresenter<T> {
    public final String TAG = this.getClass().getSimpleName();
    public T mView;
    public CompositeSubscription msubscription;//管理所有的订阅

    public void attach(T view) {
        this.mView = view;
    }

    public void dettach() {
        this.mView = null;
    }

    public void destroy() {
        if (null != msubscription) {
            msubscription.unsubscribe();
        }
    }

}
