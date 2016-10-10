package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.JoFragmentPagerAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ChatMsgBean;
import cn.zn.com.zn_android.model.bean.CoursesBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.PresentScorePresenter;
import cn.zn.com.zn_android.presenter.requestType.ChatRequestType;
import cn.zn.com.zn_android.uiclass.SmartLinearLayout;
import cn.zn.com.zn_android.uiclass.customerview.AspectLayout;
import cn.zn.com.zn_android.uiclass.customerview.ChatInput;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.customerview.MarqueeText;
import cn.zn.com.zn_android.uiclass.customerview.MediaController;
import cn.zn.com.zn_android.uiclass.fragment.ChatFragment;
import cn.zn.com.zn_android.uiclass.fragment.PrivateTalkFragment;
import cn.zn.com.zn_android.uiclass.fragment.WealthToolsFragment;
import cn.zn.com.zn_android.utils.DMSUtils;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.ChatView;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PlayerCode;
import com.pili.pldroid.player.common.Util;
import com.pili.pldroid.player.widget.VideoView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import de.greenrobot.event.EventBus;
import rx.Observable;
import rx.Subscription;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import tv.danmaku.ijk.media.player.IMediaPlayer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;

/**
 * 特约讲堂
 */
public class SpecialLectureActivity extends BaseActivity implements
        View.OnClickListener,
        WealthToolsFragment.OnFragmentInteractionListener,
        JoDialog.ButtonCallback,
        IjkMediaPlayer.OnCompletionListener,
        IjkMediaPlayer.OnInfoListener,
        IjkMediaPlayer.OnErrorListener,
        IjkMediaPlayer.OnVideoSizeChangedListener,
        IjkMediaPlayer.OnPreparedListener,
        ChatView {
    private static final String TAG = SpecialLectureActivity.class.getSimpleName();
    private static final int REQ_DELAY_MILLS = 3000;
    private static final int CHAT_LIST = 0;
    private static final String MSG = "1";
    private static final String GIFT = "2";
    private static final String DEFAULT_TID = "9898";
    private static final String DEFAULT_VIPFD = "";
    @Bind(R.id.ib_back)
    ImageButton mIbBack;
    @Bind(R.id.sll_chat_input)
    SmartLinearLayout mSllChatInput;
    private String currentType = MSG;

    @Bind(R.id.video_view)
    VideoView mVideoView;
    @Bind(R.id.aspect_layout)
    AspectLayout mAspectLayout;
    @Bind(R.id.buffering_progress)
    ProgressBar mBufferingProgress;
    @Bind(R.id.buffering_msg)
    TextView mBufferingMsg;
    @Bind(R.id.buffering_indicator)
    LinearLayout mBufferingIndicator;
    @Bind(R.id.ib_full_screen)
    ImageButton mIbFullScreen;
    @Bind(R.id.sll_menu_bottom)
    SmartLinearLayout mSllMenuBottom;
    @Bind(R.id.ib_share)
    ImageButton mIbShare;
    @Bind(R.id.sll_menu_top)
    SmartLinearLayout mSllMenuTop;
    @Bind(R.id.fl_lecture)
    FrameLayout mFlLecture;
    @Bind(R.id.tv_notice)
    MarqueeText mTvNotice;
    @Bind(R.id.tl_chat)
    TabLayout mTlChat;
    @Bind(R.id.vp_chat)
    ViewPager mVpChat;
    @Bind(R.id.ll_content_view)
    LinearLayout mLlContentView;
//    @Bind(R.id.rl_root)
//    RelativeLayout mRlRoot;

    public ChatInput getmChatInput() {
        return mChatInput;
    }

    @Bind(R.id.chat_input)
    ChatInput mChatInput;
    private String vipfd = DEFAULT_VIPFD;
    private String tid = DEFAULT_TID;
    private DMSUtils dms;

    private MediaController mMediaController;

//    private String mVideoPath = "rtmp://live.hkstv.hk.lxdns.com/live/hks";
//    private String mVideoPath = "http://9626.hlsplay.aodianyun.com/98cfw/stream.m3u8";
//    private String playUrl = "http://9626.hlsplay.aodianyun.com/98cfw/stream.m3u8";
    //    private String playUrl = "rtmp://9626.lssplay.aodianyun.com/98cfw/stream";


    private boolean mIsLiveStream = false;
    private boolean mIsCompleted = false;
    private Runnable mVideoReconnect;
    private int mReqDelayMills = REQ_DELAY_MILLS;
    private Pair<Integer, Integer> mScreenSize;
    private ViewGroup.LayoutParams mLayoutParams;
    private long mLastPosition = 0;
    private boolean isFullScreen = false;
    private IMediaPlayer mIMediaPlayer;
    private int prePagePostion = 0;
    private Subscription timerSubscription; // 计时隐藏播放器上的图标

    private EditText priMsg;
    private TextView msgSend;
    private List<String> qqList = new ArrayList<>();
    private ChatFragment mChatFragment;
    private PresentScorePresenter sharepresenter;
    private int currentPage = 0;
    private String shareContent = Constants.specificShareContent;
    private String shareTitle = Constants.specificShareTitle;
    private String mUrl = Constants.specificShareUrl;
    UMImage image = new UMImage(SpecialLectureActivity.this, Constants.iconResourece);

    @Override
    public void onFragmentInteraction(Uri uri) {
        ToastUtil.showShort(_mApplication, uri.toString());
    }

    @Override
    public void onPositive(JoDialog dialog) {
        initPlayer();
        dialog.dismiss();
    }

    @Override
    public void onNegtive(JoDialog dialog) {
        dialog.dismiss();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(true);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_special_lecture);

        // 初始化聊天会话
        dms = new DMSUtils(this, null, mChatFragment.getChatView());
        dms.initDMS(tid);
        try {
            dms.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void initView() {
        sharepresenter = new PresentScorePresenter(this);
        queryNotice();
        int[] width = DensityUtil.getScreenWidth(this);
//        RelativeLayout.LayoutParams fLayoutParams = new RelativeLayout.LayoutParams(width[0], width[0] * 9 / 16);
        LinearLayout.LayoutParams fLayoutParams = new LinearLayout.LayoutParams(width[0], width[0] * 9 / 16);
        mFlLecture.setLayoutParams(fLayoutParams);
        mLayoutParams = mAspectLayout.getLayoutParams();
        mLayoutParams.width = width[0];
        mLayoutParams.height = width[0] * 9 / 16;
        mAspectLayout.setLayoutParams(mLayoutParams);

        if (NetUtil.isWIFIConnected(_mApplication)) {
            initPlayer();
        } else if (NetUtil.isMOBILEConnected(_mApplication)) {
            confirmUsePhoneDialog();
        } else {
            mBufferingIndicator.setVisibility(View.VISIBLE);
            mBufferingProgress.setVisibility(View.GONE);
            mBufferingMsg.setText(getString(R.string.no_special_lecture));
        }

        if (mVpChat != null) {
            setupViewPager(mVpChat);
        }
        mTlChat.setFocusable(true);
        mTlChat.setupWithViewPager(mVpChat);
        mTlChat.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_bar_color));

        mTvNotice.startScroll();
        mTvNotice.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTvNotice.stopScroll();
                        return true;
                    case MotionEvent.ACTION_UP:
                        mTvNotice.startScroll();

                        return false;
                    case MotionEvent.ACTION_CANCEL:
                        mTvNotice.startScroll();

                        return false;
                }
                return true;
            }
        });

        mChatInput.setmChatView(this);
        mChatInput.set_activity(this);
        mChatInput.setContentView(mLlContentView).initEmotionKeyBoard();
