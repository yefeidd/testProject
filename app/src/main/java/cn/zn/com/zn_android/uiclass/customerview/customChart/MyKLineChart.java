package cn.zn.com.zn_android.uiclass.customerview.customChart;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import cn.zn.com.zn_android.model.chartBean.KDJBean;
import cn.zn.com.zn_android.model.chartBean.RSIBean;
import cn.zn.com.zn_android.presenter.ChartParse;
import cn.zn.com.zn_android.utils.UnitUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * Created by zjs on 2016/8/18 0018.
 * email: m15267280642@163.com
 * explain:
 */
public class MyKLineChart extends LineChart {
    private MyLeftMarkerView myMarkerViewLeft;
    private MyRightMarkerView myMarkerViewRight;
    private ChartParse kDataHelper;
    private float leftPosx, leftPosy, rightPosx, rightPosy;
    private SpannableString str1, str2, str3, strTime;
    private int xIndex;
    private SpannableStringBuilder builder;
    private LineChartType type;

    public enum LineChartType {
        KDJ, RSI
    }

    public MyKLineChart(Context context) {
        this(context, null);
    }

    public MyKLineChart(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyKLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public void setMarker(MyLeftMarkerView markerLeft, MyRightMarkerView markerRight, ChartParse kDataHelper, LineChartType type) {
        this.myMarkerViewLeft = markerLeft;
        this.myMarkerViewRight = markerRight;
        this.kDataHelper = kDataHelper;
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
        builder = new SpannableStringBuilder();
        if (!mDrawMarkerViews)
            return;

        if (!valuesToHighlight()) {
            xIndex = kDataHelper.getKLineDatas().size() - 1;
            if (type == LineChartType.KDJ) {
                SpannableString[] ss = getKDJSpannable();
                myMarkerViewLeft.setData(ss[0]);
                myMarkerViewRight.setData(ss[1]);
                myMarkerViewLeft.refreshContent(null, null);
                myMarkerViewRight.refreshContent(null, null);
            } else {
                SpannableString[] ss = getRSISpannable();
                myMarkerViewLeft.setData(ss[0]);
                myMarkerViewRight.setData(ss[1]);
                myMarkerViewLeft.refreshContent(null, null);
                myMarkerViewRight.refreshContent(null, null);
            }
             /*重新计算大小*/
            calcMarketMeasure();
            leftPosx = mViewPortHandler.contentLeft();
            leftPosy = mViewPortHandler.contentTop();
            rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
            rightPosy = mViewPortHandler.contentTop();
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
                    if (type == LineChartType.KDJ) {
                        SpannableString[] ss = getKDJSpannable();
                        myMarkerViewLeft.setData(ss[0]);
                        myMarkerViewRight.setData(ss[1]);
                        myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                        myMarkerViewRight.refreshContent(e, mIndicesToHighlight[i]);
                    } else {
                        SpannableString[] ss = getRSISpannable();
                        myMarkerViewLeft.setData(ss[0]);
                        myMarkerViewRight.setData(ss[1]);
                        myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                        myMarkerViewRight.refreshContent(e, mIndicesToHighlight[i]);
                    }
             /*重新计算大小*/
                    calcMarketMeasure();
                    leftPosx = mViewPortHandler.contentLeft();
                    leftPosy = mViewPortHandler.contentTop() - myMarkerViewLeft.getHeight();
                    rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
                    rightPosy = mViewPortHandler.contentTop() - myMarkerViewRight.getHeight();
                    leftPosx = mViewPortHandler.contentLeft();
                    leftPosy = mViewPortHandler.contentTop();
                    rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
                    rightPosy = mViewPortHandler.contentTop();
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
     * 设置KDJ的高亮值显示
     */
    private SpannableString[] getKDJSpannable() {
        SpannableString[] ss = new SpannableString[2];
        builder.clear();
        KDJBean kdjbean = kDataHelper.getKDJDatas().get(xIndex);
        str1 = new SpannableString("K:" + UnitUtils.getTwoDecimal(kdjbean.K) + " ");
        str2 = new SpannableString("D:" + UnitUtils.getTwoDecimal(kdjbean.D) + " ");
        str3 = new SpannableString("J:" + UnitUtils.getTwoDecimal(kdjbean.J) + " ");
        ForegroundColorSpan spanK = new ForegroundColorSpan(myMarkerViewLeft.colors[0]);
        ForegroundColorSpan spanD = new ForegroundColorSpan(myMarkerViewLeft.colors[1]);
        ForegroundColorSpan spanJ = new ForegroundColorSpan(myMarkerViewLeft.colors[2]);
        str1.setSpan(spanK, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str2.setSpan(spanD, 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str3.setSpan(spanJ, 0, str3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str1).append(str2).append(str3);
        ss[0] = SpannableString.valueOf(builder);
        strTime = new SpannableString(SpannableString.valueOf(""));
        ForegroundColorSpan spanTime = new ForegroundColorSpan(myMarkerViewRight.colors[0]);
        strTime.setSpan(spanTime, 0, strTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss[1] = strTime;
        return ss;
    }

    /**
     * 设置RSI的高亮值显示
     */
    private SpannableString[] getRSISpannable() {
        SpannableString[] ss = new SpannableString[2];
        builder.clear();
        RSIBean rsiBean = kDataHelper.getRSIDatas().get(xIndex);
        str1 = new SpannableString("6:" + UnitUtils.getTwoDecimal(rsiBean.RSI1) + " ");
        str2 = new SpannableString("12:" + UnitUtils.getTwoDecimal(rsiBean.RSI2) + " ");
        str3 = new SpannableString("24:" + UnitUtils.getTwoDecimal(rsiBean.RSI3));
        ForegroundColorSpan spanR = new ForegroundColorSpan(myMarkerViewLeft.colors[0]);
        ForegroundColorSpan spanS = new ForegroundColorSpan(myMarkerViewLeft.colors[1]);
        ForegroundColorSpan spanI = new ForegroundColorSpan(myMarkerViewLeft.colors[2]);
        str1.setSpan(spanR, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str2.setSpan(spanS, 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str3.setSpan(spanI, 0, str3.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str1).append(str2).append(str3);
        ss[0] = SpannableString.valueOf(builder);
        strTime = new SpannableString(SpannableString.valueOf(""));
        ForegroundColorSpan spanTime = new ForegroundColorSpan(myMarkerViewRight.colors[0]);
        strTime.setSpan(spanTime, 0, strTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss[1] = strTime;
        return ss;
    }
}
