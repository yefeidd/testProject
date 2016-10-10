package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

/**
 * 监听滚动到底部的ListView
 *
 * Created by WJL on 2016/3/11 0011 10:57.
 */
public class LoadMoreListView extends ListView implements AbsListView.OnScrollListener {
    private int getLastVisiblePosition = 0, lastVisiblePositionY = 0;

    private LoadMoreListener loadMoreListener;
    public interface LoadMoreListener {
        void loadMore();
    }

    public LoadMoreListView(Context context) {
        this(context, null);
    }

    public LoadMoreListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setOnScrollListener(this);
    }

    public void setLoadMoreListener(LoadMoreListener loadMoreListener) {
        this.loadMoreListener = loadMoreListener;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
            //滚动到底部
            if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
                View v = (View) view.getChildAt(view.getChildCount() - 1);
                int[] location = new int[2];
                v.getLocationOnScreen(location);//获取在整个屏幕内的绝对坐标
                int y = location[1];
                //第一次到底部
                if (view.getLastVisiblePosition() != getLastVisiblePosition
                        && lastVisiblePositionY != y) {
                    getLastVisiblePosition = view.getLastVisiblePosition();
                    lastVisiblePositionY = y;
                    loadMoreListener.loadMore();
                    return;
                    //第二次到底部
                } else if (view.getLastVisiblePosition() == getLastVisiblePosition
                        && lastVisiblePositionY == y) {
                    loadMoreListener.loadMore();
                }
            }
            //未滚动到底部，第二次拖至底部都初始化
            getLastVisiblePosition = 0;
            lastVisiblePositionY = 0;
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {

    }
}
