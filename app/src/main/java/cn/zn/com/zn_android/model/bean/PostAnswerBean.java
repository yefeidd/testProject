package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/12/19 0019.
 * email: m15267280642@163.com
 * explain: 提交回答的bean类
 */

public class PostAnswerBean {
    private int strend;     // strend 短线趋势 1-5
    private int mtrend;     // mtrend 中线趋势 1-5
    private String tranalysis; // tanalysis 技术分析
    private String fanalysis;  // fanalysis 基本面分析
    private String supplement; // supplement 补充（可传可不传）
    private String img1;       // 传的图片
    private String img2;
    private String img3;
    private String id;         // id问题id
    private String from;       // from 1 安卓 2 IOS

    private String ask;
    private String nickName;
    private String star;
    private String time;

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getStrend() {
        return strend;
    }

    public void setStrend(int strend) {
        this.strend = strend;
    }

    public int getMtrend() {
        return mtrend;
    }

    public void setMtrend(int mtrend) {
        this.mtrend = mtrend;
    }

    public String getTranalysis() {
        return tranalysis;
    }

    public void setTranalysis(String tranalysis) {
        this.tranalysis = tranalysis;
    }

    public String getFanalysis() {
        return fanalysis;
    }

    public void setFanalysis(String fanalysis) {
        this.fanalysis = fanalysis;
    }

    public String getSupplement() {
        return supplement;
    }

    public void setSupplement(String supplement) {
        this.supplement = supplement;
    }

    public String getImg1() {
        return img1;
    }

    public void setImg1(String img1) {
        this.img1 = img1;
    }

    public String getImg2() {
        return img2;
    }

    public void setImg2(String img2) {
        this.img2 = img2;
    }

    public String getImg3() {
        return img3;
    }

    public void setImg3(String img3) {
        this.img3 = img3;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
