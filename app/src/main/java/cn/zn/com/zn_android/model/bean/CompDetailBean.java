package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class CompDetailBean extends MessageBean {
    private DetailIntroductBean introduct;
    private DetailFinanceBean finance;
    private DetailShareholderBean shareholder;

    public DetailIntroductBean getIntroduct() {
        return introduct;
    }

    public void setIntroduct(DetailIntroductBean introduct) {
        this.introduct = introduct;
    }

    public DetailFinanceBean getFinance() {
        return finance;
    }

    public void setFinance(DetailFinanceBean finance) {
        this.finance = finance;
    }

    public DetailShareholderBean getShareholder() {
        return shareholder;
    }

    public void setShareholder(DetailShareholderBean shareholder) {
        this.shareholder = shareholder;
    }
}
