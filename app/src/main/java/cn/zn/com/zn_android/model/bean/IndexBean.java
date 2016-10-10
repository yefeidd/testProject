package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/7/27 0027.
 */
public class IndexBean extends MessageBean {

    /**
     * LastIndex : 2986.2301
     * Change : -63.94
     * ChangeRate : -2.14
     */

    private String LastIndex;
    private String Change;
    private String ChangeRate;

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
}
