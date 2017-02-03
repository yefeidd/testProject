package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.uiclass.fragment.MyAnswerFragment;
import cn.zn.com.zn_android.uiclass.fragment.MyQuestionFragment;
import de.greenrobot.event.EventBus;

/**
 * 我的问答
 * Created by Jolly on 2016/12/7.
 */

public class MyQAActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tl_qa)
    TabLayout mTlQa;
    @Bind(R.id.vp_qa)
    ViewPager mVpQa;

//    private MyQuestionFragment questionFragment = new MyQuestionFragment();
//    private MyAnswerFragment answerFragment = new MyAnswerFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_my_question);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.my_qa);
        setupViewPager();
        mTlQa.setupWithViewPager(mVpQa);
        mTlQa.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_bar_color));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }

    private void setupViewPager() {
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MyQuestionFragment(), getString(R.string.my_question));
        adapter.addFragment(new MyAnswerFragment(), getString(R.string.my_answer));
        mVpQa.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);       //统计时长
        super.onResume();
        //viewPager设置页面
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

}
