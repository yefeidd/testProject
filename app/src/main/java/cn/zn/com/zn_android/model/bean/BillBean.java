package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by zjs on 2016/12/26 0026.
 * email: m15267280642@163.com
 * explain:
 */

public class BillBean extends MessageBean{

    /**
     * data : [{"id":"2308","times":"12-23","user_id":"10015","way":"提问","wealth":"-10","week":"周五"},{"id":"2307","times":"12-23","user_id":"10015","way":"提问","wealth":"-2","week":"周五"},{"id":"2306","times":"12-23","user_id":"10015","way":"提问","wealth":"-15","week":"周五"},{"id":"2305","times":"12-23","user_id":"10015","way":"提问","wealth":"-15","week":"周五"},{"id":"2301","times":"12-23","user_id":"10015","way":"提问","wealth":"-15","week":"周五"},{"id":"2300","times":"12-23","user_id":"10015","way":"提问","wealth":"-15","week":"周五"},{"id":"2299","times":"12-23","user_id":"10015","way":"提问","wealth":"-25","week":"周五"},{"id":"2298","times":"12-23","user_id":"10015","way":"提问","wealth":"-15","week":"周五"},{"id":"2286","times":"12-22","user_id":"10015","way":"提问","wealth":"-5","week":"周四"},{"id":"2269","times":"12-21","user_id":"10015","way":"提问","wealth":"-2","week":"周三"}]
     * month : 12月
     */

    private String month; //月份
    private List<DataBean> data;

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * id : 2308  账单ID
         * times : 12-23  账单时间
         * user_id : 10015
         * way : 提问   财富币用途
         * wealth : -10 财富币支出还是收入的数量
         * week : 周五
         */

        private String id;
        private String times;
        private String user_id;
        private String way;
        private String wealth;
        private String week;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getWay() {
            return way;
        }

        public void setWay(String way) {
            this.way = way;
        }

        public String getWealth() {
            return wealth;
        }

        public void setWealth(String wealth) {
            this.wealth = wealth;
        }

        public String getWeek() {
            return week;
        }

        public void setWeek(String week) {
            this.week = week;
        }
    }
}
