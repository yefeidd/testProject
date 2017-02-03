package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.FinishAnswerModel;
import cn.zn.com.zn_android.model.ListviewItemModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.activity.QuestionAnswerDetailActivity;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenu;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenuCreator;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenuItem;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenuListView;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;
import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/11/29 0029.
 * email: m15267280642@163.com
 * explain: 已经回答三个页面
 */

public class FinishProgenyFragment extends BaseMVPFragment<DiagnosedStockView, DiagnosesSocketPresenter> implements DiagnosedStockView, SwipeMenuListView.IXListViewListener {
    @Bind(R.id.sm_lv)
    SwipeMenuListView mSmLv;
    private int NOT_ANSWER = 1;
    private final static int PRE_ANSWER = 3;
    private int page = 0;
    private int num = 10;

    private List<ListviewItemModel> dataList = new ArrayList<>();
    //    @Bind(R.id.test)
//    TextView mTest;
    private int type = 0;//0 老师抢答 1 学员主动 2 精彩回答
    private ListViewAdapter adapter;
    private boolean isRefreshing = false;
    private TextView view; //点击的按钮
    private int currentItem = 0;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.type = (int) getArguments().get("type");
    }

    @Override
    public DiagnosesSocketPresenter initPresenter() {
        return new DiagnosesSocketPresenter(_mActivity, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.fragment_progeny_answer;
    }


    @Override
    protected void initEvent() {
        mSmLv.setXListViewListener(this);
        mSmLv.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        currentItem = position;
                        presenter.delAnswer(((FinishAnswerModel) dataList.get(position)).getBean().getId());
//                        // delete
//                        ToastUtil.show(_mActivity, "已删除", Toast.LENGTH_SHORT);
//                        dataList.remove(position);
//                        adapter.notifyDataSetChanged();
//                        break;
                }
                // false : close the menu; true : not close the menu
                return false;
            }
        });
        mSmLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentItem = position;
                if (position == 0) return;
                String questionId = ((FinishAnswerModel) dataList.get(position - 1)).getBean().getId();
                EventBus.getDefault().postSticky(new AnyEventType().setState(true).setTid(questionId)
                        .setType(Constants.COMMENT_UN));
                startActivity(new Intent(_mActivity, QuestionAnswerDetailActivity.class));
            }
        });
    }


    @Override
    protected void initView(View view) {
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        _mActivity.getApplicationContext());
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        _mActivity.getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(DensityUtil.dip2px(_mActivity, 90));
                // set a icon
                deleteItem.setTitle("删除");
                // set item title fontsize
                deleteItem.setTitleSize(18);
//                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };


// set creator
        mSmLv.setMenuCreator(creator);
        adapter = new ListViewAdapter(_mActivity, R.layout.item_finish_answer, dataList, "FinishAnswerHolder");
        mSmLv.setAdapter(adapter);

    }


    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }


    /**
     * 初始化数据
     */
    public void initData(boolean isRefreshing) {
        this.isRefreshing = isRefreshing;
        NOT_ANSWER = type + 1;
        int numCount = isRefreshing ? (page + 1) * num : num;
        int pageCount = isRefreshing ? 0 : page;
        presenter.queryFinishTeaAnswerList(PRE_ANSWER, NOT_ANSWER, pageCount, numCount, listener);
    }

    /**
     * 设置分页数据显示的方法
     *
     * @param list
     */
    public void setPageData(List<FinishAnswerModel> list) {
        updatListView();
        if (list.size() == 0 || list.size() < num) {
            mSmLv.setLoadMoreEnable(false);
            mSmLv.setLoadMoreEnableShow(false);
            mSmLv.setPullLoadEnable(false);
        } else {
            mSmLv.setLoadMoreEnable(true);
            mSmLv.setLoadMoreEnableShow(true);
            mSmLv.setPullLoadEnable(true);
        }
        dataList.addAll(list);
        adapter.setDataList(dataList);
        if (currentItem != 0) { //如果选中的条目不是0
            mSmLv.setSelection(currentItem);
            adapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新数据
     */
    public void notifyData() {
        dataList.clear();
        NOT_ANSWER = type + 1;
        presenter.queryFinishTeaAnswerList(PRE_ANSWER, NOT_ANSWER, 0, num * (page + 1), listener);
    }


    /**
     * 判断刷新状态的方法
     */
    private void updatListView() {

        if (mSmLv.ismPullRefreshing()) {
            mSmLv.stopRefresh();
            dataList.clear();
        }

        if (isRefreshing) {
            isRefreshing = false;
            dataList.clear();
        }

        mSmLv.stopLoadMore();

    }


    @Override
    public void onSuccess(DiagnosedType requestType, Object object) {
        switch (requestType) {
            case TEA_ANSWER_QUESTION: //老师回答列表
                setPageData((List<FinishAnswerModel>) object);
                break;
            case ADD_GOOD_ANSWER:      //添加精彩回答
                ToastUtil.show(_mActivity, (String) object, Toast.LENGTH_SHORT);
                addGoodAnswer();
                break;
            case CANCEL_GOOD_ANSWER:   //取消精彩回答
                ToastUtil.show(_mActivity, (String) object, Toast.LENGTH_SHORT);
                cancelGoodAnswer();
                break;
            case DEL_ANSWER:            //删除精彩回答
                ToastUtil.show(_mActivity, "删除成功", Toast.LENGTH_SHORT);
                notifyData();
                break;
        }
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {
//        if (requestType == DiagnosedType.TEA_ANSWER_QUESTION) {
//            Log.i(TAG, "onError: " + t);
//        }
//        switch (requestType) {
//            case TEA_ANSWER_QUESTION:
//                break;
//            case ADD_GOOD_ANSWER:
//                break;
//            case CANCEL_GOOD_ANSWER:
//                break;
//        }
        ToastUtil.show(_mActivity, t.getMessage(), Toast.LENGTH_SHORT);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData(true);
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onRefresh() {
        page = 0;
        isRefreshing = true;
        initData(isRefreshing);
    }

    @Override
    public void onLoadMore() {
        page++;
        initData(false);
    }


    /**
     * 设为精彩回答的方法(界面改变)
     */
    private void addGoodAnswer() {
        notifyData();
//        if (type == 2) {
//        } else {
//            view.setText(_mActivity.getString(R.string.cancel_perfect));
//        }
    }


    /**
     * 取消精彩回答的方法(界面改变)
     */

    private void cancelGoodAnswer() {
        notifyData();
//        if (type == 2) {
//            notifyData();
//        } else {
//            view.setText(_mActivity.getString(R.string.set_perfect));
//        }
    }


    /**
     * 监听取消或者设为精彩的按钮
     */
    private FinishAnswerModel.OnPerfectListener listener = new FinishAnswerModel.OnPerfectListener() {
        @Override
        public void onclick(int type, TextView view, int position, String id) {
            String ss = "设为精彩";
            if (ss.equals(view.getText().toString())) {
                presenter.addGoodAnswer(id);
                currentItem = position;
                FinishProgenyFragment.this.view = view;
//                ToastUtil.show(_mActivity, "设置成功", Toast.LENGTH_SHORT);
//                view.setText(_mActivity.getString(R.string.cancel_perfect));
            } else {
//                ToastUtil.show(_mActivity, "取消成功", Toast.LENGTH_SHORT);
//                view.setText(_mActivity.getString(R.string.set_perfect));
                presenter.cancelGoodAnswer(id);
                currentItem = position;
                FinishProgenyFragment.this.view = view;
            }
        }
    };


}
