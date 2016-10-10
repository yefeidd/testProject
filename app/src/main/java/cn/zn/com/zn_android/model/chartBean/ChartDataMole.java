package cn.zn.com.zn_android.model.chartBean;

import cn.zn.com.zn_android.model.bean.MessageBean;

import java.util.ArrayList;

/**
 * Created by zjs on 2016/8/24 0024.
 * email: m15267280642@163.com
 * explain:
 */
public interface ChartDataMole {
    void requestData();

    ArrayList<MessageBean> getData();
}
