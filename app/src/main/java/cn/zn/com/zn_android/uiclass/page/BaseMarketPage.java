package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by zjs on 2016/6/27 0027.
 * email: m15267280642@163.com
 * explain: 行情页面的基类
 */
public class BaseMarketPage extends BasePage {

    private TextView mTv;

    public BaseMarketPage(Activity activity) {
        super(activity);
    }

    @Override
    public View initView() {
        return super.initView();
    }

    @Override
    public void initData() {
        mTv = new TextView(mActivity);
        if (flContent != null) {
            flContent.removeAllViews();
        }
        flContent.addView(mTv);
    }

    /**
     * 设置板块标题
     *
     * @param str
     */
    public void setText(String str) {
        this.mTv.setText(str);
    }
}
