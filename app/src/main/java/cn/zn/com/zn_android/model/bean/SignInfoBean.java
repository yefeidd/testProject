package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/9/26 0026.
 */

public class SignInfoBean {

    /**
     * total_day : 1
     * continuity_day : 1
     * today : 2016-09-26
     * sign_state : 1
     */

    private String total_day;
    private String continuity_day;
    private String today;
    private int sign_state;

    public String getTotal_day() {
        return total_day;
    }

    public void setTotal_day(String total_day) {
        this.total_day = total_day;
    }

    public String getContinuity_day() {
        return continuity_day;
    }

    public void setContinuity_day(String continuity_day) {
        this.continuity_day = continuity_day;
    }

    public String getToday() {
        return today;
    }

    public void setToday(String today) {
        this.today = today;
    }

    public int getSign_state() {
        return sign_state;
    }

    public void setSign_state(int sign_state) {
        this.sign_state = sign_state;
    }
}
