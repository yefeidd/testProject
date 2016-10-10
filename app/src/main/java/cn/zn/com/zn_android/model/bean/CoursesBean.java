package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/4/5 0005.
 */
public class CoursesBean {
//    content: "9:00-10:00 股圣老周 大盘讲解 9:00-10:00 股圣老周 大盘讲解 9:00-10:00 股圣老周 大盘讲解 9:00-10:00 股圣老周 大盘讲解 9:00-10:00 股圣老周 大盘讲解 "
    private String content;
    private String message;

    public String getContent() {
        return content;
    }

    public CoursesBean setContent(String content) {
        this.content = content;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public CoursesBean setMessage(String message) {
        this.message = message;
        return this;
    }
}
