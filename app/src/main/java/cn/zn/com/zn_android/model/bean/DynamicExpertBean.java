package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/9/18 0018.
 */
public class DynamicExpertBean {
    /**
     * attentionType : 1
     * avatars :
     * first_time : 1473823380
     * mon_income : 0.66
     * nickname :
     * profit : 24.89
     * user_id : 10
     * week_income : 24.89
     * win_rate : 3
     */

    private String attentionType; //  1 未登陆 2 已登陆 已关注  3 已登陆 未关注
    private String avatars;
    private String first_time;
    private String mon_income;
    private String nickname;
    private String profit;
    private String user_id;
    private String week_income;
    private String win_rate;
    /**
     * totalmoney : 1243772.98
     * ranking : 1
     */

    private String totalmoney;
    private String ranking;
    /**
     * now_position : 2.13
     */

    private String now_position;


    public String getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(String attentionType) {
        this.attentionType = attentionType;
    }

    public String getAvatars() {
        return avatars;
    }

    public void setAvatars(String avatars) {
        this.avatars = avatars;
    }

    public String getFirst_time() {
        return first_time;
    }

    public void setFirst_time(String first_time) {
        this.first_time = first_time;
    }

    public String getMon_income() {
        return mon_income;
    }

    public void setMon_income(String mon_income) {
        this.mon_income = mon_income;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getProfit() {
        return profit;
    }

    public void setProfit(String profit) {
        this.profit = profit;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getWeek_income() {
        return week_income;
    }

    public void setWeek_income(String week_income) {
        this.week_income = week_income;
    }

    public String getWin_rate() {
        return win_rate;
    }

    public void setWin_rate(String win_rate) {
        this.win_rate = win_rate;
    }

    public String getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getNow_position() {
        return now_position;
    }

    public void setNow_position(String now_position) {
        this.now_position = now_position;
    }
}
