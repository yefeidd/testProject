package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.SSListAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.SSStockModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HKListBean;
import cn.zn.com.zn_android.model.bean.HKRankListBean;
import cn.zn.com.zn_android.model.bean.IndexBean;
import cn.zn.com.zn_android.model.bean.StocksBean;
import cn.zn.com.zn_android.model.factory.StockRankingFactory;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.presenter.HKPresenter;
import cn.zn.com.zn_android.presenter.requestType.IndexRequestType;
import cn.zn.com.zn_android.uiclass.TitledListView;
import cn.zn.com.zn_android.uiclass.activity.StockIndicesActivity;
import cn.zn.com.zn_android.uiclass.activity.UpDownRankingActivity;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.viewfeatures.HKView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/6/29 0029.
 * email: m15267280642@163.com
 * explain:
 */
public class HKStockPage extends BaseMvpPage<HKView, HKPresenter>
        implements AbsListView.OnScrollListener, View.OnClickListener, HKView,
        XListView.IXListViewListener{
    private TitledListView HKStockListView;
    private RelativeLayout mRlSse;
    private TextView mTvTitle1, mTvSse, mTvSse1, mTvSse2;
    private RelativeLayout mRlSzstock;
    private TextView mTvTitle2, mTvSz, mTvSz1, mTvSz2;
    private RelativeLayout mRlGem;
    private TextView mTvTitle3, mTvGem, mTvGem1, mTvGem2;

    private List<SSStockModel> dataList = new ArrayList<>();
    private SSListAdapter mAdapter;

    public HKStockPage(Activity activity) {
        super(activity);
    }

    @Override
    public HKPresenter initPresenter() {
        return new HKPresenter(this, mActivity);
    }

    @Override
    public void queryIndexData() {
        queryData();
    }

    @Override
    public void initData() {
        initPage();
        initTestData();
        mAdapter = new SSListAdapter(mActivity, R.layout.item_ss_list, dataList);
        HKStockListView.setAdapter(mAdapter);
        HKStockListView.setOnScrollListener(this);
        HKStockListView.setTitleClickListener(new TitledListView.TitleClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(v.getTag().toString()));
                mActivity.startActivity(new Intent(mActivity, UpDownRankingActivity.class));
            }
        });
        HKStockListView.setXListViewListener(this);
        queryData();
    }


    /**
     * 初始化改港股行情首页
     */
    private void initPage() {
        HKStockListView = new TitledListView(mActivity);
        HKStockListView.setLoadMoreEnableShow(false);
        if (flContent != null) {
            flContent.removeAllViews();
            flContent.addView(HKStockListView);
        }
        addHeadView();
    }

    private void addHeadView() {
        View headView = LayoutInflater.from(mActivity).inflate(R.layout.layout_index_num, new ListView(mActivity), false);
        HKStockListView.addHeaderView(headView);

        headView.findViewById(R.id.rl_gem).setOnClickListener(this);
        headView.findViewById(R.id.rl_sse).setOnClickListener(this);
        headView.findViewById(R.id.rl_szstock).setOnClickListener(this);

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

    }


    /**
     * 获取假数据
     */
    private void initTestData() {
        String[] typeArray = mActivity.getResources().getStringArray(R.array.hk_classic);
        int count = typeArray.length;
        for (int i = 0; i < count; i++) {
            StocksBean bean = new StocksBean();
            bean.setType(typeArray[i]);
            List<MarketImp> list = new ArrayList<>();
            bean.setBeanList(list);
            SSStockModel model = new SSStockModel(mActivity, bean);
            dataList.add(model);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 第一项与第二项标题不同，说明标题需要移动
        if (dataList.size() == 0) {
            return;
        }
        if (firstVisibleItem == 0 || 1 == firstVisibleItem) return;
        if (dataList.size() == firstVisibleItem) {
            ((TitledListView) view).updateTitle(dataList.get(firstVisibleItem - 2).getBean().getType(), firstVisibleItem - 2);
            return;
        }

        if (!dataList.get(firstVisibleItem - 1).getBean().getType().equals(dataList.get(firstVisibleItem - 2).getBean().getType())) {
            ((TitledListView) view).moveTitle();
            if (firstVisibleItem > 1) {
                ((TitledListView) view).updateTitle(dataList.get(firstVisibleItem - 2).getBean().getType(), firstVisibleItem - 2);
            }
        } else {
            ((TitledListView) view).updateTitle(dataList.get(firstVisibleItem - 1).getBean().getType(), firstVisibleItem - 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_sse:
                EventBus.getDefault().postSticky(new AnyEventType(mTvTitle1.getText().toString()).setTid(Constants.indexType[3]));
                mActivity.startActivity(new Intent(mActivity, StockIndicesActivity.class));
                break;
            case R.id.rl_szstock:
                EventBus.getDefault().postSticky(new AnyEventType(mTvTitle2.getText().toString()).setTid(Constants.indexType[4]));
                mActivity.startActivity(new Intent(mActivity, StockIndicesActivity.class));
                break;
            case R.id.rl_gem:
                EventBus.getDefault().postSticky(new AnyEventType(mTvTitle3.getText().toString()).setTid(Constants.indexType[5]));
                mActivity.startActivity(new Intent(mActivity, StockIndicesActivity.class));
                break;
        }
    }

    @Override
    public void onSuccess(IndexRequestType requestType, Object object) {
        switch (requestType) {
            case HS_INDEX:
                IndexBean shBean = (IndexBean) object;
                initHkIndex(shBean, mTvTitle1, mActivity.getString(R.string.hs_index),
                        mTvSse, mTvSse1, mTvSse2, mRlSse);
                break;
            case GQ_INDEX:
                IndexBean szBean = (IndexBean) object;
                initHkIndex(szBean, mTvTitle2, mActivity.getString(R.string.gq_index),
                        mTvSz, mTvSz1, mTvSz2, mRlSzstock);
                break;
            case HC_INDEX:
                IndexBean cyBean = (IndexBean) object;
                initHkIndex(cyBean, mTvTitle3, mActivity.getString(R.string.hc_index),
                        mTvGem, mTvGem1, mTvGem2, mRlGem);
                break;
            case HK_LIST:
                HKListBean ssListBean = (HKListBean) object;
                updateListData(ssListBean);
                break;
        }
    }

    @Override
    public void onError(IndexRequestType requestType, Throwable t) {

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
     * @param bean 指数bean
     * @param tv0 指数名称Textview
     * @param title 指数名称
     * @param tv1 LastIndex
     * @param tv2 Change
     * @param tv3 ChangeRate
     * @param rl 背景
     */
    private void initHkIndex(IndexBean bean, TextView tv0, String title, TextView tv1,
                             TextView tv2, TextView tv3, RelativeLayout rl) {
        if (HKStockListView.ismPullRefreshing()) {
            HKStockListView.stopRefresh();
        }
        if (HKStockListView.ismPullLoading()) {
            HKStockListView.stopLoadMore();
        }

        if (bean.getChange().startsWith("-")) {
            rl.setBackgroundColor(mActivity.getResources().getColor(R.color.green_down));
            tv2.setText(bean.getChange());
            tv3.setText(bean.getChangeRate());
        } else {
            rl.setBackgroundColor(mActivity.getResources().getColor(R.color.app_bar_color));
            if (!bean.getChange().startsWith("+")){
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

    /**
     * 更新行情列表数据
     * @param listBean
     */
    private void updateListData(HKListBean listBean) {

        // 涨幅榜
        List<HKRankListBean> mainUpBeans = listBean.getMian_up();
        dataList.get(0).getBean().setBeanList(StockRankingFactory.HKToMarketImp(mainUpBeans, Constants.HK));

        // 跌幅榜
        List<HKRankListBean> mainDownBeans = listBean.getMian_down();
        dataList.get(1).getBean().setBeanList(StockRankingFactory.HKToMarketImp(mainDownBeans, Constants.HK));

        // 换手率榜
        List<HKRankListBean> mainMoneyBeans = listBean.getMian_money();
        dataList.get(2).getBean().setBeanList(StockRankingFactory.HKToMarketImp(mainMoneyBeans, Constants.HK));
        dataList.get(2).getBean().setmStockType(Constants.HK);

        // 振幅榜
        List<HKRankListBean> newUpBeans = listBean.getNew_up();
        dataList.get(3).getBean().setBeanList(StockRankingFactory.HKToMarketImp(newUpBeans, Constants.HK));

        // 高价股榜
        List<HKRankListBean> newDownBeans = listBean.getNew_down();
        dataList.get(4).getBean().setBeanList(StockRankingFactory.HKToMarketImp(newDownBeans, Constants.HK));

        // 低价股榜
        List<HKRankListBean> newMoneyBeans = listBean.getNew_money();
        dataList.get(5).getBean().setBeanList(StockRankingFactory.HKToMarketImp(newMoneyBeans, Constants.HK));
        dataList.get(5).getBean().setmStockType(Constants.HK);

//        // 港股通
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        queryData();
    }

    @Override
    public void onLoadMore() {

    }

    /**
     * 请求接口指数数据
     */
    private void queryData() {
        presenter.queryHsIndex();
        presenter.queryGqIndex();
        presenter.queryHcIndex();
        presenter.queryHK();
    }
}
