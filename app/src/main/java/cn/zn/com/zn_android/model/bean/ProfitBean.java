package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class ProfitBean {
    /**
     * BASIC_EPS : 0.11元
     * REVENUE : 27.49亿元
     * OPERATE_PROFIT : 1.43亿元
     * N_INCOME : 1.05亿元
     */

    private String BASIC_EPS;
    private String REVENUE;
    private String OPERATE_PROFIT;
    private String N_INCOME;

    public String getBASIC_EPS() {
        return BASIC_EPS;
    }

    public void setBASIC_EPS(String BASIC_EPS) {
        this.BASIC_EPS = BASIC_EPS;
    }

    public String getREVENUE() {
        return REVENUE;
    }

    public void setREVENUE(String REVENUE) {
        this.REVENUE = REVENUE;
    }

    public String getOPERATE_PROFIT() {
        return OPERATE_PROFIT;
    }

    public void setOPERATE_PROFIT(String OPERATE_PROFIT) {
        this.OPERATE_PROFIT = OPERATE_PROFIT;
    }

    public String getN_INCOME() {
        return N_INCOME;
    }

    public void setN_INCOME(String N_INCOME) {
        this.N_INCOME = N_INCOME;
    }
}
