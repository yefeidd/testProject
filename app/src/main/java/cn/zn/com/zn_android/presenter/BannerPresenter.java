package cn.zn.com.zn_android.presenter;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import cn.zn.com.zn_android.adapter.BannerAdapter;
import cn.zn.com.zn_android.model.BannerModel;
import cn.zn.com.zn_android.model.bean.BannerBean;
import cn.zn.com.zn_android.uiclass.fragment.BaseFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by zjs on 2016/9/10 0010.
 * email: m15267280642@163.com
 * explain: banner的presenter
 */
public class BannerPresenter extends BasePresenter<ViewPager> {

    private Subscription bnSubscription;
    private BaseFragment mFragmert;
    private Context mContext;
    private ArrayList<BannerBean> bannerList = new ArrayList<>();
    private BannerModel bannerModel;
    private BannerAdapter mBannerAdapter;

    public BannerPresenter(BaseFragment mFragmert, Context context, BannerAdapter bannerAdapter) {
        this.mFragmert = mFragmert;
        this.mContext = context;
        this.mBannerAdapter = bannerAdapter;
        bannerModel = new BannerModel(context, this);
    }



    public void setBannerList(List<BannerBean> bannerList) {
        this.bannerList = (ArrayList<BannerBean>) bannerList;
        mBannerAdapter.setBannerList(bannerList);
        mBannerAdapter.notifyDataSetChanged();
        playBanner();
    }

    @Override
    public void attach(ViewPager view) {
        super.attach(view);
        setOnTouchLinstener();
    }


    public void setOnTouchLinstener() {
        mView.setOnTouchListener(touchListener);
    }


    /**
     * 向服务器请求数据
     */
    public void postBannerFromServer(String source) {
        bannerModel.postBannerFromServer(source);
    }

    public void resultData(List<?> dataList) {
        bannerList = (ArrayList<BannerBean>) dataList;
        mBannerAdapter.setBannerList(bannerList);
        mBannerAdapter.notifyDataSetChanged();
        playBanner();
    }


    public View.OnTouchListener touchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    //取消注册
                    if (bnSubscription != null) {
                        bnSubscription.unsubscribe();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    //注册计时器
                    playBanner();
                    break;
            }
            return false;
        }
    };


    public void playBanner() {
        if (mFragmert.isHidden()) return;
        if (bnSubscription != null && !bnSubscription.isUnsubscribed()) {
            bnSubscription.unsubscribe();
        }

        bnSubscription = Observable.timer(4, 4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    int currentItem = mView.getCurrentItem() + 1;
                    currentItem = (currentItem == bannerList.size()) ? 0 : currentItem;
                    mView.setCurrentItem(currentItem);
                }, throwable -> {
                    Log.e(TAG, "playBanner: Banner轮播异常\n", throwable);
                });
    }
}
