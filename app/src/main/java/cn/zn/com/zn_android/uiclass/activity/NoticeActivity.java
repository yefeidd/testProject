package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.CoursesBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class NoticeActivity extends BaseActivity {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;

    private String tid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_notice);
    }

    public void onEventMainThread(AnyEventType event) {
        tid = (String) event.getObject();
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.notice));
        queryNotice();
    }

    @Override
    protected void initEvent() {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NoticeActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NoticeActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    private void queryNotice() {
        _apiManager.getService().queryNotice(tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultNotice, throwable -> {
                    Log.e(TAG, "queryNotice: ", throwable);
                });
//        AppObservable.bindActivity(this, _apiManager.getService().queryNotice(tid))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultNotice, throwable -> {
//                    Log.e(TAG, "queryNotice: ", throwable);
//                });
    }

    private void resultNotice(ReturnValue<CoursesBean> returnValue) {
        mTvNotice.setText(returnValue.getData().getMessage());
    }
}
