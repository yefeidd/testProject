package cn.zn.com.zn_android.uiclass.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.helper.SpfHelper;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.utils.DensityUtil;
import de.greenrobot.event.EventBus;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;

/**
 * Created by zjs on 2016/4/20 0020.
 * explain:
 */
public class GuideActivity extends Activity implements View.OnClickListener {
    private ViewPager mViewPager;
    private LinearLayout llContainer;
    private ImageView ivRedPoint;// 小红点
    private TextView mTvLogin;
    private TextView mTvStart;
    private ArrayList<ImageView> mImageViewList; // imageView集合
    private RnApplication _mApplication = RnApplication.getInstance();
    // 引导页图片id数组
    private int[] mImageIds = new int[]{R.drawable.guide_1,
            R.drawable.guide_2, R.drawable.guide_3};

    // 小红点移动距离
    private int mPointDis;
    private SpfHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 必须在setContentView之前调用
        setContentView(R.layout.activity_guide);

        mViewPager = (ViewPager) findViewById(R.id.vp_guide);
        llContainer = (LinearLayout) findViewById(R.id.ll_container);
        ivRedPoint = (ImageView) findViewById(R.id.iv_red_point);
        mTvStart = (TextView) findViewById(R.id.tv_start);
        mTvLogin = (TextView) findViewById(R.id.tv_login);
        helper = RnApplication.getInstance().getSpfHelper();

        initData();// 先初始化数据
        initEvent();//初始化点击事件
        mViewPager.setAdapter(new GuideAdapter());// 设置数据

        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // 某个页面被选中
                if (position == mImageViewList.size() - 1) {// 最后一个页面显示开始体验的按钮
                    mTvStart.setVisibility(View.VISIBLE);
                    mTvLogin.setVisibility(View.VISIBLE);
                } else {
                    mTvStart.setVisibility(View.INVISIBLE);
                    mTvLogin.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
                // 当页面滑动过程中的回调
                System.out.println("当前位置:" + position + ";移动偏移百分比:"
                        + positionOffset);
                // 更新小红点距离
                int leftMargin = (int) (mPointDis * positionOffset) + position
                        * mPointDis;// 计算小红点当前的左边距
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) ivRedPoint
                        .getLayoutParams();
                params.leftMargin = leftMargin;// 修改左边距

                // 重新设置布局参数
                ivRedPoint.setLayoutParams(params);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // 页面状态发生变化的回调
            }
        });


        // 计算两个圆点的距离
        // 移动距离=第二个圆点left值 - 第一个圆点left值
        // measure->layout(确定位置)->draw(activity的onCreate方法执行结束之后才会走此流程)
        // mPointDis = llContainer.getChildAt(1).getLeft()
        // - llContainer.getChildAt(0).getLeft();
        // System.out.println("圆点距离:" + mPointDis);

        // 监听layout方法结束的事件,位置确定好之后再获取圆点间距
        // 视图树
        ivRedPoint.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {

                    @Override
                    public void onGlobalLayout() {
                        // 移除监听,避免重复回调
                        ivRedPoint.getViewTreeObserver()
                                .removeGlobalOnLayoutListener(this);
                        // ivRedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        // layout方法执行结束的回调
                        mPointDis = llContainer.getChildAt(1).getLeft()
                                - llContainer.getChildAt(0).getLeft();
                        System.out.println("圆点距离:" + mPointDis);
                    }
                });


        //添加到activity集合
        _mApplication.addActivity(this);
    }

    @Override
    protected void onDestroy() {
        _mApplication.removeActivity(this);
        super.onDestroy();
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        mTvLogin.setOnClickListener(this);
        mTvStart.setOnClickListener(this);
    }

    // 初始化数据
    private void initData() {
        mImageViewList = new ArrayList<ImageView>();
        for (int i = 0; i < mImageIds.length; i++) {
            ImageView view = new ImageView(this);
            view.setBackgroundResource(mImageIds[i]);// 通过设置背景,可以让宽高填充布局
            // view.setImageResource(resId)
            mImageViewList.add(view);

            // 初始化小圆点
            ImageView point = new ImageView(this);
            point.setImageResource(R.drawable.shape_point_red);// 设置图片(shape形状)

            // 初始化布局参数, 宽高包裹内容,父控件是谁,就是谁声明的布局参数
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            if (i > 0) {
                // 从第二个点开始设置左边距
                params.leftMargin = DensityUtil.dip2px(this, 10);
            }

            point.setLayoutParams(params);// 设置布局参数

            llContainer.addView(point);// 给容器添加圆点
        }
    }

    /**
     * 点击事件
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                EventBus.getDefault().postSticky(new AnyEventType().setState(true));
                startActivity(new Intent(this, LoginActivity.class));
                helper.saveData("is_first_enter", "1");
                finish();
                break;
            case R.id.tv_start:
                startActivity(new Intent(this, MainActivity.class));
                helper.saveData("is_first_enter", "1");
                finish();
                break;
        }
    }


    class GuideAdapter extends PagerAdapter {

        // item的个数
        @Override
        public int getCount() {
            return mImageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        // 初始化item布局
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView view = mImageViewList.get(position);
            container.addView(view);
            return view;
        }

        // 销毁item
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("GuideActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("GuideActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}

