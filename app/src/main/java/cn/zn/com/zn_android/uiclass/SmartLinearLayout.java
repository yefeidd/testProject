package cn.zn.com.zn_android.uiclass;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.LinearLayout;

import com.nineoldandroids.view.ViewHelper;
import com.nineoldandroids.view.ViewPropertyAnimator;

/**
 * 动画显示隐藏布局
 * Created by Jolly on 2016/4/21 0021.
 */
public class SmartLinearLayout extends LinearLayout {

    private static final int TRANSLATE_DURATION_MILLIS = 200;

    private final Interpolator mInterpolator = new AccelerateDecelerateInterpolator();
    private boolean mVisible;

    public SmartLinearLayout(Context context) {
        this(context, null);
    }

    public SmartLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SmartLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mVisible = true;
    }

    public boolean isVisible() {
        return mVisible;
    }

    /**
     * 底部向上显示
     */
    public void show() {
        show(true);
    }

    /**
     * 底部向下隐藏
     */
    public void hide() {
        hide(true);
    }

    public void show(boolean animate) {
        toggle(true, animate, false, false);
    }

    public void hide(boolean animate) {
        toggle(false, animate, false, false);
    }

    /**
     * 顶部往下显示
     *
     * @param animate
     * @param isTop 布局位于顶部
     */
    public void show(boolean animate, boolean isTop) {
        toggle(true, animate, false, isTop);
    }

    /**
     * 顶部往上隐藏
     *
     * @param animate
     * @param isTop 布局位于顶部
     */
    public void hide(boolean animate, boolean isTop) {
        toggle(false, animate, false, isTop);
    }

    /**
     * @param visible
     * @param animate
     * @param force 是否强制显示隐藏
     * @param isTop 布局位于顶部
     */
    private void toggle(final boolean visible, final boolean animate, boolean force, boolean isTop) {
        if (mVisible != visible || force) {
            mVisible = visible;
            int height = getHeight();
            if (height == 0 && !force) {
                ViewTreeObserver vto = getViewTreeObserver();
                if (vto.isAlive()) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                        @Override
                        public boolean onPreDraw() {
                            ViewTreeObserver currentVto = getViewTreeObserver();
                            if (currentVto.isAlive()) {
                                currentVto.removeOnPreDrawListener(this);
                            }
                            toggle(visible, animate, true, isTop);
                            return true;
                        }
                    });
                    return;
                }
            }
            int translationY = visible ? 0 : height;
            if (isTop) translationY = -translationY;
            if (animate) {
                ViewPropertyAnimator.animate(this).setInterpolator(mInterpolator)
                        .setDuration(TRANSLATE_DURATION_MILLIS)
                        .translationY(translationY);
            } else {
                ViewHelper.setTranslationY(this, translationY);
            }

            if (!hasHoneycombApi()) {
                setClickable(visible);
            }
        }
    }

    /**
     *
     * @return
     */
    private int getMarginBottom() {
        int marginBottom = 0;
        final ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (layoutParams instanceof MarginLayoutParams) {
            marginBottom = ((MarginLayoutParams) layoutParams).bottomMargin;
        }
        return marginBottom;
    }

    private boolean hasHoneycombApi() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB;
    }
}