package cn.zn.com.zn_android.uiclass.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.SelfSelectAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.HotHyGnModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ConceptBean;
import cn.zn.com.zn_android.model.bean.DfbBean;
import cn.zn.com.zn_android.model.bean.GjgbBean;
import cn.zn.com.zn_android.model.bean.HKRankListBean;
import cn.zn.com.zn_android.model.bean.HotConceptBean;
import cn.zn.com.zn_android.model.bean.HotHyBean;
import cn.zn.com.zn_android.model.bean.HslBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.ZfbBean;
import cn.zn.com.zn_android.model.bean.ZflBean;
import cn.zn.com.zn_android.model.factory.StockRankingFactory;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.presenter.UpDownRankingPresenter;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.service.RefreshDataService;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.uiclass.xlistview.XScrollView;
import cn.zn.com.zn_android.viewfeatures.UpDownRankingView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/7/1 0001.
 * email: m15267280642@163.com
 * explain:
 */
public class UpDownRankingActivity extends BaseMVPActivity<UpDownRankingView, UpDownRankingPresenter>
        implements View.OnClickListener, XListView.OnItemClickListener, XScrollView.IXScrollViewListener,
                    UpDownRankingView{

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xsv_up_down)
    XScrollView mXsvUpDown;
    ScrollListView mXlvList;
    ImageView mIvUpDownTips, mIvNewPriceTips;
    TextView mTvUpDown, mTvNewPrice, mTvNameCode;
    TextView mTvGnUd, mTvGnUp, mTvGnDown, mTvGnPing;
    private SelfSelectAdapter mAdapter;
    private List<MarketImp> dataList = new ArrayList<>();
    private boolean OrderUpDown = true;//true代表升序排列,false代表降序排列

    private boolean isHotConcept = false;
    private String title = "";
    private String hotCodeId = "";

    LocalBroadcastManager lbm;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(RefreshDataService.TAG)) {
                Log.d(TAG, "onReceive: ");
//                queryData();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    public UpDownRankingPresenter initPresenter() {
        return new UpDownRankingPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_up_down_list;
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            title = event.getObject().toString();
        } else if (event.getObject() instanceof HotHyGnModel){
            isHotConcept = event.getState();
            HotHyGnModel hyGnModel = (HotHyGnModel) event.getObject();
            title = hyGnModel.getName();
            hotCodeId = hyGnModel.getCodeId();
        } else if (event.getObject() instanceof HotConceptBean){
            isHotConcept = event.getState();
            HotConceptBean bean = (HotConceptBean) event.getObject();
            title = bean.getHyname();
            hotCodeId = bean.getCode_id();
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(title);

        lbm = LocalBroadcastManager.getInstance(this);
        View content = LayoutInflater.from(this).inflate(R.layout.layout_up_down_list_content, null);
        initContentView(content);
        mXsvUpDown.setView(content);
        mXsvUpDown.setPullLoadEnable(false);
        mXsvUpDown.setIXScrollViewListener(this);

        queryData();
    }

    private void initContentView(View content) {
        mTvNameCode = (TextView) content.findViewById(R.id.tv_name_code);

        mTvUpDown = (TextView) content.findViewById(R.id.tv_updown);
        mTvUpDown.setOnClickListener(this);
        mIvUpDownTips = (ImageView) content.findViewById(R.id.iv_up_down_tips);
        mIvUpDownTips.setOnClickListener(this);

        mTvNewPrice = (TextView) content.findViewById(R.id.tv_new_price);
        mTvNewPrice.setOnClickListener(this);
        mIvNewPriceTips = (ImageView) content.findViewById(R.id.iv_new_price_tips);
        mIvNewPriceTips.setOnClickListener(this);

        mXlvList = (ScrollListView) content.findViewById(R.id.xlv_list);
        mAdapter = new SelfSelectAdapter(_Activity, R.layout.item_self_show, dataList);
        if (title.equals(getString(R.string.main_money)) || title.equals(getString(R.string.new_money))) {
            mAdapter.setTurnVolume(true);
        }
        mXlvList.setAdapter(mAdapter);
        mXlvList.setFocusable(false);
        mXlvList.setOnItemClickListener(this);

//        ((CanvasChartView)content.findViewById(R.id.ccv_chat)).setData(getData());

        if (isHotConcept) {
            content.findViewById(R.id.ll_hot_concept_head).setVisibility(View.VISIBLE);
        } else {
            content.findViewById(R.id.ll_hot_concept_head).setVisibility(View.GONE);
        }

        mTvGnUd = (TextView) content.findViewById(R.id.tv_up_down);
        mTvGnUp = (TextView) content.findViewById(R.id.tv_up);
        mTvGnDown = (TextView) content.findViewById(R.id.tv_down);
        mTvGnPing = (TextView) content.findViewById(R.id.tv_liquadate);
    }

    private void queryData() {
        if (title.equals(getString(R.string.up_list))) {
            OrderUpDown = true;
//            mIvUpDownTips.setImageResource(R.drawable.down_arrows);
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryZfbList();
//            if (OrderUpDown) {
//            } else {
//                presenter.queryDfbList();
//            }
        }
        if (title.equals(getString(R.string.drop_list))) {
            OrderUpDown = false;
            mIvUpDownTips.setImageResource(R.drawable.up_arrows);
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryDfbList();
//            if (OrderUpDown) {
//            } else {
//                presenter.queryZfbList();
//            }
        }
        if (title.equals(getString(R.string.exchange_rate_list))) {
            mTvUpDown.setText(getString(R.string.change_hand_rate));
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryHslList(OrderUpDown ? "" : "1");
        }
        if (title.equals(getString(R.string.amplitude_list))) {
            mTvUpDown.setText(getString(R.string.swing));
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryZfList(OrderUpDown ? "" : "1");
        }
        if (title.equals(getString(R.string.high_price_stock_list))) {
//            OrderUpDown = true;
//            mIvNewPriceTips.setImageResource(R.drawable.down_arrows);
            mIvNewPriceTips.setVisibility(View.VISIBLE);
            presenter.queryGjgbList(OrderUpDown ? "" : "1");
        }
        if (title.equals(getString(R.string.low_price_stock_list))) {
//            OrderUpDown = false;
            mIvNewPriceTips.setImageResource(R.drawable.up_arrows);
            mIvNewPriceTips.setVisibility(View.VISIBLE);
            presenter.queryGjgbList(OrderUpDown ? "1" : "");
        }
        if (!hotCodeId.equals("") && !isHotConcept) {
            mTvUpDown.setText(getString(R.string.up_rate));
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryHotHyList(hotCodeId, OrderUpDown ? "1" : "2");
        }
        if (!hotCodeId.equals("") && isHotConcept) {
            mTvUpDown.setText(getString(R.string.up_rate));
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryHotGnList(hotCodeId, OrderUpDown ? "1" : "2");
        }

        if (title.equals(getString(R.string.main_up))) {
            OrderUpDown = true;
//            mIvUpDownTips.setImageResource(R.drawable.down_arrows);
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryMainUpdown("1");
        }
        if (title.equals(getString(R.string.main_down))) {
            OrderUpDown = false;
            mIvUpDownTips.setImageResource(R.drawable.up_arrows);
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryMainUpdown("2");
        }
        if (title.equals(getString(R.string.main_money))) {
            mTvUpDown.setText(getString(R.string.deel_money));
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryMainMoney(OrderUpDown ? "3" : "4");
        }
        if (title.equals(getString(R.string.new_up))) {
            Log.d(TAG, "new_up: ");
            OrderUpDown = true;
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryNewUpdown("1");
        }
        if (title.equals(getString(R.string.new_down))) {
            OrderUpDown = false;
            mIvUpDownTips.setImageResource(R.drawable.up_arrows);
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryNewUpdown("2");
        }
        if (title.equals(getString(R.string.new_money))) {
            mTvUpDown.setText(getString(R.string.deel_money));
            mIvUpDownTips.setVisibility(View.VISIBLE);
            presenter.queryNewMoney(OrderUpDown ? "3" : "4");
        }

    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("UpDownRankingActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"UpDownRankingActivity"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("UpDownRankingActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"UpDownRankingActivity"为页面名称，可自定义
        MobclickAgent.onPause(this);
        lbm.unregisterReceiver(mReceiver);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(RefreshDataService.TAG);
        lbm.registerReceiver(mReceiver, mFilter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.iv_up_down_tips:
            case R.id.tv_updown:
                if (title.equals(getString(R.string.high_price_stock_list)) || title.equals(getString(R.string.low_price_stock_list))) {
                    break;
                }
                if (OrderUpDown) {
                    if (title.equals(getString(R.string.up_list))) {
                        title = getString(R.string.drop_list);
                        mToolbarTitle.setText(title);
                    }
                    if (title.equals(getString(R.string.main_up))) {
                        title = getString(R.string.main_down);
                        mToolbarTitle.setText(title);
                    }
                    if (title.equals(getString(R.string.new_up))) {
                        title = getString(R.string.new_down);
                        mToolbarTitle.setText(title);
                    }
                    mIvUpDownTips.setImageResource(R.drawable.up_arrows);
                } else {
                    if (title.equals(getString(R.string.drop_list))) {
                        title = getString(R.string.up_list);
                        mToolbarTitle.setText(title);
                    }
                    if (title.equals(getString(R.string.main_down))) {
                        title = getString(R.string.main_up);
                        mToolbarTitle.setText(title);
                    }
                    if (title.equals(getString(R.string.new_down))) {
                        title = getString(R.string.new_up);
                        mToolbarTitle.setText(title);
                    }
                    mIvUpDownTips.setImageResource(R.drawable.down_arrows);
                }
                OrderUpDown = !OrderUpDown;
                queryData();
                break;

            case R.id.tv_new_price:
            case R.id.iv_new_price_tips:
                if (title.equals(getString(R.string.high_price_stock_list)) ||
                        title.equals(getString(R.string.low_price_stock_list))) {
                    if (OrderUpDown) {
                        mIvNewPriceTips.setImageResource(R.drawable.up_arrows);
                        OrderUpDown = !OrderUpDown;
                    } else {
                        mIvNewPriceTips.setImageResource(R.drawable.down_arrows);
                        OrderUpDown = !OrderUpDown;
                    }
                    queryData();
                }
                break;
        }
    }

    /**
     * listview条目点击事件
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        OptionalStockBean stockBean = (OptionalStockBean) dataList.get(position);
        EventBus.getDefault().postSticky(new AnyEventType(stockBean));
        startActivity(new Intent(_Activity, MarketDetailActivity.class));
    }

    @Override
    public void onSuccess(StockRequestType requestType, Object object) {
        mXsvUpDown.stopRefresh();
        dataList.clear();
        switch (requestType) {
            case QUERY_ZFB_LIST:
                List<ZfbBean> zfbBeanList = (List<ZfbBean>) object;
                dataList.addAll(StockRankingFactory.ZfbToMarketImp(zfbBeanList));
                break;

            case QUERY_DFB_LIST:
                List<DfbBean> dfbBeanList = (List<DfbBean>) object;
                dataList.addAll(StockRankingFactory.DfbToMarketImp(dfbBeanList));
                break;

            case QUERY_HSL_LIST:
                List<HslBean> hslBeanList = (List<HslBean>) object;
                dataList.addAll(StockRankingFactory.HslToMarketImp(hslBeanList));
                break;

            case QUERY_ZF_LIST:
                List<ZflBean> zflBeanList = (List<ZflBean>) object;
                dataList.addAll(StockRankingFactory.ZfToMarketImp(zflBeanList));
                break;

            case QUERY_GJGB_LIST:
                List<GjgbBean> gjgbBeanList = (List<GjgbBean>) object;
                dataList.addAll(StockRankingFactory.GjgbToMarketImp(gjgbBeanList));
                break;

            case QUERY_DJGB_LIST:
                List<GjgbBean> djgbBeanList = (List<GjgbBean>) object;
                dataList.addAll(StockRankingFactory.GjgbToMarketImp(djgbBeanList));
                break;

            case QUERY_HY_LIST:
                List<HotHyBean> hotHyBeanList = (List<HotHyBean>) object;
                dataList.addAll(StockRankingFactory.HotHyToMarketImp(hotHyBeanList));
                break;

            case QUERY_GN_LIST:
                ConceptBean conceptBean = (ConceptBean) object;
                initHeadUI(conceptBean);
                dataList.addAll(StockRankingFactory.HotHyToMarketImp(conceptBean.getList()));
                break;

            case QUERY_HK_MAIN_UP:
            case QUERY_HK_MAIN_DOWN:
            case QUERY_HK_MAIN_MONEY:
            case QUERY_HK_NEW_UP:
            case QUERY_HK_NEW_DOWN:
            case QUERY_HK_NEW_MONEY:
                List<HKRankListBean> newMoneyList = (List<HKRankListBean>) object;
                dataList.addAll(StockRankingFactory.HKToMarketImp(newMoneyList, Constants.HK));
                break;

        }
        mAdapter.setData(dataList, 1);
    }

    @Override
    public void onError(StockRequestType requestType, Throwable t) {
        mXsvUpDown.stopRefresh();

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onRefresh() {
        queryData();

    }

    @Override
    public void onLoadMore() {

    }

    private void initHeadUI(ConceptBean bean) {
        if (bean.getUpdownrate().startsWith("-")) {
            mTvGnUd.setTextColor(getResources().getColor(R.color.green_down));
            mTvGnUd.setText(bean.getUpdownrate() + "%");
        } else if (Float.valueOf(bean.getUpdownrate()) == 0){
            mTvGnUd.setTextColor(getResources().getColor(R.color.font_value_black));
            mTvGnUd.setText(bean.getUpdownrate() + "%");
        } else if (Float.valueOf(bean.getUpdownrate()) + 100 == 0) {
            mTvGnUd.setTextColor(getResources().getColor(R.color.font_value_black));
            mTvGnUd.setText("停牌");
        } else {
            mTvGnUd.setTextColor(getResources().getColor(R.color.app_bar_color));
            mTvGnUd.setText("+" + bean.getUpdownrate() + "%");
        }
        mTvGnUp.setText(bean.getUp());
        mTvGnDown.setText(bean.getDown());
        mTvGnPing.setText(bean.getPing());
    }
}
