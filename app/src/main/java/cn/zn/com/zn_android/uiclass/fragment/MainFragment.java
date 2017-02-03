package cn.zn.com.zn_android.uiclass.fragment;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.BannerAdapter;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.DynamicExpertModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.BannerBean;
import cn.zn.com.zn_android.model.bean.DynamicExpertBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.BannerPresenter;
import cn.zn.com.zn_android.service.RefreshDataService;
import cn.zn.com.zn_android.uiclass.activity.ArticleListActivity;
import cn.zn.com.zn_android.uiclass.activity.ArticleSearchActivity;
import cn.zn.com.zn_android.uiclass.activity.DiagnoseSocketActivity;
import cn.zn.com.zn_android.uiclass.activity.DianosedStockActivity;
import cn.zn.com.zn_android.uiclass.activity.GeniusRankingActivity;
import cn.zn.com.zn_android.uiclass.activity.ImitateFryActivity;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.MainActivity;
import cn.zn.com.zn_android.uiclass.customerview.vpindicator.CirclePageIndicator;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页Fragment
 * <p>
 * Created by Administrator on 2016/3/9 0009.
 */
public class MainFragment extends BaseFragment implements View.OnClickListener,
        XListView.IXListViewListener, DynamicExpertModel.FocusChangeListner {
    private final String TAG = MainFragment.class.getSimpleName();
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.lv_live)
    XListView lv_live;

    private View header;
    private ViewPager mViewpager;
    private CirclePageIndicator mIndicator;
    private Dialog dialog;

    private List<BannerBean> bannerList = new ArrayList<>();
    private BannerAdapter mBannerAdapter;
    private ListViewAdapter mHotAdapter;
    private List<DynamicExpertModel> dynamicExpertList = new ArrayList<>();
    private String kwords = "";
    private boolean isFirst = true;
    private BannerPresenter bannerPresenter;
    private int page = 0, num = 5;
    private boolean isRefreshing = false;

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (isFirst) {
            super.onHiddenChanged(hidden);
        } else {

        }
        Log.d(TAG, "onHiddenChanged: " + hidden);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_main);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        mIvLeftmenu.setVisibility(View.GONE);
        mToolbarTitle.setText(getString(R.string.home));
        mIbSearch.setVisibility(View.VISIBLE);
        mIbSearch.setImageResource(R.drawable.iv_search);

        header = LayoutInflater.from(_mContext).inflate(R.layout.layout_home_header, null);
        lv_live.addHeaderView(header);

        //bannar相关的控件初始化
        //查找viewpager和indicator对象
        mViewpager = (ViewPager) header.findViewById(R.id.viewpager);
        mIndicator = (CirclePageIndicator) header.findViewById(R.id.dot_indicator);
        bannerList = new ArrayList<>();
        mBannerAdapter = new BannerAdapter(_mActivity, bannerList, (MainActivity) getActivity(), TAG);
        mViewpager.setAdapter(mBannerAdapter);
        mIndicator.setViewPager(mViewpager);
        bannerPresenter = new BannerPresenter(this, _mContext, mBannerAdapter);
        bannerPresenter.attach(mViewpager);

        header.findViewById(R.id.btn_school).setOnClickListener(this);
        header.findViewById(R.id.btn_course).setOnClickListener(this);
        header.findViewById(R.id.btn_article).setOnClickListener(this);
        header.findViewById(R.id.btn_popular_expert).setOnClickListener(this);
        header.findViewById(R.id.btn_earn_most).setOnClickListener(this);
        header.findViewById(R.id.btn_short_term).setOnClickListener(this);

        /**
         * 初始化listView控件
         */
//        getListData();
        mHotAdapter = new ListViewAdapter(_mContext, R.layout.item_dynamic_expert,
                dynamicExpertList, "DynamicExpertViewHolder");
        lv_live.setAdapter(mHotAdapter);
        lv_live.setFooterDividersEnabled(false);
//        lv_live.setLoadMoreEnableShow(false);
//        lv_live.setAutoLoadEnable(false);
        lv_live.setPullLoadEnable(true);
        lv_live.setPullRefreshEnable(true);
        lv_live.setLoadMoreEnable(true);

        //初始化内部数据
