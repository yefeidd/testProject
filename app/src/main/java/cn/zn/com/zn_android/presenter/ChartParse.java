package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import cn.zn.com.zn_android.model.bean.HsDayKLineBean;
import cn.zn.com.zn_android.model.bean.HsWeekKLineBean;
import cn.zn.com.zn_android.model.bean.MinuteTradingBean;
import cn.zn.com.zn_android.model.bean.VariateDayLineBean;
import cn.zn.com.zn_android.model.chartBean.HsDayKModel;
import cn.zn.com.zn_android.model.chartBean.HsMonthKModel;
import cn.zn.com.zn_android.model.chartBean.HsWeekKModel;
import cn.zn.com.zn_android.model.chartBean.KDJBean;
import cn.zn.com.zn_android.model.chartBean.KLineBean;
import cn.zn.com.zn_android.model.chartBean.MacdBean;
import cn.zn.com.zn_android.model.chartBean.MinutesBean;
import cn.zn.com.zn_android.model.chartBean.MinutesModel;
import cn.zn.com.zn_android.model.chartBean.RSIBean;
import cn.zn.com.zn_android.uiclass.page.BaseChartPage;

public class ChartParse {
    private Activity mActivity;
    private BaseChartPage chartPage;
    private ArrayList<MinutesBean> datas = new ArrayList<>();
    private ArrayList<KLineBean> kDatas = new ArrayList<>();
    private ArrayList<MinuteTradingBean> minuteTradingList = new ArrayList<>();
    private ArrayList<MacdBean> MACDDatas = new ArrayList<>();
    private ArrayList<KDJBean> KDJDatas = new ArrayList<>();
    private ArrayList<RSIBean> RSIDatas = new ArrayList<>();
    private ArrayList<VariateDayLineBean> dayLineDatas = new ArrayList<>();
    private int KdjParameter = 9;
    private float baseValue;
    private float permaxmin;
    private float volmax = 0;
    private SparseArray<String> dayLabels;
    private String code = "sz002081";
    private int decreasingColor;
    private int increasingColor;
    private String stockExchange;
    private SparseArray<String> xValuesLabel = new SparseArray<>();
    private int firstDay = 10;
    float sum = 0;
    private ArrayList<KLineBean> kLineBeans = new ArrayList<>();
    private HsDayKModel hsDayKModel;
    private HsWeekKModel hsWeekKModel;
    private HsMonthKModel hsMonthKModel;
    private MinutesModel minutesModel;

