package cn.zn.com.zn_android.model.bean;

import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.modelMole.MarketImp;

import java.util.List;

/**
 * Created by Jolly on 2016/6/29 0029.
 */
public class StocksBean {
    private List<MarketImp> beanList;
    private String type;
    private int mStockType = Constants.SH;//0代表深交所，1代表港交所

    public int getmStockType() {
        return mStockType;
    }

    public void setmStockType(int mStockType) {
        this.mStockType = mStockType;
    }

    public List<MarketImp> getBeanList() {
        return beanList;
    }

    public void setBeanList(List<MarketImp> beanList) {
        this.beanList = beanList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
