package cn.zn.com.zn_android.uiclass.activity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.ExchangeRecordModel;
import cn.zn.com.zn_android.model.OperateDetailModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.OtherHomeMsgBean;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.presenter.TaPresenter;
import cn.zn.com.zn_android.uiclass.customerview.ShareDialogBuilder;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.BaseMvpView;
import de.greenrobot.event.EventBus;

/**
 * Ta的主页
 * Created by Jolly on 2016/9/13 0013.
 */
public class TaActivity extends BaseMVPActivity<BaseMvpView, TaPresenter> implements BaseMvpView,
        XListView.IXListViewListener {
    private final String STATUS_KEY = "share_status";

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_record)
    XListView mXlvRecord;

//    private List<OperateDetailModel> operateDetailList = new ArrayList<>();
    private List<ExchangeRecordModel> recordList = new ArrayList<>();
    private ListViewAdapter recordAdapter;
    private final static int head = 0;
    private final static int detail = 1;
    private final static int history = 2;
    private final static int focus = 3;
    private final static int unfocus = 4;
    private OtherHomeMsgBean otherHomeMsgBean;
    private String userId;
    private int page = 1, pageSize = 10;
    private ViewHolder headHolder;
    private Dialog dialog;
    private ShareDialogBuilder builder;
    private String curDate;
    private String saveDate;
    private boolean flag = true;
    private PresentScorePresenter sharepresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
    }

    public void onEventMainThread(AnyEventType event) {

        if (null != event && null != event.getStockCode()) {
            userId = event.getStockCode();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public TaPresenter initPresenter() {
        return new TaPresenter(this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_ta;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
        sharepresenter = new PresentScorePresenter(this);
        mToolbarTitle.setText(getString(R.string.ta_page));
        builder = ShareDialogBuilder.getInstance();
        View headView = LayoutInflater.from(this).inflate(R.layout.layout_ta_head, null, false);
        headHolder = new ViewHolder(headView);
        mXlvRecord.addHeaderView(headView);

        headHolder.mTvAddFocus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    if (headHolder.mTvAddFocus.getText().equals(getString(R.string.add_focus))) {
                        presenter.attentionOther(_mApplication.getUserInfo().getSessionID(), userId);
                    } else {
                        presenter.unsetConcern(_mApplication.getUserInfo().getSessionID(), userId);
                    }
                } else {
                    startActivity(new Intent(_Activity, LoginActivity.class));
                }
            }
        });

        headHolder.mTvDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                curDate = DateUtils.getStringDate(System.currentTimeMillis(), "yyyy-MM-dd");
                saveDate = _mApplication.getSpfHelper().getData(STATUS_KEY);

                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    if (curDate.equals(saveDate)) {
                        EventBus.getDefault().postSticky(new AnyEventType(userId));
                        startActivity(new Intent(_Activity, OperatingDetailActivity.class));
                    } else {
                        showShareDialog();
                    }
                } else {
                    startActivity(new Intent(_Activity, LoginActivity.class));
                }

            }
        });

        recordAdapter = new ListViewAdapter(this, R.layout.item_ta_exchange_record,
                recordList, "TaRecordViewHolder");
        mXlvRecord.setAdapter(recordAdapter);
        mXlvRecord.setXListViewListener(this);
    }

    @Override
    protected void initEvent() {
        presenter.attach(this);
    }

    @OnClick({R.id.iv_leftmenu})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
        }
    }


    @Override
    protected void initData() {
        presenter.requestHeadDate(_mApplication.getUserInfo().getSessionID(), userId);
        presenter.requestHistoryDate(userId, page, pageSize);
    }

    private void setData() {

        if (otherHomeMsgBean != null && otherHomeMsgBean.getImg() != null) {
            headHolder.mSdvAvatar.setImageURI(Uri.parse(otherHomeMsgBean.getImg()));
        }
        headHolder.mTvName.setText(otherHomeMsgBean.getNickname());
        if (2 == otherHomeMsgBean.getAttention_type()) {
            headHolder.mTvAddFocus.setText(R.string.finish_focus);
//            headHolder.mTvAddFocus.setClickable(false);
        } else {
            headHolder.mTvAddFocus.setText(R.string.add_focus);
//            headHolder.mTvAddFocus.setClickable(true);
        }
        headHolder.mTvSelfSelect.setText(otherHomeMsgBean.getCode_count() + "\n" + getResources().getString(R.string.self_choose));
        headHolder.mTvFocus.setText(otherHomeMsgBean.getConcern_num() + "\n" + getResources().getString(R.string.foucus_num));
        headHolder.mTvPopularity.setText(otherHomeMsgBean.getAttention_num() + "\n" + getResources().getString(R.string.concern_num));
        headHolder.mTvInitialCapital.setText(otherHomeMsgBean.getJoin_money());

        headHolder.mTvTotalProfitRate.setText(otherHomeMsgBean.getAll_yl() + "%");
        if (otherHomeMsgBean.getAll_yl() != null && otherHomeMsgBean.getAll_yl().startsWith("-")) {
            headHolder.mTvTotalProfitRate.setTextColor(getResources().getColor(R.color.green_down));
        } else {
            headHolder.mTvTotalProfitRate.setTextColor(getResources().getColor(R.color.app_bar_color));
        }

        headHolder.mTvAverageProfit.setText(otherHomeMsgBean.getSy() + "%");
        if (otherHomeMsgBean.getSy() != null && otherHomeMsgBean.getSy().startsWith("-")) {
            headHolder.mTvAverageProfit.setTextColor(getResources().getColor(R.color.green_down));
        } else {
            headHolder.mTvAverageProfit.setTextColor(getResources().getColor(R.color.app_bar_color));
        }
        headHolder.mTvJoinTime.setText(otherHomeMsgBean.getTime());

    }


    /**
     * 分享的时候的dialog
     */
    private void showShareDialog() {
        builder.setActivity(this);
        builder.setUmShareListener(shareListener);
        dialog = builder.getDialog();
//        builder.setIsWidthFull(true);
        dialog.show();
    }

    private UMShareListener shareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
            Toast.makeText(_Activity, platform + " 分享成功", Toast.LENGTH_SHORT).show();
            flag = false;
            dialog.dismiss();
            curDate = DateUtils.getStringDate(System.currentTimeMillis(), "yyyy-MM-dd");
            _mApplication.getSpfHelper().saveData(STATUS_KEY, curDate);
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
            Toast.makeText(_Activity, platform + " 分享失败", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
            if (flag) {
                Toast.makeText(_Activity, platform + " 分享取消", Toast.LENGTH_SHORT).show();
            } else {
                flag = true;
            }
            dialog.dismiss();
        }
    };


    @Override
    public void onSuccess(int type, Object object) {
        switch (type) {
            case head:
                otherHomeMsgBean = (OtherHomeMsgBean) object;
                setData();
                break;
            case detail:
                break;
            case history:
                if (mXlvRecord.ismPullRefreshing()) {
                    recordList.clear();
                    mXlvRecord.stopRefresh();
                }
                mXlvRecord.stopLoadMore();
                recordList.addAll((List<ExchangeRecordModel>) object);
                if (recordList.size() == 0) {
                    mXlvRecord.setPullLoadEnable(false);
                }
                recordAdapter.setDataList(recordList);
                break;
            case focus:
                MessageBean msgBean = (MessageBean) object;
                ToastUtil.showShort(this, msgBean.getMessage());
                if (!msgBean.getMessage().contains("登录")) {
                    if (msgBean.getMessage().contains("成功")) {
                        headHolder.mTvAddFocus.setText(getString(R.string.finish_focus));
                    }
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case unfocus:
                MessageBean msgBean1 = (MessageBean) object;
                ToastUtil.showShort(this, msgBean1.getMessage());
                if (!msgBean1.getMessage().contains("登录")) {
                    if (msgBean1.getMessage().contains("成功")) {
                        headHolder.mTvAddFocus.setText(getString(R.string.add_focus));
                    }
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
        }

    }

    @Override
    public void onError(int Type, Throwable t) {
        switch (Type) {
            case history:
                mXlvRecord.stopLoadMore();
                mXlvRecord.stopRefresh();
                break;
        }
    }

    /**
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public void onRefresh() {
        page = 1;
        initData();
        mXlvRecord.setPullLoadEnable(true);
    }

    @Override
    public void onLoadMore() {
        page++;
        initData();
    }

    static class ViewHolder {
        @Bind(R.id.sdv_avatar)
        SimpleDraweeView mSdvAvatar;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_add_focus)
        TextView mTvAddFocus;
        @Bind(R.id.tv_self_select)
        TextView mTvSelfSelect;
        @Bind(R.id.tv_focus)
        TextView mTvFocus;
        @Bind(R.id.tv_popularity)
        TextView mTvPopularity;
        @Bind(R.id.tv_initial_capital)
        TextView mTvInitialCapital;
        @Bind(R.id.tv_total_profit_rate)
        TextView mTvTotalProfitRate;
        @Bind(R.id.tv_average_profit)
        TextView mTvAverageProfit;
        @Bind(R.id.tv_join_time)
        TextView mTvJoinTime;
        @Bind(R.id.tv_detail)
        TextView mTvDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
