package cn.zn.com.zn_android.model.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jolly on 2016/5/18 0018.
 */
public class CSQQBean extends MessageBean {
    private List<String> qqlist = new ArrayList<>();

    public List<String> getQqlist() {
        return qqlist;
    }

    public CSQQBean setQqlist(List<String> qqlist) {
        this.qqlist = qqlist;
        return this;
    }
}
