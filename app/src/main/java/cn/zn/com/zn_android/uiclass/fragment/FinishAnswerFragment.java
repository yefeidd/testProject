package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;

/**
 * Created by zjs on 2016/11/24 0024.
 * email: m15267280642@163.com
 * explain:已回答页面
 */

public class FinishAnswerFragment extends BaseMVPFragment<DiagnosedStockView, DiagnosesSocketPresenter> implements DiagnosedStockView, RadioGroup.OnCheckedChangeListener {


    @Bind(R.id.rb_self)
    RadioButton mRbSelf;
    @Bind(R.id.rb_give)
    RadioButton mRbGive;
    @Bind(R.id.rb_perfect)
    RadioButton mRbPerfect;
    @Bind(R.id.rg_title)
    RadioGroup mRgTitle;
    private BaseFragment[] fragments;
    private FinishProgenyFragment selfFragment;
    private FinishProgenyFragment giveFragment;
    private FinishProgenyFragment perfectFragment;
    private int CURRENT_POS = 0;


    @Override
    public DiagnosesSocketPresenter initPresenter() {
        return new DiagnosesSocketPresenter(_mActivity, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.fragment_finish_answer;
    }

    @Override
    protected void initView(View view) {
        mRbSelf.setChecked(true);
        initFragment();
    }


    @Override
    protected void initEvent() {
        mRgTitle.setOnCheckedChangeListener(this);
    }


    private void initFragment() {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        fragments = new BaseFragment[3];
        if (null == selfFragment) {
            selfFragment = new FinishProgenyFragment();
            Bundle bundle0 = new Bundle();
            bundle0.putInt("type", 0);
            selfFragment.setArguments(bundle0);
            fragments[0] = selfFragment;
            transaction.add(R.id.fl_content, selfFragment);
            transaction.hide(selfFragment);
        }
        if (null == giveFragment) {
            giveFragment = new FinishProgenyFragment();
            Bundle bundle1 = new Bundle();
            bundle1.putInt("type", 1);
            giveFragment.setArguments(bundle1);
            fragments[1] = giveFragment;
            transaction.add(R.id.fl_content, giveFragment);
            transaction.hide(giveFragment);

        }
        if (null == perfectFragment) {
            perfectFragment = new FinishProgenyFragment();
            Bundle bundle2 = new Bundle();
            bundle2.putInt("type", 2);
            perfectFragment.setArguments(bundle2);
            fragments[2] = perfectFragment;
            transaction.add(R.id.fl_content, perfectFragment);
            transaction.hide(perfectFragment);
        }
        //设置显示的fragment
        transaction.show(fragments[CURRENT_POS]);
        if (!transaction.isEmpty()) transaction.commit();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_self:
                switchFragment(0);//老师抢答
                break;
            case R.id.rb_give:
                switchFragment(1);//学员主动
                break;
            case R.id.rb_perfect:
                switchFragment(2);//精彩回答
                break;
        }
    }


    /**
     * 切换fragment的方法
     */
    public void switchFragment(int positon) {
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        if (CURRENT_POS != positon && positon < fragments.length) {
            transaction.hide(fragments[CURRENT_POS]).show(fragments[positon]).commit();
            CURRENT_POS = positon;
            ((FinishProgenyFragment) fragments[CURRENT_POS]).initData(true);
        }
    }

    @Override
    public void showLoading() {

    }


    @Override
    public void hideLoading() {

    }

    @Override
    public void onSuccess(DiagnosedType requestType, Object object) {

    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

}
