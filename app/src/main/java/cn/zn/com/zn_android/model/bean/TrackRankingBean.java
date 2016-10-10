package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/9/24 0024.
 * email: m15267280642@163.com
 * explain:
 */

public class TrackRankingBean {

    /**
     * attentionType : 1
     * nickname :
     * profit : 24.89
     * ranking : 1
     * totalmoney : 1248884.98
     * user_id : 10
     */

    private int attentionType;
    private String nickname;
    private String profit;
    private String ranking;
    private String totalmoney;
    private String user_id;

    public int getAttentionType() {
        return attentionType;
    }

    public void setAttentionType(int attentionType) {
        this.attentionType = attentionType;
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

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public String getTotalmoney() {
        return totalmoney;
    }

    public void setTotalmoney(String totalmoney) {
        this.totalmoney = totalmoney;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
