package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import cn.zn.com.zn_android.R;
import com.umeng.analytics.MobclickAgent;

/**
 * 我的关注——私人
 * Created by Jolly on 2016/9/7 0007.
 */
public class FocusPersonFragment extends BaseFragment {

    public static FocusPersonFragment newInstance() {
        return new FocusPersonFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_focus_room);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

}
