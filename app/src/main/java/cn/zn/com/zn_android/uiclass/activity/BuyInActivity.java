package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.Selection;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.model.BuyInModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.BuyInBean;
import cn.zn.com.zn_android.model.bean.BuySellStockBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.presenter.BuyInPresenter;
import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.BuyInView;
import com.umeng.analytics.MobclickAgent;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 买入
 * <p>
 * Created by Jolly on 2016/9/12 0012.
 */
public class BuyInActivity extends BaseMVPActivity<BuyInView, BuyInPresenter> implements
        BuyInView {
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_code)
    TextView mTvCode;
    @Bind(R.id.tv_name)
    TextView mTvName;
    @Bind(R.id.iv_minus)
    ImageView mIvMinus;
    @Bind(R.id.et_price)
    EditText mEtPrice;
    @Bind(R.id.iv_add)
    ImageView mIvAdd;
    @Bind(R.id.tv_price_min)
    TextView mTvPriceMin;
    @Bind(R.id.tv_price_max)
    TextView mTvPriceMax;
    @Bind(R.id.et_num)
    EditText mEtNum;
    @Bind(R.id.tv_num_max)
    TextView mTvNumMax;
    @Bind(R.id.tv_num_0)
    TextView mTvNum0;
    @Bind(R.id.tv_num_1)
    TextView mTvNum1;
    @Bind(R.id.tv_num_all)
    TextView mTvNumAll;
    @Bind(R.id.tv_buy_in)
    TextView mTvBuyIn;
    @Bind(R.id.tv_market_buy_in)
    TextView mTvMarketBuyIn;
    @Bind(R.id.lv_sell)
    ListView mLvSell;
    @Bind(R.id.lv_buy)
    ListView mLvBuy;
    @Bind(R.id.ll_sell_buy)
    LinearLayout mLlSellBuy;

    private JoDialog buySellDialog;

    private List<BuyInModel> sellList = new ArrayList<>();
    private List<BuyInModel> buyList = new ArrayList<>();
    private ListViewAdapter sellAdapter;
    private ListViewAdapter buyAdapter;

    private String title = "";
    private String code = "";
    private int itemHeight;
    private Double maxPrice = 0.0, minPrice = 0.0;
    private int maxNum = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
    }

    public void onEventMainThread(AnyEventType event) {
        if (null != event) {
            code = event.getObject().toString();
            title = event.getTid();
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
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(title);
        mTvCode.setFocusable(true);

        if (getString(R.string.buy_in).equals(title)) {
            mTvBuyIn.setBackgroundResource(R.drawable.sp_rect_orange);
            mTvMarketBuyIn.setBackgroundResource(R.drawable.sp_rect_orange);
            mTvMarketBuyIn.setText(R.string.mark_buy_in);
        } else {
            mTvBuyIn.setBackgroundResource(R.drawable.sp_rect_blue);
            mTvMarketBuyIn.setBackgroundResource(R.drawable.sp_rect_blue);
            mTvMarketBuyIn.setText(R.string.mark_sell_out);
        }
        mTvBuyIn.setText(title);

    }

    @Override
    protected void initEvent() {
        mEtPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().equals("")) {
                    return;
                }

                Double editPrice = Double.valueOf(s.toString());

                if (maxPrice > 0 && editPrice > maxPrice) {
                    ToastUtil.showShort(getApplicationContext(), "价格过高");

                } else if (editPrice < minPrice) {
                    ToastUtil.showShort(getApplicationContext(), "价格过低");
                }
            }
        });
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            itemHeight = (mLlSellBuy.getHeight() - DensityUtil.dip2px(this, 16)) / 10;
            presenter.buySellStock(_mApplication.getUserInfo().getSessionID(),
                    code, getString(R.string.buy_in).equals(title)? "1" : "2");
        }

    }

    @Override
    public BuyInPresenter initPresenter() {
        return new BuyInPresenter(this, this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_buy_in;
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @OnClick({R.id.iv_minus, R.id.iv_add, R.id.tv_num_0, R.id.tv_num_1, R.id.tv_num_all, R.id.tv_buy_in, R.id.tv_market_buy_in})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_minus:
                if (TextUtils.isEmpty(mEtPrice.getText())) {
                    return;
                }
                String subPrice = mEtPrice.getText().toString();
                if (null != subPrice) {
                    BigDecimal currentPrice = new BigDecimal(subPrice);
                    currentPrice = currentPrice.subtract(new BigDecimal(0.01));
                    currentPrice.setScale(3, 4);
                    mEtPrice.setText(Double.toString(currentPrice.doubleValue()));
                }
                break;
            case R.id.iv_add:
                if (TextUtils.isEmpty(mEtPrice.getText())) {
                    return;
                }
                String addPrice = mEtPrice.getText().toString();
                if (null != addPrice) {
                    BigDecimal currentPrice = new BigDecimal(addPrice);
                    currentPrice = currentPrice.add(new BigDecimal(0.01));
                    currentPrice.setScale(3, 4);
                    mEtPrice.setText(Double.toString(currentPrice.doubleValue()));
                }
                break;
            case R.id.tv_num_0:
                mEtNum.setText((maxNum / 4 / 100 * 100) + "");
                break;
            case R.id.tv_num_1:
                mEtNum.setText((maxNum / 2 / 100 * 100) + "");
                break;
            case R.id.tv_num_all:
                mEtNum.setText(maxNum / 100 * 100 + "");
                break;
            case R.id.tv_buy_in:
                if (TextUtils.isEmpty(mEtNum.getText())) {
                    ToastUtil.showShort(this, "请输入购买数量");
                } else {
                    startBuySellDialog("1", "委托" + mTvBuyIn.getText());
                }
                break;
            case R.id.tv_market_buy_in:
                if (TextUtils.isEmpty(mEtNum.getText())) {
                    ToastUtil.showShort(this, "请输入购买数量");
                } else {
                    startBuySellDialog("2", mTvMarketBuyIn.getText().toString());
                }
                break;
        }
    }

    @OnClick(R.id.iv_leftmenu)
    public void onClick() {
        finish();
    }

    /**
     * 买卖确认dialog
     * @param buyType 1:委托买卖  2:市价买卖
     */
    private void startBuySellDialog(String buyType, String dialogTitle) {
        JoDialog.Builder builder = new JoDialog.Builder(this);
        builder.setCancelable(true).setBgRes(android.R.color.transparent);

        View contentView = LayoutInflater.from(this).inflate(R.layout.layout_buy_sell_dialog, null, false);
        ViewHolder viewHolder = new ViewHolder(contentView);
        viewHolder.mTvTitle.setText(dialogTitle + "确认");
//        viewHolder.mTvTitle.setText(String.format(getString(R.string.mark_buy_in_confirm), title));
        viewHolder.mTvCode.setText(String.format(getString(R.string.buy_sell_code), code));
        viewHolder.mTvName.setText(String.format(getString(R.string.buy_sell_name), mTvName.getText().toString()));
        viewHolder.mTvPrice.setText(String.format(getString(R.string.buy_sell_price), mEtPrice.getText().toString()));
        viewHolder.mTvNum.setText(String.format(getString(R.string.buy_sell_num), mEtNum.getText().toString()));

        viewHolder.mBtnBuyIn.setText(title);
        if (getString(R.string.buy_in).equals(title)) {
            viewHolder.mBtnBuyIn.setBackgroundColor(getResources().getColor(R.color.app_bar_color));
        } else {
            viewHolder.mBtnBuyIn.setBackgroundColor(getResources().getColor(R.color.sell_out_blue));
        }

        viewHolder.mBtnBuyIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = getString(R.string.buy_in).equals(title) ? "1" : "2";
                presenter.tradeStock(_mApplication.getUserInfo().getSessionID(), code,
                        mEtNum.getText().toString(), type, mTvName.getText().toString(),
                        mEtPrice.getText().toString(), buyType);
                buySellDialog.dismiss();
            }
        });
        viewHolder.mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buySellDialog.dismiss();
            }
        });

        builder.setContentView(contentView);
        buySellDialog = builder.show(true);
    }

    private void resultBuyStock(BuySellStockBean bean) {
        sellList.clear();
        buyList.clear();

        BuyInBean selloutBean5 = new BuyInBean();
        selloutBean5.setMark("卖" + 5);
        selloutBean5.setPrice(bean.getSell5());
        selloutBean5.setNum(bean.getSellnum5() + "");
        BuyInModel sellModel5 = new BuyInModel(selloutBean5, itemHeight);
        sellList.add(sellModel5);

        BuyInBean selloutBean4 = new BuyInBean();
        selloutBean4.setMark("卖" + 4);
        selloutBean4.setPrice(bean.getSell4());
        selloutBean4.setNum(bean.getSellnum4() + "");
        BuyInModel sellModel4 = new BuyInModel(selloutBean4, itemHeight);
        sellList.add(sellModel4);

        BuyInBean selloutBean3 = new BuyInBean();
        selloutBean3.setMark("卖" + 3);
        selloutBean3.setPrice(bean.getSell3());
        selloutBean3.setNum(bean.getSellnum3() + "");
        BuyInModel sellModel3 = new BuyInModel(selloutBean3, itemHeight);
        sellList.add(sellModel3);

        BuyInBean selloutBean2 = new BuyInBean();
        selloutBean2.setMark("卖" + 2);
        selloutBean2.setPrice(bean.getSell2());
        selloutBean2.setNum(bean.getSellnum2() + "");
        BuyInModel sellModel2 = new BuyInModel(selloutBean2, itemHeight);
        sellList.add(sellModel2);

        BuyInBean selloutBean1 = new BuyInBean();
        selloutBean1.setMark("卖" + 1);
        selloutBean1.setPrice(bean.getSell1());
        selloutBean1.setNum(bean.getSellnum1() + "");
        BuyInModel sellModel1 = new BuyInModel(selloutBean1, itemHeight);
        sellList.add(sellModel1);

        BuyInBean buyInBean1 = new BuyInBean();
        buyInBean1.setMark("买1");
        buyInBean1.setPrice(bean.getMai1());
        buyInBean1.setNum(bean.getMainum1() + "");
        BuyInModel buyInModel1 = new BuyInModel(buyInBean1, itemHeight);
        buyList.add(buyInModel1);

        BuyInBean buyInBean2 = new BuyInBean();
        buyInBean2.setMark("买2");
        buyInBean2.setPrice(bean.getMai2());
        buyInBean2.setNum(bean.getMainum2() + "");
        BuyInModel buyInModel2 = new BuyInModel(buyInBean2, itemHeight);
        buyList.add(buyInModel2);

        BuyInBean buyInBean3 = new BuyInBean();
        buyInBean3.setMark("买3");
        buyInBean3.setPrice(bean.getMai3());
        buyInBean3.setNum(bean.getMainum3() + "");
        BuyInModel buyInModel3 = new BuyInModel(buyInBean3, itemHeight);
        buyList.add(buyInModel3);

        BuyInBean buyInBean4 = new BuyInBean();
        buyInBean4.setMark("买4");
        buyInBean4.setPrice(bean.getMai4());
        buyInBean4.setNum(bean.getMainum4() + "");
        BuyInModel buyInModel4 = new BuyInModel(buyInBean4, itemHeight);
        buyList.add(buyInModel4);

        BuyInBean buyInBean5 = new BuyInBean();
        buyInBean5.setMark("买5");
        buyInBean5.setPrice(bean.getMai5());
        buyInBean5.setNum(bean.getMainum5() + "");
        BuyInModel buyInModel5 = new BuyInModel(buyInBean5, itemHeight);
        buyList.add(buyInModel5);

        sellAdapter = new ListViewAdapter(this, R.layout.item_buy_in, sellList, "BuyInViewHolder");
        mLvSell.setAdapter(sellAdapter);
        buyAdapter = new ListViewAdapter(this, R.layout.item_buy_in, buyList, "BuyInViewHolder");
        mLvBuy.setAdapter(buyAdapter);

        mTvCode.setText(bean.getCode_id());
        mTvName.setText(bean.getCode_name());
        mEtPrice.setText(bean.getPrice());
        Editable edit = mEtPrice.getText();
        Selection.setSelection(edit, edit.length());
        maxPrice = Double.valueOf(bean.getHeight());
        minPrice = Double.valueOf(bean.getLow());
        mTvPriceMin.setText(String.format(getString(R.string.down_stop), bean.getLow() + ""));
        mTvPriceMax.setText(String.format(getString(R.string.up_stop), bean.getHeight() + ""));

        if (getString(R.string.buy_in).equals(title)) {
            mTvNumMax.setText(String.format(getString(R.string.buy_num_max), bean.getEnable_buy() + ""));
            maxNum = bean.getEnable_buy();
        } else {
            mTvNumMax.setText(String.format(getString(R.string.sell_num_max), bean.getEnable_sell() + ""));
            maxNum = bean.getEnable_sell();
        }

    }

    @Override
    public void onSuccess(SimulativeBoardType requestType, Object object) {
        switch (requestType) {
            case BUY_STOCK:
                BuySellStockBean bean = (BuySellStockBean) object;
                resultBuyStock(bean);
                break;
            case TRADE_STOCK:
                MessageBean msg = (MessageBean) object;
                ToastUtil.showShort(this, msg.getMessage());
                break;
        }
    }

    @Override
    public void onError(SimulativeBoardType requestType, Throwable t) {

    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_market)
        TextView mTvMarket;
        @Bind(R.id.tv_code)
        TextView mTvCode;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_num)
        TextView mTvNum;
        @Bind(R.id.btn_buy_in)
        Button mBtnBuyIn;
        @Bind(R.id.btn_cancel)
        Button mBtnCancel;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
