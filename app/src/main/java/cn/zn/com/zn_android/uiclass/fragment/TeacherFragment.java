package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.HostLiveAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.activity.ArticleSearchActivity;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 老师 Fragment
 * <p>
 * Created by WJL on 2016/3/11 0011 09:01.
 */
public class TeacherFragment extends BaseFragment implements View.OnClickListener, XListView.IXListViewListener {
    @Bind(R.id.lv_live)
    XListView mLvLive;
//    @Bind(R.id.srl_teachers)
//    InterceptSwpRefLayout mSrlTeachers;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;

    private List<HotLiveBean> hotLiveList = new ArrayList<>();
    private HostLiveAdapter mHotLiveAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        _setLayoutRes(R.layout.fragment_teacher);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        /**
         * 初始化listView控件
         */
        mHotLiveAdapter = new HostLiveAdapter(hotLiveList, getActivity());
        mLvLive.setAdapter(mHotLiveAdapter);
        mLvLive.setPullLoadEnable(false);
        mLvLive.setPullRefreshEnable(true);
        mLvLive.setDragLoadEnable(false);
//        postHotLiveRequest();
        mIvLeftmenu.setVisibility(View.GONE);
        mToolbarTitle.setText(getString(R.string.teacher));
        mIbSearch.setVisibility(View.VISIBLE);
        mIbSearch.setImageResource(R.drawable.iv_search);
        mIbSearch.setOnClickListener(this);
    }

    @Override
    protected void initEvent() {
        /**
         * 数据刷新的接口
         */
        mLvLive.setXListViewListener(this);
//        mSrlTeachers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                postHotLiveRequest();
//                mSrlTeachers.setRefreshing(false);
//            }
//        });
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("TeacherFragment"); //统计页面，"MainScreen"为页面名称，可自定义

        if (hotLiveList.size() == 0) {
            postHotLiveRequest();
        }
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TeacherFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);

    }

    /**
     * 发送请求老师直播列表请求
     */
    private void postHotLiveRequest() {
        AppObservable.bindFragment(this, _apiManager.getService().getHotLiveList("")).
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultLiveList, throwable -> {
                    Log.e(TAG, "postHotLiveRequest: 异常", throwable);
                    mLvLive.stopRefresh();
//                    if (mSrlTeachers.isRefreshing()) {
//                        mSrlTeachers.setRefreshing(false);
//                    }
                });
    }

    private void resultLiveList(ReturnListValue<HotLiveBean> returnValue) {
//        if (mSrlTeachers.isRefreshing()) {
//            mSrlTeachers.setRefreshing(false);
//        }
        mLvLive.stopRefresh();

        hotLiveList = new ArrayList<>();
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            hotLiveList = returnValue.getData();
            if (hotLiveList != null) {
                mHotLiveAdapter.setHostLiveList(hotLiveList);
            } else {
                ToastUtil.showShort(_mContext, returnValue.getMsg());
            }
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_search:
                EventBus.getDefault().postSticky(new AnyEventType(Constants.VIDEO));
                startActivity(new Intent(getActivity(), ArticleSearchActivity.class));
                break;
        }
    }

    @Override
    public void onRefresh() {
        postHotLiveRequest();
    }

    @Override
    public void onLoadMore() {

    }
}
