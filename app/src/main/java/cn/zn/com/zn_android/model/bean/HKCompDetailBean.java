package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/9/5 0005.
 */
public class HKCompDetailBean {

    /**
     * tic_name : 远东控股国际有限公司
     * list_date : 1973-02-12
     * hy_name : 服装
     */

    private TicInfoEntity tic_info;
    /**
     * PUBLISH_DATE : 2014
     * EX_DATE : 2014-09-11
     * BONUS_PRICE : 派息0.1港元
     */

    private List<BonusListEntity> bonus_list;
    /**
     * END_DATE : 2014-12-31
     * SH_NAME : Active Dynamic Limited
     * HOLD_VOL : 2.68亿
     */

    private List<TenShBean> holder;

    public TicInfoEntity getTic_info() {
        return tic_info;
    }

    public void setTic_info(TicInfoEntity tic_info) {
        this.tic_info = tic_info;
    }

    public List<BonusListEntity> getBonus_list() {
        return bonus_list;
    }

    public void setBonus_list(List<BonusListEntity> bonus_list) {
        this.bonus_list = bonus_list;
    }

    public List<TenShBean> getHolder() {
        return holder;
    }

    public void setHolder(List<TenShBean> holder) {
        this.holder = holder;
    }

    public static class TicInfoEntity {
        private String tic_name;
        private String list_date;
        private String hy_name;

        public String getTic_name() {
            return tic_name;
        }

        public void setTic_name(String tic_name) {
            this.tic_name = tic_name;
        }

        public String getList_date() {
            return list_date;
        }

        public void setList_date(String list_date) {
            this.list_date = list_date;
        }

        public String getHy_name() {
            return hy_name;
        }

        public void setHy_name(String hy_name) {
            this.hy_name = hy_name;
        }
    }

    public static class BonusListEntity {
        private String PUBLISH_DATE;
        private String EX_DATE;
        private String BONUS_PRICE;

        public String getPUBLISH_DATE() {
            return PUBLISH_DATE;
        }

        public void setPUBLISH_DATE(String PUBLISH_DATE) {
            this.PUBLISH_DATE = PUBLISH_DATE;
        }

        public String getEX_DATE() {
            return EX_DATE;
        }

        public void setEX_DATE(String EX_DATE) {
            this.EX_DATE = EX_DATE;
        }

        public String getBONUS_PRICE() {
            return BONUS_PRICE;
        }

        public void setBONUS_PRICE(String BONUS_PRICE) {
            this.BONUS_PRICE = BONUS_PRICE;
        }
    }

    public static class HolderEntity {
        private String END_DATE;
        private String SH_NAME;
        private String HOLD_VOL;

        public String getEND_DATE() {
            return END_DATE;
        }

        public void setEND_DATE(String END_DATE) {
            this.END_DATE = END_DATE;
        }

        public String getSH_NAME() {
            return SH_NAME;
        }

        public void setSH_NAME(String SH_NAME) {
            this.SH_NAME = SH_NAME;
        }

        public String getHOLD_VOL() {
            return HOLD_VOL;
        }

        public void setHOLD_VOL(String HOLD_VOL) {
            this.HOLD_VOL = HOLD_VOL;
        }
    }
}
