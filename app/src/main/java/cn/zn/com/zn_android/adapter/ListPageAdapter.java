package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/3/28 0028.
 */
public class ListPageAdapter extends PagerAdapter {

    public List<View> list_view;
    private String[] tabNames;                              //tab名的列表
    private int[] tabImg = null;                                   //tab名前的图片
    private Context context;

    public ListPageAdapter(Context context, List<View> list_view, String[] tabNames, int[] tabImg) {
        this.list_view = list_view;
        this.tabNames = tabNames;
        this.context = context;
        this.tabImg = tabImg;
    }

    public ListPageAdapter(Context context, List<View> list_view, String[] tabNames) {
        this.list_view = list_view;
        this.tabNames = tabNames;
        this.context = context;
    }


    @Override
    public int getCount() {
        return list_view.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ((ViewPager) container).addView(list_view.get(position), 0);
        return list_view.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView(list_view.get(position));
    }

    /**
     * 此方法是给tablayout中的tab赋值的，就是显示名称,并且给其添加icon的图标
     *
     * @param position
     * @return
     */
    @Override
    public CharSequence getPageTitle(int position) {
        if (tabImg == null) {
            //是只显示文字，不显示图标
            return tabNames[position % tabNames.length];
        }
        Drawable dImage = context.getResources().getDrawable(tabImg[position]);
        dImage.setBounds(0, 0, dImage.getIntrinsicWidth(), dImage.getIntrinsicHeight());
        //这里前面加的空格就是为图片显示
        SpannableString sp = new SpannableString("  " + tabNames[position]);
        ImageSpan imageSpan = new ImageSpan(dImage, ImageSpan.ALIGN_BOTTOM);
        sp.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

}
