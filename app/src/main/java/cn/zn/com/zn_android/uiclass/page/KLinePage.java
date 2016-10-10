package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.chartBean.KLineBean;
import cn.zn.com.zn_android.presenter.ChartParse;
import cn.zn.com.zn_android.uiclass.customerview.customChart.CoupleChartGestureListener;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyCombinedChart;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyKLineChart;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyLeftMarkerView;
import cn.zn.com.zn_android.uiclass.customerview.customChart.MyRightMarkerView;
import cn.zn.com.zn_android.uiclass.listener.MyChartListener;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.UnitUtils;
import cn.zn.com.zn_android.utils.VolFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.BarLineChartTouchListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zjs on 2016/7/18 0018.
 * email: m15267280642@163.com
 * explain:
 */
public class KLinePage extends BaseChartPage implements RadioGroup.OnCheckedChangeListener {

    private static final String TAG = "KLinePage";
    protected MyCombinedChart combinedchart;
    protected MyCombinedChart macdChart;
    private MyKLineChart kdjChart;
    private MyKLineChart rsiChart;
    protected BarChart barChart;
    private ChartParse mData;
    private ArrayList<KLineBean> kLineDatas;
    protected MyChartListener myChartListener;

    private boolean mShowBarLeftLable = true;
    private RadioGroup mButtomChartType;
    private LinearLayout mChart;
    private ProgressBar mPbLoad;
    XAxis xAxisBar, xAxisK, xMacdAxisK, xKdjAxisk, xRsiAxisk;
    YAxis axisLeftBar;
    YAxis axisLeftK, macdAxisLeftK, kdjAxisLeftk, rsiAxisLeftK;
    YAxis axisRightBar, axisRightK, macdAxisRightK, kdjAxisRightK, rsiAxisRightK;
    BarDataSet barDataSet;
    private Message msg = new Message();
    private BarLineChartTouchListener mChartTouchListener;
    private CoupleChartGestureListener coupleChartGestureListener;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (null == mData.getKLineDatas() || mData.getDayLineDatas().size() == 0) {
                        mPbLoad.setVisibility(View.GONE);
                        mChart.setVisibility(View.VISIBLE);
                    } else setData(mData);
                    break;
                default:
//            barChart.setAutoScaleMinMaxEnabled(true);
//                    combinedchart.setAutoScaleMinMaxEnabled(true);
                    //            macdChart.setAutoScaleMinMaxEnabled(true);
                    kdjChart.notifyDataSetChanged();
                    combinedchart.notifyDataSetChanged();
                    barChart.notifyDataSetChanged();
                    macdChart.notifyDataSetChanged();
                    combinedchart.invalidate();
                    barChart.invalidate();
                    macdChart.invalidate();
                    kdjChart.invalidate();
                    mPbLoad.setVisibility(View.GONE);
                    mChart.setVisibility(View.VISIBLE);
//                    syncCharts(combinedchart, new Chart[]{barChart});
//                    syncCharts(barChart, new Chart[]{combinedchart});
            }

        }
    };
    private float xscale;
    private float xscaleCombin;
    private float xscaleMacd;
    private float xscaleKdj;
    private float xscaleRsi;


    public KLinePage(Activity activity, int bottomChartVisibility) {
        super(activity, bottomChartVisibility);
    }


    @Override
    public View initView() {
        View kLineView = LayoutInflater.from(mActivity).inflate(R.layout.page_k_line_chart, new LinearLayout(mActivity), false);
        combinedchart = (MyCombinedChart) kLineView.findViewById(R.id.combinedchart);
        macdChart = (MyCombinedChart) kLineView.findViewById(R.id.macd_chart);
        barChart = (BarChart) kLineView.findViewById(R.id.barchart);
        kdjChart = (MyKLineChart) kLineView.findViewById(R.id.kdj_chart);
        rsiChart = (MyKLineChart) kLineView.findViewById(R.id.rsi_chart);
        mChart = (LinearLayout) kLineView.findViewById(R.id.chart);
        mPbLoad = (ProgressBar) kLineView.findViewById(R.id.pb_load);
        mButtomChartType = (RadioGroup) kLineView.findViewById(R.id.rg_chart_type);
        mButtomChartType.setVisibility(visibility);
        initPage();
        return kLineView;
    }

    @Override
    public void initData() {
//        setData(mData);
        msg.what = 0;
        handler.sendMessage(msg);
    }


    @Override
    public void initPage() {
        initBarChart();
        initCombinedChart();
        initMACDChart();
        initKdjChart();
        initRsiChart();
        initEvent();
    }


    /**
     * 请求数据
     */
    public void requestData(Constants.kLineType type, String ticCode) {

        String dateTime = DateUtils.getCurDate("yyyyMMdd");
        mPbLoad.setVisibility(View.VISIBLE);
        mChart.setVisibility(View.GONE);
        if (mData == null) {
            mData = new ChartParse(mActivity);
        }
        mData.setChartPage(this);
        switch (type) {
            case DAY:
                xscaleCombin = 3f;
                xscale = 3f;
                xscaleKdj = 3f;
                xscaleMacd = 3f;
                xscaleRsi = 3f;
                mData.requestHsDayKData(ticCode, "20120101", dateTime);
                break;
            case WEEK:
                xscaleCombin = 2f;
                xscale = 2f;
                xscaleKdj = 2f;
                xscaleMacd = 2f;
                xscaleRsi = 2f;
                mData.requestHsWeekKData(ticCode, "20100101", dateTime);
                break;
            case MONTH:
                xscaleCombin = 1f;
                xscale = 1f;
                xscaleKdj = 1f;
                xscaleMacd = 1f;
                xscaleRsi = 1f;
                mData.requestHsMonthKData(ticCode, "20090101", dateTime);
                break;
        }


    }


    private void initEvent() {

        mButtomChartType.setOnCheckedChangeListener(this);
        // 将K线控的滑动事件传递给交易量控件,MACD控件
        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{barChart}));

