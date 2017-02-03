package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/12/26 0026.
 * email: m15267280642@163.com
 * explain: 账单详情页的bean类
 */

public class BillDetailBean extends MessageBean {
    /**
     * money : -10币
     * nickname : 天王驾到
     * remark : 交易已完成
     * times : 2016-03-31 16:07
     * way : 充值
     */

    private String order_num;
    private String money;
    private String nickname;
    private String remark;
    private String times;
    private String way;

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getWay() {
        return way;
    }

    public void setWay(String way) {
        this.way = way;
    }
}
