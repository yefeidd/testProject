package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class DetailFinanceBean {
    private ProfitBean profit;
    private DebtBean debt;
    private CashBean cash;
    private String date;

    public ProfitBean getProfit() {
        return profit;
    }

    public void setProfit(ProfitBean profit) {
        this.profit = profit;
    }

    public DebtBean getDebt() {
        return debt;
    }

    public void setDebt(DebtBean debt) {
        this.debt = debt;
    }

    public CashBean getCash() {
        return cash;
    }

    public void setCash(CashBean cash) {
        this.cash = cash;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
