package cn.zn.com.zn_android.uiclass.activity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.helper.SpfHelper;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.UserInfoBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.AppUtil;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.OtherUtils;
import cn.zn.com.zn_android.utils.StorageUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/4/20 0020.
 * explain:
 */
public class SplashActivity extends AppCompatActivity {

    private RelativeLayout rlRoot;
    private SpfHelper helper;
    private String oldVersion;
    private String currentVersion;
    private String mDownLoadUrl;
    private String fileName;
    private AnimationSet set;
    private SharedPreferences sPreferences;
    private RnApplication _mApplication = RnApplication.getInstance();
//    private String updataUrl = Constants_api.BASE_API_URL + Constants_api.ANDREWS_VER;
//    //微信app_id
//    //IWXAPI是第三方app和微信通信的openapi接口
//    private IWXAPI api;
//
//    //注册到微信
//    private void regToWx() {
//        //通过WXAOIFactory工厂,获取IWXAPI的实例
//        api = WXAPIFactory.createWXAPI(this, APP_ID, true);
//        //将应用的appid注册到微信
//        api.registerApp(APP_ID);
//    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 避免按home键后，再点击launcher会从此页面重新进入应用
        if (!isTaskRoot()) {
            finish();
            return;
        }

        setContentView(R.layout.activity_splash);
        initView();
        initEvent();
        _mApplication.addActivity(this);

//        Log.e(TAG, "onCreate: " + getDeviceInfo(this));//获取设备信息
    }

    @Override
    protected void onDestroy() {
        _mApplication.removeActivity(this);
        super.onDestroy();
    }

    protected void initView() {
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
//        checkVersion();

        // 渐变动画
        AlphaAnimation animAlpha = new AlphaAnimation(0, 1);
        animAlpha.setDuration(2000);// 动画时间
        animAlpha.setFillAfter(true);// 保持动画结束状态

        // 缩放动画
        ScaleAnimation animScale = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animScale.setDuration(1000);
        animScale.setFillAfter(true);// 保持动画结束状态
        // 旋转动画
        RotateAnimation animRotate = new RotateAnimation(0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animRotate.setDuration(1000);// 动画时间
        animRotate.setFillAfter(true);// 保持动画结束状态
        // 动画集合
        set = new AnimationSet(true);
//        set.addAnimation(animRotate);
//        set.addAnimation(animScale);
        set.addAnimation(animAlpha);
        rlRoot.startAnimation(set);
    }

    protected void initEvent() {

        /**
         * 动画执行的监听事件
         */
        set.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //从服务器拿到接口
                getData();
                login();
                String channelStr = OtherUtils.getChannelName(_mApplication);
                Observable.just(channelStr)
                        .subscribeOn(Schedulers.io())
                        .subscribeOn(Schedulers.io())
                        .flatMap(new Func1<String, Observable<Integer>>() {
                            @Override
                            public Observable<Integer> call(String s) {
                                int channel = 0;
                                if (null == channelStr || "".equals(channelStr))
                                    channel = 0;
                                else {
                                    for (int i = 0; i < Constants.channels.length; i++) {
                                        if (channelStr.equals(Constants.channels[i])) {
                                            channel = i;
                                            break;
                                        }
                                    }
                                }
                                return Observable.just(channel);
                            }
                        }).subscribe(value -> {
                    RnApplication.getInstance().setChannel(value);
                });

            }


            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                checkVersion();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("SplashActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("SplashActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    /**
     * 检查版本号,并弹出更新的dialog
     */
    private void checkVersion() {
        oldVersion = AppUtil.getAppVersionName(this);
        if (!StringUtil.isEmpty(currentVersion)) {
            if (oldVersion.compareTo(currentVersion) == -1) {
                if (NetUtil.isWIFIConnected(_mApplication)) {
                    showUpdataDiolog(getString(R.string.update_tips));
                } else if (NetUtil.isMOBILEConnected(_mApplication)) {
                    showUpdataDiolog(getString(R.string.update_tips_no_wifi));
                } else {
                    ToastUtil.showShort(_mApplication, getString(R.string.no_net));
                }

            } else {
                openFormalActivity();
            }
        } else {
            openFormalActivity();
        }
//        showUpdataDiolog(getString(R.string.update_tips));
    }


    /**
     * 更新的dialog
     */
    private void showUpdataDiolog(String content) {
        new JoDialog.Builder(this)
                .setCancelableOut(false)
                .setGravity(Gravity.CENTER)
                .setPositiveTextRes(R.string.confirm)
                .setNegativeTextRes(R.string.cancel)
                .setStrTitle(R.string.tips)
                .setStrContent(content)
                .setCallback(new JoDialog.ButtonCallback() {
                    @Override
                    public void onPositive(JoDialog dialog) {
                        downloadUpdate(mDownLoadUrl);
                        dialog.dismiss();
                        openFormalActivity();
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {
                        dialog.dismiss();
                        openFormalActivity();
                    }
                })
                .show(true);
    }

    /**
     * 进入下一个页面
     */
    private void openFormalActivity() {
        helper = RnApplication.getInstance().getSpfHelper();
        // 动画结束,跳转页面
        // 如果是第一次进入, 跳新手引导
        // 否则跳主页面
        // 返回空或者“0”表示第一次登陆，否则不是
        String isFirstEnter = helper.getData(
                "is_first_enter");
        Intent intent;
        if (isFirstEnter.equals("") ||
                isFirstEnter.equals("0")) {
            // 新手引导
//                     ToastUtil.showShort(SplashActivity.this, "首次登陆");
            intent = new Intent(getApplicationContext(),
                    GuideActivity.class);
//                    helper.saveData("is_first_enter", "1");
        } else {
            // 主页面
            intent = new Intent(getApplicationContext(),
                    MainActivity.class);
        }
        startActivity(intent);
        finish();// 结束当前页面
    }


    /**
     * 调用系统的下载下载文件,使用downloadManager下载
     */
    private void downloadUpdate(String dowloadPath) {
        DownloadManager dManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(dowloadPath);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        // 设置下载路径和文件名
        fileName = StorageUtil.getFileName() + currentVersion + ".apk";
        request.setDestinationInExternalPublicDir("download", fileName);
        request.setDescription("融牛app更新下载");
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setMimeType("application/vnd.android.package-archive");
        // 设置为可被媒体扫描器找到
        request.allowScanningByMediaScanner();
        // 设置为可见和可管理
        request.setVisibleInDownloadsUi(true);
        long refernece = dManager.enqueue(request);
        // 把当前下载的ID保存起来
        sPreferences = getSharedPreferences("downloadcomplete", 0);
        sPreferences.edit().putLong("refernece", refernece).commit();
    }


    /**
     * HttpConnection获取json数据
     */
    private static final String TAG = "SplashActivity";

    private void getData() {
//        new Thread() {
//            private String mStrContent;
//
//            @Override
//            public void run() {
//                try {
//                    StringBuilder resultData = new StringBuilder();
//                    URL url = new URL(httpUrl);
//                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                    conn.setRequestMethod("GET");
//                    //inputStreamReader一个个字节读取转为字符,可以一个个字符读也可以读到一个buffer
//                    //getInputStream是真正去连接网络获取数据
//                    if (conn.getResponseCode() == 200) {
//                        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
//                        //使用缓冲一行行的读入，加速InputStreamReader的速度
//                        BufferedReader buffer = new BufferedReader(isr);
//                        String inputLine;
//                        while ((inputLine = buffer.readLine()) != null) {
//                            resultData.append(inputLine);
//                        }
//                        buffer.close();
//                        isr.close();
//                        conn.disconnect();
//                        mStrContent = resultData.toString();
//                        Gson gson = new Gson();
//                        UpdataVersionBean updataVersionBean = gson.fromJson(mStrContent, UpdataVersionBean.class);
//                        currentVersion = updataVersionBean.getData().getVersion();
//                        mDownLoadUrl = updataVersionBean.getData().getUrl();
//                        _mApplication.setDownLoadUrl(mDownLoadUrl);
//                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();

        ApiManager.getInstance().getService().queryAndroidVer("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(ret -> {
                    if (null != ret) {
                        currentVersion = ret.getData().getVersion();
                        mDownLoadUrl = ret.getData().getUrl();
                        _mApplication.setDownLoadUrl(mDownLoadUrl);

                    }
                }, throwable -> {
                    Log.e(TAG, "getData: ", throwable);
                });

    }

    /**
     * 登录
     */
    private void login() {
        String name = RnApplication.getInstance().getSpfHelper().getData(Constants.SPF_KEY_PHONE);
        String password = RnApplication.getInstance().getSpfHelper().getData(Constants.SPF_KEY_PWD);

        if (password == null && password.equals("")) {
            return;
        }
        if (name != null && !name.equals("") && password != null && !password.equals("")) {
            /* 登录 */
            ApiManager.getInstance().getService().login(name, password, "2")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(returnValue -> {
                        if (returnValue != null) {
                            if (returnValue.getMsg().equals(Constants.SUCCESS)) {
                                // 登录成功，保存数据
                                _mApplication.getUserInfo().setPhone(name); // 手机号
                                _mApplication.getUserInfo().setIsLogin(1);
                                _mApplication.getUserInfo().setIsTeacher(returnValue.getData().getIs_teacher());
                                startActivity(new Intent(_mApplication, MainActivity.class));
                                finish();
                            } else {
                                ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
                            }
                        }
                    }, throwable -> {
                        Log.e(TAG, "sendResInfoResult: 异常",throwable);
                        ToastUtil.show(this, getString(R.string.no_net), Toast.LENGTH_SHORT);
                    });
        }

    }

    private void getUserInfo() {

        ApiManager.getInstance().getService().queryMemberInfo(_mApplication.getUserInfo().getSessionID(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> queryUserInfoResult(returnValue), throwable -> {
                    Log.i(TAG, "queryMemberInfo: 异常");
                });
    }

    public void queryUserInfoResult(ReturnValue<UserInfoBean> returnValue) {
        if (returnValue == null) return;
        if (returnValue.getMsg().equals(Constants.SUCCESS)) { // 查询接口成功
            if (returnValue.getData() != null) {
                _mApplication.getUserInfo().setMemberGrade(returnValue.getData().getGrade());
                _mApplication.getUserInfo().setName(returnValue.getData().getNickname());
                _mApplication.getUserInfo().setSex(returnValue.getData().getSex());
                _mApplication.getUserInfo().setBirthday(returnValue.getData().getBirthday());
                _mApplication.getUserInfo().setProvince(returnValue.getData().getProvince());
                _mApplication.getUserInfo().setCity(returnValue.getData().getCity());
                _mApplication.getUserInfo().setAvatars(returnValue.getData().getAvatars());
                _mApplication.getUserInfo().setSignature(returnValue.getData().getSignature());
                _mApplication.getUserInfo().setWealth(returnValue.getData().getWealth());
                _mApplication.getUserInfo().setIsTeacher(returnValue.getData().getIs_teacher());
                if (!returnValue.getData().getFen().equals("")) {
                    _mApplication.getUserInfo().setFen(returnValue.getData().getFen());
                } else {
                    _mApplication.getUserInfo().setFen("0");
                }
            }
        } else { // 失败
            ToastUtil.showShort(_mApplication, "查询会员信息失败，请稍后重试");
            if (_mApplication.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
            }
        }
    }

//    /**
//     * 友盟获取设备信息
//     */
//    public static boolean checkPermission(Context context, String permission) {
//        boolean result = false;
//        if (Build.VERSION.SDK_INT >= 23) {
//            try {
//                Class<?> clazz = Class.forName("android.content.Context");
//                Method method = clazz.getMethod("checkSelfPermission", String.class);
//                int rest = (Integer) method.invoke(context, permission);
//                if (rest == PackageManager.PERMISSION_GRANTED) {
//                    result = true;
//                } else {
//                    result = false;
//                }
//            } catch (Exception e) {
//                result = false;
//            }
//        } else {
//            PackageManager pm = context.getPackageManager();
//            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
//                result = true;
//            }
//        }
//        return result;
//    }
//
//    public static String getDeviceInfo(Context context) {
//        try {
//            org.json.JSONObject json = new org.json.JSONObject();
//            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
//                    .getSystemService(Context.TELEPHONY_SERVICE);
//            String device_id = null;
//            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
//                device_id = tm.getDeviceId();
//            }
//            String mac = null;
//            FileReader fstream = null;
//            try {
//                fstream = new FileReader("/sys/class/net/wlan0/address");
//            } catch (FileNotFoundException e) {
//                fstream = new FileReader("/sys/class/net/eth0/address");
//            }
//            BufferedReader in = null;
//            if (fstream != null) {
//                try {
//                    in = new BufferedReader(fstream, 1024);
//                    mac = in.readLine();
//                } catch (IOException e) {
//                } finally {
//                    if (fstream != null) {
//                        try {
//                            fstream.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if (in != null) {
//                        try {
//                            in.close();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//            json.put("mac", mac);
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = mac;
//            }
//            if (TextUtils.isEmpty(device_id)) {
//                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
//                        android.provider.Settings.Secure.ANDROID_ID);
//            }
//            json.put("device_id", device_id);
//            return json.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


}
