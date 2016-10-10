package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/9/23 0023.
 * email: m15267280642@163.com
 * explain:
 */

public class OtherHomeMsgBean extends MessageBean {

    /**
     * all_yl : 15.67
     * sy : 7.835
     * img : http://qiniu.98cfw.com/avatars.png
     * nickname : test111
     * code_count : 6
     * time : 2016-08-02
     * join_money : 1000000.00
     * concern_num : 0
     * attention_num : 0
     * guanzhu : 1
     */

    private String all_yl;
    private String sy;
    private String img;
    private String nickname;
    private String code_count;
    private String time;
    private String join_money;
    private String concern_num;
    private String attention_num;
    private int attention_type; // 1 未登陆 2 已登陆 已关注  3 已登陆 未关注

    public int getAttention_type() {
        return attention_type;
    }

    public void setAttention_type(int attention_type) {
        this.attention_type = attention_type;
    }

    public String getAll_yl() {
        return all_yl;
    }

    public void setAll_yl(String all_yl) {
        this.all_yl = all_yl;
    }

    public String getSy() {
        return sy;
    }

    public void setSy(String sy) {
        this.sy = sy;
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

    public String getCode_count() {
        return code_count;
    }

    public void setCode_count(String code_count) {
        this.code_count = code_count;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getJoin_money() {
        return join_money;
    }

    public void setJoin_money(String join_money) {
        this.join_money = join_money;
    }

    public String getConcern_num() {
        return concern_num;
    }

    public void setConcern_num(String concern_num) {
        this.concern_num = concern_num;
    }

    public String getAttention_num() {
        return attention_num;
    }

    public void setAttention_num(String attention_num) {
        this.attention_num = attention_num;
    }

}
