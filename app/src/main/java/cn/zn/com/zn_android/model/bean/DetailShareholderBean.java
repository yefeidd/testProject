package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class DetailShareholderBean {
    private EquityBean equity;
    private List<TenShBean> ten_sh;

    public EquityBean getEquity() {
        return equity;
    }

    public void setEquity(EquityBean equity) {
        this.equity = equity;
    }

    public List<TenShBean> getTen_sh() {
        return ten_sh;
    }

    public void setTen_sh(List<TenShBean> ten_sh) {
        this.ten_sh = ten_sh;
    }
}
