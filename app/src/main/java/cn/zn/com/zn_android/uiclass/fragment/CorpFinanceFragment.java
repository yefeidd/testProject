package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.DetailFinanceBean;
import com.umeng.analytics.MobclickAgent;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/7/12 0012.
 */
public class CorpFinanceFragment extends BaseFragment {


    @Bind(R.id.tv_basic_eps)
    TextView mTvBasicEps;
    @Bind(R.id.tv_revenue)
    TextView mTvRevenue;
    @Bind(R.id.tv_oprate_profit)
    TextView mTvOprateProfit;
    @Bind(R.id.tv_income)
    TextView mTvIncome;
    @Bind(R.id.tv_t_each_assets)
    TextView mTvTEachAssets;
    @Bind(R.id.tv_t_assets)
    TextView mTvTAssets;
    @Bind(R.id.tv_t_cl)
    TextView mTvTCl;
    @Bind(R.id.tv_t_sh_equity)
    TextView mTvTShEquity;
    @Bind(R.id.tv_cf_operate)
    TextView mTvCfOperate;
    @Bind(R.id.tv_cf_invest)
    TextView mTvCfInvest;
    @Bind(R.id.tv_cf_finance)
    TextView mTvCfFinance;
    @Bind(R.id.tv_date)
    TextView mTvDate;

    public static CorpFinanceFragment newInstance() {
        return new CorpFinanceFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_corporate_finance);
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);

    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void setData(Object data) {
        if (data instanceof DetailFinanceBean) {
            DetailFinanceBean bean = (DetailFinanceBean) data;
            updateUi(bean);
        }
    }

    private void updateUi(DetailFinanceBean bean) {
        mTvDate.setText(bean.getDate());

        mTvBasicEps.setText(bean.getProfit().getBASIC_EPS());
        mTvRevenue.setText(bean.getProfit().getREVENUE());
        mTvOprateProfit.setText(bean.getProfit().getOPERATE_PROFIT());
        mTvIncome.setText(bean.getProfit().getN_INCOME());

        mTvTEachAssets.setText(bean.getDebt().getEPS());
        mTvTAssets.setText(bean.getDebt().getT_ASSETS());
        mTvTCl.setText(bean.getDebt().getT_CL());
        mTvTShEquity.setText(bean.getDebt().getT_SH_EQUITY());

        mTvCfOperate.setText(bean.getCash().getN_CF_OPERATE_A());
        mTvCfInvest.setText(bean.getCash().getN_CF_FR_INVEST_A());
        mTvCfFinance.setText(bean.getCash().getN_CF_FR_FINAN_A());
    }

}
