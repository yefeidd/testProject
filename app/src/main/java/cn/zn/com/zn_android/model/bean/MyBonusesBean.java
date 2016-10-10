package cn.zn.com.zn_android.model.bean;

import cn.zn.com.zn_android.model.modelMole.MarketImp;

/**
 * Created by Jolly on 2016/7/5 0005.
 */
public class MyBonusesBean implements MarketImp {
    private String name;

    public MyBonusesBean(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
