package cn.zn.com.zn_android.uiclass.customerview.customChart;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import cn.zn.com.zn_android.model.bean.VariateDayLineBean;
import cn.zn.com.zn_android.model.chartBean.MacdBean;
import cn.zn.com.zn_android.presenter.ChartParse;
import cn.zn.com.zn_android.utils.UnitUtils;
import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Created by zjs on 2016/8/16 0016.
 * email: m15267280642@163.com
 * explain:
 */
public class MyCombinedChart extends CombinedChart {

    private MyLeftMarkerView myMarkerViewLeft;
    private MyRightMarkerView myMarkerViewRight;
    private ChartParse chartHelper;
    private combineChartType type;
    private float leftPosx, leftPosy, rightPosx, rightPosy;
    private SpannableString str1, str2, str3, strTime;
    private int xIndex;
    private SpannableStringBuilder builder;

    public enum combineChartType {
        CANDLE, MACD
    }

    public MyCombinedChart(Context context) {
        this(context, null);
    }

    public MyCombinedChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCombinedChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setMarker(MyLeftMarkerView markerLeft, MyRightMarkerView markerRight, ChartParse chartHelper, combineChartType type) {
        this.myMarkerViewLeft = markerLeft;
        this.myMarkerViewRight = markerRight;
        this.chartHelper = chartHelper;
        this.type = type;
    }

    public void setHighlightValue(Highlight h) {
        if (mData == null)
            mIndicesToHighlight = null;
        else {
            mIndicesToHighlight = new Highlight[]{
                    h};
        }
        invalidate();
    }

    @Override
    protected void drawMarkers(Canvas canvas) {
        if (!mDrawMarkerViews)
            return;
        if (!valuesToHighlight()) {
            builder = new SpannableStringBuilder();
            xIndex = chartHelper.getKLineDatas().size() - 1;
            if (type == combineChartType.CANDLE) {
                SpannableString[] ss = getCandleSpannable();
                myMarkerViewLeft.setData(ss[0]);
                myMarkerViewRight.setData(ss[1]);
                myMarkerViewLeft.refreshContent(null, null);
                myMarkerViewRight.refreshContent(null, null);
             /*重新计算大小*/
                calcMarketMeasure();
                leftPosx = mViewPortHandler.contentLeft();
                leftPosy = mViewPortHandler.contentTop() - myMarkerViewLeft.getHeight();
                rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
                rightPosy = mViewPortHandler.contentTop() - myMarkerViewRight.getHeight();
            } else {
                SpannableString[] ss = getMacdSpannable();
                myMarkerViewLeft.setData(ss[0]);
                myMarkerViewRight.setData(ss[1]);
                myMarkerViewLeft.refreshContent(null, null);
                myMarkerViewRight.refreshContent(null, null);
             /*重新计算大小*/
                calcMarketMeasure();
                leftPosx = mViewPortHandler.contentLeft();
                leftPosy = mViewPortHandler.contentTop();
                rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
                rightPosy = mViewPortHandler.contentTop();
            }
            myMarkerViewLeft.draw(canvas, leftPosx, leftPosy);
            myMarkerViewRight.draw(canvas, rightPosx, rightPosy);
        } else {

            for (int i = 0; i < mIndicesToHighlight.length; i++) {
                Highlight highlight = mIndicesToHighlight[i];
                xIndex = mIndicesToHighlight[i].getXIndex();
                int dataSetIndex = mIndicesToHighlight[i].getDataSetIndex();
                float deltaX = mXAxis != null
                        ? mXAxis.mAxisRange
                        : ((mData == null ? 0.f : mData.getXValCount()) - 1.f);
                if (xIndex <= deltaX && xIndex <= deltaX * mAnimator.getPhaseX()) {
                    Entry e = mData.getEntryForHighlight(mIndicesToHighlight[i]);
                    // make sure entry not null
                    if (e == null || e.getXIndex() != mIndicesToHighlight[i].getXIndex())
                        continue;
                    float[] pos = getMarkerPosition(e, highlight);
                    // check bounds
                    if (!mViewPortHandler.isInBounds(pos[0], pos[1]))
                        continue;

                    if (type == combineChartType.CANDLE) {
                        SpannableString[] ss = getCandleSpannable();
                        myMarkerViewLeft.setData(ss[0]);
                        myMarkerViewRight.setData(ss[1]);
                        myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                        myMarkerViewRight.refreshContent(e, mIndicesToHighlight[i]);
                        calcMarketMeasure();
                        leftPosx = mViewPortHandler.contentLeft();
                        leftPosy = mViewPortHandler.contentTop() - myMarkerViewLeft.getHeight();
                        rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
                        rightPosy = mViewPortHandler.contentTop() - myMarkerViewRight.getHeight();
                    } else {
                        SpannableString[] ss = getMacdSpannable();
                        myMarkerViewLeft.setData(ss[0]);
                        myMarkerViewRight.setData(ss[1]);
                        myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                        myMarkerViewRight.refreshContent(e, mIndicesToHighlight[i]);
                        calcMarketMeasure();
                        leftPosx = mViewPortHandler.contentLeft();
                        leftPosy = mViewPortHandler.contentTop();
                        rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
                        rightPosy = mViewPortHandler.contentTop();
                    }
                    myMarkerViewLeft.draw(canvas, leftPosx, leftPosy);
                    myMarkerViewRight.draw(canvas, rightPosx, rightPosy);
                }
            }
        }
    }


