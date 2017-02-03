package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.UserInfoBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.PersonPresenter;
import cn.zn.com.zn_android.uiclass.activity.AlertListActivity;
import cn.zn.com.zn_android.uiclass.activity.BillListActivity;
import cn.zn.com.zn_android.uiclass.activity.GuessMarketActivity;
import cn.zn.com.zn_android.uiclass.activity.HistoryPrivateActivity;
import cn.zn.com.zn_android.uiclass.activity.ImitateFryActivity;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.MainActivity;
import cn.zn.com.zn_android.uiclass.activity.MyCollectionActivity;
import cn.zn.com.zn_android.uiclass.activity.MyFocusActivity;
import cn.zn.com.zn_android.uiclass.activity.MyInvolvementActivity;
import cn.zn.com.zn_android.uiclass.activity.MyOrdersActivity;
import cn.zn.com.zn_android.uiclass.activity.MyQAActivity;
import cn.zn.com.zn_android.uiclass.activity.MyTacticsActivity;
import cn.zn.com.zn_android.uiclass.activity.PersonActivity;
import cn.zn.com.zn_android.uiclass.activity.QuestionaireActivity;
import cn.zn.com.zn_android.uiclass.activity.RechargeActivity;
import cn.zn.com.zn_android.uiclass.activity.SettingActivity;
import cn.zn.com.zn_android.uiclass.activity.VoucherActivity;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.PersonView;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/6/27 0027.
 */
public class PersonFragment extends BaseMVPFragment<PersonView, PersonPresenter> implements PersonView, View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.tv_leftMenu)
    TextView mTvLeftMenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.sdv_avatar)
    SimpleDraweeView mSdvAvatar;
    @Bind(R.id.tv_nick)
    TextView mTvNick;
    @Bind(R.id.tv_wealth)
    TextView mTvWealth;
    @Bind(R.id.tv_fen)
    TextView mTvFen;
    @Bind(R.id.btn_recharge)
    Button mBtnRecharge;
    @Bind(R.id.tv_foucus)
    TextView mTvFoucus;
    @Bind(R.id.tv_collect)
    TextView mTvCollect;
    @Bind(R.id.rl_person)
    RelativeLayout mRlPerson;
    @Bind(R.id.btn_msg)
    TextView mBtnMsg;
    @Bind(R.id.btn_alert)
    TextView mBtnAlert;
    @Bind(R.id.btn_tastic)
    TextView mBtnTastic;
    @Bind(R.id.btn_qa)
    TextView mBtnQa;
    @Bind(R.id.btn_orders)
    TextView mBtnOrders;
    @Bind(R.id.btn_activitis)
    TextView mBtnActivitis;
    @Bind(R.id.btn_sysmsg)
    TextView mBtnSysmsg;
    @Bind(R.id.btn_question)
    TextView mBtnQuestion;
    @Bind(R.id.tv_past)
    TextView mTvPast;
    @Bind(R.id.btn_analog)
    TextView mBtnAnalog;
    @Bind(R.id.btn_game)
    TextView mBtnGame;
    @Bind(R.id.btn_diagnosed)
    TextView mBtnRecommend;
