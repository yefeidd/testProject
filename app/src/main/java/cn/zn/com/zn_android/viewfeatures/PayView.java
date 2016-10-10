package cn.zn.com.zn_android.viewfeatures;

/**
 * Created by Jolly on 2016/9/28 0028.
 */

public interface PayView extends BaseView {
    public static int LIAN_LIAN = 0x01;
    public static int WEI_XIN = 0x02;

    void onSuccess(int flag, Object object);

    void onError(int flag, Throwable t);
}
