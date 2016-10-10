package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by Jolly on 2016/6/3 0003.
 */
public abstract class BaseMVPFragment<V, T> extends BaseFragment {
    public T presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter = initPresenter();
        _setLayoutRes(initResLayout());
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        presenter.attach((V)this); // 绑定
//    }
//
//    @Override
//    public void onDestroyView() {
//        presenter.dettach(); // 解绑
//        super.onDestroyView();
//    }

    // 实例化Presenter
    public abstract T initPresenter();

    public abstract int initResLayout();

//    public abstract void initView();

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {

    }
}
