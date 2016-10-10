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

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.helper.SpfHelper;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.UpdataVersionBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.AppUtil;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.StorageUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Header;
import retrofit.client.Response;

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
    private String updataUrl = Constants_api.BASE_API_URL + Constants_api.ANDREWS_VER;
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

        login();
    }

    @Override
    protected void onDestroy() {
        _mApplication.removeActivity(this);
        super.onDestroy();
    }

    protected void initView() {
        rlRoot = (RelativeLayout) findViewById(R.id.rl_root);
//        checkVersion();
        //从服务器拿到接口
        getData(updataUrl);
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

    private void getData(String httpUrl) {
        new Thread() {
            private String mStrContent;

            @Override
            public void run() {
                try {
                    StringBuilder resultData = new StringBuilder();
                    URL url = new URL(httpUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    //inputStreamReader一个个字节读取转为字符,可以一个个字符读也可以读到一个buffer
                    //getInputStream是真正去连接网络获取数据
                    if (conn.getResponseCode() == 200) {
                        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                        //使用缓冲一行行的读入，加速InputStreamReader的速度
                        BufferedReader buffer = new BufferedReader(isr);
                        String inputLine;
                        while ((inputLine = buffer.readLine()) != null) {
                            resultData.append(inputLine);
                        }
                        buffer.close();
                        isr.close();
                        conn.disconnect();
                        mStrContent = resultData.toString();
                        Gson gson = new Gson();
                        UpdataVersionBean updataVersionBean = gson.fromJson(mStrContent, UpdataVersionBean.class);
                        currentVersion = updataVersionBean.getData().getVersion();
                        mDownLoadUrl = updataVersionBean.getData().getUrl();
                        _mApplication.setDownLoadUrl(mDownLoadUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
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

        Callback<ReturnValue<MessageBean>> callback = new Callback<ReturnValue<MessageBean>>() {
            @Override
            public void success(ReturnValue<MessageBean> returnValue, Response response) {
                List<Header> headerList = response.getHeaders();
                for (Header header : headerList) {
                    Log.d(TAG, header.getName() + " " + header.getValue());
                    if (header.getName().equals(Constants.SET_COOKIE)) {
                        String phpSessId = header.getValue().split(";")[0];
                        _mApplication.getUserInfo().setSessionID(phpSessId);
                        break;
                    }
                }
                if (returnValue.getMsg().equals(Constants.SUCCESS)) {
                    // 登录成功，保存数据
                    Log.d(TAG, "success: 登录成功");
//                    _spfHelper.saveData(Constants.SPF_KEY_PHONE, mEtUsername.getText().toString().trim());
                    _mApplication.getUserInfo().setPhone(name); // 手机号
                    _mApplication.getUserInfo().setIsLogin(1);
                    _mApplication.getUserInfo().setPassword(password);

//                    _spfHelper.saveData(Constants.SPF_KEY_PWD, mEtPassword.getText().toString().trim());
////                    startActivity(new Intent(_mApplication, MainActivity.class));
//                    finish();
                } else {
                    ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, "failure: " + error);
                NetUtil.errorTip(error.getKind());

            }
        };

        if (name != null && !name.equals("") && password != null && !password.equals("")) {
            Log.d(TAG, "login: " + name + "\n" + password);
            ApiManager.getInstance().getService().login(name, password, "2", callback);
        }
    }

}
