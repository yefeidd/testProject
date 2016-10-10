package cn.zn.com.zn_android.presenter;

/**
 * Created by Jolly on 2016/5/27 0027.
 */
public abstract class BasePresenter<T> {
    public final String TAG = this.getClass().getSimpleName();
    public T mView;

    public void attach(T view) {
        this.mView = view;
    }

    public void dettach() {
        this.mView = null;
    }
}
