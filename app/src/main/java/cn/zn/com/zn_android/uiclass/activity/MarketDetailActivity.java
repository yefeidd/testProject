package cn.zn.com.zn_android.uiclass.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.YAxis;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.NewsAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.CompDetailBean;
import cn.zn.com.zn_android.model.bean.HKCompDetailBean;
import cn.zn.com.zn_android.model.bean.HotTicBean;
import cn.zn.com.zn_android.model.bean.MarketDetailBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.StockNewsBean;
import cn.zn.com.zn_android.presenter.MarketDetailPresenter;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.presenter.requestType.MarketDetailType;
import cn.zn.com.zn_android.service.RefreshDataService;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.fragment.BaseFragment;
import cn.zn.com.zn_android.uiclass.fragment.BriefFragment;
import cn.zn.com.zn_android.uiclass.fragment.CorpFinanceFragment;
import cn.zn.com.zn_android.uiclass.fragment.ShareholdersFragment;
import cn.zn.com.zn_android.uiclass.page.KLinePage;
import cn.zn.com.zn_android.uiclass.page.MinutesPage;
import cn.zn.com.zn_android.uiclass.xlistview.XScrollView;
import cn.zn.com.zn_android.utils.ClassUtils;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.MarketDetailView;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/7/4 0004.
 * email: m15267280642@163.com
 * explain:
 */