//        mChatInput.set_activity(this);
    }

    private void setupViewPager(ViewPager mVpChat) {
        mVpChat.setOffscreenPageLimit(2);
        JoFragmentPagerAdapter adapter = new JoFragmentPagerAdapter(getSupportFragmentManager());
        mChatFragment = ChatFragment.newInstance("9898");
        adapter.addFragment(mChatFragment, "聊天室");
        adapter.addFragment(PrivateTalkFragment.newInstance(tid), "会员悄悄话");
        adapter.addFragment(WealthToolsFragment.newInstance("", ""), "财富工具");
        mVpChat.setAdapter(adapter);
    }

    @Override
    protected void initEvent() {
        mFlLecture.setOnClickListener(this);
        mIbShare.setOnClickListener(this);

        mVpChat.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                currentPage = position;
//                if (currentPage > 0) {
//                    mSllChatInput.hide();
//                    mChatInput.setVisibility(View.GONE);
//                } else {
//                    mChatInput.setVisibility(View.VISIBLE);
//                    mSllChatInput.show();
//                    mChatFragment.getmLvChat().setSelection(mChatFragment.getmLvChat().getAdapter().getCount() - 1);
//
//                }
            }

            @Override
            public void onPageSelected(int position) {
                currentPage = position;
//                mChatInput.hideKeyBoard();
                if (currentPage > 0) {
                    mSllChatInput.hide();
                    mChatInput.setVisibility(View.GONE);
                } else {
                    mChatInput.setVisibility(View.VISIBLE);
                    mSllChatInput.show();
                    mChatFragment.getmLvChat().setSelection(mChatFragment.getmLvChat().getAdapter().getCount() - 1);

                }
                mChatInput.updateView(ChatInput.InputMode.NONE);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTlChat.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
//                mChatInput.hideKeyBoard();
                currentPage = tab.getPosition();
                if (currentPage > 0) {
                    mSllChatInput.hide();
                    mChatInput.setVisibility(View.GONE);
                } else {
                    mChatInput.setVisibility(View.VISIBLE);
                    mSllChatInput.show();
                    mChatFragment.getmLvChat().setSelection(mChatFragment.getmLvChat().getAdapter().getCount() - 1);
                }
                mVpChat.setCurrentItem(currentPage);
                mChatInput.updateView(ChatInput.InputMode.NONE);

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


//
//    /**
//     * 发送悄悄话的dialog
//     */
//    Dialog privateTalkDialog = null;
//
//    private void showSendPrivateTalkDialog() {
//        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(this);
//        View layout = LayoutInflater.from(this).inflate(R.layout.layout_send_private_talk, new ListView(this), false);
//        priMsg = (EditText) layout.findViewById(R.id.et_private_msg);
//        msgSend = (TextView) layout.findViewById(R.id.tv_send_msg);
//        msgSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String msg = priMsg.getText().toString();
//                if (StringUtil.isEmpty(msg)) {
//                    ToastUtil.showShort(SpecialLectureActivity.this, getString(R.string.msg_not_empty));
//                    return;
//                }
//                postMsg(msg);
//                privateTalkDialog.dismiss();
//                LogUtils.i("发送消息");
//            }
//        });
//        builder.setLayoutView(layout);
//        builder.setCancelable(true);
//        privateTalkDialog = builder.create();
//        privateTalkDialog.show();
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mTvNotice.removeScroll();

        if (timerSubscription != null && !timerSubscription.isUnsubscribed()) {
            timerSubscription.unsubscribe();
        }
//        getSupportFragmentManager().getFragments().clear();

        try {
            dms.disconnect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_back:
                onBackPressed();
                break;
            case R.id.ib_share:
                societyShare();
                break;
            case R.id.ib_full_screen:
                if (!isFullScreen) {
                    isFullScreen = true;
                    mIbFullScreen.setImageResource(R.mipmap.ic_narrow);
                } else {
                    isFullScreen = false;
                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    mIbFullScreen.setImageResource(R.mipmap.ic_full_screen);
                }
                if (mIMediaPlayer != null) {
                    onVideoSizeChanged(mIMediaPlayer, mIMediaPlayer.getVideoWidth(), mIMediaPlayer.getVideoHeight(), 0, 0);
                }

                break;
            case R.id.fl_lecture:
                if (mSllMenuBottom.isVisible()) {
//                    TopHide(mFlMenuTop);
                    mSllMenuTop.hide(true, true);
                    mSllMenuBottom.hide();
                } else {
                    mSllMenuTop.show(true, true);
                    mSllMenuBottom.show();
                    startCountDown();
                }
                break;
//            case R.id.ib_fan:
//                if (tid.equals(DEFAULT_TID)) {
//                    if (_mApplication.getUserInfo().getIsLogin() == Constants.IS_LOGIN) {
//                        addCollectRoom();
//                    } else {
//                        startActivity(new Intent(SpecialLectureActivity.this, LoginActivity.class));
//                    }
//                } else {
//                    showSendPrivateTalkDialog();
//                }
//                break;
//            case R.id.ib_vip:
//                if (_mApplication.getUserInfo().getIsLogin() == 1) {
//                    EventBus.getDefault().postSticky(new AnyEventType(tid));
//                    startActivity(new Intent(SpecialLectureActivity.this, MemberAreaActivity.class));
//                } else {
//                    startActivity(new Intent(SpecialLectureActivity.this, LoginActivity.class));
//                }
//                break;
        }
    }


    /**
     * 初始化播放器
     */
    private void initPlayer() {
        boolean useFastForward = true;
        boolean disableProgressBar = false;
        mMediaController = new MediaController(this, useFastForward, disableProgressBar);
        mVideoView.setMediaController(new MediaController(this, false, true));

        mIsLiveStream = true;

        mMediaController.setMediaPlayer(mVideoView);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setMediaBufferingIndicator(mBufferingIndicator);

        AVOptions options = new AVOptions();
        options.setInteger(AVOptions.KEY_MEDIACODEC, 0); // 1 -> enable, 0 -> disable

        if (mIsLiveStream) {
            options.setInteger(AVOptions.KEY_BUFFER_TIME, 1000); // the unit of buffer time is ms
            options.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000); // the unit of timeout is ms
            options.setString(AVOptions.KEY_FFLAGS, AVOptions.VALUE_FFLAGS_NOBUFFER); // "nobuffer"
            options.setInteger(AVOptions.KEY_LIVE_STREAMING, 1);
        }
        mVideoView.setAVOptions(options);

        mVideoView.setVideoPath(Constants_api.LECTURE_URL);

        mVideoView.setOnErrorListener(this);
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnInfoListener(this);
        mVideoView.setOnPreparedListener(this);
        mVideoView.setOnVideoSizeChangedListener(this);

        mVideoView.requestFocus();
        mBufferingIndicator.setVisibility(View.VISIBLE);

    }

    @Override
    public void onCompletion(IMediaPlayer iMediaPlayer) {
        Log.d(TAG, "onCompletion");
        mIsCompleted = true;
        mBufferingIndicator.setVisibility(View.GONE);
        mVideoView.start();

    }

    @Override
    public boolean onError(IMediaPlayer iMediaPlayer, int what, int extra) {
        Log.d(TAG, "onError what=" + what + ", extra=" + extra);
        if (what == -10000) {
            if (extra == PlayerCode.EXTRA_CODE_INVALID_URI || extra == PlayerCode.EXTRA_CODE_EOF) {
                if (mBufferingIndicator != null)
                    mBufferingIndicator.setVisibility(View.GONE);
                return true;
            }
            if (mIsCompleted && extra == PlayerCode.EXTRA_CODE_EMPTY_PLAYLIST) {
                Log.d(TAG, "mVideoView reconnect!!!");
                mVideoView.removeCallbacks(mVideoReconnect);
                mVideoReconnect = new Runnable() {
                    @Override
                    public void run() {
                        mVideoView.setVideoPath(Constants_api.LECTURE_URL);
                    }
                };
                mVideoView.postDelayed(mVideoReconnect, mReqDelayMills);
                mReqDelayMills += 200;
            } else if (extra == PlayerCode.EXTRA_CODE_404_NOT_FOUND) {
                // NO ts exist
                if (mBufferingIndicator != null)
                    mBufferingIndicator.setVisibility(View.GONE);
            } else if (extra == PlayerCode.EXTRA_CODE_IO_ERROR) {
                // NO rtmp stream exist
                if (mBufferingIndicator != null)
                    mBufferingIndicator.setVisibility(View.GONE);
            } else {
                mBufferingIndicator.setVisibility(View.VISIBLE);
                mBufferingProgress.setVisibility(View.GONE);
                mBufferingMsg.setText(getString(R.string.no_special_lecture));
            }
        }
        // return true means you handle the onError, hence System wouldn't handle it again(popup a dialog).
        return true;
    }

    @Override
    public boolean onInfo(IMediaPlayer iMediaPlayer, int what, int extra) {
        Log.d(TAG, "onInfo what=" + what + ", extra=" + extra);


        if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_START) {
            Log.i(TAG, "onInfo: (MEDIA_INFO_BUFFERING_START)");
            if (mBufferingIndicator != null)
                mBufferingIndicator.setVisibility(View.VISIBLE);
        } else if (what == IMediaPlayer.MEDIA_INFO_BUFFERING_END) {
            Log.i(TAG, "onInfo: (MEDIA_INFO_BUFFERING_END)");
            if (mBufferingIndicator != null)
                mBufferingIndicator.setVisibility(View.GONE);
        } else if (what == IMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START) {
            Log.i(TAG, "duration:" + mVideoView.getDuration());
        } else if (what == IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START) {
            Log.i(TAG, "duration:" + mVideoView.getDuration());
        }
        return true;
    }

    @Override
    public void onPrepared(IMediaPlayer iMediaPlayer) {
//        Log.d(TAG, "onPrepared");
        mBufferingIndicator.setVisibility(View.GONE);
        mReqDelayMills = REQ_DELAY_MILLS;
        startCountDown(); // 加载完后 4s隐藏遮罩层


    }

    @Override
    public void onVideoSizeChanged(IMediaPlayer iMediaPlayer, int width, int height, int sarNum, int sarDen) {
        mIMediaPlayer = iMediaPlayer;
//        Log.i(TAG, "onVideoSizeChanged " + iMediaPlayer.getVideoWidth() + "x" + iMediaPlayer.getVideoHeight() + ",width:" + width + ",height:" + height + ",sarDen:" + sarDen + ",sarNum:" + sarNum);
        if (isFullScreen) {
            // land video
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
            mScreenSize = Util.getResolution(this);
        } else {
            // port video
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
            mScreenSize = Util.getResolution(this);
        }

        int currentWidth;
        int currentHeight;
        int[] sreenWH = DensityUtil.getScreenWidth(this);
        mLayoutParams = mAspectLayout.getLayoutParams();
        if (isFullScreen) {
            currentWidth = mScreenSize.first;
            currentHeight = mScreenSize.second;
        } else {
            currentWidth = mScreenSize.first;
            currentHeight = sreenWH[0] * 9 / 16;
        }

        if (width * 1.0 / currentWidth > (height * 1.0 / currentHeight)) {
            height = width * currentHeight / currentWidth;
            width = currentWidth;
        } else {
            if (isFullScreen) {
                width = width * currentHeight / height;
                height = currentHeight;
            } else {
                width = currentWidth;
                height = currentWidth * 9 / 16;
            }
        }

        mLayoutParams.width = width;
        mLayoutParams.height = height;
        mAspectLayout.setLayoutParams(mLayoutParams);


        LinearLayout.LayoutParams fLayoutParams = null;
        if (isFullScreen) {
            fLayoutParams = new LinearLayout.LayoutParams(mScreenSize.first, mScreenSize.second);

        } else {
            fLayoutParams = new LinearLayout.LayoutParams(mScreenSize.first, currentHeight);
        }
        mFlLecture.setLayoutParams(fLayoutParams);

        if (!isFullScreen && 0 == currentPage) {
            mChatInput.setVisibility(View.VISIBLE);
            mSllChatInput.show();
            mChatFragment.getmLvChat().setSelection(mChatFragment.getmLvChat().getAdapter().getCount() - 1);

        } else {
            mSllChatInput.hide();
            mChatInput.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);       //统计时长

        mReqDelayMills = REQ_DELAY_MILLS;
        if (mVideoView != null && !mIsLiveStream && mLastPosition != 0) {
            mVideoView.seekTo(mLastPosition);
            mVideoView.start();
        }

    }

    @Override
    public void onPause() {
        MobclickAgent.onPause(this);

        if (mVideoView != null) {
            mLastPosition = mVideoView.getCurrentPosition();
            mVideoView.pause();
        }
        super.onPause();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    public void onBackPressed() {
        if (mIMediaPlayer != null && isFullScreen) {
            isFullScreen = !isFullScreen;
            onVideoSizeChanged(mIMediaPlayer, mIMediaPlayer.getVideoWidth(), mIMediaPlayer.getVideoHeight(), 0, 0);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            mIbFullScreen.setImageResource(R.mipmap.ic_full_screen);
        } else {
            finish();
        }
    }

    /**
     * 4s后隐藏视频遮罩层
     */
    private void startCountDown() {
        timerSubscription = Observable.timer(1, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    if (aLong == 3) {
                        timerSubscription.unsubscribe();
                        mSllMenuTop.hide(true, true);
                        mSllMenuBottom.hide();
                    }
                }, throwable -> {
                    Log.e(TAG, "视频倒计时出错！");
                });
    }

    /**
     * 提示是否用流量看直播
     */
    private void confirmUsePhoneDialog() {
        new JoDialog.Builder(this)
                .setStrTitle(R.string.tips)
                .setStrContent(getString(R.string.use_phone_flow))
                .setPositiveTextRes(R.string.confirm)
                .setNegativeTextRes(R.string.cancel)
                .setCallback(this)
                .show(true);
    }

    /**
     * 查询公告
     */
    private void queryNotice() {
        AppObservable.bindActivity(this, _apiManager.getService().queryNotice(""))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultNotice, throwable -> {
                    mTvNotice.setVisibility(View.GONE);
                    Log.e(TAG, "queryNotice: ", throwable);
                });
    }

    /**
     * 处理公告结果
     *
     * @param returnValue
     */
    private void resultNotice(ReturnValue<CoursesBean> returnValue) {
        if (returnValue.getData() != null && returnValue.getData().getMessage() != null && !returnValue.getData().getMessage().equals("")) {
            mTvNotice.setVisibility(View.VISIBLE);
            mTvNotice.setText(returnValue.getData().getMessage());
        } else {
            mTvNotice.setVisibility(View.GONE);
        }
    }

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
                .withText(shareContent)
                .withTitle(shareTitle)
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
     * 设置三方跳转的时候的dialog
     */
    private void setDialog() {
        JoDialog dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        Config.dialog = dialog;
    }


    /**
     * 分享的监听器
     */
    private UMShareListener umShareListener = new UMShareListener() {
        @Override
        public void onResult(SHARE_MEDIA platform) {
//            Toast.makeText(SpecialLectureActivity.this, platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
            sharepresenter.sharePresentScore();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {
//            Toast.makeText(SpecialLectureActivity.this, platform + " 分享失败啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {
//            Toast.makeText(SpecialLectureActivity.this, platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 发送消息到服务器进行推送
     * vipfd(1:VIP聊天,0:普通[默认0，特约讲堂此参数为空])
     */
    public void sendMsg(String sessionID, String type, String msg, String giftid, String giftnum) {
        String vipfd = "";
        if (tid.equals(Constants.DEFAULT_TID)) {
            vipfd = "";
        } else {
            vipfd = (tid.contains("vip")) ? "1" : "0";
        }

        AppObservable.bindActivity(this, _apiManager.getService().sendMsg(sessionID, type, tid, StringUtil.handlerSendMsg(msg), vipfd, giftid, giftnum, Constants.ANDROID))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::sendMsgResult, throwable -> {
                    Log.e(TAG, getString(R.string.register_fail));
                    ToastUtil.showShort(this, this.getString(R.string.register_fail));
                });
    }

    /**
     * 返回发送结果
     */
    private void sendMsgResult(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null) {
            sendMsgSucc(returnValue);
        }
    }

    /**
     * 收藏房间
     */
    private void addCollectRooms() {
        if (_mApplication.getUserInfo().getIsLogin() != Constants.IS_LOGIN) {
            startActivity(new Intent(this, LoginActivity.class));
            return;
        }
        AppObservable.bindActivity(this, _apiManager.getService().addRoomCollect(_mApplication.getUserInfo().getSessionID(), tid))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultCollectRoom, throwable -> {
                    Log.e(TAG, "addCollectRoom: ", throwable);
                    this.onError(ChatRequestType.FANS, throwable);
                });
    }

    private void resultCollectRoom(ReturnValue<MessageBean> returnValue) {
        this.onSuccess(ChatRequestType.FANS, returnValue);
    }

    @Override
    public void showMessage(MqttMessage msg) {

    }

    @Override
    public void showImData(List<ChatMsgBean> msgList) {

    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void sendMsgSucc(ReturnValue<MessageBean> returnValue) {
        if (returnValue.getMsg().equals(Constants.SUCCESS)) {
            if (currentType.equals(MSG)) {
                mChatInput.clearEdit();
                mChatInput.hideKeyBoard();
                mChatInput.updateView(ChatInput.InputMode.NONE);
            }
        } else {
            ToastUtil.showShort(this, returnValue.getData().getMessage());
            if (currentType.equals(MSG)) {
                mChatInput.hideKeyBoard();
                mChatInput.updateView(ChatInput.InputMode.NONE);
            }
            if (getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
                startActivity(new Intent(this, LoginActivity.class));
            }
        }
    }

    @Override
    public void sendMsg() {
        currentType = MSG;
        sendMsg(_mApplication.getUserInfo().getSessionID(), MSG, mChatInput.getMsg().toString(), "", "");
    }

    @Override
    public void sendGift(String giftIndex, String giftNum) {
        currentType = GIFT;
        sendMsg(_mApplication.getUserInfo().getSessionID(), GIFT, "", giftIndex, giftNum);
    }

    @Override
    public void showRanking() {
        EventBus.getDefault().postSticky(new AnyEventType(tid));
        startActivity(new Intent(this, RankingListActivity.class));
    }

    @Override
    public void showVip() {
        if (_mApplication.getUserInfo().getIsLogin() == 1) {
            EventBus.getDefault().postSticky(new AnyEventType(tid));
            startActivity(new Intent(this, MemberAreaActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    public void showRecharge() {
        startActivity(new Intent(this, RechargeActivity.class));
    }

    @Override
    public void addCollectRoom() {
        addCollectRooms();
    }

    @Override
    public void onSuccess(ChatRequestType type, Object object) {
        switch (type) {
            case FANS:
                ReturnValue<MessageBean> returnValue = (ReturnValue<MessageBean>) object;
                if (returnValue.getMsg().equals(Constants.ERROR)) {
                    mChatInput.showFanDialog(returnValue.getData().getMessage());
                } else {
                    mChatInput.showFanDialog(getString(R.string.had_fan));
                }
                break;
        }
    }

    @Override
    public void onError(ChatRequestType type, Object object) {

    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }
}
