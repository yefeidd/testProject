package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 意见反馈
 */
public class FeedBackActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.et_feedback)
    EditText mEtFeedback;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_feed_back);
    }

    @Override
    protected void initView() {
        mTvSave.setVisibility(View.VISIBLE);
        mToolbarTitle.setText(getString(R.string.feedback));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvSave.setText(getString(R.string.confirm).replace(" ", ""));
        mTvSave.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_save:
                if (TextUtils.isEmpty(mEtFeedback.getText())) {
                    ToastUtil.showShort(this, getString(R.string.null_feedback));
                } else {
                    feedBack();
                }
                break;
        }
    }

    private void feedBack() {
        AppObservable.bindActivity(this, _apiManager.getService().feedback(_mApplication.getUserInfo().getSessionID(), mEtFeedback.getText().toString()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultFeedback, throwable -> {
                    Log.e(TAG, "feedBack: ", throwable);
                    ToastUtil.showShort(this, "提交失败，请稍后重试");
                });
    }

    private void resultFeedback(ReturnValue<MessageBean> returnValue) {
        if (returnValue.getMsg().equals(Constants.ERROR)) {
            if (returnValue.getData() != null && returnValue.getData().getMessage() != null) {
                ToastUtil.showShort(this, returnValue.getData().getMessage());
            }
        } else {
            ToastUtil.showShort(this, returnValue.getData().getMessage());
            mEtFeedback.setText("");
            UIUtil.hidekeyBoard(mEtFeedback);
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("FeedBackActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("FeedBackActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
