package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ShareHoldersAdapter;
import cn.zn.com.zn_android.model.bean.DetailShareholderBean;
import cn.zn.com.zn_android.model.bean.TenShBean;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import com.umeng.analytics.MobclickAgent;

import java.util.List;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/7/12 0012.
 */
public class ShareholdersFragment extends BaseFragment {
    @Bind(R.id.tv_total_sh)
    TextView mTvTotalSh;
    @Bind(R.id.tv_nonrest_float)
    TextView mTvNonrestFloat;
    @Bind(R.id.slv_ten_sh)
    ScrollListView mSlvTenSh;
    @Bind(R.id.tv_title_pct)
    TextView mTvTitlePct;
    @Bind(R.id.tv_title1)
    TextView mTvTitle1;
    @Bind(R.id.ll_1)
    LinearLayout mLl1;
    @Bind(R.id.ll_2)
    LinearLayout mLl2;

    public static ShareholdersFragment newInstance() {
        return new ShareholdersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_shareholders);
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
        mSlvTenSh.setFocusable(false);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void setData(Object data) {
        if (data instanceof DetailShareholderBean) {
            DetailShareholderBean bean = (DetailShareholderBean) data;
            updateUi(bean);
        } else if (data instanceof List) {
            List<TenShBean> holderEntityList = (List<TenShBean>) data;
            updateHkUi(holderEntityList);
        }
    }

    private void updateUi(DetailShareholderBean bean) {
        mTvTotalSh.setText(bean.getEquity().getTOTAL_SHARES());
        mTvNonrestFloat.setText(bean.getEquity().getNONREST_FLOAT_A());
        mTvTitlePct.setText(getString(R.string.proportion));

        ShareHoldersAdapter adapter = new ShareHoldersAdapter(getContext(), R.layout.item_ten_sh, bean.getTen_sh());
        mSlvTenSh.setAdapter(adapter);
    }

    private void updateHkUi(List<TenShBean> holderEntities) {
        mTvTitle1.setVisibility(View.GONE);
        mLl1.setVisibility(View.GONE);
        mLl2.setVisibility(View.GONE);
        mTvTitlePct.setText(getString(R.string.data));

        ShareHoldersAdapter adapter = new ShareHoldersAdapter(getContext(), R.layout.item_ten_sh, holderEntities);
        mSlvTenSh.setAdapter(adapter);
    }

}
