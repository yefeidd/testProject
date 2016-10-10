package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/9/8 0008.
 */
public class ImitateFryItemBean {

    /**
     * now_price : 23.54
     * to_sell_num : 3000
     * code_id : 000002
     * code_name : 万科A
     * position_num : 4000
     * new_worth : 94160.00
     * profit : -35.10
     * avage_price : 45.78
     */

    private String now_price;
    private int to_sell_num;
    private String code_id;
    private String code_name;
    private String position_num;
    private String new_worth;
    private String profit;
    private String avage_price;

    public String getNow_price() {
        return now_price;
    }

    public void setNow_price(String now_price) {
        this.now_price = now_price;
    }

    public int getTo_sell_num() {
        return to_sell_num;
    }

    public void setTo_sell_num(int to_sell_num) {
        this.to_sell_num = to_sell_num;
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

    public String getPosition_num() {
        return position_num;
    }

    public void setPosition_num(String position_num) {
        this.position_num = position_num;
    }

    public String getNew_worth() {
        return new_worth;
    }

    public void setNew_worth(String new_worth) {
        this.new_worth = new_worth;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getAvage_price() {
        return avage_price;
    }

    public void setAvage_price(String avage_price) {
        this.avage_price = avage_price;
    }
}
