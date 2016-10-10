package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/6/3 0003.
 */
public class ChatMsgBean {

    /**
     * avatars : http://qiniu.98cfw.com/avatars.png
     * gift : []
     * level : 小白
     * memid : 10015
     * msg : 111
     * nickname : 328081
     * shijp : 0
     * shuohr : 0
     * times : 14:33
     * type : 1
     */
    private String avatars;
    private String level;
    private String memid;
    private String msg;
    private String nickname;
    private int shijp;
    private int shuohr;
    private String times;
    /**
     * 1：消息；2：礼物
     */
    private int type;
    private GiftMsgBean gift;

    /**
     * content : rrrrrrrrrr
     * giftid : 0
     * giftnum : 0
     * id : 995
     * isreply : 0
     * isvip : 0
     * jiepan : 0
     * platform : 1
     * tid : 9898
     * uid : 28
     * vabout : 0
     */
    private String content;
    private String giftid;
    private String giftnum;
    private String id;
    private String isreply; // 0：普通，1：回复
    private String isvip;
    private String jiepan;
    private String platform;
    private String tid;
    private String uid;
    private String vabout;

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getMemid() {
        return memid;
    }

    public void setMemid(String memid) {
        this.memid = memid;
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

    public int getShijp() {
        return shijp;
    }

    public void setShijp(int shijp) {
        this.shijp = shijp;
    }

    public int getShuohr() {
        return shuohr;
    }

    public void setShuohr(int shuohr) {
        this.shuohr = shuohr;
    }

    public String getTimes() {
        return times;
    }

    public void setTimes(String times) {
        this.times = times;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public GiftMsgBean getGift() {
        return gift;
    }

    public void setGift(GiftMsgBean gift) {
        this.gift = gift;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getGiftid() {
        return giftid;
    }

    public void setGiftid(String giftid) {
        this.giftid = giftid;
    }

    public String getGiftnum() {
        return giftnum;
    }

    public void setGiftnum(String giftnum) {
        this.giftnum = giftnum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsreply() {
        return isreply;
    }

    public void setIsreply(String isreply) {
        this.isreply = isreply;
    }

    public String getIsvip() {
        return isvip;
    }

    public void setIsvip(String isvip) {
        this.isvip = isvip;
    }

    public String getJiepan() {
        return jiepan;
    }

    public void setJiepan(String jiepan) {
        this.jiepan = jiepan;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getVabout() {
        return vabout;
    }

    public void setVabout(String vabout) {
        this.vabout = vabout;
    }
}
