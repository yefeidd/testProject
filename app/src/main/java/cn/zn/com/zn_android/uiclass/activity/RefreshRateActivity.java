package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.RefreshRateAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.RefreshRateBean;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/7/1 0001.
 */
public class RefreshRateActivity extends BaseActivity {
    @Bind(R.id.ll_data_traffic)
    ScrollListView mLlDataTraffic;
    @Bind(R.id.ll_wifi)
    ScrollListView mLlWifi;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    private List<RefreshRateBean> data = new ArrayList<>();
    private List<RefreshRateBean> wifiData = new ArrayList<>();
    private int dataIndex = 0;
    private int wifiIndex = 1;
    private RefreshRateAdapter dataTrafficAdpter;
    private RefreshRateAdapter wifiAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_refresh_rate);
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

    private void getDataTraffic() {
        for (int i = 0; i < 5; i++) {
            RefreshRateBean bean;
            if (_spfHelper.getIntData(Constants.SPF_KEY_REFRESH_DT) == i) {
                if (_spfHelper.getIntData(Constants.SPF_KEY_REFRESH_DT) == 0) {
                    bean = new RefreshRateBean("不刷新", true);
                } else {
                    bean = new RefreshRateBean(i * 5 + "秒", true);
                    dataIndex = _spfHelper.getIntData(Constants.SPF_KEY_REFRESH_DT);
                }
            } else {
                if (0 == i) {
                    bean = new RefreshRateBean("不刷新", false);
                } else {
                    bean = new RefreshRateBean(i * 5 + "秒", false);
                }
            }
            data.add(bean);
        }
    }

    private void getWifiData() {
        if (_spfHelper.getIntData(Constants.SPF_KEY_REFRESH_WIFI) == 0) {
            RefreshRateBean bean1 = new RefreshRateBean("不刷新", true);
            wifiData.add(bean1);
            RefreshRateBean bean2 = new RefreshRateBean("5秒", false);
            wifiData.add(bean2);
            wifiIndex = 0;
        } else {
            RefreshRateBean bean1 = new RefreshRateBean("不刷新", false);
            wifiData.add(bean1);
            RefreshRateBean bean2 = new RefreshRateBean("5秒", true);
            wifiData.add(bean2);
        }

    }

    @Override
    protected void initView() {
        getDataTraffic();
        getWifiData();
        dataTrafficAdpter = new RefreshRateAdapter(this, android.R.layout.simple_list_item_1,
                data);
        mLlDataTraffic.setAdapter(dataTrafficAdpter);
        wifiAdapter = new RefreshRateAdapter(this, android.R.layout.simple_list_item_1,
                wifiData);
        mLlWifi.setAdapter(wifiAdapter);

        mToolbarTitle.setText(getString(R.string.about_refresh_rate));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(v -> {
            finish();
        });

        mLlDataTraffic.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != dataIndex) {
                    boolean dataSelected = data.get(position).isSelected();
                    data.get(position).setSelected(!dataSelected);
                    data.get(dataIndex).setSelected(false);
                    dataIndex = position;
                    dataTrafficAdpter.notifyDataSetChanged();
                    _spfHelper.saveIntData(Constants.SPF_KEY_REFRESH_DT, position);
                    try {
                        _mApplication.getmRefreshBinder().transact(0xff, null, null, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        mLlDataTraffic.deferNotifyDataSetChanged();

        mLlWifi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position != wifiIndex) {
                    boolean dataSelected = wifiData.get(position).isSelected();
                    wifiData.get(position).setSelected(!dataSelected);
                    wifiData.get(wifiIndex).setSelected(false);
                    wifiIndex = position;
                    wifiAdapter.notifyDataSetChanged();
                    _spfHelper.saveIntData(Constants.SPF_KEY_REFRESH_WIFI, position);
                    try {
                        _mApplication.getmRefreshBinder().transact(0xff, null, null, 0);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }
}
