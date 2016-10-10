package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class DebtBean {
    /**
     * T_ASSETS : 62.31亿元
     * T_CL : 23.83亿元
     * T_SH_EQUITY : 37.72亿元
     * EPS : 5.73元
     */

    private String T_ASSETS;
    private String T_CL;
    private String T_SH_EQUITY;
    private String EPS;

    public String getT_ASSETS() {
        return T_ASSETS;
    }

    public void setT_ASSETS(String T_ASSETS) {
        this.T_ASSETS = T_ASSETS;
    }

    public String getT_CL() {
        return T_CL;
    }

    public void setT_CL(String T_CL) {
        this.T_CL = T_CL;
    }

    public String getT_SH_EQUITY() {
        return T_SH_EQUITY;
    }

    public void setT_SH_EQUITY(String T_SH_EQUITY) {
        this.T_SH_EQUITY = T_SH_EQUITY;
    }

    public String getEPS() {
        return EPS;
    }

    public void setEPS(String EPS) {
        this.EPS = EPS;
    }
}
