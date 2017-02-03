package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/12/21.
 */

public class MyAskAnswerBean extends MessageBean {

    /**
     * question : 安徽水利(600502)16这只股票怎么样，能不能让我发家致富
     * teacher_name :
     * headimg : http://qiniu.98cfw.com/avatars.png
     * answer : 阿斯达所大所大所大所大所大所大所大所大所大所所所所所所所所所所所所所阿斯达所大所大所大所大所大所大所大所大所大所所所所所所所所所所所所所阿斯达所大所大所大所大所大所大所大所大所大所所所所所所所所所所所所所
     * htime : 12-13 10:20
     * type : 1
     * ask_id : 16
     */

    private String question;
    private String teacher_name;
    private String headimg;
    private String answer;
    private String htime;
    private int type; // 1:未评价，2：已评价，3：问题作废
    private String ask_id;
    private String tid;

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getHeadimg() {
        return headimg;
    }

    public void setHeadimg(String headimg) {
        this.headimg = headimg;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getHtime() {
        return htime;
    }

    public void setHtime(String htime) {
        this.htime = htime;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getAsk_id() {
        return ask_id;
    }

    public void setAsk_id(String ask_id) {
        this.ask_id = ask_id;
    }
}
