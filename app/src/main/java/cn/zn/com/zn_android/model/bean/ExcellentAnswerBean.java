package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/12/14.
 */

public class ExcellentAnswerBean {

    /**
     * id : 11
     * scode : 600502
     * sname : 安徽水利
     * pdescription : 11.这只股票怎么样，能不能让我发家致富
     * avatars : http://qiniu.98cfw.com/2016-07-20_09:16:09M8ZCct1w
     * nickname : 翻翻翻翻翻翻
     * htime : 12-13 10:16
     */

    private String id;
    private String tid;
    private String scode;
    private String sname;
    private String pdescription;
    private String avatars;
    private String nickname;
    private String htime;
    private String answer_info;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getAnswer_info() {
        return answer_info;
    }

    public void setAnswer_info(String answer_info) {
        this.answer_info = answer_info;
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

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHtime() {
        return htime;
    }

    public void setHtime(String htime) {
        this.htime = htime;
    }
}
