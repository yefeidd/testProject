package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * 基础MVPActivity
 * Created by Jolly on 2016/6/28 0028.
 */
public abstract class BaseMVPActivity<V, T> extends BaseActivity {
    public T presenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        _setLayoutRes(initResLayout());
    }

    public abstract T initPresenter();

    public abstract int initResLayout();

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }


}
