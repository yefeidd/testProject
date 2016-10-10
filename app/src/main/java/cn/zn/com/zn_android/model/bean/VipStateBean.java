package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/4/1 0001.
 * explain:
 */
public class VipStateBean {
    /**
     * isroomvip : 1
     * not login ：2
     * not vip：0
     */

    private String isroomvip;// 0:非会员

    public String getIsroomvip() {
        return isroomvip;
    }

    public void setIsroomvip(String isroomvip) {
        this.isroomvip = isroomvip;
    }
}
