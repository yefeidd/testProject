package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/7/29 0029.
 * email: m15267280642@163.com
 * explain:
 */
public class SelfSelectStockBean {

    /**
     * id : 1
     * user_id : 10016
     * ticker : 600360
     * orders : 1
     * addtime : 1470386412
     * stock_name : 华微电子
     * cprice : 12.23
     * change_rate : 6.07
     */

    private String id;
    private String user_id;
    private String ticker;
    private String orders;
    private String addtime;
    private String stock_name;
    private String cprice;
    private double change_rate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getStock_name() {
        return stock_name;
    }

    public void setStock_name(String stock_name) {
        this.stock_name = stock_name;
    }

    public String getCprice() {
        return cprice;
    }

    public void setCprice(String cprice) {
        this.cprice = cprice;
    }

    public double getChange_rate() {
        return change_rate;
    }

    public void setChange_rate(double change_rate) {
        this.change_rate = change_rate;
    }
}
