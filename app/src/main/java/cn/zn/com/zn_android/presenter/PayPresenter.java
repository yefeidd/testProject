package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.manage.EnvConstants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.OrderResultBean;
import cn.zn.com.zn_android.model.bean.PayOrder;
import cn.zn.com.zn_android.uiclass.activity.ChoosePayActivity;
import cn.zn.com.zn_android.utils.BaseHelper;
import cn.zn.com.zn_android.utils.Md5RSAAlgorithm;
import cn.zn.com.zn_android.utils.MobileSecurePayer;
import cn.zn.com.zn_android.viewfeatures.PayView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/27 0027.
 * email: m15267280642@163.com
 * explain:支付相关的presenter类
 */

public class PayPresenter extends BasePresenter<PayView> {
    private Activity mActivity;
    private PayView mPayView;
    private ApiManager _apiManager;

    public PayPresenter(Activity activity, PayView payView) {
        this.mPayView = payView;
        this.mActivity = activity;
        this._apiManager = ApiManager.getInstance();
    }

    /**
     * 生成充值订单
     *
     * @param sessionId
     * @param type      (1:微信2:连连)
     * @param money
     */
    public void addRechargeOrder(String sessionId, String type, String money) {
        _apiManager.getService().addRechargeOrder(sessionId, type, money, "a")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mPayView.onSuccess(PayView.LIAN_LIAN, retValue);
                }, throwable -> {
                    Log.e(TAG, "addRechargeOrder: ", throwable);
                    mPayView.onError(PayView.LIAN_LIAN, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().addRechargeOrder(sessionId, type, money, "a"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mPayView.onSuccess(PayView.LIAN_LIAN, retValue);
//                }, throwable -> {
//                    Log.e(TAG, "addRechargeOrder: ", throwable);
//                    mPayView.onError(PayView.LIAN_LIAN, throwable);
//                });
    }


    public void lianlianPay(OrderResultBean orderResultBean, Handler mHandler, String money) {

        PayOrder order = constructGesturePayOrder(orderResultBean, money);
        Log.i(TAG, "lianlianPay: " + order.getNo_order() + "///" + order.getUser_id());

        String content4Pay = BaseHelper.toJSONString(order);

        // 关键 content4Pay 用于提交到支付SDK的订单支付串，如遇到签名错误的情况，请将该信息帖给我们的技术支持
        Log.i(ChoosePayActivity.class.getSimpleName(), content4Pay);
        MobileSecurePayer msp = new MobileSecurePayer();
        boolean bRet = msp.pay(content4Pay, mHandler,
                Constants.RQF_PAY, mActivity, false);
        Log.i(ChoosePayActivity.class.getSimpleName(), String.valueOf(bRet));
    }

    private PayOrder constructGesturePayOrder(OrderResultBean orderResultBean, String money) {
        SimpleDateFormat dataFormat = new SimpleDateFormat(
                "yyyyMMddHHmmss");
        Date date = new Date();
        String timeString = dataFormat.format(date);

        PayOrder order = new PayOrder();
        order.setBusi_partner(Constants.LL_BUSI_PARTNER);
        order.setNo_order(orderResultBean.getOrder_num());
        order.setDt_order(timeString);
        order.setName_goods(Constants.LL_RECHARGE_NAME);
//        order.setNotify_url("http://beta.www.youtx.com/youtxpay/LianlianPay/YTPay_Notify.aspx");
        order.setNotify_url(Constants_api.LL_NOTIFY_URL);
        // TODO MD5 签名方式
        order.setSign_type(PayOrder.SIGN_TYPE_MD5);
        order.setValid_order("100");

        order.setFlag_modify("0");
        order.setUser_id(orderResultBean.getUser_id());
        order.setId_no("");
        order.setAcct_name("");
        order.setMoney_order(money);

        // 风险控制参数  暂时不传
        order.setRisk_item(constructRiskItem(orderResultBean));

        String sign = "";
        order.setOid_partner(EnvConstants.PARTNER);
        String content = BaseHelper.sortParam(order);
        // TODO MD5 签名方式, 签名方式包括两种，一种是MD5，一种是RSA 这个在商户站管理里有对验签方式和签名Key的配置。
        sign = Md5RSAAlgorithm.getInstance().sign(content, EnvConstants.MD5_KEY);
        order.setSign(sign);
        return order;
    }

    private String constructRiskItem(OrderResultBean orderResultBean) {
        JSONObject mRiskItem = new JSONObject();
        try {
//            mRiskItem.put("user_info_bind_phone", orderResultBean.getMobile()); // 账号绑定的手机号
            mRiskItem.put("user_info_dt_register", orderResultBean.getRegtime()); // 用户在我们平台的注册时间
            mRiskItem.put("frms_ware_category", "1002"); // 这个传 1002
//            mRiskItem.put("request_imei", "1122111221"); // 这个不用传
            mRiskItem.put("user_info_mercht_userno", orderResultBean.getUser_id()); // 用户的id

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return mRiskItem.toString();
    }

    public void queryWXPaySign(String sessionId, String money, String name) {
        _apiManager.getService().queryWXPaySign(sessionId, money, name)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mPayView.onSuccess(PayView.WEI_XIN, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryWXPaySign: ", throwable);
                    mPayView.onError(PayView.WEI_XIN, throwable);
                });

//        AppObservable.bindActivity(mActivity, _apiManager.getService().queryWXPaySign(sessionId, money, name))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mPayView.onSuccess(PayView.WEI_XIN, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryWXPaySign: ", throwable);
//                    mPayView.onError(PayView.WEI_XIN, throwable);
//                }); // java.net.UnknownHostException: Unable to resolve host "tt.zhengniu.net": No address associated with hostname
    }


    public void addDataToServer(String value1, String value2) {
        _apiManager.getService().addDateTo(RnApplication.getInstance().getUserInfo().getSessionID(), value1, value2)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        Log.i(TAG, "addDataToServer: " + retValue.getMsg() + "::" + retValue.getData().getMessage());
                    }
                    Log.i(TAG, "addDataToServer: " + "success");
                }, throwable -> {
                    Log.e(TAG, "addDataToServer: ", throwable);
                }); // java.net.

//        AppObservable.bindActivity(mActivity, _apiManager.getService().addDateTo(RnApplication.getInstance().getUserInfo().getSessionID(), value1, value2))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        Log.i(TAG, "addDataToServer: " + retValue.getMsg() + "::" + retValue.getData().getMessage());
//                    }
//                    Log.i(TAG, "addDataToServer: " + "success");
//                }, throwable -> {
//                    Log.e(TAG, "addDataToServer: ", throwable);
//                }); // java.net.
    }
}
