package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/3/31 0031.
 * explain:
 */
public class HotLiveBean {
    /**
     * avatars : http://qiniu.98cfw.com/avatars.png
     * click : 582
     * collect : 20
     * hresults : 历史业绩
     * id : 2
     * mem_presentation : 会员介绍
     * operation : 不清楚
     * ordurl : http://192.168.1.3:98/Api/Index/t_ordim?tid=9999
     * paction : 策略操作,公告
     * placard : 公告
     * room_number : 666666
     * status : 1
     * summary : 简介
     * tid : 9999
     * times : 1459818960
     * title : 房间222
     * vipurl : http://192.168.1.3:98/Api/Index/t_vipim?tid=9999
     */

    private String avatars = "";
    private String click = "";
    private String collect = "";
    private String hresults = "";
    private String id = "";
    private String mem_presentation = "";
    private String operation = "";
    private String ordurl = "";
    private String paction = "";
    private String placard = "";
    private String room_number = "";
    private String status = "";
    private String summary = "";
    private String tid = "";
    private String times = "";
    private String title = "";
    private String vipurl = "";

    public String getAvatars() {
        if (avatars == null) return "";
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getClick() {
        return click;
    }

    public void setClick(String click) {
        this.click = click;
    }

    public String getCollect() {
        return collect;
    }

    public void setCollect(String collect) {
        this.collect = collect;
    }

    public String getHresults() {
        return hresults;
    }

    public void setHresults(String hresults) {
        this.hresults = hresults;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMem_presentation() {
        return mem_presentation;
    }

    public void setMem_presentation(String mem_presentation) {
        this.mem_presentation = mem_presentation;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getOrdurl() {
        return ordurl;
    }

    public void setOrdurl(String ordurl) {
        this.ordurl = ordurl;
    }

    public String getPaction() {
        return paction;
    }

    public void setPaction(String paction) {
        this.paction = paction;
    }

    public String getPlacard() {
        return placard;
    }

    public void setPlacard(String placard) {
        this.placard = placard;
    }

    public String getRoom_number() {
        return room_number;
    }

    public void setRoom_number(String room_number) {
        this.room_number = room_number;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
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

    public String getVipurl() {
        return vipurl;
    }

    public void setVipurl(String vipurl) {
        this.vipurl = vipurl;
    }


}
