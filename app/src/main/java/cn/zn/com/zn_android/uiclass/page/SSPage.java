package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.IndustryAdapter;
import cn.zn.com.zn_android.adapter.SSListAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.HotHyGnModel;
import cn.zn.com.zn_android.model.SSStockModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.DfbBean;
import cn.zn.com.zn_android.model.bean.DjgbBean;
import cn.zn.com.zn_android.model.bean.GjgbBean;
import cn.zn.com.zn_android.model.bean.HotGnBean;
import cn.zn.com.zn_android.model.bean.HotHyBean;
import cn.zn.com.zn_android.model.bean.HslBean;
import cn.zn.com.zn_android.model.bean.IndexBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.SSListBean;
import cn.zn.com.zn_android.model.bean.StocksBean;
import cn.zn.com.zn_android.model.bean.ZfbBean;
import cn.zn.com.zn_android.model.bean.ZflBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.presenter.SSPresenter;
import cn.zn.com.zn_android.presenter.requestType.IndexRequestType;
import cn.zn.com.zn_android.uiclass.TitledListView;
import cn.zn.com.zn_android.uiclass.activity.HotConceptsActivity;
import cn.zn.com.zn_android.uiclass.activity.StockIndicesActivity;
import cn.zn.com.zn_android.uiclass.activity.UpDownRankingActivity;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ClassUtils;
import cn.zn.com.zn_android.viewfeatures.SSView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 沪深
 * Created by Jolly on 2016/6/29 0029.
 */
public class SSPage extends BaseMvpPage<SSView, SSPresenter>
        implements AbsListView.OnScrollListener, View.OnClickListener, SSView, XListView.IXListViewListener {

    private TitledListView mTitledListView;
    private RelativeLayout mRlSse;
    private TextView mTvTitle1, mTvSse, mTvSse1, mTvSse2;
    private RelativeLayout mRlSzstock;
    private TextView mTvTitle2, mTvSz, mTvSz1, mTvSz2;
    private RelativeLayout mRlGem;
    private TextView mTvTitle3, mTvGem, mTvGem1, mTvGem2;

    private List<SSStockModel> data = new ArrayList<>();
    private SSListAdapter mSsAdapter;

    private List<HotHyGnModel> hotHyData = new ArrayList<>();
    private IndustryAdapter industryAdapter;

    private List<HotHyGnModel> hotGnData = new ArrayList<>();
    private IndustryAdapter conceptAdapter;

    @Override
    public SSPresenter initPresenter() {
        return new SSPresenter(this, mActivity);
    }

    public SSPage(Activity activity) {
        super(activity);
    }

    @Override
    public void initData() {
        super.initData();
        mTitledListView = new TitledListView(mActivity);
        mTitledListView.setTitleClickListener(new TitledListView.TitleClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(v.getTag().toString()));
                mActivity.startActivity(new Intent(mActivity, UpDownRankingActivity.class));
            }
        });
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_ss_head, null, false);
        mTitledListView.addHeaderView(headView);
