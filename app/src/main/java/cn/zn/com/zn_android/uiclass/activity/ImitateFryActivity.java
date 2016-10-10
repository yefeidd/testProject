package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ImitateFryAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.ImitateFryModel;
import cn.zn.com.zn_android.model.bean.ImitateFryBean;
import cn.zn.com.zn_android.model.bean.ImitateFryItemBean;
import cn.zn.com.zn_android.presenter.ImitateFryPresenter;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;
import cn.zn.com.zn_android.uiclass.customerview.CanvasChartView;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.viewfeatures.ImitateFryView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 模拟炒股
 * Created by Jolly on 2016/9/8 0008.
 */
public class ImitateFryActivity extends BaseMVPActivity<ImitateFryView, ImitateFryPresenter>
        implements ImitateFryView, View.OnClickListener, XListView.IXListViewListener {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.xlv_imitate)
    XListView mXlvImitate;

    private ImitateFryAdapter mAdapter;
    private ViewHolder mHolder;
    private int page = 0;
    private List<ImitateFryModel> userPositonList = new ArrayList<>();
    private ImitateFryBean fryBean = null;


    /**
     * 分享的平台数组
     */
    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };
    private String shareContent = Constants.simulateShareContent;
    private String shareTitle = Constants.simulateShareTitle;
    private String mUrl = Constants.simulateShareUrl;
    UMImage image = new UMImage(ImitateFryActivity.this, Constants.iconResourece);
    private RadioButton minSBtn;
    private RadioButton dayKBtn;
    private PresentScorePresenter sharepresenter;

    /**
     * 三方平台的分享
     */
    private void societyShare() {
        setDialog();
        new ShareAction(this).setDisplayList(displaylist)
                .withText(shareContent)
                .withTitle(shareTitle)
                .withTargetUrl(mUrl)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
        //关闭log和toast
        Config.OpenEditor = false;
//        Log.LOG = false;
        Config.IsToastTip = false;
        Config.dialog.dismiss();

    }

    /**
     * 设置三方跳转的时候的dialog
     */
    private void setDialog() {
        JoDialog dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        Config.dialog = dialog;
    }


    /**
     * 分享的监听器
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(ImitateFryActivity.this, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(ImitateFryActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            Toast.makeText(ImitateFryActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
    public ImitateFryPresenter initPresenter() {
        return new ImitateFryPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_imitate_fry;
    }

    @Override
    protected void initView() {
        sharepresenter = new PresentScorePresenter(this);
        mToolbarTitle.setText(R.string.analog);
        mIbSearch.setImageResource(R.mipmap.ic_share);
        mIbSearch.setVisibility(View.VISIBLE);
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_imitate_fry_head, null, false);
        mHolder = new ViewHolder(headView);
        mHolder.mBtnBuyIn.setOnClickListener(this);
        mHolder.mBtnRecords.setOnClickListener(this);
        mXlvImitate.addHeaderView(headView);

        mAdapter = new ImitateFryAdapter(this, R.layout.item_imitate_fry, userPositonList);
        mXlvImitate.setAdapter(mAdapter);

    }

    protected void initData() {
        presenter.queryUserPosition(_mApplication.getUserInfo().getSessionID(), page + "");
        presenter.queryImitateFry(_mApplication.getUserInfo().getSessionID());

    }

    @Override
    protected void initEvent() {
        mIbSearch.setOnClickListener(this);
        mXlvImitate.setXListViewListener(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_buy_in:
                startActivity(new Intent(this, BuySearchActivity.class));
                break;
            case R.id.btn_records:
                if (null != fryBean) {
                    startActivity(new Intent(this, StockRecordActivity.class));
                }
                break;

            case R.id.ib_search:
                societyShare();
                break;
        }
    }

    private void resultUser(List<ImitateFryItemBean> list) {
        if (list.size() == 0) {
            mXlvImitate.setPullLoadEnable(false);
            return;
        }

        for (ImitateFryItemBean bean : list) {
            ImitateFryModel model = new ImitateFryModel(this, bean);
            userPositonList.add(model);
        }

        mAdapter.notifyDataSetChanged();
    }

    private void updateHead(ImitateFryBean fryBean) {
        this.fryBean = fryBean;
        mHolder.mTvNetValue.setText(fryBean.getNet_worth());
        if (!fryBean.getAll_income().equals("")) {
            mHolder.mTvTotalGains.setText(fryBean.getAll_income() + "%");
        }
        mHolder.mTvRank.setText(fryBean.getRanking());
        mHolder.mTvTotalAssets.setText(fryBean.getTotal_money());
        mHolder.mTvExpendableFund.setText(fryBean.getUsable_money());
        mHolder.mTvInitialCapital.setText(fryBean.getInitial_money());
        mHolder.mTvDayBeak.setText(fryBean.getToday_win());
        if (fryBean.getMon_income().startsWith("-")) {
            mHolder.mTvMonthGainRatio.setText(fryBean.getMon_income() + "%");
            mHolder.mTvMonthGainRatio.setTextColor(getResources().getColor(R.color.green_down));
        } else if (!fryBean.getMon_income().equals("")) {
            mHolder.mTvMonthGainRatio.setText("+" + fryBean.getMon_income() + "%");
            mHolder.mTvMonthGainRatio.setTextColor(getResources().getColor(R.color.app_bar_color));
        }
        if (!fryBean.getWin_rate().equals("")) {
            mHolder.mWinRatio.setText(fryBean.getWin_rate() + "%");
        }
        if (!fryBean.getNow_position().equals("")) {
            mHolder.mTvCurrentPosition.setText(fryBean.getNow_position() + "%");
        }
        mHolder.mTvAvarageStockOwnership.setText(fryBean.getAverage_position());
        mHolder.mTvAvarageMonthly.setText(fryBean.getMon_ave_trans());
        mHolder.mTvInitialTransaction.setText(fryBean.getFirst_trans());
        mHolder.mTvLastTransaction.setText(fryBean.getLast_trans());

        mHolder.mCcvChat.setData(fryBean.getLine_chart().getList(),
                fryBean.getLine_chart().getStart_time(),
                fryBean.getLine_chart().getEnd_time());
    }

    @Override
    public void onSuccess(SimulativeBoardType requestType, Object object) {
        switch (requestType) {
            case USER_POSITION:
                if (mXlvImitate.ismPullRefreshing()) {
                    userPositonList.clear();
                    mXlvImitate.stopRefresh();
                }
                mXlvImitate.stopLoadMore();
                List<ImitateFryItemBean> userPositions = (List<ImitateFryItemBean>) object;
                resultUser(userPositions);
                break;
            case QUERY_IMITATE_FRY:
                ImitateFryBean fryBean = (ImitateFryBean) object;
                updateHead(fryBean);
                break;
        }

    }

    @Override
    public void onError(SimulativeBoardType requestType, Throwable t) {
        mXlvImitate.stopLoadMore();
        mXlvImitate.stopRefresh();
    }

    @Override
    public void onRefresh() {
        page = 0;
        initData();
        mXlvImitate.setPullLoadEnable(true);
    }

    @Override
    public void onLoadMore() {
        page++;
        initData();
    }

    static class ViewHolder {
        @Bind(R.id.tv_net_value)
        TextView mTvNetValue;
        @Bind(R.id.tv_total_gains)
        TextView mTvTotalGains;
        @Bind(R.id.tv_rank)
        TextView mTvRank;
        @Bind(R.id.tv_total_assets)
        TextView mTvTotalAssets;
        @Bind(R.id.tv_expendable_fund)
        TextView mTvExpendableFund;
        @Bind(R.id.tv_initial_capital)
        TextView mTvInitialCapital;
        @Bind(R.id.tv_day_beak)
        TextView mTvDayBeak;
        @Bind(R.id.btn_buy_in)
        Button mBtnBuyIn;
        @Bind(R.id.btn_records)
        Button mBtnRecords;
        @Bind(R.id.tv_month_gain_ratio)
        TextView mTvMonthGainRatio;
        @Bind(R.id.win_ratio)
        TextView mWinRatio;
        @Bind(R.id.tv_current_position)
        TextView mTvCurrentPosition;
        @Bind(R.id.tv_avarage_stock_ownership)
        TextView mTvAvarageStockOwnership;
        @Bind(R.id.tv_avarage_monthly)
        TextView mTvAvarageMonthly;
        @Bind(R.id.tv_initial_transaction)
        TextView mTvInitialTransaction;
        @Bind(R.id.tv_last_transaction)
        TextView mTvLastTransaction;
        @Bind(R.id.ccv_chat)
        CanvasChartView mCcvChat;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
