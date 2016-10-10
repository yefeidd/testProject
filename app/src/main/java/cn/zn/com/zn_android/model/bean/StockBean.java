package cn.zn.com.zn_android.model.bean;

import cn.zn.com.zn_android.model.modelMole.MarketImp;

/**
 * Created by Jolly on 2016/8/3 0003.
 */
public class StockBean implements MarketImp {

    /**
     * dataTime : 16:30:35
     * ticker : 300372
     * exchangeCD : XSHE
     * shortNM : *欣泰
     * prevClosePrice : 3.42
     * openPrice : 3.13
     * volume : 17250901
     * value : 5.351901903E7
     * deal : 11753
     * highPrice : 3.25
     * lowPrice : 3.08
     * lastPrice : 3.08
     * change : -0.34
     * changePct : -0.0994
     */

    private String dataTime;
    private String ticker;
    private String exchangeCD;
    private String shortNM;
    private double prevClosePrice;
    private double openPrice;
    private int volume;
    private double value;
    private int deal;
    private double highPrice;
    private double lowPrice;
    private double lastPrice;
    private double change;
    private double changePct;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public String getExchangeCD() {
        return exchangeCD;
    }

    public void setExchangeCD(String exchangeCD) {
        this.exchangeCD = exchangeCD;
    }

    public String getShortNM() {
        return shortNM;
    }

    public void setShortNM(String shortNM) {
        this.shortNM = shortNM;
    }

    public double getPrevClosePrice() {
        return prevClosePrice;
    }

    public void setPrevClosePrice(double prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public int getDeal() {
        return deal;
    }

    public void setDeal(int deal) {
        this.deal = deal;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(double lastPrice) {
        this.lastPrice = lastPrice;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public double getChangePct() {
        return changePct;
    }

    public void setChangePct(double changePct) {
        this.changePct = changePct;
    }
}