//        combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{macdChart}));
        // 将交易量控件的滑动事件传递给K线控件
        barChart.setOnChartGestureListener(new CoupleChartGestureListener(barChart, new Chart[]{combinedchart}));
        macdChart.setOnChartGestureListener(new CoupleChartGestureListener(macdChart, new Chart[]{combinedchart}));
        kdjChart.setOnChartGestureListener(new CoupleChartGestureListener(kdjChart, new Chart[]{combinedchart}));
        rsiChart.setOnChartGestureListener(new CoupleChartGestureListener(rsiChart, new Chart[]{combinedchart}));

        rsiChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                combinedchart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                if (null != myChartListener) {
                    myChartListener.move(mData.getKLineDatas().get(h.getXIndex()));
                }
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
                if (null != myChartListener) {
                    myChartListener.cancelMove();
                }

            }
        });


        kdjChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                combinedchart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                if (null != myChartListener) {
                    myChartListener.move(mData.getKLineDatas().get(h.getXIndex()));
                }
            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
                if (null != myChartListener) {
                    myChartListener.cancelMove();
                }
            }
        });


        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                combinedchart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                if (null != myChartListener) {
                    myChartListener.move(mData.getKLineDatas().get(h.getXIndex()));
                }

            }


            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
                if (null != myChartListener) {
                    myChartListener.cancelMove();
                }
            }
        });


        macdChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                combinedchart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                if (null != myChartListener) {
                    myChartListener.move(mData.getKLineDatas().get(h.getXIndex()));
                }

            }

            @Override
            public void onNothingSelected() {
                combinedchart.highlightValue(null);
                if (null != myChartListener) {
                    myChartListener.cancelMove();
                }
            }
        });


        combinedchart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                Log.i(TAG, "onValueSelected2: " + h.getXIndex());
                barChart.highlightValues(new Highlight[]{h});
                macdChart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                kdjChart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                rsiChart.setHighlightValue(new Highlight(h.getXIndex(), 0));
                if (null != myChartListener) {
                    myChartListener.move(mData.getKLineDatas().get(h.getXIndex()));
                }

            }

            @Override
            public void onNothingSelected() {
                barChart.highlightValue(null);
                macdChart.highlightValue(null);
                kdjChart.highlightValue(null);
                rsiChart.highlightValue(null);
                if (null != myChartListener) {
                    myChartListener.cancelMove();
                }
            }
        });
    }

    /**
     * 初始化RsiChart
     */

    private void initRsiChart() {
        rsiChart.setDrawBorders(true);
        rsiChart.setBorderWidth(1);
        rsiChart.setBorderColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        rsiChart.setDescription("");
        rsiChart.setScaleYEnabled(false);
        rsiChart.setAutoScaleMinMaxEnabled(true);
//        macdChart.getViewPortHandler().setMinimumScaleX(3f);
        rsiChart.setBackgroundColor(Color.WHITE);
        Legend combinedchartLegend = rsiChart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xRsiAxisk = rsiChart.getXAxis();
        xRsiAxisk.setDrawLabels(false);
        xRsiAxisk.setDrawGridLines(false);
        xRsiAxisk.setDrawAxisLine(false);
        xRsiAxisk.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        xRsiAxisk.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xRsiAxisk.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));

        rsiAxisLeftK = rsiChart.getAxisLeft();
//        axisLeftK.setDrawLabels(true);
        rsiAxisLeftK.setDrawGridLines(false);
        rsiAxisLeftK.setDrawAxisLine(false);
        rsiAxisLeftK.setDrawLabels(true);
        rsiAxisLeftK.setShowOnlyMinMax(true);
        rsiAxisLeftK.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        rsiAxisLeftK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        rsiAxisLeftK.setSpaceTop(0);
        rsiAxisLeftK.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });


        rsiAxisRightK = rsiChart.getAxisRight();
        rsiAxisRightK.setDrawLabels(false);
        rsiAxisRightK.setDrawGridLines(false);
        rsiAxisRightK.setDrawAxisLine(false);
        rsiAxisRightK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        rsiChart.setDragDecelerationEnabled(true);
        rsiChart.setDragDecelerationFrictionCoef(0.2f);
    }


    /**
     * 初始化KDJChart
     */
    private void initKdjChart() {
        kdjChart.setDrawBorders(true);
        kdjChart.setBorderWidth(1);
        kdjChart.setBorderColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        kdjChart.setDescription("");
        kdjChart.setDragEnabled(true);
        kdjChart.setScaleYEnabled(false);
        kdjChart.setAutoScaleMinMaxEnabled(true);
//        macdChart.getViewPortHandler().setMinimumScaleX(3f);
        kdjChart.setBackgroundColor(Color.WHITE);
        Legend combinedchartLegend = kdjChart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xKdjAxisk = kdjChart.getXAxis();
        xKdjAxisk.setDrawLabels(false);
        xKdjAxisk.setDrawGridLines(false);
        xKdjAxisk.setDrawAxisLine(false);
        xKdjAxisk.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        xKdjAxisk.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xKdjAxisk.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));

        kdjAxisLeftk = kdjChart.getAxisLeft();
