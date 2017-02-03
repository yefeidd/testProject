package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 *
 * Created by Jolly on 2016/9/22 0022.
 */
public class StockRecordBean extends MessageBean {
    private String start_time;
    private String end_time;
    private List<StockRecordItemBean> data;

    public String getStart_time() {
        return start_time;
    }

    public StockRecordBean setStart_time(String start_time) {
        this.start_time = start_time;
        return this;
    }

    public String getEnd_time() {
        return end_time;
    }

    public StockRecordBean setEnd_time(String end_time) {
        this.end_time = end_time;
        return this;
    }

    public List<StockRecordItemBean> getData() {
        return data;
    }

    public StockRecordBean setData(List<StockRecordItemBean> data) {
        this.data = data;
        return this;
    }
}
