package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/9/9 0009.
 */
public class TransDetailListBean {

    /**
     * time : 1474440155
     * price : 24.22
     * num : 100
     * money : 2422
     * shui : 7.31
     * trade_type : 2
     */

    private String time;
    private String price;
    private String num;
    private String money;
    private String tax;
    /**
     * 1：买入   2：卖出
     */
    private String trade_type;

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTax() {
        return tax;
    }

    public void setTax(String shui) {
        this.tax = shui;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }
}