//        axisLeftK.setDrawLabels(true);
        kdjAxisLeftk.setDrawGridLines(false);
        kdjAxisLeftk.setDrawAxisLine(false);
        kdjAxisLeftk.setDrawLabels(true);
        kdjAxisLeftk.setShowOnlyMinMax(true);
        kdjAxisLeftk.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        kdjAxisLeftk.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        kdjAxisLeftk.setSpaceTop(0);
        kdjAxisLeftk.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });


        kdjAxisRightK = kdjChart.getAxisRight();
        kdjAxisRightK.setDrawLabels(false);
        kdjAxisRightK.setDrawGridLines(false);
        kdjAxisRightK.setDrawAxisLine(false);
        kdjAxisRightK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        kdjChart.setDragDecelerationEnabled(true);
        kdjChart.setDragDecelerationFrictionCoef(0.2f);

    }


    /**
     * 初始化barchart
     */
    private void initBarChart() {
        barChart.setDrawBorders(true);
        barChart.setBorderWidth(1f);
        barChart.setBorderColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        barChart.setDescription("");
        barChart.setAutoScaleMinMaxEnabled(true);
        barChart.setDragEnabled(true);
        barChart.setScaleYEnabled(false);
        barChart.setBackgroundColor(Color.WHITE);
//        barChart.getViewPortHandler().setMinimumScaleX(3f);
        Legend barChartLegend = barChart.getLegend();
        barChartLegend.setEnabled(false);

        //BarYAxisFormatter  barYAxisFormatter=new BarYAxisFormatter();
        //bar x y轴
        xAxisBar = barChart.getXAxis();
//        xAxisBar.setDrawLabels(true);
        xAxisBar.setDrawLabels(false);
        xAxisBar.setDrawGridLines(false);
        xAxisBar.setDrawAxisLine(false);
        xAxisBar.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        xAxisBar.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisBar.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));

        axisLeftBar = barChart.getAxisLeft();
        axisLeftBar.setAxisMinValue(0);
        axisLeftBar.setDrawGridLines(false);
        axisLeftBar.setDrawAxisLine(false);
        axisLeftBar.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
//        axisLeftBar.setDrawLabels(true);
        axisLeftBar.setDrawLabels(mShowBarLeftLable);
        axisLeftBar.setSpaceTop(0);
        axisLeftBar.setShowOnlyMinMax(true);
        axisRightBar = barChart.getAxisRight();
        axisRightBar.setDrawLabels(false);
        axisRightBar.setDrawGridLines(false);
        axisRightBar.setDrawAxisLine(false);
        barChart.setDragDecelerationEnabled(true);
        barChart.setDragDecelerationFrictionCoef(0.2f);
    }


    /**
     * 初始化combinedChart
     */
    private void initCombinedChart() {
        combinedchart.setDrawBorders(true);
        combinedchart.setBorderWidth(1);
        combinedchart.setBorderColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        combinedchart.setDescription("");
        combinedchart.setDragEnabled(true);
        combinedchart.setScaleYEnabled(false);
        combinedchart.setAutoScaleMinMaxEnabled(true);
//        combinedchart.getViewPortHandler().setMinimumScaleX(3f);
        combinedchart.setBackgroundColor(Color.WHITE);
        Legend combinedchartLegend = combinedchart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xAxisK = combinedchart.getXAxis();
        xAxisK.setDrawLabels(true);
        xAxisK.setDrawGridLines(false);
        xAxisK.setDrawAxisLine(false);
        xAxisK.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        xAxisK.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxisK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));

        axisLeftK = combinedchart.getAxisLeft();
