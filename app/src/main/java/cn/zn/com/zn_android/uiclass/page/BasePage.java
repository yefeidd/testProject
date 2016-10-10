package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import cn.zn.com.zn_android.R;

/**
 * 一个pager的基类
 * Created by zjs on 2016/3/25 0025.
 */
public class BasePage {
    public Activity mActivity;

    public FrameLayout flContent;// 空的帧布局对象, 要动态添加布局
    public View mRootView;// 当前页面的布局对象

    public BasePage(Activity activity) {
        mActivity = activity;
        mRootView = initView();
    }

    // 初始化布局
    public View initView() {
        View view = View.inflate(mActivity, R.layout.base_pager, null);
        flContent = (FrameLayout) view.findViewById(R.id.lv_chat);
        return view;
    }

    // 初始化数据
    public void initData() {
    }

    public void queryIndexData(){};
}
