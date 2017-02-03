package cn.zn.com.zn_android.model.bean;

import cn.zn.com.zn_android.manage.Constants;

/**
 * 用户基础信息
 * Created by WJL on 2016/3/10 0010 10:04.
 */
public class RnBean extends UserInfoBean {
    /**
     * 是否登录状态，0为未登录
     */
    private int isLogin = Constants.NOT_LOGIN;
    /**
     * 手机号
     */
    private String phone = "";
    /**
     * 用户名
     */
    private String name;
    /**
     * 密码
     */
    private String password = "";

    /**
     * 会员等级
     */
    private String memberGrade = "";

    /**
     * 头像图片URL
     */
    private String avatars;

    private String sessionID = "";

    private String wealth; // 财富

    private boolean isConnected = false;

    /**
     * 流量数据下行情刷新频率 = tDRefreshRate * 5
     */
    private int tDRefreshRate;

    /**
     * WiFi下行情刷新频率 = wifiRefreshRate * 5
     */
    private int wifiRefreshRate;

    /**
     * 微信支付后台订单号
     */
    private String out_trade_no;

    /**
     * 该账号是否是老师账号
     * 1：是
     * 0：不是，学生账号
     */
    private boolean isTeacher = false;

    public boolean getIsTeacher() {
        return isTeacher;
    }

    public void setIsTeacher(int isTeacher) {
        if (isTeacher == 1) this.isTeacher = true;
        else this.isTeacher = false;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    @Override
    public String getWealth() {
        return wealth;
    }

    @Override
    public RnBean setWealth(String wealth) {
        this.wealth = wealth;
        return this;
    }

    public String getSessionID() {
        return sessionID;
    }

    public RnBean setSessionID(String sessionID) {
        this.sessionID = sessionID;
        return this;
    }


    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getMemberGrade() {
        return memberGrade;
    }

    public void setMemberGrade(String memberGrade) {
        this.memberGrade = memberGrade;
    }

    public int getIsLogin() {
        return isLogin;
    }

    public void setIsLogin(int isLogin) {

//        if (isLogin == Constants.NOT_LOGIN) {
//            clearInfo();
//        }
        this.isLogin = isLogin;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public RnBean setConnected(boolean connected) {
        isConnected = connected;
        return this;
    }

    public int gettDRefreshRate() {
        return tDRefreshRate;
    }

    public void settDRefreshRate(int tDRefreshRate) {
        this.tDRefreshRate = tDRefreshRate;
    }

    public int getWifiRefreshRate() {
        return wifiRefreshRate;
    }

    public void setWifiRefreshRate(int wifiRefreshRate) {
        this.wifiRefreshRate = wifiRefreshRate;
    }

//    public void clearInfo() {
//        this.phone = "";
//        this.name = "";
//        this.password = "";
//        this.memberGrade = "";
//        this.avatars = "";
//        this.sessionID = "";
//    }

}
