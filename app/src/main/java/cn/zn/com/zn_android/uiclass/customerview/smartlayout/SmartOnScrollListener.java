package cn.zn.com.zn_android.uiclass.customerview.smartlayout;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.AbsListView;


/**
 * fab浮动的滑动监听类
 */
public class SmartOnScrollListener implements AbsListView.OnScrollListener {
    private int mLastScrollY;
    private int mPreviousFirstVisibleItem;
    private AbsListView mListView;
    private int mScrollThreshold;

    private boolean state = true;

    private SmartLinearLayout linearLayout;

    public SmartOnScrollListener(SmartLinearLayout linearLayout) {
        this.linearLayout = linearLayout;
    }

    private void showFab() {
        if (state) {
            linearLayout.show();
        }
    }

    private void hideFab() {
        if (state) {
            linearLayout.hide();
        }
    }

    /**
     * 开启/关闭    fab的动作
     */
    public void switchFabState(boolean state) {
        this.state = state;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount == 0) return;
        if (isSameRow(firstVisibleItem)) {
            int newScrollY = getTopItemScrollY();
            boolean isSignificantDelta = Math.abs(mLastScrollY - newScrollY) > mScrollThreshold;
            if (isSignificantDelta) {
                if (mLastScrollY > newScrollY) {
                    hideFab();
                } else {
                    showFab();
                }
            }
            mLastScrollY = newScrollY;
        } else {
            if (firstVisibleItem > mPreviousFirstVisibleItem) {
                hideFab();
            } else {
                showFab();
            }

            mLastScrollY = getTopItemScrollY();
            mPreviousFirstVisibleItem = firstVisibleItem;
        }
    }

    public void setListView(@NonNull AbsListView listView) {
        mListView = listView;
    }

    private boolean isSameRow(int firstVisibleItem) {
        return firstVisibleItem == mPreviousFirstVisibleItem;
    }

    private int getTopItemScrollY() {
        if (mListView == null || mListView.getChildAt(0) == null) return 0;
        View topChild = mListView.getChildAt(0);
        return topChild.getTop();
    }
}
