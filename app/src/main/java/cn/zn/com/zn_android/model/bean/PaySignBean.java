package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/9/29 0029.
 */

public class PaySignBean extends MessageBean {

    /**
     * appid : wx0adb85454b5306af
     * parnerid : 1386667302
     * prepay_id : wx2016092909355758ccae92e00263305680
     * nonce_str : u0gw8yaGpyFDLH14
     * timestamp : 1475112957
     * sign : E0D1379DA074F33562EB6481BE9328AF
     */

    private String appid;
    private String parnerid;
    private String prepay_id;
    private String nonce_str;
    private String timestamp;
    private String sign;
    private String out_trade_no;

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getParnerid() {
        return parnerid;
    }

    public void setParnerid(String parnerid) {
        this.parnerid = parnerid;
    }

    public String getPrepay_id() {
        return prepay_id;
    }

    public void setPrepay_id(String prepay_id) {
        this.prepay_id = prepay_id;
    }

    public String getNonce_str() {
        return nonce_str;
    }

    public void setNonce_str(String nonce_str) {
        this.nonce_str = nonce_str;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }
}
