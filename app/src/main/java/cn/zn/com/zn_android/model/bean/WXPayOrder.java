package cn.zn.com.zn_android.model.bean;

import java.io.Serializable;

/**
 * Created by Jolly on 2016/9/29 0029.
 */

public class WXPayOrder implements Serializable {
    private String prepayid;
    private String noncestr;
    private String timestamp;
    private String partnerid;
    private String appid;

    public String getPrepayId() {
        return prepayid;
    }

    public void setPrepayId(String prepayId) {
        this.prepayid = prepayId;
    }

    public String getNonceStr() {
        return noncestr;
    }

    public void setNonceStr(String nonceStr) {
        this.noncestr = nonceStr;
    }

    public String getTimeStamp() {
        return timestamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timestamp = timeStamp;
    }

    public String getPartnerId() {
        return partnerid;
    }

    public void setPartnerId(String partnerId) {
        this.partnerid = partnerId;
    }

    public String getAppId() {
        return appid;
    }

    public void setAppId(String appId) {
        this.appid = appId;
    }
}
