package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/12/13.
 */

public class OrderInfoBean extends MessageBean {

    /**
     * order_title : 提问
     * remark : 万泽股份(000534)摸摸哦哦弄(⊙o⊙)哦
     * money : 2.00
     * times : 1481598310
     * status : success
     * order_num : wd_584f6566319b2647982464
     * wealth : 241170.39
     * tic_list : [{"id":"14","uid":"28","face_value":"5","status":"2","use_time":"1481530381","times":"1481273463"},{"id":"1","uid":"28","face_value":"3","status":"1","use_time":null,"times":"1458521652"}]
     */

    private String order_title;
    private String remark;
    private float money;
    private long times;
    private long sy_time;
    private String status;
    private String order_num;
    private float wealth;
    private List<TicListBean> tic_list;

    public long getSy_time() {
        return sy_time;
    }

    public void setSy_time(long sy_time) {
        this.sy_time = sy_time;
    }

    public String getOrder_title() {
        return order_title;
    }

    public void setOrder_title(String order_title) {
        this.order_title = order_title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public float getMoney() {
        return money;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public long getTimes() {
        return times;
    }

    public void setTimes(long times) {
        this.times = times;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrder_num() {
        return order_num;
    }

    public void setOrder_num(String order_num) {
        this.order_num = order_num;
    }

    public float getWealth() {
        return wealth;
    }

    public void setWealth(float wealth) {
        this.wealth = wealth;
    }

    public List<TicListBean> getTic_list() {
        return tic_list;
    }

    public void setTic_list(List<TicListBean> tic_list) {
        this.tic_list = tic_list;
    }

    public static class TicListBean {
        /**
         * id : 14
         * uid : 28
         * face_value : 5
         * status : 2
         * use_time : 1481530381
         * times : 1481273463
         */

        private String id;
        private String uid;
        private int face_value;
        private String status;
        private String use_time;
        private String times;
        private boolean isChecked = false;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }

        public int getFace_value() {
            return face_value;
        }

        public void setFace_value(int  face_value) {
            this.face_value = face_value;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getUse_time() {
            return use_time;
        }

        public void setUse_time(String use_time) {
            this.use_time = use_time;
        }

        public String getTimes() {
            return times;
        }

        public void setTimes(String times) {
            this.times = times;
        }
    }
}
