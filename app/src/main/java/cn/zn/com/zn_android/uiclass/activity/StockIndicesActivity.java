package cn.zn.com.zn_android.uiclass.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.SelfSelectAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HKRankListBean;
import cn.zn.com.zn_android.model.bean.MarketDetailBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.ShCfgBean;
import cn.zn.com.zn_android.model.bean.ShSzDetailBean;
import cn.zn.com.zn_android.model.factory.StockRankingFactory;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.presenter.StockIndicesPresenter;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.service.RefreshDataService;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.page.KLinePage;
import cn.zn.com.zn_android.uiclass.page.MinutesPage;
import cn.zn.com.zn_android.uiclass.xlistview.XScrollView;
import cn.zn.com.zn_android.utils.ClassUtils;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.viewfeatures.StockIndicesView;
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
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/7/13 0013.
 */
public class StockIndicesActivity extends BaseMVPActivity<StockIndicesView, StockIndicesPresenter>
        implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, StockIndicesView,
        AdapterView.OnItemClickListener, XScrollView.IXScrollViewListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_subtitle)
    TextView mTvSubtitle;
    @Bind(R.id.ib_share)
    ImageButton mIbShare;
    TextView mTvCurPrice;
    TextView mTvUpDownPrice;
    TextView mTvUpDownRatio;
    TextView mTvTodayOpen;
    TextView mTvTrading;
    TextView mTvRat;

    private TextView mTvLastClose;
    private TextView mTvHeighest;
    private TextView mTvLowest;
    private TextView mTvTurnVolume;
    private TextView mTvZjs;
    private TextView mTvPjs;
    private TextView mTvDjs;
    private RadioButton mRbBrief, mRbFinance, mRbShareHolders;

    //    @Bind(R.id.ll_data_show)
    LinearLayout mLlDataShow;
    //    @Bind(R.id.rb_chart_0)
    RadioButton mRbChart0;
    //    @Bind(R.id.rb_chart_1)
    RadioButton mRbChart1;
    //    @Bind(R.id.rb_chart_2)
    RadioButton mRbChart2;
    //    @Bind(R.id.rb_chart_3)
    RadioButton mRbChart3;
    //    @Bind(R.id.fl_chart)
    FrameLayout mFlChart;
    ScrollListView mLvStockIndices;
    @Bind(R.id.xsv_stock_indices)
    XScrollView mXsvStockIndices;

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
    private String title;
    private boolean isOpenStatus = false;
    private String constiStoIndex = "1";
    private String marketType;
    private SelfSelectAdapter adapter;
    private MarketDetailBean marketDetailBean = new MarketDetailBean();

    private List<MarketImp> upList = new ArrayList<>();
    private List<MarketImp> downList = new ArrayList<>();
    private List<MarketImp> changeList = new ArrayList<>();


    /**
     * 分享的平台数组
     */
    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };
    private String shareContent = Constants.indexShareContent;
    private String shareTitle = Constants.indexShareTitle;
    private String mUrl = Constants.indexShareUrl;
    UMImage image = new UMImage(StockIndicesActivity.this, Constants.iconResourece);
    private PresentScorePresenter sharepresenter;

    LocalBroadcastManager lbm;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshDataService.TAG)) {
//                Log.d("mark", TAG + "网络状态已经改变\n" + intent.getStringExtra("mark"));
                mTvSubtitle.setText(DateUtils.stockStatus() + "  " +
                        DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
                queryData();
            }
        }
    };
    private RadioButton minSBtn;
    private RadioButton dayKBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        lbm = LocalBroadcastManager.getInstance(getApplicationContext());
    }

    @Override
    public StockIndicesPresenter initPresenter() {
        return new StockIndicesPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_stock_indices;
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            title = event.getObject().toString();
            marketType = event.getTid();
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
        lbm.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {
        sharepresenter = new PresentScorePresenter(this);
        mToolbarTitle.setText(title);
        if (DateUtils.stockStatus().equals("交易中")) {
            isOpenStatus = true;
        }
        mTvSubtitle.setText(DateUtils.stockStatus() + "  " +
                DateUtils.getStringDate(System.currentTimeMillis(), "MM-dd HH:mm"));
        View content = LayoutInflater.from(this).inflate(R.layout.layout_stock_indices_content, null);
        minSBtn = (RadioButton) content.findViewById(R.id.rb_chart_0);
        dayKBtn = (RadioButton) content.findViewById(R.id.rb_chart_1);
        initContentView(content);
        mXsvStockIndices.setView(content);
        mXsvStockIndices.setPullLoadEnable(false);
        mXsvStockIndices.setIXScrollViewListener(this);
    }

    private void initContentView(View content) {
        mLvStockIndices = (ScrollListView) content.findViewById(R.id.lv_stock_indices);
        adapter = new SelfSelectAdapter(this, R.layout.item_self_show, upList);
        mLvStockIndices.setAdapter(adapter);
        mLvStockIndices.setFocusable(false);

        ((RadioGroup) content.findViewById(R.id.rg_simple_status)).setOnCheckedChangeListener(this);
        ((RadioGroup) content.findViewById(R.id.rg_chart_choice)).setOnCheckedChangeListener(this);
        content.findViewById(R.id.ll_1).setVisibility(View.GONE);
        content.findViewById(R.id.ll_2).setVisibility(View.GONE);
        mRbBrief = (RadioButton) content.findViewById(R.id.rb_brief);
        mRbFinance = (RadioButton) content.findViewById(R.id.rb_finance);
        mRbShareHolders = (RadioButton) content.findViewById(R.id.rb_shareholders);

        mTvCurPrice = (TextView) content.findViewById(R.id.tv_cur_price);
        mTvUpDownPrice = (TextView) content.findViewById(R.id.tv_up_down_price);
        mTvUpDownRatio = (TextView) content.findViewById(R.id.tv_up_down_ratio);
        mTvTodayOpen = (TextView) content.findViewById(R.id.tv_today_open);
        mTvLastClose = (TextView) content.findViewById(R.id.tv_last_close);
        mTvTrading = (TextView) content.findViewById(R.id.tv_trading);
        mTvRat = (TextView) content.findViewById(R.id.tv_rat);
        mTvHeighest = (TextView) content.findViewById(R.id.tv_heighest);
        mTvLowest = (TextView) content.findViewById(R.id.tv_lowest);
        mTvTurnVolume = (TextView) content.findViewById(R.id.tv_turn_volume);
        mTvZjs = (TextView) content.findViewById(R.id.tv_zjs);
        mTvPjs = (TextView) content.findViewById(R.id.tv_pjs);
        mTvDjs = (TextView) content.findViewById(R.id.tv_djs);

        queryData();

        initCharts(content);

    }

    /**
     * 初始化图表的控件
     */
    private void initCharts(View view) {
        mFlChart = (FrameLayout) view.findViewById(R.id.fl_chart);
        if (marketType == "SZ" || marketType == "SZCZ" || marketType == "CY") {
            minSBtn.setVisibility(View.VISIBLE);
            minSBtn.setChecked(true);
            addChart(MINUTES_TYPE);
        } else {
            minSBtn.setVisibility(View.GONE);
            dayKBtn.setChecked(true);
            addChart(DAY_K_TYPE);
        }
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mIbShare.setOnClickListener(this);
        mLvStockIndices.setOnItemClickListener(this);
    }

    private void queryData() {
        if (title.equals(getString(R.string.sse))) {
            Log.d(TAG, "queryData: " + title);
            presenter.queryShDetail();
            presenter.queryShCFG("1");
        } else if (title.equals(getString(R.string.szstock))) {
            presenter.querySzDetail("0");
            presenter.queryShCFG("2");
        } else if (title.equals(getString(R.string.gem))) {
            presenter.querySzDetail("1");
            presenter.queryShCFG("3");
        } else if (title.equals(getString(R.string.hs_index))) {
            presenter.queryHkUpdownHs(constiStoIndex);
            presenter.queryHkHsDetail();
        } else if (title.equals(getString(R.string.gq_index))) {
            Log.d(TAG, "queryData: " + title + "  " + constiStoIndex);
            presenter.queryHkUpdownGq(constiStoIndex);
            presenter.queryHkGqDetail();
        } else if (title.equals(getString(R.string.hc_index))) {
            presenter.queryHkUpdownHc(constiStoIndex);
            presenter.queryHkHcDetail();
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
            case R.id.rg_simple_status:
                switch (checkedId) {
                    case R.id.rb_brief:
                        constiStoIndex = "1";
                        if (upList.size() == 0) {
                            queryData();
                        } else {
                            adapter.setData(upList, 1);
                        }
                        break;
                    case R.id.rb_finance:
                        constiStoIndex = "2";
                        if (downList.size() == 0) {
                            queryData();
                        } else {
                            adapter.setData(downList, 1);
                        }
                        break;
                    case R.id.rb_shareholders:
                        constiStoIndex = "3";
                        if (changeList.size() == 0) {
                            queryData();
                        } else {
                            adapter.setData(changeList, 1);
                        }
                        break;
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.ib_share:
                societyShare();
                break;
        }
    }


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
            Toast.makeText(StockIndicesActivity.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(StockIndicesActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(StockIndicesActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
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

    /**
     * 添加行情图
     */
    private void addChart(int chartType) {
        if (null == chartViews) chartViews = new ArrayList<>();
        switch (chartType) {
            case MINUTES_TYPE:
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
                            minutesPage = new MinutesPage(StockIndicesActivity.this, height[0]);
                            minutesPage.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
                            minutesPage.requestData(Constants.MinutesType.INDEX, marketType);
                            minutesPage.setOnTouchEanable(false);
                            minutesView = minutesPage.getmContentView();
                            chartViews.add(minutesView);
                            mFlChart.addView(minutesView);
                            minutesView.setVisibility(View.VISIBLE);
                            minutesView.setOnClickListener(v -> {
                                EventBus.getDefault().postSticky(new AnyEventType(marketDetailBean).setTid(marketType).setType(0));
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
                    dayKLinePage.requestData(Constants.kLineType.DAY, marketType);
                    dayKLinePage.setOnTouchEnable(false);
                    daykLineView = dayKLinePage.getmContentView();
                    chartViews.add(daykLineView);
                    mFlChart.addView(daykLineView);
                    daykLineView.setVisibility(View.VISIBLE);
                    daykLineView.setOnClickListener(v -> {
                        EventBus.getDefault().postSticky(new AnyEventType(marketDetailBean).setTid(marketType).setType(1));
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
                    weekKLinePage.requestData(Constants.kLineType.WEEK, marketType);
                    weekKLinePage.setOnTouchEnable(false);
                    weekKLineView = weekKLinePage.getmContentView();
                    chartViews.add(weekKLineView);
                    mFlChart.addView(weekKLineView);
                    weekKLineView.setVisibility(View.VISIBLE);
                    weekKLineView.setOnClickListener(v -> {
                        EventBus.getDefault().postSticky(new AnyEventType(marketDetailBean).setTid(marketType).setType(2));
                        startActivity(new Intent(_Activity, BigStockChatActivity.class));
                    });

                }
                break;
            case MONTH_K_TYPE:
                Log.i(TAG, "onCheckedChanged: 2");
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
//                    dayKLinePage.initData();
                    MonthLinePage.setShowMarketEnable(false);
                    MonthLinePage.requestData(Constants.kLineType.MONTH, marketType);
                    MonthLinePage.setOnTouchEnable(false);
                    MonthKLineView = MonthLinePage.getmContentView();
                    chartViews.add(MonthKLineView);
                    mFlChart.addView(MonthKLineView);
                    MonthKLineView.setVisibility(View.VISIBLE);
                    MonthKLineView.setOnClickListener(v -> {
                        EventBus.getDefault().postSticky(new AnyEventType(marketDetailBean).setTid(marketType).setType(3));
                        startActivity(new Intent(_Activity, BigStockChatActivity.class));
                    });

                }
                break;
        }

    }


    /**
     * 将ShSzDetailBean转换为marketBean
     */
    private void translateData(ShSzDetailBean bean) {
        marketDetailBean.setStockName(title);
        marketDetailBean.setLastPrice(bean.getLastIndex());
        marketDetailBean.setOpenprice(bean.getOpenIndex());
        marketDetailBean.setHeightprice(bean.getHighIndex());
        marketDetailBean.setLowPrice(bean.getLowIndex());
        marketDetailBean.setVolume(bean.getTradVolume());
        marketDetailBean.setAmplitude(bean.getZfl());
        marketDetailBean.setZf(bean.getChange());
        marketDetailBean.setZfl(bean.getZfl());
        marketDetailBean = new ClassUtils<>(marketDetailBean).initField(MarketDetailBean.class);
    }

    private void updateDetailUi(ShSzDetailBean bean) {
        mTvCurPrice.setText(bean.getLastIndex());
        Log.d(TAG, "updateDetailUi: " + bean.getLastIndex());
        if (bean.getChangeRate().startsWith("-")) {
            if (isOpenStatus) {
                mTvCurPrice.setTextColor(getResources().getColor(R.color.green_down));
                mTvUpDownPrice.setTextColor(getResources().getColor(R.color.green_down));
                mTvUpDownRatio.setTextColor(getResources().getColor(R.color.green_down));
            } else {
                mTvCurPrice.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvUpDownPrice.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvUpDownRatio.setTextColor(getResources().getColor(R.color.font_value_black));
            }

            mTvUpDownPrice.setText(bean.getChange());
            mTvUpDownRatio.setText(bean.getChangeRate() + "%");
        } else {
            if (isOpenStatus) {
                mTvCurPrice.setTextColor(getResources().getColor(R.color.app_bar_color));
                mTvUpDownPrice.setTextColor(getResources().getColor(R.color.app_bar_color));
                mTvUpDownRatio.setTextColor(getResources().getColor(R.color.app_bar_color));
            } else {
                mTvCurPrice.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvUpDownPrice.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvUpDownRatio.setTextColor(getResources().getColor(R.color.font_value_black));
            }

            mTvUpDownPrice.setText("+" + bean.getChange());
            mTvUpDownRatio.setText("+" + bean.getChangeRate() + "%");
        }

        mTvTodayOpen.setText(bean.getOpenIndex());
        mTvLastClose.setText(bean.getPreCloseIndex());
        mTvTrading.setText(bean.getTradVolume());
        mTvRat.setText(bean.getZfl() + "%");
        mTvHeighest.setText(bean.getHighIndex());
        mTvLowest.setText(bean.getLowIndex());
        mTvTurnVolume.setText(bean.getTurnover());
        mTvZjs.setText(bean.getZjs() + "");
        mTvPjs.setText(bean.getPjs() + "");
        mTvDjs.setText(bean.getDjs() + "");
    }

    @Override
    public void onSuccess(StockRequestType requestType, Object object) {
        mXsvStockIndices.stopRefresh();
        switch (requestType) {
            case QUERY_SH_DETAIL:
            case QUERY_SZ_DETAIL:
                ShSzDetailBean szDetail = (ShSzDetailBean) object;
                updateDetailUi(szDetail);
                translateData(szDetail);
                break;

            case QUERY_HK_UPDOWN_HC_UP:
            case QUERY_HK_UPDOWN_GQ_UP:
            case QUERY_HK_UPDOWN_HS_UP:
                List<HKRankListBean> hsUpBean = (List<HKRankListBean>) object;
                Log.d(TAG, "onSuccess: " + hsUpBean.size());
                upList.clear();
                upList.addAll(StockRankingFactory.HKToMarketImp(hsUpBean, Constants.HK));
                adapter.setData(upList, Constants.HK);
                break;

            case QUERY_HK_UPDOWN_HC_DOWN:
            case QUERY_HK_UPDOWN_GQ_DOWN:
            case QUERY_HK_UPDOWN_HS_DOWN:
                List<HKRankListBean> hsDownBean = (List<HKRankListBean>) object;
                Log.d(TAG, "onSuccess: " + hsDownBean.size());
                downList.clear();
                downList.addAll(StockRankingFactory.HKToMarketImp(hsDownBean, Constants.HK));
                adapter.setData(downList, Constants.HK);
                break;

            case QUERY_HK_UPDOWN_HC_CHANGE:
            case QUERY_HK_UPDOWN_GQ_CHANGE:
            case QUERY_HK_UPDOWN_HS_CHANGE:
                List<HKRankListBean> hsChangeBean = (List<HKRankListBean>) object;
                Log.d(TAG, "onSuccess: " + hsChangeBean.size());
                changeList.clear();
                changeList.addAll(StockRankingFactory.HKToMarketImp(hsChangeBean, Constants.HK));
                adapter.setData(changeList, Constants.HK);
                break;

            case QUERY_SH_UPDOWN_REM:
            case QUERY_SH_UPDOWN_SZ:
            case QUERY_SH_UPDOWN_SSE:
                ShCfgBean sseCfg = (ShCfgBean) object;
                upList.clear();
                downList.clear();
                changeList.clear();
                upList.addAll(StockRankingFactory.ZfToMarketImp(sseCfg.getZfb()));
                downList.addAll(StockRankingFactory.ZfToMarketImp(sseCfg.getDfb()));
                changeList.addAll(StockRankingFactory.HslToMarketImp(sseCfg.getHsl()));
                if (mRbBrief.isChecked()) {
                    adapter.setData(upList, Constants.SH);
                } else if (mRbFinance.isChecked()) {
                    adapter.setData(downList, Constants.SH);
                } else if (mRbShareHolders.isChecked()) {
                    adapter.setData(changeList, Constants.SH);
                }
                break;

        }
    }

    @Override
    public void onError(StockRequestType requestType, Throwable t) {
        mXsvStockIndices.stopRefresh();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionalStockBean stockBean = new OptionalStockBean();
        if (mRbBrief.isChecked()) {
            stockBean = (OptionalStockBean) upList.get(position);
        } else if (mRbFinance.isChecked()) {
            stockBean = (OptionalStockBean) downList.get(position);
        } else if (mRbShareHolders.isChecked()) {
            stockBean = (OptionalStockBean) changeList.get(position);
        }
        EventBus.getDefault().postSticky(new AnyEventType(stockBean));
        startActivity(new Intent(_Activity, MarketDetailActivity.class));
    }

    @Override
    public void onRefresh() {
        queryData();
    }

    @Override
    public void onLoadMore() {

    }
}
