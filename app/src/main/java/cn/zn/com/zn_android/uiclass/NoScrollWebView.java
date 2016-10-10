package cn.zn.com.zn_android.uiclass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;

/**
 * Created by zjs on 2016/4/26 0026.
 * explain:
 */
public class NoScrollWebView extends X5WebView {
    public NoScrollWebView(Context context) {
        this(context, null);
    }

    public NoScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 计算webview的高度
     *
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        int expandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
