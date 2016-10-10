package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.zn.com.zn_android.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by WJL on 2016/3/11 0011 09:04.
 */
public class NewsFragment extends BaseFragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news, container, false);

        initView(view);
        initEvent();

        return view;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("NewsFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("NewsFragment");
    }
}
