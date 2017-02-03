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
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.PreAnswerModel;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;

/**
 * Created by zjs on 2016/11/24 0024.
 * email: m15267280642@163.com
 * explain:抢答诊股页面
 */

public class PreAnswerFragment extends BaseMVPFragment<DiagnosedStockView, DiagnosesSocketPresenter> implements DiagnosedStockView, XListView.IXListViewListener {

    @Bind(R.id.xlv_pre_answer)
    XListView mXlvPreAnswer;
    private List<PreAnswerModel> dataList = new ArrayList<>();
    //抢答诊股
    private final static int PRE_ANSWER = 1;
    private final static int NOT_ANSWER = 0;
    private int page = 0;
    private int num = 10;
    private ListViewAdapter adapter;
    private boolean isRefreshing = false;

    @Override
    public DiagnosesSocketPresenter initPresenter() {
        return new DiagnosesSocketPresenter(_mActivity, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.fragment_pre_answer;
    }

    @Override
    protected void initView(View view) {
        adapter = new ListViewAdapter(_mActivity, R.layout.item_prepare_answer, dataList, "PreAnswerHolder");
        mXlvPreAnswer.setAdapter(adapter);
    }


    @Override
    protected void initEvent() {
        mXlvPreAnswer.setXListViewListener(this);
    }


    /**
     * 初始化数据
     */
    @Override
    public void initData(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        presenter.queryTeaAnswerList(PRE_ANSWER, NOT_ANSWER, page, num);
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
    public void onSuccess(DiagnosedType requestType, Object object) {
        if (requestType == DiagnosedType.TEA_ANSWER_QUESTION) {
            setPageData((List<PreAnswerModel>) object);
//            dataList = (List<PreAnswerModel>) object;
        }
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {
        if (requestType == DiagnosedType.TEA_ANSWER_QUESTION) {
            Log.i(TAG, "onError: " + t);
        }
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
    public void setPageData(List<PreAnswerModel> list) {
        updatListView();
        if (list.size() == 0 || list.size() < num) {
            mXlvPreAnswer.setLoadMoreEnable(false);
            mXlvPreAnswer.setLoadMoreEnableShow(false);
            mXlvPreAnswer.setPullLoadEnable(false);
        } else {
            mXlvPreAnswer.setLoadMoreEnable(true);
            mXlvPreAnswer.setLoadMoreEnableShow(true);
            mXlvPreAnswer.setPullLoadEnable(true);
        }
        dataList.addAll(list);
        adapter.setDataList(dataList);
    }


    /**
     * 判断刷新状态的方法
     */
    private void updatListView() {

        if (mXlvPreAnswer.ismPullRefreshing()) {
            mXlvPreAnswer.stopRefresh();
            dataList.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            dataList.clear();
        }

        mXlvPreAnswer.stopLoadMore();

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(true);
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.cancleCountDown();
    }
}
