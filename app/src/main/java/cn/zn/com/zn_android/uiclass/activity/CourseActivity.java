package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.CoursesBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 课程表
 */
public class CourseActivity extends BaseActivity {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_course)
    TextView mTvCourse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_course);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.courses));
        queryCourse();
    }

    @Override
    protected void initEvent() {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CourseActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CourseActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    private void queryCourse() {
        AppObservable.bindActivity(this, _apiManager.getService().queryCourse(""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultCourses, throwable -> {
                    Log.e(TAG, "queryCourse: ", throwable);
                });
    }

    private void resultCourses(ReturnValue<CoursesBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            mTvCourse.setText(returnValue.getData().getContent());
        } else {
            ToastUtil.showShort(_mApplication, getString(R.string.about_clear));
        }
    }
}
