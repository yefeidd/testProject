package cn.zn.com.zn_android.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.utils.NetUtil;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/7/28 0028.
 */
public class RefreshDataService extends Service {
    public static final String TAG = "RefreshDataService";
    private Subscription timerSubscription;    // 计时
    private LocalBroadcastManager lbm;

    private int dtRefreshRate, wifiRefreshRate;

    public class MyBinder extends Binder {
        public RefreshDataService getService() {
            return RefreshDataService.this;
        }

        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply, int flags) throws RemoteException {
//            handleTransactions(code, data, reply, flags);
            dtRefreshRate = RnApplication.getInstance().getSpfHelper().
                    getIntData(Constants.SPF_KEY_REFRESH_DT) * 5;
            wifiRefreshRate = RnApplication.getInstance().getSpfHelper().
                    getIntData(Constants.SPF_KEY_REFRESH_WIFI) * 5;
            stratRefresh();
            return super.onTransact(code, data, reply, flags);
        }
    }

    private void stratRefresh() {

        if (wifiRefreshRate > 0 && NetUtil.isWIFIConnected(getApplicationContext())) {
            startCountDown();
        } else if (dtRefreshRate > 0 && NetUtil.isMOBILEConnected(getApplicationContext())) {
            startCountDown();
        } else {
            if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
                timerSubscription.unsubscribe();
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (null != intent) {
            stratRefresh();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        lbm = LocalBroadcastManager.getInstance(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        IBinder result = null;
        if (null == result) {
            result = new MyBinder();
        }
//        throw new UnsupportedOperationException("not yet implemented");
        return result;
    }

    /**
     * 打开计时器
     */
    private void startCountDown() {
        timerSubscription = Observable.timer(1, 1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
//                .toBlocking()
                .subscribe(aLong -> {
//                    Log.d(TAG, "startCountDown: " + aL
// ong);
                    deelCountResult(aLong);
                }, throwable -> {
                    Log.e(TAG, "倒计时出错！\n" + throwable);
                    stopCountDown();
                    startCountDown();
                });
    }

    /**
     * 关闭计时器
     */
    private void stopCountDown() {

    }

    /**
     * 处理计时结果
     * @param aLong
     */
    private void deelCountResult(Long aLong) {
        String str;
        Intent i = new Intent(TAG);
        if (!NetUtil.checkNet(getApplicationContext())) {
            return;
        }

        if (NetUtil.isWIFIConnected(getApplicationContext())) {
            str = "当前网络为WIFI";
            if (aLong % wifiRefreshRate == 0) {
                i.putExtra("mark", str);
                lbm.sendBroadcast(i);
                Log.d(TAG, "deelCountResult: " + str + "   " + aLong);
            }
        } else if (NetUtil.isMOBILEConnected(getApplicationContext())){
            str = "当前网络为 数据流量";
            if (aLong % dtRefreshRate == 0) {
                i.putExtra("mark", str);
                lbm.sendBroadcast(i);
                Log.d(TAG, "deelCountResult: " + str);
            }
        }

        if (aLong >= 60) {
            timerSubscription.unsubscribe();
            stopCountDown();
            startCountDown();
            return;
        }
    }

}
