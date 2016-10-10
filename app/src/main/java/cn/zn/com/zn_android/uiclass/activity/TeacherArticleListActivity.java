package cn.zn.com.zn_android.uiclass.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ArticleListAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 名家博文列表
 * Created by zjs on 2016/3/25 0025.
 */
public class TeacherArticleListActivity extends BaseActivity implements View.OnClickListener {


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
    @Bind(R.id.xlv_article_list)
    XListView mXlvArticleList;
    private List<ArticleBean> articleList;
    private List<ArticleBean> tempList;
    private ArticleListAdapter articAdapter;
    //每页显示的博文数量
    private final String PCOUNT = "10";
    //页数
    private int page = 1;
    private String tid = "9898";
    private JoDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_teacher_artile);
    }

    public void onEventMainThread(AnyEventType event) {
        tid = (String) event.getObject();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TeacherArticleListActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
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

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("TeacherArticleListActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        //设置标题
        mToolbarTitle.setText(getString(R.string.article));
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        articAdapter.setmContentList(articleList);
    }

    @Override
    protected void initView() {
        dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);

        getArticleData(page);
        articleList = new ArrayList<>();
        articAdapter = new ArticleListAdapter(this, articleList);
        mXlvArticleList.setFooterDividersEnabled(false);
        mXlvArticleList.setLoadMoreEnableShow(false);
        mXlvArticleList.setAdapter(articAdapter);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mXlvArticleList.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page++;
                getArticleData(page);
            }

            @Override
            public void onLoadMore() {
                page++;
                getArticleData(page);

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
//            case :
//                break;
//            case :
//                break;
//            default:
//                break;
        }
    }

    /**
     * 获取老师博文数据
     */
    public void getArticleData(int page) {
        String pageNum = page + "";
        AppObservable.bindActivity(this, _apiManager.getService().getTeacherArticle(tid, pageNum, PCOUNT))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultArticleList, throwable -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                });

    }

    private void resultArticleList(ReturnListValue<ArticleBean> returnValue) {
        if (dialog != null) {
            dialog.dismiss();
        }

        if (returnValue != null && returnValue.getMsg().equals("success")) {
            if (tempList != null) tempList.clear();
            tempList = returnValue.getData();
            articleList.addAll(tempList);
            mXlvArticleList.stopLoadMore();
            mXlvArticleList.stopRefresh();
            if (tempList.size() == 0) {
                ToastUtil.showShort(this, getString(R.string.not_more_data));
            } else {
                mXlvArticleList.setLoadMoreEnableShow(true);
            }
            initData();
        } else {
            ToastUtil.showShort(this, returnValue.getMsg());
        }
    }
}
