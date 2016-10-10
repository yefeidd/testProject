package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.EditOpStockAdapter;
import cn.zn.com.zn_android.model.OptionalStockModel;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.presenter.OptionalStockPresenter;
import cn.zn.com.zn_android.presenter.requestType.StockRequestType;
import cn.zn.com.zn_android.uiclass.DividerItemDecoration;
import cn.zn.com.zn_android.viewfeatures.OptionalStockView;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * 编辑自选股
 * Created by Jolly on 2016/6/28 0028.
 */
public class EditOptionalStockActivity extends BaseMVPActivity<OptionalStockView, OptionalStockPresenter>
        implements OptionalStockView, View.OnClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.rv_optional_stock)
    RecyclerView mRvOptionalStock;

    private List<OptionalStockModel> data = new ArrayList<>();
    private EditOpStockAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);

    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof List) {
            List<OptionalStockBean> selfDataList = (List<OptionalStockBean>) event.getObject();
            for (OptionalStockBean bean : selfDataList) {
                OptionalStockModel model = new OptionalStockModel(bean);
                data.add(model);
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public OptionalStockPresenter initPresenter() {
        return new OptionalStockPresenter(this, this);
    }

    @Override
    protected void initView() {
        super.initView();
        mToolbarTitle.setText(getString(R.string.market));

        adapter = new EditOpStockAdapter(mRvOptionalStock, R.layout.item_edit_optional_stock, data);
        mRvOptionalStock.setAdapter(adapter);
//        mRvOptionalStock.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        mRvOptionalStock.setLayoutManager(new LinearLayoutManager(this));
        mRvOptionalStock.setItemAnimator(new DefaultItemAnimator());

//        final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RVItemTouchCallback(adapter));
//        itemTouchHelper.attachToRecyclerView(mRvOptionalStock);

//        mRvOptionalStock.addOnItemTouchListener(new DragRVItemClickListener(mRvOptionalStock) {
//            @Override
//            public void onLongClick(RecyclerView.ViewHolder vh) {
//                if (vh.getLayoutPosition()!=getData().size()-1) {
//                    itemTouchHelper.startDrag(vh);
////                    VibratorUtil.Vibrate(getActivity(), 70);   //震动70ms
//                }
//            }
//        });
    }

    @Override
    protected void initEvent() {
        super.initEvent();
        mIbSearch.setOnClickListener(this);
        mIvLeftmenu.setOnClickListener(this);
    }

    @Override
    public int initResLayout() {
        return R.layout.activity_edit_optional_stock;
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
            case R.id.ib_search:

                break;
        }
    }

    @Override
    public void onSuccess(StockRequestType requestType, Object object) {
        switch (requestType) {
            case DEL_SELF_SELECT:

                break;
        }
    }

    @Override
    public void onError(StockRequestType requestType, Throwable t) {

    }
}
