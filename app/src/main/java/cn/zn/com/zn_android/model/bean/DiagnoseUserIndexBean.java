package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * 诊股大厅用户首页
 *
 * Created by Jolly on 2016/12/15.
 */

public class DiagnoseUserIndexBean {

    private List<QustionRollBean> ques_list;
    private List<TeacherListBean> teacher_list;

    public List<QustionRollBean> getQues_list() {
        return ques_list;
    }

    public void setQues_list(List<QustionRollBean> ques_list) {
        this.ques_list = ques_list;
    }

    public List<TeacherListBean> getTeacher_list() {
        return teacher_list;
    }

    public void setTeacher_list(List<TeacherListBean> teacher_list) {
        this.teacher_list = teacher_list;
    }

    public static class TeacherListBean {
        /**
         * avatars : http://qiniu.98cfw.com/2016-07-20_09:16:09M8ZCct1w
         * nickname : 翻翻翻翻翻翻
         * month_num : 2
         */

        private String avatars;
        private String nickname;
        private int month_num;
        private int star_num;
        private String tid;

        public int getStar_num() {
            return star_num;
        }

        public void setStar_num(int star_num) {
            this.star_num = star_num;
        }

        public String getTid() {
            return tid;
        }

        public void setTid(String tid) {
            this.tid = tid;
        }

        public String getAvatars() {
            return avatars;
        }

        public void setAvatars(String avatars) {
            this.avatars = avatars;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getMonth_num() {
            return month_num;
        }

        public void setMonth_num(int month_num) {
            this.month_num = month_num;
        }
    }
}
