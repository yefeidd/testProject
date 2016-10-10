package cn.zn.com.zn_android.model.bean;

/**
 * 交易记录
 * Created by Jolly on 2016/9/12 0012.
 */
public class StockRecordItemBean {

    /**
     * buy_all_money : 1399796
     * sell_all_money : 987716
     * code_id : 000002
     * code_name : 万科A
     * win_lose : 82.42
     * sy : 412080
     */

    private String buy_all_money;
    private String sell_all_money;
    private String code_id;
    private String code_name;
    private String win_lose;
    private String sy;

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
