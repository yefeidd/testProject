package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.SearchMarketAdapter;
import cn.zn.com.zn_android.adapter.SelfSelectAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.SelfSelectStockBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.model.factory.StockRankingFactory;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 模拟买入股票  搜索列表
 * Created by Jolly on 2016/9/10 0010.
 */
public class BuySearchActivity extends BaseActivity implements TextWatcher, AdapterView.OnItemClickListener {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.et_search)
    EditText mEtSearch;
    @Bind(R.id.tv_self_choose)
    TextView mTvSelfChoose;
    @Bind(R.id.xlv_self_stock)
    XListView mXlvSelfStock;

    private SelfSelectAdapter mAdapter;
    private SearchMarketAdapter searchAdapter;
    private List<MarketImp> selfDataList = new ArrayList<>();
    private List<MarketImp> dataList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_buy_search);
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
    protected void initView() {
        mToolbarTitle.setText(R.string.simulated_buy);

        mEtSearch.addTextChangedListener(this);

        mAdapter = new SelfSelectAdapter(this, R.layout.item_self_show, dataList, Constants.SH);
        mXlvSelfStock.setAdapter(mAdapter);
        searchAdapter = new SearchMarketAdapter(this);

        querySelfStock(_mApplication.getUserInfo().getSessionID());
    }

    @Override
    protected void initEvent() {
        mXlvSelfStock.setPullLoadEnable(false);
        mXlvSelfStock.setPullRefreshEnable(false);
        mXlvSelfStock.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UIUtil.hidekeyBoard(v);
                return false;
            }
        });
        mXlvSelfStock.setOnItemClickListener(this);
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (s.length() >= 3) {
            searchStock(s.toString());
        }
    }

    public void querySelfStock(String sessionId) {
        _apiManager.getService().querySelfStock(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultSelfStock, throwable -> {
                    Log.e(TAG, "querySelfStock: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().querySelfStock(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultSelfStock, throwable -> {
//                    Log.e(TAG, "querySelfStock: ", throwable);
//                });
    }

    private void resultSelfStock(ReturnListValue<SelfSelectStockBean> retValue) {
        if (null != mXlvSelfStock) {
            mXlvSelfStock.stopRefresh();
        }
        selfDataList.clear();
        List<SelfSelectStockBean> selfSelectStockList = retValue.getData();
        Iterator<SelfSelectStockBean> iter = selfSelectStockList.iterator();
        while (iter.hasNext()) {
            SelfSelectStockBean bean = iter.next();
            if (bean.getTicker().length() == 5) {
                iter.remove();
            }
        }
        selfDataList.addAll(StockRankingFactory.SelfStockToMarketImp(selfSelectStockList));
        mAdapter = new SelfSelectAdapter(this, R.layout.item_self_show, selfDataList, Constants.SH);
        mXlvSelfStock.setAdapter(mAdapter);
//        mAdapter.setData(selfDataList, Constants.SH);

//        setContentShow();
    }

    /**
     * 查询股票
     * <p>
     *
     * @param keyword 股票代码，股票拼音简称
     */
    private void searchStock(String keyword) {
        _apiManager.getService().searchStock(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultSearchStock, throwable -> {
                    Log.e(TAG, "searchStock: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().searchStock(keyword))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultSearchStock, throwable -> {
//                    Log.e(TAG, "searchStock: ", throwable);
//                });
    }

    private void resultSearchStock(ReturnListValue<OptionalStockBean> retValue) {
        dataList.clear();
        List<OptionalStockBean> list = retValue.getData();
        Iterator<OptionalStockBean> iter = list.iterator();
        while (iter.hasNext()) {
            OptionalStockBean bean = iter.next();
            if (bean.getCode().length() == 5) {
                iter.remove();
            }
        }
        if (null == list || list.size() == 0) {
            mTvSelfChoose.setVisibility(View.VISIBLE);
            ToastUtil.showShort(this, "搜索结果为空");
            mAdapter = new SelfSelectAdapter(this, R.layout.item_self_show, selfDataList, Constants.SH);
            mXlvSelfStock.setAdapter(mAdapter);
        } else {
            mTvSelfChoose.setVisibility(View.GONE);
            dataList.addAll(list);
            searchAdapter.setMarketList(dataList);
            mXlvSelfStock.setAdapter(searchAdapter);
//            mAdapter = new SelfSelectAdapter(this, R.layout.item_self_show, dataList, Constants.SH);
//            mXlvSelfStock.setAdapter(mAdapter);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (0 == position) return;

        String code;
        if (dataList.size() == 0) {
            code = ((OptionalStockBean) selfDataList.get(position - 1)).getCode();
        } else {
            code = ((OptionalStockBean) dataList.get(position - 1)).getCode();
        }
        EventBus.getDefault().postSticky(new AnyEventType(code).setTid(getString(R.string.buy_in)));
        startActivity(new Intent(this, BuyInActivity.class));
    }
}
