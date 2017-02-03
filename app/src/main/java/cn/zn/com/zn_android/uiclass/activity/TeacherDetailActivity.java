package cn.zn.com.zn_android.uiclass.activity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ShowGiftGridAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.GiftInfoBean;
import cn.zn.com.zn_android.model.bean.RoomInfoBean;
import cn.zn.com.zn_android.model.bean.RoomSummaryBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.uiclass.NoScrollGridView;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TeacherDetailActivity extends BaseActivity {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_teacher_name)
    TextView mTvTeacherName;
    @Bind(R.id.tv_teacher_fans)
    TextView mTvTeacherFans;
    @Bind(R.id.sdv_teacher_img)
    SimpleDraweeView mSdvTeacherImg;
    @Bind(R.id.tv_teacher_num)
    TextView mTvTeacherNum;
    @Bind(R.id.tv_teacher_style)
    TextView mTvTeacherStyle;
    @Bind(R.id.tv_teacher_sign)
    TextView mTvTeacherSign;
    @Bind(R.id.tv_teacher_intro)
    TextView mTvTeacherIntro;
    @Bind(R.id.gv_teacher_gifts)
    NoScrollGridView mGvTeacherGifts;
    @Bind(R.id.ll_gift)
    LinearLayout mLlGift;

    private String tid = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_teacher_detail);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String) {
            tid = (String) event.getObject();
        }
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.introduction));
        mGvTeacherGifts.setFocusable(false);
        queryRoomSummary();
    }

    @Override
    protected void initEvent() {
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("TeacherDetailActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TeacherDetailActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    private void queryRoomSummary() {
        _apiManager.getService().queryRoomSummary(tid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultRoomSummary, throwable -> {
                    Log.e(TAG, "queryRoomSummary: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryRoomSummary(tid))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultRoomSummary, throwable -> {
//                    Log.e(TAG, "queryRoomSummary: ", throwable);
//                });
    }

    private void resultRoomSummary(ReturnValue<RoomSummaryBean> returnValue) {
        if (returnValue.getMsg().equals(Constants.ERROR)) {
            ToastUtil.showShort(this, returnValue.getMsg());
        } else {
            if (returnValue.getData().getRoom_info() != null) {
                notifyTeacherUI(returnValue.getData().getRoom_info());
            }
            if (returnValue.getData().getGift_list() != null && returnValue.getData().getGift_list().size() > 0) {
                mLlGift.setVisibility(View.VISIBLE);
                notifyGifts(returnValue.getData().getGift_list());
            } else {
                mLlGift.setVisibility(View.GONE);
            }
        }
    }

    private void notifyTeacherUI(RoomInfoBean bean) {
        mTvTeacherName.setText(bean.getTitle());
        mTvTeacherFans.setText(String.format(getString(R.string.teacher_fans), new Object[]{bean.getCollect()}));
        mSdvTeacherImg.setImageURI(Uri.parse(bean.getAvatars()));
        mTvTeacherNum.setText(bean.getRoom_number());
        mTvTeacherStyle.setText(bean.getOperation());
        mTvTeacherSign.setText(bean.getSignature());
        mTvTeacherIntro.setText(bean.getSummary());
    }

    private void notifyGifts(List<GiftInfoBean> list) {
        ShowGiftGridAdapter adapter = new ShowGiftGridAdapter(this, list);
        mGvTeacherGifts.setAdapter(adapter);
    }
}
