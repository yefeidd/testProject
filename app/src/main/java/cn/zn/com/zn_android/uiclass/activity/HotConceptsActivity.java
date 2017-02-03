package cn.zn.com.zn_android.uiclass.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.HotAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotConceptBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.service.RefreshDataService;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 热门行业，热门概念 列表
 * Created by Jolly on 2016/7/14 0014.
 */
public class HotConceptsActivity extends BaseActivity implements View.OnClickListener,
    XListView.IXListViewListener, AdapterView.OnItemClickListener {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.iv_up_down)
    ImageView mIvUpDown;
    @Bind(R.id.ll_up_down)
    LinearLayout mLlUpDown;
    @Bind(R.id.xlv_hot)
    XListView mXlvHot;

    private HotAdapter hotAdapter;
    private List<HotConceptBean> data = new ArrayList<>();
    private boolean orderUpDown = true;//true代表升序排列,false代表降序排列
    private String title;

    LocalBroadcastManager lbm;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshDataService.TAG)) {
                Log.d(TAG, "onReceive: ");
                getData();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_hot_concept);
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
        lbm.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(RefreshDataService.TAG);
        lbm.registerReceiver(mReceiver, mFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            title = (String) event.getObject();
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(title);
        hotAdapter = new HotAdapter(this, R.layout.item_hot_concept, data);
        mXlvHot.setAdapter(hotAdapter);
        getData();
        lbm = LocalBroadcastManager.getInstance(this);
    }

    @Override
    protected void initEvent() {
        mLlUpDown.setOnClickListener(this);
        mIvLeftmenu.setOnClickListener(this);
        mXlvHot.setXListViewListener(this);
        mXlvHot.setPullLoadEnable(false);
        mXlvHot.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.ll_up_down:
                if (orderUpDown) {
                    mIvUpDown.setImageResource(R.drawable.down_arrows);
                    orderUpDown = !orderUpDown;
                } else {
                    mIvUpDown.setImageResource(R.drawable.up_arrows);
                    orderUpDown = !orderUpDown;
                }
                getData();
                break;
        }
    }

    private void getData() {
        if (title.equals(getString(R.string.hot_industry))) {
            getHotHy();
        } else {
            getHotGn();
        }
    }

    private void getHotHy() {
        _apiManager.getService().queryHotHy(orderUpDown ? "1" : "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultHotHy, throwable -> {
                    mXlvHot.stopRefresh();
                    Log.e(TAG, "getHotHy: ", throwable);
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryHotHy(orderUpDown ? "1" : "2"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultHotHy, throwable -> {
//                    mXlvHot.stopRefresh();
//                    Log.e(TAG, "getHotHy: ", throwable);
//                    ToastUtil.showShort(this, getString(R.string.no_net));
//                });
    }

    private void resultHotHy(ReturnListValue<HotConceptBean> returnListValue) {
        mXlvHot.stopRefresh();
        if (null != returnListValue) {
            if (null != returnListValue.getData()) {
                hotAdapter.setData(returnListValue.getData());
            }
        }
    }

    private void getHotGn() {
        _apiManager.getService().queryHotGn(orderUpDown ? "1" : "2")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultHotGn, throwable -> {
                    mXlvHot.stopRefresh();
                    Log.e(TAG, "getHotGn: ", throwable);
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryHotGn(orderUpDown ? "1" : "2"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultHotGn, throwable -> {
//                    mXlvHot.stopRefresh();
//                    Log.e(TAG, "getHotGn: ", throwable);
//                    ToastUtil.showShort(this, getString(R.string.no_net));
//                });
    }

    private void resultHotGn(ReturnListValue<HotConceptBean> returnListValue) {
        mXlvHot.stopRefresh();
        if (null != returnListValue) {
            if (null != returnListValue.getData()) {
                hotAdapter.setData(returnListValue.getData());
            }
        }
    }

    @Override
    public void onRefresh() {
        getData();
    }

    @Override
    public void onLoadMore() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) {
            return;
        }
        AnyEventType et = new AnyEventType(hotAdapter.getItem(position - 1));
        if (title.equals(getString(R.string.hot_concept))) {
            et.setState(true);
        } else {
            et.setState(false);
        }
        EventBus.getDefault().postSticky(et);
        startActivity(new Intent(this, UpDownRankingActivity.class));
    }
}
