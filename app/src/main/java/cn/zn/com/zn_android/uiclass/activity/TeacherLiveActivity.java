package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.eclipse.paho.client.mqttv3.MqttException;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.VipStateBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.customerview.MarqueeText;
import cn.zn.com.zn_android.uiclass.fragment.ChatVIPFragment;
import cn.zn.com.zn_android.uiclass.fragment.GoodAnswerFragment;
import cn.zn.com.zn_android.uiclass.fragment.TeacherChatFragment;
import cn.zn.com.zn_android.uiclass.fragment.WealthToolsFragment;
import cn.zn.com.zn_android.utils.DMSUtils;
import cn.zn.com.zn_android.utils.LogUtils;
import cn.zn.com.zn_android.utils.UIUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/4/12 0012.
 * explain:
 */
public class TeacherLiveActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbShare;
    @Bind(R.id.sdv_head)
    SimpleDraweeView mSdvHead;
    @Bind(R.id.tv_room_name)
    TextView mTvRoomName;
    @Bind(R.id.tv_fans)
    TextView mTvFans;
    @Bind(R.id.tv_hot)
    TextView mTvHot;
    @Bind(R.id.tv_notice)
    MarqueeText mTvNotice;
    @Bind(R.id.tl_chat)
    TabLayout mTlChat;
    @Bind(R.id.vp_chat)
    ViewPager mVpChat;
    @Bind(R.id.cb_follow)
    CheckBox mCbFollow;
    @Bind(R.id.rl_follow)
    RelativeLayout mRlFollow;
    @Bind(R.id.rl_head)
    RelativeLayout mRlHead;
    @Bind(R.id.ll_head)
    LinearLayout mLlHead;
    @Bind(R.id.iv_star)
    ImageView mIvStar;
    @Bind(R.id.ll_head_img)
    LinearLayout mLlHeadImg;

    private HotLiveBean liveInfo;
    private StringBuilder roomName;
    private StringBuilder fans;
    private StringBuilder hotClick;
    private VipStateBean vipState;
    private boolean isRoomVip;
    //    private ChatVipListFragment vipChatFragment;
    private PresentScorePresenter sharepresenter;
    private TeacherChatFragment mChatFragment;
    private ChatVIPFragment mChatVIPFragment;
    private DMSUtils dms;
    private String shareContent = Constants.teacherShareContent;
    private String shareTitle = Constants.teacherShareTitle;
    private String mUrl = Constants.teacherShareUrl;
    UMImage image = new UMImage(TeacherLiveActivity.this, Constants.iconResourece);
    private int currentItem = 0;

    /**
     * 分享的平台数组
     */
    final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]
            {
                    SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA,
                    SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE
            };

    /**
     * 三方平台的分享
     */
    private void societyShare() {
        setDialog();
        new ShareAction(this).setDisplayList(displaylist)
                .withText(shareTitle)
                .withTitle(shareContent)
                .withTargetUrl(mUrl)
                .withMedia(image)
                .setListenerList(umShareListener)
                .open();
        //关闭log和toast
        Config.OpenEditor = false;
//        Log.LOG = false;
        Config.IsToastTip = false;
        Config.dialog.dismiss();

    }

    /**
     * 分享的监听器
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(ArticleDetailActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(ArticleDetailActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(ArticleDetailActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };


    /**
     * 设置三方跳转的时候的dialog
     */
    private void setDialog() {
        JoDialog dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        Config.dialog = dialog;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_person_live);

        // 初始化聊天会话
        dms = new DMSUtils(this, mChatVIPFragment.getChatVipView(), mChatFragment.getChatVipView());
        dms.initDMS(liveInfo.getTid());
        try {
            dms.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过EventBus获取传送过来的数据
     *
     * @param event
     */
    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof HotLiveBean) {
            liveInfo = (HotLiveBean) event.getObject();
            currentItem = liveInfo.getCurrentItem();
        }
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
        mTvNotice.removeScroll();
        try {
            dms.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

//        HashMap<String, Object> bitMaps = RnApplication.getInstance().getBmMap();
//        if (null != bitMaps) {
//            Iterator<String> keyIter = bitMaps.keySet().iterator();
//            while (keyIter.hasNext()) {
//                String key = keyIter.next();
//                Vector<GifDecoder.GifFrame> frameVector = (Vector<GifDecoder.GifFrame>) bitMaps.get(key);
//                for (GifDecoder.GifFrame frame: frameVector) {
//                    frame.image.recycle();
//                }
//                keyIter.remove();
//                bitMaps.remove(key);
//            }
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);       //统计时长

        //启动跑马灯
        mTvNotice.startScroll();
        if (_mApplication.getUserInfo().getIsLogin() == Constants.NOT_LOGIN) {
            mCbFollow.setChecked(false);
        } else {
            getCollectState(liveInfo.getTid());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        sharepresenter = new PresentScorePresenter(this);
        mIbShare.setImageResource(R.drawable.article_share);
        mIbShare.setVisibility(View.VISIBLE);
        getVipState(liveInfo.getTid());
        mTlChat.setFocusable(true);
        mTlChat.setupWithViewPager(mVpChat);
        //获取关注该房间的状态
//        initData();
        if (mVpChat != null) {
            setupViewPager(mVpChat);
        }
        queryTeacherInfo();
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mRlFollow.setOnClickListener(this);
        mSdvHead.setOnClickListener(this);


        mVpChat.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                hideKeyBoard();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTlChat.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                hideKeyBoard();
                mVpChat.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        mIbShare.setOnClickListener(v -> {
            societyShare();
        });

    }

    public void hideKeyBoard() {
        //隐藏软键盘
        View view = this.getCurrentFocus();
        UIUtil.hidekeyBoard(view);
    }

    private void setupViewPager(ViewPager mVpChat) {
        mVpChat.setOffscreenPageLimit(3);
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
        mChatFragment = TeacherChatFragment.newInstance(liveInfo.getTid());
        adapter.addFragment(mChatFragment, "普通直播");
//        vipChatFragment = ChatVipListFragment.newInstance(liveInfo.getTid(), liveInfo.getVipurl());
        mChatVIPFragment = ChatVIPFragment.newInstance(liveInfo.getTid());
        adapter.addFragment(mChatVIPFragment, "VIP直播");
//        adapter.addFragment(ChatVipListFragment.newInstance(liveInfo.getTid(), liveInfo.getVipurl()), "VIP直播");
//        adapter.addFragment(TacticsFragment.newInstance(liveInfo.getTid(), ""), "最新策略");
        adapter.addFragment(GoodAnswerFragment.newInstance(liveInfo.getTid()), "精彩问答");
        adapter.addFragment(WealthToolsFragment.newInstance(liveInfo.getTid(), ""), "财富工具");
        mVpChat.setAdapter(adapter);
        mVpChat.setCurrentItem(currentItem);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.rl_follow:
                if (_mApplication.getUserInfo().getIsLogin() == Constants.IS_LOGIN) {
                    if (!mCbFollow.isChecked()) {
                        addCollectRoom();
                    } else {
                        cancelRoom(liveInfo.getTid());
                    }
                } else {
                    startActivity(new Intent(this, LoginActivity.class));
                }
                break;
            case R.id.sdv_head:
                startActivity(new Intent(this, TeacherDetailActivity.class));
                EventBus.getDefault().postSticky(new AnyEventType(liveInfo.getTid()));
                break;
            default:
                break;
        }
    }

    /**
     * 初始化数据
     */
    protected void initData() {
        updateUi();
    }

    /**
     * 拿到数据更行UI
     */
    private void updateUi() {
        if (liveInfo.getAvatars() != null) {
            mSdvHead.setImageURI(Uri.parse(liveInfo.getAvatars()));
        }
        mToolbarTitle.setText(liveInfo.getTitle());
        roomName = new StringBuilder();
        roomName.append(liveInfo.getTitle())
                .append("   (")
                .append(liveInfo.getRoom_number())
                .append(")");
        mTvRoomName.setText(roomName);
        fans = new StringBuilder("关注:  ");
        fans.append(liveInfo.getCollect());
        mTvFans.setText(fans.toString());
        hotClick = new StringBuilder("人气:  ");
        hotClick.append(liveInfo.getClick());
        mTvHot.setText(hotClick.toString());
        mTvNotice.setText(liveInfo.getPlacard());

        String resName = "ic_teacher_star" + liveInfo.getStar_num();
        mIvStar.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(resName, "drawable", getPackageName())));


    }

    /**
     * 获取是否关注过该房间
     *
     * @param tid
     */
    private void getCollectState(String tid) {
        _apiManager.getService().isCollectRoom(_mApplication.getUserInfo().getSessionID(), tid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultCollectState, Throwable -> {
                    Throwable.printStackTrace();
                });

//        AppObservable.bindActivity(this, _apiManager.getService().isCollectRoom(_mApplication.getUserInfo().getSessionID(), tid))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultCollectState, Throwable -> {
//                    Throwable.printStackTrace();
//                });
    }

    private void resultCollectState(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null) {
            if (returnValue.getData().getMessage().equals(getString(R.string.is_collect))) {
                mCbFollow.setChecked(true);
            } else {
                mCbFollow.setChecked(false);
            }
        }
    }

    /**
     * 关注该房间
     */
    private void addCollectRoom() {
        _apiManager.getService().addRoomCollect(_mApplication.getUserInfo().getSessionID(), liveInfo.getTid())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultCollectRoom, throwable -> {
                    Log.e(TAG, "addCollectRoom: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().addRoomCollect(_mApplication.getUserInfo().getSessionID(), liveInfo.getTid()))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultCollectRoom, throwable -> {
//                    Log.e(TAG, "addCollectRoom: ", throwable);
//                });
    }

    private void resultCollectRoom(ReturnValue<MessageBean> returnValue) {
        if (returnValue.getMsg().equals(Constants.ERROR)) {
            showFanDialog(returnValue.getData().getMessage());
            mCbFollow.setChecked(true);
        } else {
            showFanDialog(getString(R.string.had_fan));
            mCbFollow.setChecked(true);
        }
    }

    private void cancelRoom(String tid) {
        _apiManager.getService().cancelRoom(_mApplication.getUserInfo().getSessionID(), tid)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(ret -> {
                    showFanDialog(ret.getData().getMessage());
                    if (ret.getData().getMessage().contains("成功")) {
                        mCbFollow.setChecked(false);
                    }
                }, throwable -> {
                    Log.e(TAG, "cancelRoom: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().cancelRoom(_mApplication.getUserInfo().getSessionID(), tid))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(ret -> {
//                    showFanDialog(ret.getData().getMessage());
//                    if (ret.getData().getMessage().contains("成功")) {
//                        mCbFollow.setChecked(false);
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "cancelRoom: ", throwable);
//                });
    }

    /**
     * 获取VIP状态
     *
     * @param tid
     */
    private void getVipState(String tid) {
        _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), tid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultVipState, Throwable -> {
                    Throwable.printStackTrace();
                });
//        AppObservable.bindActivity(this, _apiManager.getService().getVipState(_mApplication.getUserInfo().getSessionID(), tid))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultVipState, Throwable -> {
//                    Throwable.printStackTrace();
//                });
    }

    private void resultVipState(ReturnValue<VipStateBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            vipState = returnValue.getData();
            if (vipState.getIsroomvip().equals(Constants.IS_VIP)) {
                isRoomVip = true;
            } else {
                isRoomVip = false;
            }
        } else {
            LogUtils.i(returnValue.getMsg());
        }
//        vipChatFragment.setVipState(isRoomVip);
    }

    /**
     * 提示已经关注
     *
     * @param content
     */
    private void showFanDialog(String content) {

        new JoDialog.Builder(this)
                .setGravity(Gravity.CENTER)
                .setPositiveTextRes(R.string.confirm)
                .setStrTitle(R.string.tips)
                .setStrContent(content)
                .setCallback(new JoDialog.ButtonCallback() {
                    @Override
                    public void onPositive(JoDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {
                    }
                })
                .show(true);
    }

    private void queryTeacherInfo() {
        _apiManager.getService().queryTeacherInfo(liveInfo.getTid())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    HotLiveBean bean = retValue.getData();
                    liveInfo.setAvatars(bean.getAvatars());
                    liveInfo.setClick(bean.getClick());
                    liveInfo.setCollect(bean.getCollect());
                    liveInfo.setRoom_number(bean.getRoom_number());
                    liveInfo.setTitle(bean.getTitle());
                    liveInfo.setPlacard(bean.getPlacard());
                    liveInfo.setNickname(bean.getNickname());
                    liveInfo.setStar_num(bean.getStar_num());
                    updateUi();
                }, throwable -> {
                    Log.e(TAG, "queryTeacherInfo: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryTeacherInfo(liveInfo.getTid()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    HotLiveBean bean = retValue.getData();
//                    liveInfo.setAvatars(bean.getAvatars());
//                    liveInfo.setClick(bean.getClick());
//                    liveInfo.setCollect(bean.getCollect());
//                    liveInfo.setRoom_number(bean.getRoom_number());
//                    liveInfo.setTitle(bean.getTitle());
//                    liveInfo.setPlacard(bean.getPlacard());
//                    liveInfo.setNickname(bean.getNickname());
//                    liveInfo.setStar_num(bean.getStar_num());
//                    updateUi();
//                }, throwable -> {
//                    Log.e(TAG, "queryTeacherInfo: ", throwable);
//                });
    }

}
