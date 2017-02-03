package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.QuestionRollRVAdapter;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.ExcellentAnswerModel;
import cn.zn.com.zn_android.model.OnlineTeacherGVModel;
import cn.zn.com.zn_android.model.bean.DiagnoseUserIndexBean;
import cn.zn.com.zn_android.model.bean.ExcellentAnswerBean;
import cn.zn.com.zn_android.model.bean.QustionRollBean;
import cn.zn.com.zn_android.presenter.DianosedStockPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.NoScrollGridView;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import cn.zn.com.zn_android.uiclass.SmartLinearLayout;
import cn.zn.com.zn_android.uiclass.customerview.ObservableScrollListener;
import cn.zn.com.zn_android.uiclass.customerview.ObservableScrollView;
import cn.zn.com.zn_android.uiclass.xlistview.XScrollView;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 诊股大厅（用户）
 * Created by Jolly on 2016/11/24.
 */
public class DianosedStockActivity extends BaseMVPActivity<DiagnosedStockView, DianosedStockPresenter>
        implements DiagnosedStockView, View.OnClickListener, ObservableScrollView.OnScrollToBottomListener,
        ObservableScrollListener, XScrollView.IXScrollViewListener {

    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.xslv_dianosed)
    ObservableScrollView mXslvDianosed;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.btn_quick_ask)
    ImageButton mBtnQuickAsk;
    @Bind(R.id.sll_quick)
    SmartLinearLayout mSllQuick;

    private ViewHolder mHolder;
    private List<QustionRollBean> questionList = new ArrayList<>();
    private QuestionRollRVAdapter questionAdapter;
    private Subscription timerSubscription;    // 计时

    @Override
    protected void initView() {
        presenter.attach(this);
        mSllQuick.hide();
        toolbarTitle.setText(R.string.diagnoseSocketOffice);
        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_dianosed_stock, null);
        mHolder = new ViewHolder(contentView);
        mXslvDianosed.setView(contentView);

        mHolder.mRvQuestion.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        Log.e(TAG, "onTouch: ACTION_DOWN");

                    case MotionEvent.ACTION_MOVE:
                        if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
                            timerSubscription.unsubscribe();
                            timerSubscription = null;
                            Log.e(TAG, "onTouch: ACTION_MOVE");
                        }
                        break;

                    case MotionEvent.ACTION_CANCEL:
