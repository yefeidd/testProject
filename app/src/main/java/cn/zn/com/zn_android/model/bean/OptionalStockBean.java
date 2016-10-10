package cn.zn.com.zn_android.model.bean;

import cn.zn.com.zn_android.model.modelMole.MarketImp;

/**
 * Created by Jolly on 2016/6/28 0028.
 */
public class OptionalStockBean implements MarketImp {
    private String name;
    private String code;
    private String price;
    private String upDown;
    private int mStockType;//0代表深交所，1代表港交所

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
    private String prevClosePrice;
    private String openPrice;
    private int volume;
    private String value;
    private int deal;
    private String highPrice;
    private String lowPrice;
    private String lastPrice;
    private String change;
    private float changePct;
    /**
     * id : 2
     * user_id : 10016
     * orders : 2
     * addtime : 1471424583
     * stock_name : null
     * cprice : 0.00
     * change_rate : 0
     */

    private String id;
    private String user_id;
    private String orders;
    private String addtime;
    private Object stock_name;
    private String cprice;
    private int change_rate;

    private String code_name;
    /**
     * money : 43.41亿
     */

    private String money;

    public String getCode_name() {
        return code_name;
    }

    public void setCode_name(String code_name) {
        this.code_name = code_name;
    }

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

    public String getPrevClosePrice() {
        return prevClosePrice;
    }

    public void setPrevClosePrice(String prevClosePrice) {
        this.prevClosePrice = prevClosePrice;
    }

    public String getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(String openPrice) {
        this.openPrice = openPrice;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getDeal() {
        return deal;
    }

    public void setDeal(int deal) {
        this.deal = deal;
    }

    public String getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(String highPrice) {
        this.highPrice = highPrice;
    }

    public String getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(String lowPrice) {
        this.lowPrice = lowPrice;
    }

    public String getLastPrice() {
        return lastPrice;
    }

    public void setLastPrice(String lastPrice) {
        this.lastPrice = lastPrice;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public float getChangePct() {
        return changePct;
    }

    public void setChangePct(float changePct) {
        this.changePct = changePct;
    }

    public int getmStockType() {
        return mStockType;
    }

    public void setmStockType(int mStockType) {
        this.mStockType = mStockType;
    }


    public String getName() {
        return name;
    }

    public OptionalStockBean setName(String name) {
        this.name = name;
        return this;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getUpDown() {
        return upDown;
    }

    public void setUpDown(String upDown) {
        this.upDown = upDown;
    }

    public String getCode() {
        return code;
    }

    public OptionalStockBean setCode(String code) {
        this.code = code;
        return this;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getOrders() {
        return orders;
    }

    public void setOrders(String orders) {
        this.orders = orders;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public Object getStock_name() {
        return stock_name;
    }

    public void setStock_name(Object stock_name) {
        this.stock_name = stock_name;
    }

    public String getCprice() {
        return cprice;
    }

    public void setCprice(String cprice) {
        this.cprice = cprice;
    }

    public int getChange_rate() {
        return change_rate;
    }

    public void setChange_rate(int change_rate) {
        this.change_rate = change_rate;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }
}
