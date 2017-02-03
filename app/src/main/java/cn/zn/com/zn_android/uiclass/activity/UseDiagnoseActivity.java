package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.DiagnoseAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.DiagnoseBean;
import cn.zn.com.zn_android.model.bean.OrderInfoBean;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import de.greenrobot.event.EventBus;

/**
 * 使用诊股券
 * Created by Jolly on 2016/11/30.
 */

public class UseDiagnoseActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_dianose)
    XListView mXlvDianose;

    private ViewHolder mHolder;
    private DiagnoseAdapter mAdapter;
    private List<OrderInfoBean.TicListBean> ticList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_use_dianose);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.clearCaches();
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

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof List) {
            ticList = (List<OrderInfoBean.TicListBean>) event.getObject();
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.diagnose_use);
        mXlvDianose.setPullRefreshEnable(false);
        mXlvDianose.setPullLoadEnable(false);
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_use_dianose, null, false);
        mHolder = new ViewHolder(headView);
        mXlvDianose.addHeaderView(headView);

    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mHolder.mCbUseZnb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    for (int i = 0; i < ticList.size(); i ++) {
                        ticList.get(i).setChecked(false);
                    }
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    protected void initData() {
        mAdapter = new DiagnoseAdapter(this, ticList);
        mAdapter.setmCbUseZnb(mHolder.mCbUseZnb);
        mXlvDianose.setAdapter(mAdapter);

        String diagNum = String.format(getString(R.string.diagnose_count), ticList.size());
        SpannableString diagNumSs = new SpannableString(diagNum);
        diagNumSs.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)),
                1, 1 + (ticList.size()+ "").length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        mHolder.mTvCount.setText(diagNumSs);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                ArrayList<Integer> useTics = new ArrayList<>();
                for (int i = 0; i < ticList.size(); i ++) {
                    if (ticList.get(i).isChecked()) {
                        useTics.add(i);
                    }
                }
                setResult(OrderConfirmActivity._useTicCode, new Intent().putIntegerArrayListExtra("USE_TIC_LIST", useTics));
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            ArrayList<Integer> useTics = new ArrayList<>();
            for (int i = 0; i < ticList.size(); i ++) {
                if (ticList.get(i).isChecked()) {
                    useTics.add(i);
                }
            }
            setResult(OrderConfirmActivity._useTicCode, new Intent().putIntegerArrayListExtra("USE_TIC_LIST", useTics));
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    static class ViewHolder {
        @Bind(R.id.cb_use_znb)
        AppCompatCheckBox mCbUseZnb;
        @Bind(R.id.tv_count)
        TextView mTvCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
