package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.sqlclass.SQLCopyDB;
import cn.zn.com.zn_android.utils.StorageUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 所在地
 * Created by Jolly on 2016/3/21 0021.
 */
public class LocationActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.lv_location)
    ListView mLvLocation;

    private List<Map<String, String>> addressList = new ArrayList<>();
    private SimpleAdapter locationAdapter;
    private String city = "";
//    private FinishActivitiesBroad finishActivitiesBroad = new FinishActivitiesBroad(this);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_location);

        locationAdapter = new SimpleAdapter(_mApplication, addressList, R.layout.item_location, new String[]{Constants.AREA}, new int[]{R.id.tv_location});
        mLvLocation.setAdapter(locationAdapter);

//        IntentFilter filter = new IntentFilter();
//        filter.addAction(FinishActivitiesBroad.LOCATION);
//        registerReceiver(finishActivitiesBroad, filter);

        addressList.clear();
        if (getIntent() != null && getIntent().getStringExtra(Constants.ID) != null) {
            addressList.addAll(getAddress(getIntent().getStringExtra(Constants.ID)));

        } else {
            addressList.addAll(getAddress("0"));
        }
        locationAdapter.notifyDataSetChanged();
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.person_location));
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mLvLocation.setOnItemClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("LocationActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("LocationActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
//        unregisterReceiver(finishActivitiesBroad);
        super.onDestroy();
    }

    private List<Map<String, String>> getAddress(String pid) {
        List<Map<String, String>> addrList = new ArrayList<>();
        String path = Environment.getExternalStorageDirectory() + "/address.db";
        SQLCopyDB sqlCopyDB = new SQLCopyDB();
        //复制数据库应该放到引导页做
        SQLiteDatabase db = sqlCopyDB.SQLCopyDB(this, StorageUtil.getCachePath(_mApplication), Constants.DB_FILE);
//        SQLiteDatabase db = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = db.rawQuery("select id, area from [address] where pid=?", new String[]{pid});

        while (cursor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.ID, cursor.getString(cursor.getColumnIndex(Constants.ID)));
            map.put(Constants.AREA, cursor.getString(cursor.getColumnIndex(Constants.AREA)));
            addrList.add(map);
        }
        db.close();
        cursor.close();
        return addrList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (getIntent() == null || getIntent().getStringExtra(Constants.ID) == null) {
            if (getAddress(addressList.get(position).get(Constants.ID)).size() == 0) {
                city = addressList.get(position).get(Constants.AREA);
                modifyLocation(addressList.get(position).get(Constants.ID));
            } else {
                Intent intent = new Intent(this, LocationActivity.class);
                intent.putExtra(Constants.ID, addressList.get(position).get(Constants.ID));
                intent.putExtra(Constants.AREA, addressList.get(position).get(Constants.AREA));
                startActivity(intent);
            }
        } else {
            city = addressList.get(position).get(Constants.AREA);
            modifyLocation(addressList.get(position).get(Constants.ID));
        }
    }

    private void modifyLocation(String cityID) {
        String provinceID = getIntent().getStringExtra(Constants.ID);
        AppObservable.bindActivity(this, _apiManager.getService().modifyMemberLocation(_mApplication.getUserInfo().getSessionID(), provinceID, cityID))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::modifyResult, throwable -> {
                    Log.i(TAG, "modifyLocation: 异常");
                    ToastUtil.showShort(this, getString(R.string.modify_fail));
                });
    }

    /**
     * 修改返回结果
     *
     * @param returnValue
     */
    private void modifyResult(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            if (getIntent().getStringExtra(Constants.AREA) != null &&
                    !TextUtils.isEmpty(getIntent().getStringExtra(Constants.AREA))) {
                _mApplication.getUserInfo().setProvince(getIntent().getStringExtra(Constants.AREA));
                _mApplication.getUserInfo().setCity(city);
                ToastUtil.showShort(this, "选择所在地为：" + getIntent().getStringExtra(Constants.AREA) + city);
            } else {
                _mApplication.getUserInfo().setProvince(city);
                _mApplication.getUserInfo().setCity("");
                ToastUtil.showShort(this, "选择所在地为：" + city);
            }
            startActivity(new Intent(_mApplication, PersonActivity.class));
        } else {
            ToastUtil.showShort(this, returnValue.getData().getMessage());
            if (_mApplication.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
            }
        }

    }
}
