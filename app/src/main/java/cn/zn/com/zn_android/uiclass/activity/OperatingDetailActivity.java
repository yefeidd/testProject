package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.ActionDetailModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OperateDetailBean;
import cn.zn.com.zn_android.model.bean.OperateDetailEntity;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/29 0029.
 * email: m15267280642@163.com
 * explain:
 */

public class OperatingDetailActivity extends BaseActivity implements XListView.IXListViewListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.xlv_fy_list)
    XListView mXlvFyList;
    private List<ActionDetailModel> dataList;
    private ListViewAdapter actionDetailAdapter;
    private int page = 0;
    private String userId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_earnings_ranking);
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

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            userId = (String) event.getObject();
        }
    }

    @Override
    protected void initView() {
        dataList = new ArrayList<>();
        page = 0;
        mToolbarTitle.setText("操作明细");
        View headView = LayoutInflater.from(this).inflate(R.layout.item_action_detail_dialog_head, new ListView(this), false);
        mXlvFyList.addHeaderView(headView);
        dataList = new ArrayList<>();
        actionDetailAdapter = new ListViewAdapter(this, R.layout.item_action_detail_dialog, dataList, "ActionDetailViewHolder");
        mXlvFyList.setAdapter(actionDetailAdapter);
        setData(page++);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(v -> {
            finish();
        });
        mXlvFyList.setXListViewListener(this);

    }


    public void setData(int pageNum) {
        AppObservable.bindActivity(this, _apiManager.getService().queryOperateList(_mApplication.getUserInfo().getSessionID(), userId, pageNum))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultOperateList, throwable -> {
                    Log.e(TAG, "queryOperateList: ", throwable);
                    mXlvFyList.stopRefresh();
                    mXlvFyList.stopLoadMore();
                });
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void resultOperateList(ReturnValue<OperateDetailEntity> retValue) {
        mXlvFyList.stopLoadMore();
        mXlvFyList.stopRefresh();
        if (null != retValue && null != retValue.getData()) {
            OperateDetailEntity entity = retValue.getData();
            List<OperateDetailBean> list = entity.getPosition();
            for (OperateDetailBean bean : list) {
                ActionDetailModel model = new ActionDetailModel(this, bean);
                dataList.add(model);
            }
            if (dataList.size() == 0) {
                mXlvFyList.setLoadMoreEnableShow(false);
            }
            actionDetailAdapter.setDataList(dataList);
            actionDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh() {
        setData(page++);
    }

    @Override
    public void onLoadMore() {

    }
}
