package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.NewsAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.StockNewsBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/7/15 0015.
 */
public class NewsActivity extends BaseActivity implements XListView.IXListViewListener {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_news)
    XListView mXlvNews;

    private String ticCode;
    private String title;
    private NewsAdapter newsAdapter;
    private List<StockNewsBean> data = new ArrayList<>();
    private int page = 1;
    private boolean isHs = true;
    private JoDialog dialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_news);

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(AnyEventType event) {
        if (null != event) {
            ticCode = event.getTid();
            if (event.getTid().length() == 6) {
                isHs = true;
            } else {
                isHs = false;
            }
            if (event.getState()) {
                title = getString(R.string.chart_news);
            } else {
                title = getString(R.string.notice);
            }
//            data.clear();
//            List<StockNewsBean> list = (List<StockNewsBean>) event.getObject();
//            data.addAll(list);
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(title);

        newsAdapter = new NewsAdapter(this, R.layout.item_stock_news, data);
        mXlvNews.setAdapter(newsAdapter);
        mXlvNews.setXListViewListener(this);

        String type = title.equals(getString(R.string.chart_news)) ? "1" : "2";
        if (dialog == null) {
            dialog = new JoDialog.Builder(this)
                    .setViewRes(R.layout.layout_loading)
                    .setBgRes(Color.TRANSPARENT)
                    .show(false);
        }
        if (isHs) {
            queryHs(type);
        } else {
            queryHk(type);
        }
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(v -> {
            finish();
        });

        mXlvNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0 || data.size() == 0) {
                    ToastUtil.showShort(getApplicationContext(), getString(R.string.not_more_data));
                    return;
                }
                if (data.size() < position) {
                    ToastUtil.showShort(getApplicationContext(), getString(R.string.not_more_data));
                    return;
                }
                EventBus.getDefault().postSticky(new AnyEventType(data.get(position-1).getUrl()).setTid(title));
                startActivity(new Intent(getApplicationContext(), NewsDetailActivity.class));
            }
        });
    }

    @Override
    public void onRefresh() {
        page = 1;
        String type = title.equals(getString(R.string.chart_news)) ? "1" : "2";
        if (isHs) {
            queryHs(type);
        } else {
            queryHk(type);
        }
    }

    @Override
    public void onLoadMore() {
        Log.d(TAG, "onLoadMore: ");
        page++;
        String type = title.equals(getString(R.string.chart_news)) ? "1" : "2";
        if (isHs) {
            queryHs(type);
        } else {
            queryHk(type);
        }
        if (!mXlvNews.ismPullLoading()) {

        }
    }

    private void queryHk(String type) {
        _apiManager.getService().queryHkNewsList(type, ticCode, page + "", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultNewsData, throwable -> {
                    mXlvNews.stopLoadMore();
                    mXlvNews.stopRefresh();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.e(TAG, "queryHkNewsList: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryHkNewsList(type, ticCode, page + "", "10"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultNewsData, throwable -> {
//                    mXlvNews.stopLoadMore();
//                    mXlvNews.stopRefresh();
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                    Log.e(TAG, "queryHkNewsList: ", throwable);
//                });
    }

    private void queryHs(String type) {
        Log.d(TAG, "type: " + type + "\n" + "ticCode: " + ticCode + "\npage: " + page);
        _apiManager.getService().queryHsNewsList(type, ticCode, page + "", "10")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultNewsData, throwable -> {
                    mXlvNews.stopLoadMore();
                    mXlvNews.stopRefresh();
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.e(TAG, "queryHkNewsList: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryHsNewsList(type, ticCode, page + "", "10"))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultNewsData, throwable -> {
//                    mXlvNews.stopLoadMore();
//                    mXlvNews.stopRefresh();
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                    Log.e(TAG, "queryHkNewsList: ", throwable);
//                });
    }

    private void resultNewsData(ReturnListValue<StockNewsBean> retValue) {
        if (dialog != null) {
            dialog.dismiss();
        }
        List<StockNewsBean> newsBeanList = retValue.getData();
        Log.d(TAG, "resultNewsData: " + newsBeanList.size());

        if (mXlvNews.ismPullRefreshing()) {
            data.clear();
            mXlvNews.stopRefresh();
        }
        mXlvNews.stopLoadMore();
        if (0 == newsBeanList.size()) {
            mXlvNews.setPullLoadEnable(false);
        } else {
            mXlvNews.setPullLoadEnable(true);
        }
        data.addAll(newsBeanList);
        if (data.size() < 10) {
            mXlvNews.setPullLoadEnable(false);
        } else {
            mXlvNews.setPullLoadEnable(true);
        }
        newsAdapter.notifyDataSetChanged();
    }

}
