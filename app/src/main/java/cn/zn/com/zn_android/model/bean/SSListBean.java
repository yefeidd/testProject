package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/8/12 0012.
 */
public class SSListBean {

    /**
     * changePct : -9.99
     * lastPrice : 23.7
     * shortNM : 银禧科技
     * ticker : 300221
     */

    private List<DfbBean> dfb;
    /**
     * LastPrice : 2.38
     * SecurityID : 000725
     * SecurityName : 京东方Ａ
     * zfl : 1.70
     */

    private List<DjgbBean> djgb;
    /**
     * Change : 2.50
     * ChangePct : 2.68
     * TickerSymbol : 00001
     * Ticker_name : 长和
     */

    private List<GgtBean> ggt;
    /**
     * LastPrice : 317.00
     * SecurityID : 600519
     * SecurityName : 贵州茅台
     * zfl : 1.07
     */

    private List<GjgbBean> gjgb;
    /**
     * average : -1.71
     * change : 0.35
     * change_rate : 1.6
     * code_id : 150
     * id : 1
     * name : 成渝特区
     * tic_name : 银河磁体
     */

    private List<HotGnBean> hot_gn;
    /**
     * average : 1.07
     * change : 1.40
     * change_rate : 10.00
     * code_id : 536
     * id : 30
     * name : 专用设备
     * tic_name : 华舟应急
     */

    private List<HotHyBean> hot_hy;
    /**
     * LastPrice : 59.31
     * SEC_SHORT_NAME : 吉宏股份
     * TICKER_SYMBOL : 002803
     * hsl : 53.31
     */

    private List<HslBean> hsl;
    /**
     * changePct : 44.04
     * lastPrice : 9.91
     * shortNM : N亚虹
     * ticker : 603159
     */

    private List<ZfbBean> zfb;
    /**
     * LastPrice : 26.64
     * SecurityID : 300535
     * SecurityName : N达威
     * zfl : 24.00
     */

    private List<ZflBean> zfl;

    public List<DfbBean> getDfb() {
        return dfb;
    }

    public void setDfb(List<DfbBean> dfb) {
        this.dfb = dfb;
    }

    public List<DjgbBean> getDjgb() {
        return djgb;
    }

    public void setDjgb(List<DjgbBean> djgb) {
        this.djgb = djgb;
    }

    public List<GgtBean> getGgt() {
        return ggt;
    }

    public void setGgt(List<GgtBean> ggt) {
        this.ggt = ggt;
    }

    public List<GjgbBean> getGjgb() {
        return gjgb;
    }

    public void setGjgb(List<GjgbBean> gjgb) {
        this.gjgb = gjgb;
    }

    public List<HotGnBean> getHot_gn() {
        return hot_gn;
    }

    public void setHot_gn(List<HotGnBean> hot_gn) {
        this.hot_gn = hot_gn;
    }

    public List<HotHyBean> getHot_hy() {
        return hot_hy;
    }

    public void setHot_hy(List<HotHyBean> hot_hy) {
        this.hot_hy = hot_hy;
    }

    public List<HslBean> getHsl() {
        return hsl;
    }

    public void setHsl(List<HslBean> hsl) {
        this.hsl = hsl;
    }

    public List<ZfbBean> getZfb() {
        return zfb;
    }

    public void setZfb(List<ZfbBean> zfb) {
        this.zfb = zfb;
    }

    public List<ZflBean> getZfl() {
        return zfl;
    }

    public void setZfl(List<ZflBean> zfl) {
        this.zfl = zfl;
    }
}
