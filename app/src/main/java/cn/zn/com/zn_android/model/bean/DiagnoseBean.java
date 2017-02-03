package cn.zn.com.zn_android.model.bean;

/**
 * Created by Jolly on 2016/11/30.
 */

public class DiagnoseBean {
    private int loan;
    private boolean isChecked = false;

    public int getLoan() {
        return loan;
    }

    public void setLoan(int loan) {
        this.loan = loan;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
