package cn.zn.com.zn_android.uiclass.page;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.SelfSelectAdapter;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.SelfSelectStockBean;
import cn.zn.com.zn_android.model.factory.StockRankingFactory;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.presenter.SelfStockPresenter;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.MarketSearchActivity;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.viewfeatures.SelfStockView;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/6/27 0027.
 * email: m15267280642@163.com
 * explain:自选股页面
 */
public class SelfSelectStockPage extends BaseMvpPage<SelfStockView, SelfStockPresenter>
        implements View.OnClickListener, AdapterView.OnItemClickListener, SelfStockView,
        XListView.IXListViewListener {
    private View mSelfSelectView;
    private TextView mTvNewPrice;
    private TextView mTvUpDown;
//    private ImageView mIvNewPrice;
//    private ImageView mIvUpDown;
    private LinearLayout mRlTitle;
    private FrameLayout mFlSelfContent;
    private XListView mXlvSelfSelect;
    private List<MarketImp> selfDataList;
    private boolean OrderPrice = true;
    private boolean OrderUpDown = true;
    private RnApplication _application = RnApplication.getInstance();

    public List<MarketImp> getSelfDataList() {
        return selfDataList;
    }

    public SelfSelectStockPage(Activity activity) {
        super(activity);
    }

    @Override
    public SelfStockPresenter initPresenter() {
        return new SelfStockPresenter(this, mActivity);
    }

    @Override
    public void initData() {
        mSelfSelectView = LayoutInflater.from(mActivity).inflate(R.layout.page_self_select, flContent, false);
        if (flContent != null) {
            flContent.removeAllViews();
            flContent.addView(mSelfSelectView);
        }
        selfDataList = new ArrayList<>();
        queryIndexData();
        initPage();
        setContentShow();
    }

    @Override
    public void queryIndexData() {
        if (_application.getUserInfo().getIsLogin() != 0) {
            presenter.querySelfStock(_application.getUserInfo().getSessionID());
        }

    }

    /**
     * 初始化页面各控件
     */
    private void initPage() {
        mRlTitle = (LinearLayout) mSelfSelectView.findViewById(R.id.rl_title);
        mFlSelfContent = (FrameLayout) mSelfSelectView.findViewById(R.id.fl_self_content);
//        mXlvSelfSelect = (XListView) mSelfSelectView.findViewById(R.id.xlv_self_select);
        mTvNewPrice = (TextView) mSelfSelectView.findViewById(R.id.tv_new_price);
        mTvUpDown = (TextView) mSelfSelectView.findViewById(R.id.tv_up_down);
//        mIvNewPrice = (ImageView) mSelfSelectView.findViewById(R.id.iv_price_tips);
//        mIvUpDown = (ImageView) mSelfSelectView.findViewById(R.id.iv_up_down_tips);
        mTvNewPrice.setOnClickListener(this);
        mTvUpDown.setOnClickListener(this);
    }

    /**
     * 如果没有数据则设置界面显示为添加自选股
     */
    private void setContentShow() {
        if (null == selfDataList || selfDataList.size() == 0) {
            mRlTitle.setVisibility(View.GONE);
            if (mFlSelfContent.getChildCount() != 0)
                mFlSelfContent.removeAllViews();
            ImageView mAddData = new ImageView(mActivity);
            mAddData.setImageResource(R.drawable.self_select_add);
            LayoutParams layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;
            mAddData.setLayoutParams(layoutParams);
            mAddData.setOnClickListener(v -> {
                mActivity.startActivity(new Intent(mActivity, MarketSearchActivity.class));
            });
            mFlSelfContent.addView(mAddData);
        } else {
            mRlTitle.setVisibility(View.VISIBLE);
            if (mFlSelfContent.getChildCount() != 0)
                mFlSelfContent.removeAllViews();
            mXlvSelfSelect = new XListView(mActivity);
            mXlvSelfSelect.setLoadMoreEnableShow(false);
            SelfSelectAdapter adapter = new SelfSelectAdapter(mActivity, R.layout.item_self_show, selfDataList);
            mXlvSelfSelect.setAdapter(adapter);
            mXlvSelfSelect.setOnItemClickListener(this);
            mXlvSelfSelect.setXListViewListener(this);
            mFlSelfContent.addView(mXlvSelfSelect);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) return;

        OptionalStockBean stockBean = (OptionalStockBean) selfDataList.get(position - 1);
        EventBus.getDefault().postSticky(new AnyEventType(stockBean));
        mActivity.startActivity(new Intent(mActivity, MarketDetailActivity.class));
    }

    @Override
    public void onSuccess(StockRequestType requestType, Object object) {
        switch (requestType) {
            case QUERY_SELF_SELECT:
                if (null != mXlvSelfSelect) {
                    mXlvSelfSelect.stopRefresh();
                }
                selfDataList.clear();
                List<SelfSelectStockBean> selfSelectStockList = (List<SelfSelectStockBean>) object;
                selfDataList.addAll(StockRankingFactory.SelfStockToMarketImp(selfSelectStockList));

                setContentShow();

                break;

        }
    }

    @Override
    public void onError(StockRequestType requestType, Throwable t) {
        switch (requestType) {
            case QUERY_SELF_SELECT:
                setContentShow();

                break;

        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onRefresh() {
        queryIndexData();
    }

    @Override
    public void onLoadMore() {

    }
}
