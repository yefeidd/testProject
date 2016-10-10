package cn.zn.com.zn_android.uiclass.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.UserPrivateTalkBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/3/30 0030.
 * explain:
 */
public class HistoryPrivateActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_history)
    ImageButton mIbHistory;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.tv_save)
    TextView mTvSave;
    @Bind(R.id.toolbar)
    Toolbar mToolbar;
    @Bind(R.id.lv_history)
    XListView mLvHistory;
    private final String USER = "1";
    private final String TEACHER = "2";
    private int page = 1;
    private final String PACOUT = "10";
    private List<UserPrivateTalkBean> mPrivateMsgList;
    private List<UserPrivateTalkBean> mTempList;
    private userAdapter mPrivateAdapter;
    private JoDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_history_private_talk);
    }

    @Override
    protected void initView() {
        mPrivateMsgList = new ArrayList<>();
        mPrivateAdapter = new userAdapter();
        mLvHistory.setAdapter(mPrivateAdapter);
        mLvHistory.setLoadMoreEnableShow(false);
        dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("HistoryPrivateActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        mToolbarTitle.setText(R.string.my_private_msg);
        setData();
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("HistoryPrivateActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    /**
     * 请求数据和设置数据
     */
    private void setData() {
        getMsgList(page + "");
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mLvHistory.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                page++;
                setData();
            }

            @Override
            public void onLoadMore() {
                page++;
                setData();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            default:
                break;
        }
    }


    /**
     * 获取消息列表
     */
    private void getMsgList(String page) {
        AppObservable.bindActivity(this, _apiManager.getService().queryUserprivateTalkList(_mApplication.getUserInfo().getSessionID(), page, PACOUT, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultPrivateMsg, Throwable -> {
                    dialog.dismiss();
                    Throwable.printStackTrace();
                    ToastUtil.showShort(this, getString(R.string.no_net));
                    stopRefresh();
                });
    }

    private void resultPrivateMsg(ReturnListValue<UserPrivateTalkBean> returnValue) {
        dialog.dismiss();
        stopRefresh();
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            if (mTempList != null) mTempList.clear();
            mTempList = returnValue.getData();
            if (mTempList.size() == 0 || mTempList.size() < Integer.valueOf(PACOUT)) {
                mLvHistory.setLoadMoreEnableShow(false);
            } else {
                mLvHistory.setLoadMoreEnableShow(true);
            }
            if (mTempList != null && (mTempList.size() != 0)) {
                mPrivateMsgList.addAll(mTempList);
                mPrivateAdapter.notifyDataSetChanged();
            } else if (mTempList.size() == 0) {
                ToastUtil.showShort(this, getString(R.string.not_more_data));
                mLvHistory.setLoadMoreEnableShow(false);
            }
        } else {
            ToastUtil.showShort(this, returnValue.getMsg());
        }

    }


    private void stopRefresh() {
        mLvHistory.stopRefresh();
        mLvHistory.stopLoadMore();
    }

    /**
     * 悄悄话列表适配器
     */

    class userAdapter extends BaseAdapter {

        ViewHolder holder;
        private UserPrivateTalkBean privateInfo;
        private String nickName;
        private StringBuilder title;
        private SpannableString spanString;

        @Override
        public int getCount() {
            return mPrivateMsgList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = UIUtil.inflate(R.layout.item_private_talk);
                holder = new ViewHolder(view);
                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }
            privateInfo = mPrivateMsgList.get(position);
            nickName = privateInfo.getNickname();
            title = new StringBuilder();
            title.append(nickName);
            if (privateInfo.getStatus().equals(TEACHER)) {
                title.append("【老师】");
                holder.mTvContent.setBackgroundResource(R.color.chat_strock);
            } else {
                title.append("【VIP会员】");
                holder.mTvContent.setBackgroundResource(android.R.color.transparent);
            }
            spanString = new SpannableString(title);
            spanString.setSpan(new ForegroundColorSpan(Color.GRAY), 0, nickName.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (privateInfo.getStatus().equals(TEACHER)) {
                spanString.setSpan(new ForegroundColorSpan(UIUtil.getResources().getColor(R.color.red)),
                        nickName.length(), spanString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spanString.setSpan(new ForegroundColorSpan(UIUtil.getResources().getColor(R.color.vip_color)),
                        nickName.length(), spanString.length(),
                        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            holder.mTvTitle.setText(spanString);
            holder.mTvContent.setText(privateInfo.getQacontent());
            holder.mTvTime.setText("  " + privateInfo.getTimes());
            return view;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        class ViewHolder {
            @Bind(R.id.tv_title)
            TextView mTvTitle;
            @Bind(R.id.tv_time)
            TextView mTvTime;
            @Bind(R.id.tv_content)
            TextView mTvContent;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
