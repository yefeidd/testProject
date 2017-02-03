package cn.zn.com.zn_android.model;

import android.app.Activity;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.BannerBean;
import cn.zn.com.zn_android.model.bean.BaseBannerBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.presenter.BannerPresenter;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/10 0010.
 * email: m15267280642@163.com
 * explain:
 */
public class BannerModel {
    private Activity mActivity;
    private ApiManager _apiManager;
    private List<BaseBannerBean> bannerList = new ArrayList<>();
    private BannerPresenter presenter;

    public BannerModel(Context context, BannerPresenter presenter) {
        this.mActivity = (Activity) context;
        this.presenter = presenter;
        _apiManager = ApiManager.getInstance();
    }


    public void postBannerFromServer(String source) {
//        if ("MainBanner".equals(source)) {
        _apiManager.getService().getHomeBanner("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultBanner, throwable -> {
                });
//        AppObservable.bindActivity(mActivity, _apiManager.getService().getHomeBanner("")).
//                    subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::resultBanner, throwable -> {
//                    });
//        } else if ("ContestBanner".equals(source)) {
//            presenter.resultData(bannerList);
//        }
    }

    /**
     * 请回返回的结果
     */
    private void resultBanner(ReturnListValue<BannerBean> returnValue) {

        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            if (returnValue.getData() != null) {
                presenter.resultData(returnValue.getData());
            }
        } else {
            ToastUtil.showShort(mActivity, returnValue.getMsg());
        }
    }
}
