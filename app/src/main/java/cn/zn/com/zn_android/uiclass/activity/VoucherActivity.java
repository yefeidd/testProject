package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.VoucherModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 诊股券
 * Created by Jolly on 2016/9/27 0027.
 */

public class VoucherActivity extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.lv_voucher)
    ListView mLvVoucher;

    private ListViewAdapter mAdapter;
    private List<VoucherModel> data = new ArrayList<>();
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_voucher);
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
        count = Integer.valueOf(event.getObject().toString());
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.diagnosed));

        for (int i = 0; i < count; i ++) {
            VoucherModel model = new VoucherModel();
            data.add(model);
        }
        mAdapter = new ListViewAdapter(this, R.layout.item_voucher, data, "VoucherViewHolder");
        mLvVoucher.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }
}
