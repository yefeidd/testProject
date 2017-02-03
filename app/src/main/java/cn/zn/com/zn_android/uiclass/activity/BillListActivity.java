package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.BillListModel;
import cn.zn.com.zn_android.model.BillModel;
import cn.zn.com.zn_android.model.bean.BillBean;
import cn.zn.com.zn_android.uiclass.TitledListView;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/12/7.
 * 列表
 */

public class BillListActivity extends BaseActivity implements View.OnClickListener, AbsListView.OnScrollListener, XListView.IXListViewListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tlv_bill)
    TitledListView mTlvBill;
    @Bind(android.R.id.empty)
    TextView mEmpty;
    @Bind(R.id.ll_empty)
    LinearLayout mLlEmpty;

    private List<BillListModel> modelList = new ArrayList<>();
    private boolean isRefreshing = false;
    private int page = 0;
    private int num = 10;
    private ListViewAdapter mAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_bill_list);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.bill);

        mEmpty.setText(String.format(getString(R.string.no_data), getString(R.string.bill)));
        mTlvBill.setEmptyView(mLlEmpty);

        mAdapter = new ListViewAdapter(this, R.layout.item_ss_list, modelList, "BillListHolder");
        mTlvBill.setAdapter(mAdapter);
        mTlvBill.setOnScrollListener(this);
        initData(true);
    }

    @Override
    protected void initEvent() {
        mTlvBill.setXListViewListener(this);
        mIvLeftmenu.setOnClickListener(this);
        mTlvBill.setFirstPos(0);
        mTlvBill.setTitleClickListener(new TitledListView.TitleClickListener() {
            @Override
            public void onClick(View v) {
                int pos = Integer.valueOf(v.getTag().toString());
                mTlvBill.smoothScrollToPosition(pos);
//                TextView tv = (TextView) v;
//                String title = tv.getText().toString();
//                EventBus.getDefault().postSticky(new AnyEventType(title));
//                startActivity(new Intent(getApplicationContext(), UpDownRankingActivity.class));
            }
        });
    }


//
//    private void getData() {
//        for (int i = 0; i < 5; i++) {
//            List<BillModel> list = new ArrayList<>();
//            for (int j = 0; j < 4; j++) {
//                BillModel model = new BillModel("周二\n11-22", "+" + (j + 1) * 5 + "币", "策略");
//                list.add(model);
//            }
//            BillListModel listModel = new BillListModel((12 - i) + "月", list);
//            modelList.add(listModel);
//        }
//        ListViewAdapter mAdapter = new ListViewAdapter(this, R.layout.item_ss_list, modelList, "BillListHolder");
//        mTlvBill.setAdapter(mAdapter);
//        mTlvBill.setOnScrollListener(this);
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 第一项与第二项标题不同，说明标题需要移动
        if (modelList.size() == 0) {
            return;
        }
        if (firstVisibleItem == 0) {
//            ((TitledListView) view).moveTitle();
            ((TitledListView) view).updateTitle(modelList.get(firstVisibleItem).getMonth(), firstVisibleItem);
            return;
        }
        if (modelList.size() == firstVisibleItem - 1 || modelList.size() == firstVisibleItem) {
            ((TitledListView) view).updateTitle(modelList.get(firstVisibleItem - 1).getMonth(), firstVisibleItem);
            return;
        }

        ((TitledListView) view).moveTitle();
        ((TitledListView) view).updateTitle(modelList.get(firstVisibleItem - 1).getMonth(), firstVisibleItem);

