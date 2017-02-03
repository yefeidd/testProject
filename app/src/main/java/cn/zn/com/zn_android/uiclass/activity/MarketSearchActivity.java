package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.SearchMarketAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.presenter.StockPresenter;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import cn.zn.com.zn_android.viewfeatures.StockView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/6/29 0029.
 * email: m15267280642@163.com
 * explain:
 */
public class MarketSearchActivity extends BaseMVPActivity<StockView, StockPresenter>
        implements OnClickListener, StockView, TextWatcher, AdapterView.OnItemClickListener {


    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.et_keywords)
    EditText mEtKeywords;
    @Bind(R.id.iv_article_search)
    ImageView mIvMaketSearch;
    @Bind(R.id.xlv_search_list)
    XListView mXlvSearchList;

    private ImageView selectImgV;

    private List<MarketImp> marketDataList;
    private SearchMarketAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public StockPresenter initPresenter() {
        return new StockPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_article_search;
    }

    @Override
    protected void initView() {
        mEtKeywords.setHint(getString(R.string.market_search_tips));
        mToolbarTitle.setText(getString(R.string.search));
        mToolbarTitle.setVisibility(View.VISIBLE);
        marketDataList = new ArrayList<>();
        mAdapter = new SearchMarketAdapter(_Activity);
//        mAdapter.setAddSelfStockListener((view, bean) -> {
//            selectImgV = (ImageView) view;
//            presenter.addSelfStock(_mApplication.getUserInfo().getSessionID(), bean.getCode());
//        });
        mXlvSearchList.setLoadMoreEnableShow(false);
        mXlvSearchList.setAdapter(mAdapter);
        mXlvSearchList.setPullRefreshEnable(false);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mIvMaketSearch.setOnClickListener(this);
        mEtKeywords.addTextChangedListener(this);
        mXlvSearchList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UIUtil.hidekeyBoard(v);
                return false;
            }
        });
        mXlvSearchList.setOnItemClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.iv_article_search:
                if (mEtKeywords.getText().length() > 0) {
                    presenter.searchStock(mEtKeywords.getText().toString());
                } else {
                    ToastUtil.showShort(this, getString(R.string.market_search_tips));
                }
                break;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MarketSearchActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MarketSearchActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onSuccess(StockRequestType requestType, Object object) {
        switch (requestType) {
            case ADD_SELF_SELECT:
                MessageBean msgBean = (MessageBean) object;
                if (msgBean.getMessage().contains("登录")) {
                    startActivity(new Intent(this, LoginActivity.class));
                } else {
                    selectImgV.setImageResource(R.drawable.already_add);
                }
                ToastUtil.showShort(this, msgBean.getMessage());
                break;

            case SEARCH_STOCK:
                marketDataList.clear();
                List<OptionalStockBean> list = (List<OptionalStockBean>) object;
                if (null == list || list.size() == 0) {
                    ToastUtil.showShort(this, "搜索结果为空");
                }
                marketDataList.addAll(list);
                mAdapter.setMarketList(marketDataList);

                break;
//            case :
//            break;
//            case :
//            break;
        }
    }

    @Override
    public void onError(StockRequestType requestType, Throwable t) {
        switch (requestType) {
            case ADD_SELF_SELECT:

                break;

//            case :
//            break;
//            case :
//            break;
//            case :
//            break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
            presenter.searchStock(s.toString());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) return;

        EventBus.getDefault().postSticky(new AnyEventType(marketDataList.get(position - 1)));
        startActivity(new Intent(this, MarketDetailActivity.class));
    }
}
