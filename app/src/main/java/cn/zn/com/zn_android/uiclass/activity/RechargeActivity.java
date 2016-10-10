package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.RechargeAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * 充值
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.gv_recharge)
    GridView mGvRecharge;
    @Bind(R.id.et_wealth)
    EditText mEtWealth;
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;

    private List<Map<String, String>> data = new ArrayList<>();
    private RechargeAdapter mAdapter;
    private int indexBef = -1;
    private String wealthStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_recharge);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.recharge);
        getData();
        mAdapter = new RechargeAdapter(this, data);
        mGvRecharge.setAdapter(mAdapter);

        mEtWealth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                int len = s.toString().length();
                if (len == 1 && text.equals("0")) {
                    s.clear();
                    ToastUtil.showShort(_mApplication, "输入的金额要大于0");
                }
                mEtWealth.setSelection(s.length());
            }
        });
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mGvRecharge.setOnItemClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("RechargeActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("RechargeActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.btn_recharge_now:
                if (_mApplication.getUserInfo().getIsLogin() == 0) {
                    startActivity(new Intent(this, LoginActivity.class));
                    return;
                }
                if (!TextUtils.isEmpty(mEtWealth.getText())) {
                    EventBus.getDefault().postSticky(new AnyEventType(mEtWealth.getText().toString()));
                    startActivity(new Intent(this, ChoosePayActivity.class));
                } else if (indexBef >= 0){
                    EventBus.getDefault().postSticky(new AnyEventType(data.get(indexBef).get(Constants.TITLE)));
                    startActivity(new Intent(this, ChoosePayActivity.class));
                } else {
                    ToastUtil.showShort(this, "请选择财富币数量，或者输入财富币数");
                }
                break;
        }
    }

    private void getData() {
        String[] wealths = getResources().getStringArray(R.array.recharge_nums);
        for (String str : wealths) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.TITLE, str);
            map.put(Constants.INFO, "false");
            data.add(map);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (data.get(position).get(Constants.INFO).equals("false")) {
            data.get(position).put(Constants.INFO, "true");
        } else {
            data.get(position).put(Constants.INFO, "false");
        }

        if (indexBef >= 0) {
            data.get(indexBef).put(Constants.INFO, "false");
        }
        if (indexBef == position) {
            indexBef = -1;
        } else {
            indexBef = position;
        }
        mEtWealth.setText(data.get(position).get(Constants.TITLE));
        mAdapter.notifyDataSetChanged();
    }
}
