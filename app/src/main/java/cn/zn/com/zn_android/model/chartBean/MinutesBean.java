package cn.zn.com.zn_android.model.chartBean;


public class MinutesBean implements ChartImp {
    public String time;  //时间
    public float LastPrice;//成交价格
    public String current_volume;//成交量
    public float average = Float.NaN;//均价
    public String zf_rate;//涨跌幅度
    public String zf;//涨跌价格
    public float vol;

    /**
     * HighPrice : 10.65
     * LowPrice : 10.65
     */

    private String HighPrice;
    private String LowPrice;

    public String getHighPrice() {
        return HighPrice;
    }

    public void setHighPrice(String HighPrice) {
        this.HighPrice = HighPrice;
    }

    public String getLowPrice() {
        return LowPrice;
    }

    public void setLowPrice(String LowPrice) {
        this.LowPrice = LowPrice;
    }


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public float getLastPrice() {
        return LastPrice;
    }

    public void setLastPrice(float lastPrice) {
        LastPrice = lastPrice;
    }

    public String getCurrent_volume() {
        return current_volume;
    }

    public void setCurrent_volume(String current_volume) {
        this.current_volume = current_volume;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public String getZf_rate() {
        return zf_rate;
    }

    public void setZf_rate(String zf_rate) {
        this.zf_rate = zf_rate;
    }

    public String getZf() {
        return zf;
    }

    public void setZf(String zf) {
        this.zf = zf;
    }

    public float getVol() {
        return vol;
    }

    public void setVol(float vol) {
        this.vol = vol;
    }
}
