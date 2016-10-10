package cn.zn.com.zn_android.model.bean;

/**
 * 成分股
 * Created by Jolly on 2016/8/25 0025.
 */
public class ConstituentStockBean {

    /**
     * code : 00027
     * price : 26.500
     * change : 2.64
     * money : 6.48亿
     * Volume : 24671845
     * name : 银河娱乐
     */

    private String code;
    private String price;
    private double change;
    private String money;
    private int Volume;
    private String name;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public int getVolume() {
        return Volume;
    }

    public void setVolume(int Volume) {
        this.Volume = Volume;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