public class MarketDetailActivity extends BaseMVPActivity<MarketDetailView, MarketDetailPresenter>
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, MarketDetailView,
        XScrollView.IXScrollViewListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.tv_subtitle)
    TextView mTvSubtitle;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_share)
    ImageButton mIbShare;
    @Bind(R.id.xsv_market_detail)
    XScrollView mXsvMarketDetail;
    RadioGroup mRgChartChoice;
    FrameLayout mFlChart;
    ScrollListView mSlvNews;
    RadioButton mRbNews, mRbAnnounce;
    @Bind(R.id.tv_add_self_stock)
    TextView mTvAddSelfStock;
    @Bind(R.id.tv_buy_in)
    TextView mTvBuyIn;
    private Button mBtnClick;

    private BaseFragment[] fragments = new BaseFragment[3];
    private ArrayList<View> chartViews;
    private int fragmentIndex = 0;
    private final int MINUTES_TYPE = 0;
    private final int DAY_K_TYPE = 1;
    private final int WEEK_K_TYPE = 2;
    private final int MONTH_K_TYPE = 3;
    private View minutesView;
    private View daykLineView, weekKLineView, MonthKLineView;
    private MinutesPage minutesPage;
    private KLinePage dayKLinePage, weekKLinePage, MonthLinePage;
    private NewsAdapter newsAdapter;
    private String ticCode = "";
    private String title = "";
    private String newType = "1";
    private boolean isOpenStatus = false;
    private ViewHolder marketDetaiHolder;
    private MarketDetailBean marketData;
    private List<StockNewsBean> newsList = new ArrayList<>();
    private List<StockNewsBean> announceList = new ArrayList<>();
    private JoDialog dialog = null;

    UMImage image = new UMImage(MarketDetailActivity.this, Constants.iconResourece);
    private String shareContent = Constants.marketShareContent;
    private String shareTitle = Constants.marketShareTitle;
    private String mUrl = Constants.marketShareUrl;
    private PresentScorePresenter sharepresenter;
    /**
     * 分享的平台数组
     */
    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };
    private RadioButton minSBtn;
    private RadioButton dayKBtn;
    private String optionalID;

    /**
     * 三方平台的分享
     */
    private void societyShare() {
        setDialog();
        new ShareAction(this).setDisplayList(displaylist)
                .withText(shareContent)
                .withTitle(shareTitle)
                .withTargetUrl(mUrl)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
        //关闭log和toast
        Config.OpenEditor = false;
//        Log.LOG = false;
        Config.IsToastTip = false;
        Config.dialog.dismiss();

    }

    /**
     * 设置三方跳转的时候的dialog
     */
    private void setDialog() {
        JoDialog dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        Config.dialog = dialog;
    }


    /**
     * 分享的监听器
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(MarketDetailActivity.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(MarketDetailActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(MarketDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    LocalBroadcastManager lbm;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshDataService.TAG)) {
//                Log.d("mark", "MarketFragment网络状态已经改变\n" + intent.getStringExtra("mark"));
//                mTvSubtitle.setText(DateUtils.stockStatus() + "  " +
//                        DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
                if (ticCode.length() == 6) {
                    mTvSubtitle.setVisibility(View.VISIBLE);
                    mTvSubtitle.setText(DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
                } else {
                    mTvSubtitle.setVisibility(View.GONE);
                }
                initData();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        lbm = LocalBroadcastManager.getInstance(getApplicationContext());
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof OptionalStockBean) {
            OptionalStockBean stockBean = (OptionalStockBean) event.getObject();
            ticCode = stockBean.getCode();
            title = stockBean.getName();
        } else if (event.getObject() instanceof HotTicBean) {
            ticCode = ((HotTicBean) event.getObject()).getCode_id();
            title = ((HotTicBean) event.getObject()).getCode_name();
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(RefreshDataService.TAG);
        lbm.registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        presenter.destroy();
    }

    @Override
    public MarketDetailPresenter initPresenter() {
        return new MarketDetailPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_market_detail;
    }

    @Override
    protected void initView() {
        sharepresenter = new PresentScorePresenter(this);
        mToolbarTitle.setText(title + " ( " + ticCode + " )");
        if (DateUtils.stockStatus().equals("交易中")) {
            isOpenStatus = true;
        }

//        mTvSubtitle.setText(DateUtils.stockStatus() + "  " +
//                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
        if (ticCode.length() == 6) {
            mTvSubtitle.setVisibility(View.VISIBLE);
            mTvSubtitle.setText(DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
        } else {
            mTvSubtitle.setVisibility(View.GONE);
        }
        View content = LayoutInflater.from(this).inflate(R.layout.layout_market_detail_content, null);

        minSBtn = (RadioButton) content.findViewById(R.id.rb_chart_0);
        dayKBtn = (RadioButton) content.findViewById(R.id.rb_chart_1);
//        mTvAddSelfStock = (TextView) content.findViewById(R.id.tv_add_self_stock);
        marketDetaiHolder = new ViewHolder(content);
        mXsvMarketDetail.setView(content);
        mXsvMarketDetail.setPullLoadEnable(false);
        mXsvMarketDetail.setIXScrollViewListener(this);
        initCharts(content);
        initFragments();
        initContentViews(content);
        initData();
    }

    /**
     * 初始化图表的控件
     */
    private void initCharts(View view) {
        mRgChartChoice = ((RadioGroup) view.findViewById(R.id.rg_chart_choice));
        mFlChart = (FrameLayout) view.findViewById(R.id.fl_chart);
        if (6 == ticCode.length()) {
            minSBtn.setVisibility(View.VISIBLE);
            minSBtn.setChecked(true);
            addChart(MINUTES_TYPE);
        } else {
            mIbShare.setVisibility(View.GONE);
            minSBtn.setVisibility(View.GONE);
            dayKBtn.setChecked(true);
            addChart(DAY_K_TYPE);
        }
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mIbShare.setOnClickListener(this);
    }

    private void initContentViews(View content) {
        mRgChartChoice.setOnCheckedChangeListener(this);
        ((RadioGroup) content.findViewById(R.id.rg_simple_status)).setOnCheckedChangeListener(this);
        ((RadioGroup) content.findViewById(R.id.rg_news)).setOnCheckedChangeListener(this);
        mBtnClick = (Button) content.findViewById(R.id.btn_click);
        mBtnClick.setOnClickListener(this);
        content.findViewById(R.id.ll_3).setVisibility(View.GONE);
        mSlvNews = (ScrollListView) content.findViewById(R.id.slv_news);
        mRbNews = (RadioButton) content.findViewById(R.id.rb_news);
        mRbAnnounce = (RadioButton) content.findViewById(R.id.rb_announce);
        mTvAddSelfStock.setOnClickListener(this);

        if (ticCode.length() == 5) {
//            ((TextView) content.findViewById(R.id.tv_buy_in)).setText(getString(R.string.share));
            mTvBuyIn.setText(getString(R.string.share));
        }
//        (content.findViewById(R.id.tv_buy_in)).setOnClickListener(this);
        mTvBuyIn.setOnClickListener(this);

        newsAdapter = new NewsAdapter(this, R.layout.item_stock_news, newsList);
        mSlvNews.setAdapter(newsAdapter);
        mSlvNews.setFocusable(false);
        mSlvNews.setSelector(android.R.color.transparent);
        mSlvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mRbNews.isChecked()) {
                    EventBus.getDefault().postSticky(new AnyEventType(newsList.get(position).getUrl())
                            .setTid(mRbNews.getText().toString()));
                } else {
                    EventBus.getDefault().postSticky(new AnyEventType(announceList.get(position).getUrl())
                            .setTid(mRbAnnounce.getText().toString()));
                }
                startActivity(new Intent(getApplicationContext(), NewsDetailActivity.class));
            }
        });

        queryNews();
    }

    protected void initData() {
        if (ticCode.length() == 6) {
            presenter.queryTicInfo(ticCode);
        } else {
            presenter.queryHkTicInfo(ticCode);
        }
        presenter.queryMarketDetail(_mApplication.getUserInfo().getSessionID(), ticCode);
    }

    private void queryNews() {
        if (ticCode.length() == 6) {
            presenter.queryHsNewsList(newType, ticCode, "1", "5");
        } else if (ticCode.length() == 5) {
            presenter.queryHkNewsList(newType, ticCode, "1", "5");
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
        lbm.unregisterReceiver(mReceiver);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.btn_click:
                if (mRbNews.isChecked()) {
                    EventBus.getDefault().postSticky(new AnyEventType(newsList).setTid(ticCode).setState(true)); // 新闻
                } else if (mRbAnnounce.isChecked()) {
                    EventBus.getDefault().postSticky(new AnyEventType(announceList).setTid(ticCode).setState(false)); // 公告
                }
                startActivity(new Intent(this, NewsActivity.class));
                break;
            case R.id.ib_share:
                societyShare();
                break;
            case R.id.tv_add_self_stock:
                if (mTvAddSelfStock.getText().toString().startsWith("+")) {
                    presenter.addSelfStock(_mApplication.getUserInfo().getSessionID(), ticCode);
                } else {
                    presenter.delSelfStock(_mApplication.getUserInfo().getSessionID(), optionalID);
                }
                break;
            case R.id.tv_buy_in:
                if (ticCode.length() == 6) {
                    if (_mApplication.getUserInfo().getIsLogin() == 1) {
                        EventBus.getDefault().postSticky(new AnyEventType(ticCode).setTid(getString(R.string.buy_in)));
                        startActivity(new Intent(this, BuyInActivity.class));
                    } else {
                        startActivity(new Intent(this, LoginActivity.class));
                    }
                } else {
                    societyShare();
                }
                break;
//            default:
//                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_chart_choice:
                switch (checkedId) {
                    case R.id.rb_chart_0:
                        addChart(MINUTES_TYPE);
                        break;
                    case R.id.rb_chart_1:
                        addChart(DAY_K_TYPE);
                        break;
                    case R.id.rb_chart_2:
                        addChart(WEEK_K_TYPE);
                        break;
                    case R.id.rb_chart_3:
                        addChart(MONTH_K_TYPE);
                        break;
                }
                break;
            case R.id.rg_news:
                switch (checkedId) {
                    case R.id.rb_news:
                        newsAdapter.setData(newsList);
                        if (newsList.size() == 0) {
                            newType = "1";
                            queryNews();
                            mBtnClick.setVisibility(View.INVISIBLE);
                        } else {
                            Log.d(TAG, "------------->news");
                            mBtnClick.setVisibility(View.VISIBLE);
                        }
                        break;
                    case R.id.rb_announce:
                        newsAdapter.setData(announceList);
                        if (announceList.size() == 0) {
                            newType = "2";
                            Log.d(TAG, "announceList: " + announceList.size());
                            if (dialog == null) {
                                dialog = new JoDialog.Builder(this)
                                        .setViewRes(R.layout.layout_loading)
                                        .setBgRes(Color.TRANSPARENT)
                                        .show(false);
                            }
                            queryNews();
                            mBtnClick.setVisibility(View.INVISIBLE);
                        } else {
                            Log.d(TAG, "------------->announce");
                            mBtnClick.setVisibility(View.VISIBLE);
                        }
                        break;
                }
                break;
            case R.id.rg_simple_status:
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (checkedId) {
                    case R.id.rb_brief:
                        transaction.hide(fragments[fragmentIndex]).show(fragments[0]);
                        fragmentIndex = 0;
                        break;
                    case R.id.rb_finance:
                        transaction.hide(fragments[fragmentIndex]).show(fragments[1]);
                        fragmentIndex = 1;
                        break;
                    case R.id.rb_shareholders:
                        transaction.hide(fragments[fragmentIndex]).show(fragments[2]);
                        fragmentIndex = 2;
                        break;
                }
                transaction.commit();
                break;

        }
    }


    /**
     * 添加行情图
     */
    private void addChart(int chartType) {
        if (null == chartViews) chartViews = new ArrayList<>();
        switch (chartType) {
            case MINUTES_TYPE:
                Log.i(TAG, "onCheckedChanged: 0");
                if (chartViews.contains(minutesView)) {
                    for (View mView : chartViews) {
                        mView.setVisibility(View.GONE);
                    }
                    minutesView.setVisibility(View.VISIBLE);
                } else {
                    if (chartViews.size() != 0) {
                        for (View mView : chartViews) {
                            mView.setVisibility(View.GONE);
                        }
                    }
                    final int[] height = {0};
                    ViewTreeObserver vto = mFlChart.getViewTreeObserver();
                    vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            mFlChart.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            height[0] = mFlChart.getHeight();
                            minutesPage = new MinutesPage(MarketDetailActivity.this, height[0]);
                            minutesPage.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                            minutesPage.setShowMarketEnable(false);
//                            MinutesPage.initData();
                            minutesPage.setOnTouchEanable(false);
                            minutesPage.requestData(Constants.MinutesType.HS, ticCode);
                            minutesView = minutesPage.getmContentView();
                            chartViews.add(minutesView);
                            mFlChart.addView(minutesView);
                            minutesView.setVisibility(View.VISIBLE);
                            minutesView.setOnClickListener(v -> {
                                EventBus.getDefault().postSticky(new AnyEventType(marketData).setTid(ticCode).setType(0));
                                startActivity(new Intent(_Activity, BigStockChatActivity.class));
                            });
                        }
                    });
                }
                break;
            case DAY_K_TYPE:
                Log.i(TAG, "onCheckedChanged: 1");
                if (chartViews.contains(daykLineView)) {
                    for (View mView : chartViews) {
                        mView.setVisibility(View.GONE);
                    }
                    daykLineView.setVisibility(View.VISIBLE);
                } else {
                    if (chartViews.size() != 0) {
                        for (View mView : chartViews) {
                            mView.setVisibility(View.GONE);
                        }
                    }
                    dayKLinePage = new KLinePage(this, View.GONE);
                    dayKLinePage.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                    dayKLinePage.setShowMarketEnable(false);
                    dayKLinePage.requestData(Constants.kLineType.DAY, ticCode);
//                    dayKLinePage.initData();
                    dayKLinePage.setOnTouchEnable(false);
                    daykLineView = dayKLinePage.getmContentView();
                    chartViews.add(daykLineView);
                    mFlChart.addView(daykLineView);
                    daykLineView.setVisibility(View.VISIBLE);
                    daykLineView.setOnClickListener(v -> {
                        EventBus.getDefault().postSticky(new AnyEventType(marketData).setTid(ticCode).setType(1));
                        startActivity(new Intent(_Activity, BigStockChatActivity.class));
                    });

                }
                break;
            case WEEK_K_TYPE:
                Log.i(TAG, "onCheckedChanged: 2");
                if (chartViews.contains(weekKLineView)) {
                    for (View mView : chartViews) {
                        mView.setVisibility(View.GONE);
                    }
                    weekKLineView.setVisibility(View.VISIBLE);
                } else {
                    if (chartViews.size() != 0) {
                        for (View mView : chartViews) {
                            mView.setVisibility(View.GONE);
                        }
                    }
                    weekKLinePage = new KLinePage(this, View.GONE);
                    weekKLinePage.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
//                    dayKLinePage.initData();
                    weekKLinePage.setShowMarketEnable(false);
                    weekKLinePage.requestData(Constants.kLineType.WEEK, ticCode);
                    weekKLinePage.setOnTouchEnable(false);
                    weekKLineView = weekKLinePage.getmContentView();
                    chartViews.add(weekKLineView);
                    mFlChart.addView(weekKLineView);
                    weekKLineView.setVisibility(View.VISIBLE);
                    weekKLineView.setOnClickListener(v -> {
                        EventBus.getDefault().postSticky(new AnyEventType(marketData).setTid(ticCode).setType(2));
                        startActivity(new Intent(_Activity, BigStockChatActivity.class));
                    });

                }
                break;
            case MONTH_K_TYPE:
                Log.i(TAG, "onCheckedChanged: 3");
                if (chartViews.contains(MonthKLineView)) {
                    for (View mView : chartViews) {
                        mView.setVisibility(View.GONE);
                    }
                    MonthKLineView.setVisibility(View.VISIBLE);
                } else {
                    if (chartViews.size() != 0) {
                        for (View mView : chartViews) {
                            mView.setVisibility(View.GONE);
                        }
                    }
                    MonthLinePage = new KLinePage(this, View.GONE);
                    MonthLinePage.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                    MonthLinePage.setShowMarketEnable(false);
//                    dayKLinePage.initData();
                    MonthLinePage.requestData(Constants.kLineType.MONTH, ticCode);
                    MonthLinePage.setOnTouchEnable(false);
                    MonthKLineView = MonthLinePage.getmContentView();
                    chartViews.add(MonthKLineView);
                    mFlChart.addView(MonthKLineView);
                    MonthKLineView.setVisibility(View.VISIBLE);
                    MonthKLineView.setOnClickListener(v -> {
                        EventBus.getDefault().postSticky(new AnyEventType(marketData).setTid(ticCode).setType(3));
                        startActivity(new Intent(_Activity, BigStockChatActivity.class));
                    });
                }
                break;
        }

    }


    /**
     * 初始化某只股票的简况，财务，股东的fragment
     */

    private void initFragments() {
        fragments[0] = BriefFragment.newInstance();
        fragments[1] = CorpFinanceFragment.newInstance();
        fragments[2] = ShareholdersFragment.newInstance();

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (BaseFragment fragment : fragments) {
            transaction.add(R.id.fl_content, fragment, fragment.getClass().getSimpleName()).hide(fragment);
        }
        transaction.show(fragments[0]);
        transaction.commit();
    }

    @Override
    public void onSuccess(MarketDetailType requestType, Object object) {
        mXsvMarketDetail.stopRefresh();
        switch (requestType) {
            case STOCK_INFO_INTRU:
                CompDetailBean compDetailBean = (CompDetailBean) object;
                fragments[0].setData(compDetailBean.getIntroduct());
                fragments[1].setData(compDetailBean.getFinance());
                fragments[2].setData(compDetailBean.getShareholder());
                break;

            case ADD_SELF_SELECT:
                MessageBean msgBean = (MessageBean) object;
                if (msgBean.getMessage().contains("登录")) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else if (msgBean.getMessage().contains("成功")){
//                    mTvAddSelfStock.setText(getString(R.string.del_self_select));
                    presenter.queryMarketDetail(_mApplication.getUserInfo().getSessionID(), ticCode);
                }
                ToastUtil.showShort(this, msgBean.getMessage());
                break;
            case DEL_SELF_SELECT:
                MessageBean msg1Bean = (MessageBean) object;
                if (msg1Bean.getMessage().contains("登录")) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else if (msg1Bean.getMessage().contains("成功")) {
                    mTvAddSelfStock.setText(getString(R.string.add_self_select));
                }
                ToastUtil.showShort(this, msg1Bean.getMessage());
                break;
            case QUERY_MARKET_DETAIL:
                setMarketData(object);
                if (6 == ticCode.length()) {
                    minutesPage.setBuySellData(object);
                }
                this.marketData = (MarketDetailBean) object;
                marketData.setStockName(title);
                break;
            case QUERY_HK_NEWS_LIST:
            case QUERY_HS_NEWS_LIST:
                List<StockNewsBean> newsBeanList = (List<StockNewsBean>) object;
                if (mRbNews.isChecked()) {
                    if (newsList.size() == 0) {
                        newsList.clear();
                        newsList.addAll(newsBeanList);
                        Log.d(TAG, "newsList: " + newsList.size());
//                    newsAdapter.setData(newsList);
                        newsAdapter = new NewsAdapter(this, R.layout.item_stock_news, newsList);
                        mSlvNews.setAdapter(newsAdapter);
                        if (newsBeanList.size() == 0) {
                            mBtnClick.setVisibility(View.INVISIBLE);
                        } else {
                            mBtnClick.setVisibility(View.VISIBLE);
                        }
                    }
                } else if (mRbAnnounce.isChecked()) {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    if (announceList.size() == 0) {
                        announceList.clear();
                        announceList.addAll(newsBeanList);
                        Log.d(TAG, "newsBeanList: " + newsBeanList.size());
                        Log.d(TAG, "announceList: " + announceList.size());
//                    newsAdapter.setData(announceList);
                        newsAdapter = new NewsAdapter(this, R.layout.item_stock_news, announceList);
                        mSlvNews.setAdapter(newsAdapter);
                        if (newsBeanList.size() == 0) {
                            mBtnClick.setVisibility(View.INVISIBLE);
                        } else {
                            mBtnClick.setVisibility(View.VISIBLE);
                        }
                    }
                }

                break;

            case HK_STOCK_INFO_INTRU:
                HKCompDetailBean hkCompDetailBean = (HKCompDetailBean) object;
                fragments[0].setData(hkCompDetailBean);
                if (null != hkCompDetailBean) {
                    fragments[2].setData(hkCompDetailBean.getHolder());
                }
                break;
        }
    }


    /**
     * 设置详情数据
     */
    public void setMarketData(Object object) {
        MarketDetailBean marketDetailBean = (MarketDetailBean) object;
        marketDetailBean = new ClassUtils<>(marketDetailBean).initField(MarketDetailBean.class);
        Log.d(TAG, "setMarketData: " + marketDetailBean.getLastPrice());
        optionalID = marketDetailBean.getOptional_id();
        marketDetaiHolder.mTvRatio.setText(getString(R.string.change_hand));
        marketDetaiHolder.mTvTodayOpen.setText(marketDetailBean.getOpenprice());
        marketDetaiHolder.mTvLastClose.setText(marketDetailBean.getPrecloprice());
        marketDetaiHolder.mTvTrading.setText(marketDetailBean.getVolume());
        marketDetaiHolder.mTvRat.setText(marketDetailBean.getTurnover_rate() + "%");
        marketDetaiHolder.mTvHeighest.setText(marketDetailBean.getHeightprice());
        marketDetaiHolder.mTvLowest.setText(marketDetailBean.getLowPrice());
        marketDetaiHolder.mTvTurnVolume.setText(marketDetailBean.getTurnover());
        marketDetaiHolder.mTvInvol.setText(marketDetailBean.getInvol());
        marketDetaiHolder.mTvOuterDisc.setText(marketDetailBean.getOuter());
        marketDetaiHolder.mTvMarketValue.setText(marketDetailBean.getAll_worth());
        marketDetaiHolder.mTvPeRatio.setText(marketDetailBean.getPE());
        marketDetaiHolder.mTvSwing.setText(marketDetailBean.getAmplitude());
        marketDetaiHolder.mTvCirculateMarket.setText(marketDetailBean.getCircula_worth());
        marketDetaiHolder.mTvCurPrice.setText(marketDetailBean.getLastPrice());
        try {
            float zf = Float.valueOf(marketDetailBean.getZf());
            if (zf > 0) {
                marketDetaiHolder.mTvUpDownPrice.setText("+" + marketDetailBean.getZf());
                marketDetaiHolder.mTvUpDownRatio.setText("+" + marketDetailBean.getZfl() + "%");
                marketDetaiHolder.mTvCurPrice.setTextColor(getResources().getColor(R.color.market_red));
                marketDetaiHolder.mTvUpDownPrice.setTextColor(getResources().getColor(R.color.market_red));
                marketDetaiHolder.mTvUpDownRatio.setTextColor(getResources().getColor(R.color.market_red));
            } else if (zf == 0) {
                marketDetaiHolder.mTvUpDownPrice.setText("+" + marketDetailBean.getZf());
                marketDetaiHolder.mTvUpDownRatio.setText("+" + marketDetailBean.getZfl() + "%");
                marketDetaiHolder.mTvCurPrice.setTextColor(getResources().getColor(R.color.market_tittle_gray_color));
                marketDetaiHolder.mTvUpDownPrice.setTextColor(getResources().getColor(R.color.market_tittle_gray_color));
                marketDetaiHolder.mTvUpDownRatio.setTextColor(getResources().getColor(R.color.market_tittle_gray_color));
            } else {
                marketDetaiHolder.mTvUpDownPrice.setText("" + marketDetailBean.getZf());
                marketDetaiHolder.mTvUpDownRatio.setText("" + marketDetailBean.getZfl() + "%");
                marketDetaiHolder.mTvCurPrice.setTextColor(getResources().getColor(R.color.market_green));
                marketDetaiHolder.mTvUpDownPrice.setTextColor(getResources().getColor(R.color.market_green));
                marketDetaiHolder.mTvUpDownRatio.setTextColor(getResources().getColor(R.color.market_green));
            }
            if (ticCode.length() == 5) {
                marketDetaiHolder.mTvTitleInvol.setText(getString(R.string.in_vol_52));
                marketDetaiHolder.mTvTitleOuter.setText(getString(R.string.outer_vol_52));
            } else if (ticCode.length() == 6) {
                marketDetaiHolder.mTvTitleInvol.setText(getString(R.string.in_vol));
                marketDetaiHolder.mTvTitleOuter.setText(getString(R.string.outer_vol));
            }
        } catch (Exception e) {
            Log.e(TAG, "setMarketData: ", e);
        }

        if (marketDetailBean.getOptional() == 1) {
            mTvAddSelfStock.setText(getString(R.string.del_self_select));
        } else {
            mTvAddSelfStock.setText(getString(R.string.add_self_select));
        }

//        if (ticCode.length() == 6) {
//            if (marketDetailBean.getTradingPhaseCode() != null) {
//                switch (marketDetailBean.getTradingPhaseCode()) {
//                    case "0":
//                        mTvSubtitle.setText("交易中  " +
//                                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
//                        break;
//                    case "1":
//                        mTvSubtitle.setText("休市中  " +
//                                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
//                        break;
//                    case "2":
//                        mTvSubtitle.setText("已闭市  " +
//                                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
//                        break;
//                    case "3":
//                        mTvSubtitle.setText("已停牌  " +
//                                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
//                        break;
//                }
//
//            }
//
//            if (marketDetailBean.getDeletionIndicator() != null) {
//                switch (marketDetailBean.getDeletionIndicator()) {
//                    case "0":
//                        mTvSubtitle.setText("交易中  " +
//                                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
//                        break;
//                    case "1":
//                        mTvSubtitle.setText("休市中  " +
//                                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
//                        break;
//                    case "2":
//                        mTvSubtitle.setText("已闭市  " +
//                                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
//                        break;
//                }
//            }
//        }

    }


    @Override
    public void onError(MarketDetailType requestType, Throwable t) {
        mXsvMarketDetail.stopRefresh();
        if (dialog != null) {
            dialog.dismiss();
        }
        switch (requestType) {
            case QUERY_HK_NEWS_LIST:
            case QUERY_HS_NEWS_LIST:

                if (mRbNews.isChecked()) {
                    newsAdapter = new NewsAdapter(this, R.layout.item_stock_news, newsList);
                    mSlvNews.setAdapter(newsAdapter);
                    if (newsList.size() == 0) {
                        mBtnClick.setVisibility(View.INVISIBLE);
                    } else {
                        mBtnClick.setVisibility(View.VISIBLE);
                    }
                } else {
                    newsAdapter = new NewsAdapter(this, R.layout.item_stock_news, announceList);
                    mSlvNews.setAdapter(newsAdapter);
                    if (announceList.size() == 0) {
                        mBtnClick.setVisibility(View.INVISIBLE);
                    } else {
                        mBtnClick.setVisibility(View.VISIBLE);
                    }
                }
                break;
        }
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {

    }

    static class ViewHolder {
        @Bind(R.id.tv_cur_price)
        TextView mTvCurPrice;
        @Bind(R.id.tv_up_down_price)
        TextView mTvUpDownPrice;
        @Bind(R.id.tv_up_down_ratio)
        TextView mTvUpDownRatio;
        @Bind(R.id.tv_today_open)
        TextView mTvTodayOpen;
        @Bind(R.id.tv_last_close)
        TextView mTvLastClose;
        @Bind(R.id.tv_trading)
        TextView mTvTrading;
        @Bind(R.id.tv_ratio)
        TextView mTvRatio;
        @Bind(R.id.tv_rat)
        TextView mTvRat;
        @Bind(R.id.tv_heighest)
        TextView mTvHeighest;
        @Bind(R.id.tv_lowest)
        TextView mTvLowest;
        @Bind(R.id.tv_turn_volume)
        TextView mTvTurnVolume;
        @Bind(R.id.tv_title_invol)
        TextView mTvTitleInvol;
        @Bind(R.id.tv_invol)
        TextView mTvInvol;
        @Bind(R.id.tv_title_outer)
        TextView mTvTitleOuter;
        @Bind(R.id.tv_outer_disc)
        TextView mTvOuterDisc;
        @Bind(R.id.tv_market_value)
        TextView mTvMarketValue;
        @Bind(R.id.tv_pe_ratio)
        TextView mTvPeRatio;
        @Bind(R.id.tv_swing)
        TextView mTvSwing;
        @Bind(R.id.tv_circulate_market)
        TextView mTvCirculateMarket;
        @Bind(R.id.tv_zjs)
        TextView mTvZjs;
        @Bind(R.id.tv_pjs)
        TextView mTvPjs;
        @Bind(R.id.tv_djs)
        TextView mTvDjs;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }


    }
}
