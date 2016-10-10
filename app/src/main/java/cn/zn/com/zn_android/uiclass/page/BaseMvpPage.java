package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;

/**
 * Created by Jolly on 2016/7/27 0027.
 */
public abstract class BaseMvpPage<V, T> extends BasePage {
    public T presenter;

    public BaseMvpPage(Activity activity) {
        super(activity);
        this.presenter = initPresenter();

    }

    public abstract T initPresenter();

}
