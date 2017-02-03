package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.AddPhotoAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.PostAnswerBean;
import cn.zn.com.zn_android.model.bean.QuestionDetailBean;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.customerview.widget.PhotoPickerActivity;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;
import de.greenrobot.event.EventBus;

import static cn.zn.com.zn_android.adapter.AddPhotoAdapter.maxNum;

/**
 * Created by zjs on 2016/11/28 0028.
 * email: m15267280642@163.com
 * explain: 老师回答的详情页面
 */

public class TeacherAnswerDetailActivity extends BaseActivity implements View.OnClickListener, DiagnosedStockView, RadioGroup.OnCheckedChangeListener {
    private static final int PICK_PHOTO = 1;


    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_question)
    TextView mTvQuestion;
    @Bind(R.id.iv_level)
    ImageView mIvLevel;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_preview)
    TextView mTvPreview;
    @Bind(R.id.rg_short)
    RadioGroup mRgShort;
    @Bind(R.id.rg_middle)
    RadioGroup mRgMiddle;
    @Bind(R.id.tv_technology)
    TextView mTvTechnology;
    @Bind(R.id.et_technology)
    EditText mEtTechnology;
    @Bind(R.id.tv_basic)
    TextView mTvBasic;
    @Bind(R.id.et_basic)
    EditText mEtBasic;
    @Bind(R.id.tv_supplement)
    TextView mTvSupplement;
    @Bind(R.id.gv_pic)
    GridView mGvPic;
    @Bind(R.id.et_supplement)
    EditText mEtSupplement;
    @Bind(R.id.tv_04)
    TextView mTv04;
    @Bind(R.id.tv_06)
    TextView mTv06;

    private List<String> mResults;
    private AddPhotoAdapter mAdapter;
    private ArrayList<String> result = new ArrayList<>();
    private String id;
    private DiagnosesSocketPresenter presenter;
    private PostAnswerBean postAnswerBean = new PostAnswerBean();
    private boolean isRightLayout1 = false;
    private boolean isRightLayout2 = false;
    private String type = "PreAnswer";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_teacher_answer);
    }

    @Override
    protected void initView() {
        presenter = new DiagnosesSocketPresenter(this, this);
        id = getIntent().getStringExtra("id");
        type = getIntent().getStringExtra("type");
        presenter.queryTeacherAnswer(id);
        mToolbarTitle.setText(getString(R.string.teacher_answer));
        ((RadioButton) findViewById(R.id.rb_01)).setChecked(true);
        ((RadioButton) findViewById(R.id.rb_003)).setChecked(true);
        postAnswerBean.setStrend(1);
        postAnswerBean.setMtrend(3);
        showResult(result);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvPreview.setOnClickListener(this);
        mRgMiddle.setOnCheckedChangeListener(this);
        mRgShort.setOnCheckedChangeListener(this);
        mEtTechnology.addTextChangedListener(new MyTextWatcher(mEtTechnology));
        mEtBasic.addTextChangedListener(new MyTextWatcher(mEtBasic));

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_preview:
                if (isRightLayout1 && isRightLayout2) {
                    setCacheData();
                    EventBus.getDefault().postSticky(new AnyEventType(postAnswerBean));
                    startActivity(new Intent(this, AnswerPreviewActivity.class));
                } else {
                    ToastUtil.show(this, "请检查输入的内容是否符合规范", Toast.LENGTH_SHORT);
                }
            default:
                break;
        }
    }


    private void setCacheData() {
        postAnswerBean.setId(id);
        postAnswerBean.setFrom("1");
        postAnswerBean.setTranalysis(mEtTechnology.getText().toString());
        postAnswerBean.setFanalysis(mEtBasic.getText().toString());
        postAnswerBean.setSupplement(mEtSupplement.getText().toString());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PHOTO) {
            if (resultCode == RESULT_OK) {
                result = data.getStringArrayListExtra(PhotoPickerActivity.KEY_RESULT);
                showResult(result);
            }
        }
    }


    private void showResult(ArrayList<String> paths) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        if (mResults.size() >= maxNum) {
            mResults.clear();
            mResults.addAll(paths);
        } else {
            paths.addAll(mResults);
            mResults = paths;
        }

        if (mAdapter == null) {
            mAdapter = new AddPhotoAdapter(mResults, this);
            mGvPic.setAdapter(mAdapter);
        } else {
            mAdapter.setPathList(mResults);
            mAdapter.notifyDataSetChanged();
        }
        if (null != paths) {
            switch (paths.size()) {
                case 1:
                    postAnswerBean.setImg1(paths.get(0));
                    break;
                case 2:
                    postAnswerBean.setImg1(paths.get(0));
                    postAnswerBean.setImg2(paths.get(1));
                    break;
                case 3:
                    postAnswerBean.setImg1(paths.get(0));
                    postAnswerBean.setImg2(paths.get(1));
                    postAnswerBean.setImg3(paths.get(2));
                    break;
                default:
                    break;
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
    public void onSuccess(DiagnosedType requestType, Object object) {
        if (requestType == DiagnosedType.ANSWER_DETAIL) {
            QuestionDetailBean bean = (QuestionDetailBean) object;
            String str = bean.getQuestion();
            if (str.length() >= 30) {
                str = str.substring(0, 30);
                str = str + "...";
            }
            mTvQuestion.setText(str);
            setStarLevel(bean.getStart(), mIvLevel);
            mTvUserName.setText(bean.getName());
            mTvTime.setText(bean.getTime());
            postAnswerBean.setAsk(bean.getQuestion());
            postAnswerBean.setStar(bean.getStart());
            postAnswerBean.setNickName(bean.getName());
            postAnswerBean.setTime(bean.getTime());

        }
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {
        if (requestType == DiagnosedType.ANSWER_DETAIL) {
            Log.i(TAG, "onError: " + t);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    /**
     * 设置星星
     *
     * @param star
     * @param view
     */
    private void setStarLevel(String star, ImageView view) {
        switch (star) {
            case "1":
                view.setImageResource(R.drawable.answer_star1);
                break;
            case "2":
                view.setImageResource(R.drawable.answer_star2);

                break;
            case "3":
                view.setImageResource(R.drawable.answer_star3);

                break;
            case "4":
                view.setImageResource(R.drawable.answer_star4);

                break;
            case "5":
                view.setImageResource(R.drawable.answer_star5);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (group.getId()) {
            case R.id.rg_short:
                switch (checkedId) {
                    case R.id.rb_01:
                        postAnswerBean.setStrend(1);
                        break;
                    case R.id.rb_02:
                        postAnswerBean.setStrend(2);
                        break;
                    case R.id.rb_03:
                        postAnswerBean.setStrend(3);
                        break;
                    case R.id.rb_04:
                        postAnswerBean.setStrend(4);
                        break;
                    case R.id.rb_05:
                        postAnswerBean.setStrend(5);
                        break;
                }
                break;
            case R.id.rg_middle:
                switch (checkedId) {
                    case R.id.rb_001:
                        postAnswerBean.setMtrend(1);
                        break;
                    case R.id.rb_002:
                        postAnswerBean.setMtrend(2);
                        break;
                    case R.id.rb_003:
                        postAnswerBean.setMtrend(3);
                        break;
                    case R.id.rb_004:
                        postAnswerBean.setMtrend(4);
                        break;
                    case R.id.rb_005:
                        postAnswerBean.setMtrend(5);
                        break;
                }
                break;
        }
    }


    private boolean isRightFormat(TextView view, CharSequence str) {
        if (str.length() < 15) {
            view.setText("不能少于15个字");
            view.setTextColor(getResources().getColor(R.color.text_red));
            return false;
        } else if (str.length() >= 250) {
            view.setText("不能大于250个字");
            view.setTextColor(getResources().getColor(R.color.text_red));
            return false;
        } else {
            view.setText("输入合法");
            view.setTextColor(getResources().getColor(R.color.text_gray));
            return true;
        }
    }

    public class MyTextWatcher implements TextWatcher {

        private EditText edittext;
        private CharSequence temp;

        public MyTextWatcher(EditText editText) {
            this.edittext = editText;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (edittext.getId()) {
                case R.id.et_basic:
                    temp = s;
                    break;
                case R.id.et_technology:
                    temp = s;
                    break;
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (edittext.getId()) {
                case R.id.et_basic:
                    isRightLayout1 = isRightFormat(mTv06, temp);
                    break;
                case R.id.et_technology:
                    isRightLayout2 = isRightFormat(mTv04, temp);
                    break;
            }
        }
    }
}
