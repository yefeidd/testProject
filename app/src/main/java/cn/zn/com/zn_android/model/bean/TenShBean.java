package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class TenShBean {
    /**
     * SH_NAME : 北京首都旅游集团有限责任公司
     * HOLD_VOL : 2.5亿
     * HOLD_PCT : 37.790
     */

    private String SH_NAME;
    private String HOLD_VOL;
    private String HOLD_PCT;
    private String END_DATE;

    public String getEND_DATE() {
        return END_DATE;
    }

    public void setEND_DATE(String END_DATE) {
        this.END_DATE = END_DATE;
    }

    public String getSH_NAME() {
        return SH_NAME;
    }

    public void setSH_NAME(String SH_NAME) {
        this.SH_NAME = SH_NAME;
    }

    public String getHOLD_VOL() {
        return HOLD_VOL;
    }

    public void setHOLD_VOL(String HOLD_VOL) {
        this.HOLD_VOL = HOLD_VOL;
    }

    public String getHOLD_PCT() {
        return HOLD_PCT;
    }

    public void setHOLD_PCT(String HOLD_PCT) {
        this.HOLD_PCT = HOLD_PCT;
    }
}
