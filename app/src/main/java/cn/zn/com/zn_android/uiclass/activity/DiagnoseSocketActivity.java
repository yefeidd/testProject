package cn.zn.com.zn_android.uiclass.activity;

/**
 * Created by zjs on 2016/11/24 0024.
 * email: m15267280642@163.com
 * explain: 诊股大厅(老师)
 */

import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.fragment.BaseFragment;
import cn.zn.com.zn_android.uiclass.fragment.FinishAnswerFragment;
import cn.zn.com.zn_android.uiclass.fragment.PreAnswerFragment;
import cn.zn.com.zn_android.uiclass.fragment.WaitAnswerFragment;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;

public class DiagnoseSocketActivity extends BaseMVPActivity<DiagnosedStockView, DiagnosesSocketPresenter> implements DiagnosedStockView, View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.rg_title)
    RadioGroup mRgTitle;
    private int CURRENT_POS = 0;
    private BaseFragment[] fragments;
    private PreAnswerFragment preAnswerFragment;
    private WaitAnswerFragment waitAnswerFragment;
    private FinishAnswerFragment finishAnswerFragment;


    @Override
    public DiagnosesSocketPresenter initPresenter() {
        return new DiagnosesSocketPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_diagnose_socket;
    }


    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.diagnoseSocketOffice));
        //初始化fragment
        initFragment();
    }


    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mRgTitle.setOnCheckedChangeListener(this);
    }


    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragments = new BaseFragment[3];
        if (null == preAnswerFragment) {
            preAnswerFragment = new PreAnswerFragment();
            fragments[0] = preAnswerFragment;
            transaction.add(R.id.fl_content, preAnswerFragment);
            transaction.hide(preAnswerFragment);
        }
        if (null == waitAnswerFragment) {
            waitAnswerFragment = new WaitAnswerFragment();
            fragments[1] = waitAnswerFragment;
            transaction.add(R.id.fl_content, waitAnswerFragment);
            transaction.hide(waitAnswerFragment);

        }
        if (null == finishAnswerFragment) {
            finishAnswerFragment = new FinishAnswerFragment();
            fragments[2] = finishAnswerFragment;
            transaction.add(R.id.fl_content, finishAnswerFragment);
            transaction.hide(finishAnswerFragment);
        }
        //设置显示的fragment
        transaction.show(fragments[0]);
        //记录当前显示的fragment
        CURRENT_POS = 0;
        if (!transaction.isEmpty()) transaction.commit();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
//            case:
//            break;
//            case:
//            break;
            default:
                break;
        }
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_pre_answer:
                switchFragment(0);//切换到preAnswerFrament
                break;
            case R.id.rb_wait_answer:
                switchFragment(1);//切换到waitAnswerFrament
                break;
            case R.id.rb_finish_answer:
                switchFragment(2);//切换到finishAnswerFrament
                break;
        }
    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        ButterKnife.unbind(this);
//    }

    /**
     * 切换fragment的方法
     */
    public void switchFragment(int positon) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        if (CURRENT_POS != positon && positon < fragments.length) {
            transaction.hide(fragments[CURRENT_POS]).show(fragments[positon]).commit();
            CURRENT_POS = positon;
            fragments[CURRENT_POS].initData(true);
            fragments[CURRENT_POS].startCountDown();
        }

    }

    /**
     * 显示进度条君
     */
    @Override
    public void showLoading() {

    }

    /**
     * 隐藏进度条君
     */
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
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);          //统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