//
//    @Bind(R.id.btn_test)
//    TextView mBtnTest;

    private int count = 0;

    public static PersonFragment newInstance() {
        PersonFragment personFragment = new PersonFragment();

        return personFragment;
    }

    @Override
    public void onResume() {
        if (null != _mApplication.getUserInfo() && _mApplication.getUserInfo().getIsLogin() == 1) {
            presenter.queryMemberInfo();
        } else {
            clearUI();
        }
        super.onResume();
    }

    @Override
    public PersonPresenter initPresenter() {
        return new PersonPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.fragment_person;
    }

    @Override
    protected void initView(View view) {
        mTvLeftMenu.setVisibility(View.VISIBLE);
        mTvLeftMenu.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_person_pay, 0 , 0, 0);
        mTvLeftMenu.setCompoundDrawablePadding(DensityUtil.dip2px(_mContext, 4));
        mTvLeftMenu.setText(R.string.bill);
        mTvLeftMenu.setOnClickListener(this);
        mIvLeftmenu.setVisibility(View.GONE);
        mToolbarTitle.setText(getString(R.string.person));
        mIbSearch.setVisibility(View.VISIBLE);
        mIbSearch.setImageResource(R.mipmap.ic_left_setting);
    }


    @Override
    protected void initEvent() {
        mIbSearch.setOnClickListener(this);
        mRlPerson.setOnClickListener(this);
        mBtnRecharge.setOnClickListener(this);
        mTvFoucus.setOnClickListener(this);
        mTvCollect.setOnClickListener(this);
        mBtnMsg.setOnClickListener(this);
        mBtnAlert.setOnClickListener(this);
        mBtnTastic.setOnClickListener(this);
        mBtnActivitis.setOnClickListener(this);
        mBtnSysmsg.setOnClickListener(this);
        mBtnQuestion.setOnClickListener(this);
        mBtnQa.setOnClickListener(this);
        mBtnOrders.setOnClickListener(this);

        mTvPast.setOnClickListener(this);
        mBtnAnalog.setOnClickListener(this);
        mBtnGame.setOnClickListener(this);
        mBtnRecommend.setOnClickListener(this);

//        mBtnTest.setOnClickListener(this);
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_leftMenu: //账单
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, BillListActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.ib_search: // 设置
                startActivity(new Intent(_mContext, SettingActivity.class));
                break;
            case R.id.rl_person: // 个人中心
                if (_mApplication.getUserInfo().getIsLogin() == 0) { //未登录
                    startActivity(new Intent(_mContext, LoginActivity.class));
                } else {
                    startActivity(new Intent(_mContext, PersonActivity.class));
                }
                break;
            case R.id.btn_msg: // 悄悄话
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, HistoryPrivateActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_alert: // 智能预警
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(getActivity(), AlertListActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_tastic: // 我的策略
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, MyTacticsActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(""));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_qa: // 我的问答
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, MyQAActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_orders: // 我的订单
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, MyOrdersActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_activitis: // 我的参与
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, MyInvolvementActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_sysmsg: // 系统消息
                if (_mApplication.getUserInfo().getIsLogin() == 1) {

                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_question: // 问卷调查
                startActivity(new Intent(_mContext, QuestionaireActivity.class));
                break;
            case R.id.tv_collect: // 收藏
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    EventBus.getDefault().postSticky(new AnyEventType(Constants.ARTICLE).setState(false));
                    startActivity(new Intent(_mContext, MyCollectionActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.tv_foucus: // 关注
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, MyFocusActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_recharge: // 充值
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, RechargeActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.tv_past: // 猜大盘
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    startActivity(new Intent(_mContext, GuessMarketActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_analog: // 模拟炒股
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    presenter.virReg(_mApplication.getUserInfo().getSessionID(), 1);
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_game: // 我的大赛
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
                    presenter.virReg(_mApplication.getUserInfo().getSessionID(), 2);
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;
            case R.id.btn_diagnosed: // 诊股券
                if (_mApplication.getUserInfo().getIsLogin() == 1) {
//                    EventBus.getDefault().postSticky(new AnyEventType(count));
                    startActivity(new Intent(_mContext, VoucherActivity.class));
                } else {
                    startActivity(new Intent(_mContext, LoginActivity.class));
                }
                break;

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        currentView = inflater.inflate(layoutRes, container, false);
        ButterKnife.bind(this, currentView);
        initView(currentView);
        initEvent();
        this.inflater = inflater;
        this.container = container;
        return currentView;
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        ButterKnife.unbind(this);
//    }

    @Override
    public void queryUserInfoResult(ReturnValue<UserInfoBean> returnValue) {
        if (returnValue == null) return;
        if (returnValue.getMsg().equals(Constants.SUCCESS)) { // 查询接口成功
//                    isFirst = false;
            if (returnValue.getData() != null) {
                _mApplication.getUserInfo().setMemberGrade(returnValue.getData().getGrade());
                _mApplication.getUserInfo().setName(returnValue.getData().getNickname());
                _mApplication.getUserInfo().setSex(returnValue.getData().getSex());
                _mApplication.getUserInfo().setBirthday(returnValue.getData().getBirthday());
                _mApplication.getUserInfo().setProvince(returnValue.getData().getProvince());
                _mApplication.getUserInfo().setCity(returnValue.getData().getCity());
                _mApplication.getUserInfo().setAvatars(returnValue.getData().getAvatars());
                _mApplication.getUserInfo().setSignature(returnValue.getData().getSignature());
                _mApplication.getUserInfo().setWealth(returnValue.getData().getWealth());
                if (!returnValue.getData().getFen().equals("")) {
                    _mApplication.getUserInfo().setFen(returnValue.getData().getFen());
                } else {
                    _mApplication.getUserInfo().setFen("0");
                }
                count = returnValue.getData().getTicket();
                initUserView();
            }
        } else { // 失败
            ToastUtil.showShort(_mApplication, "查询会员信息失败，请稍后重试");
            if (_mApplication.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
            }
        }
    }

    @Override
    public void virReg(ReturnValue<MessageBean> returnValue, int flag) {
        if (returnValue.getCode().equals("2000")) {
            switch (flag) {
                case 1:
                    startActivity(new Intent(_mContext, ImitateFryActivity.class));
                    break;
                case 2:
                    ((MainActivity) _mActivity).clickPage(5);
                    break;
            }
        }
    }

    /**
     * 显示个人信息数据
     */
    private void initUserView() {
        if (null != mTvWealth && null != mTvNick) {
            mTvWealth.setVisibility(View.VISIBLE);
            mTvNick.setText(_mApplication.getUserInfo().getName());

            String wealthLeft = String.format(getString(R.string.wealth_left), _mApplication.getUserInfo().getWealth());
            SpannableString spannable = new SpannableString(wealthLeft);
            spannable.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(_mApplication, 11)),
                    spannable.length() - 3, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvWealth.setText(spannable);

            String fen = String.format(getString(R.string.fen_left), _mApplication.getUserInfo().getFen());
            SpannableString ssFen = new SpannableString(fen);
            ssFen.setSpan(new AbsoluteSizeSpan(DensityUtil.sp2px(_mApplication, 11)),
                    ssFen.length() - 2, ssFen.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            mTvFen.setText(ssFen);
            mTvFen.setVisibility(View.VISIBLE);

            if (_mApplication.getUserInfo().getAvatars() != null) {
                mSdvAvatar.setImageURI(Uri.parse(_mApplication.getUserInfo().getAvatars()));
            }
        }
    }

    /**
     * 清除界面数据
     */
    public void clearUI() {
        mTvWealth.setVisibility(View.GONE);
        mTvFen.setVisibility(View.GONE);
        mTvNick.setText(getString(R.string.no_login));
        mSdvAvatar.setImageURI(null);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
//        if (isFirst) {
//            super.onHiddenChanged(hidden);
//        } else {
//
//        }
        Log.d(TAG, "onHiddenChanged: " + hidden);
    }

}
