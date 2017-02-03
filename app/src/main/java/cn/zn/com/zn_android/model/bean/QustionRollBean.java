package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/11/25.
 */

public class QustionRollBean {

    /**
     * id : 84
     * scode : 000710
     * sname : 天兴仪表
     * pdescription : 擦擦擦擦啊啊啊
     * times : 2016-12-13 17:38:38
     */

    private String id;
    private String scode;
    private String sname;
    private String nickname;
    private String pdescription;
    private String times;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }
}
