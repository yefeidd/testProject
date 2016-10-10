package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.ContestGameModel;
import cn.zn.com.zn_android.model.ContestNoticeModel;
import cn.zn.com.zn_android.model.ContestRankingListModel;
import cn.zn.com.zn_android.model.bean.ContestDynamicBean;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.zn.com.zn_android.R.id.xlv_all_game;

/**
 *
 */
public class ContestDynamicFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, XListView.IXListViewListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.rb_01)
    RadioButton mRb01;
    @Bind(R.id.rb_02)
    RadioButton mRb02;
    @Bind(R.id.rb_03)
    RadioButton mRb03;
    @Bind(R.id.rg_checked)
    RadioGroup mRgChecked;
    @Bind(xlv_all_game)
    XListView mXlvAllGame;
    @Bind(R.id.xlv_list)
    XListView mXlvList;
    @Bind(R.id.xlv_notice)
    XListView mXlvNotice;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<ContestRankingListModel> listDataList;
    private List<ContestGameModel> gameDataList;
    private List<ContestNoticeModel> noticeDataList;
    private List<ContestDynamicBean> gameList = new ArrayList<>();
    private List<ContestDynamicBean> lvList = new ArrayList<>();
    private List<ContestDynamicBean> noticeList = new ArrayList<>();
    private final String ALL_GAME = "1";
    private final String LV_lIST = "2";
    private final String LV_NOTICE = "3";
    private int gamePage = 0;
    private int lvPage = 0;
    private int noticePage = 0;
    private ListViewAdapter allGameListAdataper;
    private ListViewAdapter rankingListAdataper;
    private ListViewAdapter noticeListAdataper;

    public ContestDynamicFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContestDynamicFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContestDynamicFragment newInstance(String param1, String param2) {
        ContestDynamicFragment fragment = new ContestDynamicFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        _setLayoutRes(R.layout.fragment_contest_dynamic);
    }


    @Override
    protected void initView(View view) {
        mXlvAllGame.setPullRefreshEnable(false);
        mXlvAllGame.setDragLoadEnable(false);
        mXlvAllGame.setAutoLoadEnable(false);
        mXlvAllGame.setPullLoadEnable(true);
        //全部竞赛列表初始化
        gameDataList = new ArrayList<>();
        allGameListAdataper = new ListViewAdapter(_mContext, R.layout.item_contest_dynamic_game, gameDataList, "ContestGameHolder");
        mXlvAllGame.setAdapter(allGameListAdataper);

        //
        mXlvList.setPullRefreshEnable(false);
        mXlvList.setDragLoadEnable(false);
        mXlvList.setAutoLoadEnable(false);
        mXlvList.setPullLoadEnable(true);
        View listHeadView = View.inflate(_mContext, R.layout.item_contest_dynamic_list_head, null);
        mXlvList.addHeaderView(listHeadView);
        listDataList = new ArrayList<>();
        rankingListAdataper = new ListViewAdapter(_mContext, R.layout.item_contest_dynamic_list, listDataList, "ContestRankingListHolder");
        mXlvList.setAdapter(rankingListAdataper);

        //
        mXlvNotice.setPullRefreshEnable(false);
        mXlvNotice.setDragLoadEnable(false);
        mXlvNotice.setAutoLoadEnable(false);
        mXlvNotice.setPullLoadEnable(true);
        noticeDataList = new ArrayList<>();
        noticeListAdataper = new ListViewAdapter(_mContext, R.layout.item_contest_dynamic_notice, noticeDataList, "ContestNoticeListHolder");
        mXlvNotice.setAdapter(noticeListAdataper);
        initData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        currentView = inflater.inflate(layoutRes, container, false);
        ButterKnife.bind(this, currentView);
        initView(currentView);
        initEvent();
        this.inflater = inflater;
        this.container = container;
        return currentView;
    }


    /**
     * 初始化数据
     */
    private void initData() {
        gamePage = 0;
        lvPage = 0;
        noticePage = 0;
        //初始化数据
        getDataForServer(ALL_GAME, gamePage++);
        getDataForServer(LV_lIST, lvPage++);
        getDataForServer(LV_NOTICE, noticePage++);
    }

    @Override
    protected void initEvent() {
        mRgChecked.setOnCheckedChangeListener(this);
        mXlvAllGame.setXListViewListener(this);
        mXlvList.setXListViewListener(this);
        mXlvNotice.setXListViewListener(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (null != mXlvAllGame && null != mXlvList && null != mXlvNotice) {
            switch (checkedId) {
                case R.id.rb_01:
                    mXlvAllGame.setVisibility(View.VISIBLE);
                    mXlvList.setVisibility(View.GONE);
                    mXlvNotice.setVisibility(View.GONE);
                    break;
                case R.id.rb_02:
                    mXlvAllGame.setVisibility(View.GONE);
                    mXlvList.setVisibility(View.VISIBLE);
                    mXlvNotice.setVisibility(View.GONE);
                    break;
                case R.id.rb_03:
                    mXlvAllGame.setVisibility(View.GONE);
                    mXlvList.setVisibility(View.GONE);
                    mXlvNotice.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    @Override
    public void onRefresh() {
        getDataForServer(ALL_GAME, gamePage++);
        getDataForServer(LV_lIST, lvPage++);
        getDataForServer(LV_NOTICE, noticePage++);
    }

    @Override
    public void onLoadMore() {
        getDataForServer(ALL_GAME, gamePage++);
        getDataForServer(LV_lIST, lvPage++);
        getDataForServer(LV_NOTICE, noticePage++);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ContestDynamicFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ContestDynamicFragment");
    }


    public void setDataForAdapter(String type, int currentPage) {
        switch (type) {
            case ALL_GAME:
                if (currentPage != gamePage) {
                    for (ContestDynamicBean bean : gameList) {
                        gameDataList.add(new ContestGameModel(_mActivity, bean));
                    }
                }
//                if (gameList.size() < 5) mXlvAllGame.setLoadMoreEnableShow(false);
                allGameListAdataper.setDataList(gameDataList);
                break;
            case LV_lIST:
                if (currentPage != lvPage) {

                    for (ContestDynamicBean bean : lvList) {
                        listDataList.add(new ContestRankingListModel(_mActivity, bean));
                    }
                }
//                if (gameList.size() < 5) mXlvList.setLoadMoreEnableShow(false);
                rankingListAdataper.setDataList(listDataList);
                break;
            case LV_NOTICE:
                if (currentPage != noticePage) {
                    for (ContestDynamicBean bean : noticeList) {
                        noticeDataList.add(new ContestNoticeModel(_mActivity, bean));
                    }
                }
//                if (gameList.size() <5) mXlvNotice.setLoadMoreEnableShow(false);
                noticeListAdataper.setDataList(noticeDataList);
                break;
        }
        mXlvAllGame.stopRefresh();
        mXlvList.stopRefresh();
        mXlvNotice.stopRefresh();
        mXlvAllGame.stopLoadMore();
        mXlvList.stopLoadMore();
        mXlvNotice.stopLoadMore();
    }


    private void getDataForServer(String type, int page) {
        AppObservable.bindActivity(_mActivity, _apiManager.getService().queryContestDynamicList(type, String.valueOf(page)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnListValue -> {
                    if (null != returnListValue && null != returnListValue.getData()) {
                        switch (type) {
                            case ALL_GAME:
                                gameList = returnListValue.getData();
                                setDataForAdapter(type, page);
                                break;
                            case LV_lIST:
                                lvList = returnListValue.getData();
                                setDataForAdapter(type, page);
                                break;
                            case LV_NOTICE:
                                noticeList = returnListValue.getData();
                                setDataForAdapter(type, page);
                                break;
                        }

                    }
                }, throwable -> {
                    Log.e(TAG, "tradeStock: " + throwable);
                });
    }


}
