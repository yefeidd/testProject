package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.AskQuestionModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 选择支付金额
 * Created by Jolly on 2016/11/25.
 */

public class ChoosePriceActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.rb_znb2)
    RadioButton mRbZnb2;
    @Bind(R.id.rb_znb5)
    RadioButton mRbZnb5;
    @Bind(R.id.rg_choose_price1)
    RadioGroup mRgChoosePrice1;
    @Bind(R.id.rb_znb10)
    RadioButton mRbZnb10;
    @Bind(R.id.rb_znb15)
    RadioButton mRbZnb15;
    @Bind(R.id.rb_znb25)
    RadioButton mRbZnb25;
    @Bind(R.id.rg_choose_price2)
    RadioGroup mRgChoosePrice2;
    @Bind(R.id.btn_pay_now)
    Button mBtnPayNow;
    @Bind(R.id.iv_znb)
    ImageView mIvZnb;

    private AskQuestionModel aQModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_choose_price);
    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof AskQuestionModel && null == aQModel) {
            aQModel = (AskQuestionModel) event.getObject();
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.clearCaches();
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

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.choose_price);
        if (!aQModel.getNiubi().equals("")) {
            mRgChoosePrice1.setVisibility(View.GONE);
            mRgChoosePrice2.setVisibility(View.GONE);
            mIvZnb.setVisibility(View.VISIBLE);
            String resName = "ic_teacher_znb_" + aQModel.getNiubi();
            mIvZnb.setImageBitmap(BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier(resName, "drawable", getPackageName())));
        } else {
            aQModel.setNiubi("10");
        }
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mBtnPayNow.setOnClickListener(this);
        mRgChoosePrice1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_znb2:
                        if (mRbZnb2.isChecked()) {
                            aQModel.setNiubi("2");
                            mRgChoosePrice2.clearCheck();
                        }
                        break;
                    case R.id.rb_znb5:
                        if (mRbZnb5.isChecked()) {
                            aQModel.setNiubi("5");
                            mRgChoosePrice2.clearCheck();
                        }
                        break;
                }
            }
        });
        mRgChoosePrice2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_znb10:
                        if (mRbZnb10.isChecked()) {
                            aQModel.setNiubi("10");
                            mRgChoosePrice1.clearCheck();
                        }
                        break;
                    case R.id.rb_znb15:
                        if (mRbZnb15.isChecked()) {
                            aQModel.setNiubi("15");
                            mRgChoosePrice1.clearCheck();
                        }
                        break;
                    case R.id.rb_znb25:
                        if (mRbZnb25.isChecked()) {
                            aQModel.setNiubi("25");
                            mRgChoosePrice1.clearCheck();
                        }
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.btn_pay_now:
                if (!TextUtils.isEmpty(aQModel.getNiubi())) {
                    askQuestion();
                } else {
                    ToastUtil.showShort(this, "请选择提问的星级");
                }
                break;
        }
    }

    private void askQuestion() {
        _apiManager.getService().askQuestion(_mApplication.getUserInfo().getSessionID(),
                aQModel.getTid(), aQModel.getPtype(),aQModel.getNiubi(), aQModel.getScode(), aQModel.getSname(), aQModel.getScosts(),
                aQModel.getPdescription(), aQModel.getForm())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    if (retValue.getMsg().equals("success") && retValue.getData().getStatus().equals("success")) {
                        Intent intent = new Intent(this, OrderConfirmActivity.class);
                        intent.putExtra("orderNum", retValue.getData().getOrder_num());
                        startActivity(intent);
                    } else {
                        ToastUtil.showShort(_Activity, "提交订单失败，请稍后重试");
                    }
                }, throwable -> {
                    Log.e(TAG, "askQuestion: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().askQuestion(_mApplication.getUserInfo().getSessionID(),
//                aQModel.getTid(), aQModel.getPtype(),aQModel.getNiubi(), aQModel.getScode(), aQModel.getSname(), aQModel.getScosts(),
//                aQModel.getPdescription(), aQModel.getForm()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    if (retValue.getMsg().equals("success") && retValue.getData().getStatus().equals("success")) {
//                        Intent intent = new Intent(this, OrderConfirmActivity.class);
//                        intent.putExtra("orderNum", retValue.getData().getOrder_num());
//                        startActivity(intent);
//                    } else {
//                        ToastUtil.showShort(_Activity, "提交订单失败，请稍后重试");
//                    }
//                }, throwable -> {
//                    Log.e(TAG, "askQuestion: ", throwable);
//                });
    }
}