//        View footView = LayoutInflater.from(mActivity).inflate(R.layout.layout_ss_foot, null, false);
//        mTitledListView.addFooterView(footView);
        mTitledListView.setXListViewListener(this);

        initPage(headView);

        if (null != flContent) {
            flContent.removeAllViews();
            flContent.addView(mTitledListView);
        }

        initListData();
        mSsAdapter = new SSListAdapter(mActivity, R.layout.item_ss_list, data);
        mTitledListView.setAdapter(mSsAdapter);
        mTitledListView.setOnScrollListener(this);

        queryData();

    }

    @Override
    public void queryIndexData() {
        queryData();
    }

    private void queryData() {
        presenter.queryShIndex();
        presenter.querySzIndex();
        presenter.queryCyIndex();
        presenter.querySS();
    }

    private void initPage(View headView) {
        headView.findViewById(R.id.rl_gem).setOnClickListener(this);
        headView.findViewById(R.id.rl_sse).setOnClickListener(this);
        headView.findViewById(R.id.rl_szstock).setOnClickListener(this);
        headView.findViewById(R.id.tv_hot_industry).setOnClickListener(this);
        headView.findViewById(R.id.tv_hot_concept).setOnClickListener(this);

        mRlSse = (RelativeLayout) headView.findViewById(R.id.rl_sse);
        mTvTitle1 = (TextView) headView.findViewById(R.id.title1);
        mTvSse = (TextView) headView.findViewById(R.id.tv_sse);
        mTvSse1 = (TextView) headView.findViewById(R.id.tv_sse1);
        mTvSse2 = (TextView) headView.findViewById(R.id.tv_sse2);

        mRlSzstock = (RelativeLayout) headView.findViewById(R.id.rl_szstock);
        mTvTitle2 = (TextView) headView.findViewById(R.id.title2);
        mTvSz = (TextView) headView.findViewById(R.id.tv_szstock);
        mTvSz1 = (TextView) headView.findViewById(R.id.tv_szstock1);
        mTvSz2 = (TextView) headView.findViewById(R.id.tv_szstock2);

        mRlGem = (RelativeLayout) headView.findViewById(R.id.rl_gem);
        mTvTitle3 = (TextView) headView.findViewById(R.id.title3);
        mTvGem = (TextView) headView.findViewById(R.id.tv_gem);
        mTvGem1 = (TextView) headView.findViewById(R.id.tv_gem1);
        mTvGem2 = (TextView) headView.findViewById(R.id.tv_gem2);

        GridView industryGV = (GridView) headView.findViewById(R.id.gv_hot_indus);
        industryAdapter = new IndustryAdapter(mActivity, R.layout.item_gv_hot_stocks, hotHyData);
        industryGV.setAdapter(industryAdapter);
        industryGV.setOnItemClickListener((parent, view, position, id) -> {
            EventBus.getDefault().postSticky(new AnyEventType(hotHyData.get(position)).setState(false));
            mActivity.startActivity(new Intent(mActivity, UpDownRankingActivity.class));
        });

        GridView conceptGV = (GridView) headView.findViewById(R.id.gv_hot_concept);
        conceptAdapter = new IndustryAdapter(mActivity, R.layout.item_gv_hot_stocks, hotGnData);
        conceptGV.setAdapter(conceptAdapter);
        conceptGV.setOnItemClickListener((parent, view, position, id) -> {
            EventBus.getDefault().postSticky(new AnyEventType(hotGnData.get(position)).setState(true));
            mActivity.startActivity(new Intent(mActivity, UpDownRankingActivity.class));
        });

//        ScrollListView ahStockLV = (ScrollListView) footView.findViewById(R.id.lv_ah_stock);
//        SimpleAdapter shStockAdapter = new SimpleAdapter(mActivity, getAHData(), R.layout.item_ah,
//                new String[]{"name", "aPrice", "aUp", "hPrice", "hUp", "premium"},
//                new int[]{R.id.tv_name, R.id.tv_a, R.id.tv_a_updown, R.id.tv_h, R.id.tv_h_updown, R.id.tv_up_down});
//        ahStockLV.setAdapter(shStockAdapter);
    }
