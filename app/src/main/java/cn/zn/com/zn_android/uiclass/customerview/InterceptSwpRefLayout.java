package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * 横向滑动距离>mTouchSlop 表示触发了滑动事件
 * 然后就响应将滑动事件的处理交给子view处理 否则就拦截事件
 * Created by white on 2015/11/12.
 */
public class InterceptSwpRefLayout extends SwipeRefreshLayout {

    private int mTouchSlop;//触发滑动事件的最小距离
    private int mLastX;

    public InterceptSwpRefLayout(Context context) {
        super(context);
    }

    public InterceptSwpRefLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastX = (int) ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                int eventX = (int) ev.getX();
                int dealX = Math.abs(eventX - mLastX);

                if (dealX > mTouchSlop) {
                    return false;
                }

                break;
        }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
