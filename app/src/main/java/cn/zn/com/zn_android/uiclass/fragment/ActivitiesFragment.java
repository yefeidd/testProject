package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import com.tencent.smtt.sdk.WebChromeClient;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by WJL on 2016/3/11 0011 09:07.
 */
public class ActivitiesFragment extends BaseFragment {

    @Bind(R.id.wv_activity)
    X5WebView mWvActivity;
    @Bind(R.id.pb_web_load)
    ProgressBar mPbWebLoad;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_activities);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {

        mWvActivity.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(com.tencent.smtt.sdk.WebView view, int newProgress) {
                // TODO Auto-generated method stub
                mPbWebLoad.setProgress(newProgress);
                if (mPbWebLoad != null && newProgress != 100) {
                    mPbWebLoad.setVisibility(View.VISIBLE);
                } else if (mPbWebLoad != null) {
                    mPbWebLoad.setVisibility(View.GONE);
                }
            }

        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ActivitiesFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ActivitiesFragment");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
//        FrameLayout view = (FrameLayout) mWvActivity.getParent();
//        view.removeView(mWvActivity);
//        mWvActivity.destroy();

    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden && mWvActivity != null && mWvActivity.getUrl() == null) {
            mWvActivity.loadUrl(Constants_api.ACTIVITIS_URL);

        }
    }
}
