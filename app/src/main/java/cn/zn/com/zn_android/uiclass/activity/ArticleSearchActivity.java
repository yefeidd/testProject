package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.qiniu.android.utils.StringUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ArticleListAdapter;
import cn.zn.com.zn_android.adapter.HostLiveAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/4/8 0008.
 * explain:
 */
public class ArticleSearchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_history)
    ImageButton mIbHistory;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.et_keywords)
    EditText mEtKeywords;
    @Bind(R.id.iv_article_search)
    ImageView mIvArticleSearch;
    @Bind(R.id.xlv_search_list)
    XListView mXlvSearchList;
    private final String NEWARTICLE = "2";
    private List<ArticleBean> newList;  //最新文章列表
    private List<ArticleBean> tempList;
    private ArticleListAdapter newAdapter;
    private String kwords = "";
    private final String PAGECOUNT = "10";
    private int page = 0;
    private String origin = Constants.ARTICLE;

    private HostLiveAdapter mHotLiveAdapter;
    private List<HotLiveBean> hotLiveList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_article_search);
    }

    public void onEventMainThread(AnyEventType event) {
        origin = (String) event.getObject();
    }

    @Override
    protected void initView() {
        newList = new ArrayList<>();
        tempList = new ArrayList<>();
        newAdapter = new ArticleListAdapter(this, newList);
        mXlvSearchList.setFooterDividersEnabled(false);
        mXlvSearchList.setAdapter(newAdapter);
        if (!origin.equals(Constants.VIDEO)) {
            mXlvSearchList.setLoadMoreEnable(false);
            mXlvSearchList.setPullLoadEnable(false);
            mXlvSearchList.setPullRefreshEnable(false);
            mEtKeywords.setHint(R.string.input_keyword);
        } else {
            mXlvSearchList.setLoadMoreEnable(false);
            mXlvSearchList.setPullLoadEnable(false);
            mXlvSearchList.setPullRefreshEnable(false);
            mEtKeywords.setHint(R.string.input_name_num);
        }
        mXlvSearchList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page++;
                initData();
            }

            @Override
            public void onLoadMore() {
                page++;
                initData();
            }
        });
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mIvArticleSearch.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("ArticleSearchActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        if (!origin.equals(Constants.VIDEO)) {
//            initData();
            if (origin.equals(Constants.ARTICLE)) {
                mToolbarTitle.setText(getString(R.string.article));
            } else {
                mToolbarTitle.setText(getString(R.string.news));
            }
        } else {
            mToolbarTitle.setText(getString(R.string.search));
            mEtKeywords.setHint(getString(R.string.input_name_num));
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ArticleSearchActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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

    /**
     * 更新数据
     */
    protected void initData() {
        getNewArticleData(kwords, page, PAGECOUNT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.iv_article_search:
                if (!StringUtils.isNullOrEmpty(mEtKeywords.getText().toString())) {
                    newList.clear();
                    kwords = mEtKeywords.getText().toString();
                    initData();
                } else {
                    newList.clear();
                    kwords = "";
                    initData();
                }
                break;
//            case :
//                break;
//            default:
//                break;
        }
    }

    /**
     * 获取最新博文列表
     *
     * @param kwords
     * @param page
     * @param pcount
     */
    private void getNewArticleData(String kwords, int page, String pcount) {
        int pageNum = 0;
        if (page == 0) pageNum = 1;
        else pageNum = page;
        if (!origin.equals(Constants.VIDEO)) {
            getNewArticleData(kwords, origin, pageNum + "", pcount);
        } else {
            mHotLiveAdapter = new HostLiveAdapter(hotLiveList, this);
            mXlvSearchList.setAdapter(mHotLiveAdapter);
            postHotLiveRequest();
        }
    }

    /**
     * 获取最新数据搜索结果
     *
     * @return
     */
    private void getNewArticleData(String kwords, String order, String page, String pcount) {
        _apiManager.getService().getArticleList(kwords, origin, order, page, pcount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultNewArticle, Throwable -> {
                    mXlvSearchList.stopRefresh();
                    mXlvSearchList.stopLoadMore();
                    Throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });


//        AppObservable.bindActivity(this, _apiManager.getService().getArticleList(kwords, origin, order, page, pcount))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultNewArticle, Throwable -> {
//                    mXlvSearchList.stopRefresh();
//                    mXlvSearchList.stopLoadMore();
//                    Throwable.printStackTrace();
//                    ToastUtil.showShort(this, getString(R.string.no_net));
//                });
    }

    private void resultNewArticle(ReturnListValue<ArticleBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals("success")) {

            if (!tempList.isEmpty()) {
                tempList.clear();
            }
            tempList = returnValue.getData();
            newList.addAll(tempList);
            if (newList.isEmpty())
                ToastUtil.showShort(this, getString(R.string.no_data_about_keyword));
            newAdapter.setmContentList(newList);
            mXlvSearchList.stopRefresh();
            mXlvSearchList.stopLoadMore();
            UIUtil.hidekeyBoard(mEtKeywords);
        }
    }

    /**
     * 发送请求热门直播列表请求
     */
    private void postHotLiveRequest() {
        _apiManager.getService().getHotLiveList(kwords)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultLiveList, throwable -> {
                    Log.e(TAG, "." +
                            "postHotLiveRequest: 异常");
                });
//        AppObservable.bindActivity(this, _apiManager.getService().getHotLiveList(kwords)).
//                subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultLiveList, throwable -> {
//                    Log.e(TAG, "." +
//                            "postHotLiveRequest: 异常");
//                });
    }

    private void resultLiveList(ReturnListValue<HotLiveBean> returnValue) {
        hotLiveList = new ArrayList<>();
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            hotLiveList = returnValue.getData();
            if (hotLiveList != null) {
                mHotLiveAdapter.setHostLiveList(hotLiveList);
                UIUtil.hidekeyBoard(mEtKeywords);
            } else {
                ToastUtil.showShort(_mApplication, returnValue.getMsg());
            }
        }
    }

}
