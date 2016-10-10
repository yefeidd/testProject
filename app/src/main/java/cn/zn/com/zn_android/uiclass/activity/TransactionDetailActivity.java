package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.TransactionDetailAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.TransDetailBean;
import cn.zn.com.zn_android.model.bean.TransDetailListBean;
import cn.zn.com.zn_android.presenter.TransactionDetalPresenter;
import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.viewfeatures.TransactionDetailView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 模拟盘当前持仓，成交明细
 * Created by Jolly on 2016/9/9 0009.
 */
public class TransactionDetailActivity extends BaseMVPActivity<TransactionDetailView, TransactionDetalPresenter>
        implements TransactionDetailView, XListView.IXListViewListener {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_transaction_detail)
    XListView mXlvTransactionDetail;

    private TransactionDetailAdapter mAdapter;
    private ViewHolder headHolder;
    private List<TransDetailListBean> detailList = new ArrayList<>();
    private String title, code;
    private int page = 1, pageSize = 10;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public TransactionDetalPresenter initPresenter() {
        return new TransactionDetalPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_transaction_detail;
    }

    public void onEventMainThread(AnyEventType event) {
        title = event.getTid();
        code = event.getObject().toString();
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_transaction_detail_head, null, false);
        headHolder = new ViewHolder(headView);
        mXlvTransactionDetail.addHeaderView(headView);

        mToolbarTitle.setText(title);
        if (title.equals(getString(R.string.mnpdqcc))) {
            headHolder.mLl1.setVisibility(View.GONE);
            headHolder.mTvTitlePl.setText(R.string.last_market_value);
        } else {
            headHolder.mLl2.setVisibility(View.GONE);
            headHolder.mLl3.setVisibility(View.GONE);
            headHolder.mLl4.setVisibility(View.GONE);
        }

        mAdapter = new TransactionDetailAdapter(this, R.layout.item_transaction_detail, detailList);
        mXlvTransactionDetail.setAdapter(mAdapter);
        mXlvTransactionDetail.setXListViewListener(this);

        queryData();
    }

    private void queryData() {
        if (title.equals(getString(R.string.mnpdqcc))) {
            presenter.queryHold(_mApplication.getUserInfo().getSessionID(), code);
            presenter.queryHoldList(_mApplication.getUserInfo().getSessionID(), code, page, pageSize);
        } else {
            presenter.queryChangeList(_mApplication.getUserInfo().getSessionID(), code);
            presenter.queryChangeCodeList(_mApplication.getUserInfo().getSessionID(), code, page, pageSize);
        }
    }

    @Override
    protected void initEvent() {

    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    /**
     * 模拟盘当前持仓 头部
     * @param detailBean
     */
    private void updateHeadDetail(TransDetailBean detailBean) {
        if (null == detailBean) return;

        headHolder.mTvName.setText(detailBean.getCode_name());

        String lastPrice = String.format(getString(R.string.last_price), detailBean.getNew_index());
        SpannableString ss1 = new SpannableString(lastPrice);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_value_black)),
                0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headHolder.mTvLastPrice.setText(ss1);

        String chg;
        if (detailBean.getNew_zdf().startsWith("-")) {
            headHolder.mTvLastPrice.setTextColor(getResources().getColor(R.color.green_down));
            headHolder.mTvChg.setTextColor(getResources().getColor(R.color.green_down));
            chg = String.format(getString(R.string.new_zdf), detailBean.getNew_zdf() + "%");
        } else {
            headHolder.mTvLastPrice.setTextColor(getResources().getColor(R.color.app_bar_color));
            headHolder.mTvChg.setTextColor(getResources().getColor(R.color.app_bar_color));
            String zdf = "+" + detailBean.getNew_zdf() + "%";
            chg = String.format(getString(R.string.new_zdf), zdf);
        }
        SpannableString ss2 = new SpannableString(chg);
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_value_black)),
                0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headHolder.mTvChg.setText(ss2);

        headHolder.mTvPl.setText(detailBean.getNew_index_money());

        if (detailBean.getSyl().startsWith("-")) {
            headHolder.mTvGains.setText(detailBean.getSyl() + "%");
            headHolder.mTvGains.setTextColor(getResources().getColor(R.color.green_down));
            headHolder.mTvPl.setTextColor(getResources().getColor(R.color.green_down));
        } else {
            headHolder.mTvGains.setText("+" + detailBean.getSyl() + "%");
            headHolder.mTvGains.setTextColor(getResources().getColor(R.color.app_bar_color));
            headHolder.mTvPl.setTextColor(getResources().getColor(R.color.app_bar_color));
        }

        headHolder.mTvFloatingPl.setText(detailBean.getFdyk());
        headHolder.mTvCostPerShare.setText(detailBean.getCode_cb_price());
        headHolder.mTvPositionNum.setText(detailBean.getCode_num() + "");
        headHolder.mTvSellNum.setText(detailBean.getCode_change_num() + "");
        headHolder.mTvPosition.setText(detailBean.getCangwei() + "%");
        headHolder.mTvTime.setText(detailBean.getCode_start_time());

    }

    /**
     * 成交明细头部
     */
    private void updateRecordHead(TransDetailBean detailBean) {
        headHolder.mTvName.setText(detailBean.getCode_name());

        String lastPrice = String.format(getString(R.string.last_price), detailBean.getNew_price());
        SpannableString ss1 = new SpannableString(lastPrice);
        ss1.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_value_black)),
                0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headHolder.mTvLastPrice.setText(ss1);

