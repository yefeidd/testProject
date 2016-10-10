package cn.zn.com.zn_android.uiclass.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.helper.SpfHelper;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.utils.SystemBarTintManager;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * 基础Activity
 * <p>
 * Created by Administrator on 2016/3/9 0009.
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();

    protected SystemBarTintManager _mTintManager;
    protected RnApplication _mApplication = RnApplication.getInstance();
    protected SpfHelper _spfHelper;

    protected ApiManager _apiManager;
    protected CompositeSubscription _mSubscriptions = new CompositeSubscription();

    private boolean isLight = false;
    private int bg_res_id = R.color.app_bar_color;
    protected Activity _Activity = this;

    //提供一个获取apiManager的接口
    public ApiManager get_apiManager() {
        return _apiManager;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isLight) {
            // 设置沉浸式下拉通知栏
            setTanslucent();
        }

//        _mApplication = RnApplication.getInstance();
        _spfHelper = _mApplication.getSpfHelper();
        _apiManager = ApiManager.getInstance();
        _mApplication.addActivity(this);
    }

    private void setTanslucent() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        _mTintManager = new SystemBarTintManager(this);
        _mTintManager.setNavigationBarTintEnabled(false);// 激活导航栏设置
        _mTintManager.setStatusBarTintEnabled(true);
        _mTintManager.setStatusBarTintResource(bg_res_id);

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    /**
     * 设置沉浸式菜单 主题模式
     */
    protected void _setLightSystemBarTheme(boolean isLight) {
        this.isLight = isLight;
    }

    /**
     * 设置状态栏字体为黑色颜色
     */
    protected void setStatusBarBlackText() {
        _setLightSystemBarTheme(true);
        _mTintManager.setStatusBarTintResource(R.color.white);
        _mTintManager.setStatusBarDarkMode(true, this);
    }

    /**
     * 设置bar背景颜色
     */
    protected void _setBarColor(@ColorRes int resId) {
        bg_res_id = resId;
    }

    /**
     * 设置布局资源文件
     *
     * @param layoutResId
     */
    protected void _setLayoutRes(@LayoutRes int layoutResId) {
        setContentView(layoutResId);
        ButterKnife.bind(this);
        initView();
        initEvent();
        initData();
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();

    /**
     * 初始化事件
     */
    protected abstract void initEvent();

    /**
     * 请求数据
     */
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
    }

    /**
     * 保存信息
     *
     * @param key
     * @param value
     */
    public void setInfoSP(String key, String value) {
        SharedPreferences preferences = getSharedPreferences(Constants.SPF_NAME_APP,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(key, value);
        edit.commit();
    }

    /**
     * 获取信息
     *
     * @param key
     * @return
     */
    public String getInfoSP(String key) {
        SharedPreferences preferences = getSharedPreferences(Constants.SPF_NAME_APP,
                Context.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        _mApplication.removeActivity(this);
        if (_mSubscriptions != null && _mSubscriptions.hasSubscriptions()) {
            _mSubscriptions.unsubscribe();
            _mSubscriptions = null;
        }
        finish();
        super.onDestroy();
    }

}
