package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/8/25 0025.
 */
public class ShCfgBean {
    private List<ZflBean> zfb;
    private List<ZflBean> dfb;
    private List<HslBean> hsl;

    public List<ZflBean> getZfb() {
        return zfb;
    }

    public void setZfb(List<ZflBean> zfb) {
        this.zfb = zfb;
    }

    public List<ZflBean> getDfb() {
        return dfb;
    }

    public void setDfb(List<ZflBean> dfb) {
        this.dfb = dfb;
    }

    public List<HslBean> getHsl() {
        return hsl;
    }

    public void setHsl(List<HslBean> hsl) {
        this.hsl = hsl;
    }
}
