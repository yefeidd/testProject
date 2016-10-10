package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MarketDetailBean;
import cn.zn.com.zn_android.model.chartBean.ChartImp;
import cn.zn.com.zn_android.model.chartBean.KLineBean;
import cn.zn.com.zn_android.model.chartBean.MinutesBean;
import cn.zn.com.zn_android.uiclass.listener.MyChartListener;
import cn.zn.com.zn_android.uiclass.page.KLinePage;
import cn.zn.com.zn_android.uiclass.page.MinutesPage;
import cn.zn.com.zn_android.utils.ClassUtils;
import cn.zn.com.zn_android.utils.UnitUtils;
import com.github.mikephil.charting.components.YAxis;
import com.umeng.analytics.MobclickAgent;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * 股票全屏图
 * Created by Jolly on 2016/7/19 0019.
 */
public class BigStockChatActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, MyChartListener {
    private static final String TAG = "BigStockChatActivity";

    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.tv_code)
    TextView mTvCode;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_up_down)
    TextView mTvUpDown;
    @Bind(R.id.tv_up_down_rate)
    TextView mTvUpDownRate;
    @Bind(R.id.rl_price)
    RelativeLayout mRlPrice;
    @Bind(R.id.rg_chart_choice)
    RadioGroup mRgChartChoice;
    @Bind(R.id.fl_chart)
    FrameLayout mFlChart;
    @Bind(R.id.rb_chart_0)
    RadioButton mRbChart0;
    @Bind(R.id.rb_chart_1)
    RadioButton mRbChart1;
    @Bind(R.id.rb_chart_2)
    RadioButton mRbChart2;
    @Bind(R.id.rb_chart_3)
    RadioButton mRbChart3;
    @Bind(R.id.tv_heighest)
    TextView mTvHeighest;
    @Bind(R.id.tv_close_deal)
    TextView mTvCloseDeal;
    @Bind(R.id.tv_lowest)
    TextView mTvLowest;
    @Bind(R.id.tv_today_open)
    TextView mTvTodayOpen;
    @Bind(R.id.tv_change_hand)
    TextView mTvChangeHand;
    @Bind(R.id.tv_swing)
    TextView mTvSwing;

    private ArrayList<View> chartViews;
    private final int MINUTES_TYPE = 0;
    private final int DAY_K_TYPE = 1;
    private final int WEEK_K_TYPE = 2;
    private final int MONTH_K_TYPE = 3;
    private String ticCode;
    private View minutesView;
    private View daykLineView, weekKLineView, monthKLineView;
    private MinutesPage minutesPage;
    private KLinePage dayKLinePage, weekKLinePage, monthLinePage;
    private MarketDetailBean marketDetailBean;

    private int chartIndex = MINUTES_TYPE;
    private boolean isRestart = false;
    private RadioButton minSBtn;
    private RadioButton dayKBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_stock_chat);
        ButterKnife.bind(this);
        if (null == savedInstanceState) {
            isRestart = false;
//            chartIndex = MINUTES_TYPE;
        } else if (0 == savedInstanceState.getInt(Constants.FROM)) {
            isRestart = false;
            chartIndex = MINUTES_TYPE;

        } else {
            isRestart = true;
            chartIndex = savedInstanceState.getInt(Constants.FROM);
            Log.d(TAG, "onCreate: chartIndex" + chartIndex);
        }
        initView();
        initEvent();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(Constants.FROM, chartIndex);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() != null) {
            marketDetailBean = (MarketDetailBean) event.getObject();
        }
        chartIndex = event.getType();
        ticCode = event.getTid();
    }

    protected void initView() {
        mTvCode.setText(ticCode);
        minSBtn = (RadioButton) findViewById(R.id.rb_chart_0);
        dayKBtn = (RadioButton) findViewById(R.id.rb_chart_1);
        if (6 == ticCode.length() || ticCode == "SZ" || ticCode == "SZCZ" || ticCode == "CY") {
            minSBtn.setVisibility(View.VISIBLE);
            dayKBtn.setChecked(false);
        } else {
            minSBtn.setVisibility(View.GONE);
            dayKBtn.setChecked(true);
        }
        addChart(chartIndex);
        marketDetailBean = new ClassUtils<>(marketDetailBean).initField(MarketDetailBean.class);
        if (marketDetailBean != null) {
            initTitleData();
        }
        mRbChart0.setChecked(MINUTES_TYPE == chartIndex);
        mRbChart1.setChecked(DAY_K_TYPE == chartIndex);
        mRbChart2.setChecked(WEEK_K_TYPE == chartIndex);
        mRbChart3.setChecked(MONTH_K_TYPE == chartIndex);
    }

    protected void initEvent() {
        mRgChartChoice.setOnCheckedChangeListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_close:
                finish();
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
                Log.i(TAG, "addChart: 0");
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
                            minutesPage = new MinutesPage(BigStockChatActivity.this, height[0]);
                            minutesPage.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//                            MinutesPage.initData();
                            minutesPage.requestData(Constants.MinutesType.HS, ticCode);
                            minutesView = minutesPage.getmContentView();
                            chartViews.add(minutesView);
                            mFlChart.addView(minutesView);
                            if (ticCode.length() == 6) {
                                minutesPage.setBuySellData(marketDetailBean);
                            }
                            minutesView.setVisibility(View.VISIBLE);
                            minutesPage.setMyChartListener(BigStockChatActivity.this);
                        }
                    });
                }
                break;
            case DAY_K_TYPE:
                Log.i(TAG, "addChart: 1");
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
                    dayKLinePage = new KLinePage(this, View.VISIBLE);
                    dayKLinePage.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//                    dayKLinePage.initData();
                    dayKLinePage.requestData(Constants.kLineType.DAY, ticCode);
                    daykLineView = dayKLinePage.getmContentView();
                    chartViews.add(daykLineView);
                    mFlChart.addView(daykLineView);
                    daykLineView.setVisibility(View.VISIBLE);
                    dayKLinePage.setMyChartListener(this);

                }
                break;
            case WEEK_K_TYPE:
                Log.i(TAG, "addChart: 2");
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
                    weekKLinePage = new KLinePage(this, View.VISIBLE);
                    weekKLinePage.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//                    dayKLinePage.initData();
                    weekKLinePage.requestData(Constants.kLineType.WEEK, ticCode);
                    weekKLineView = weekKLinePage.getmContentView();
                    chartViews.add(weekKLineView);
                    mFlChart.addView(weekKLineView);
                    weekKLineView.setVisibility(View.VISIBLE);
                    weekKLinePage.setMyChartListener(this);
                }
                break;
            case MONTH_K_TYPE:
                Log.i(TAG, "addChart: 3");
                if (chartViews.contains(monthKLineView)) {
                    for (View mView : chartViews) {
                        mView.setVisibility(View.GONE);
                    }
                    monthKLineView.setVisibility(View.VISIBLE);
                } else {
                    if (chartViews.size() != 0) {
                        for (View mView : chartViews) {
                            mView.setVisibility(View.GONE);
                        }
                    }
                    monthLinePage = new KLinePage(this, View.VISIBLE);
                    monthLinePage.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//                    dayKLinePage.initData();
                    monthLinePage.requestData(Constants.kLineType.MONTH, ticCode);
                    monthKLineView = monthLinePage.getmContentView();
                    chartViews.add(monthKLineView);
                    mFlChart.addView(monthKLineView);
                    monthKLineView.setVisibility(View.VISIBLE);
                    monthLinePage.setMyChartListener(this);

                }
                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_chart_0:
                if (!isRestart) {
                    chartIndex = MINUTES_TYPE;
                } else {
                    isRestart = !isRestart;
                }
                Log.d(TAG, "onCheckedChanged: " + MINUTES_TYPE);
                break;
            case R.id.rb_chart_1:
                chartIndex = DAY_K_TYPE;
                Log.d(TAG, "onCheckedChanged: " + DAY_K_TYPE);
                break;
            case R.id.rb_chart_2:
                chartIndex = WEEK_K_TYPE;
                Log.d(TAG, "onCheckedChanged: " + WEEK_K_TYPE);
                break;
            case R.id.rb_chart_3:
                chartIndex = MONTH_K_TYPE;
                Log.d(TAG, "onCheckedChanged: " + MONTH_K_TYPE);
                break;

        }
        addChart(chartIndex);

    }

    @Override
    public void move(ChartImp chartBean) {
        if (null != chartBean) {
            if (chartBean instanceof KLineBean) {
                KLineBean kLineBean = (KLineBean) chartBean;
                mTvPrice.setText(String.valueOf(kLineBean.close));
                float upDown = kLineBean.close - kLineBean.pre;
                if (upDown > 0) {
                    mTvUpDown.setText("+" + String.valueOf(UnitUtils.getTwoDecimal(upDown)));
                    mTvUpDown.setTextColor(getResources().getColor(R.color.market_red));
                    mTvPrice.setTextColor(getResources().getColor(R.color.market_red));
                    mTvUpDownRate.setText("+" + new DecimalFormat("#0.00%").format(upDown / kLineBean.pre));
                    mTvUpDownRate.setTextColor(getResources().getColor(R.color.market_red));
                } else {
                    mTvUpDown.setText(String.valueOf(UnitUtils.getTwoDecimal(upDown)));
                    mTvUpDownRate.setText(new DecimalFormat("#0.00%").format(upDown / kLineBean.pre));
                    mTvUpDown.setTextColor(getResources().getColor(R.color.market_green));
                    mTvPrice.setTextColor(getResources().getColor(R.color.market_green));
                    mTvUpDownRate.setTextColor(getResources().getColor(R.color.market_green));
                }
                mTvHeighest.setText(String.valueOf(kLineBean.high));
                mTvLowest.setText(String.valueOf(kLineBean.low));
                String vol = UnitUtils.getVol(kLineBean.vol) + UnitUtils.getVolUnit1(kLineBean.vol);
                mTvCloseDeal.setText(vol);
                mTvTodayOpen.setText(String.valueOf(kLineBean.open));
                mTvChangeHand.setText("--");
                mTvSwing.setText("--");
            } else {
                MinutesBean minutesBean = (MinutesBean) chartBean;
                mTvPrice.setText(String.valueOf(minutesBean.LastPrice));
                float upDown = Float.valueOf(minutesBean.getZf());
                if (upDown > 0) {
                    mTvUpDown.setText("+" + String.valueOf(UnitUtils.getTwoDecimal(upDown)));
                    mTvUpDown.setTextColor(getResources().getColor(R.color.market_red));
                    mTvPrice.setTextColor(getResources().getColor(R.color.market_red));
                    mTvUpDownRate.setText("+" + minutesBean.zf_rate + "%");
                    mTvUpDownRate.setTextColor(getResources().getColor(R.color.market_red));
                } else {
                    mTvUpDown.setText(String.valueOf(UnitUtils.getTwoDecimal(upDown)));
                    mTvUpDownRate.setText(minutesBean.zf_rate + "%");
                    mTvUpDown.setTextColor(getResources().getColor(R.color.market_green));
                    mTvPrice.setTextColor(getResources().getColor(R.color.market_green));
                    mTvUpDownRate.setTextColor(getResources().getColor(R.color.market_green));
                }
                mTvHeighest.setText(UnitUtils.clacUnit(minutesBean.getHighPrice()));
                mTvLowest.setText(UnitUtils.clacUnit(minutesBean.getLowPrice()));
                mTvCloseDeal.setText(String.valueOf(UnitUtils.getVol(minutesBean.getVol())) + UnitUtils.getVolUnit1(minutesBean.getVol()));
            }
        }
    }

    @Override
    public void cancelMove() {
        initTitleData();
    }


    /**
     * 初始化头数据
     */
    public void initTitleData() {
        mTvName.setText(marketDetailBean.getStockName());
        mTvPrice.setText(marketDetailBean.getLastPrice());
        float upDown;
        try {
            upDown = Float.valueOf(marketDetailBean.getZf());
            if (upDown > 0) {
                mTvUpDown.setText("+" + marketDetailBean.getZf());
                mTvUpDown.setTextColor(getResources().getColor(R.color.market_red));
                mTvPrice.setTextColor(getResources().getColor(R.color.market_red));
                mTvUpDownRate.setTextColor(getResources().getColor(R.color.market_red));
                mTvUpDownRate.setText("+" + marketDetailBean.getZfl() + "%");
            } else {
                mTvUpDown.setText(marketDetailBean.getZf());
                mTvUpDown.setTextColor(getResources().getColor(R.color.market_green));
                mTvPrice.setTextColor(getResources().getColor(R.color.market_green));
                mTvUpDownRate.setTextColor(getResources().getColor(R.color.market_green));
                mTvUpDownRate.setText(marketDetailBean.getZfl() + "%");
            }
        } catch (Exception e) {
            mTvUpDown.setText(marketDetailBean.getZf());
            mTvUpDownRate.setText(marketDetailBean.getZfl());
        }
        mTvHeighest.setText(marketDetailBean.getHeightprice());
        mTvLowest.setText(marketDetailBean.getLowPrice());
        mTvCloseDeal.setText(marketDetailBean.getVolume());
        mTvTodayOpen.setText(marketDetailBean.getOpenprice());
        setViewRateData(mTvChangeHand, marketDetailBean.getTurnover_rate());
        setViewRateData(mTvSwing, marketDetailBean.getAmplitude());
    }


    /**
     * 给View设置数据，如果是数据是--，不加上百分号。
     */
    public void setViewRateData(TextView textView, String value) {
        if (value.equals("--")) {
            textView.setText(value);
        } else {
            textView.setText(value + "%");
        }

    }
}
