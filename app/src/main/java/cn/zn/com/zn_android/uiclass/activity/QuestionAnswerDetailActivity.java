package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ImageGVAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.QADetailBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.QuestionAnswerDetailPresenter;
import cn.zn.com.zn_android.presenter.requestType.QADetailType;
import cn.zn.com.zn_android.uiclass.SmartLinearLayout;
import cn.zn.com.zn_android.uiclass.customerview.ObservableScrollListener;
import cn.zn.com.zn_android.uiclass.customerview.ObservableScrollView;
import cn.zn.com.zn_android.uiclass.xlistview.XScrollView;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.QuestionAnswerDetailView;
import de.greenrobot.event.EventBus;

/**
 * 问答详情页，问股评价
 * Created by Jolly on 2016/12/2.
 */

public class QuestionAnswerDetailActivity extends BaseMVPActivity<QuestionAnswerDetailView, QuestionAnswerDetailPresenter>
        implements QuestionAnswerDetailView, View.OnClickListener, ObservableScrollListener,
        ObservableScrollView.OnScrollToBottomListener, AdapterView.OnItemClickListener,
        XScrollView.IXScrollViewListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.osv_detail)
    ObservableScrollView mOsvDetail;
    @Bind(R.id.ratingBar)
    RatingBar mRatingBar;
    @Bind(R.id.tv_commit)
    TextView mTvCommit;
    @Bind(R.id.sll_comment)
    SmartLinearLayout mSllComment;
    @Bind(R.id.sdv_big_img)
    SimpleDraweeView mSdvBigImg;

    private ViewHolder mHolder;
    private boolean isDetail = false; // true问答详情页，false问股评价
    private String id = "";
    private List<String> imglist = new ArrayList<>();
    private String tid = "";
    private int is_back = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    public void onEventMainThread(AnyEventType event) {
        if (event != null) {
            isDetail = event.getState();
            id = event.getTid();
            is_back = event.getType();
        }
        EventBus.getDefault().cancelEventDelivery(this);
    }

    @Override
    protected void initView() {
        presenter.attach(this);
        mIvLeftmenu.setOnClickListener(this);
        mToolbarTitle.setText(R.string.question_answer_detail);

        if (isDetail) {
            mSllComment.setVisibility(View.GONE);
        } else {
            mSllComment.setVisibility(View.VISIBLE);
        }

        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_question_answer_detail, null, false);
        mHolder = new ViewHolder(contentView);
        mOsvDetail.setView(contentView);
        mOsvDetail.setOnScrollListener(this);
        mOsvDetail.setOnScrollToBottomLintener(this);
        mOsvDetail.setFocusable(false);
        mHolder.mGvImgs.setFocusable(false);
    }

    @Override
    protected void initEvent() {
        mOsvDetail.setPullLoadEnable(false);
        mOsvDetail.setIXScrollViewListener(this);
        mHolder.mGvImgs.setOnItemClickListener(this);
        mSdvBigImg.setOnClickListener(this);
        mHolder.mSdvAvatar.setOnClickListener(this);
        mHolder.mTvName.setOnClickListener(this);
        mTvCommit.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        presenter.queryQADetail(_mApplication.getUserInfo().getSessionID(), id);
    }

    @Override
    public QuestionAnswerDetailPresenter initPresenter() {
        return new QuestionAnswerDetailPresenter(this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_question_answer_detail;
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
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_commit:
                presenter.postEvaluate(_mApplication.getUserInfo().getSessionID(), id, mRatingBar.getNumStars() + "");
                break;
            case R.id.sdv_big_img:
                mSdvBigImg.setVisibility(View.GONE);
                break;
            case R.id.sdv_avatar:
            case R.id.tv_name:
                HotLiveBean hotLiveBean = new HotLiveBean();
                hotLiveBean.setTid(tid);
//                hotLiveBean.setOrdurl(roomBeanList.get(position - 1).getOrdurl());
                hotLiveBean.setAvatars("");
                hotLiveBean.setTitle(mHolder.mTvName.getText().toString());
                hotLiveBean.setRoom_number("");
                hotLiveBean.setCollect("");
                hotLiveBean.setClick("");
                hotLiveBean.setPlacard("");
                hotLiveBean.setCurrentItem(2);
                EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
                startActivity(new Intent(this, TeacherLiveActivity.class));
                break;
        }
    }

    @Override
    public void onScrollChanged(int l, int t, int oldl, int oldt) {

    }

    @Override
    public void onScrollBottomListener(boolean isBottom) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        mSdvBigImg.setVisibility(View.VISIBLE);
