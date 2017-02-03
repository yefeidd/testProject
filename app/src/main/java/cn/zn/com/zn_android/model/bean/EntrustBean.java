package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/10/20 0020.
 */

public class EntrustBean {

    /**
     * code_id : 300287
     * code_num : 100
     * code_name : 飞利信
     * code_price : 13.00
     * trade_type : 1
     * entrust_time : 2016-10-20 10:25
     * frozen_money : 1300
     */

    private String code_id;
    private String code_num; // 委托数量
    private String code_name;
    private String code_price; // 委托价格
    private String trade_type; // 委托类型   1买入   2  卖出
    private String entrust_time; // 委托提交时间
    private String frozen_money; // 冻结资金
    /**
     * id : 492
     */

    private String id;

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getCode_num() {
        return code_num;
    }

    public void setCode_num(String code_num) {
        this.code_num = code_num;
    }

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getCode_price() {
        return code_price;
    }

    public void setCode_price(String code_price) {
        this.code_price = code_price;
    }

    public String getTrade_type() {
        return trade_type;
    }

    public void setTrade_type(String trade_type) {
        this.trade_type = trade_type;
    }

    public String getEntrust_time() {
        return entrust_time;
    }

    public void setEntrust_time(String entrust_time) {
        this.entrust_time = entrust_time;
    }

    public String getFrozen_money() {
        return frozen_money;
    }

    public void setFrozen_money(String frozen_money) {
        this.frozen_money = frozen_money;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
