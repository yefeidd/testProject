package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/7/1 0001.
 */
public class RefreshRateBean {
    private String time;
    private boolean isSelected;

    public RefreshRateBean(String time, boolean isSelected) {
        this.time = time;
        this.isSelected = isSelected;
    }

    public String getTime() {
        return time;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

}
