package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/6/3 0003.
 */
public class GiftMsgBean {

    /**
     * imgurl : /Public/images/gifts/g14.gif
     * name : 游轮
     * num : 1
     * index: 1
     */

    private String imgurl;
    private String name;
    private String num;
    private String index;

    public String getIndex() {
        return index;
    }

    public GiftMsgBean setIndex(String index) {
        this.index = index;
        return this;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }
}
