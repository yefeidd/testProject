package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.AskQuestionModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.RegularStockBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.QuickQaPresenter;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import cn.zn.com.zn_android.viewfeatures.QuickQaView;
import de.greenrobot.event.EventBus;

/**
 * 快速问股
 * Created by Jolly on 2016/11/25.
 */

public class QuickQaActivity extends BaseMVPActivity<QuickQaView, QuickQaPresenter> implements
        QuickQaView, TextWatcher, AdapterView.OnItemClickListener {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.et_stock_name)
    EditText mEtStockName;
    @Bind(R.id.et_price)
    EditText mEtPrice;
    @Bind(R.id.et_detail)
    EditText mEtDetail;
    @Bind(R.id.cb_agreement)
    AppCompatCheckBox mCbAgreement;
    @Bind(R.id.tv_register_pro)
    TextView mTvRegisterPro;
    @Bind(R.id.ib_ask)
    ImageButton mIbAsk;
    @Bind(R.id.tv_warning_code)
    TextView mTvWarningCode;
    @Bind(R.id.tv_warning_cost)
    TextView mTvWarningCost;
    @Bind(R.id.lv_stocks)
    ListView mLvStocks;
    @Bind(R.id.rl_quick_qa)
    RelativeLayout mRlQuickQa;

    //    DropDownListPopWindow popWindow;
    private String teacherName = "", tid = "";
    private int starNum = 0;
    private ArrayAdapter dropDownAdapter;
    private String scode = "", sname = "";
    private String ptype = "1";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String && TextUtils.isEmpty(teacherName)) {
            teacherName = event.getObject().toString();
            tid = event.getTid();
            starNum = event.getType();
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
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        if (TextUtils.isEmpty(teacherName)) {
            mToolbarTitle.setText(R.string.quick_ask_stock);
            ptype = "1";
            tid = "";
        } else {
            mToolbarTitle.setText(String.format(getString(R.string.ask_teacher), teacherName));
            ptype = "2";
        }
//        popWindow = new DropDownListPopWindow(this);
        SpannableString ss = new SpannableString(getString(R.string.qa_agreement));
        ss.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.app_bar_color)),
                4, ss.length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        mTvRegisterPro.setText(ss);

        mEtDetail.setFilters(new InputFilter[]{new InputFilter.LengthFilter(250)});
    }

    @Override
    protected void initEvent() {
        mCbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mIbAsk.setBackgroundResource(R.drawable.ic_qa_ok);
                } else {
                    mIbAsk.setBackgroundResource(R.drawable.ic_qa_un);
                }
            }
        });

        mEtStockName.addTextChangedListener(this);
        mLvStocks.setOnItemClickListener(this);

        mEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) return;

                if (s.length() == 1 && (s.toString().equals("0") || s.toString().equals("."))) {
                    s.delete(s.length() - 1, s.length());
                    ToastUtil.showShort(_Activity, "请输入正确的股票成本");
                    return;
                }

                if (s.toString().contains(".")) {
                    if (s.toString().split("\\.").length > 1) {
                        String str = s.toString().split("\\.")[1];
                        if (str.length() > 2) {
                            s.delete(s.length() - 1, s.length());
                            ToastUtil.showShort(_Activity, "精确到小数点后两位就够啦");
                        }
                    }

                }

                float f = Float.valueOf(s.toString());
                if (f > 500) {
                    s.delete(s.length() - 1, s.length());
                    ToastUtil.showShort(_Activity, "请输入正确的股票成本");
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mEtStockName.setSelection(s.length());
        presenter.searchStock(s.toString(), "1", "1");
    }

    @Override
    public QuickQaPresenter initPresenter() {
        return new QuickQaPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_quick_qa;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R.id.iv_leftmenu, R.id.ib_ask, R.id.tv_register_pro, R.id.rl_quick_qa})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.ib_ask:
                if (!mCbAgreement.isChecked()) {
                    ToastUtil.showShort(this, "请阅读《证牛投顾付费服务使用条款》");
                    break;
                }
                if (TextUtils.isEmpty(mEtStockName.getText())) {
                    mTvWarningCode.setVisibility(View.VISIBLE);
                    break;
                } else {
                    mTvWarningCode.setVisibility(View.GONE);
                }

                if (TextUtils.isEmpty(mEtPrice.getText())) {
                    mTvWarningCost.setVisibility(View.VISIBLE);
                    break;
                } else {
                    mTvWarningCost.setVisibility(View.GONE);
                }

                if (mEtDetail.getText().length() < 6) {
                    showWarningDialog(getString(R.string.question_count_limit));
                    break;
                }

                if (StringUtil.isRegularStr("^[*]*[A-Z]*[\\u4e00-\\u9fa5]*[A-Z]*\\([0,3,6]\\d{5}\\)$",
                        mEtStockName.getText().toString())) {
                    presenter.isRegularStock(mEtStockName.getText().toString());
                } else {
                    ToastUtil.showShort(this, "请输入正确格式的股票名称及代码!");
                }

                break;
            case R.id.tv_register_pro:
                startActivity(new Intent(this, InvestAdviseRuleActivity.class));
                break;
            case R.id.rl_quick_qa:
                hideDropDown(true);
                break;
        }
    }

    @Override
    public void onSuccess(int requestType, Object object) {
        switch (requestType) {
            case QuickQaPresenter.STOCK_SEARCH:
                List<OptionalStockBean> stockList = (List<OptionalStockBean>) object;
                showStockDropDown(stockList);
                break;
            case QuickQaPresenter.REGULAR_STOCK:
                ReturnValue<RegularStockBean> regular = (ReturnValue<RegularStockBean>) object;
                if (regular.getMsg().equals(Constants.ERROR)) {
                    ToastUtil.showShort(this, "请输入正确格式的股票名称及代码");
                } else {
                    if (regular.getData().getMsg().equals(Constants.ERROR)) {
                        ToastUtil.showShort(this, "股票名称和代码不匹配");
                    } else {
                        String str = mEtStockName.getText().toString();
                        scode = StringUtil.findStringByRE(str, "(?<=\\()(.+?)(?=\\))");
                        sname = str.split("\\(")[0];
                        if (TextUtils.isEmpty(teacherName)) {
                            EventBus.getDefault().postSticky(new AnyEventType(new AskQuestionModel(
                                    "1", tid, "", scode, sname, mEtPrice.getText().toString(),
                                    mEtDetail.getText().toString(), "1")));
                        } else {
                            String znb = "";
                            switch (starNum) {
                                case 1:
                                    znb = "2";
                                    break;
                                case 2:
                                    znb = "5";
                                    break;
                                case 3:
                                    znb = "10";
                                    break;
                                case 4:
                                    znb = "15";
                                    break;
                                case 5:
                                    znb = "25";
                                    break;

                            }
                            EventBus.getDefault().postSticky(new AnyEventType(new AskQuestionModel(
                                    ptype, tid, znb, scode, sname, mEtPrice.getText().toString(),
                                    mEtDetail.getText().toString(), "1")));
                        }
                        startActivity(new Intent(this, ChoosePriceActivity.class));
                    }
                }
                break;
        }
    }

    @Override
    public void onError(int requestType, Throwable t) {

    }

    /**
     * 提示的dialog
     *
     * @param content
     */
    public void showWarningDialog(String content) {

        new JoDialog.Builder(this)
                .setGravity(Gravity.CENTER)
                .setPositiveTextRes(R.string.confirm)
                .setStrContent(content)
                .setColorContent(R.color.font_value_black)
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

    private void showStockDropDown(List<OptionalStockBean> stockList) {
        if (stockList.size() == 0) {
            hideDropDown(false);
            return;
        }
        if (!mLvStocks.isShown()) {
            mLvStocks.setVisibility(View.VISIBLE);
        }
        dropDownAdapter = new ArrayAdapter(this, R.layout.item_drop_down, presenter.stockListFatory(stockList));
        mLvStocks.setAdapter(dropDownAdapter);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mEtStockName.setText(dropDownAdapter.getItem(position).toString());
        hideDropDown(true);
    }

    private void hideDropDown(boolean hideKeyBoard) {
        if (mLvStocks.isShown()) {
            mLvStocks.setVisibility(View.GONE);
        }
        if (hideKeyBoard) {
            UIUtil.hidekeyBoard(mEtStockName);
        }
    }
}
