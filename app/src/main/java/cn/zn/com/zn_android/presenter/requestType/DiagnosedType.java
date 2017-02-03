package cn.zn.com.zn_android.presenter.requestType;

/**
 * Created by zjs on 2016/12/13 0013.
 * email: m15267280642@163.com
 * explain:
 */

public enum DiagnosedType {
    /********************** 老师页面接口***************************/
    TEA_ANSWER_QUESTION,   //老师待回答/回答的问题的列表
    QUESTION_DETAIL,       //老师回答的问题详情
    ANSWER_DETAIL,         //老师回答页面的问题详情
    GET_TOKEN,             // 获取七牛云的token
    UPLOAD_IMG,            //上传图片
    COMMIT_TEACHER_ANSWER,      //提交答案
    ADD_GOOD_ANSWER,            //设为精彩回答
    CANCEL_GOOD_ANSWER,         //取消精彩回答
    DEL_ANSWER,                 //删除回答
    RESPONDER,                 //抢答

    /********************** 用户页面接口 **************************/
    USER_EXCELLENT_ANSWER_LIST, // 首页精彩回答
    USER_DIAGNOSE_INDEX,        // 首页精彩回答
}
