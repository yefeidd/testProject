package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/25 0025.
 */
public class ShSzDetailBean {

    /**
     * LastIndex : 3085.88
     * Change : -0.00
     * ChangeRate : 0.00
     * OpenIndex : 0.00
     * PreCloseIndex : 3085.88
     * HighIndex : 0.00
     * LowIndex : 0.00
     * Turnover : 0.00
     * TradVolume : 0.00
     * zfl : 0.00
     * zjs : 0
     * pjs : 67
     * djs : 1045
     */

    private String LastIndex;
    private String Change;
    private String ChangeRate;
    private String OpenIndex;
    private String PreCloseIndex;
    private String HighIndex; //
    private String LowIndex;
    private String Turnover;//成交额
    private String TradVolume;//成交量
    private String zfl;
    private int zjs;
    private int pjs;
    private int djs;

    public String getLastIndex() {
        return LastIndex;
    }

    public void setLastIndex(String LastIndex) {
        this.LastIndex = LastIndex;
    }

    public String getChange() {
        return Change;
    }

    public void setChange(String Change) {
        this.Change = Change;
    }

    public String getChangeRate() {
        return ChangeRate;
    }

    public void setChangeRate(String ChangeRate) {
        this.ChangeRate = ChangeRate;
    }

    public String getOpenIndex() {
        return OpenIndex;
    }

    public void setOpenIndex(String OpenIndex) {
        this.OpenIndex = OpenIndex;
    }

    public String getPreCloseIndex() {
        return PreCloseIndex;
    }

    public void setPreCloseIndex(String PreCloseIndex) {
        this.PreCloseIndex = PreCloseIndex;
    }

    public String getHighIndex() {
        return HighIndex;
    }

    public void setHighIndex(String HighIndex) {
        this.HighIndex = HighIndex;
    }

    public String getLowIndex() {
        return LowIndex;
    }

    public void setLowIndex(String LowIndex) {
        this.LowIndex = LowIndex;
    }

    public String getTurnover() {
        return Turnover;
    }

    public void setTurnover(String Turnover) {
        this.Turnover = Turnover;
    }

    public String getTradVolume() {
        return TradVolume;
    }

    public void setTradVolume(String TradVolume) {
        this.TradVolume = TradVolume;
    }

    public String getZfl() {
        return zfl;
    }

    public void setZfl(String zfl) {
        this.zfl = zfl;
    }

    public int getZjs() {
        return zjs;
    }

    public void setZjs(int zjs) {
        this.zjs = zjs;
    }

    public int getPjs() {
        return pjs;
    }

    public void setPjs(int pjs) {
        this.pjs = pjs;
    }

    public int getDjs() {
        return djs;
    }

    public void setDjs(int djs) {
        this.djs = djs;
    }
}
