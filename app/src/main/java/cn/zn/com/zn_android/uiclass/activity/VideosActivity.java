package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.fragment.VideosFragment;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;

/**
 * 视频列表
 */
public class VideosActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.fl_videos)
    FrameLayout mFlVideos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_videos);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.video));
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fl_videos, VideosFragment.newInstance("", ""));
        transaction.commit();

    }

    @Override
    protected void initEvent() {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }
}
