package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.BillDetailBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 账单详情
 * Created by Jolly on 2016/12/8.
 */

public class BillDetailActivity extends BaseActivity {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_status)
    TextView mTvStatus;
    @Bind(R.id.tv_explain)
    TextView mTvExplain;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_number)
    TextView mTvNumber;
    private String billID;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_bill_detail);
    }

    @Override
    protected void initView() {
        this.billID = getIntent().getStringExtra("billID"); //获取传递过来的billID
        mToolbarTitle.setText(R.string.bill_detail);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initData() {
        queryBillDetail();
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    private void queryBillDetail() {
        _apiManager.getService().queryBillDeatail(_mApplication.getUserInfo().getSessionID(), billID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::setData, throwable -> {
                            Log.e(TAG, "queryBillDetail: " + throwable.getMessage());
                            ToastUtil.show(_Activity, "网络连接失败", Toast.LENGTH_SHORT);
                        }
                );

//        AppObservable.bindActivity(this, _apiManager.getService().queryBillDeatail(_mApplication.getUserInfo().getSessionID(), billID))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::setData, throwable -> {
//                            Log.e(TAG, "queryBillDetail: " + throwable.getMessage());
//                            ToastUtil.show(_Activity, "网络连接失败", Toast.LENGTH_SHORT);
//                        }
//                );
    }

    /**
     * 设置数据
     *
     * @param returnValue
     */
    private void setData(ReturnValue<BillDetailBean> returnValue) {
        if (null == returnValue || returnValue.getData() == null) {
            Log.e(TAG, "setData: " + "服务器返回了NULL");
        } else {
            if (returnValue.getCode().equals("4000")) {
                Log.e(TAG, "setData: " + returnValue.getData().getMessage());
                ToastUtil.show(_Activity, returnValue.getData().getMessage(), Toast.LENGTH_SHORT);
            } else {
                BillDetailBean bean = returnValue.getData();
                mTvPrice.setText(bean.getMoney());
                mTvStatus.setText(bean.getRemark());
                mTvUserName.setText(bean.getNickname());
                mTvExplain.setText(bean.getWay());
                mTvTime.setText(bean.getTimes());
                mTvNumber.setText(bean.getOrder_num());
            }
        }
    }


}
