package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.OnLineTeacherRankingType;

/**
 * Created by Jolly on 2016/11/29.
 */

public interface OnlineTeacherRankingView extends BaseView {
    void onSuccess(OnLineTeacherRankingType requestType, Object object);

    void onError(OnLineTeacherRankingType requestType, Throwable t);
}
