package cn.zn.com.zn_android.model.chartBean;

/**
 * Created by zjs on 2016/8/1 0001.
 * email: m15267280642@163.com
 * explain: The data of MACD ,contain
 * EMA(12)= 前一日EMA（12） X 11/13 + 今日收盘价 X 2/13
 * EMA(26)= 前一日EMA（26） X 25/27 + 今日收盘价 X 2/27
 * DIF=EMA(12)-EMA(26)
 * DEA = （前一日DEA X 8/10 + 今日DIF X 2/10）
 * MACD=（DIF-DEA）*2
 */
public class MacdBean implements ChartImp {
    public float ema12;
    public float ema26;
    /**
     * 快速线
     */
    public float dif;
    /**
     * 慢速线
     */
    public float dea;
    /**
     * MACD柱形图
     */
    public float macd;
    /**
     * 日期
     */
}
