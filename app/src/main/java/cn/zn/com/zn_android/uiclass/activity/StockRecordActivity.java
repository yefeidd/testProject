package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.StockRecordAdapter;
import cn.zn.com.zn_android.model.bean.StockRecordBean;
import cn.zn.com.zn_android.model.bean.StockRecordItemBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/10 0010.
 */
public class StockRecordActivity extends BaseActivity implements XListView.IXListViewListener {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_record)
    XListView mXlvRecord;

    private ViewHolder holder;
    private StockRecordAdapter mAdapter;
    private List<StockRecordItemBean> itemList = new ArrayList<>();
    private int page = 1, pageSize = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_stock_record);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.histoty_records);

        View headView = LayoutInflater.from(this).inflate(R.layout.layout_stock_record_head, null, false);
        holder = new ViewHolder(headView);
        mXlvRecord.addHeaderView(headView);

        mAdapter = new StockRecordAdapter(this, R.layout.item_stock_record, itemList);
        mXlvRecord.setAdapter(mAdapter);
        mXlvRecord.setXListViewListener(this);

        queryRecordList();
    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    private void queryRecordList() {
        AppObservable.bindActivity(this, _apiManager.getService().queryRecordList(_mApplication.getUserInfo().getSessionID(), page, pageSize))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultRecordList, throwable -> {
                    Log.e(TAG, "queryRecordList: " + throwable);
                    mXlvRecord.stopLoadMore();
                    mXlvRecord.stopRefresh();
                    mXlvRecord.setLoadMoreEnable(true);
                });
    }

    private void resultRecordList(ReturnValue returnValue) {
        if (null == returnValue) return;

        if (mXlvRecord.ismPullRefreshing()) {
            itemList.clear();
        }
        mXlvRecord.stopLoadMore();
        mXlvRecord.stopRefresh();

        StockRecordBean recordBean = (StockRecordBean) returnValue.getData();
        holder.mTvStart.setText(String.format(getString(R.string.start_time), recordBean.getStart_time()));
        holder.mTvEnd.setText(String.format(getString(R.string.end_time), recordBean.getEnd_time()));

        List<StockRecordItemBean> list = recordBean.getData();

        if (list.size() == 0) {
            mXlvRecord.setLoadMoreEnable(false);
        }
        itemList.addAll(list);
        mAdapter.notifyDataSetChanged();


    }

    @Override
    public void onRefresh() {
        page = 1;
        queryRecordList();
        mXlvRecord.setLoadMoreEnable(true);
    }

    @Override
    public void onLoadMore() {
        page++;
        queryRecordList();
    }

    static class ViewHolder {
        @Bind(R.id.tv_start)
        TextView mTvStart;
        @Bind(R.id.tv_end)
        TextView mTvEnd;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
