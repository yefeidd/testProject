package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/9/21 0021.
 */
public class ImitateFryBean {

    /**
     * usable_money : 907043.62
     * initial_money : 1000000.00
     * total_money : 1026881.62
     * today_win : 11000.00
     * mon_ave_trans : 7.00
     * win_rate : 17
     * average_position : 75.23
     * all_income : 2.69
     * net_worth : 1.0269
     * mon_income : -0.02
     * now_position : 11.67
     * line_chart : {"start_time":"2016-08-02","end_time":"2016-09-20","list":["2.71","2.69"]}
     * first_trans : 2016-09-14
     * last_trans : 2016-09-21
     * ranking : 135
     */

    private String usable_money; // 可用资产
    private String initial_money; // 初始资金
    private String total_money; // 总资产
    private String today_win; // 今日盈利
    private String mon_ave_trans; // 月均交易
    private String win_rate; // 胜率
    private String average_position; // 平均持股
    private String all_income; // 总收益
    private String net_worth; // 净值
    private String mon_income; // 月收益
    private String now_position; // 当前仓位
    /**
     * start_time : 2016-08-02
     * end_time : 2016-09-20
     * list : ["2.71","2.69"]
     */

    private LineChartEntity line_chart; // 折线图
    private String first_trans; // 初次交易
    private String last_trans; // 最近交易
    private String ranking; // 排名

    public String getUsable_money() {
        return usable_money;
    }

    public void setUsable_money(String usable_money) {
        this.usable_money = usable_money;
    }

    public String getInitial_money() {
        return initial_money;
    }

    public void setInitial_money(String initial_money) {
        this.initial_money = initial_money;
    }

    public String getTotal_money() {
        return total_money;
    }

    public void setTotal_money(String total_money) {
        this.total_money = total_money;
    }

    public String getToday_win() {
        return today_win;
    }

    public void setToday_win(String today_win) {
        this.today_win = today_win;
    }

    public String getMon_ave_trans() {
        return mon_ave_trans;
    }

    public void setMon_ave_trans(String mon_ave_trans) {
        this.mon_ave_trans = mon_ave_trans;
    }

    public String getWin_rate() {
        return win_rate;
    }

    public void setWin_rate(String win_rate) {
        this.win_rate = win_rate;
    }

    public String getAverage_position() {
        return average_position;
    }

    public void setAverage_position(String average_position) {
        this.average_position = average_position;
    }

    public String getAll_income() {
        return all_income;
    }

    public void setAll_income(String all_income) {
        this.all_income = all_income;
    }

    public String getNet_worth() {
        return net_worth;
    }

    public void setNet_worth(String net_worth) {
        this.net_worth = net_worth;
    }

    public String getMon_income() {
        return mon_income;
    }

    public void setMon_income(String mon_income) {
        this.mon_income = mon_income;
    }

    public String getNow_position() {
        return now_position;
    }

    public void setNow_position(String now_position) {
        this.now_position = now_position;
    }

    public LineChartEntity getLine_chart() {
        return line_chart;
    }

    public void setLine_chart(LineChartEntity line_chart) {
        this.line_chart = line_chart;
    }

    public String getFirst_trans() {
        return first_trans;
    }

    public void setFirst_trans(String first_trans) {
        this.first_trans = first_trans;
    }

    public String getLast_trans() {
        return last_trans;
    }

    public void setLast_trans(String last_trans) {
        this.last_trans = last_trans;
    }

    public String getRanking() {
        return ranking;
    }

    public void setRanking(String ranking) {
        this.ranking = ranking;
    }

    public static class LineChartEntity {
        private String start_time;
        private String end_time;
        private List<Float> list;

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public List<Float> getList() {
            return list;
        }

        public void setList(List<Float> list) {
            this.list = list;
        }
    }
}