    public ChartParse(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * 请求数据
     */
    public void requestMinData(String tic_code) {
        if (minutesModel != null) {
            minutesModel.setParameter(tic_code);
        } else {
            minutesModel = new MinutesModel(mActivity, this, tic_code);
        }
        minutesModel.requestData();
    }

    /**
     * 计算分时数据
     *
     * @param minDatas
     */
    public void calcMinData(List<MinutesBean> minDatas) {
        new Thread() {
            @Override
            public void run() {
                MinutesBean minutesBean;
                for (int i = 0; i < minDatas.size(); i++) {
                    minutesBean = minDatas.get(i);
                    try {
                        minutesBean.vol = Float.valueOf(minDatas.get(i).current_volume) / 100;
                    } catch (Exception e) {
                        minutesBean.vol = 0.0f;
                    }

                    datas.add(minutesBean);
                    volmax = Math.max(Float.valueOf(minDatas.get(i).current_volume) / 100, volmax);

                }
                if (datas.size() > 0) {
                    minutesBean = datas.get(datas.size() - 1);
                    float curPrice = minutesBean.getLastPrice();
                    float upPrice, downPrice;
                    if (curPrice == 0.0f) {
                        baseValue = 0;
                        permaxmin = 10f;
                    } else {
                        baseValue = curPrice - Float.valueOf(minutesBean.getZf());
                        upPrice = Math.abs(Float.valueOf(minutesBean.getHighPrice()) - baseValue);
                        downPrice = Math.abs(Float.valueOf(minutesBean.getLowPrice()) - baseValue);
                        permaxmin = upPrice > downPrice ? upPrice : downPrice;
                    }
                } else {
                    MinutesBean initMinutesBean = new MinutesBean();
                    initMinutesBean.setHighPrice("10");
                    initMinutesBean.setLowPrice("-10");
                    initMinutesBean.setTime("--");
                    initMinutesBean.setLastPrice(0);
                    initMinutesBean.setCurrent_volume("0");
                    initMinutesBean.setAverage(0);
                    initMinutesBean.setVol(0);
                    initMinutesBean.setZf("0");
                    initMinutesBean.setZf_rate("0");
                    datas.add(initMinutesBean);
                    float curPrice = initMinutesBean.getLastPrice();
                    float upPrice, downPrice;
                    if (curPrice == 0.0f) {
                        baseValue = 0;
                        permaxmin = 10f;
                    } else {
                        baseValue = curPrice - Float.valueOf(initMinutesBean.getZf());
                        upPrice = Math.abs(Float.valueOf(initMinutesBean.getHighPrice()) - baseValue);
                        downPrice = Math.abs(Float.valueOf(initMinutesBean.getLowPrice()) - baseValue);
                        permaxmin = upPrice > downPrice ? upPrice : downPrice;

                }}

                if (chartPage != null) chartPage.initData();
            }
        }.start();

    }


    /**
     * 计算K线图中所有的数据
     *
     * @param dayKDatas
     */
    public void calcDayKLineData(List<HsDayKLineBean> dayKDatas) {
        new Thread() {
            @Override
            public void run() {
                if (kDatas != null) kDatas.clear();
                if (kLineBeans != null) kLineBeans.clear();
                if (MACDDatas != null) MACDDatas.clear();
                if (KDJDatas != null) KDJDatas.clear();
                if (RSIDatas != null) RSIDatas.clear();
                int count = dayKDatas.size();
                for (int i = 0; i < count; i++) {
                    KLineBean kLineData = new KLineBean();
                    HsDayKLineBean hsDayKLineBean = dayKDatas.get(i);
                    kLineBeans.add(kLineData);
                    kLineData.date = hsDayKLineBean.getTradeDate();
                    kLineData.open = hsDayKLineBean.getOpenPrice();
                    kLineData.close = hsDayKLineBean.getClosePrice();
                    kLineData.high = hsDayKLineBean.getHighestPrice();
                    kLineData.low = hsDayKLineBean.getLowestPrice();
                    kLineData.vol = hsDayKLineBean.getTurnoverVol();
                    if (i > 0) {
                        kLineData.pre = dayKDatas.get(i - 1).getClosePrice();
                    } else {
                        kLineData.pre = kLineData.close;
                    }
                    volmax = Math.max(kLineData.vol, volmax);
                    xValuesLabel.put(i, kLineData.date);
                    clacDayLine(i, kLineData);
                    clacMacd(i, kLineData);
                    clacKDJ(i);
                    clacRsi(i);
                }
                kDatas.addAll(kLineBeans);
                if (chartPage != null) chartPage.initData();
            }
        }.start();

    }

    /**
     * 计算K线图中所有的数据
     *
     * @param weekKDatas
     */
    public void calcWeekKLineData(List<HsWeekKLineBean> weekKDatas) {
        new Thread() {
            @Override
            public void run() {
                if (kDatas != null) kDatas.clear();
                if (kLineBeans != null) kLineBeans.clear();
                if (MACDDatas != null) MACDDatas.clear();
                if (KDJDatas != null) KDJDatas.clear();
                if (RSIDatas != null) RSIDatas.clear();
                int count = weekKDatas.size();
                for (int i = 0; i < count; i++) {
                    KLineBean kLineData = new KLineBean();
                    HsWeekKLineBean hsWeekKLineBean = weekKDatas.get(i);
                    kLineBeans.add(kLineData);
                    kLineData.date = hsWeekKLineBean.getEndDate();
                    kLineData.open = hsWeekKLineBean.getOpenPrice();
                    kLineData.close = hsWeekKLineBean.getClosePrice();
                    kLineData.high = hsWeekKLineBean.getHighestPrice();
                    kLineData.low = hsWeekKLineBean.getLowestPrice();
                    kLineData.vol = hsWeekKLineBean.getTurnoverVol();
                    if (i > 0) {
                        kLineData.pre = weekKDatas.get(i - 1).getClosePrice();
                    } else {
                        kLineData.pre = kLineData.close;
                    }
                    volmax = Math.max(kLineData.vol, volmax);
                    xValuesLabel.put(i, kLineData.date);
                    clacDayLine(i, kLineData);
                    clacMacd(i, kLineData);
                    clacKDJ(i);
                    clacRsi(i);
                }
                kDatas.addAll(kLineBeans);
                if (chartPage != null) chartPage.initData();
            }
        }.start();

    }


    /**
     * 请求HS日K的数据
     */

    public void requestHsDayKData(String ticCode, String startTime, String endTime) {
        if (hsDayKModel != null) {
            hsDayKModel.setParameter(ticCode, startTime, endTime);
        } else {
            hsDayKModel = new HsDayKModel(mActivity, this, ticCode, startTime, endTime);
        }

        hsDayKModel.requestData();
    }


    /**
     * 请求HS周K的数据
     */

    public void requestHsWeekKData(String ticCode, String startTime, String endTime) {
        if (hsWeekKModel != null) {
            hsWeekKModel.setParameter(ticCode, startTime, endTime);
        } else {
            hsWeekKModel = new HsWeekKModel(mActivity, this, ticCode, startTime, endTime);
        }

        hsWeekKModel.requestData();
    }


    /**
     * 请求HS月K的数据
     */

    public void requestHsMonthKData(String ticCode, String startTime, String endTime) {
        if (hsMonthKModel != null) {
            hsMonthKModel.setParameter(ticCode, startTime, endTime);
        } else {
            hsMonthKModel = new HsMonthKModel(mActivity, this, ticCode, startTime, endTime);
        }

        hsMonthKModel.requestData();
    }


    /**
     * 计算RSI
     */
    private void clacRsi(int i) {

        RSIBean rsiBean = new RSIBean();
        if (i < 7) {
            rsiBean.RSI1 = 0;
            rsiBean.RSI2 = 0;
            rsiBean.RSI3 = 0;
        } else if (i < 13) {
            rsiBean.RSI1 = getRSI(i, 6);
            rsiBean.RSI2 = 0;
            rsiBean.RSI3 = 0;
        } else if (i < 25) {
            rsiBean.RSI1 = getRSI(i, 6);
            rsiBean.RSI2 = getRSI(i, 12);
            rsiBean.RSI3 = 0;
        } else {
            rsiBean.RSI1 = getRSI(i, 6);
            rsiBean.RSI2 = getRSI(i, 12);
            rsiBean.RSI3 = getRSI(i, 24);
        }

        RSIDatas.add(rsiBean);
    }


    /**
     * 计算KDJ
     */
    private void clacKDJ(int i) {

        KDJBean kdjBean = new KDJBean();
        if (i < KdjParameter) {
            kdjBean.RSV = getRsv(0, i);
        } else {
            kdjBean.RSV = getRsv(i - KdjParameter + 1, i);
        }
        if (i == 0) {
            kdjBean.K = 50;
            kdjBean.D = 50;
        } else {
            kdjBean.K = KDJDatas.get(i - 1).K * 2 / 3 + kdjBean.RSV * 1 / 3;
            kdjBean.D = KDJDatas.get(i - 1).D * 2 / 3 + kdjBean.K * 1 / 3;
        }
        kdjBean.J = 3 * kdjBean.K - 2 * kdjBean.D;
        KDJDatas.add(kdjBean);
    }

    /**
     * 计算MACD数据
     */
    private void clacMacd(int i, KLineBean kLineData) {
        MacdBean macdBean = new MacdBean();
        if (i == 0) {
            macdBean.ema12 = kLineData.close;
            macdBean.ema26 = kLineData.close;
        } else {
            macdBean.ema12 = MACDDatas.get(i - 1).ema12 * 11 / 13 + kLineData.close * 2 / 13;
            macdBean.ema26 = MACDDatas.get(i - 1).ema26 * 25 / 27 + kLineData.close * 2 / 27;
        }
        macdBean.dif = macdBean.ema12 - macdBean.ema26;
        if (i == 0) macdBean.dea = 0;
        else macdBean.dea = MACDDatas.get(i - 1).dea * 8 / 10 + macdBean.dif * 2 / 10;
        macdBean.macd = (macdBean.dif - macdBean.dea) * 2;
        MACDDatas.add(macdBean);
    }

    /**
     * 计算5,10,20日线数据
     */
    private void clacDayLine(int i, KLineBean kLineData) {
        VariateDayLineBean dayLineBean = new VariateDayLineBean();
        dayLineBean.day5Value = 0;
        dayLineBean.day10Value = 0;
        dayLineBean.day20Value = 0;
        dayLineBean.time = kLineData.date;
        if (i >= 4) {
            sum = 0;
            dayLineBean.day5Value = getSum(i - 4, i) / 5;
        }
        if (i >= 9) {
            sum = 0;
            dayLineBean.day10Value = getSum(i - 9, i) / 10;
        }
        if (i >= 19) {
            sum = 0;
            dayLineBean.day20Value = getSum(i - 19, i) / 20;
        }
        dayLineDatas.add(dayLineBean);
    }

    private float getSum(Integer a, Integer b) {

        for (int i = a; i <= b; i++) {
            sum += kLineBeans.get(i).close;
        }
        return sum;
    }


    /**
     * 计算6日RSI值
     */
    private float getRSI(int xIndex, int num) {
        float sumUp = 0;
        float sumDown = 0;
        for (int i = xIndex - num; i <= xIndex; i++) {
            float sum = kLineBeans.get(i).close - kLineBeans.get(i - 1).close;
            if (sum < 0) sumDown += sum;
            else sumUp += sum;
        }
        float rs = Math.abs((sumUp / num) / (sumDown / num));
        float rsi = rs / (1 + rs) * 100;
        if (rsi >= 0 && rsi <= 100)
            return rsi;
        else return 100;
    }


    /**
     * 计算Rsv值
     *
     * @param start
     * @param end
     * @return
     */
    private float getRsv(int start, int end) {
        float C9 = kLineBeans.get(end).close;
        float L9 = Float.MAX_VALUE;
        float H9 = Float.MIN_VALUE;
        for (int i = start; i <= end; i++) {
            if (L9 > kLineBeans.get(i).low) L9 = kLineBeans.get(i).low;
            if (H9 < kLineBeans.get(i).high) H9 = kLineBeans.get(i).high;
        }

        return (C9 - L9) / (H9 - L9) * 100;
    }


    public float getMin() {
        return baseValue - permaxmin;
    }

    public float getMax() {
        return baseValue + permaxmin;
    }

    public float getPercentMax() {
        if (baseValue == 0) return 0.1f;
        else return permaxmin / baseValue;
    }

    public float getPercentMin() {
        if (baseValue == 0) return -0.1f;
        else return -getPercentMax();
    }


    public float getVolmax() {
        return volmax;
    }

    public ArrayList<MacdBean> getMACDDatas() {
        return MACDDatas;
    }

    public ArrayList<MinutesBean> getDatas() {
        return datas;
    }

    public ArrayList<KLineBean> getKLineDatas() {
        return kDatas;
    }

    public ArrayList<KDJBean> getKDJDatas() {
        return KDJDatas;
    }

    public ArrayList<RSIBean> getRSIDatas() {
        return RSIDatas;
    }

    public ArrayList<VariateDayLineBean> getDayLineDatas() {
        return dayLineDatas;
    }

    public SparseArray<String> getXValuesLabel() {
        return xValuesLabel;
    }

    public void setChartPage(BaseChartPage chartPage) {
        this.chartPage = chartPage;
    }

    public ArrayList<MinuteTradingBean> getMinuteTradingList() {
        return minuteTradingList;
    }
}
