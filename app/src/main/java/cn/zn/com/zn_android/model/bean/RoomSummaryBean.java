package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * 老师房间简介
 *
 * Created by Jolly on 2016/4/12 0012.
 */
public class RoomSummaryBean {
    private List<GiftInfoBean> gift_list;
    private RoomInfoBean room_info;

    public List<GiftInfoBean> getGift_list() {
        return gift_list;
    }

    public void setGift_list(List<GiftInfoBean> gift_list) {
        this.gift_list = gift_list;
    }

    public RoomInfoBean getRoom_info() {
        return room_info;
    }

    public void setRoom_info(RoomInfoBean room_info) {
        this.room_info = room_info;
    }
}
