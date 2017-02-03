package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.ExcellentAnswerModel;
import cn.zn.com.zn_android.model.bean.ExcellentAnswerBean;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 精彩回答列表
 * Created by Jolly on 2016/11/29.
 */

public class ExcellentAnswerListActivity extends BaseActivity implements XListView.IXListViewListener {
    @Bind(R.id.xlv_answers)
    XListView mXlvAnswers;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;

    private int page = 0;
    private String num = "5";
    private List<ExcellentAnswerModel> modelList = new ArrayList<>();
    private ListViewAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_excellent_answer_list);
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
        mToolbarTitle.setText(R.string.accellent_answer);
        adapter = new ListViewAdapter(this, R.layout.item_excellent_answer, modelList,
                "ExcellentAnswerHolder");
        mXlvAnswers.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        mXlvAnswers.setXListViewListener(this);
    }

    @Override
    protected void initData() {
        excellentAnswerList();
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    public List<ExcellentAnswerModel> getExcellentAnswer(List<ExcellentAnswerBean> list) {
        if (mXlvAnswers.ismPullRefreshing()) {
            modelList.clear();
        }
        for (ExcellentAnswerBean bean : list) {
            ExcellentAnswerModel model = new ExcellentAnswerModel(bean.getSname() + "（" + bean.getScode() + "）" + bean.getPdescription(),
                    bean.getAnswer_info(), bean.getAvatars(), bean.getNickname(), bean.getHtime(), bean.getId(), bean.getTid());
            modelList.add(model);
        }
        return modelList;
    }

    /**
     * 首页精彩回答
     */
    private void excellentAnswerList() {
        _apiManager.getService().excellentAnswerList(_mApplication.getUserInfo().getSessionID(), page + "", num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(retValue -> Observable.just(getExcellentAnswer(retValue.getData())))
                .subscribe(retValue -> {
                    adapter.notifyDataSetChanged();
                    mXlvAnswers.stopRefresh();
                    mXlvAnswers.stopLoadMore();
                }, throwable -> {
                    Log.e(TAG, "excellentAnswerList: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().excellentAnswerList(_mApplication.getUserInfo().getSessionID(), page + "", num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(retValue -> Observable.just(getExcellentAnswer(retValue.getData())))
//                .subscribe(retValue -> {
//                    adapter.notifyDataSetChanged();
//                    mXlvAnswers.stopRefresh();
//                    mXlvAnswers.stopLoadMore();
//                }, throwable -> {
//                    Log.e(TAG, "excellentAnswerList: ", throwable);
//                });
    }

    @Override
    public void onRefresh() {
        page = 0;
        excellentAnswerList();
    }

    @Override
    public void onLoadMore() {
        page ++;
        excellentAnswerList();
    }
}
