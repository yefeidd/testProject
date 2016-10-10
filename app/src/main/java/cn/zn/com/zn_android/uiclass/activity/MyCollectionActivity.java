package cn.zn.com.zn_android.uiclass.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.CollectVideoArtBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.fragment.ArticlesFragment;
import cn.zn.com.zn_android.uiclass.fragment.CollectionArtFragment;
import cn.zn.com.zn_android.uiclass.fragment.CollectionVideoFragment;
import cn.zn.com.zn_android.uiclass.fragment.VideosFragment;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MyCollectionActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tl_collect)
    TabLayout mTlCollect;
    @Bind(R.id.vp_collect)
    ViewPager mVpCollect;

    private boolean isStockSchool = false;

    private CollectionVideoFragment videoFragment = CollectionVideoFragment.newInstance();
    private CollectionArtFragment artFragment = CollectionArtFragment.newInstance();

    private VideosFragment videosFragment = VideosFragment.newInstance("stock", "");
    private ArticlesFragment articlesFragment = ArticlesFragment.newInstance("", "");
    private JoDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_my_collection);
        ButterKnife.bind(this);
    }

    @Override
    protected void initView() {
        dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);

        if (!isStockSchool) {
            mToolbarTitle.setText(getString(R.string.my_collect));
            queryCollectList();
        } else {
            mToolbarTitle.setText(getString(R.string.school));
            queryStockSchool();
        }

        setupViewPager();
        mTlCollect.setupWithViewPager(mVpCollect);
        mTlCollect.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_bar_color));
    }

    @Override
    protected void initEvent() {

    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getState() != null) {
            isStockSchool = event.getState();
        }
    }

    private void setupViewPager() {
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
        if (!isStockSchool) {
            adapter.addFragment(videoFragment, "视频");
            adapter.addFragment(artFragment, "文章");
        } else {
            adapter.addFragment(videosFragment, "热门视频");
            adapter.addFragment(articlesFragment, "经典博文");
        }
        mVpCollect.setAdapter(adapter);
    }

    private void queryCollectList() {
        AppObservable.bindActivity(this, _apiManager.getService().queryCollectList(_mApplication.getUserInfo().getSessionID(), ""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultCollect, throwable -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.e(TAG, "queryCollectList: ", throwable);
                });

    }

    private void resultCollect(ReturnValue<CollectVideoArtBean> returnValue) {
        if (dialog != null) {
            dialog.dismiss();
        }
        Log.i(TAG, "resultCollect: ");
        if (returnValue.getMsg().equals(Constants.ERROR)) {
            Log.e(TAG, "resultCollect: " + returnValue.getMsg());
            return;
        }
        artFragment.setArtList(returnValue.getData().getArt());
        videoFragment.setVideoList(returnValue.getData().getVideo());
    }

    private void queryStockSchool() {
        AppObservable.bindActivity(this, _apiManager.getService().queryStockSchool(1, 10))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultStockSchool, throwable -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.e(TAG, "queryStockSchool: ", throwable);
                });
    }

    private void resultStockSchool(ReturnValue<CollectVideoArtBean> returnValue) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (returnValue != null && !returnValue.getMsg().equals(Constants.ERROR) && returnValue.getData() != null) {
//            articleAdapter = new ArticleListAdapter(getActivity(), returnValue.getData().getArt());
            articlesFragment.setData(returnValue.getData().getArt());
            videosFragment.setVideoList(returnValue.getData().getVideo());
        } else {
            ToastUtil.showShort(_mApplication, returnValue.getMsg());
        }
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }
}
