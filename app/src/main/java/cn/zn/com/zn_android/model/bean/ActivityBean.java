package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/7/5 0005.
 */
public class ActivityBean {
    private String imgURL;
    private String name;
    private String endTime;
    private String status;

    public ActivityBean(String imgURL, String name, String endTime, String status) {
        this.imgURL = imgURL;
        this.name = name;
        this.endTime = endTime;
        this.status = status;
    }

    public String getImgURL() {
        return imgURL;
    }

    public String getName() {
        return name;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getStatus() {
        return status;
    }
}
