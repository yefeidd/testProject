package cn.zn.com.zn_android.model.bean;

import cn.zn.com.zn_android.uiclass.customerview.CountDown.CountDownInfo;

/**
 * Created by zjs on 2017/1/3 0003.
 * email: m15267280642@163.com
 * explain:
 */

public class WaitAnswerBean extends CountDownInfo {
    public QuestionBean getQuestionBean() {
        return bean;
    }

    public void setQuestionBean(QuestionBean bean) {
        this.bean = bean;
    }

    private QuestionBean bean;
}
