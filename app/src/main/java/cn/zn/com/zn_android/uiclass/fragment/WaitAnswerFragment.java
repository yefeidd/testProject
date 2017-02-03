package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.CountDownAdapter;
import cn.zn.com.zn_android.model.bean.WaitAnswerBean;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.customerview.CountDown.CountDownTask;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;

/**
 * Created by zjs on 2016/11/24 0024.
 * email: m15267280642@163.com
 * explain:诊股大厅待回答页面
 */

public class WaitAnswerFragment extends BaseMVPFragment<DiagnosedStockView, DiagnosesSocketPresenter> implements DiagnosedStockView, XListView.IXListViewListener {


    @Bind(R.id.xlv_wait_answer)
    XListView mXlvWaitAnswer;
    //待回答诊股
    private final static int PRE_ANSWER = 2;
    private final static int NOT_ANSWER = 0;
    private int page = 0;
    private int num = 10;
    private boolean isRefreshing = false;
    private CountDownTask mCountDownTask;
    private List<WaitAnswerBean> data = new ArrayList<>();
    private CountDownAdapter adapter;


    @Override
    public DiagnosesSocketPresenter initPresenter() {
        return new DiagnosesSocketPresenter(_mActivity, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.fragment_wait_answer;
    }

    @Override
    protected void initView(View view) {
        adapter = new CountDownAdapter(_mActivity, data, R.layout.item_prepare_answer);
        mXlvWaitAnswer.setAdapter(adapter);
    }

    /**
     * 初始化数据
     */
    @Override
    public void initData(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        presenter.queryWaitAnswerList(PRE_ANSWER, NOT_ANSWER, page, num);
    }

    @Override
    protected void initEvent() {
        mXlvWaitAnswer.setXListViewListener(this);
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {
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

    @Override
    public void onRefresh() {
        page = 0;
        isRefreshing = true;
        initData(isRefreshing);
    }

    @Override
    public void onLoadMore() {
        page++;
        initData(false);
    }

    /**
     * 设置分页数据显示的方法
     *
     * @param list
     */
    public void setPageData(List<WaitAnswerBean> list) {
        updatListView();
        if (list.size() == 0 || list.size() < num) {
            mXlvWaitAnswer.setLoadMoreEnable(false);
            mXlvWaitAnswer.setLoadMoreEnableShow(false);
            mXlvWaitAnswer.setPullLoadEnable(false);
        } else {
            mXlvWaitAnswer.setLoadMoreEnable(true);
            mXlvWaitAnswer.setLoadMoreEnableShow(true);
            mXlvWaitAnswer.setPullLoadEnable(true);
        }
        data.addAll(list);
        adapter.notifyDataSetChanged();
    }

    /**
     * 判断刷新状态的方法
     */
    private void updatListView() {

        if (mXlvWaitAnswer.ismPullRefreshing()) {
            mXlvWaitAnswer.stopRefresh();
            data.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            data.clear();
        }

        mXlvWaitAnswer.stopLoadMore();

    }

    @Override
    public void onSuccess(DiagnosedType requestType, Object object) {
        if (requestType == DiagnosedType.TEA_ANSWER_QUESTION) {
            setPageData((List<WaitAnswerBean>) object);
        }
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {
        if (requestType == DiagnosedType.TEA_ANSWER_QUESTION) {
            Log.i(TAG, "onError: " + t);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        cancelCountDown();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(true);
        startCountDown();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        cancelCountDown();
    }

    @Override
    public void startCountDown() {
        mCountDownTask = CountDownTask.create();
        adapter.setCountDownTask(mCountDownTask);
    }

    public void cancelCountDown() {
        adapter.setCountDownTask(null);
        if (mCountDownTask != null) {
            mCountDownTask.cancel();
        }
    }


}
