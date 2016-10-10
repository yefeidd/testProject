package cn.zn.com.zn_android.uiclass.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ChoosePayAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.EnvConstants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OrderResultBean;
import cn.zn.com.zn_android.model.bean.PaySignBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.PayPresenter;
import cn.zn.com.zn_android.utils.BaseHelper;
import cn.zn.com.zn_android.utils.Md5RSAAlgorithm;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.OrderInfoUtil2_0;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.PayView;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

public class ChoosePayActivity extends BaseMVPActivity<PayView, PayPresenter>
        implements View.OnClickListener, AdapterView.OnItemClickListener, PayView {
    private final int[] imgArray = new int[]{R.mipmap.ic_zfb, R.mipmap.ic_wx, R.mipmap.ic_lianlian, R.mipmap.ic_yl};

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.lv_choose_pay)
    ListView mLvChoosePay;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;

    private List<Map<String, String>> data = new ArrayList<>();
    private ChoosePayAdapter mAdapter;
    private int indexBef = 0;
    private int indexNow = 0;
    private static final int SDK_PAY_FLAG = 1;
    private static final int SDK_AUTH_FLAG = 2;
    private String money = "0";
    private static final String PAY_OK = "9000";// 支付成功
    private static final String PAY_WAIT_CONFIRM = "8000";// 交易待确认
    private static final String PAY_NET_ERR = "6002";// 网络出错
    private static final String PAY_CANCLE = "6001";// 交易取消
    private static final String PAY_FAILED = "4000";// 交易失败

    /**
     * 支付宝支付业务：入参app_id
     */
    public static final String APPID = "2016082501803107";

    /**
     * 支付宝账户登录授权业务：入参pid值
     */
    public static final String PID = "2088421649328106";
    /**
     * 支付宝账户登录授权业务：入参target_id值
     */
    public static final String TARGET_ID = "";

    /**
     * 商户私钥，pkcs8格式
     */
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAMbPrjEQME+1J7PGr6e9Gvp7tGWgmiNA9RSXUVAgim+b5flc4oWbdJa2iVtNe/hx5TaujLRmAyjT9MwU5ivBsQXW9G2ztI6YcU//OXivy79GWdioShnY7QTBj+w4YWbUvmbDZxxjWA01wbY5eTwlMcxt8KRakvABB10cwQJV+aq9AgMBAAECgYEAimmukZP+vo+Vy5DJXFPJA5i6WIK+rUqdFi/fOZTeLKPyMXDceD5ppkABUyKX67mH5lERvsrC6lretHLcaJxaHEcgysPlVhM2/h48o13J/sxfWWXEyJukbpRLBe8YvtO6CUCQQVBaw3YG/FN6SGN+PvoyVh0++2QMtVjfgz8+K0ECQQDtzWaf2XoCHFhWZvOfXp/lMFay2CyYLIrKlI2YWnPRlTpNJMh2opWpNsu6fiULxOs9IdLKjhVQCJcAzyvuwspRAkEA1gZvhGI4X/glxFsAqnA0hjV3IpIrX1MMqI1PARPThyozV8pCXvQpgVGW4IC4qTrc9St7v8KN9BfZ/YHhc7lSrQJAaBHRDLm3TH8tJbrueVuG7F1IRAKZGYu8vcVfZkKHlzwQhLxJQqoKh0BkzlvwSINIU7uerWia6SipNQB1gIsc0QJACrfTydtpW1T//y9XfzGTT81JpA+R4Ho2PhKljDZD95bjb1gPrtWnZnSBi8imdsMd4aFF1zqVBU5Uel+QvMzhGQJAQv6jHDvS36ZXYUXV5AJFjpliSyMuQ8rY3dGQfYnrX2r8Bhi5fifuPu/Aca65iOI23fpytNZ7J7GZrYSrSSpHfQ==";
    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";
    private Map<String, String> params;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public PayPresenter initPresenter() {
        return new PayPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_choose_pay;
    }

    public void onEventMainThread(AnyEventType event) {
        money = (String) event.getObject();
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.pay));
        getData();
        mAdapter = new ChoosePayAdapter(this, data);
        mLvChoosePay.setAdapter(mAdapter);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mLvChoosePay.setOnItemClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ChoosePayActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ChoosePayActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void getData() {
        String[] payArray = getResources().getStringArray(R.array.choose_pay);
        String[] payInfoArray = getResources().getStringArray(R.array.choose_pay_info);
        for (int i = 0; i < payArray.length; i++) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.IMG, imgArray[i] + "");
            map.put(Constants.TITLE, payArray[i]);
            map.put(Constants.INFO, payInfoArray[i]);
            if (i == 0) {
                map.put(Constants.SELECT, Constants.SELECT);
            } else {
                map.put(Constants.SELECT, "unselect");
            }
            data.add(map);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.btn_pay_now:
                switch (indexNow) {
                    case 0:
//                        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis());
                        params = OrderInfoUtil2_0.buildOrderParamMap(APPID, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()), money);
                        String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
                        String sign = OrderInfoUtil2_0.getSign(params, RSA_PRIVATE);
                        final String orderInfo = orderParam + "&" + sign;