//        String chg = String.format(getString(R.string.new_zdf), detailBean.getZdf());
//        SpannableString ss2 = new SpannableString(chg);
//        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_value_black)),
//                0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        if (detailBean.getZdf().startsWith("-")) {
//            headHolder.mTvLastPrice.setTextColor(getResources().getColor(R.color.green_down));
//            headHolder.mTvChg.setTextColor(getResources().getColor(R.color.green_down));
//            headHolder.mTvChg.setText(ss2 + "%");
//        } else {
//            headHolder.mTvLastPrice.setTextColor(getResources().getColor(R.color.app_bar_color));
//            headHolder.mTvChg.setTextColor(getResources().getColor(R.color.app_bar_color));
//            headHolder.mTvChg.setText("+" + ss2 + "%");
//        }

        String chg;
        if (detailBean.getZdf().startsWith("-")) {
            headHolder.mTvLastPrice.setTextColor(getResources().getColor(R.color.green_down));
            headHolder.mTvChg.setTextColor(getResources().getColor(R.color.green_down));
            chg = String.format(getString(R.string.new_zdf), detailBean.getZdf() + "%");
        } else {
            headHolder.mTvLastPrice.setTextColor(getResources().getColor(R.color.app_bar_color));
            headHolder.mTvChg.setTextColor(getResources().getColor(R.color.app_bar_color));
            String zdf = "+" + detailBean.getZdf() + "%";
            chg = String.format(getString(R.string.new_zdf), zdf);
        }
        SpannableString ss2 = new SpannableString(chg);
        ss2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.font_value_black)),
                0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        headHolder.mTvChg.setText(ss2);

        headHolder.mTvPl.setText(detailBean.getWin_lose());

        if (detailBean.getSy().startsWith("-")) {
            headHolder.mTvGains.setText(detailBean.getSy() + "%");
            headHolder.mTvGains.setTextColor(getResources().getColor(R.color.green_down));
            headHolder.mTvPl.setTextColor(getResources().getColor(R.color.green_down));
        } else {
            headHolder.mTvGains.setText("+" + detailBean.getSy() + "%");
            headHolder.mTvGains.setTextColor(getResources().getColor(R.color.app_bar_color));
            headHolder.mTvPl.setTextColor(getResources().getColor(R.color.app_bar_color));

        }

        headHolder.mTvBuyInAmount.setText(detailBean.getBuy_all_money());
        headHolder.mTvSellOutAmount.setText(detailBean.getSell_all_money());
    }

    @Override
    public void onSuccess(SimulativeBoardType requestType, Object object) {
        switch (requestType) {
            case QUERY_HOLD:
                TransDetailBean detailBean = (TransDetailBean) object;
                updateHeadDetail(detailBean);
                break;
            case QUERY_CHANGE_CODE_LIST:
            case QUERY_HOLD_LIST:
                List<TransDetailListBean> detailListBeens = (List<TransDetailListBean>) object;
                if (detailListBeens.size() == 0) {
                    mXlvTransactionDetail.setPullLoadEnable(false);
                    return;
                }
                if (mXlvTransactionDetail.ismPullRefreshing()) {
                    detailList.clear();
                    mXlvTransactionDetail.stopRefresh();
                }
                detailList.addAll(detailListBeens);
                mAdapter.notifyDataSetChanged();
                break;
            case QUERY_CHANGE_LIST:
                TransDetailBean dBean = (TransDetailBean) object;
                updateRecordHead(dBean);
                break;

        }

        mXlvTransactionDetail.stopLoadMore();

    }

    @Override
    public void onError(SimulativeBoardType requestType, Throwable t) {
        mXlvTransactionDetail.stopRefresh();
        mXlvTransactionDetail.stopLoadMore();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onRefresh() {
        page = 1;
        queryData();
        mXlvTransactionDetail.setPullLoadEnable(true);
    }

    @Override
    public void onLoadMore() {
        page++;
        if (title.equals(getString(R.string.mnpdqcc))) {
            presenter.queryHoldList(_mApplication.getUserInfo().getSessionID(), code, page, pageSize);
        } else {
            presenter.queryChangeCodeList(_mApplication.getUserInfo().getSessionID(), code, page, pageSize);
        }
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_last_price)
        TextView mTvLastPrice;
        @Bind(R.id.tv_chg)
        TextView mTvChg;
        @Bind(R.id.tv_title_pl)
        TextView mTvTitlePl;
        @Bind(R.id.tv_pl)
        TextView mTvPl;
        @Bind(R.id.tv_gains)
        TextView mTvGains;
        @Bind(R.id.tv_buy_in_amount)
        TextView mTvBuyInAmount;
        @Bind(R.id.tv_sell_out_amount)
        TextView mTvSellOutAmount;
        @Bind(R.id.ll_1)
        LinearLayout mLl1;
        @Bind(R.id.tv_cost_per_share)
        TextView mTvCostPerShare;
        @Bind(R.id.tv_floating_pl)
        TextView mTvFloatingPl;
        @Bind(R.id.ll_2)
        LinearLayout mLl2;
        @Bind(R.id.tv_position_num)
        TextView mTvPositionNum;
        @Bind(R.id.tv_sell_num)
        TextView mTvSellNum;
        @Bind(R.id.ll_3)
        LinearLayout mLl3;
        @Bind(R.id.tv_position)
        TextView mTvPosition;
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.ll_4)
        LinearLayout mLl4;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
