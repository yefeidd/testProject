package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;

import cn.zn.com.zn_android.utils.DensityUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jolly on 2016/7/6 0006.
 */
public class CanvasChartView extends View {

    private final int X_DEFAULT = 500;
    private final int Y_DEFAULT = 300;
    private int TXT_SIZE = 9;

    private final String TAG = "CanvasChartViewTag";

    private Context mContext;
    private List<Float> data = new ArrayList<>();
    private String startTime = "", endTime ="";

    private int xStart = 0, yStart = 10; // 原点

    private float xLength, yLength; // x、y轴长度
    private int xPointSum, yPointSum = 5; // x轴最多画多少点
    private float xStep = 10, yStep; // y轴步长
    private float minY, maxY;
    private long touchDownTime = 0;
    private float labelLen = 30;
    private float touchX, touchY;
    private float paintStrockWidth = 5.0f;

//    private String[] yLabels = new String[yPointSum + 1]; // y轴上的标注
    private String[] yLabels = {"-3", "-2", "-1", "0", "1", "2"}; // y轴上的标注
    private float yLabel = 1; // y轴 数据的间距

    public CanvasChartView(Context context) {
        this(context, null);
    }

    public CanvasChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CanvasChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.TXT_SIZE = DensityUtil.dip2px(context, TXT_SIZE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        //Measure Width
        if (widthMode == MeasureSpec.EXACTLY) {
            //Must be this size
            width = widthSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            width = Math.min(X_DEFAULT, widthSize);
        } else {
            //Be whatever you want
            width = X_DEFAULT;
        }

        //Measure Height
        if (heightMode == MeasureSpec.EXACTLY) {
            //Must be this size
            height = heightSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            //Can't be bigger than...
            height = Math.min(Y_DEFAULT, heightSize);
        } else {
            //Be whatever you want
            height = Y_DEFAULT;
        }

        //MUST CALL THIS
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (yLabels[0] == null) {
            return;
        }

        xPointSum = data.size();

        xLength = getWidth();
        yLength = getHeight();

        Paint axisPaint = new Paint();
        axisPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        axisPaint.setColor(Color.GRAY);
        axisPaint.setAntiAlias(true);
        axisPaint.setTextSize(TXT_SIZE);

//        xLength = xLength - 10 - axisPaint.measureText(yLabels[yPointSum]);
        xLength = xLength - DensityUtil.dip2px(mContext, 5) - TXT_SIZE * (yLabels[yPointSum] + "%").length();
        yLength = yLength - TXT_SIZE - axisPaint.measureText(yLabels[yPointSum] + "%");
        yStep = yLength / yPointSum;

        if (data.size() > 1) xStep = xLength / (data.size() - 1);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.RED);
        paint.setAntiAlias(true);
        paint.setTextSize(TXT_SIZE);

        // 画Y轴
//        canvas.drawLine(xStart, yStart, xStart, yLength + yStart, axisPaint);
//        canvas.drawLine(xLength, yStart, xLength, yLength + yStart, axisPaint);

        // 画X轴
        for (int i = 0; i <= yPointSum; i ++) {
            canvas.drawText(yLabels[i] + "%", xLength + 5, yLength - i * yStep + 10 + yStart, axisPaint); // 标注
            canvas.drawLine(xStart, yLength - i * yStep + yStart, xLength, yLength - i * yStep + yStart, axisPaint);  //刻度
        }

        // 画开始结束时间
        canvas.drawText(startTime, xStart, yLength + yStart * 2 + TXT_SIZE, axisPaint);
        canvas.drawText(endTime, xLength - (axisPaint.measureText(endTime)),
                yLength + yStart * 2 + TXT_SIZE, axisPaint);

        // 画底部说明
//        String lab1 = "概念指数";
//        String lab2 = "沪深300";
//        String lab3 = "（近一年走势）";

//        canvas.drawRect(xStart, yLength + yStart + yStart, xStart + TXT_SIZE, yLength + yStart + yStart + TXT_SIZE, paint);
//        canvas.drawText(lab1, xStart + TXT_SIZE / 2.0f * 3, yLength + yStart * 2 + TXT_SIZE, paint);
//        canvas.drawRect(xStart + TXT_SIZE * (lab1.length() + 2) + TXT_SIZE * 0.5f, yLength + yStart * 2,
//                xStart + TXT_SIZE * (lab1.length() + 3) + TXT_SIZE * 0.5f, yLength + yStart + yStart + TXT_SIZE, axisPaint);
//        canvas.drawText(lab2, xStart + TXT_SIZE * (lab1.length() + 4), yLength + yStart * 2 + TXT_SIZE, axisPaint);
//        canvas.drawText(lab3, xLength - (lab3.length() - 2) * TXT_SIZE, yLength + yStart * 2 + TXT_SIZE, axisPaint);

