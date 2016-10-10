package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.view.View;

/**
 * Created by zjs on 2016/7/13 0013.
 * email: m15267280642@163.com
 * explain:
 */
public abstract class BaseChartPage {

    protected Activity mActivity;
    protected View mContentView;
    protected int visibility;

    public BaseChartPage(Activity activity) {
        this.mActivity = activity;
        mContentView = initView();
    }


    public BaseChartPage(Activity activity, int visibility) {
        this.mActivity = activity;
        this.visibility = visibility;
        mContentView = initView();
    }

    /**
     * 初始化布局，返回一个填充好的view
     */
    public abstract View initView();

    /**
     * 初始化数据
     */
    public abstract void initData();

    /**
     * 初始化图表显示属性，比如线条颜色，字体颜色等
     */
    public abstract void initPage();

    /**
     * 获取该View
     */
    public View getmContentView() {
        return mContentView;
    }

}