//
//    private List<Map<String, String>> getAHData() {
//        List<Map<String, String>> data = new ArrayList<>();
//        for (int i = 0; i < 5; i++) {
//            HashMap<String, String> map = new HashMap<>();
//            map.put("name", "股票" + i);
//            map.put("aPrice", i + "0");
//            map.put("aUp", "0." + i + "%");
//            map.put("hPrice", "6.55");
//            map.put("hUp", "0." + i + "%");
//            map.put("premium", i + ".0%");
//            data.add(map);
//        }
//        return data;
//    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 第一项与第二项标题不同，说明标题需要移动
        if (data.size() == 0) {
            return;
        }
        if (firstVisibleItem == 0 || 1 == firstVisibleItem) return;
        if (data.size() == firstVisibleItem - 1 || data.size() == firstVisibleItem) {

            ((TitledListView) view).updateTitle(data.get(firstVisibleItem - 2).getBean().getType(), firstVisibleItem - 2);
            return;
        }

        if (!data.get(firstVisibleItem - 1).getBean().getType().equals(data.get(firstVisibleItem - 2).getBean().getType())) {
            ((TitledListView) view).moveTitle();
            if (firstVisibleItem > 1) {
                ((TitledListView) view).updateTitle(data.get(firstVisibleItem - 2).getBean().getType(), firstVisibleItem - 2);
            }
        } else {
            ((TitledListView) view).updateTitle(data.get(firstVisibleItem - 1).getBean().getType(), firstVisibleItem - 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_sse:
                EventBus.getDefault().postSticky(new AnyEventType(mTvTitle1.getText().toString()).setTid(Constants.indexType[0]));
                mActivity.startActivity(new Intent(mActivity, StockIndicesActivity.class));
                break;
            case R.id.rl_szstock:
                EventBus.getDefault().postSticky(new AnyEventType(mTvTitle2.getText().toString()).setTid(Constants.indexType[1]));
                mActivity.startActivity(new Intent(mActivity, StockIndicesActivity.class));
                break;
            case R.id.rl_gem:
                EventBus.getDefault().postSticky(new AnyEventType(mTvTitle3.getText().toString()).setTid(Constants.indexType[2]));
                mActivity.startActivity(new Intent(mActivity, StockIndicesActivity.class));
                break;
            case R.id.tv_hot_industry:
                EventBus.getDefault().postSticky(new AnyEventType(mActivity.getString(R.string.hot_industry)));
                mActivity.startActivity(new Intent(mActivity, HotConceptsActivity.class));
                break;
            case R.id.tv_hot_concept:
                EventBus.getDefault().postSticky(new AnyEventType(mActivity.getString(R.string.hot_concept)));
                mActivity.startActivity(new Intent(mActivity, HotConceptsActivity.class));
                break;
        }
    }

    @Override
    public void onSuccess(IndexRequestType requestType, Object object) {
        switch (requestType) {
            case SH_INDEX:
                IndexBean shBean = (IndexBean) object;
                initShIndex(shBean, mTvTitle1, mActivity.getString(R.string.sse),
                        mTvSse, mTvSse1, mTvSse2, mRlSse);
                break;
            case SZ_INDEX:
                IndexBean szBean = (IndexBean) object;
                initShIndex(szBean, mTvTitle2, mActivity.getString(R.string.szstock),
                        mTvSz, mTvSz1, mTvSz2, mRlSzstock);
                break;
            case CY_INDEX:
                IndexBean cyBean = (IndexBean) object;
                initShIndex(cyBean, mTvTitle3, mActivity.getString(R.string.gem),
                        mTvGem, mTvGem1, mTvGem2, mRlGem);
                break;
            case SS_LIST:
                SSListBean ssListBean = (SSListBean) object;
                updateListData(ssListBean);
                break;

//            case :
//                break;
        }
    }

    @Override
    public void onError(IndexRequestType requestType, Throwable t) {
        switch (requestType) {
            case SH_INDEX:
                if (mTitledListView.ismPullRefreshing()) {
                    mTitledListView.stopRefresh();
                }
                if (mTitledListView.ismPullLoading()) {
                    mTitledListView.stopLoadMore();
                }
                break;
            case SZ_INDEX:

                break;
            case CY_INDEX:
                break;
//            case :
//                break;
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 刷新顶部各项指数UI
     *
     * @param bean  指数bean
     * @param tv0   指数名称Textview
     * @param title 指数名称
     * @param tv1   LastIndex
     * @param tv2   Change
     * @param tv3   ChangeRate
     * @param rl    背景
     */
    private void initShIndex(IndexBean bean, TextView tv0, String title, TextView tv1, TextView tv2, TextView tv3, RelativeLayout rl) {
        if (mTitledListView.ismPullRefreshing()) {
            mTitledListView.stopRefresh();
        }
        if (mTitledListView.ismPullLoading()) {
            mTitledListView.stopLoadMore();
        }

        if (bean.getChange().startsWith("-")) {
            rl.setBackgroundColor(mActivity.getResources().getColor(R.color.green_down));
            tv2.setText(bean.getChange());
            tv3.setText(bean.getChangeRate());
        } else {
            rl.setBackgroundColor(mActivity.getResources().getColor(R.color.app_bar_color));
            if (!bean.getChange().startsWith("+")) {
                tv2.setText("+");
                tv3.setText("+");
            }
            tv2.append(bean.getChange());
            tv3.append(bean.getChangeRate());
        }
        tv0.setText(title);
        tv1.setText(bean.getLastIndex());
        tv3.append("%");
    }

    @Override
    public void onRefresh() {
        queryData();
    }

    @Override
    public void onLoadMore() {

    }

    /**
     * 初始化列表数据
     */
    private void initListData() {
        String[] typeArray = mActivity.getResources().getStringArray(R.array.ss_classic);
        int count = typeArray.length;
        for (int i = 0; i < count; i++) {
            StocksBean bean = new StocksBean();
            bean.setType(typeArray[i]);
            List<MarketImp> list = new ArrayList<>();
            bean.setBeanList(list);
            bean.setmStockType(Constants.SH);
            SSStockModel model = new SSStockModel(mActivity, bean);
            data.add(model);
        }
    }

    /**
     * 更新行情列表数据
     *
     * @param listBean
     */
    private void updateListData(SSListBean listBean) {
        updateHyData(listBean.getHot_hy());
        updateGnData(listBean.getHot_gn());

        // 涨幅榜
        List<ZfbBean> zfbBeans = listBean.getZfb();
        List<MarketImp> zfbList = new ArrayList<>();
        if (null != zfbBeans) {
            for (ZfbBean bean : zfbBeans) {
                bean = new ClassUtils<>(bean).initField(ZfbBean.class);
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setName(bean.getShortNM());
                stockBean.setCode(bean.getTicker());
                stockBean.setPrice(bean.getLastPrice() + "");
                stockBean.setChangePct(Float.valueOf(bean.getChangePct() + ""));
                zfbList.add(stockBean);
            }
        }
        data.get(0).getBean().setBeanList(zfbList);

        // 跌幅榜
        List<DfbBean> dfbBeans = listBean.getDfb();
        List<MarketImp> dfbList = new ArrayList<>();
        if (null != dfbBeans) {
            for (DfbBean bean : dfbBeans) {
                bean = new ClassUtils<>(bean).initField(DfbBean.class);
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setName(bean.getShortNM());
                stockBean.setCode(bean.getTicker());
                stockBean.setPrice(bean.getLastPrice() + "");
                stockBean.setChangePct(Float.valueOf(bean.getChangePct() + ""));
                dfbList.add(stockBean);
            }
        }
        data.get(1).getBean().setBeanList(dfbList);

        // 换手率榜
        List<HslBean> hslBeans = listBean.getHsl();
        List<MarketImp> hslList = new ArrayList<>();
        if (null != hslBeans) {
            for (HslBean bean : hslBeans) {
                bean = new ClassUtils<>(bean).initField(HslBean.class);
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setName(bean.getSEC_SHORT_NAME());
                stockBean.setCode(bean.getTICKER_SYMBOL());
                stockBean.setPrice(bean.getLastPrice());
                stockBean.setChangePct(Float.valueOf(bean.getHsl() + ""));
                hslList.add(stockBean);
            }
        }
        data.get(2).getBean().setBeanList(hslList);

        // 振幅榜
        List<ZflBean> zflBeans = listBean.getZfl();
        List<MarketImp> zflList = new ArrayList<>();
        if (null != zflBeans) {
            for (ZflBean bean : zflBeans) {
                bean = new ClassUtils<>(bean).initField(ZflBean.class);
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setName(bean.getSecurityName());
                stockBean.setCode(bean.getSecurityID());
                stockBean.setPrice(bean.getLastPrice());
                stockBean.setChangePct(Float.valueOf(bean.getZhenful() + ""));
                zflList.add(stockBean);
            }
        }
        data.get(3).getBean().setBeanList(zflList);

        // 高价股榜
        List<GjgbBean> gjgbBeans = listBean.getGjgb();
        List<MarketImp> gjgbList = new ArrayList<>();
        if (null != gjgbBeans) {
            for (GjgbBean bean : gjgbBeans) {
                bean = new ClassUtils<>(bean).initField(GjgbBean.class);
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setName(bean.getSecurityName());
                stockBean.setCode(bean.getSecurityID());
                stockBean.setPrice(bean.getLastPrice());
                if (null != bean.getZhangful()) {
                    stockBean.setChangePct(Float.valueOf(bean.getZhangful() + ""));
                }
                gjgbList.add(stockBean);
            }
        }
        data.get(4).getBean().setBeanList(gjgbList);

        // 低价股榜
        List<DjgbBean> djgbBeans = listBean.getDjgb();
        List<MarketImp> djgbList = new ArrayList<>();
        if (null != djgbBeans) {
            for (DjgbBean bean : djgbBeans) {
                bean = new ClassUtils<>(bean).initField(DjgbBean.class);
                OptionalStockBean stockBean = new OptionalStockBean();
                if (null != bean) {
                    stockBean.setName(bean.getSecurityName());
                    stockBean.setCode(bean.getSecurityID());
                    stockBean.setPrice(bean.getLastPrice());
                    try {
                        stockBean.setChangePct(Float.valueOf(bean.getZhangful()));
                    } catch (Exception e) {
                        e.printStackTrace();
                        stockBean.setChangePct(0.0f);
                    }
                    djgbList.add(stockBean);
                }
            }
        }
        data.get(5).getBean().setBeanList(djgbList);

//        // 港股通
//        List<GgtBean> ggtBeans = listBean.getGgt();
//        List<MarketImp> ggtList = new ArrayList<>();
//        for (GgtBean bean: ggtBeans) {
//            OptionalStockBean stockBean = new OptionalStockBean();
//            stockBean.setName(bean.getTicker_name());
//            stockBean.setCode(bean.getTickerSymbol());
//            stockBean.setPrice(bean.getChangePct());
//            if (null != bean.getChange()) {
//                stockBean.setChangePct(Float.valueOf(bean.getChange() + ""));
//            }
//            ggtList.add(stockBean);
//        }
//        data.get(6).getBean().setBeanList(ggtList);

        mSsAdapter.notifyDataSetChanged();
    }

    /**
     * 更新热门行业数据
     *
     * @param hyList 热门行业数据源
     */
    private void updateHyData(List<HotHyBean> hyList) {
        hotHyData.clear();
        int count = hyList.size();
        for (int i = 0; i < count; i++) {
            HotHyGnModel model = new HotHyGnModel(hyList.get(i).getName(),
                    hyList.get(i).getAverage() + "%", hyList.get(i).getTic_name(),
                    hyList.get(i).getChange(), hyList.get(i).getChange_rate() + "%",
                    hyList.get(i).getCode_id());
            hotHyData.add(model);
        }
        industryAdapter.notifyDataSetChanged();
    }

    /**
     * 更新热门概念数据
     *
     * @param gnList 热门概念数据源
     */
    private void updateGnData(List<HotGnBean> gnList) {
        hotGnData.clear();
        int count = gnList.size();
        for (int i = 0; i < count; i++) {

            HotHyGnModel model = new HotHyGnModel(gnList.get(i).getName(),
                    gnList.get(i).getAverage() + "%", gnList.get(i).getTic_name(),
                    gnList.get(i).getChange(), gnList.get(i).getChange_rate() + "%", gnList.get(i).getCode_id());
            hotGnData.add(model);
        }
        conceptAdapter.notifyDataSetChanged();
    }

}
