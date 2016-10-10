package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

/**
 * Created by zjs on 2016/5/13 0013.
 * explain:
 */
public class CusTabLayout extends TabLayout {

    public CusTabLayout(Context context) {
        this(context, null);
    }

    public CusTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CusTabLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setupWithViewPager(@Nullable ViewPager viewPager) {
        super.setupWithViewPager(viewPager);
    }
}