//        mSdvBigImg.setImageURI(Uri.parse(mHolder.mGvImgs.getAdapter().getItem(position).toString()));
//        mSdvBigImg.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        EventBus.getDefault().postSticky(new AnyEventType(imglist).setType(position));
        startActivity(new Intent(this, PhotoVPActivity.class));
        overridePendingTransition(R.anim.scale_large_enter, R.anim.scale_small_exit);
    }

    @Override
    public void onSuccess(QADetailType requestType, Object object) {
        switch (requestType) {
            case QA_DETAIL:
                QADetailBean detailBean = (QADetailBean) object;
                updateUI(detailBean);
                break;
            case POST_EVALUATE:
                ReturnValue<MessageBean> ret = (ReturnValue<MessageBean>) object;
                if (ret.getMsg().equals("success")) {
                    ToastUtil.showShort(this, ret.getData().getMessage());
                    finish();
                } else {
                    ToastUtil.showShort(this, ret.getData().getMessage());
                }
                break;
        }
    }

    @Override
    public void onError(QADetailType requestType, Throwable t) {

    }

    private void updateUI(QADetailBean detailBean) {
        mOsvDetail.stopRefresh();

        tid = detailBean.getTid();

        mHolder.mTvQuestion.setText(detailBean.getAsk());
        mHolder.mTvQuestioner.setText(detailBean.getUser_name());
        mHolder.mTvQuestionTime.setText(detailBean.getTtime());
        String resName = "answer_star" + detailBean.getStar();
        mHolder.mIvMark.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                getResources().getIdentifier(resName, "drawable", getPackageName())));

        if (is_back != Constants.IS_BACK) {
            mHolder.mLlAnswer.setVisibility(View.VISIBLE);

            mHolder.mSdvAvatar.setImageURI(Uri.parse(detailBean.getTeacher_headimg()));
            mHolder.mTvName.setText(detailBean.getTeacher_name());
            mHolder.mTvStyle.setText(String.format(getString(R.string.qa_detail_op), detailBean.getTeacher_operation()));
            mHolder.mTvAnswerTime.setText(detailBean.getHtime());

            mHolder.mTvTrendShort.setText(String.format(getString(R.string.qa_detail_short), detailBean.getStrend()));
            mHolder.mTvTrendMiddle.setText(String.format(getString(R.string.qa_detail_middle), detailBean.getMtrend()));

            String tech = String.format(getString(R.string.qa_detail_tech_analyse), detailBean.getTanalysis());
            SpannableString techSS = new SpannableString(tech);
            techSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)), 0, 5, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mHolder.mTvTechAnalyse.setText(techSS);

            String base = String.format(getString(R.string.qa_detail_base_analyse), detailBean.getFanalysis());
            SpannableString baseSS = new SpannableString(base);
            baseSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)), 0, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            mHolder.mTvBaseAnalyse.setText(baseSS);

            if (!TextUtils.isEmpty(detailBean.getSupplement())) {
                String supple = String.format(getString(R.string.qa_detail_supplement), detailBean.getSupplement());
                SpannableString suppleSS = new SpannableString(supple);
                suppleSS.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                mHolder.mTvSupplement.setText(suppleSS);
            }

            imglist.clear();

            if (null != detailBean.getImg1() && !TextUtils.isEmpty(detailBean.getImg1())) {
                imglist.add(detailBean.getImg1());
            }
            if (null != detailBean.getImg2() && !TextUtils.isEmpty(detailBean.getImg2())) {
                imglist.add(detailBean.getImg2());
            }
            if (null != detailBean.getImg3() && !TextUtils.isEmpty(detailBean.getImg3())) {
                imglist.add(detailBean.getImg2());
            }
            ImageGVAdapter adapter = new ImageGVAdapter(this, imglist);
            mHolder.mGvImgs.setAdapter(adapter);
        } else {
            mHolder.mLlAnswer.setVisibility(View.GONE);
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
        @Bind(R.id.ll_answer)
        LinearLayout mLlAnswer;
        @Bind(R.id.iv_znb)
        ImageView mIvZnb;
        @Bind(R.id.tv_question)
        TextView mTvQuestion;
        @Bind(R.id.iv_mark)
        ImageView mIvMark;
        @Bind(R.id.tv_questioner)
        TextView mTvQuestioner;
        @Bind(R.id.tv_question_time)
        TextView mTvQuestionTime;
        @Bind(R.id.rl_question)
        RelativeLayout mRlQuestion;
        @Bind(R.id.sdv_avatar)
        SimpleDraweeView mSdvAvatar;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_answer_time)
        TextView mTvAnswerTime;
        @Bind(R.id.tv_style)
        TextView mTvStyle;
        @Bind(R.id.tv_trend_short)
        TextView mTvTrendShort;
        @Bind(R.id.tv_trend_middle)
        TextView mTvTrendMiddle;
        @Bind(R.id.tv_tech_analyse)
        TextView mTvTechAnalyse;
        @Bind(R.id.tv_base_analyse)
        TextView mTvBaseAnalyse;
        @Bind(R.id.tv_supplement)
        TextView mTvSupplement;
        @Bind(R.id.gv_imgs)
        GridView mGvImgs;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
