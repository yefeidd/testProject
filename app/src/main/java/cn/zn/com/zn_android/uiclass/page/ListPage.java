package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.widget.BaseAdapter;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;

/**
 * Created by zjs on 2016/3/25 0025.
 */
public class ListPage extends BasePage {

    private XListView list;
    private BaseAdapter adapter;
    private IXListViewListener mListener;
    private boolean loadMoreEnable = false;

    /**
     * 初始化Ranking_list_pager
     *
     * @param activity
     */
    public ListPage(Activity activity) {
        super(activity);
        list = new XListView(mActivity);
    }

    /**
     * 设置数据
     */
    @Override
    public void initData() {
        list.setAdapter(adapter);
        if (flContent != null) {
            flContent.removeAllViews();
        }
        //设置默认值为禁止
        list.setHeaderDividersEnabled(false);
        list.setFooterDividersEnabled(false);
        list.setPullRefreshEnable(false);
        list.setPullLoadEnable(false);
        list.setLoadMoreEnable(false);
        list.setSelector(R.drawable.sp_rect_trans_bg);
        list.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mListener.onRefresh();

            }

            @Override
            public void onLoadMore() {
                mListener.onLoadMore();
            }
        });
        flContent.addView(list);
    }

    /**
     * 为listview设置适配器
     */
    public void setAdapter(BaseAdapter adapter) {
        this.adapter = adapter;
    }

    /**
     * 设置是否显示加载更多控件
     */
    public void setLoadMoreEnable(boolean loadMoreEnable) {
        list.setLoadMoreEnable(loadMoreEnable);
    }


    public void setPullRefreshEnable(boolean enable) {
        list.setPullRefreshEnable(enable);
    }

    public void setPullLoadEnable(boolean enable) {
        list.setPullLoadEnable(enable);
    }

    public void stopRefresh() {
        list.stopRefresh();
    }

    public void stopLoadMore() {
        list.stopLoadMore();
    }

    public void setXListViewListener(IXListViewListener listener) {
        mListener = listener;
    }

    public void setBottonShowEnable(boolean enable) {
        list.setLoadMoreEnableShow(enable);
    }

    public void setDivider(boolean enable) {
        list.setDivider(null);
    }

    /**
     * 传递xlistView的接口
     */
    public interface IXListViewListener {
        public void onRefresh();

        public void onLoadMore();
    }


}
