package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.presenter.requestType.QADetailType;

/**
 * Created by Jolly on 2016/12/2.
 */

public interface QuestionAnswerDetailView extends BaseView {
    void onSuccess(QADetailType requestType, Object object);

    void onError(QADetailType requestType, Throwable t);
}
