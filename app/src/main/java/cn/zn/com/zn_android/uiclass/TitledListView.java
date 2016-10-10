package cn.zn.com.zn_android.uiclass;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;

/**
 * Created by Jolly on 2016/6/29 0029.
 */
public class TitledListView extends XListView {

    public View mTitle;

    public interface TitleClickListener {
        void onClick(View v);
    }

    private TitleClickListener titleClickListener;

    public void setTitleClickListener(TitleClickListener titleClickListener) {
        this.titleClickListener = titleClickListener;
    }

    public TitledListView(Context context) {
        this(context, null);
    }

    public TitledListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TitledListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mTitle = inflater.inflate(R.layout.layout_title_tv, this, false);
        this.setLoadMoreEnableShow(false);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mTitle != null) {
            measureChild(mTitle, widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mTitle != null) {
            mTitle.layout(0, 0, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight());
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (getFirstVisiblePosition() > 1) {
            drawChild(canvas, mTitle, getDrawingTime());
            mTitle.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    titleClickListener.onClick(v);
                    return true;
                }
            });
        }
    }

    private static final String TAG = "TitledListView";

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getFirstVisiblePosition() > 1) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    float heigh = mTitle.getHeight();
                    float y = ev.getY();
                    Log.d(TAG, "y= " + y + "/nheigh=" + heigh);
                    if (y - heigh < 0) {
                        Log.d(TAG, "dispatchTouchEvent: " + y);
                        titleClickListener.onClick(mTitle);
                        return false;

                    } else {
                        return super.dispatchTouchEvent(ev);
                    }
                default:
                    return super.dispatchTouchEvent(ev);
            }

        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mTitle = inflater.inflate(R.layout.layout_title_tv, this, false);
    }

    public void moveTitle() {
        View bottomChild = getChildAt(0);
        if (bottomChild != null) {
            int bottom = bottomChild.getBottom();
            int height = mTitle.getMeasuredHeight();
            int y = 0;
            if (bottom < height) {
                y = bottom - height;
            }
            mTitle.layout(0, y, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight() + y);
        }
    }

    public void updateTitle(String title, int index) {
        if (title != null) {
            TextView title_text = (TextView) mTitle.findViewById(android.R.id.title);
            title_text.setText(title);
        }
        mTitle.layout(0, 0, mTitle.getMeasuredWidth(), mTitle.getMeasuredHeight());
        mTitle.setTag(title);

    }


}