        // 画折线
        paint.setStrokeWidth(paintStrockWidth);
        if (data.size() == 0) {
            return;
        }
        for (int i = 1; i < data.size(); i ++) {
            float y1 = yStart + yLength * (Float.valueOf(yLabels[5]) - data.get(i - 1)) / (yLabel * yPointSum);
            float y2 = yStart + yLength * (Float.valueOf(yLabels[5]) - data.get(i)) / (yLabel * yPointSum);
//            float y1 = yStart + yLength * (yLabel - data.get(i - 1)) / (yLabel * yPointSum);
//            float y2 = yStart + yLength * (yLabel - data.get(i)) / (yLabel * yPointSum);
            canvas.drawLine(xStart + (i - 1) * xStep, y1,
                    xStart + xStep * i, y2, paint);
        }

        if (touchY != 0) {
            Paint touchPaint = new Paint();
            touchPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            touchPaint.setColor(Color.GREEN);
            touchPaint.setAntiAlias(true);
            touchPaint.setTextSize(TXT_SIZE);

            canvas.drawLine(xStart, touchY, xLength, touchY, touchPaint);
            canvas.drawLine(touchX, yStart, touchX, yLength, touchPaint);
            return;
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                CanvasChartView.this.invalidate();
            }
        }
    };

    public void setData(List<Float> data, String startTime, String endTime) {
        this.data = data;
        this.startTime = startTime;
        this.endTime = endTime;

        touchY = touchX = 0;
        maxY = 0.0f;
        minY = 0.0f;
        for (int i = 0; i < data.size(); i ++) {
            float bean = data.get(i);
            if (i == 0) {
                maxY = minY = bean;
            } else {
                if (bean > maxY) {
                    maxY = bean;
                }
                if (bean < minY) {
                    minY = bean;
                }
            }

        }

        float step = (maxY - minY) / 5;
        if (step < yLabel) {
            if (maxY > Float.valueOf(yLabels[5])) {
                Float end = 0.0f;
                end = maxY + 0.5f;
//                if (maxY % 2 == 1) {
//
//                } else {
//                    end = maxY + 2;
//                }
                for (int i = yLabels.length - 1; i >= 0; i --) {
                    BigDecimal b = new BigDecimal(end - (5 - i) * yLabel);
                    yLabels[i] = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                }
            } else if (minY < Float.valueOf(yLabels[0])){
                Float start = 0.0f;
                start = minY - 0.5f;
//                if ((int)minY % 2 == 1) {
//
//                } else {
//                    start = minY - 2;
//                }
                for (int i = 0; i < yLabels.length; i ++) {
                    BigDecimal b = new BigDecimal(start + i * yLabel);
                    yLabels[i] = b.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
                }
            }
        } else {
            yLabel = step;
            for (int i = 0; i < yLabels.length; i ++) {
                BigDecimal b  =   new BigDecimal(yLabel * i + minY);
                yLabels[i] = b.setScale(2,  BigDecimal.ROUND_HALF_UP).toString();
            }
        }

        handler.sendEmptyMessage(0);

    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()) {
//            case MotionEvent.ACTION_MOVE:
//                if (event.getEventTime() - touchDownTime > 700) {
//                    if (!VibratorUtils.isVirate())
//                    VibratorUtils.vibrator((Activity) mContext, 70);
//                    touchX = event.getX();
//                    touchY = event.getY();
//                    handler.sendEmptyMessage(0);
//                    return true;
//                } else {
//                    return super.onTouchEvent(event);
//                }
//
//            case MotionEvent.ACTION_DOWN:
//                touchDownTime = event.getEventTime();
//                touchX = event.getX();
//                touchY = event.getY();
//                handler.sendEmptyMessage(0);
////                return super.onTouchEvent(event);
//            case MotionEvent.ACTION_UP:
//                VibratorUtils.setIsVibrate(false);
//                touchY = touchX = 0;
//                handler.sendEmptyMessage(0);
//                return true;
//
//            default:
//                return super.onTouchEvent(event);
//        }
//
//    }
}
