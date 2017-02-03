package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.QuestionDetailBean;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;

/**
 * Created by zjs on 2016/12/15 0015.
 * email: m15267280642@163.com
 * explain: 问题详情
 */

public class QuestionDetailActivity extends BaseMVPActivity<DiagnosedStockView, DiagnosesSocketPresenter> implements DiagnosedStockView, View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_stock)
    TextView mTvStock;
    @Bind(R.id.tv_costing)
    TextView mTvCosting;
    @Bind(R.id.content)
    TextView mContent;
    @Bind(R.id.tv_ask)
    TextView mTvAsk;
    private String id;
    private String type = "PreAnswer";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);

    }

    @Override
    public DiagnosesSocketPresenter initPresenter() {
        return new DiagnosesSocketPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_question_detail;
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.question_answer_detail);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvAsk.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        if (type.equals("PreAnswer")) {
            mTvAsk.setText("抢答");
        } else {
            mTvAsk.setText("回答");
        }
        presenter.queryAskInfo(id);
    }

    @Override
    public void onSuccess(DiagnosedType requestType, Object object) {
        if (requestType == DiagnosedType.QUESTION_DETAIL) {
            QuestionDetailBean bean = (QuestionDetailBean) object;
            String str0 = "咨询股票：" + bean.getSname() + "（" + bean.getScode() + "）";
            String str1 = "股票成本：" + bean.getScosts();
            String str2 = "        " + bean.getPdescription();
            SpannableString stockString = StringUtil.setForeColorSpan(str0, 0, 5, getResources().getColor(R.color.text_red));
            SpannableString costingString = StringUtil.setForeColorSpan(str1, 0, 5, getResources().getColor(R.color.text_red));
            mTvStock.setText(stockString);
            mTvCosting.setText(costingString);
            mContent.setText(str2);
        } else if (requestType == DiagnosedType.RESPONDER) {
            skipAnswerDetailActivity(id);
        }
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {
        if (requestType == DiagnosedType.QUESTION_DETAIL) {
            Log.e(TAG, "onError: " + t.getMessage());
            ToastUtil.show(this, "未查询到数据，" + t.getMessage(), Toast.LENGTH_SHORT);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            default:
                if (type.equals("PreAnswer")) {
                    presenter.rushAnswer(id, this);
                } else {
                    skipAnswerDetailActivity(id);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    public void skipAnswerDetailActivity(String id) {
        Intent intent = new Intent(this, TeacherAnswerDetailActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }


}