//                        final String orderInfo = "app_id=2016082401793533&biz_content=%7B%22timeout_express%22%3A%2260m%22%2C%22seller_id%22%3A%22%22%2C%22product_code%22%3A%22QUICK_MSECURITY_PAY%22%2C%22total_amount%22%3A%22100%22%2C%22subject%22%3A%22abc%22%2C%22body%22%3A%22tt%22%2C%22out_trade_no%22%3A%22a_570c88d00baeb218394549%22%7D&charset=utf-8&format=json&method=alipay.trade.app.pay¬ify_url=http%3A%2F%2Ftt.zhengniu.net%2FApi%2FAlipay%2Fdd&sign_type=RSA×tamp=2016-09-30%2000%3A10%3A41&version=1.0&sign=ZkGghIR%2FUnMYiLO0wAQRSZuHa8%2FTZnTSHJvzFG6a6mpe9l4xdYGTIuKxz4FZ4%2B1A14DHk1EwDaCQNoC4iNU%2F3eWZ0ZZnsp2nrJ8m%2B%2FFoMtJngTT8epLnIYc1VA9bIUlnfRiLnrKpm%2B4tBK1b6%2BAChH4uqNTp0PM7f93%2FJpZ%2Bh%2B4%3D";
                        Runnable payRunnable = new Runnable() {
                            @Override
                            public void run() {
                                PayTask alipay = new PayTask(ChoosePayActivity.this);
                                Map<String, String> result = alipay.payV2(orderInfo, true);
                                Log.i("msp", result.toString());
                                Message msg = new Message();
                                msg.what = SDK_PAY_FLAG;
                                msg.obj = result;
                                alipayHandler.sendMessage(msg);
                            }
                        };
                        // 必须异步调用
                        Thread payThread = new Thread(payRunnable);
                        payThread.start();
