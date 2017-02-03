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
import cn.zn.com.zn_android.model.ExcellentAnswerModel;
import cn.zn.com.zn_android.model.bean.MyAskAnswerBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 问股答复
 * <p>
 * Created by Jolly on 2016/12/7.
 */

public class MyAnswerFragment extends BaseFragment implements XListView.IXListViewListener {
    @Bind(R.id.xlv_my_answer)
    XListView mXlvMyAnswer;
    @Bind(android.R.id.empty)
    TextView mEmpty;
    @Bind(R.id.ll_empty)
    LinearLayout mLlEmpty;

    private int page = 0;
    private int num = 10;
    private boolean isRefreshing = false;
    public List<ExcellentAnswerModel> modelList = new ArrayList<>();
    public ListViewAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_my_answer);
        super.onCreate(savedInstanceState);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        isRefreshing = true;
        queryAskAnswerList(0, (page + 1) * num);
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    protected void initView(View view) {
        adapter = new ListViewAdapter(_mContext, R.layout.item_excellent_answer, modelList,
                "ExcellentAnswerHolder");
        mXlvMyAnswer.setAdapter(adapter);
        mEmpty.setText(String.format(getString(R.string.no_data), "答复"));
        mXlvMyAnswer.setEmptyView(mLlEmpty);
    }

    @Override
    protected void initEvent() {
        mXlvMyAnswer.setXListViewListener(this);
    }

    private List<ExcellentAnswerModel> getData(ReturnListValue<MyAskAnswerBean> retValue) {
        List<MyAskAnswerBean> list = retValue.getData();
        List<ExcellentAnswerModel> modelList = new ArrayList<>();
        for (MyAskAnswerBean bean : list) {
            ExcellentAnswerModel model = new ExcellentAnswerModel(bean.getQuestion(), bean.getAnswer(),
                    bean.getHeadimg(), bean.getTeacher_name(), bean.getHtime(), bean.getAsk_id(), bean.getTid());
            model.setComment(true);
            model.setType(bean.getType());
            modelList.add(model);
        }
        return modelList;
    }

    private void queryAskAnswerList(int page, int num) {
        _apiManager.getService().queryAskAnswerList(
                _mApplication.getUserInfo().getSessionID(), page + "", num + "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(ret -> getData(ret))
                .subscribe(retValue -> {
                    if (isRefreshing) {
                        isRefreshing = false;
                        modelList.clear();
                    }
                    if (retValue.size() == 0 || retValue.size() < num) {
                        mXlvMyAnswer.setLoadMoreEnable(false);
                        mXlvMyAnswer.setLoadMoreEnableShow(false);
                        mXlvMyAnswer.setPullLoadEnable(false);
                    } else {
                        mXlvMyAnswer.setLoadMoreEnable(true);
                        mXlvMyAnswer.setLoadMoreEnableShow(true);
                        mXlvMyAnswer.setPullLoadEnable(true);
                    }
                    mXlvMyAnswer.stopRefresh();
                    mXlvMyAnswer.stopLoadMore();
                    modelList.addAll(retValue);
                    adapter.notifyDataSetChanged();
                }, throwable -> {
                    Log.e(TAG, "queryAskAnswerList: ", throwable);
                });

//        AppObservable.bindFragment(this, _apiManager.getService().queryAskAnswerList(
//                _mApplication.getUserInfo().getSessionID(), page + "", num + ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .map(ret -> getData(ret))
//                .subscribe(retValue -> {
//                    if (isRefreshing) {
//                        isRefreshing = false;
//                        modelList.clear();
//                    }
//                    if (retValue.size() == 0 || retValue.size() < num) {
//                        mXlvMyAnswer.setLoadMoreEnable(false);
//                        mXlvMyAnswer.setLoadMoreEnableShow(false);
//                        mXlvMyAnswer.setPullLoadEnable(false);
//                    } else {
//                        mXlvMyAnswer.setLoadMoreEnable(true);
//                        mXlvMyAnswer.setLoadMoreEnableShow(true);
//                        mXlvMyAnswer.setPullLoadEnable(true);
//                    }
//                    mXlvMyAnswer.stopRefresh();
//                    mXlvMyAnswer.stopLoadMore();
//                    modelList.addAll(retValue);
//                    adapter.notifyDataSetChanged();
//                }, throwable -> {
//                    Log.e(TAG, "queryAskAnswerList: ", throwable);
//                });
    }

    @Override
    public void onRefresh() {
        page = 0;
        isRefreshing = true;
        queryAskAnswerList(page, num);
    }

    @Override
    public void onLoadMore() {
        page++;
        queryAskAnswerList(page, num);
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
