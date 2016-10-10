package cn.zn.com.zn_android.viewfeatures;

import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.UserInfoBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;

/**
 * Created by Jolly on 2016/6/27 0027.
 */
public interface PersonView extends BaseView {
    void queryUserInfoResult(ReturnValue<UserInfoBean> returnValue);

    void virReg(ReturnValue<MessageBean> returnValue, int flag);
}
