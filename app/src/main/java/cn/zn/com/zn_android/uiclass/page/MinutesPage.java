package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.MinuteStockRradingAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.MarketDetailBean;
import cn.zn.com.zn_android.model.bean.MinuteTradingBean;
import cn.zn.com.zn_android.model.chartBean.MinutesBean;
import cn.zn.com.zn_android.presenter.ChartParse;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyBarChart;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyLeftMarkerView;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyLineChart;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyRightMarkerView;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyXAxis;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyYAxis;
import cn.zn.com.zn_android.uiclass.listener.MyChartListener;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.UnitUtils;
import cn.zn.com.zn_android.utils.VolFormatter;

/**
 * Created by zjs on 2016/7/13 0013.
 * email: m15267280642@163.com
 * explain:
 */
public class MinutesPage extends BaseChartPage {


    protected MyLineChart mLineChart;
    protected MyBarChart mBarChart;
    protected ScrollListView mLvTrading;
    protected LineDataSet d1, d2;
    protected BarDataSet barDataSet;
    protected MyXAxis xAxisLine;
    protected MyYAxis axisRightLine;
    protected MyYAxis axisLeftLine;
    protected MyXAxis xAxisBar;
    protected MyYAxis axisLeftBar;
    protected MyYAxis axisRightBar;
    protected SparseArray<String> stringSparseArray;
    protected float mItemHeight;
    private ChartParse mData;
    private boolean mShowBarLeftLable = true;
    protected MyChartListener myChartListener;
    private List<MinuteTradingBean> currentMarket = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            setData(mData);
            mPbLoad.setVisibility(View.GONE);
            mChart.setVisibility(View.VISIBLE);
        }
    };
    private LinearLayout mChart;
    private ProgressBar mPbLoad;

    public MinutesPage(Activity activity, int height) {
        super(activity);
        mItemHeight = (height - DensityUtil.px2dip(mActivity, 50f)) / 10f;
    }

    @Override
    public View initView() {
        View minutesView = LayoutInflater.from(mActivity).inflate(R.layout.page_minutes_chart, new LinearLayout(mActivity), false);
        mLineChart = (MyLineChart) minutesView.findViewById(R.id.line_chart);
        mBarChart = (MyBarChart) minutesView.findViewById(R.id.bar_chart);
        mLvTrading = (ScrollListView) minutesView.findViewById(R.id.lv_trading);
        mChart = (LinearLayout) minutesView.findViewById(R.id.chart);
        mPbLoad = (ProgressBar) minutesView.findViewById(R.id.pb_load);
        mData = new ChartParse(mActivity);
        initPage();
        return minutesView;
    }

    private static final String TAG = "MinutesPage";

    @Override
    public void initPage() {
        mLineChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                mBarChart.highlightValues(new Highlight[]{h});
                mLineChart.setHighlightValue(h);
                if (null != myChartListener) {
                    myChartListener.move(mData.getDatas().get(h.getXIndex()));
                }
            }

            @Override
            public void onNothingSelected() {
                mBarChart.highlightValue(null);
                if (null != myChartListener) {
                    myChartListener.cancelMove();
                }
            }
        });
        mBarChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                //  barChart.highlightValues(new Highlight[]{h});
                mLineChart.setHighlightValue(new Highlight(h.getXIndex(), 0));//此函数已经返回highlightBValues的变量，并且刷新，故上面方法可以注释
                // barChart.setHighlightValue(h);
                if (null != myChartListener) {
                    myChartListener.move(mData.getDatas().get(h.getXIndex()));
                }
            }

            @Override
            public void onNothingSelected() {
                mLineChart.highlightValue(null);
                if (null != myChartListener) {
                    myChartListener.cancelMove();
                }
            }
        });

        mLineChart.setScaleEnabled(false);
        mLineChart.setDrawBorders(true);
        mLineChart.setBorderWidth(0.5f);
        mLineChart.setBackgroundColor(Color.WHITE);
        mLineChart.setDrawingCacheBackgroundColor(mActivity.getResources().
                getColor(R.color.minute_zhoutv)
        );
