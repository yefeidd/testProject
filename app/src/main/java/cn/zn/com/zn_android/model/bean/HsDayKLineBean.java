package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/23 0023.
 */
public class HsDayKLineBean extends MessageBean {

    /**
     * tradeDate : 2016-07-25
     * openPrice : 12.38
     * highestPrice : 12.78
     * lowestPrice : 12.16
     * closePrice : 12.18
     * turnoverVol : 48126976
     */

    private String tradeDate;
    private float openPrice;
    private float highestPrice;
    private float lowestPrice;
    private float closePrice;
    private long turnoverVol;

    public String getTradeDate() {
        return tradeDate;
    }

    public void setTradeDate(String tradeDate) {
        this.tradeDate = tradeDate;
    }

    public float getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(float openPrice) {
        this.openPrice = openPrice;
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

    public float getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(float closePrice) {
        this.closePrice = closePrice;
    }

    public long getTurnoverVol() {
        return turnoverVol;
    }

    public void setTurnoverVol(long turnoverVol) {
        this.turnoverVol = turnoverVol;
    }
}
