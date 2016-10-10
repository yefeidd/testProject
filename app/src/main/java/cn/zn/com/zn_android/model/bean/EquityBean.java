package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class EquityBean {
    /**
     * TOTAL_SHARES : 6.58亿股
     * NONREST_FLOAT_A : 6.58亿股
     */

    private String TOTAL_SHARES;
    private String NONREST_FLOAT_A;

    public String getTOTAL_SHARES() {
        return TOTAL_SHARES;
    }

    public void setTOTAL_SHARES(String TOTAL_SHARES) {
        this.TOTAL_SHARES = TOTAL_SHARES;
    }

    public String getNONREST_FLOAT_A() {
        return NONREST_FLOAT_A;
    }

    public void setNONREST_FLOAT_A(String NONREST_FLOAT_A) {
        this.NONREST_FLOAT_A = NONREST_FLOAT_A;
    }
}
