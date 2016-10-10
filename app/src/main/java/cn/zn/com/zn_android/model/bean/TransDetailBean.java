package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/9/21 0021.
 */
public class TransDetailBean {

    /**
     * code_change_num : 0
     * code_num : 6800
     * new_index : 24.620
     * new_zdf : 4.23
     * new_index_money : 163200
     * code_id : 000002
     * code_name : 万科A
     * code_start_time : 1473830580
     * code_cb_price : 23.01
     * syl : -55.26
     * fdyk : -86464.22
     * cangwei : 18.08
     */

    private int code_change_num; // 可卖数量
    private int code_num; // 持仓数量
    private String new_index; // 最新价
    private String new_zdf; // 涨跌幅
    private String new_index_money; // 最新市值
    private String code_id;
    private String code_name;
    private String code_start_time; // 建仓时间
    private String code_cb_price; // 每股成本
    private String syl; // 收益率
    private String fdyk; // 浮动盈亏
    private String cangwei; // 仓位

    /**
     * buy_all_money : 1399796
     * sell_all_money : 987716
     * new_price : 25.47
     * zdf : -5.67
     * win_lose : 82.42
     * sy : 29
     */

    private String buy_all_money;
    private String sell_all_money;
    private String new_price;
    private String zdf;
    private String win_lose;
    private String sy;


    public int getCode_change_num() {
        return code_change_num;
    }

    public void setCode_change_num(int code_change_num) {
        this.code_change_num = code_change_num;
    }

    public int getCode_num() {
        return code_num;
    }

    public void setCode_num(int code_num) {
        this.code_num = code_num;
    }

    public String getNew_index() {
        return new_index;
    }

    public void setNew_index(String new_index) {
        this.new_index = new_index;
    }

    public String getNew_zdf() {
        return new_zdf;
    }

    public void setNew_zdf(String new_zdf) {
        this.new_zdf = new_zdf;
    }

    public String getNew_index_money() {
        return new_index_money;
    }

    public void setNew_index_money(String new_index_money) {
        this.new_index_money = new_index_money;
    }

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getCode_start_time() {
        return code_start_time;
    }

    public void setCode_start_time(String code_start_time) {
        this.code_start_time = code_start_time;
    }

    public String getCode_cb_price() {
        return code_cb_price;
    }

    public void setCode_cb_price(String code_cb_price) {
        this.code_cb_price = code_cb_price;
    }

    public String getSyl() {
        return syl;
    }

    public void setSyl(String syl) {
        this.syl = syl;
    }

    public String getFdyk() {
        return fdyk;
    }

    public void setFdyk(String fdyk) {
        this.fdyk = fdyk;
    }

    public String getCangwei() {
        return cangwei;
    }

    public void setCangwei(String cangwei) {
        this.cangwei = cangwei;
    }

    public String getBuy_all_money() {
        return buy_all_money;
    }

    public void setBuy_all_money(String buy_all_money) {
        this.buy_all_money = buy_all_money;
    }

    public String getSell_all_money() {
        return sell_all_money;
    }

    public void setSell_all_money(String sell_all_money) {
        this.sell_all_money = sell_all_money;
    }

    public String getNew_price() {
        return new_price;
    }

    public void setNew_price(String new_price) {
        this.new_price = new_price;
    }

    public String getZdf() {
        return zdf;
    }

    public void setZdf(String zdf) {
        this.zdf = zdf;
    }

    public String getWin_lose() {
        return win_lose;
    }

    public void setWin_lose(String win_lose) {
        this.win_lose = win_lose;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
    }
}
