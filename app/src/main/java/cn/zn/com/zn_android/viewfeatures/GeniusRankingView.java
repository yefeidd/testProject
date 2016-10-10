package cn.zn.com.zn_android.viewfeatures;


/**
 * Created by Jolly on 2016/9/28 0028.
 */

public interface GeniusRankingView extends BaseView {

    void onSuccess(int flag, Object object);

    void onError(int flag, Throwable t);
}