//        axisLeftK.setDrawLabels(true);
        axisLeftK.setDrawGridLines(false);
        axisLeftK.setDrawAxisLine(false);
        axisLeftK.setDrawLabels(true);
        axisLeftK.setShowOnlyMinMax(true);
        axisLeftK.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        axisLeftK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        axisLeftK.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        axisRightK = combinedchart.getAxisRight();
        axisRightK.setDrawLabels(false);
        axisRightK.setDrawGridLines(false);
        axisRightK.setDrawAxisLine(false);
        axisRightK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        combinedchart.setDragDecelerationEnabled(true);
        combinedchart.setDragDecelerationFrictionCoef(0.2f);

    }


    /**
     * 初始化MACDChart
     */
    private void initMACDChart() {
        macdChart.setDrawBorders(true);
        macdChart.setBorderWidth(1);
        macdChart.setBorderColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        macdChart.setDescription("");
        macdChart.setDragEnabled(true);
        macdChart.setScaleYEnabled(false);
        macdChart.setAutoScaleMinMaxEnabled(true);
//        macdChart.getViewPortHandler().setMinimumScaleX(3f);
        macdChart.setBackgroundColor(Color.WHITE);
        Legend combinedchartLegend = macdChart.getLegend();
        combinedchartLegend.setEnabled(false);
        //bar x y轴
        xMacdAxisK = macdChart.getXAxis();
        xMacdAxisK.setDrawLabels(false);
        xMacdAxisK.setDrawGridLines(false);
        xMacdAxisK.setDrawAxisLine(false);
        xMacdAxisK.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        xMacdAxisK.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xMacdAxisK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));

        macdAxisLeftK = macdChart.getAxisLeft();
//        axisLeftK.setDrawLabels(true);
        macdAxisLeftK.setDrawGridLines(false);
        macdAxisLeftK.setDrawAxisLine(false);
        macdAxisLeftK.setDrawLabels(true);
        macdAxisLeftK.setShowOnlyMinMax(true);
        macdAxisLeftK.setTextColor(mActivity.getResources().getColor(R.color.market_detail_font_color));
        macdAxisLeftK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        macdAxisLeftK.setSpaceTop(0);
        macdAxisLeftK.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                DecimalFormat mFormat = new DecimalFormat("#0.00");
                return mFormat.format(value);
            }
        });


        macdAxisRightK = macdChart.getAxisRight();
        macdAxisRightK.setDrawLabels(false);
        macdAxisRightK.setDrawGridLines(false);
        macdAxisRightK.setDrawAxisLine(false);
        macdAxisRightK.setGridColor(mActivity.getResources().getColor(R.color.minute_zhoutv));
        macdChart.setDragDecelerationEnabled(true);
        macdChart.setDragDecelerationFrictionCoef(0.2f);

    }


