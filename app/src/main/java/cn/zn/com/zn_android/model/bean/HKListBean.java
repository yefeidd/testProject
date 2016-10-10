package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/8/24 0024.
 */
public class HKListBean {
    private List<HKRankListBean> mian_up;
    private List<HKRankListBean> mian_down;
    private List<HKRankListBean> mian_money;
    private List<HKRankListBean> new_up;
    private List<HKRankListBean> new_down;
    private List<HKRankListBean> new_money;

    public List<HKRankListBean> getMian_up() {
        return mian_up;
    }

    public void setMian_up(List<HKRankListBean> mian_up) {
        this.mian_up = mian_up;
    }

    public List<HKRankListBean> getMian_down() {
        return mian_down;
    }

    public void setMian_down(List<HKRankListBean> mian_down) {
        this.mian_down = mian_down;
    }

    public List<HKRankListBean> getMian_money() {
        return mian_money;
    }

    public void setMian_money(List<HKRankListBean> mian_money) {
        this.mian_money = mian_money;
    }

    public List<HKRankListBean> getNew_up() {
        return new_up;
    }

    public void setNew_up(List<HKRankListBean> new_up) {
        this.new_up = new_up;
    }

    public List<HKRankListBean> getNew_down() {
        return new_down;
    }

    public void setNew_down(List<HKRankListBean> new_down) {
        this.new_down = new_down;
    }

    public List<HKRankListBean> getNew_money() {
        return new_money;
    }

    public void setNew_money(List<HKRankListBean> new_money) {
        this.new_money = new_money;
    }
}
