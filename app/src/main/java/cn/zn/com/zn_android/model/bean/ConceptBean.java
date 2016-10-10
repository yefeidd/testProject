package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * 某概念下股票列表及相关数据
 * Created by Jolly on 2016/8/18 0018.
 */
public class ConceptBean {
    private String updownrate;
    private String up;
    private String ping;
    private String down;
    private List<HotHyBean> list;

    public String getUpdownrate() {
        return updownrate;
    }

    public void setUpdownrate(String updownrate) {
        this.updownrate = updownrate;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getPing() {
        return ping;
    }

    public void setPing(String ping) {
        this.ping = ping;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public List<HotHyBean> getList() {
        return list;
    }

    public void setList(List<HotHyBean> list) {
        this.list = list;
    }
}