//    private void getOffLineData() {
//           /*方便测试，加入假数据*/
//        mData = new ChartParse(mActivity);
//        new Thread() {
//            @Override
//            public void run() {
//                JSONObject object = null;
//                try {
//                    object = new JSONObject(ConstantTest.KLINEURL);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                mData.parseKLine(object);
//                msg.what = 0;
//                handler.sendMessage(msg);
//            }
//        }.start();
//    }


    private void setData(ChartParse mData) {
        setMarkerView(mData);
        kLineDatas = mData.getKLineDatas();
        // axisLeftBar.setAxisMaxValue(mData.getVolmax());
        String unit = UnitUtils.getVolUnit(mData.getVolmax());
        int u = 1;
        if ("万手".equals(unit)) {
            u = 4;
        } else if ("亿手".equals(unit)) {
            u = 8;
        }
        axisLeftBar.setValueFormatter(new VolFormatter((int) Math.pow(10, u)));
        // axisRightBar.setAxisMaxValue(mData.getVolmax());
        Log.e("@@@", mData.getVolmax() + "da");
        axisLeftBar.setDrawLabels(mShowBarLeftLable);
        ArrayList<String> xVals = new ArrayList<>();
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<CandleEntry> candleEntries = new ArrayList<>();
        ArrayList<BarEntry> barEntriesMacd = new ArrayList<>();//MACD数据
        ArrayList<Entry> lineEntriesDea = new ArrayList<>();//DEA数据
        ArrayList<Entry> lineEntriesDif = new ArrayList<>();//DIF数据
        ArrayList<Entry> lineEntriesK = new ArrayList<>();//KDJ中的K线数据
        ArrayList<Entry> lineEntriesD = new ArrayList<>();//KDJ中的D线数据
        ArrayList<Entry> lineEntriesJ = new ArrayList<>();//KDJ中的J线数据
        ArrayList<Entry> lineEntriesR = new ArrayList<>();//KDJ中的R线数据
        ArrayList<Entry> lineEntriesS = new ArrayList<>();//KDJ中的S线数据
        ArrayList<Entry> lineEntriesI = new ArrayList<>();//KDJ中的I线数据
        ArrayList<Entry> line5Entries = new ArrayList<>();
        ArrayList<Entry> line10Entries = new ArrayList<>();
        ArrayList<Entry> line20Entries = new ArrayList<>();
        List<Integer> colors = new ArrayList<Integer>();
        for (int i = 0, j = 0; i < mData.getKLineDatas().size(); i++, j++) {
            xVals.add(mData.getKLineDatas().get(i).date + "");
            BarEntry barEntry = new BarEntry(mData.getKLineDatas().get(i).vol, i);
            barEntry.setmOpen(mData.getKLineDatas().get(i).open);
            barEntry.setmColse(mData.getKLineDatas().get(i).close);
            barEntries.add(barEntry);
            candleEntries.add(new CandleEntry(i, mData.getKLineDatas().get(i).high,
                    mData.getKLineDatas().get(i).low,
                    mData.getKLineDatas().get(i).open,
                    mData.getKLineDatas().get(i).close));
            barEntriesMacd.add(new BarEntry(mData.getMACDDatas().get(i).macd, i));
            lineEntriesDea.add(new Entry(mData.getMACDDatas().get(i).dea, i));
            lineEntriesDif.add(new Entry(mData.getMACDDatas().get(i).dif, i));
            lineEntriesK.add(new Entry(mData.getKDJDatas().get(i).K, i));
            lineEntriesD.add(new Entry(mData.getKDJDatas().get(i).D, i));
            lineEntriesJ.add(new Entry(mData.getKDJDatas().get(i).J, i));
            if (mData.getMACDDatas().get(i).macd >= 0)
                colors.add(mActivity.getResources().getColor(R.color.market_red));
            else colors.add(mActivity.getResources().getColor(R.color.market_green));

            if (i >= 6) lineEntriesR.add(new Entry(mData.getRSIDatas().get(i).RSI1, i));
            if (i >= 12) lineEntriesS.add(new Entry(mData.getRSIDatas().get(i).RSI2, i));
            if (i >= 24) lineEntriesI.add(new Entry(mData.getRSIDatas().get(i).RSI3, i));


            if (i >= 4) {
                line5Entries.add(new Entry(mData.getDayLineDatas().get(i).day5Value, i));
            }
            if (i >= 9) {
                line10Entries.add(new Entry(mData.getDayLineDatas().get(i).day10Value, i));
            }
            if (i >= 19) {
                line20Entries.add(new Entry(mData.getDayLineDatas().get(i).day20Value, i));
            }

        }
        /*********************************VOL图数据添加*********************/
        barDataSet = new BarDataSet(barEntries, "成交量");
        barDataSet.setBarSpacePercent(50); //bar空隙
        barDataSet.setHighlightEnabled(true);
        barDataSet.setHighLightAlpha(255);
        barDataSet.setHighLightColor(Color.BLACK);
        barDataSet.setDrawValues(false);
        barDataSet.setIncreasingColor(mActivity.getResources().getColor(R.color.market_red));
        barDataSet.setDecreasingColor(mActivity.getResources().getColor(R.color.market_green));
        barDataSet.setColor(mActivity.getResources().getColor(R.color.market_red));
        BarData barData = new BarData(xVals, barDataSet);
        barChart.setData(barData);
        final ViewPortHandler viewPortHandlerBar = barChart.getViewPortHandler();
        viewPortHandlerBar.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix touchmatrix = viewPortHandlerBar.getMatrixTouch();
        touchmatrix.postScale(xscale, 1f);

        /********************************蜡烛图数据添加********************/
        CandleDataSet candleDataSet = new CandleDataSet(candleEntries, "KLine");
        candleDataSet.setDrawHorizontalHighlightIndicator(false);
        candleDataSet.setHighlightEnabled(true);
        candleDataSet.setHighLightColor(Color.BLACK);
        candleDataSet.setValueTextSize(10f);
        candleDataSet.setDrawValues(false);
//        candleDataSet.setColor(Color.RED);
        candleDataSet.setIncreasingColor(mActivity.getResources().getColor(R.color.market_red));
        candleDataSet.setDecreasingColor(mActivity.getResources().getColor(R.color.market_green));
        candleDataSet.setNeutralColor(mActivity.getResources().getColor(R.color.market_red));
        candleDataSet.setShadowWidth(1f);
        candleDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        CandleData candleData = new CandleData(xVals, candleDataSet);


        ArrayList<ILineDataSet> sets = new ArrayList<>();
        sets.add(setMaLine(5, xVals, line5Entries));
        sets.add(setMaLine(10, xVals, line10Entries));
        sets.add(setMaLine(30, xVals, line20Entries));


        CombinedData combinedData = new CombinedData(xVals);
        LineData lineData = new LineData(xVals, sets);
        combinedData.setData(candleData);
        combinedData.setData(lineData);
        combinedchart.setData(combinedData);
        combinedchart.moveViewToX(mData.getKLineDatas().size() - 1);
        final ViewPortHandler viewPortHandlerCombin = combinedchart.getViewPortHandler();
        viewPortHandlerCombin.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix matrixCombin = viewPortHandlerCombin.getMatrixTouch();
        matrixCombin.postScale(xscaleCombin, 1f);

        /*********************************MACD数据添加*********************/
        BarDataSet barDataSetMacd = new BarDataSet(barEntriesMacd, "MACD");
        barDataSetMacd.setHighlightEnabled(true);
        barDataSetMacd.setHighLightAlpha(255);
        barDataSetMacd.setHighLightColor(Color.BLACK);
        barDataSetMacd.setDrawValues(false);
        barDataSetMacd.setColors(colors);
        barDataSetMacd.setBarSpacePercent(80);
        barDataSetMacd.setIncreasingColor(mActivity.getResources().getColor(R.color.market_red));
        barDataSetMacd.setDecreasingColor(mActivity.getResources().getColor(R.color.market_green));
        barDataSetMacd.setAxisDependency(YAxis.AxisDependency.LEFT);
        BarData barDataMacd = new BarData(xVals, barDataSetMacd);

        ArrayList<ILineDataSet> setsMacd = new ArrayList<>();
        setsMacd.add(setMacdLine("DEA", xVals, lineEntriesDea));
        setsMacd.add(setMacdLine("DIF", xVals, lineEntriesDif));


        CombinedData macdCombinedData = new CombinedData(xVals);
        LineData lineDataMacd = new LineData(xVals, setsMacd);
        macdCombinedData.setData(barDataMacd);
        macdCombinedData.setData(lineDataMacd);
        macdChart.setData(macdCombinedData);
        final ViewPortHandler viewPortHandlerMacd = macdChart.getViewPortHandler();
        viewPortHandlerMacd.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix matrixMacd = viewPortHandlerCombin.getMatrixTouch();
        matrixMacd.postScale(xscaleMacd, 1f);

        /*********************************KDJ数据添加*********************/

        ArrayList<ILineDataSet> setsKdj = new ArrayList<>();
        setsKdj.add(setKdjLine("K", xVals, lineEntriesK));
        setsKdj.add(setKdjLine("D", xVals, lineEntriesD));
        setsKdj.add(setKdjLine("J", xVals, lineEntriesJ));
//        CombinedData kdjCombineData = new CombinedData(xVals);
        LineData lineDataKdj = new LineData(xVals, setsKdj);
//        kdjCombineData.setData(lineDataKdj);
        kdjChart.setData(lineDataKdj);
        final ViewPortHandler viewPortHandlerKdj = kdjChart.getViewPortHandler();
        viewPortHandlerKdj.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix matrixKdj = viewPortHandlerKdj.getMatrixTouch();
        matrixKdj.postScale(xscaleKdj, 1f);


        /*********************************RSI数据添加*********************/
        ArrayList<ILineDataSet> setRsi = new ArrayList<>();
        setRsi.add(setRsiLine("S", xVals, lineEntriesS));
        setRsi.add(setRsiLine("R", xVals, lineEntriesR));
        setRsi.add(setRsiLine("I", xVals, lineEntriesI));
//        CombinedData kdjCombineData = new CombinedData(xVals);
        LineData lineDataRsi = new LineData(xVals, setRsi);
//        kdjCombineData.setData(lineDataKdj);
        rsiChart.setData(lineDataRsi);
        final ViewPortHandler viewPortHandlerRsi = rsiChart.getViewPortHandler();
        viewPortHandlerRsi.setMaximumScaleX(culcMaxscale(xVals.size()));
        Matrix matrixRsi = viewPortHandlerKdj.getMatrixTouch();
        matrixRsi.postScale(xscaleRsi, 1f);


        rsiChart.moveViewToX(mData.getKLineDatas().size() - 1);
        kdjChart.moveViewToX(mData.getKDJDatas().size() - 1);
        combinedchart.moveViewToX(mData.getKLineDatas().size() - 1);
        barChart.moveViewToX(mData.getKLineDatas().size() - 1);
        macdChart.moveViewToX(mData.getKLineDatas().size() - 1);
        setOffset();
        /****************************************************************************************
         此处解决方法来源于CombinedChartDemo，k线图y轴显示问题，图表滑动后才能对齐的bug，希望有人给出解决方法
         (注：此bug现已修复，感谢和chenguang79一起研究)
         ****************************************************************************************/
//        barChart.setAutoScaleMinMaxEnabled(true);
//        combinedchart.setAutoScaleMinMaxEnabled(true);
//        macdChart.setAutoScaleMinMaxEnabled(true);
        syncCharts(combinedchart, new Chart[]{barChart});
//        combinedchart.notifyDataSetChanged();
//        barChart.notifyDataSetChanged();
//        macdChart.notifyDataSetChanged();
//        combinedchart.invalidate();
//        barChart.invalidate();
//        macdChart.invalidate();
//        mPbLoad.setVisibility(View.GONE);
//        mChart.setVisibility(View.VISIBLE);
        handler.sendEmptyMessageDelayed(1, 300);

    }


    @NonNull
    private LineDataSet setRsiLine(String type, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetRsi = new LineDataSet(lineEntries, "RSI" + type);
        if ("S".equals(type)) {
            lineDataSetRsi.setHighlightEnabled(true);
            lineDataSetRsi.setDrawHorizontalHighlightIndicator(false);
            lineDataSetRsi.setHighLightColor(Color.BLACK);
        } else {/*此处必须得写*/
            lineDataSetRsi.setHighlightEnabled(false);
        }
        lineDataSetRsi.setDrawValues(false);
        if ("S".equals(type)) {
            lineDataSetRsi.setColor(mActivity.getResources().getColor(R.color.chart_yellow));
        } else if ("I".equals(type)) {
            lineDataSetRsi.setColor(mActivity.getResources().getColor(R.color.chart_purple));
        } else {
            lineDataSetRsi.setColor(mActivity.getResources().getColor(R.color.chart_blue));
        }
        lineDataSetRsi.setLineWidth(1f);
        lineDataSetRsi.setDrawCircles(false);
        lineDataSetRsi.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetRsi;
    }


    @NonNull
    private LineDataSet setKdjLine(String type, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetKdj = new LineDataSet(lineEntries, "KDJ" + type);
        if ("K".equals(type)) {
            lineDataSetKdj.setHighlightEnabled(true);
            lineDataSetKdj.setDrawHorizontalHighlightIndicator(false);
            lineDataSetKdj.setHighLightColor(Color.BLACK);
        } else {/*此处必须得写*/
            lineDataSetKdj.setHighlightEnabled(false);
        }
        lineDataSetKdj.setDrawValues(false);
        if ("D".equals(type)) {
            lineDataSetKdj.setColor(mActivity.getResources().getColor(R.color.chart_yellow));
        } else if ("J".equals(type)) {
            lineDataSetKdj.setColor(mActivity.getResources().getColor(R.color.chart_purple));
        } else {
            lineDataSetKdj.setColor(mActivity.getResources().getColor(R.color.chart_blue));
        }
        lineDataSetKdj.setLineWidth(1f);
        lineDataSetKdj.setDrawCircles(false);
        lineDataSetKdj.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetKdj;
    }


    @NonNull
    private LineDataSet setMaLine(int ma, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMa = new LineDataSet(lineEntries, "ma" + ma);
        if (ma == 5) {
            lineDataSetMa.setHighlightEnabled(true);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMa.setHighLightColor(Color.BLACK);
        } else {/*此处必须得写*/
            lineDataSetMa.setHighlightEnabled(false);
            lineDataSetMa.setDrawHorizontalHighlightIndicator(false);
        }
        lineDataSetMa.setDrawValues(false);
        if (ma == 5) {
            lineDataSetMa.setColor(mActivity.getResources().getColor(R.color.chart_blue));
        } else if (ma == 10) {
            lineDataSetMa.setColor(mActivity.getResources().getColor(R.color.chart_yellow));
        } else {
            lineDataSetMa.setColor(mActivity.getResources().getColor(R.color.chart_purple));
        }
        lineDataSetMa.setLineWidth(1f);
        lineDataSetMa.setDrawCircles(false);
        lineDataSetMa.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMa;
    }

    @NonNull
    private LineDataSet setMacdLine(String type, ArrayList<String> xVals, ArrayList<Entry> lineEntries) {
        LineDataSet lineDataSetMacd = new LineDataSet(lineEntries, "MACD" + type);
        if ("DEA".equals(type)) {
            lineDataSetMacd.setHighlightEnabled(true);
            lineDataSetMacd.setDrawHorizontalHighlightIndicator(false);
            lineDataSetMacd.setHighLightColor(Color.BLACK);
        } else {/*此处必须得写*/
            lineDataSetMacd.setHighlightEnabled(false);
        }
        lineDataSetMacd.setDrawValues(false);
        if ("DEA".equals(type)) {
            lineDataSetMacd.setColor(mActivity.getResources().getColor(R.color.chart_yellow));
        } else if ("DIF".equals(type)) {
            lineDataSetMacd.setColor(mActivity.getResources().getColor(R.color.chart_purple));
        }
        lineDataSetMacd.setLineWidth(1f);
        lineDataSetMacd.setDrawCircles(false);
        lineDataSetMacd.setAxisDependency(YAxis.AxisDependency.LEFT);
        return lineDataSetMacd;
    }


    private float culcMaxscale(float count) {
        float max = 1;
        max = count / 127 * 5;
        return max;
    }

    /*设置量表对齐*/
    private void setOffset() {
        float lineLeft = combinedchart.getViewPortHandler().offsetLeft();
        float barLeft = barChart.getViewPortHandler().offsetLeft();
        float macdLeft = macdChart.getViewPortHandler().offsetLeft();
        float kdjLeft = kdjChart.getViewPortHandler().offsetLeft();
        float rsiLeft = rsiChart.getViewPortHandler().offsetLeft();

        float lineRight = combinedchart.getViewPortHandler().offsetRight();
        float barRight = barChart.getViewPortHandler().offsetRight();
        float macdRight = macdChart.getViewPortHandler().offsetRight();
        float kdjRight = kdjChart.getViewPortHandler().offsetRight();
        float rsiRight = rsiChart.getViewPortHandler().offsetRight();

        float lifeBottom = combinedchart.getViewPortHandler().offsetBottom();
        float barBottom = barChart.getViewPortHandler().offsetBottom();
        float macdBottom = macdChart.getViewPortHandler().offsetBottom();
        float kdjBottom = kdjChart.getViewPortHandler().offsetBottom();
        float rsiBottom = rsiChart.getViewPortHandler().offsetBottom();

        float transLeft = 0, transRight = 0;
 /*注：setExtraLeft...函数是针对图表相对位置计算，比如A表offLeftA=20dp,B表offLeftB=30dp,则A.setExtraLeftOffset(10),并不是30，还有注意单位转换*/
        float bottomLeft = UnitUtils.getMax(macdLeft, barLeft, kdjLeft, rsiLeft);
        float bottomRight = UnitUtils.getMax(macdRight, barRight, kdjRight, rsiRight);
        if (bottomLeft < lineLeft) {
           /* offsetLeft = Utils.convertPixelsToDp(lineLeft - barLeft);
            barChart.setExtraLeftOffset(offsetLeft);*/
            transLeft = lineLeft;
        } else {
            transLeft = bottomLeft;
        }

  /*注：setExtraRight...函数是针对图表绝对位置计算，比如A表offRightA=20dp,B表offRightB=30dp,则A.setExtraLeftOffset(30),并不是10，还有注意单位转换*/
        if (bottomRight < lineRight) {
          /*  offsetRight = Utils.convertPixelsToDp(lineRight);
            barChart.setExtraRightOffset(offsetRight);*/
            transRight = lineRight;
        } else {
            transRight = bottomRight;
        }

        combinedchart.setViewPortOffsets(transLeft, 30, transRight, lifeBottom);
        barChart.setViewPortOffsets(transLeft, 15, transRight, barBottom);
        macdChart.setViewPortOffsets(transLeft, 15, transRight, macdBottom);
        kdjChart.setViewPortOffsets(transLeft, 15, transRight, kdjBottom);
        rsiChart.setViewPortOffsets(transLeft, 15, transRight, rsiBottom);
    }


    /**
     * 设置触摸事件
     *
     * @param flag
     */
    public void setOnTouchEnable(boolean flag) {
        combinedchart.setTouchEnabled(flag);
        barChart.setTouchEnabled(flag);
        macdChart.setTouchEnabled(flag);
        kdjChart.setTouchEnabled(flag);
        rsiChart.setTouchEnabled(flag);
    }

    public void setPosition(YAxis.YAxisLabelPosition pos) {
        axisLeftK.setPosition(pos);
        macdAxisLeftK.setPosition(pos);
        kdjAxisLeftk.setPosition(pos);
        rsiAxisLeftK.setPosition(pos);
        if (YAxis.YAxisLabelPosition.INSIDE_CHART.equals(pos)) {
            mShowBarLeftLable = false;
        } else {
            mShowBarLeftLable = true;
        }
    }


    /**
     * chart选择的选项
     *
     * @param group
     * @param checkedId
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
            case R.id.rb_chart_0:
                barChart.setVisibility(View.VISIBLE);
                kdjChart.setVisibility(View.GONE);
                macdChart.setVisibility(View.GONE);
                rsiChart.setVisibility(View.GONE);
                combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{barChart}));
                syncCharts(combinedchart, new Chart[]{barChart});
                break;
            case R.id.rb_chart_1:
                macdChart.setVisibility(View.VISIBLE);
                kdjChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                rsiChart.setVisibility(View.GONE);
                combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{macdChart}));
                syncCharts(combinedchart, new Chart[]{macdChart});
                break;
            case R.id.rb_chart_2:
                kdjChart.setVisibility(View.VISIBLE);
                macdChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                rsiChart.setVisibility(View.GONE);
                combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{kdjChart}));
                syncCharts(combinedchart, new Chart[]{kdjChart});
                break;
            default:
                rsiChart.setVisibility(View.VISIBLE);
                kdjChart.setVisibility(View.GONE);
                macdChart.setVisibility(View.GONE);
                barChart.setVisibility(View.GONE);
                combinedchart.setOnChartGestureListener(new CoupleChartGestureListener(combinedchart, new Chart[]{rsiChart}));
                syncCharts(combinedchart, new Chart[]{rsiChart});
                break;
        }
    }


    private boolean flag = true;

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


    private void setMarkerView(ChartParse mData) {
        MyLeftMarkerView leftMarkerView = new MyLeftMarkerView(mActivity, R.layout.mymarkerview);
        MyRightMarkerView rightMarkerView = new MyRightMarkerView(mActivity, R.layout.mymarkerview);
        int[] leftColors = {mActivity.getResources().getColor(R.color.chart_blue), mActivity.getResources().getColor(R.color.chart_yellow), mActivity.getResources().getColor(R.color.chart_purple)};
        int[] rightColors = {mActivity.getResources().getColor(R.color.market_detail_font_color)};
        leftMarkerView.setColors(leftColors);
        rightMarkerView.setColors(rightColors);
        combinedchart.setMarker(leftMarkerView, rightMarkerView, mData, MyCombinedChart.combineChartType.CANDLE);
        macdChart.setMarker(leftMarkerView, rightMarkerView, mData, MyCombinedChart.combineChartType.MACD);
        kdjChart.setMarker(leftMarkerView, rightMarkerView, mData, MyKLineChart.LineChartType.KDJ);
        rsiChart.setMarker(leftMarkerView, rightMarkerView, mData, MyKLineChart.LineChartType.RSI);
    }

    /**
     * 设置是否显示标记内容
     *
     * @param flag
     */
    public void setShowMarketEnable(boolean flag) {
        macdChart.setShowMarketEnable(flag);
        combinedchart.setShowMarketEnable(flag);
        kdjChart.setShowMarketEnable(flag);
        rsiChart.setShowMarketEnable(flag);
    }


    /**
     * 设置监听事件，监听高亮线移动时候的角标
     *
     * @param myChartListener
     */
    public void setMyChartListener(MyChartListener myChartListener) {
        this.myChartListener = myChartListener;
    }
}
