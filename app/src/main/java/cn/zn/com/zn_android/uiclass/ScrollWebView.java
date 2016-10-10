package cn.zn.com.zn_android.uiclass;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by Jolly on 2016/4/21 0021.
 */
public class ScrollWebView extends WebView {

    public interface OnScrollListner {
        void scrollChange(int l, int t, int oldl, int oldt);

        void onBottom();
    }

    public OnScrollListner mListner;

    public ScrollWebView setmListner(OnScrollListner mListner) {
        this.mListner = mListner;
        return this;
    }

    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ScrollWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (mListner != null) {
            if (scrollY != 0) {
                if (getIsBottom() && clampedY) {
                    mListner.onBottom();
                }
            }
        }

    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (mListner != null) {
            mListner.scrollChange(l, t, oldl, oldt);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
    }


    private boolean getIsBottom() {
        //内容的高度小于或等于scrollView的高度加与顶部的距离
        return this.getHeight() <= getHeight() + getScrollY();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