//        mLineChart.setBorderColor(mActivity.getResources().getColor(R.color.minute_grayLine));
        mLineChart.setBorderColor(mActivity.getResources().
                getColor(R.color.minute_zhoutv)
        );
        mLineChart.setDescription("");
        mLineChart.setAutoScaleMinMaxEnabled(false);
        Legend lineChartLegend = mLineChart.getLegend();
        lineChartLegend.setEnabled(false);

        mBarChart.setScaleEnabled(false);
        mBarChart.setDrawBorders(true);
        mBarChart.setBorderWidth(0.5f);
        mBarChart.setBackgroundColor(Color.WHITE);
        mBarChart.setAutoScaleMinMaxEnabled(true);
//        mBarChart.setBorderColor(mActivity.getResources().getColor(R.color.minute_grayLine));
        mBarChart.setBorderColor(mActivity.getResources().
                getColor(R.color.minute_zhoutv)
        );
        mBarChart.setDescription("");


        Legend barChartLegend = mBarChart.getLegend();
        barChartLegend.setEnabled(false);
        //x轴
        xAxisLine = mLineChart.getXAxis();
        xAxisLine.setDrawLabels(true);
        xAxisLine.setPosition(XAxis.XAxisPosition.BOTTOM);
        // xAxisLine.setLabelsToSkip(59);


        //左边y
        axisLeftLine = mLineChart.getAxisLeft();
        /*折线图y轴左没有basevalue，调用系统的*/
        axisLeftLine.setLabelCount(5, true);
        axisLeftLine.setDrawLabels(true);
        axisLeftLine.setMaxColor(mActivity.getResources().
                getColor(R.color.market_red)
        );
        axisLeftLine.setMinColor(mActivity.getResources().
                getColor(R.color.market_green)
        );
        axisLeftLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisLeftLine.setDrawGridLines(false);
        /*轴不显示 避免和border冲突*/
        axisLeftLine.setDrawAxisLine(false);


        //右边y
        axisRightLine = mLineChart.getAxisRight();
        axisRightLine.setLabelCount(2, true);
        axisRightLine.setDrawLabels(true);
        axisRightLine.setMaxColor(mActivity.getResources().
                getColor(R.color.market_red)
        );
        axisRightLine.setMinColor(mActivity.getResources().
                getColor(R.color.market_green)
        );
        axisRightLine.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightLine.setValueFormatter(new YAxisValueFormatter() {
                                            @Override
                                            public String getFormattedValue(float value, YAxis yAxis) {
                                                DecimalFormat mFormat = new DecimalFormat("#0.00%");
                                                return mFormat.format(value);
                                            }
                                        }
        );

        axisRightLine.setStartAtZero(false);
        axisRightLine.setDrawGridLines(false);
        axisRightLine.setDrawAxisLine(false);
        //背景线
        xAxisLine.setGridColor(mActivity.getResources().

                getColor(R.color.minute_zhoutv)

        );
        xAxisLine.setAxisLineColor(mActivity.getResources().

                getColor(R.color.minute_zhoutv)

        );
        xAxisLine.setTextColor(mActivity.getResources().

                getColor(R.color.market_detail_font_color)

        );
        axisLeftLine.setGridColor(mActivity.getResources().

                getColor(R.color.minute_zhoutv)

        );
        axisLeftLine.setTextColor(mActivity.getResources().

                getColor(R.color.market_detail_font_color)

        );
        axisRightLine.setAxisLineColor(mActivity.getResources().

                getColor(R.color.minute_zhoutv)

        );
        axisRightLine.setTextColor(mActivity.getResources().

                getColor(R.color.market_detail_font_color)

        );

        //bar x y轴
        xAxisBar = mBarChart.getXAxis();
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(true);
        xAxisBar.setDrawAxisLine(false);
        // xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(mActivity.getResources().

                getColor(R.color.market_detail_font_color)

        );
        axisLeftBar = mBarChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
