package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/12 0012.
 */
public class DfbBean {
    private double changePct;
    private double lastPrice;
    private String shortNM;
    private String ticker;

    public double getChangePct() {
        return changePct;
    }

    public void setChangePct(double changePct) {
        this.changePct = changePct;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getShortNM() {
        return shortNM;
    }

    public void setShortNM(String shortNM) {
        this.shortNM = shortNM;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }
}
