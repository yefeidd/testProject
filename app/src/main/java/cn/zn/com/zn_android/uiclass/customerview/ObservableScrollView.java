package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ScrollView;

import cn.zn.com.zn_android.uiclass.xlistview.XScrollView;

/**
 * 监听滑动事件的ScrollView
 */
public class ObservableScrollView extends XScrollView {
    private boolean isTop = false;
    private OnScrollToBottomListener onScrollToBottom;
    private ObservableScrollListener listener;
    private View contentView;
    public ObservableScrollView(Context context) {
        super(context);
    }

    public ObservableScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ObservableScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }
    public interface OnScrollToBottomListener{
        void onScrollBottomListener(boolean isBottom);
    }
    public void setOnScrollToBottomLintener(OnScrollToBottomListener listener){
        onScrollToBottom = listener;
    }
    public void setOnScrollListener(ObservableScrollListener listener) {
        this.listener = listener;
    }
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        //scrollView只有一个view
        if(getChildCount()>0){
            contentView=getChildAt(0);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(contentView==null) return;
    }
    @Override
    protected void onScrollChanged(int x, int y, int oldx, int oldy) {
        super.onScrollChanged(x, y, oldx, oldy);
        if (y == 0) {
            isTop = true;
        } else {
            isTop = false;
        }
        if (listener != null) {
            listener.onScrollChanged(x, y, oldx, oldy);
        }
        if(getIsBottom()) {
            onScrollToBottom.onScrollBottomListener(true);
        }
    }

    @Override
    protected void onOverScrolled(int scrollX, int scrollY, boolean clampedX, boolean clampedY) {
        super.onOverScrolled(scrollX, scrollY, clampedX, clampedY);
        if (scrollY!=0) {
            if(getIsBottom()) {
                onScrollToBottom.onScrollBottomListener(clampedY);
            }
        } else {
            if (isTop) {

            }
        }
    }

    private boolean getIsBottom() {
        //内容的高度小于或等于scrollView的高度加与顶部的距离
        return contentView.getHeight()<=getHeight()+getScrollY();
    }
}
