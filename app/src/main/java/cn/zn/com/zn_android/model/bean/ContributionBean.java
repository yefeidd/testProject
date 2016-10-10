package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by zjs on 2016/4/5 0005.
 * explain:
 */
public class ContributionBean {

    /**
     * fraction : 10000
     * nickname : QQ
     */

    private List<MonthBean> month;
    /**
     * fraction : 1000000
     * nickname : FSSSSS
     */

    private List<TotalBean> total;

    public List<MonthBean> getMonth() {
        return month;
    }

    public void setMonth(List<MonthBean> month) {
        this.month = month;
    }

    public List<TotalBean> getTotal() {
        return total;
    }

    public void setTotal(List<TotalBean> total) {
        this.total = total;
    }

    public static class MonthBean extends BaseContribution {
    }

    public static class TotalBean extends BaseContribution {
    }
}