//        initData();

    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: ");
        if (isFirst) {
            IntentFilter mFilter = new IntentFilter();
            mFilter.addAction(RefreshDataService.TAG);
//            lbm.registerReceiver(mReceiver, mFilter);
        }
        isFirst = false;
    }


    @Override
    public void onResume() {
        super.onResume();
        lv_live.setLoadMoreEnable(true);
//        page = 0;
//        initData();
//        onRefresh();
        bannerPresenter.postBannerFromServer("MainBanner");
        isRefreshing = true;
        queryHotWarrenList(0, num * (page + 1));

        MobclickAgent.onPageStart("MainFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MainFragment");
    }

    /**
     * 初始化数据
     */
    public void initData() {
        //从服务器请求数据
        bannerPresenter.postBannerFromServer("MainBanner");
        queryHotWarrenList(page, num);
    }

    @Override
    protected void initEvent() {
        //特约讲堂点击事件注册
        mIbSearch.setOnClickListener(this);

        lv_live.setXListViewListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_school: // 诊股大厅
//                EventBus.getDefault().postSticky(new AnyEventType(Constants.ARTICLE).setState(true));
//                startActivity(new Intent(getActivity(), MyCollectionActivity.class));
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    if (_mApplication.getUserInfo().getIsTeacher()) {
                        startActivity(new Intent(getActivity(), DiagnoseSocketActivity.class));
                    } else {
                        startActivity(new Intent(getActivity(), DianosedStockActivity.class));
                    }
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_course:
//                startActivity(new Intent(getActivity(), SpecialLectureActivity.class));
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    virReg(_mApplication.getUserInfo().getSessionID(), 1);
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_article:
//                EventBus.getDefault().postSticky(new AnyEventType(Constants.ARTICLE));
                startActivity(new Intent(getActivity(), ArticleListActivity.class));
                break;
            case R.id.ib_search:
                EventBus.getDefault().postSticky(new AnyEventType(Constants.VIDEO));
                startActivity(new Intent(getActivity(), ArticleSearchActivity.class));
                break;
            case R.id.btn_popular_expert:
                EventBus.getDefault().postSticky(new AnyEventType().setType(0));
                startActivity(new Intent(getActivity(), GeniusRankingActivity.class));
                break;
            case R.id.btn_earn_most:
                EventBus.getDefault().postSticky(new AnyEventType().setType(1));
                startActivity(new Intent(getActivity(), GeniusRankingActivity.class));
                break;
            case R.id.btn_short_term:
                EventBus.getDefault().postSticky(new AnyEventType().setType(2));
                startActivity(new Intent(getActivity(), GeniusRankingActivity.class));
                break;
        }
    }

    public void virReg(String sessionId, int flag) {
        _apiManager.getService().virReg(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    resultVirReg(retValue, flag);
                }, throwable -> {
                    Log.e(TAG, "queryUserSign: ", throwable);
                });

//        AppObservable.bindFragment(this, _apiManager.getService().virReg(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    resultVirReg(retValue, flag);
//                }, throwable -> {
//                    Log.e(TAG, "queryUserSign: ", throwable);
//                });
    }

    public void resultVirReg(ReturnValue<MessageBean> returnValue, int flag) {
        if (returnValue.getCode().equals("2000")) {
            switch (flag) {
                case 1:
                    startActivity(new Intent(_mContext, ImitateFryActivity.class));
                    break;
                case 2:
                    ((MainActivity) _mActivity).clickPage(5);
                    break;
            }
        }
    }

    private void queryHotWarrenList(int page, int num) {
        _apiManager.getService().queryMoneyStockGenius(_mApplication.getUserInfo().getSessionID(), page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultOperateList, throwable -> {
                    Log.e(TAG, "queryOperateList: ", throwable);
                    isRefreshing = false;
                    if (lv_live != null) {
                        lv_live.stopRefresh();
                    }
                });

//        AppObservable.bindFragment(this, _apiManager.getService().queryMoneyStockGenius(_mApplication.getUserInfo().getSessionID(), page, num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultOperateList, throwable -> {
//                    Log.e(TAG, "queryOperateList: ", throwable);
//                    isRefreshing = false;
//                    lv_live.stopRefresh();
//                });
    }

    private void resultOperateList(ReturnListValue<DynamicExpertBean> retValue) {

        if (lv_live.ismPullRefreshing()) {
            if (lv_live != null) {
                lv_live.stopRefresh();
            }
            dynamicExpertList.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            dynamicExpertList.clear();
        }

        List<DynamicExpertBean> list = retValue.getData();

        List<DynamicExpertModel> modelList = new ArrayList<>();
        int beforeCount = dynamicExpertList.size();
        int count = list.size();
        for (int i = 0; i < count; i++) {
            DynamicExpertModel model = new DynamicExpertModel(getActivity(), list.get(i), beforeCount + i, this);
            modelList.add(model);
        }

        if (modelList.size() == 0) {
            lv_live.setLoadMoreEnable(false);
        }

        lv_live.stopLoadMore();

        dynamicExpertList.addAll(modelList);
        mHotAdapter.notifyDataSetChanged();
    }

    Subscription timerSubscription;

    private void timeCount() {
        timerSubscription = Observable.timer(1, 1, TimeUnit.SECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
                .observeOn(Schedulers.io())
                .subscribeOn(Schedulers.io())
//                .toBlocking()
                .subscribe(aLong -> {
                    if (aLong == 5) {
                        if (lv_live.ismPullRefreshing()) {
                            lv_live.stopRefresh();
                            timerSubscription.unsubscribe();
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "倒计时出错！\n" + throwable);
                    timerSubscription.unsubscribe();
                });
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        lv_live.setLoadMoreEnable(true);
        page = 0;
        queryHotWarrenList(page, num);
        timeCount();
        bannerPresenter.postBannerFromServer("MainBanner");

    }

    @Override
    public void onLoadMore() {
        page++;
        queryHotWarrenList(page, num);
    }

    @Override
    public void focusChange(boolean focus, String userId, int position) {
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
                        ToastUtil.showShort(_mActivity, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            dynamicExpertList.get(pos).getBean().setAttentionType("1");
                            mHotAdapter.notifyDataSetChanged();
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });

//        AppObservable.bindActivity(_mActivity, ApiManager.getInstance().getService().attentionOther(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(_mActivity, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            dynamicExpertList.get(pos).getBean().setAttentionType("1");
//                            mHotAdapter.notifyDataSetChanged();
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
                        ToastUtil.showShort(_mActivity, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            dynamicExpertList.get(pos).getBean().setAttentionType("0");
                            mHotAdapter.notifyDataSetChanged();
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "unsetConcern: ", throwable);
                });

//        AppObservable.bindActivity(_mActivity, ApiManager.getInstance().getService().unsetConcern(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(_mActivity, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            dynamicExpertList.get(pos).getBean().setAttentionType("0");
//                            mHotAdapter.notifyDataSetChanged();
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "unsetConcern: ", throwable);
//                });
    }

}
