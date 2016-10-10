package cn.zn.com.zn_android.presenter;

import android.app.Activity;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.FYListModel;
import cn.zn.com.zn_android.model.HotAritcleContestModel;
import cn.zn.com.zn_android.model.HotStockGodModel;
import cn.zn.com.zn_android.model.HotStockListModel;
import cn.zn.com.zn_android.model.TeacherExplainModel;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.model.bean.BannerBean;
import cn.zn.com.zn_android.model.bean.ContestHomeListBean;
import cn.zn.com.zn_android.model.bean.DynamicExpertBean;
import cn.zn.com.zn_android.model.bean.FyRankingBean;
import cn.zn.com.zn_android.model.bean.HotTicBean;
import cn.zn.com.zn_android.model.bean.TrackRankingBean;
import cn.zn.com.zn_android.model.bean.VideoBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.viewfeatures.BaseMvpView;

import java.util.ArrayList;
import java.util.List;

import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/24 0024.
 * email: m15267280642@163.com
 * explain:
 */

public class ContestHomePresenter extends BasePresenter<BaseMvpView> {


    private ApiManager _apiManager;
    private Activity mActivity;
    private RnApplication _mApplication;
    private String total_money;
    private List<ArticleBean> art_list;
    private List<BannerBean> banner;
    private List<FyRankingBean> fy_ranking;
    private List<HotTicBean> hot_tic;
    private List<TrackRankingBean> track_ranking;
    private List<DynamicExpertBean> hot_warren;
    private List<VideoBean> videos;
    private List<HotAritcleContestModel> hotAritcleContestModels;
    private ArrayList<HotStockGodModel> hotStockGodModels;
    private List<FYListModel> fyDatas;
    private List<FYListModel> earningsDatas;
    private List<HotStockListModel> hotDatas;
    private List<TeacherExplainModel> teacherExplainDatas;


    public ContestHomePresenter(Activity mActivity) {
        this.mActivity = mActivity;
        _apiManager = ApiManager.getInstance();
        _mApplication = RnApplication.getInstance();
    }

    @Override
    public void attach(BaseMvpView view) {
        super.attach(view);
    }


    /**
     * 首页请求数据
     */
    public void queryContestDataList() {
        AppObservable.bindActivity(mActivity, _apiManager.getService().queryContestHomeList("", _mApplication.getUserInfo().getSessionID()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::disposeData, throwable -> {
                    mView.onError(0, throwable);
                });
    }


    private void disposeData(ReturnValue<ContestHomeListBean> ReturnValue) {
        if (null != ReturnValue) {
            ContestHomeListBean bean = ReturnValue.getData();
            if (null != bean.getTotal_money() && !"".equals(bean.getTotal_money())) {
                total_money = bean.getTotal_money();
            } else {
                total_money = "";
            }
            art_list = bean.getArt_list();
            banner = bean.getBanner();
            fy_ranking = bean.getFy_ranking();
            hot_tic = bean.getHot_tic();
            hot_warren = bean.getHot_warren();
            track_ranking = bean.getTrack_ranking();
            videos = bean.getVideo();
            mView.onSuccess(0, "");
        }
    }


    public List<HotAritcleContestModel> getArtListModles() {
        hotAritcleContestModels = new ArrayList<>();
        int index = 0;
        for (ArticleBean bean : art_list) {
            HotAritcleContestModel model = new HotAritcleContestModel(mActivity,bean).setOrder(index++);
            hotAritcleContestModels.add(model);
        }
        return hotAritcleContestModels;
    }


    public List<HotStockGodModel> getHotStockGodModles() {
        hotStockGodModels = new ArrayList<>();
        for (DynamicExpertBean bean : hot_warren) {
            HotStockGodModel model = new HotStockGodModel(mActivity,bean);
            hotStockGodModels.add(model);
        }
        return hotStockGodModels;
    }


    public List<FYListModel> getFyDatas() {
        fyDatas = new ArrayList<>();
        int i = 0;
        for (FyRankingBean bean : fy_ranking) {
            FYListModel model = new FYListModel(mActivity, 0).setBean(bean).setIndex(i++);
            fyDatas.add(model);
        }
        return fyDatas;
    }

    public List<FYListModel> getEarningsDatas() {
        earningsDatas = new ArrayList<>();
        int i = 0;
        for (TrackRankingBean bean : track_ranking) {
            FYListModel model = new FYListModel(mActivity, 1).setBean(bean).setIndex(i++);
            earningsDatas.add(model);
        }
        return earningsDatas;
    }


    public List<HotStockListModel> getHotDatas() {
        hotDatas = new ArrayList<>();
        int i = 0;
        for (HotTicBean bean : hot_tic) {
            HotStockListModel model = new HotStockListModel(mActivity,bean).setIndex(i++);
            hotDatas.add(model);
        }
        return hotDatas;
    }


    public List<TeacherExplainModel> getVedioDatas() {
        teacherExplainDatas = new ArrayList<>();
        int i = 0;
        for (VideoBean bean : videos) {
            teacherExplainDatas.add(new TeacherExplainModel(mActivity, bean));
        }
        return teacherExplainDatas;
    }

    public String getTotal_money() {
        return total_money;
    }

    public List<BannerBean> getBanner() {
        return banner;
    }
}
