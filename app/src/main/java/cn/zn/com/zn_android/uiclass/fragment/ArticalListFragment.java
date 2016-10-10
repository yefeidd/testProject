package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ArticleListAdapter;
import cn.zn.com.zn_android.adapter.ListPageAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.activity.ArticleSearchActivity;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.page.ListPage;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/9/6 0006.
 */
public class ArticalListFragment extends BaseFragment {
    private final String HOSTARTICLE = "1";
    private final String NEWARTICLE = "2";
    private final int PAGECOUNT = 10;
    private int hotpage = 1;
    private int newpage = 1;

    private String type = Constants.NEWS;

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.tab_title)
    TabLayout mTabTitle;
    @Bind(R.id.vp_article_list)
    ViewPager mVpArticleList;

    private String[] tab_names = null;
    private List<View> listViews;
    private ListPage newPager;
    private ListPage hotPager;
    private List<ArticleBean> hotList;  //热门文章列表
    private List<ArticleBean> newList;  //最新文章列表
    private List<ArticleBean> tempList;
    private ArticleListAdapter newAdapter;
    private ArticleListAdapter hotAdapter;
    private JoDialog dialog = null;
//
//    public ArticalListFragment setType(String type) {
//        this.type = type;
//        if (type.equals(Constants.NEWS)) {
//            mIvLeftmenu.setVisibility(View.GONE);
//        } else {
//            mIvLeftmenu.setVisibility(View.VISIBLE);
//        }
//        return this;
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (null != getArguments()) {
            type = getArguments().getString(Constants.FROM);
        }
    }

    @Override
    protected void initView(View view) {
        if (type.equals(Constants.NEWS)) {
            mIvLeftmenu.setVisibility(View.GONE);
        } else {
            mIvLeftmenu.setVisibility(View.VISIBLE);
        }

        mIbSearch.setVisibility(View.VISIBLE);
        mIbSearch.setImageResource(R.drawable.iv_search);

        tab_names = UIUtil.getStringArray(R.array.Article_list_tab_names);
        hotList = new ArrayList<>();
        newList = new ArrayList<>();
        tempList = new ArrayList<>();
        newAdapter = new ArticleListAdapter(getActivity(), newList);
        newAdapter.setType(type);
        hotAdapter = new ArticleListAdapter(getActivity(), hotList);
        hotAdapter.setType(type);
        //初始化两个页面
        newPager = new ListPage(getActivity());
        newPager.setAdapter(newAdapter);
        newPager.initData();
        newPager.setPullRefreshEnable(true);
        newPager.setPullLoadEnable(true);
        newPager.setLoadMoreEnable(true);
        hotPager = new ListPage(getActivity());
        hotPager.setAdapter(hotAdapter);
        hotPager.initData();
        hotPager.setPullRefreshEnable(true);
        hotPager.setPullLoadEnable(true);
        hotPager.setLoadMoreEnable(true);
        viewChanage();
        //初始化底部加载更多不可见
        newPager.setBottonShowEnable(false);
        hotPager.setBottonShowEnable(false);

        if (dialog == null) {
            dialog = new JoDialog.Builder(getActivity())
                    .setViewRes(R.layout.layout_loading)
                    .setBgRes(Color.TRANSPARENT)
                    .show(false);
            getHotArticleData("", hotpage, hotpage * PAGECOUNT);
            getNewArticleData("", newpage, newpage * PAGECOUNT);
        }
    }

    @Override
    protected void initEvent() {
        //设置返回键功能
        mIvLeftmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        mIbSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(type));
                startActivity(new Intent(getActivity(), ArticleSearchActivity.class));
            }
        });

        //设置最新下拉刷新
        newPager.setXListViewListener(new ListPage.IXListViewListener() {
            @Override
            public void onRefresh() {
                newpage = 1;
                getNewArticleData("", 1, newpage * PAGECOUNT);
            }

            @Override
            public void onLoadMore() {
                newpage++;
                getNewArticleData("", 1, newpage * PAGECOUNT);
            }
        });
        //设置最热下拉刷新
        hotPager.setXListViewListener(new ListPage.IXListViewListener() {
            @Override
            public void onRefresh() {
                hotpage = 1;
                getHotArticleData("", 1, hotpage * PAGECOUNT);
            }

            @Override
            public void onLoadMore() {
                hotpage++;
                getHotArticleData("", 1, hotpage * PAGECOUNT);
            }
        });
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_artical_list);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();

        if (type.equals(Constants.ARTICLE)) {
            mToolbarTitle.setText(getString(R.string.article));
        } else {
            mToolbarTitle.setText(R.string.news);
        }

        if (hotList.size() == 0) {
            if (dialog == null) {
                dialog = new JoDialog.Builder(getActivity())
                        .setViewRes(R.layout.layout_loading)
                        .setBgRes(Color.TRANSPARENT)
                        .show(false);
            }
        }
        getHotArticleData("", 1, hotpage * PAGECOUNT);
        if (newList.size() == 0) {
            if (dialog == null) {
                dialog = new JoDialog.Builder(getActivity())
                        .setViewRes(R.layout.layout_loading)
                        .setBgRes(Color.TRANSPARENT)
                        .show(false);
            }
        }
        getNewArticleData("", 1, newpage * PAGECOUNT);
    }

    private void viewChanage() {
        listViews = new ArrayList<>();
        listViews.add(newPager.mRootView);
        listViews.add(hotPager.mRootView);


        //设置TabLayout的模式,这里主要是用来显示tab展示的情况的
        //TabLayout.MODE_FIXED          各tab平分整个工具栏,如果不设置，则默认就是这个值
        //TabLayout.MODE_SCROLLABLE     适用于多tab的，也就是有滚动条的，一行显示不下这些tab可以用这个
        //                              当然了，你要是想做点特别的，像知乎里就使用的这种效果
        mTabTitle.setTabMode(TabLayout.MODE_FIXED);

        //设置tablayout距离上下左右的距离
        //tab_title.setPadding(20,20,20,20);

        //为TabLayout添加tab名称
        mTabTitle.addTab(mTabTitle.newTab().setText(tab_names[0]), true);
        mTabTitle.addTab(mTabTitle.newTab().setText(tab_names[1]));


        ListPageAdapter pAdapter = new ListPageAdapter(getActivity(), listViews, tab_names);
        mVpArticleList.setAdapter(pAdapter);

        //将tabLayout与viewpager连起来
        mTabTitle.setupWithViewPager(mVpArticleList);
    }

    /**
     * 获取最热博文列表
     *
     * @param kwords
     * @param page
     * @param pcount
     */
    private void getHotArticleData(String kwords, int page, int pcount) {
        getHotArticleData(kwords, HOSTARTICLE, page + "", pcount + "");
    }


    /**
     * 获取最新博文列表
     *
     * @param kwords
     * @param page
     * @param pcount
     */
    private void getNewArticleData(String kwords, int page, int pcount) {
        getNewArticleData(kwords, NEWARTICLE, page + "", pcount + "");
    }


    /**
     * 获取最热的博文数据
     *
     * @return
     */
    private void getHotArticleData(String kwords, String order, String page, String pcount) {
        AppObservable.bindFragment(this, _apiManager.getService().getArticleList(kwords, type, order, page, pcount))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultHotArticle, Throwable -> {
                    hotPager.stopRefresh();
                    hotPager.stopLoadMore();
                    Throwable.printStackTrace();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
    }

    private void resultHotArticle(ReturnListValue<ArticleBean> returnValue) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (returnValue != null && returnValue.getMsg().equals("success")) {

            if (!tempList.isEmpty()) {
                tempList.clear();
            }
            tempList = returnValue.getData();

            if (tempList.size() == 0 || tempList.size() < Integer.valueOf(PAGECOUNT)) {
                hotPager.setBottonShowEnable(false);
            } else {
                hotPager.setBottonShowEnable(true);
            }
            hotList.clear();
            hotList.addAll(tempList);
            hotAdapter.setmContentList(hotList);
            hotPager.stopRefresh();
            hotPager.stopLoadMore();
        }
    }


    /**
     * 获取最新的博文数据
     *
     * @return
     */
    private void getNewArticleData(String kwords, String order, String page, String pcount) {
        AppObservable.bindFragment(this, _apiManager.getService().getArticleList(kwords, type, order, page, pcount))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultNewArticle, Throwable -> {
                    SystemClock.sleep(2000);
                    newPager.stopRefresh();
                    newPager.stopLoadMore();
                    Throwable.printStackTrace();
                    ToastUtil.showShort(getActivity(), getString(R.string.no_net));
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                });
    }

    private void resultNewArticle(ReturnListValue<ArticleBean> returnValue) {
        if (dialog != null) {
            dialog.dismiss();
        }
        if (returnValue != null && returnValue.getMsg().equals("success")) {

            if (!tempList.isEmpty()) {
                tempList.clear();
            }
            tempList = returnValue.getData();
            if (tempList.size() == 0 || tempList.size() < Integer.valueOf(PAGECOUNT)) {
                newPager.setBottonShowEnable(false);
            } else {
                newPager.setBottonShowEnable(true);
            }
            newList.clear();
            newList.addAll(tempList);
            newAdapter.setmContentList(newList);
            newPager.stopRefresh();
            newPager.stopLoadMore();
        }
    }

}
