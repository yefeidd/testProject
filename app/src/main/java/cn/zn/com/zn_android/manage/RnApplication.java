package cn.zn.com.zn_android.manage;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.helper.FrescoHelper;
import cn.zn.com.zn_android.helper.SpfHelper;
import cn.zn.com.zn_android.model.bean.RnBean;
import cn.zn.com.zn_android.service.RefreshDataService;
import cn.zn.com.zn_android.uiclass.activity.SplashActivity;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.StorageUtil;
import cn.zn.com.zn_android.utils.ToastUtil;

/**
 * Created by WJL on 2016/3/10 0010 09:41.
 */
public class RnApplication extends Application implements Thread.UncaughtExceptionHandler,
        ServiceConnection {
    public static final String TAG = RnApplication.class.getSimpleName();
    ArrayList<Activity> list = new ArrayList<Activity>();
    private RnBean userInfo;
    private String downLoadUrl;
    private SpfHelper spfHelper, spfHelperConfig;
    private static RnApplication instance;
    public static boolean isShowLog = true;



    private int channel;
    // 获取到主线程的handler
    private static Handler mMainThreadHandler = null;
    // 获取到主线程
    private static Thread mMainThread = null;
    // 获取到主线程的id
    private static int mMainThreadId;
    // 获取到主线程的looper
    private static Looper mMainThreadLooper = null;
    private Thread.UncaughtExceptionHandler mExceptionHandler;
    private IBinder mRefreshBinder;
    //微信支付
    final IWXAPI msgApi = WXAPIFactory.createWXAPI(this, null);
    public static final String wx_APP_ID = "wx0adb85454b5306af";
    public static final String wx_App_Secret = "7129b2d357f6e79f6270b78c2130517c";


    //imageloader初始化
    public static DisplayImageOptions mNormalImageOptions;
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().toString();
    public static final String IMAGES_FOLDER = SDCARD_PATH + File.separator + "证牛" + File.separator + "images" + File.separator;

    public HashMap<String, Object> getBmMap() {
        return bmMap;
    }

    private HashMap<String, Object> bmMap = new HashMap<>();

    public synchronized static RnApplication getInstance() {
        return instance;
    }

    /**
     * 微信注册对象
     *
     * @return
     */
    public synchronized IWXAPI getMsgApi() {
        return msgApi;
    }

    @Override
    public void onCreate() {
        MultiDex.install(this); // 解决友盟分析问题
        super.onCreate();
        /**
         * 自定义捕捉错误
         */
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());

        Thread.setDefaultUncaughtExceptionHandler(this);
        //获取系统默认的UncaughtException处理器
        mExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        instance = this;
        userInfo = new RnBean();
        spfHelper = new SpfHelper(this, Constants.SPF_NAME_APP);
        spfHelperConfig = new SpfHelper(this, Constants.SPF_NAME_APP_CONFIG);
        this.mMainThreadHandler = new Handler();
        this.mMainThread = Thread.currentThread();
        this.mMainThreadId = android.os.Process.myTid();
        this.mMainThreadLooper = getMainLooper();

        // 初始化 用户基础
        initUserInfo();

        initAppDirs();


        if (!NetUtil.checkNet(this)) {
            ToastUtil.showShort(this, R.string.no_net);
//            getSpfHelper().saveData(Constants.LOCATIONEVENT, "");
        }

        //初始化Fresco
        Fresco.initialize(this, FrescoHelper.getImagePipelineConfig(this));


        /**
         * 社会化组件的appid等
         */
        //微信 appid appsecret
        PlatformConfig.setWeixin(wx_APP_ID, wx_App_Secret);
        //新浪微博 appkey appsecret
        PlatformConfig.setSinaWeibo("2323659575", "0fcd4ae08b6b1cf33e3231a27cdd60ed");
        // QQ和Qzone appid appkey
        PlatformConfig.setQQZone("1105702354", "Pg12kYzEO3bGbNfP");

        initRefreshService(); // 初始化行情刷新频率

        /**
         * 注册到微信(支付)
         */
        msgApi.registerApp(wx_APP_ID);
    }

    /**
     * 初始化用户信息
     */
    private void initUserInfo() {
        if (spfHelper.getData(Constants.SPF_KEY_IS_LOGIN).equals("1")) {
            userInfo.setIsLogin(1);
        }
        userInfo.setPhone(spfHelper.getData(Constants.SPF_KEY_PHONE));
        userInfo.setName(spfHelper.getData(Constants.SPF_KEY_NAME, "未登录"));

        Log.i(TAG, userInfo.toString());
    }

    /**
     * 初始化app目录
     */
    private void initAppDirs() {
        String strApp = StorageUtil.getSDCardPath() + Constants.FILE_APP + Constants.FILE_APP_IMG;
        File fileApp = new File(strApp);
        if (!fileApp.exists()) {
            fileApp.mkdirs();
        }
        initImageLoader(this);

    }

    /**
     * Spf配置文件对象 app相关
     */
    public SpfHelper getSpfHelper() {
        if (spfHelper == null) {
            synchronized (SpfHelper.class) {
                if (spfHelper == null) {
                    spfHelper = new SpfHelper(this, Constants.SPF_NAME_APP);
                }
            }
        }
        return spfHelper;
    }

    /**
     * Spf配置文件对象 app配置文件相关
     */
    public SpfHelper getSetupSpfHelper() {
        if (spfHelperConfig == null) {
            synchronized (SpfHelper.class) {
                if (spfHelperConfig == null) {
                    spfHelperConfig = new SpfHelper(this, Constants.SPF_NAME_APP_CONFIG);
                }
            }
        }
        return spfHelperConfig;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public RnBean getUserInfo() {
        if (userInfo == null) {
            initUserInfo();
        }
        return userInfo;
    }

    public void clearUserInfo() {
        if (userInfo != null) {
            userInfo.setIsLogin(Constants.NOT_LOGIN);
            userInfo.setName("");
            userInfo.setPhone("");
            userInfo.setPassword("");
        }

    }

    /**
     * 获取下载链接
     *
     * @return
     */
    public String getDownLoadUrl() {
        return downLoadUrl;
    }

    public void setDownLoadUrl(String downLoadUrl) {
        this.downLoadUrl = downLoadUrl;
    }

    /**
     * Activity关闭时，删除Activity列表中的Activity对象
     */
    public void removeActivity(Activity a) {
        list.remove(a);
    }

    /**
     * 向Activity列表中添加Activity对象
     */
    public void addActivity(Activity a) {
        list.add(a);
    }

    /**
     * 关闭Activity列表中的所有Activity
     */
    public void finishActivity() {
        for (Activity activity : list) {
            if (null != activity) {
                activity.finish();
            }
        }
        //杀死该应用进程
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    // 对外暴露主线程的handler
    public static Handler getMainThreadHandler() {
        return mMainThreadHandler;
    }

    // 对外暴露主线程
    public static Thread getMainThread() {
        return mMainThread;
    }

    // 对外暴露主线程id
    public static int getMainThreadId() {
        return mMainThreadId;
    }

    // 对外暴露主线程的looper
    public static Looper getMainThreadLooper() {
        return mMainThreadLooper;
    }

    /**
     * 捕捉错误并处理
     *
     * @param thread
     * @param ex
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mExceptionHandler != null) {
            mExceptionHandler.uncaughtException(thread, ex);
        } else {
            try {
                Thread.sleep(3000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            ((AlarmManager) getSystemService(Context.ALARM_SERVICE)).set(AlarmManager.ELAPSED_REALTIME_WAKEUP, 100,
                    PendingIntent.getActivity(this, 0, new Intent(this, SplashActivity.class),
                            PendingIntent.FLAG_ONE_SHOT));
            Log.e(TAG, "uncaughtException: ", ex);
            //退出程序
            finishActivity();
//            android.os.Process.killProcess(android.os.Process.myPid());
//            System.exit(0);
        }

    }


    /**
     * 异常处理
     *
     * @param throwable
     * @return
     */
    private boolean handleException(Throwable throwable) {
        if (throwable == null) {
            return false;
        }
        new Thread(new Runnable() {

            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(getInstance(), "很抱歉,程序出现异常,即将重启.", Toast.LENGTH_SHORT).show();
                Looper.loop();
            }
        }) {
        }.start();
        return true;
    }

    private void initRefreshService() {
        Intent serviceIntent = new Intent(this, RefreshDataService.class);
        bindService(serviceIntent, this, Context.BIND_AUTO_CREATE);
    }

    public IBinder getmRefreshBinder() {
        return mRefreshBinder;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        mRefreshBinder = service;
        try {
            mRefreshBinder.transact(0xff, null, null, 0);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }

    /**
     * 初始化imageloader
     */

    private void initImageLoader(Context context) {
        int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        MemoryCacheAware<String, Bitmap> memoryCache;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            memoryCache = new LruMemoryCache(memoryCacheSize);
        } else {
            memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
        }

        mNormalImageOptions = new DisplayImageOptions.Builder().bitmapConfig(Bitmap.Config.RGB_565).cacheInMemory(true).cacheOnDisc(true)
                .resetViewBeforeLoading(true).build();

        // This
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context).defaultDisplayImageOptions(mNormalImageOptions)
                .denyCacheImageMultipleSizesInMemory().discCache(new UnlimitedDiscCache(new File(IMAGES_FOLDER)))
                // .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .memoryCache(memoryCache)
                // .memoryCacheSize(memoryCacheSize)
                .tasksProcessingOrder(QueueProcessingType.LIFO).threadPriority(Thread.NORM_PRIORITY - 2).threadPoolSize(3).build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }



    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
