package cn.zn.com.zn_android.uiclass.listener;

/**
 * Created by zjs on 2016/12/23 0023.
 * email: m15267280642@163.com
 * explain: 倒计时监听
 */

public interface CountDownListener {
    void onStart(Object obj);

    void onFail(Throwable t);
}