//                        Log.e(TAG, "onTouch: ACTION_CANCEL");
                        if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
                            timerSubscription.unsubscribe();
                            timerSubscription = null;
                            Log.e(TAG, "onTouch: ACTION_CANCEL");
                        } else if (questionList.size() > 4){
                            Log.e(TAG, "onTouch: ACTION_CANCEL");
                            startCountDown();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (questionList.size() > 4) {
                            Log.e(TAG, "onTouch: ACTION_UP");
                            startCountDown();
                        }
                        break;
                    default:
                        Log.e(TAG, "onTouch: default");
                        break;
                }

                return false;
            }
        });
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mBtnQuickAsk.setOnClickListener(this);

        mHolder.mLlQuickAsk.setOnClickListener(this);
        mHolder.mTvMoreAnswer.setOnClickListener(this);
        mHolder.mTvMoreTeacher.setOnClickListener(this);

        mXslvDianosed.setAutoLoadEnable(false);
        mXslvDianosed.setPullRefreshEnable(true);
        mXslvDianosed.setPullLoadEnable(false);
        mXslvDianosed.setOnScrollToBottomLintener(this);
        mXslvDianosed.setOnScrollListener(this);
        mXslvDianosed.setIXScrollViewListener(this);
    }

    @Override
    protected void initData() {
        if (NetUtil.checkNet(this)) {
            presenter.excellentAnswerList(_mApplication.getUserInfo().getSessionID(), 0, "");
            presenter.diagnoseIndex();
        } else {
            ToastUtil.showShort(this, R.string.no_net);
        }
    }

    @Override
    public DianosedStockPresenter initPresenter() {
        return new DianosedStockPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_dianosed_stock;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.btn_quick_ask: //快速诊股
                startActivity(new Intent(this, QuickQaActivity.class));
                break;
            case R.id.ll_quick_ask:  //快速诊股
                startActivity(new Intent(this, QuickQaActivity.class));
                break;
            case R.id.tv_more_teacher:
                startActivity(new Intent(this, OnlineTeacherRankingActivity.class));
                break;
            case R.id.tv_more_answer:
                startActivity(new Intent(this, ExcellentAnswerListActivity.class));
                break;
        }
    }


    public void getOnlineTeacher(List<DiagnoseUserIndexBean.TeacherListBean> teacherList) {
        List<OnlineTeacherGVModel> modelList = new ArrayList<>();
        for (DiagnoseUserIndexBean.TeacherListBean bean : teacherList) {
            if (modelList.size() < 5) {
                OnlineTeacherGVModel model = new OnlineTeacherGVModel(this, bean.getAvatars(),
                        bean.getNickname(), bean.getMonth_num(), bean.getTid(), bean.getStar_num());
                modelList.add(model);
            }
        }
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.item_online_teacher_gv, modelList, "OnlineTeacherGVHolder");
        mHolder.mNsgvOnlineTeacher.setAdapter(adapter);
    }

    public void getExcellentAnswer(List<ExcellentAnswerBean> beanList) {
        List<ExcellentAnswerModel> modelList = new ArrayList<>();
        for (ExcellentAnswerBean bean : beanList) {
            ExcellentAnswerModel model = new ExcellentAnswerModel(bean.getSname() + "（" + bean.getScode() + "）" + bean.getPdescription(),
                    bean.getAnswer_info(), bean.getAvatars(), bean.getNickname(), bean.getHtime(), bean.getId(), bean.getTid());
            modelList.add(model);
        }
        ListViewAdapter adapter = new ListViewAdapter(this, R.layout.item_excellent_answer, modelList, "ExcellentAnswerHolder");
        mHolder.mSlvExcellent.setAdapter(adapter);
    }

    private void getQuestions(List<QustionRollBean> questions) {
        if (questions.size() == 0) {
            mHolder.mRvQuestion.setVisibility(View.GONE);
            return;
        } else {
            mHolder.mRvQuestion.setVisibility(View.VISIBLE);
        }
//        if (questions.size() < 4) mHolder.mRvQuestion.set

        questionList.clear();
        questionList.addAll(questions);
        questionList.add(questionList.get(0));
        questionAdapter = new QuestionRollRVAdapter(R.layout.item_quetion_roll, questionList);
        mHolder.mRvQuestion.setAdapter(questionAdapter);
        mHolder.mRvQuestion.setLayoutManager(new LinearLayoutManager(this));
        mHolder.mRvQuestion.setItemAnimator(new DefaultItemAnimator());
        if (null == timerSubscription || timerSubscription.isUnsubscribed()) {
            startCountDown();
        }

//        mHolder.mRvQuestion.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
    }

    /**
     * 打开计时器
     */
    private void startCountDown() {
        timerSubscription = Observable.timer(1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
//                .toBlocking()
                .subscribe(aLong -> {
                    deelCountResult(aLong);
//                    Log.e("startCountDown", aLong +"");
                }, throwable -> {
                    Log.e("timer", "倒计时出错！\n" + throwable);
                    startCountDown();
                });
    }

    /**
     * 处理计时结果
     * @param aLong
     */
    private void deelCountResult(Long aLong) {
        if (aLong % 3 == 0) {
            QustionRollBean bean = questionList.get(1);
            questionList.remove(0);
            questionAdapter.notifyItemRemoved(0);
            questionList.add(bean);
            questionAdapter.notifyItemRangeChanged(0, questionList.size() - 0);
        }
        if (aLong >= 60) {
            timerSubscription.unsubscribe();
            startCountDown();
            return;
        }
    }

    @Override
    public void onScrollBottomListener(boolean isBottom) {

    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {
        Log.d("onScrollChanged", "t->" + t + "\noldt->" + oldt);
        int screenH = DensityUtil.getScreenWidth(this)[1];
        if (t < (screenH / 3)) {
            if (mSllQuick.isVisible()) {
                mSllQuick.hide(true);
            }
        } else {
            if (!mSllQuick.isVisible()){
                mSllQuick.setVisibility(View.VISIBLE);
                mSllQuick.show(true);
            }
        }
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
        super.onDestroy();
        if (null != timerSubscription && !timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
        }
    }

    @Override
    public void onSuccess(DiagnosedType requestType, Object object) {
        switch (requestType) {
            case USER_EXCELLENT_ANSWER_LIST:
                List<ExcellentAnswerBean> excellentAnswers = (List<ExcellentAnswerBean>) object;
                getExcellentAnswer(excellentAnswers);
                break;
            case USER_DIAGNOSE_INDEX:
                DiagnoseUserIndexBean indexBean = (DiagnoseUserIndexBean) object;
                getOnlineTeacher(indexBean.getTeacher_list());
                getQuestions(indexBean.getQues_list());
                break;
        }
        mXslvDianosed.stopRefresh();
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {
        switch (requestType) {
            case USER_DIAGNOSE_INDEX:
                ToastUtil.showShort(this, R.string.not_net_refresh);
                break;
        }
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void onLoadMore() {

    }

    static class ViewHolder {
        @Bind(R.id.ll_quick_ask)
        LinearLayout mLlQuickAsk;
        @Bind(R.id.rv_question)
        RecyclerView mRvQuestion;
        @Bind(R.id.tv_more_teacher)
        TextView mTvMoreTeacher;
        @Bind(R.id.nsgv_online_teacher)
        NoScrollGridView mNsgvOnlineTeacher;
        @Bind(R.id.tv_more_answer)
        TextView mTvMoreAnswer;
        @Bind(R.id.slv_excellent)
        ScrollListView mSlvExcellent;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
