package cn.zn.com.zn_android.uiclass.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TableRow;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.BonusAdapter;
import cn.zn.com.zn_android.model.bean.BonusBean;
import cn.zn.com.zn_android.model.bean.DetailIntroductBean;
import cn.zn.com.zn_android.model.bean.HKCompDetailBean;
import cn.zn.com.zn_android.uiclass.ScrollListView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;

/**
 * Created by Jolly on 2016/7/12 0012.
 */
public class BriefFragment extends BaseFragment {
    @Bind(R.id.tv_comp_name)
    TextView mTvCompName;
    @Bind(R.id.tv_industry)
    TextView mTvIndustry;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_price)
    TextView mTvPrice;
    @Bind(R.id.tv_num)
    TextView mTvNum;
    @Bind(R.id.slv_bonus)
    ScrollListView mSlvBonus;
    @Bind(R.id.tr_price)
    TableRow mTrPrice;
    @Bind(R.id.tr_num)
    TableRow mTrNum;

    public static BriefFragment newInstance() {
        return new BriefFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_brief_intru);
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
        mSlvBonus.setFocusable(false);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void setData(Object data) {
        if (data instanceof DetailIntroductBean) {
            DetailIntroductBean bean = (DetailIntroductBean) data;
            updateUi(bean);
        } else if (data instanceof HKCompDetailBean) {
            HKCompDetailBean detailBean = (HKCompDetailBean) data;
            updateHKUi(detailBean);
        }
    }

    private void updateUi(DetailIntroductBean bean) {
        mTvCompName.setText(bean.getIntroductions().getTic_name());
        mTvIndustry.setText(bean.getIntroductions().getHy_name());
        mTvDate.setText(bean.getIntroductions().getList_date());
        mTvPrice.setText(bean.getIntroductions().getPrice() + "元");
        mTvNum.setText(bean.getIntroductions().getTic_num() + "股");

        BonusAdapter bonusAdapter = new BonusAdapter(getContext(), R.layout.item_bonus, bean.getBonus());
        mSlvBonus.setAdapter(bonusAdapter);
    }

    private void updateHKUi(HKCompDetailBean detailBean) {
        mTrPrice.setVisibility(View.GONE);
        mTrNum.setVisibility(View.GONE);

        mTvCompName.setText(detailBean.getTic_info().getTic_name());
        mTvIndustry.setText(detailBean.getTic_info().getHy_name());
        mTvDate.setText(detailBean.getTic_info().getList_date());

        BonusAdapter bonusAdapter = new BonusAdapter(getContext(), R.layout.item_bonus, getHkData(detailBean.getBonus_list()));
        mSlvBonus.setAdapter(bonusAdapter);
    }

    private List<BonusBean> getHkData(List<HKCompDetailBean.BonusListEntity> entityList) {
        List<BonusBean> data = new ArrayList<>();

        for (HKCompDetailBean.BonusListEntity entity : entityList) {
            BonusBean bean = new BonusBean();
            bean.setYear(entity.getPUBLISH_DATE());
            bean.setRemark(entity.getBONUS_PRICE());
            bean.setDate(entity.getEX_DATE());
            data.add(bean);
        }
        return data;
    }
}
