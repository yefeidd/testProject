package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/8/24 0024.
 * email: m15267280642@163.com
 * explain:
 */
public class HsWeekKLineBean extends MessageBean {

    /**
     * closePrice : 47.23
     * endDate : 2015-12-18
     * highestPrice : 48.19
     * lowestPrice : 44.05
     * openPrice : 44.05
     * turnoverVol : 25054375
     */

    private float closePrice;
    private String endDate;
    private float highestPrice;
    private float lowestPrice;
    private float openPrice;
    private long turnoverVol;

    public float getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public float getHighestPrice() {
        return highestPrice;
    }

    public void setHighestPrice(float highestPrice) {
        this.highestPrice = highestPrice;
    }

    public float getLowestPrice() {
        return lowestPrice;
    }

    public void setLowestPrice(float lowestPrice) {
        this.lowestPrice = lowestPrice;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
    }

    public long getTurnoverVol() {
        return turnoverVol;
    }

    public void setTurnoverVol(long turnoverVol) {
        this.turnoverVol = turnoverVol;
    }
}
