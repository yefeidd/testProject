package cn.zn.com.zn_android.uiclass.listener;

import cn.zn.com.zn_android.model.chartBean.ChartImp;

/**
 * Created by zjs on 2016/8/18 0018.
 * email: m15267280642@163.com
 * explain:
 */
public interface MyChartListener {

    /**
     * 监听高亮线的移动
     *
     * @param chartBean 数据角标
     */
    void move(ChartImp chartBean);

    /**
     * 取消高亮移动
     */
    void cancelMove();
}