//        axisLeftBar.setTextColor(mActivity.getResources().getColor(R.color.minute_black));
        axisLeftBar.setTextColor(mActivity.getResources().

                getColor(R.color.market_detail_font_color)

        );


        axisRightBar = mBarChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        //y轴样式
        this.axisLeftLine.setValueFormatter(new YAxisValueFormatter() {
                                                @Override
                                                public String getFormattedValue(float value, YAxis yAxis) {
                                                    DecimalFormat mFormat = new DecimalFormat("#0.00");
                                                    return mFormat.format(value);
                                                }
                                            }
        );

    }


    @Override
    public void initData() {
        Message msg = Message.obtain();
        msg.what = 0;
        handler.sendMessage(msg);
    }


    /**
     * 请求数据
     */
    public void requestData(Constants.MinutesType type, String ticCode) {

        if (ticCode.length() != 6) {
            mLvTrading.setVisibility(View.GONE);
        }
        mPbLoad.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.GONE);
        if (mData == null) {
            mData = new ChartParse(mActivity);
        }
        mData.setChartPage(this);
        mData.requestMinData(ticCode);
    }

    /**
     * 设置最新的买卖信息
     */
    public void setBuySellData(Object object) {
        currentMarket.clear();
        MarketDetailBean marketDetailBean = (MarketDetailBean) object;
        List<MarketDetailBean.ToSellBean> sellData;
        List<MarketDetailBean.ToBuyBean> buyData;
        sellData = marketDetailBean.getTo_sell();
        buyData = marketDetailBean.getTo_buy();
        int count = marketDetailBean.getTo_sell().size();
        for (int i = 0; i < 5; i++) {
            MinuteTradingBean sellBean = new MinuteTradingBean();
            sellBean.setTitle("卖" + (count - i));
            if (null == sellData || sellData.size() == 0 || "" == sellData.get(i).getPrice()) {
                sellBean.setPrice("--");
                sellBean.setNumber("--");
            } else {
                sellBean.setPrice(sellData.get(i).getPrice());
                sellBean.setNumber("" + sellData.get(i).getVolume());
            }
            currentMarket.add(sellBean);
        }

        for (int i = 0; i < 5; i++) {
            MinuteTradingBean buyBean = new MinuteTradingBean();
            buyBean.setTitle("买" + (i + 1));
            if (null == buyData || buyData.size() == 0 || "" == buyData.get(i).getPrice()) {
                buyBean.setPrice("--");
                buyBean.setNumber("--");
            } else {
                buyBean.setPrice(buyData.get(i).getPrice());
                buyBean.setNumber("" + buyData.get(i).getVolume());
            }
            currentMarket.add(buyBean);
        }
    }

    /**
     * 这是显示图标上的坐标轴
     */
    public void setShowLabels(SparseArray<String> labels) {
        xAxisLine.setXLabels(labels);
        xAxisBar.setXLabels(labels);
    }

    private void setMarkerView(ChartParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(mActivity, R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(mActivity, R.layout.mymarkerview);
        int leftColors[] = {mActivity.getResources().getColor(R.color.chart_blue_1), mActivity.getResources().getColor(R.color.market_red)};
        int rightColors[] = {mActivity.getResources().getColor(R.color.market_detail_font_color)};
        leftMarkerView.setColors(leftColors);
        rightMarkerView.setColors(rightColors);
        mLineChart.setMarker(leftMarkerView, rightMarkerView, mData);
    }

    private void setData(ChartParse mData) {
        setTradingData();
        setMarkerView(mData);
        stringSparseArray = setXLabels();
        setShowLabels(stringSparseArray);
        Log.e("###", mData.getDatas().size() + "ee");
        if (mData.getDatas().size() == 0) {
            mLineChart.setNoDataText("未开盘");
            return;
        }
        //设置y左右两轴最大最小值
        axisLeftLine.setAxisMinValue(mData.getMin());
        axisLeftLine.setAxisMaxValue(mData.getMax());
        axisLeftLine.setShowOnlyMinMax(true);
        axisRightLine.setAxisMinValue(mData.getPercentMin());
        axisRightLine.setAxisMaxValue(mData.getPercentMax());
        axisRightLine.setShowOnlyMinMax(true);
        axisLeftBar.setAxisMaxValue(mData.getVolmax());
        /*单位*/
        String unit = UnitUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        /*次方*/
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        axisLeftBar.setShowMaxAndUnit(unit);
//        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setDrawLabels(mShowBarLeftLable);
        //axisLeftBar.setAxisMinValue(0);//即使最小是不是0，也无碍
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar.setAxisMaxValue(mData.getVolmax());
        //   axisRightBar.setAxisMinValue(mData.getVolmin);//即使最小是不是0，也无碍
        //axisRightBar.setShowOnlyMinMax(true);

        //基准线
        LimitLine ll = new LimitLine(0);
        ll.setLineWidth(1f);
        ll.setLineColor(mActivity.getResources().getColor(R.color.minute_jizhun));
        ll.enableDashedLine(10f, 10f, 0f);
        ll.setLineWidth(1);
        axisRightLine.addLimitLine(ll);
        axisRightLine.setBaseValue(0);

        ArrayList<Entry> lineCJEntries = new ArrayList<>();
        ArrayList<Entry> lineJJEntries = new ArrayList<>();
        ArrayList<String> dateList = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();
        Log.e("##", Integer.toString(xVals.size()));
        for (int i = 0, j = 0; i < mData.getDatas().size(); i++, j++) {
           /* //避免数据重复，skip也能正常显示
            if (mData.getDatas().get(i).time.equals("13:30")) {
                continue;
            }*/
            MinutesBean t = mData.getDatas().get(j);

            if (t == null) {
                lineCJEntries.add(new Entry(Float.NaN, i));
                lineJJEntries.add(new Entry(Float.NaN, i));
                barEntries.add(new BarEntry(Float.NaN, i));
                continue;
            }
            if (!TextUtils.isEmpty(stringSparseArray.get(i)) &&
                    stringSparseArray.get(i).contains("/")) {
                i++;
            }
            if (i < mData.getDatas().size()) {
                lineCJEntries.add(new Entry(mData.getDatas().get(i).LastPrice, i));
                lineJJEntries.add(new Entry(mData.getDatas().get(i).average, i));
                barEntries.add(new BarEntry(mData.getDatas().get(i).vol, i));
            } else {
                return;
            }
            // dateList.add(mData.getDatas().get(i).time);
        }
        d1 = new LineDataSet(lineCJEntries, "成交价");
        d2 = new LineDataSet(lineJJEntries, "均价");
        d1.setDrawValues(false);
        d2.setDrawValues(false);
        barDataSet = new BarDataSet(barEntries, "成交量");

        d1.setCircleRadius(0);
        d2.setCircleRadius(0);
        d1.setColor(mActivity.getResources().getColor(R.color.minute_blue));
        d1.setLineWidth(0.5f);
        d2.setColor(mActivity.getResources().getColor(R.color.market_red));
        d2.setLineWidth(0.5f);
//        d1.setHighLightColor(Color.BLACK);
        d1.setHighLightColor(Color.BLACK);
        d2.setHighlightEnabled(false);
        d1.setDrawFilled(true);


        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setDrawValues(false);
        barDataSet.setHighlightEnabled(true);
        barDataSet.setColor(mActivity.getResources().getColor(R.color.market_red));
        List<Integer> list = new ArrayList<>();
        list.add(mActivity.getResources().getColor(R.color.market_red));
        list.add(mActivity.getResources().getColor(R.color.market_green));
        barDataSet.setColors(list);
        //谁为基准
        d1.setAxisDependency(YAxis.AxisDependency.LEFT);
        // d2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(d1);
        sets.add(d2);
        /*注老版本LineData参数可以为空，最新版本会报错，修改进入ChartData加入if判断*/
        LineData cd = new LineData(getMinutesCount(), sets);
        mLineChart.setData(cd);
        BarData barData = new BarData(getMinutesCount(), barDataSet);
        mBarChart.setData(barData);
        setOffset();
//        syncCharts(mLineChart, new Chart[]{mBarChart});
        mLineChart.invalidate();//刷新图
        mBarChart.invalidate();

    }


    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = mLineChart.getViewPortHandler().offsetLeft();
        float barLeft = mBarChart.getViewPortHandler().offsetLeft();
        float lineRight = mLineChart.getViewPortHandler().offsetRight();
        float barRight = mBarChart.getViewPortHandler().offsetRight();
        float barBottom = mBarChart.getViewPortHandler().offsetBottom();
        float lineBottom = mLineChart.getViewPortHandler().offsetBottom();
        float offsetLeft, offsetRight;
//        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        if (barLeft < lineLeft) {
            offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            mBarChart.setExtraLeftOffset(offsetLeft);
//            transLeft = lineLeft;

        } else {
            offsetLeft = Utils.convertPixelsToDp(barLeft - lineLeft);
            mLineChart.setExtraLeftOffset(offsetLeft);
//            transLeft = barLeft;
        }

  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (barRight < lineRight) {
            offsetRight = Utils.convertPixelsToDp(lineRight);
            mBarChart.setExtraRightOffset(offsetRight);
//            transRight = lineRight;
        } else {
            offsetRight = Utils.convertPixelsToDp(barRight);
            mLineChart.setExtraRightOffset(offsetRight);
//            transRight = barRight;
        }
