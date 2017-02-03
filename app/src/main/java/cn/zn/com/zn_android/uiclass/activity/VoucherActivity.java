package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.model.VoucherModel;
import cn.zn.com.zn_android.model.bean.ShareTicBean;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 诊股券
 * Created by Jolly on 2016/9/27 0027.
 */

public class VoucherActivity extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.lv_voucher)
    ListView mLvVoucher;
    @Bind(android.R.id.empty)
    TextView mEmpty;
    @Bind(R.id.ll_empty)
    LinearLayout mLlEmpty;

    private ListViewAdapter mAdapter;
    private List<VoucherModel> data = new ArrayList<>();
    private int count = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        EventBus.getDefault().registerSticky(this);
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

//    public void onEventMainThread(AnyEventType event) {
//        count = Integer.valueOf(event.getObject().toString());
//    }

//    @Override
//    protected void onDestroy() {
//        EventBus.getDefault().unregister(this);
//        super.onDestroy();
//    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.diagnosed));

        mEmpty.setText(String.format(getString(R.string.no_data), getString(R.string.diagnosed)));
        mLvVoucher.setEmptyView(mLlEmpty);

        mAdapter = new ListViewAdapter(this, R.layout.item_use_diagnose, data, "VoucherViewHolder");
        mLvVoucher.setAdapter(mAdapter);
        queryShareTicList(_mApplication.getUserInfo().getSessionID());
    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    private void queryShareTicList(String sessionId) {
        _apiManager.getService().queryShareTicList(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ret -> ApiManager.getInstance().processReturnListValue(ret))
                .subscribe(retValue -> {
                    for (ShareTicBean bean : retValue) {
                        VoucherModel model = new VoucherModel(bean);
                        data.add(model);
                    }
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    Log.e(TAG, "queryShareTicList: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryShareTicList(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(ret -> ApiManager.getInstance().processReturnListValue(ret))
//                .subscribe(retValue -> {
//                    for (ShareTicBean bean : retValue) {
//                        VoucherModel model = new VoucherModel(bean);
//                        data.add(model);
//                    }
//                    mAdapter.notifyDataSetChanged();
//                }, throwable -> {
//                    Log.e(TAG, "queryShareTicList: ", throwable);
//                });
    }
}
