package cn.zn.com.zn_android.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.BannerBean;
import cn.zn.com.zn_android.model.bean.BaseBannerBean;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.SignUpActivity;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.umeng.socialize.utils.Log.TAG;

/**
 * Created by zjs on 2016/3/31 0031.
 * explain:
 */
public class BannerAdapter extends PagerAdapter {
    private List<?> BannerList;
    private BaseBannerBean banner;
    private pageOnlickListener mPageOnlickListener;
    private String source;


    public void setBannerList(List<?> bannerList) {
        BannerList = bannerList;
    }

    private Context mContext;

    public BannerAdapter(Context context, List<?> BannerList, pageOnlickListener mPageOnlickListener, String source) {
        this.BannerList = BannerList;
        this.mContext = context;
        this.mPageOnlickListener = mPageOnlickListener;
        this.source = source;
    }

    @Override
    public int getCount() {
        if (null == BannerList) return 0;
        else
            return BannerList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        banner = (BaseBannerBean) BannerList.get(position);
        String url = banner.getUrl();
        SimpleDraweeView view = new SimpleDraweeView(mContext);
        view.setImageURI(Uri.parse(url));
        view.setScaleType(ImageView.ScaleType.CENTER_CROP);
        container.addView(view);
        if ("MainFragment".equals(source)) {
            if (0 == position) {
                view.setOnClickListener(v -> {
                    if (null != mPageOnlickListener) {
                        regVir(position);
                    }
                });
            }
        } else {
            if (0 == position) {
                view.setOnClickListener(v -> {
                    EventBus.getDefault().postSticky(new AnyEventType(((BannerBean) BannerList.get(position)).getLink()));
                    mContext.startActivity(new Intent(mContext, SignUpActivity.class));
                });
            }
        }


        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    /**
     * viewpager每个页面点击的接口
     */
    public interface pageOnlickListener {
        void clickPage(int position);
    }

    /**
     * 模拟大赛用户注册
     *
     * @param position
     */
    private void regVir(int position) {
        if (RnApplication.getInstance().getUserInfo().getIsLogin() == 0) {
            mContext.startActivity(new Intent(mContext, LoginActivity.class));
        }
        AppObservable.bindActivity((Activity) mContext, ApiManager.getInstance().getService().virReg(RnApplication.getInstance().getUserInfo().getSessionID(), ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (retValue.getCode().equals("2000")) {
                        mPageOnlickListener.clickPage(position);
                    }
                }, throwable -> {
                    Log.e(TAG, "queryUserSign: ", throwable);
                });
    }


}
