package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.BannerAdapter;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.FYListModel;
import cn.zn.com.zn_android.model.HotAritcleContestModel;
import cn.zn.com.zn_android.model.HotStockGodModel;
import cn.zn.com.zn_android.model.HotStockListModel;
import cn.zn.com.zn_android.model.TeacherExplainModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.BaseBannerBean;
import cn.zn.com.zn_android.presenter.BannerPresenter;
import cn.zn.com.zn_android.presenter.ContestHomePresenter;
import cn.zn.com.zn_android.uiclass.NoScrollGridView;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import cn.zn.com.zn_android.uiclass.activity.ArticleListActivity;
import cn.zn.com.zn_android.uiclass.activity.EarningsRankingActivity;
import cn.zn.com.zn_android.uiclass.activity.ImitateFryActivity;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.VideosActivity;
import cn.zn.com.zn_android.uiclass.customerview.vpindicator.CirclePageIndicator;
import cn.zn.com.zn_android.viewfeatures.BaseMvpView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link ContestHomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContestHomeFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, View.OnClickListener, BaseMvpView {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.viewpager)
    ViewPager mViewpager;
    @Bind(R.id.dot_indicator)
    CirclePageIndicator mDotIndicator;
    @Bind(R.id.slv_hot_socket)
    ScrollListView mSlvHotSocket;
    @Bind(R.id.slv_fy_list)
    ScrollListView mSlvFyList;
    @Bind(R.id.slv_hot_list)
    ScrollListView mSlvHotList;
    @Bind(R.id.fl_contest_content)
    FrameLayout mFlContestContent;
    @Bind(R.id.rg_list)
    RadioGroup mRgList;
    @Bind(R.id.slv_earnings_ranking)
    ScrollListView mSlvEarningsRanking;
    @Bind(R.id.slv_hot_article)
    ScrollListView mSlvHotArticle;
    @Bind(R.id.ngv_teacher_explain)
    NoScrollGridView mNgvTeacherExplain;
    @Bind(R.id.iv_hot_god)
    ImageView mIvHotGod;
    @Bind(R.id.iv_teacher_explain)
    ImageView mIvTeacherExplain;
    @Bind(R.id.iv_earnings_ranking)
    ImageView mIvEarningsRanking;
    @Bind(R.id.tv_load_more)
    TextView mTvLoadMore;
    @Bind(R.id.tv_total_money)
    TextView mTvTotalMoney;
    @Bind(R.id.btn_deal)
    Button mBtnDeal;
    @Bind(R.id.rl_hot_god)
    RelativeLayout mRlHotGod;
    @Bind(R.id.rl_teacher_explain)
    RelativeLayout mRlTeacherExplain;
    @Bind(R.id.rl_earnings_ranking)
    RelativeLayout mRlEarningsRanking;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ArrayList<BaseBannerBean> bannerList;
    private BannerAdapter mBannerAdapter;
    private BannerPresenter bannerPresenter;
    private View fyLoadView;
    private ListViewAdapter fyAdapter;
    private View headHotView;
    private ListViewAdapter hotListAdapter;
    private ListViewAdapter earningsDatasAdapter;
    private List<HotAritcleContestModel> hotArticleDatas;
    private ListViewAdapter hotArticleAdapter;
    private View hotLoadView;
    private ContestHomePresenter contestPresenter;
    private String total_money;
    private List<HotStockGodModel> hotModelListPage1 = new ArrayList<>();
    private List<HotStockGodModel> hotModelListPage2 = new ArrayList<>();
    private ListViewAdapter hotAdapter;
    private int hotflag;
    private List<HotStockGodModel> hotModelList;
    private List<FYListModel> earningsDatas;
    private List<FYListModel> fyDatas;
    private List<HotStockListModel> hotDatas;
    private List<TeacherExplainModel> teacherExplainDatas;
    private ListViewAdapter teacherExplainAdapter;


    public ContestHomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContestHomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContestHomeFragment newInstance(String param1, String param2) {
        ContestHomeFragment fragment = new ContestHomeFragment();
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
        _setLayoutRes(R.layout.fragment_contest_home);
    }

    @Override
    protected void initView(View view) {

        //banner初始化
        bannerList = new ArrayList<>();
        mBannerAdapter = new BannerAdapter(_mActivity, bannerList, null, TAG);
        mViewpager.setAdapter(mBannerAdapter);
        mDotIndicator.setViewPager(mViewpager);
        bannerPresenter = new BannerPresenter(this, _mContext, mBannerAdapter);
        bannerPresenter.attach(mViewpager);

        //风云排行榜，热门股票排行榜列表初始化
        contestPresenter = new ContestHomePresenter(_mActivity);
        contestPresenter.attach(this);
        initListView();
    }

    @Override
    protected void initEvent() {
        mRgList.setOnCheckedChangeListener(this);
        mRlHotGod.setOnClickListener(this);
        mRlEarningsRanking.setOnClickListener(this);
        mRlTeacherExplain.setOnClickListener(this);
        mTvLoadMore.setOnClickListener(this);
    }

    protected void initHomeData() {
//        bannerPresenter.postBannerFromServer("ContestBanner");
        hotArticleDatas.clear();
        fyDatas.clear();
        hotDatas.clear();
        earningsDatas.clear();
        hotModelList.clear();
        teacherExplainDatas.clear();
        hotModelListPage1.clear();
        hotModelListPage2.clear();
        contestPresenter.queryContestDataList();

    }


    protected void initListView() {

        //热门大股神初始化
        hotModelList = new ArrayList<>();
        hotAdapter = new ListViewAdapter(_mContext, R.layout.item_hot_stock_god, hotModelList, "HotStockGodViewHolder");
        mSlvHotSocket.setAdapter(hotAdapter);

        //风云排行榜
        fyLoadView = LayoutInflater.from(_mContext).inflate(R.layout.item_load_more, new ListView(_mContext), false);
        fyLoadView.setOnClickListener(v1 -> {
            EventBus.getDefault().postSticky(new AnyEventType().setType(0));
            startActivity(new Intent(_mContext, EarningsRankingActivity.class));
        });
        View headFyView = LayoutInflater.from(_mContext).inflate(R.layout.item_contest_list_head, new ListView(_mContext), false);
        mSlvFyList.addHeaderView(headFyView);
        mSlvFyList.addFooterView(fyLoadView);
        fyDatas = new ArrayList<>();
        fyAdapter = new ListViewAdapter(_mContext, R.layout.item_contest_fy_list, fyDatas, "FYListHolder");
        mSlvFyList.setAdapter(fyAdapter);

        //热门股票排行榜
        hotLoadView = LayoutInflater.from(_mContext).inflate(R.layout.item_load_more, new ListView(_mContext), false);
        hotLoadView.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType().setType(2));
            startActivity(new Intent(_mContext, EarningsRankingActivity.class));
        });
        headHotView = LayoutInflater.from(_mContext).inflate(R.layout.item_contest_hot_list_head, new ListView(_mContext), false);
        mSlvHotList.addHeaderView(headHotView);
        mSlvHotList.addFooterView(hotLoadView);
        hotDatas = new ArrayList<>();
        hotListAdapter = new ListViewAdapter(_mContext, R.layout.item_contest_hot_list, hotDatas, "HotStockListHolder");
        mSlvHotList.setAdapter(hotListAdapter);


        //收益率跟踪排行榜
        mSlvEarningsRanking.addHeaderView(headFyView);
        earningsDatas = new ArrayList<>();
        earningsDatasAdapter = new ListViewAdapter(_mContext, R.layout.item_contest_fy_list, earningsDatas, "FYListHolder");
        mSlvEarningsRanking.setAdapter(earningsDatasAdapter);

        //人气文章排行初始化
        hotArticleDatas = new ArrayList<>();
        hotArticleAdapter = new ListViewAdapter(_mContext, R.layout.item_hot_article, hotArticleDatas, "HotArticleContestHolder");
        mSlvHotArticle.setAdapter(hotArticleAdapter);

        //名师解盘初始化
        teacherExplainDatas = new ArrayList<>();
        teacherExplainAdapter = new ListViewAdapter(_mContext, R.layout.item_teacher_explain, teacherExplainDatas, "TeacherExplainHolder");
        mNgvTeacherExplain.setAdapter(teacherExplainAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_01:
                mSlvFyList.setVisibility(View.VISIBLE);
                mSlvHotList.setVisibility(View.GONE);
                break;
            case R.id.rb_02:
                mSlvHotList.setVisibility(View.VISIBLE);
                mSlvFyList.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_load_more:
                EventBus.getDefault().postSticky(new AnyEventType(Constants.ARTICLE));
                startActivity(new Intent(_mActivity, ArticleListActivity.class));
                break;
            case R.id.rl_hot_god:
                if (hotflag == 0 && null != hotModelListPage2 && hotModelListPage2.size() != 0) {
                    hotflag = 1;
                    hotAdapter.setDataList(hotModelListPage2);
                } else if (hotflag == 1 && null != hotModelListPage1) {
                    hotflag = 0;
                    hotAdapter.setDataList(hotModelListPage1);
                }
                break;
            case R.id.rl_earnings_ranking:
                EventBus.getDefault().postSticky(new AnyEventType().setType(1));
                startActivity(new Intent(_mContext, EarningsRankingActivity.class));
                break;
            case R.id.rl_teacher_explain:
                startActivity(new Intent(_mActivity, VideosActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initHomeData();
        MobclickAgent.onPageStart("ContestHomeFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ContestHomeFragment");
    }

    @Override
    public void onSuccess(int type, Object object) {
        //bannaer数据
        bannerPresenter.setBannerList(contestPresenter.getBanner());
        //热门新闻
        hotArticleDatas = contestPresenter.getArtListModles();
        hotArticleAdapter.setDataList(hotArticleDatas);
        //热门大股神
        hotModelList = contestPresenter.getHotStockGodModles();
        int start1 = hotModelList.size() > 3 ? 2 : hotModelList.size() - 1;
        for (int i = 0; i <= start1; i++) {
            hotModelListPage1.add(hotModelList.get(i));
        }
        int start2 = start1 + 1;
        for (int i = start2; i < hotModelList.size(); i++) {
            hotModelListPage2.add(hotModelList.get(i));
        }
        hotflag = 0;
        hotAdapter.setDataList(hotModelListPage1);
        //模拟炒股资金
        total_money = contestPresenter.getTotal_money();
        if ("".equals(total_money)) {
            mTvTotalMoney.setText("领取百万模拟炒股资金");
            mBtnDeal.setText("立即领取");
            mBtnDeal.setOnClickListener(v -> {
                _mActivity.startActivity(new Intent(_mActivity, LoginActivity.class));
            });
        } else {
            mBtnDeal.setText("模拟交易");
            mTvTotalMoney.setText(total_money);
            mBtnDeal.setOnClickListener(v -> {
                _mActivity.startActivity(new Intent(_mActivity, ImitateFryActivity.class));
            });

        }
        //风云排行榜
        fyDatas = contestPresenter.getFyDatas();
        fyAdapter.setDataList(fyDatas);
        //收益率跟踪排行榜
        earningsDatas = contestPresenter.getEarningsDatas();
        earningsDatasAdapter.setDataList(earningsDatas);
        //热门股票排行榜
        hotDatas = contestPresenter.getHotDatas();
        hotListAdapter.setDataList(hotDatas);
        //名师解盘
        teacherExplainDatas = contestPresenter.getVedioDatas();
        teacherExplainAdapter.setDataList(teacherExplainDatas);
    }

    @Override
    public void onError(int Type, Throwable throwable) {

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
}
