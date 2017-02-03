package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.GoodAnswerRollRVAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ExcellentAnswerBean;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.activity.QuickQaActivity;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/12/27 0027.
 * email: m15267280642@163.com
 * explain: 老师房间精彩回答列表
 */

public class GoodAnswerFragment extends BaseFragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "teacherId";
    @Bind(R.id.rv_answer)
    RecyclerView mRvAnswer;
    @Bind(R.id.tv_ask)
    TextView mTvAsk;

    // TODO: Rename and change types of parameters
    private String teacherId;
    private String userName;
    private int starNum;
    private List<ExcellentAnswerBean> dataList = new ArrayList<>();
    private GoodAnswerRollRVAdapter goodAnswerAdapter;
    private Subscription timerSubscription;    // 计时

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_good_answer);
        if (getArguments() != null) {
            teacherId = getArguments().getString(ARG_PARAM1);
        }
    }


    public static GoodAnswerFragment newInstance(String param1) {
        GoodAnswerFragment fragment = new GoodAnswerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void initData(boolean isRefreshing) {
        getexcellentAnswerForRoom();
        queryTeacherInfo();
    }

    @Override
    protected void initView(View view) {
    }

    @Override
    protected void initEvent() {
        mTvAsk.setOnClickListener(this);
        mRvAnswer.setOnTouchListener(new View.OnTouchListener() {
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
                        } else if (dataList.size() > 4) {
                            Log.e(TAG, "onTouch: ACTION_CANCEL");
                            startCountDown();
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        if (dataList.size() > 4) {
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
        initData(true);
    }


    /**
     * 打开计时器
     */
    public void startCountDown() {
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
     *
     * @param aLong
     */
    private void deelCountResult(Long aLong) {
        if (aLong % 3 == 0) {
            ExcellentAnswerBean bean = dataList.get(1);
            dataList.remove(0);
            goodAnswerAdapter.notifyItemRemoved(0);
            dataList.add(bean);
            goodAnswerAdapter.notifyItemRangeChanged(0, dataList.size() - 0);
        }
        if (aLong >= 60) {
            timerSubscription.unsubscribe();
            startCountDown();
            return;
        }
    }

    /**
     * 从服务器拉取数据
     */
    private void getexcellentAnswerForRoom() {
        _apiManager.getService().excellentRoomAnswerList(_mApplication.getUserInfo().getSessionID(), teacherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::getData, throwable -> {
                    Log.e(TAG, "getexcellentAnswerForRoom: " + throwable.getMessage());
                });

//        AppObservable.bindFragment(this, _apiManager.getService().excellentRoomAnswerList(_mApplication.getUserInfo().getSessionID(), teacherId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::getData, throwable -> {
//                    Log.e(TAG, "getexcellentAnswerForRoom: " + throwable.getMessage());
//                });

    }


    /**
     * 处理数据
     *
     * @param
     */
    private void getData(ReturnListValue<ExcellentAnswerBean> returnListValue) {
        List<ExcellentAnswerBean> list = new ArrayList<>();
        if (returnListValue != null && returnListValue.getData() != null) {
            list = returnListValue.getData();
            if (list.size() == 0) {
                mRvAnswer.setVisibility(View.INVISIBLE);
                return;
            } else {
                mRvAnswer.setVisibility(View.VISIBLE);
            }
            dataList.clear();
            dataList.addAll(list);
            dataList.add(dataList.get(0));
            goodAnswerAdapter = new GoodAnswerRollRVAdapter(R.layout.item_excellent_answer, dataList);
            mRvAnswer.setAdapter(goodAnswerAdapter);
            mRvAnswer.setLayoutManager(new LinearLayoutManager(_mActivity));
            mRvAnswer.setItemAnimator(new DefaultItemAnimator());
        }
        startCountDown();

    }


    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (null != timerSubscription && timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
        }
        ButterKnife.unbind(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_ask:
                if (_mApplication.getUserInfo().getIsTeacher()) {
                    ToastUtil.show(_mActivity, "您是老师,没有权限提问", Toast.LENGTH_SHORT);
                } else {
                    EventBus.getDefault().postSticky(new AnyEventType(userName).setTid(teacherId).setType(Integer.valueOf(starNum)));
                    _mActivity.startActivity(new Intent(_mActivity, QuickQaActivity.class));
                }
                break;
        }
    }


    /**
     * 查询老师信息
     */
    private void queryTeacherInfo() {
        _apiManager.getService().queryTeacherInfo(teacherId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    HotLiveBean bean = retValue.getData();
                    userName = bean.getTitle();
                    starNum = Integer.valueOf(bean.getStar_num());
                }, throwable -> {
                    Log.e(TAG, "queryTeacherInfo: ", throwable);
                });

//        AppObservable.bindActivity(_mActivity, _apiManager.getService().queryTeacherInfo(teacherId))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    HotLiveBean bean = retValue.getData();
//                    userName = bean.getTitle();
//                    starNum = Integer.valueOf(bean.getStar_num());
//                }, throwable -> {
//                    Log.e(TAG, "queryTeacherInfo: ", throwable);
//                });
    }
}
