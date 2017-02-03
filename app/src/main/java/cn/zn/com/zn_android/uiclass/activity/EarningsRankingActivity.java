package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.DynamicExpertModel;
import cn.zn.com.zn_android.model.FYListModel;
import cn.zn.com.zn_android.model.HotStockListModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.FyRankingBean;
import cn.zn.com.zn_android.model.bean.HotTicBean;
import cn.zn.com.zn_android.model.bean.TrackRankingBean;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/18 0018.
 * email: m15267280642@163.com
 * explain:
 */
public class EarningsRankingActivity extends BaseActivity implements View.OnClickListener,
        XListView.IXListViewListener, DynamicExpertModel.FocusChangeListner {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_fy_list)
    XListView mXlvFyList;
    public ListViewAdapter xlvAdapter;
    private View headFyView;
    private int type = 0;
    private int[] titleNames = new int[]{R.string.fy_ranking, R.string.earnings_ranking, R.string.hot_stock_ranking};
    private List<FYListModel> fyDatas = new ArrayList<>();
    private List<FYListModel> earningsDatas = new ArrayList<>();
    private List<HotStockListModel> hotDatas = new ArrayList<>();
    private List<FyRankingBean> fy_ranking = new ArrayList<>();
    private List<HotTicBean> hot_tic = new ArrayList<>();
    private List<TrackRankingBean> track_ranking = new ArrayList<>();
    private int page = 0;
    private int num = 10;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            mXlvFyList.stopRefresh();
            mXlvFyList.stopLoadMore();
            switch (msg.what) {
                case 0:
                    xlvAdapter.setDataList(fyDatas);
                    break;
                case 1:
                    xlvAdapter.setDataList(earningsDatas);
                    break;
                case 2:
                    xlvAdapter.setDataList(hotDatas);
                    break;
            }
        }
    };
    private int fyi;
    private int tracki;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_earnings_ranking);
    }


    public void onEventMainThread(AnyEventType event) {
        if (null != event.getType()) {
            type = event.getType();
        }
    }

    @Override
    protected void initData() {
//        switch (type) {
//            case 0:
//                setFyDatas(page++);
//                break;
//            case 1:
//                setEarningsDatas(page++);
//                break;
//            case 2:
//                setHotDatas(page++);
//                break;
//        }
//        super.initData();
    }

    private void queryData(int page) {
        switch (type) {
            case 0:
                setFyDatas(page++);
                break;
            case 1:
                setEarningsDatas(page++);
                break;
            case 2:
                setHotDatas(page++);
                break;
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(titleNames[type]);
        switch (type) {
            case 0:
                headFyView = LayoutInflater.from(_Activity).inflate(R.layout.item_contest_big_list_head, new ListView(_Activity), false);
                xlvAdapter = new ListViewAdapter(_Activity, R.layout.item_contest_fy_big_list, fyDatas, "FYListHolder");
            case 1:
                headFyView = LayoutInflater.from(_Activity).inflate(R.layout.item_contest_big_list_head, new ListView(_Activity), false);
                xlvAdapter = new ListViewAdapter(_Activity, R.layout.item_contest_fy_big_list, earningsDatas, "FYListHolder");
                break;
            case 2:
                headFyView = LayoutInflater.from(_Activity).inflate(R.layout.item_contest_big_hot_list_head, new ListView(_Activity), false);
                xlvAdapter = new ListViewAdapter(_Activity, R.layout.item_contest_hot_big_list, hotDatas, "HotStockListHolder");
                break;
        }
        //风云排行榜
        mXlvFyList.setAutoLoadEnable(false);
        mXlvFyList.setPullRefreshEnable(false);
        mXlvFyList.addHeaderView(headFyView);
        mXlvFyList.setAdapter(xlvAdapter);
        fyi = 0;
        tracki = 0;
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mXlvFyList.setXListViewListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;

            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        fyi = 0;
        tracki = 0;
        queryData(0);

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    public void setFyDatas(int p) {
        int count = 0;
        if (p == 0) {
            count = (page + 1) * num;
        } else {
            count = num;
        }

        _apiManager.getService().queryFyRanking(
                RnApplication.getInstance().getUserInfo().getSessionID(), String.valueOf(p), String.valueOf(count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue && null != retValue.getData() && retValue.getData().size() != 0) {
                        fy_ranking = retValue.getData();
                        if (p == 0) {
                            fyDatas.clear();
                        }
                        if (fy_ranking.size() < num) mXlvFyList.setLoadMoreEnableShow(false);
                        for (FyRankingBean bean : fy_ranking) {
                            FYListModel model = new FYListModel(EarningsRankingActivity.this, 0).setBean(bean).setIndex(fyi++);
                            fyDatas.add(model);
                        }
                    }
                    Message msg = Message.obtain();
                    msg.what = 0;
//                    mHandler.handleMessage(msg);
                    mHandler.sendEmptyMessageDelayed(0, 500);


                }, throwable -> {
                    Log.e(TAG, "fyDatas: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryFyRanking(
//                RnApplication.getInstance().getUserInfo().getSessionID(), String.valueOf(p), String.valueOf(count)))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue && null != retValue.getData() && retValue.getData().size() != 0) {
//                        fy_ranking = retValue.getData();
//                        if (p == 0) {
//                            fyDatas.clear();
//                        }
//                        if (fy_ranking.size() < num) mXlvFyList.setLoadMoreEnableShow(false);
//                        for (FyRankingBean bean : fy_ranking) {
//                            FYListModel model = new FYListModel(EarningsRankingActivity.this, 0).setBean(bean).setIndex(fyi++);
//                            fyDatas.add(model);
//                        }
//                    }
//                    Message msg = Message.obtain();
//                    msg.what = 0;
////                    mHandler.handleMessage(msg);
//                    mHandler.sendEmptyMessageDelayed(0, 500);
//
//
//                }, throwable -> {
//                    Log.e(TAG, "fyDatas: ", throwable);
//                });


    }


    public void setEarningsDatas(int p) {
        int count = 0;
        if (p == 0) {
            count = (page + 1) * num;
        } else {
            count = num;
        }
        _apiManager.getService().queryTrankRanking(
                _mApplication.getUserInfo().getSessionID(), String.valueOf(p), String.valueOf(count))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue && null != retValue.getData() && retValue.getData().size() != 0) {
                        track_ranking = retValue.getData();
                        if (track_ranking.size() < num) mXlvFyList.setLoadMoreEnableShow(false);

                        if (p == 0) {
                            earningsDatas.clear();
                        }
                        for (TrackRankingBean bean : track_ranking) {
                            FYListModel model = new FYListModel(EarningsRankingActivity.this, 1).setBean(bean).setIndex(tracki++);
                            model.setmListner(this);
                            earningsDatas.add(model);
                        }
                    }
                    Message msg = Message.obtain();
                    msg.what = 1;
                    mHandler.handleMessage(msg);

                }, throwable -> {
                    Log.e(TAG, "earningsDatas: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryTrankRanking(
//                _mApplication.getUserInfo().getSessionID(), String.valueOf(p), String.valueOf(count)))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue && null != retValue.getData() && retValue.getData().size() != 0) {
//                        track_ranking = retValue.getData();
//                        if (track_ranking.size() < num) mXlvFyList.setLoadMoreEnableShow(false);
//
//                        if (p == 0) {
//                            earningsDatas.clear();
//                        }
//                        for (TrackRankingBean bean : track_ranking) {
//                            FYListModel model = new FYListModel(EarningsRankingActivity.this, 1).setBean(bean).setIndex(tracki++);
//                            model.setmListner(this);
//                            earningsDatas.add(model);
//                        }
//                    }
//                    Message msg = Message.obtain();
//                    msg.what = 1;
//                    mHandler.handleMessage(msg);
//
//                }, throwable -> {
//                    Log.e(TAG, "earningsDatas: ", throwable);
//                });


    }


    public void setHotDatas(int page) {
        if (page == 0) {
            _apiManager.getService().queryHotTic("")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(retValue -> {
                        if (null != retValue && null != retValue.getData() && retValue.getData().size() != 0) {
                            hot_tic = retValue.getData();
                            if (0 == page) {
                                hotDatas.clear();
                            }
                            if (hot_tic.size() < num) mXlvFyList.setLoadMoreEnableShow(false);
                            int start = page * num < hot_tic.size() ? page * num : hot_tic.size();
                            int end = (page + 1) * num < hot_tic.size() ? (page + 1) * num : hot_tic.size();
                            for (int i = start; i < end; i++) {
                                HotTicBean bean = hot_tic.get(i);
                                HotStockListModel model = new HotStockListModel(this, bean).setIndex(i);
                                hotDatas.add(model);
                            }
                        }
                        Message msg = Message.obtain();
                        msg.what = 2;
                        mHandler.handleMessage(msg);
                    }, throwable -> {
                        Log.e(TAG, "hotDatas: ", throwable);
                    });

//            AppObservable.bindActivity(this, _apiManager.getService().queryHotTic(""))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(retValue -> {
//                        if (null != retValue && null != retValue.getData() && retValue.getData().size() != 0) {
//                            hot_tic = retValue.getData();
//                            if (0 == page) {
//                                hotDatas.clear();
//                            }
//                            if (hot_tic.size() < num) mXlvFyList.setLoadMoreEnableShow(false);
//                            int start = page * num < hot_tic.size() ? page * num : hot_tic.size();
//                            int end = (page + 1) * num < hot_tic.size() ? (page + 1) * num : hot_tic.size();
//                            for (int i = start; i < end; i++) {
//                                HotTicBean bean = hot_tic.get(i);
//                                HotStockListModel model = new HotStockListModel(this, bean).setIndex(i);
//                                hotDatas.add(model);
//                            }
//                        }
//                        Message msg = Message.obtain();
//                        msg.what = 2;
//                        mHandler.handleMessage(msg);
//                    }, throwable -> {
//                        Log.e(TAG, "hotDatas: ", throwable);
//                    });
        } else {
            int start = page * num < hot_tic.size() ? page * num : hot_tic.size();
            int end = (page + 1) * num < hot_tic.size() ? (page + 1) * num : hot_tic.size();
            for (int i = start; i < end; i++) {
                HotTicBean bean = hot_tic.get(i);
                HotStockListModel model = new HotStockListModel(this, bean).setIndex(i);
                hotDatas.add(model);
            }
            Message msg = Message.obtain();
            msg.what = 2;
            mHandler.handleMessage(msg);
        }
    }

    /**
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRefresh() {
    }

    @Override
    public void onLoadMore() {
//        initData();
        page ++;
        queryData(page);
    }

    @Override
    public void focusChange(boolean focus, String userId, int position) {
        if (focus) {
            attentionOther(userId, position);
        } else {
            unsetConcern(userId, position);
        }
    }

    public void attentionOther(String userId, int pos) {
        ApiManager.getInstance().getService().attentionOther(RnApplication.getInstance().getUserInfo().getSessionID(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(this, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            earningsDatas.get(pos).getTrackBean().setAttentionType(1);
                            xlvAdapter.setDataList(earningsDatas);
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "attentionOther: ", throwable);
                });

//        AppObservable.bindActivity(this, ApiManager.getInstance().getService().attentionOther(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(this, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            earningsDatas.get(pos).getTrackBean().setAttentionType(1);
//                            xlvAdapter.setDataList(earningsDatas);
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "attentionOther: ", throwable);
//                });
    }

    public void unsetConcern(String userId, int pos) {
        ApiManager.getInstance().getService().unsetConcern(
                RnApplication.getInstance().getUserInfo().getSessionID(), userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (null != retValue) {
                        ToastUtil.showShort(this, retValue.getData().getMessage());
                        if (retValue.getData().getMessage().contains("成功")) {
                            earningsDatas.get(pos).getTrackBean().setAttentionType(0);
                            xlvAdapter.setDataList(earningsDatas);
                        }
                    }
                }, throwable -> {
                    Log.e(TAG, "unsetConcern: ", throwable);
                });

//        AppObservable.bindActivity(this, ApiManager.getInstance().getService().unsetConcern(
//                RnApplication.getInstance().getUserInfo().getSessionID(), userId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (null != retValue) {
//                        ToastUtil.showShort(this, retValue.getData().getMessage());
//                        if (retValue.getData().getMessage().contains("成功")) {
//                            earningsDatas.get(pos).getTrackBean().setAttentionType(0);
//                            xlvAdapter.setDataList(earningsDatas);
//                        }
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "unsetConcern: ", throwable);
//                });
    }

}
