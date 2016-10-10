package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/4/6 0006.
 */
public class RoomBean {

    /**
     * avatars : http://qiniu.98cfw.com/avatars.png
     * id : 1
     * room_number : 888888
     * summary : 房间简介房间简介房间简介
     * times : 2016-04-05 09:15:59
     * title : 房间名称
     * url : http://192.168.1.3:98/Api/Index/t_ordim?id=888888
     */

    private String avatars;
    private String id;
    private String room_number;
    private String summary;
    private String times;
    private String title;
    private String url;

    /**
     * click : 828
     * coll_id : 9
     * collect : 45
     * ordurl  : http://192.168.1.3:98/Api/Index/t_ordim?tid=9999
     * placard : 公告
     * tid : 9999
     * vipurl  : http://192.168.1.3:98/Api/Index/t_vipim?tid=9999
     */

    private String click;
    private String coll_id;
    private String collect;
    private String ordurl;
    private String placard;
    private String tid;
    private String vipurl;


    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getColl_id() {
        return coll_id;
    }

    public void setColl_id(String coll_id) {
        this.coll_id = coll_id;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getOrdurl() {
        return ordurl;
    }

    public void setOrdurl(String ordurl) {
        this.ordurl = ordurl;
    }

    public String getPlacard() {
        return placard;
    }

    public void setPlacard(String placard) {
        this.placard = placard;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getVipurl() {
        return vipurl;
    }

    public void setVipurl(String vipurl) {
        this.vipurl = vipurl;
    }
}
