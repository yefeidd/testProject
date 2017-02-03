package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.OrderAllModel;
import cn.zn.com.zn_android.model.bean.MyOrderBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/12/7.
 */

public class OrdersAllFragment extends BaseFragment implements XListView.IXListViewListener {
    @Bind(R.id.xlv_my_answer)
    XListView mXlvMyAnswer;
    @Bind(android.R.id.empty)
    TextView mEmpty;
    @Bind(R.id.ll_empty)
    LinearLayout mLlEmpty;

    private ListViewAdapter mAdapter;
    private List<OrderAllModel> data = new ArrayList<>();

    private int type = 1;
    private int page = 0;
    private int num = 10;
    private boolean isRefreshing;

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_my_answer);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    protected void initView(View view) {
        mAdapter = new ListViewAdapter(_mContext, R.layout.item_order_all, data,
                "OrderAllHolder");
        mXlvMyAnswer.setAdapter(mAdapter);
        initData(isRefreshing);
        mEmpty.setText(String.format(getString(R.string.no_data), "订单"));
        mXlvMyAnswer.setEmptyView(mLlEmpty);
    }

    @Override
    protected void initEvent() {
        mXlvMyAnswer.setXListViewListener(this);
    }


    public void queryMyOrderList() {
        _apiManager.getService().queryMyOrderList(
                _mApplication.getUserInfo().getSessionID(), type + "", page + "", num + "")
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnListValue<MyOrderBean>, Observable<List<OrderAllModel>>>() {
                    @Override
                    public Observable<List<OrderAllModel>> call(ReturnListValue<MyOrderBean> returnListValue) {
                        if (null == returnListValue || null == returnListValue.getData()) {
                            return Observable.error(new Throwable("服务器错误，返回来null"));
                        } else {
                            if (returnListValue.getCode().equals("4000")) {
                                return Observable.error(new Throwable(returnListValue.getMsg()));
                            } else {
                                List<OrderAllModel> list = new ArrayList<>();
                                for (MyOrderBean bean : returnListValue.getData()) {
                                    list.add(new OrderAllModel(bean.getRemark(), bean.getMoney(), bean.getOrder_title(), bean.getTime(), bean.getType(), bean.getOrder_num()));
                                }
                                return Observable.just(list);
                            }
                        }

                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribe(returnListValue -> {
                    setPageData(returnListValue);
                }, throwable -> {
                    Log.e(TAG, "queryMyOrderList: ", throwable);
                });

//        AppObservable.bindFragment(this, _apiManager.getService().queryMyOrderList(
//                _mApplication.getUserInfo().getSessionID(), type + "", page + "", num + ""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnListValue<MyOrderBean>, Observable<List<OrderAllModel>>>() {
//                    @Override
//                    public Observable<List<OrderAllModel>> call(ReturnListValue<MyOrderBean> returnListValue) {
//                        if (null == returnListValue || null == returnListValue.getData()) {
//                            return Observable.error(new Throwable("服务器错误，返回来null"));
//                        } else {
//                            if (returnListValue.getCode().equals("4000")) {
//                                return Observable.error(new Throwable(returnListValue.getMsg()));
//                            } else {
//                                List<OrderAllModel> list = new ArrayList<>();
//                                for (MyOrderBean bean : returnListValue.getData()) {
//                                    list.add(new OrderAllModel(bean.getRemark(), bean.getMoney(), bean.getOrder_title(), bean.getTime(), bean.getType(), bean.getOrder_num()));
//                                }
//                                return Observable.just(list);
//                            }
//                        }
//
//                    }
//                })
//                .subscribeOn(Schedulers.io())
//                .subscribe(returnListValue -> {
//                    setPageData(returnListValue);
//                }, throwable -> {
//                    Log.e(TAG, "queryMyOrderList: ", throwable);
//                });

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


    @Override
    public void initData(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        queryMyOrderList();
    }

    /**
     * 设置分页数据显示的方法
     *
     * @param list
     */
    public void setPageData(List<OrderAllModel> list) {
        updatListView();
        if (list.size() == 0 || list.size() < num) {
            mXlvMyAnswer.setLoadMoreEnable(false);
            mXlvMyAnswer.setLoadMoreEnableShow(false);
            mXlvMyAnswer.setPullLoadEnable(false);
        } else {
            mXlvMyAnswer.setLoadMoreEnable(true);
            mXlvMyAnswer.setLoadMoreEnableShow(true);
            mXlvMyAnswer.setPullLoadEnable(true);
        }
        data.addAll(list);
        mAdapter.setDataList(data);
    }


    /**
     * 判断刷新状态的方法
     */
    private void updatListView() {

        if (mXlvMyAnswer.ismPullRefreshing()) {
            mXlvMyAnswer.stopRefresh();
            data.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            data.clear();
        }

        mXlvMyAnswer.stopLoadMore();

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

}
