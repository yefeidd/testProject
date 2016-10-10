package cn.zn.com.zn_android.uiclass.activity;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.UpdataVersionBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.AppUtil;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.StorageUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import butterknife.Bind;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 设置
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener, JoDialog.ButtonCallback {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_exit)
    TextView mTvExit;

    private String oldVersion;
    private String currentVersion;
    private String fileName;
    private String mDownLoadUrl;
    private SharedPreferences sPreferences;
    private String updataUrl = Constants_api.BASE_API_URL + Constants_api.ANDREWS_VER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_setting);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.setting));
        if (_mApplication.getUserInfo().getIsLogin() != Constants.IS_LOGIN) {
            mTvExit.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initEvent() {

        mIvLeftmenu.setOnClickListener(this);
        mTvExit.setOnClickListener(this);
//        int dd = 10 / 0;
    }

    public void onResume() {
        super.onResume();
        //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onPageStart("SettingActivity");
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,
        // 因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPageEnd("SettingActivity");
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.btn_about_us:
                startActivity(new Intent(_mApplication, AboutUsActivity.class));
                break;
            case R.id.btn_feedback:
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mApplication, FeedBackActivity.class));
                } else {
                    startActivity(new Intent(_mApplication, LoginActivity.class));
                }
                break;
            case R.id.btn_update:
                getData(updataUrl);
                break;
            case R.id.btn_clear:
//                if (_mApplication.getUserInfo().getIsLogin() == 1) {
//                    startCleanDialog();
//                } else {
//                    startActivity(new Intent(_mApplication, LoginActivity.class));
//                }
                startCleanDialog();
                break;
            case R.id.btn_refresh_rate:
                startActivity(new Intent(_mApplication, RefreshRateActivity.class));
                break;
            case R.id.btn_push:
                startActivity(new Intent(_mApplication, PushActivity.class));
                break;
            case R.id.btn_favourable_comment:
                // 给个好评
                Intent i = AppUtil.getIntent(this);
                if(!AppUtil.judge(this, i)) {
                    startActivity(i);
                }
                break;
            case R.id.tv_exit:
                postExitLogin();
                break;
        }
    }

    private void startCleanDialog() {
        new JoDialog.Builder(this)
                .setGravity(Gravity.CENTER)
                .setPositiveTextRes(R.string.confirm)
                .setNegativeTextRes(R.string.cancel)
                .setStrTitle(R.string.tips)
                .setStrContent(getString(R.string.about_us_clean))
                .setCallback(this)
                .show(true);
    }

    @Override
    public void onPositive(JoDialog dialog) {
        dialog.dismiss();
        StorageUtil.delete(new File(StorageUtil.getCachePath(_mApplication)), false); // 清除Cache
        StorageUtil.delete(new File(StorageUtil.getSDCardPath() + Constants.FILE_APP), false); // 清除 rn文件夹
    }

    @Override
    public void onNegtive(JoDialog dialog) {
        dialog.dismiss();
    }


    /**
     * 退出登陆
     */
    private void postExitLogin() {
        AppObservable.bindActivity(this, _apiManager.getService().postExitLogin(_mApplication.getUserInfo().getSessionID(), "")).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultExitLogin, throwable -> {
                    Log.e(TAG, "getQNToken: 异常");
                });
    }

    private void resultExitLogin(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
//            ToastUtil.showShort(_mApplication, returnValue.getMsg());
            _mApplication.clearUserInfo();
            _spfHelper.saveData(Constants.SPF_KEY_PWD, "");
            finish();
        } else {
            ToastUtil.showShort(_mApplication, returnValue.getData().getMessage());
        }
    }

    /**
     * 检查版本号,并弹出更新的dialog
     */
    private void checkVersion() {
        oldVersion = AppUtil.getAppVersionName(this);
        if (!StringUtil.isEmpty(currentVersion)) {
            if (oldVersion.compareTo(currentVersion) < 0) {
                if (NetUtil.isWIFIConnected(_mApplication)) {
                    showUpdataDiolog(getString(R.string.update_version)+oldVersion+getString(R.string.update_tips));
                } else if (NetUtil.isMOBILEConnected(_mApplication)) {
                    showUpdataDiolog(getString(R.string.update_tips_no_wifi));
                } else {
                    ToastUtil.showShort(_mApplication, getString(R.string.no_net));
                }

            } else {
                showUpdataDiolog(getString(R.string.update_recent)+oldVersion);
            }
        } else {
            showUpdataDiolog(getString(R.string.update_recent)+oldVersion);
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
                        if (!content.equals(getString(R.string.update_recent)+oldVersion)) {
                            downloadUpdate(mDownLoadUrl);
                        }
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show(true);
    }

    /**
     * 调用系统的下载下载文件,使用downloadManager下载
     */
    private void downloadUpdate(String dowloadPath) {
        DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
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
    private void getData(String httpUrl) {
        new Thread() {
            private String mStrContent;

            @Override
            public void run() {
                StringBuilder resultData = new StringBuilder();
                mStrContent = null;
                try {
                    URL url = new URL(httpUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    //inputStreamReader一个个字节读取转为字符,可以一个个字符读也可以读到一个buffer
                    //getInputStream是真正去连接网络获取数据
                    if (conn.getResponseCode() == 200) {
                        InputStreamReader isr = new InputStreamReader(conn.getInputStream());
                        //使用缓冲一行行的读入，加速InputStreamReader的速度
                        BufferedReader buffer = new BufferedReader(isr);
                        String inputLine = null;
                        while ((inputLine = buffer.readLine()) != null) {
                            resultData.append(inputLine);
                        }
                        buffer.close();
                        isr.close();
                        conn.disconnect();
                        mStrContent = resultData.toString();
                        UIUtil.post(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                UpdataVersionBean updataVersionBean = gson.fromJson(mStrContent, UpdataVersionBean.class);
                                currentVersion = updataVersionBean.getData().getVersion();
                                mDownLoadUrl = updataVersionBean.getData().getUrl();
                            }
                        });

                        handler.sendMessage(new Message());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            checkVersion();
            return false;
        }
    });
}
