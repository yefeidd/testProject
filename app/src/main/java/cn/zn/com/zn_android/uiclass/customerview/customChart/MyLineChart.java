package cn.zn.com.zn_android.uiclass.customerview.customChart;

import android.content.Context;
import android.graphics.Canvas;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;

import cn.zn.com.zn_android.model.chartBean.MinutesBean;
import cn.zn.com.zn_android.presenter.ChartParse;
import cn.zn.com.zn_android.utils.UnitUtils;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;

/**
 * 作者：ajiang
 * 邮箱：1025065158
 * 博客：http://blog.csdn.net/qqyanjiang
 */
public class MyLineChart extends LineChart {
    private MyLeftMarkerView myMarkerViewLeft;
    private MyRightMarkerView myMarkerViewRight;
    private ChartParse minuteHelper;
    private float leftPosx, leftPosy, rightPosx, rightPosy;
    private int xIndex;
    private SpannableString str1, str2, strTime;
    private SpannableStringBuilder builder;


    public MyLineChart(Context context) {
        super(context);
    }

    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyLineChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void init() {
        super.init();
        /*此两处不能重新示例*/
        mXAxis = new MyXAxis();
        mAxisLeft = new MyYAxis(YAxis.AxisDependency.LEFT);
        mXAxisRenderer = new MyXAxisRenderer(mViewPortHandler, (MyXAxis) mXAxis, mLeftAxisTransformer, this);
        mAxisRendererLeft = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisLeft, mLeftAxisTransformer);

        mAxisRight = new MyYAxis(YAxis.AxisDependency.RIGHT);
        mAxisRendererRight = new MyYAxisRenderer(mViewPortHandler, (MyYAxis) mAxisRight, mRightAxisTransformer);

    }

    /*返回转型后的左右轴*/
    @Override
    public MyYAxis getAxisLeft() {
        return (MyYAxis) super.getAxisLeft();
    }

    @Override
    public MyXAxis getXAxis() {
        return (MyXAxis) super.getXAxis();
    }


    @Override
    public MyYAxis getAxisRight() {
        return (MyYAxis) super.getAxisRight();
    }

    public void setMarker(MyLeftMarkerView markerLeft, MyRightMarkerView markerRight, ChartParse minuteHelper) {
        this.myMarkerViewLeft = markerLeft;
        this.myMarkerViewRight = markerRight;
        this.minuteHelper = minuteHelper;
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
            xIndex = minuteHelper.getDatas().size() - 1;
            SpannableString[] ss = getMinuteSpannable();
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

                    SpannableString[] ss = getMinuteSpannable();
                    myMarkerViewLeft.setData(ss[0]);
                    myMarkerViewRight.setData(ss[1]);
                    myMarkerViewLeft.refreshContent(e, mIndicesToHighlight[i]);
                    myMarkerViewRight.refreshContent(e, mIndicesToHighlight[i]);
             /*重新计算大小*/
                    calcMarketMeasure();
                    leftPosx = mViewPortHandler.contentLeft();
                    leftPosy = mViewPortHandler.contentTop() - myMarkerViewLeft.getHeight();
                    rightPosx = mViewPortHandler.contentRight() - myMarkerViewRight.getWidth();
                    rightPosy = mViewPortHandler.contentTop() - myMarkerViewRight.getHeight();
                    myMarkerViewLeft.draw(canvas, leftPosx, leftPosy);
                    myMarkerViewRight.draw(canvas, rightPosx, rightPosy);
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
     * 设置分时图的高亮值显示
     */
    private SpannableString[] getMinuteSpannable() {
        SpannableString[] ss = new SpannableString[2];
        builder.clear();
        MinutesBean minutesBean = minuteHelper.getDatas().get(xIndex);
        str1 = new SpannableString("分时：" + UnitUtils.getTwoDecimal(minutesBean.LastPrice) + "  ");
        str2 = new SpannableString("均线：" + UnitUtils.getTwoDecimal(minutesBean.average));
        ForegroundColorSpan spanCj = new ForegroundColorSpan(myMarkerViewLeft.colors[0]);
        ForegroundColorSpan spanAv = new ForegroundColorSpan(myMarkerViewLeft.colors[1]);
        str1.setSpan(spanCj, 0, str1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        str2.setSpan(spanAv, 0, str2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(str1).append(str2);
        ss[0] = SpannableString.valueOf(builder);

        strTime = new SpannableString(minutesBean.time);
        ForegroundColorSpan spanTime = new ForegroundColorSpan(myMarkerViewRight.colors[0]);
        strTime.setSpan(spanTime, 0, strTime.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss[1] = strTime;
        return ss;
    }
}