//        mLineChart.setViewPortOffsets(transLeft, 30, transRight, lineBottom);
//        mBarChart.setViewPortOffsets(transLeft, 15, transRight, barBottom);

    }


    /**
     * 设置X轴的坐标
     */
    private SparseArray<String> setXLabels() {
        SparseArray<String> xLabels = new SparseArray<>();
        xLabels.put(0, "09:30");
        xLabels.put(60, "10:30");
        xLabels.put(120, "11:30/13:00");
        xLabels.put(180, "14:00");
        xLabels.put(240, "15:00");
        return xLabels;
    }

    /**
     * 分时的数组
     */
    public String[] getMinutesCount() {
        return new String[241];
    }

    /**
     * 设置是否显示market
     */
    public void setShowMarketEnable(boolean flag) {
        mLineChart.setShowMarketEnable(flag);
    }

    /**
     * 设置图表不可触摸
     */
    public void setOnTouchEanable(boolean flag) {
        mLineChart.setTouchEnabled(flag);
        mBarChart.setTouchEnabled(flag);
    }

    public void setTradingData() {
        MinuteStockRradingAdapter tradingAdapter = new MinuteStockRradingAdapter(mActivity, R.layout.item_stock_trading, currentMarket, mItemHeight);
        mLvTrading.setAdapter(tradingAdapter);
    }


    public void setPosition(YAxis.YAxisLabelPosition pos) {
        axisLeftLine.setPosition(pos);
        axisLeftBar.setPosition(pos);
        if (YAxis.YAxisLabelPosition.INSIDE_CHART.equals(pos)) {
            mShowBarLeftLable = false;
        } else {
            mShowBarLeftLable = true;

        }
    }


    public void setMyChartListener(MyChartListener myChartListener) {
        this.myChartListener = myChartListener;
    }


    public void syncCharts(Chart srcChart, Chart[] dstCharts) {
        Matrix srcMatrix;
        Matrix dstMatrix;
        float[] srcVals = new float[9];
        float[] dstVals = new float[9];

        srcMatrix = srcChart.getViewPortHandler().getMatrixTouch();
        srcMatrix.getValues(srcVals);
        // apply X axis scaling and position to dst charts:
        for (Chart dstChart : dstCharts) {
            if (dstChart.getVisibility() == View.VISIBLE) {
//            if (dstChart.getVisibility() != View.GONE) {
                dstMatrix = dstChart.getViewPortHandler().getMatrixTouch();
                dstMatrix.getValues(dstVals);

                dstVals[Matrix.MSCALE_X] = srcVals[Matrix.MSCALE_X];
                dstVals[Matrix.MSKEW_X] = srcVals[Matrix.MSKEW_X];
                dstVals[Matrix.MTRANS_X] = srcVals[Matrix.MTRANS_X];
                dstVals[Matrix.MSKEW_Y] = srcVals[Matrix.MSKEW_Y];
                dstVals[Matrix.MSCALE_Y] = srcVals[Matrix.MSCALE_Y];
                dstVals[Matrix.MTRANS_Y] = srcVals[Matrix.MTRANS_Y];
                dstVals[Matrix.MPERSP_0] = srcVals[Matrix.MPERSP_0];
                dstVals[Matrix.MPERSP_1] = srcVals[Matrix.MPERSP_1];
                dstVals[Matrix.MPERSP_2] = srcVals[Matrix.MPERSP_2];

                dstMatrix.setValues(dstVals);
                dstChart.getViewPortHandler().refresh(dstMatrix, dstChart, true);
            }
        }

    }
}
