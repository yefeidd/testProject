package cn.zn.com.zn_android.model.bean;

import java.util.List;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class DetailIntroductBean {
    private IntroductionsBean introductions;
    private List<BonusBean> bonus;

    public IntroductionsBean getIntroductions() {
        return introductions;
    }

    public void setIntroductions(IntroductionsBean introductions) {
        this.introductions = introductions;
    }

    public List<BonusBean> getBonus() {
        return bonus;
    }

    public void setBonus(List<BonusBean> bonus) {
        this.bonus = bonus;
    }
}
