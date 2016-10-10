package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by zjs on 2016/8/26 0026.
 * email: m15267280642@163.com
 * explain:
 */
public class MarketDetailBean extends MessageBean {


    /**
     * stockName 股票名字
     * to_sell : [{"Price":"0.00","Volume":0},{"Price":"0.00","Volume":0},{"Price":"0.00","Volume":0},{"Price":"0.00","Volume":0},{"Price":"0.00","Volume":0}]
     * to_buy : [{"Price":"6.01","Volume":9715550},{"Price":"6.00","Volume":74900},{"Price":"5.99","Volume":14900},{"Price":"5.98","Volume":16900},{"Price":"5.97","Volume":29900}]
     * PE :               //市盈率
     * LastPrice : 6.01   //当前价格
     * zf : 0.55      //涨幅
     * zfl : 10.07        //涨幅率
     * openprice : 5.48   //今开
     * precloprice : 5.46 //昨收
     * volume : 5031.29万 //成交量
     * turnover_rate : 9.37//换手率
     * heightprice : 6.01  //最高价
     * lowPrice : 5.46     //最低价格
     * turnover : 2.97亿   //成交额
     * all_worth : 32.26   //总市值
     * circula_worth : 32.26 //流通市值
     * amplitude : 10.07     //振幅
     * invol                 //52周内(内盘)
     * outer                //52周外(外盘)
     */

    private String stockName;
    private String PE;
    private String LastPrice;
    private String zf;
    private String zfl;
    private String openprice;
    private String precloprice;
    private String volume;
    private String turnover_rate;
    private String heightprice;
    private String lowPrice;
    private String turnover;
    private String all_worth;
    private String circula_worth;
    private String amplitude;
    private String invol;
    private String outer;
    private String DeletionIndicator;
    private String TradingPhaseCode;

    /**
     * Price : 0.00
     * Volume : 0
     */

    private List<ToSellBean> to_sell;
    /**
     * Price : 6.01
     * Volume : 9715550
     */

    private List<ToBuyBean> to_buy;

    public String getTradingPhaseCode() {
        return TradingPhaseCode;
    }

    public void setTradingPhaseCode(String tradingPhaseCode) {
        TradingPhaseCode = tradingPhaseCode;
    }

    public String getDeletionIndicator() {
        return DeletionIndicator;
    }

    public void setDeletionIndicator(String deletionIndicator) {
        DeletionIndicator = deletionIndicator;
    }

    public String getStockName() {
        return stockName;
    }

    public void setStockName(String stockName) {
        this.stockName = stockName;
    }

    public String getPE() {
        return PE;
    }

    public void setPE(String PE) {
        this.PE = PE;
    }

    public String getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(String LastPrice) {
        this.LastPrice = LastPrice;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public String getZfl() {
        return zfl;
    }

    public void setZfl(String zfl) {
        this.zfl = zfl;
    }

    public String getOpenprice() {
        return openprice;
    }

    public void setOpenprice(String openprice) {
        this.openprice = openprice;
    }

    public String getPrecloprice() {
        return precloprice;
    }

    public void setPrecloprice(String precloprice) {
        this.precloprice = precloprice;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(String turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public String getHeightprice() {
        return heightprice;
    }

    public void setHeightprice(String heightprice) {
        this.heightprice = heightprice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getTurnover() {
        return turnover;
    }

    public void setTurnover(String turnover) {
        this.turnover = turnover;
    }

    public String getAll_worth() {
        return all_worth;
    }

    public void setAll_worth(String all_worth) {
        this.all_worth = all_worth;
    }

    public String getCircula_worth() {
        return circula_worth;
    }

    public void setCircula_worth(String circula_worth) {
        this.circula_worth = circula_worth;
    }

    public String getAmplitude() {
        return amplitude;
    }

    public void setAmplitude(String amplitude) {
        this.amplitude = amplitude;
    }

    public List<ToSellBean> getTo_sell() {
        return to_sell;
    }

    public void setTo_sell(List<ToSellBean> to_sell) {
        this.to_sell = to_sell;
    }

    public List<ToBuyBean> getTo_buy() {
        return to_buy;
    }

    public void setTo_buy(List<ToBuyBean> to_buy) {
        this.to_buy = to_buy;
    }

    public String getInvol() {
        return invol;
    }

    public void setInvol(String invol) {
        this.invol = invol;
    }

    public String getOuter() {
        return outer;
    }

    public void setOuter(String outer) {
        this.outer = outer;
    }

    public static class ToSellBean {
        private String Price;
        private int Volume;

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public int getVolume() {
            return Volume;
        }

        public void setVolume(int Volume) {
            this.Volume = Volume;
        }
    }

    public static class ToBuyBean {
        private String Price;
        private int Volume;

        public String getPrice() {
            return Price;
        }

        public void setPrice(String Price) {
            this.Price = Price;
        }

        public int getVolume() {
            return Volume;
        }

        public void setVolume(int Volume) {
            this.Volume = Volume;
        }
    }
}
