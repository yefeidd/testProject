package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import cn.zn.com.zn_android.utils.DensityUtil;

/**
 * 跑马灯 TextView
 * Created by Jolly on 2016/3/28 0028.
 */
public class MarqueeText extends TextView implements Runnable {
    private final int SPEED = 2; // 步长
    private final int TIME = 50; // 时间间隔

    private int currentScrollX = 0;// 当前滚动的位置
    private boolean isStop = false;
    private int textWidth;
    private boolean isMeasure = false;

    public MarqueeText(Context context) {
        this(context, null);
// TODO Auto-generated constructor stub
    }
    public MarqueeText(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public MarqueeText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    protected void onDraw(Canvas canvas) {
// TODO Auto-generated method stub
        super.onDraw(canvas);
        if (!isMeasure) {// 文字宽度只需获取一次就可以了
            getTextWidth();
            isMeasure = true;
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d("getTextWidth", "onWindowFocusChanged: " +getMeasuredWidth());

    }

    /**
     * 获取文字宽度
     */
    private void getTextWidth() {
        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
    }
    @Override
    public void run() {
        scrollTo(currentScrollX, 0);
        if (isStop) {
            return;
        }

//        Log.i("MarqueeText", "  getWidth: " + this.getWidth() + "  textWidth: " + textWidth + "  currentScrollX: " + currentScrollX + "  getScrollX : " + getScrollX());
        if (getScrollX() >= textWidth) {
            scrollTo(-(this.getWidth()), 0);
            currentScrollX = -(this.getWidth());
// return;
        }
        currentScrollX += SPEED;// 滚动速度
        postDelayed(this, TIME);
    }
    // 开始滚动
    public void startScroll() {
        isStop = false;
        this.removeCallbacks(this);
        post(this);
    }
    // 停止滚动
    public void stopScroll() {
        isStop = true;
    }
    // 从头开始滚动
    public void startFor0() {
        currentScrollX = 0;
        startScroll();
    }

    public void removeScroll() {
        this.removeCallbacks(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        Paint paint = this.getPaint();
        String str = this.getText().toString();
        textWidth = (int) paint.measureText(str);
        //MUST CALL THIS
        if (DensityUtil.getScreenWidth(getContext())[0] < textWidth) {
            Log.d("onMeasure", "true: ");
            setMeasuredDimension(textWidth, heightSize);
        } else {
            Log.d("onMeasure", "false: " + DensityUtil.getScreenWidth(getContext())[0] + "--" + textWidth);
            setMeasuredDimension(DensityUtil.getScreenWidth(getContext())[0], heightSize);
        }

    }
}
