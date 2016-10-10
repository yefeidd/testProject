package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/4/11 0011.
 * explain:
 */
public class UserPrivateTalkBean {

    /**
     * nickname : 代建华
     * qacontent : 聊天内容
     * status : 0,1,2  0代表未回答,1代表已经回答,2代表老师
     * times : 1460357622
     */

    private String nickname;
    private String qacontent;
    private String status;
    private String times;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getQacontent() {
        return qacontent;
    }

    public void setQacontent(String qacontent) {
        this.qacontent = qacontent;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
