package cn.zn.com.zn_android.model.bean;

/**
 * 礼物实体类
 *
 * Created by Jolly on 2016/4/12 0012.
 */
public class GiftInfoBean {

    /**
     * gid : 1
     * img : /Public/images/gifts/g1.gif
     * nickname : 代建华
     * times : 2016-04-11
     * title : 鲜花
     */

    private int gid;
    private String img;
    private String nickname;
    private String times;
    private String title;

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
