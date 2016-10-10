package cn.zn.com.zn_android.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.EnvConstants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.PayView;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/28 0028.
 */

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, EnvConstants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {

    }

    /**
     * 得到支付结果回调
     */
    @Override
    public void onResp(BaseResp resp) {
        Log.d(TAG, "onPayFinish, errCode = " + resp.errCode);// 支付结果码
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            Log.d(TAG, "onPayFinish,errCode=" + resp.errCode);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tips);
        }
        String orderid = RnApplication.getInstance().getUserInfo().getOut_trade_no();
        if (resp.errCode == -1) {
//            ToastUtil.showShort(this, "支付出错啦");
            closeWXOrder(orderid);
        } else if (resp.errCode == -2) {
//            ToastUtil.showShort(this, "取消支付");
            closeWXOrder(orderid);
        } else if (resp.errCode == 0) {
            queryOrder(orderid);
        }
    }

    /**
     * 查询订单接口
     * @param orderid
     */
    private void queryOrder(String orderid) {
        AppObservable.bindActivity(this, ApiManager.getInstance().getService().queryWXCheckOrder(orderid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    ToastUtil.showShort(getApplicationContext(), retValue.getData().getMessage());
                    finish();
                }, throwable -> {
                    Log.e(TAG, "queryOrder: ", throwable);
                });
    }

    /**
     * 查询订单接口
     * @param orderid
     */
    private void closeWXOrder(String orderid) {
        AppObservable.bindActivity(this, ApiManager.getInstance().getService().queryWXCloseOrder(orderid))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    ToastUtil.showShort(getApplicationContext(), retValue.getData().getMessage());
                    finish();
                }, throwable -> {
                    Log.e(TAG, "queryOrder: ", throwable);
                });
    }

}
