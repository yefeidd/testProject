package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.OnlineTeacherModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.bean.OnlineTeacherBean;
import cn.zn.com.zn_android.presenter.OnlineTeacherRankingPresenter;
import cn.zn.com.zn_android.presenter.requestType.OnLineTeacherRankingType;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.viewfeatures.OnlineTeacherRankingView;
import de.greenrobot.event.EventBus;

/**
 * 精英投顾排行榜
 * Created by Jolly on 2016/11/29.
 */
public class OnlineTeacherRankingActivity extends BaseMVPActivity<OnlineTeacherRankingView, OnlineTeacherRankingPresenter>
        implements OnlineTeacherRankingView, View.OnClickListener, XListView.IXListViewListener,
        AdapterView.OnItemClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.tv_syn_sort)
    TextView mTvSynSort;
    @Bind(R.id.tv_count)
    TextView mTvCount;
    @Bind(R.id.tv_favo_comm)
    TextView mTvFavoComm;
    @Bind(R.id.xlv_online)
    XListView mXlvOnline;

    private int type = 1; // 1:综合排序2:数量排序3:好评排序
    private String sort = "2"; // 1为升序2位降序
    private int page = 0;
    private int num = 10;
    private JoDialog dialog;

    private List<OnlineTeacherModel> synSortList = new ArrayList<>();
    private ListViewAdapter synSortAdapter;

    @Override
    protected void initView() {
        presenter.attach(this);
        mToolbarTitle.setText(R.string.online_teacher_ranking);
        mIbSearch.setVisibility(View.VISIBLE);

        synSortAdapter = new ListViewAdapter(this, R.layout.item_online_teacher_ranking,
                synSortList, "OnlineTeacherRankingHolder");
        mXlvOnline.setAdapter(synSortAdapter);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mIbSearch.setOnClickListener(this);
        mTvSynSort.setOnClickListener(this);
        mTvCount.setOnClickListener(this);
        mTvFavoComm.setOnClickListener(this);
//        mXlvOnline.setAutoLoadEnable(false);
        mXlvOnline.setXListViewListener(this);
        mXlvOnline.setOnItemClickListener(this);
    }

    @Override
    protected void initData() {
        presenter.queryDiagnoseTeacherList(type, sort, page, num + "");
    }

    @Override
    public OnlineTeacherRankingPresenter initPresenter() {
        return new OnlineTeacherRankingPresenter(this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_teacher_online_ranking;
    }

    @Override
    public void showLoading() {
        if (dialog != null) {
            dialog.show();
        } else {
            dialog = new JoDialog.Builder(this)
                    .setViewRes(R.layout.layout_loading)
                    .setBgRes(Color.TRANSPARENT)
                    .show(false);
        }
    }

    @Override
    public void hideLoading() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.ib_search:
                EventBus.getDefault().postSticky(new AnyEventType(Constants.VIDEO));
                startActivity(new Intent(this, ArticleSearchActivity.class));
                break;
            case R.id.tv_syn_sort:
                mTvSynSort.setTextColor(getResources().getColor(R.color.app_bar_color));
                mTvCount.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvFavoComm.setTextColor(getResources().getColor(R.color.font_value_black));
                sort = type == 1 ? (sort.equals("1") ? "2" : "1") : "2";
                type = 1;
                synSortList.clear();
                initData();
                break;
            case R.id.tv_count:
                mTvSynSort.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvCount.setTextColor(getResources().getColor(R.color.app_bar_color));
                mTvFavoComm.setTextColor(getResources().getColor(R.color.font_value_black));
                sort = type == 2 ? (sort.equals("1") ? "2" : "1") : "2";
                type = 2;
                synSortList.clear();
                initData();
                break;
            case R.id.tv_favo_comm:
                mTvSynSort.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvCount.setTextColor(getResources().getColor(R.color.font_value_black));
                mTvFavoComm.setTextColor(getResources().getColor(R.color.app_bar_color));
                sort = type == 3 ? (sort.equals("1") ? "2" : "1") : "2";
                type = 3;
                synSortList.clear();
                initData();
                break;
        }
    }

    private void getSynSort(List<OnlineTeacherBean> list) {
        if (mXlvOnline.ismPullRefreshing()) {
            synSortList.clear();
        }
        mXlvOnline.stopRefresh();
        mXlvOnline.stopLoadMore();
        if (list.size() < num) {
            mXlvOnline.setLoadMoreEnable(false);
            mXlvOnline.setLoadMoreEnableShow(false);
        } else {
            mXlvOnline.setLoadMoreEnable(true);
            mXlvOnline.setLoadMoreEnableShow(true);
        }
        for (int i = 0; i < list.size(); i++) {
            OnlineTeacherBean bean = list.get(i);

            OnlineTeacherModel model = new OnlineTeacherModel(bean, i + 1, type);
            synSortList.add(model);
        }
        synSortAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSuccess(OnLineTeacherRankingType requestType, Object object) {
        switch (requestType) {
            case QUERY_TEACHER_LIST:
                List<OnlineTeacherBean> list = (List<OnlineTeacherBean>) object;
                getSynSort(list);
                break;
        }
    }

    @Override
    public void onError(OnLineTeacherRankingType requestType, Throwable t) {

    }

    @Override
    public void onRefresh() {
        page = 0;
        initData();
    }

    @Override
    public void onLoadMore() {
        page++;
        initData();
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (position == 0) return;
        if (position >= synSortList.size()) {
            onLoadMore();
            return;
        }

        HotLiveBean hotLiveBean = new HotLiveBean();
        hotLiveBean.setTid(synSortList.get(position - 1).getBean().getTid());
        hotLiveBean.setAvatars(synSortList.get(position - 1).getBean().getHeadimg());
        hotLiveBean.setTitle(synSortList.get(position - 1).getBean().getNickname());
        hotLiveBean.setRoom_number("");
        hotLiveBean.setCollect("");
        hotLiveBean.setClick("");
        hotLiveBean.setPlacard("");
        hotLiveBean.setCurrentItem(2);
        EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
        startActivity(new Intent(this, TeacherLiveActivity.class));
    }
}
