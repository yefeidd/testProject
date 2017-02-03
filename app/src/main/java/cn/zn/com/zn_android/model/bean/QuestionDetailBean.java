package cn.zn.com.zn_android.model.bean;

/**
 * Created by zjs on 2016/12/15 0015.
 * email: m15267280642@163.com
 * explain:问详情的Bean
 */

public class QuestionDetailBean {

    /**
     * pdescription : 哈哈哈哈哈哈，
     * scode : 002624
     * scosts : 20.00
     * sname : 完美世界
     */

    private String pdescription;
    private String scode;
    private String scosts;
    private String sname;

    public void setMessage(String message) {
        this.message = message;
    }

    private String message;

    /**
     * name : 阳光明媚
     * question : 完美世界(002624)哈哈哈哈哈哈，
     * start : 1
     * time : 12-15 11:06
     */

    private String name;
    private String question;
    private String start;
    private String time;


    public String getMessage() {
        return message;
    }

    public String getPdescription() {
        return pdescription;
    }

    public void setPdescription(String pdescription) {
        this.pdescription = pdescription;
    }

    public String getScode() {
        return scode;
    }

    public void setScode(String scode) {
        this.scode = scode;
    }

    public String getScosts() {
        return scosts;
    }

    public void setScosts(String scosts) {
        this.scosts = scosts;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