//        if (!modelList.get(firstVisibleItem).getMonth().equals(modelList.get(firstVisibleItem - 1).getMonth())) {
//            Log.e("titlelistview_onscroll", "!!!!!!!!!!!!!!!!");
//
//        } else {
//            Log.e("titlelistview_onscroll", "===============");
//            ((TitledListView) view).updateTitle(modelList.get(firstVisibleItem).getMonth(), firstVisibleItem);
//        }
    }


    @Override
    public void onRefresh() {
        page = 0;
        initData(true);
    }

    @Override
    public void onLoadMore() {
        page++;
        initData(false);
    }


    /**
     * 初始化数据
     *
     * @param isRefreshing 是否是第一次初始化数据
     */
    public void initData(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        queryBillList();
    }


    /**
     * 设置分页数据显示的方法
     *
     * @param list
     */
    public void setPageData(List<BillListModel> list) {
        updatListView();
        if (list.size() == 0 || list.size() < num) {
            mTlvBill.setLoadMoreEnable(false);
            mTlvBill.setLoadMoreEnableShow(false);
            mTlvBill.setPullLoadEnable(false);
        } else {
            mTlvBill.setLoadMoreEnable(true);
            mTlvBill.setLoadMoreEnableShow(true);
            mTlvBill.setPullLoadEnable(true);
        }
        modelList.addAll(list);
        mAdapter.setDataList(modelList);
    }


    /**
     * 判断刷新状态的方法
     */
    private void updatListView() {

        if (mTlvBill.ismPullRefreshing()) {
            mTlvBill.stopRefresh();
            modelList.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            modelList.clear();
        }

        mTlvBill.stopLoadMore();

    }


    /**
     * 查询账单
     */
    private void queryBillList() {
        _apiManager.getService().queryUserBillList(_mApplication.getUserInfo().getSessionID(), page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(returnValue -> {
                    if (null == returnValue || null == returnValue.getData()) {
                        return Observable.error(new Throwable("服务器错误，返回来null"));
                    } else {
                        if (returnValue.getCode().equals("4000")) {
                            return Observable.error(new Throwable(returnValue.getMsg()));
                        } else {
                            List<BillListModel> cusModelList = new ArrayList<>();
                            for (BillBean bean : returnValue.getData()) {
                                List<BillBean.DataBean> billList = bean.getData();
                                List<BillModel> list = new ArrayList<>();
                                for (BillBean.DataBean bean1 : billList) {
                                    BillModel model = new BillModel(bean1);
                                    list.add(model);
                                }
                                BillListModel listModel = new BillListModel(bean.getMonth(), list);
                                cusModelList.add(listModel);
                            }
                            return Observable.just(cusModelList);
                        }
                    }
                }).subscribe(returnValue -> {
            setPageData(returnValue);
        }, throwable -> {
            Log.e(TAG, "queryBillList: ", throwable);
            ToastUtil.show(this, getString(R.string.no_net), Toast.LENGTH_SHORT);
        });

//        AppObservable.bindActivity(this, _apiManager.getService().queryUserBillList(_mApplication.getUserInfo().getSessionID(), page))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(returnValue -> {
//                    if (null == returnValue || null == returnValue.getData()) {
//                        return Observable.error(new Throwable("服务器错误，返回来null"));
//                    } else {
//                        if (returnValue.getCode().equals("4000")) {
//                            return Observable.error(new Throwable(returnValue.getMsg()));
//                        } else {
//                            List<BillListModel> cusModelList = new ArrayList<>();
//                            for (BillBean bean : returnValue.getData()) {
//                                List<BillBean.DataBean> billList = bean.getData();
//                                List<BillModel> list = new ArrayList<>();
//                                for (BillBean.DataBean bean1 : billList) {
//                                    BillModel model = new BillModel(bean1);
//                                    list.add(model);
//                                }
//                                BillListModel listModel = new BillListModel(bean.getMonth(), list);
//                                cusModelList.add(listModel);
//                            }
//                            return Observable.just(cusModelList);
//                        }
//                    }
//                }).subscribe(returnValue -> {
//            setPageData(returnValue);
//        }, throwable -> {
//            Log.e(TAG, "queryBillList: " + throwable.getMessage());
//            ToastUtil.show(this, throwable.getMessage(), Toast.LENGTH_SHORT);
//        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

}
