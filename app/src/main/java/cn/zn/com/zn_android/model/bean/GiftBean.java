package cn.zn.com.zn_android.model.bean;

/**
 * 礼物实体类
 *
 * Created by Jolly on 2016/3/29 0029.
 */
public class GiftBean {
    private String giftName;
    private int giftWealth;
    private int imgRes;
    private boolean isSelected;

    public boolean isSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public String getGiftName() {
        return giftName;
    }

    public void setGiftName(String giftName) {
        this.giftName = giftName;
    }

    public int getGiftWealth() {
        return giftWealth;
    }

    public void setGiftWealth(int giftWealth) {
        this.giftWealth = giftWealth;
    }

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }
}
