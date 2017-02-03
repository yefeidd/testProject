package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.MyQuestionModel;
import cn.zn.com.zn_android.model.bean.MyQuestionBean;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的提问
 * Created by Jolly on 2016/12/7.
 */

public class MyQuestionFragment extends BaseFragment implements XListView.IXListViewListener {
    @Bind(R.id.xlv_my_questions)
    XListView mXlvMyQuestions;
    @Bind(android.R.id.empty)
    TextView mEmpty;
    @Bind(R.id.ll_empty)
    LinearLayout mLlEmpty;
    private boolean isRefreshing;
    private int page = 0;
    private int num = 10;
    private List<MyQuestionModel> data = new ArrayList<>();
    private ListViewAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_my_question);
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
        mAdapter = new ListViewAdapter(_mContext, R.layout.item_my_question, data, "MyQuestionHolder");
        mXlvMyQuestions.setAdapter(mAdapter);
        initData(true);
        mEmpty.setText(String.format(getString(R.string.no_data), "提问"));
        mXlvMyQuestions.setEmptyView(mLlEmpty);
    }

    @Override
    protected void initEvent() {
        mXlvMyQuestions.setXListViewListener(this);
    }


    @Override
    public void initData(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        queryAskList();
    }


    private void queryAskList() {
        _apiManager.getService().queryMyAskList(_mApplication.getUserInfo().getSessionID(), page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(returnListValue -> {
                    if (null == returnListValue || null == returnListValue.getData()) {
                        return Observable.error(new Throwable("服务器错误，返回了NULL"));
                    } else {
                        if (returnListValue.getCode().equals("4000")) {
                            return Observable.error(new Throwable(returnListValue.getMsg()));
                        } else {
                            List<MyQuestionModel> returnValue = new ArrayList<>();
                            for (MyQuestionBean bean : returnListValue.getData()) {
                                returnValue.add(new MyQuestionModel(bean));
                            }
                            return Observable.just(returnValue);
                        }
                    }
                })
                .subscribe(returnListValue -> {
                    setPageData(returnListValue);
                }, throwable -> {
                    Log.e(TAG, "queryAskList: " + throwable.getMessage());
                    ToastUtil.show(_mActivity, throwable.getMessage(), Toast.LENGTH_SHORT);
                });

//        AppObservable.bindActivity(_mActivity, _apiManager.getService().queryMyAskList(_mApplication.getUserInfo().getSessionID(), page, num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(returnListValue -> {
//                    if (null == returnListValue || null == returnListValue.getData()) {
//                        return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                    } else {
//                        if (returnListValue.getCode().equals("4000")) {
//                            return Observable.error(new Throwable(returnListValue.getMsg()));
//                        } else {
//                            List<MyQuestionModel> returnValue = new ArrayList<>();
//                            for (MyQuestionBean bean : returnListValue.getData()) {
//                                returnValue.add(new MyQuestionModel(bean));
//                            }
//                            return Observable.just(returnValue);
//                        }
//                    }
//                })
//                .subscribe(returnListValue -> {
//                    setPageData(returnListValue);
//                }, throwable -> {
//                    Log.e(TAG, "queryAskList: " + throwable.getMessage());
//                    ToastUtil.show(_mActivity, throwable.getMessage(), Toast.LENGTH_SHORT);
//                });

    }


    @Override
    public void onRefresh() {
        page = 0;
        isRefreshing = true;
        initData(isRefreshing);
    }


    /**
     * 设置分页数据显示的方法
     *
     * @param list
     */
    public void setPageData(List<MyQuestionModel> list) {
        updatListView();
        if (list.size() == 0 || list.size() < num) {
            mXlvMyQuestions.setLoadMoreEnable(false);
            mXlvMyQuestions.setLoadMoreEnableShow(false);
            mXlvMyQuestions.setPullLoadEnable(false);
        } else {
            mXlvMyQuestions.setLoadMoreEnable(true);
            mXlvMyQuestions.setLoadMoreEnableShow(true);
            mXlvMyQuestions.setPullLoadEnable(true);
        }
        data.addAll(list);
        mAdapter.setDataList(data);
    }


    /**
     * 判断刷新状态的方法
     */
    private void updatListView() {

        if (mXlvMyQuestions.ismPullRefreshing()) {
            mXlvMyQuestions.stopRefresh();
            data.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            data.clear();
        }

        mXlvMyQuestions.stopLoadMore();

    }

    @Override
    public void onLoadMore() {
        page++;
        initData(false);
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