//                        startActivity(new Intent(this, PayZFBActivity.class));
                        break;
                    case 1:
                        if (_mApplication.getUserInfo().getSessionID() == null || _mApplication.getUserInfo().getSessionID().equals("")) {
                            startActivity(new Intent(this, LoginActivity.class));
                        } else {
                            presenter.queryWXPaySign(_mApplication.getUserInfo().getSessionID(), money, getString(R.string.wealthy_currency));
                        }
                        break;
                    case 2: // 连连
                        if (NetUtil.checkNet(this)) {
                            presenter.addRechargeOrder(_mApplication.getUserInfo().getSessionID(),
                                    "2", money);
                        } else {
                            ToastUtil.showShort(this, getString(R.string.no_net));
                        }
                        break;
                    case 3:
                        startActivity(new Intent(_mApplication, PayYLActivity.class));
                        break;
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        indexNow = position;

        if (indexBef >= 0) {
            data.get(indexBef).put(Constants.SELECT, "unselect");
        }

        if (!data.get(position).get(Constants.SELECT).equals(Constants.SELECT)) {
            data.get(position).put(Constants.SELECT, Constants.SELECT);
        } else {
            data.get(position).put(Constants.SELECT, "unselect");
        }

        indexBef = position;
        mAdapter.notifyDataSetChanged();
    }

    private Handler mHandler = createHandler();

    private Handler createHandler() {
        return new Handler() {
            public void handleMessage(Message msg) {
                String strRet = (String) msg.obj;
                switch (msg.what) {
                    case Constants.RQF_PAY: {
                        JSONObject objContent = BaseHelper.string2JSON(strRet);
                        String retCode = objContent.optString("ret_code");
                        String retMsg = objContent.optString("ret_msg");

                        // 成功
                        if (Constants.RET_CODE_SUCCESS.equals(retCode)) {
//                            // TODO 卡前置模式返回的银行卡绑定协议号，用来下次支付时使用，此处仅作为示例使用。正式接入时去掉
                            BaseHelper.showDialog(ChoosePayActivity.this, "提示",
                                    "支付成功，交易状态码：" + retCode,
                                    android.R.drawable.ic_dialog_alert);
                        } else if (Constants.RET_CODE_PROCESS.equals(retCode)) {
                            // TODO 处理中，掉单的情形
                            String resulPay = objContent.optString("result_pay");
                            if (Constants.RESULT_PAY_PROCESSING
                                    .equalsIgnoreCase(resulPay)) {
                                BaseHelper.showDialog(ChoosePayActivity.this, "提示",
                                        objContent.optString("ret_msg") + "交易状态码："
                                                + retCode,
                                        android.R.drawable.ic_dialog_alert);
                            }

                        } else {
                            // TODO 失败
                            BaseHelper.showDialog(ChoosePayActivity.this, "提示", retMsg
                                            + "，交易状态码:" + retCode,
                                    android.R.drawable.ic_dialog_alert);
                        }
                    }
                    break;

                }
                super.handleMessage(msg);
            }
        };

    }

    /**
     * 支付宝回调handler
     *
     * @param returnValue
     */
    @SuppressLint("HandlerLeak")
    private Handler alipayHandler = new Handler() {
        @SuppressWarnings("unused")
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SDK_PAY_FLAG:
                    @SuppressWarnings("unchecked")
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                    if (resultStatus.equals(PAY_OK) && disposeResult(resultInfo)) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        Toast.makeText(ChoosePayActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                    } else if (resultStatus.equals(PAY_CANCLE)) {
                        Toast.makeText(ChoosePayActivity.this, "支付取消", Toast.LENGTH_SHORT).show();
                    } else if (resultStatus.equals(PAY_NET_ERR)) {
                        Toast.makeText(ChoosePayActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                    } else if (resultStatus.equals(PAY_WAIT_CONFIRM)) {
                        Toast.makeText(ChoosePayActivity.this, "等待连接", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChoosePayActivity.this, "未知错误", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case SDK_AUTH_FLAG:
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * { when=-80ms what=1
     * obj={memo=,
     * result={"alipay_trade_app_pay_response":
     * {"code":"10000",
     * "msg":"Success",
     * "app_id":"2016082501803107",
     * "auth_app_id":"2016082501803107",
     * "charset":"utf-8",
     * "timestamp":"2016-09-30 10:28:03",
     * "total_amount":"1.00",
     * "trade_no":"2016093021001004220260584337",
     * "seller_id":"2088421649328106",
     * "out_trade_no":"093010254948155"},
     * "sign":"K4Ba2YIhUwnfta+CXQyuEDmqC0ogddigGDXLWu2h72/XKRq52qILpZ9AAnhn4zR+RVQzvRdchM8NNV3//YrHzqTts879N4A7x/rH559YKMOwH1wQcub+ArST07xBVlO6tm+/wV7aUdoWknMVrZSTLFopf0ADlZSdlaYIulqEBlo=",
     * "sign_type":"RSA"},
     * resultStatus=9000}
     * target=cn.zn.com.zn_android.uiclass.activity.ChoosePayActivity$3 }
     */


    private boolean disposeResult(String result) {
        try {
            JSONObject jsonObject = new JSONObject(result);
            String resource = jsonObject.getString("alipay_trade_app_pay_response");
            String sign = jsonObject.getString("sign");
//            if (RSASignature.doCheck(resource, sign, RSA_PUBLIC)) {
            JSONObject paremObject = jsonObject.getJSONObject("alipay_trade_app_pay_response");
            String appId = paremObject.getString("app_id");
            String totalAmount = paremObject.getString("total_amount");
            String Seller = paremObject.getString("seller_id");
            String value2 = paremObject.getString("trade_no");
            String[] test = totalAmount.split("\\.");
            String checkMoney = test[0];
            if (appId.equals(APPID) && checkMoney.equals(money) && Seller.equals(PID)) {
                presenter.addDataToServer(money, value2);
                return true;
            }
            return false;
        } catch (Exception e) {
            return false;
        }


//
//        for (String key : result.keySet()) {
//            if (TextUtils.equals(key, "resultStatus")) {
//                resultStatus = result.get(key);
//            } else if (TextUtils.equals(key, "result")) {
//                result = result.get(key);
//            } else if (TextUtils.equals(key, "memo")) {
//                memo = rawResult.get(key);
//            }
//        }
    }

    private void resultRechargeOrder(ReturnValue<OrderResultBean> returnValue) {
        Log.i(TAG, "resultRechargeOrder: ");
        if (returnValue != null && returnValue.getData() != null && returnValue.getMsg().equals(Constants.ERROR)) {
            ToastUtil.showShort(this, returnValue.getData().getMessage());
        } else {
            presenter.lianlianPay(returnValue.getData(), mHandler, money);
        }
    }

    @Override
    public void onSuccess(int flag, Object object) {
        switch (flag) {
            case PayView.LIAN_LIAN:
                ReturnValue<OrderResultBean> llRet = (ReturnValue<OrderResultBean>) object;
                resultRechargeOrder(llRet);
                break;
            case PayView.WEI_XIN:
                PaySignBean paySignBean = (PaySignBean) object;
                IWXAPI mWxApi = _mApplication.getMsgApi();
                if (mWxApi != null) {
                    PayReq req = new PayReq();
                    req.appId = EnvConstants.APP_ID;// 微信开放平台审核通过的应用APPID
                    req.partnerId = paySignBean.getParnerid();// 微信支付分配的商户号
                    req.prepayId = paySignBean.getPrepay_id();// 预支付订单号，app服务器调用“统一下单”接口获取
                    req.nonceStr = paySignBean.getNonce_str();// 随机字符串，不长于32位，服务器小哥会给咱生成
//                    req.timeStamp = "" + Long.valueOf(System.currentTimeMillis() / 1000);
                    req.timeStamp = paySignBean.getTimestamp();// 时间戳，app服务器小哥给出
                    req.packageValue = "Sign=WXPay";// 固定值Sign=WXPay，可以直接写死，服务器返回的也是这个固定值
//                    req.sign = paySignBean.getSign(); // 签名
                    _mApplication.getUserInfo().setOut_trade_no(paySignBean.getOut_trade_no());

                    String content = "appid=" + EnvConstants.APP_ID + "&noncestr=" + paySignBean.getNonce_str() + "&package=Sign=WXPay" + "&partnerid=" + paySignBean.getParnerid() +
                            "&prepayid=" + paySignBean.getPrepay_id() + "&timestamp=" + req.timeStamp + "&key=0b4c09247ec02edce69f6a2d20160928";

//                    Log.d("content", content);
                    // TODO MD5 签名方式, 签名方式包括两种，一种是MD5，一种是RSA 这个在商户站管理里有对验签方式和签名Key的配置。
                    String sign = Md5RSAAlgorithm.getMessageDigest(content).toUpperCase();
                    req.sign = sign;// 签名，服务器小哥给出，他会根据：https://pay.weixin.qq.com/wiki/doc/api/app/app.php?chapter=4_3指导得到这个
//                    Log.d("content", sign);

                    mWxApi.sendReq(req);
                    Log.d(TAG, "发起微信支付申请");
                }
                break;
        }
    }

    @Override
    public void onError(int flag, Throwable t) {
        Log.e(TAG, "onError: ", t);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
