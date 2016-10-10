package cn.zn.com.zn_android.receive;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import cn.zn.com.zn_android.utils.AppUtil;

/**
 * Created by zjs on 2016/4/21 0021.
 * explain:
 */
public class UpdataBroadcastReceiver extends BroadcastReceiver {

    //广播接受者,发现下载完成后直接安装应用
    @SuppressLint("NewApi")
    public void onReceive(Context context, Intent intent) {
        long myDwonloadID = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
        SharedPreferences sPreferences = context.getSharedPreferences("downloadcomplete", 0);
        long refernece = sPreferences.getLong("refernece", 0);
        if (refernece == myDwonloadID) {
            String serviceString = Context.DOWNLOAD_SERVICE;
            DownloadManager dManager = (DownloadManager) context.getSystemService(serviceString);
            Uri downloadFileUri = dManager.getUriForDownloadedFile(myDwonloadID);
            AppUtil.install(context,downloadFileUri);
        }
    }

}
