package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.VideoListAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.bean.RoomBean;
import cn.zn.com.zn_android.model.bean.VideoBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.activity.SpecialLectureActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherLiveActivity;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 我的关注——直播室
 * Created by Jolly on 2016/9/7 0007.
 */
public class FocusRoomFragment extends BaseFragment implements AdapterView.OnItemClickListener {

    @Bind(R.id.lv_focus)
    XListView mLvFocus;

    private List<RoomBean> roomBeanList = new ArrayList<>();
    private List<VideoBean> list = new ArrayList<>();
    private VideoListAdapter mAdapter;
    private JoDialog dialog;

    public static FocusRoomFragment newInstance() {
        return new FocusRoomFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_focus_room);
    }

    @Override
    protected void initView(View view) {
        dialog = new JoDialog.Builder(_mActivity)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);

        mAdapter = new VideoListAdapter(_mContext, list);
        mLvFocus.setAdapter(mAdapter);
        queryFocusList();
    }

    @Override
    protected void initEvent() {
        mLvFocus.setPullLoadEnable(false);
        mLvFocus.setPullRefreshEnable(false);
        mLvFocus.setOnItemClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
    }

    private void queryFocusList() {
        AppObservable.bindFragment(this, _apiManager.getService().queryFocusList(_mApplication.getUserInfo().getSessionID(), ""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultRoom, throwable -> {
                    dialog.dismiss();
                    Log.e(TAG, "queryFocusList: ", throwable);
                });
    }

    private void resultRoom(ReturnListValue<RoomBean> returnValue) {
        dialog.dismiss();

        roomBeanList = returnValue.getData();
        for (RoomBean b:returnValue.getData()) {
            VideoBean bean = new VideoBean();
            bean.setTitle(b.getTitle());
            bean.setRemark(b.getSummary());
            bean.setImage(b.getAvatars());
            list.add(bean);
        }
        mAdapter.setData(list);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        VideoBean bean = (VideoBean) mAdapter.getItem(position);
        if (position == 0) {
            return;
        }
        if (roomBeanList.get(position - 1).getTid().equals("9898")) {
            startActivity(new Intent(_mContext, SpecialLectureActivity.class));
        } else {
            HotLiveBean hotLiveBean = new HotLiveBean();
            hotLiveBean.setTid(roomBeanList.get(position - 1).getTid());
            hotLiveBean.setOrdurl(roomBeanList.get(position - 1).getOrdurl());
            hotLiveBean.setAvatars(roomBeanList.get(position - 1).getAvatars());
            hotLiveBean.setTitle(roomBeanList.get(position - 1).getTitle());
            hotLiveBean.setRoom_number(roomBeanList.get(position - 1).getRoom_number());
            hotLiveBean.setCollect(roomBeanList.get(position - 1).getCollect());
            hotLiveBean.setClick(roomBeanList.get(position - 1).getClick());
            hotLiveBean.setPlacard(roomBeanList.get(position - 1).getPlacard());
            EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
            startActivity(new Intent(_mContext, TeacherLiveActivity.class));
        }
    }
}
