package cn.zn.com.zn_android.model.bean;

/**
 * 操作明细
 * Created by Jolly on 2016/9/14 0014.
 */
public class OperateDetailBean {

    /**
     * code_name : 天鹅股份
     * code_id : 603029
     * posi_num : 0
     * cost_price : 0.00
     * posi_cost : 0
     */

    private String code_name;
    private String code_id;
    private String posi_num;
    private String cost_price;
    private String posi_cost;

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

    public String getCode_id() {
        return code_id;
    }

    public void setCode_id(String code_id) {
        this.code_id = code_id;
    }

    public String getPosi_num() {
        return posi_num;
    }

    public void setPosi_num(String posi_num) {
        this.posi_num = posi_num;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getPosi_cost() {
        return posi_cost;
    }

    public void setPosi_cost(String posi_cost) {
        this.posi_cost = posi_cost;
    }
}
