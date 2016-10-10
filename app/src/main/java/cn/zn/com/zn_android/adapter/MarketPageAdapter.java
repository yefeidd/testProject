package cn.zn.com.zn_android.adapter;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import cn.zn.com.zn_android.uiclass.page.BasePage;

import java.util.List;

/**
 * Created by zjs on 2016/6/27 0027.
 * email: m15267280642@163.com
 * explain:
 */
public class MarketPageAdapter extends PagerAdapter {

    private List<BasePage> pageList;
    private Activity mAcitivity;
    private String[] tabNames;

    public void setTabNames(String[] tabNames) {
        this.tabNames = tabNames;
    }


    public MarketPageAdapter(Activity activity) {
        this.mAcitivity = activity;
    }

    @Override
    public int getCount() {
        return pageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    /**
     * 设置数据
     *
     * @param pageList
     */
    public void setPageList(List<BasePage> pageList) {
        this.pageList = pageList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(pageList.get(position).mRootView, 0);
        return pageList.get(position).mRootView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(pageList.get(position).mRootView);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNames[position % tabNames.length];
    }
}
