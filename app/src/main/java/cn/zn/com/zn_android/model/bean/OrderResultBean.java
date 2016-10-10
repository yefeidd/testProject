package cn.zn.com.zn_android.model.bean;

/**
 * 生成订单 结果
 * Created by Jolly on 2016/4/12 0012.
 */
public class OrderResultBean extends MessageBean {
    //'order_num'=>$order_num,'user_id'=>$uid,'mobile'=>$userinfo['mobile'],'regtime'=>$userinfo['regtime'])
    private String order_num; // 订单号
    private String user_id; // 用户id
    private String mobile; // 手机号
    private String regtime; // 用户注册时间

    public String getOrder_num() {
        return order_num;
    }

    public OrderResultBean setOrder_num(String order_num) {
        this.order_num = order_num;
        return this;
    }

    public String getUser_id() {
        return user_id;
    }

    public OrderResultBean setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public String getMobile() {
        return mobile;
    }

    public OrderResultBean setMobile(String mobile) {
        this.mobile = mobile;
        return this;
    }

    public String getRegtime() {
        return regtime;
    }

    public OrderResultBean setRegtime(String regtime) {
        this.regtime = regtime;
        return this;
    }
}
