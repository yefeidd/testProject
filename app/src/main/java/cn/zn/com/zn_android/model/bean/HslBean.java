package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/12 0012.
 */
public class HslBean {
    private String LastPrice;
    private String SEC_SHORT_NAME;
    private String TICKER_SYMBOL;
    private String hsl;

    public String getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(String LastPrice) {
        this.LastPrice = LastPrice;
    }

    public String getSEC_SHORT_NAME() {
        return SEC_SHORT_NAME;
    }

    public void setSEC_SHORT_NAME(String SEC_SHORT_NAME) {
        this.SEC_SHORT_NAME = SEC_SHORT_NAME;
    }

    public String getTICKER_SYMBOL() {
        return TICKER_SYMBOL;
    }

    public void setTICKER_SYMBOL(String TICKER_SYMBOL) {
        this.TICKER_SYMBOL = TICKER_SYMBOL;
    }

    public String getHsl() {
        return hsl;
    }

    public void setHsl(String hsl) {
        this.hsl = hsl;
    }
}