    /**
     * 重新计算market的大小
     */
    private void calcMarketMeasure() {
        myMarkerViewLeft.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        myMarkerViewLeft.layout(0, 0, myMarkerViewLeft.getMeasuredWidth(),
                myMarkerViewLeft.getMeasuredHeight());
        myMarkerViewRight.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        myMarkerViewRight.layout(0, 0, myMarkerViewRight.getMeasuredWidth(),
                myMarkerViewRight.getMeasuredHeight());
    }

    /**
     * 设置candle的高亮值显示
     */
    private SpannableString[] getCandleSpannable() {
        SpannableString[] ss = new SpannableString[2];
        builder.clear();
        VariateDayLineBean lineBean = chartHelper.getDayLineDatas().get(xIndex);
        str1 = new SpannableString("M5:" + UnitUtils.getTwoDecimal(lineBean.day5Value) + " ");
        str2 = new SpannableString("M10:" + UnitUtils.getTwoDecimal(lineBean.day10Value) + " ");
        str3 = new SpannableString("M20:" + UnitUtils.getTwoDecimal(lineBean.day20Value));
        ForegroundColorSpan span5 = new ForegroundColorSpan(myMarkerViewLeft.colors[0]);
        ForegroundColorSpan span10 = new ForegroundColorSpan(myMarkerViewLeft.colors[1]);
        ForegroundColorSpan span20 = new ForegroundColorSpan(myMarkerViewLeft.colors[2]);
        str1.setSpan(span5, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str2.setSpan(span10, 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str3.setSpan(span20, 0, str3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str1).append(str2).append(str3);
        ss[0] = SpannableString.valueOf(builder);
        if (null != lineBean && null != lineBean.time) {
            strTime = new SpannableString(lineBean.time);
            ForegroundColorSpan spanTime = new ForegroundColorSpan(myMarkerViewRight.colors[0]);
            strTime.setSpan(spanTime, 0, strTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss[1] = strTime;
        }
        return ss;
    }

    /**
     * 设置macd的高亮值显示
     */
    private SpannableString[] getMacdSpannable() {
        SpannableString[] ss = new SpannableString[2];
        builder.clear();
        MacdBean macdBean = chartHelper.getMACDDatas().get(xIndex);
        str1 = new SpannableString("DIF:" + UnitUtils.getTwoDecimal(macdBean.dif) + " ");
        str2 = new SpannableString("DEA:" + UnitUtils.getTwoDecimal(macdBean.dea));
        ForegroundColorSpan spanDif = new ForegroundColorSpan(myMarkerViewLeft.colors[2]);
        ForegroundColorSpan spanDea = new ForegroundColorSpan(myMarkerViewLeft.colors[1]);
        str1.setSpan(spanDif, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str2.setSpan(spanDea, 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str1).append(str2);
        ss[0] = SpannableString.valueOf(builder);
        ss[1] = SpannableString.valueOf("");
        return ss;
    }
}
