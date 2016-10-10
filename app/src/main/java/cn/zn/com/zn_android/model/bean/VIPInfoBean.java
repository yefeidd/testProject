package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/4/5 0005.
 */
public class VIPInfoBean {
    /*{
        "is_vip": "0",
            "vip": "问：什么是 VIP 会员？
        答：VIP 会员是 98 财富网注册用户获得特权的标志，一旦成为 VIP 会员可以终身享受 98 财富网各种专项服务和权利。
        问：VIP 会员可以享受哪些特权？
        答：免费观看股市直播、免、费查看前期视频录像、在特约讲堂中无限制发言，参
        与内部定向培训与交流、获得网站物质回馈、信息回馈以及后期将会出台的所有
        优惠举措。
        问：如何办理 VIP 会员？
        答：2000 财富币/人——点此加入"
    }*/

    private int is_vip; // 0:非会员
    private String vip; // 会员信息介绍
    private String message;
    private String vipurl;

    public int getIs_vip() {
        return is_vip;
    }

    public VIPInfoBean setIs_vip(int is_vip) {
        this.is_vip = is_vip;
        return this;
    }

    public String getVip() {
        return vip;
    }

    public VIPInfoBean setVip(String vip) {
        this.vip = vip;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public VIPInfoBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public String getVipurl() {
        return vipurl;
    }

    public VIPInfoBean setVipurl(String vipurl) {
        this.vipurl = vipurl;
        return this;
    }

}
