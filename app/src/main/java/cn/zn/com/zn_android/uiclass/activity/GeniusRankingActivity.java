package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.DynamicExpertModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.DynamicExpertBean;
import cn.zn.com.zn_android.presenter.GeniusRankingPresenter;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.GeniusRankingView;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 人气牛人、最赚钱牛人、短线牛人
 * Created by zjs on 2016/9/21 0021.
 * email: m15267280642@163.com
 * explain:
 */
public class GeniusRankingActivity extends BaseMVPActivity<GeniusRankingView, GeniusRankingPresenter>
        implements View.OnClickListener, GeniusRankingView, XListView.IXListViewListener,
        DynamicExpertModel.FocusChangeListner {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_ranking)
    XListView mXlvRanking;
    private int type = 0;
    private int[] titleNames = new int[]{R.string.popular_expert, R.string.earn_most_expert, R.string.short_term_expert};
    private int page = 0, pageSize = 10;

    private ListViewAdapter rankingAdapter;
    private List<DynamicExpertModel> dynamicExpertList = new ArrayList<>();
    private boolean isRefreshing = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);

    }

    @Override
    public GeniusRankingPresenter initPresenter() {
        return new GeniusRankingPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_genius_ranking;
    }

    public void onEventMainThread(AnyEventType event) {
        if (null != event.getType()) {
            type = event.getType();
        }
    }


    @Override
    protected void initView() {
        mToolbarTitle.setText(titleNames[type]);
        mIvLeftmenu.setVisibility(View.VISIBLE);
        rankingAdapter = new ListViewAdapter(this, R.layout.item_dynamic_expert, dynamicExpertList, "DynamicExpertViewHolder");
        mXlvRanking.setAdapter(rankingAdapter);
        mXlvRanking.setFooterDividersEnabled(false);
        mXlvRanking.setLoadMoreEnableShow(false);
    }

    @Override
    protected void initData() {
        switch (type) {
            case 0:
                presenter.queryHotWarrenList(page, pageSize);
                break;
            case 1:
                presenter.queryFyRanking(page, pageSize);
                break;
            case 2:
                presenter.queryWeekRankingList(page, pageSize);
                break;
        }
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mXlvRanking.setXListViewListener(this);
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
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }

    private void resultOperateList(List<DynamicExpertBean> list) {
        updatListView();

        int beforeCount = dynamicExpertList.size();
        List<DynamicExpertModel> modelList = new ArrayList<>();
        int count = list.size();
        for (int i = 0; i < count; i++) {
            DynamicExpertBean bean = list.get(i);
            DynamicExpertModel model = new DynamicExpertModel(this, bean, beforeCount + i, this);
            if (type == 2) {
                model.setShortTerm(true);
            }
            modelList.add(model);
        }

        if (modelList.size() == 0) {
            mXlvRanking.setLoadMoreEnable(false);
        }

        dynamicExpertList.addAll(modelList);
        rankingAdapter.notifyDataSetChanged();
    }

    private void updatListView() {

        if (mXlvRanking.ismPullRefreshing()) {
            mXlvRanking.stopRefresh();
            dynamicExpertList.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            dynamicExpertList.clear();
        }

        mXlvRanking.stopLoadMore();

    }

    private void resultZzqnr(List<DynamicExpertBean> fyList) {
        updatListView();

        List<DynamicExpertModel> modelList = new ArrayList<>();

        int beforeCount = dynamicExpertList.size();
        int count = fyList.size();
        for (int i = 0; i < count; i++) {
            DynamicExpertBean expertBean = fyList.get(i);
            DynamicExpertModel model = new DynamicExpertModel(this, expertBean, beforeCount + i, this);
            modelList.add(model);
        }

        dynamicExpertList.addAll(modelList);
        rankingAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(int flag, Object object) {
        switch (flag) {
            case GeniusRankingPresenter.RQNR:
            case GeniusRankingPresenter.DXNR: // 短线牛人
                List<DynamicExpertBean> dxnrList = (List<DynamicExpertBean>) object;
                resultOperateList(dxnrList);
                break;

            case GeniusRankingPresenter.ZZQNR:
                List<DynamicExpertBean> fyList = (List<DynamicExpertBean>) object;
                resultZzqnr(fyList);
                break;
        }
    }

    @Override
    public void onError(int flag, Throwable t) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onRefresh() {
        page = 0;
        mXlvRanking.setLoadMoreEnable(true);
        initData();

    }

    @Override
    public void onLoadMore() {
        page++;
        initData();
    }

    @Override
    public void focusChange(boolean focus, String userId, int position) {
        ToastUtil.showShort(this, position + "");
        if (focus) {
            attentionOther(userId, position);
        } else {
            unsetConcern(userId, position);
        }
    }


    public void attentionOther(String userId, int pos) {
        ApiManager.getInstance().getService().attentionOther(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(this, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            dynamicExpertList.get(pos).getBean().setAttentionType("1");
                            rankingAdapter.notifyDataSetChanged();
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });

//        AppObservable.bindActivity(this, ApiManager.getInstance().getService().attentionOther(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(this, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            dynamicExpertList.get(pos).getBean().setAttentionType("1");
//                            rankingAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "attentionOther: ", throwable);
//                });
    }

    public void unsetConcern(String userId, int pos) {
        ApiManager.getInstance().getService().unsetConcern(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(this, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            dynamicExpertList.get(pos).getBean().setAttentionType("0");
                            rankingAdapter.notifyDataSetChanged();
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "unsetConcern: ", throwable);
                });

//        AppObservable.bindActivity(this, ApiManager.getInstance().getService().unsetConcern(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(this, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            dynamicExpertList.get(pos).getBean().setAttentionType("0");
//                            rankingAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "unsetConcern: ", throwable);
//                });
    }

}
