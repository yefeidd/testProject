package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by zjs on 2016/3/28 0028.
 * explain:
 */
public class ChatMessageBean {

    /**
     * gift : []
     * level : 小白
     * msg : 9999999
     * nickname : tt
     * times : 18:59
     * type : 1
     */

    private String level;
    private String msg;
    private String nickname;
    private String times;
    private String type;
    private List<?> gift;

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<?> getGift() {
        return gift;
    }

    public void setGift(List<?> gift) {
        this.gift = gift;
    }

    @Override
    public String toString() {
        return "ChatMessageBean{" +
                "level='" + level + '\'' +
                ", msg='" + msg + '\'' +
                ", nickname='" + nickname + '\'' +
                ", times='" + times + '\'' +
                ", type='" + type + '\'' +
                ", gift=" + gift +
                '}';
    }
}
