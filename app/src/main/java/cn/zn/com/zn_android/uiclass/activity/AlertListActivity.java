package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.AlertListAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenu;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenuCreator;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenuItem;
import cn.zn.com.zn_android.uiclass.swipexlistview.SwipeMenuListView;
import cn.zn.com.zn_android.utils.DensityUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/7/1 0001.
 */
public class AlertListActivity extends BaseActivity implements View.OnClickListener {
    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.ib_search)
    ImageButton mIbSearch;
    @Bind(R.id.xlv_alert)
    SwipeMenuListView mXlvAlert;

    private List<MarketImp> list = new ArrayList<>();
    private AlertListAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_alert_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("AlertListActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AlertListActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {
        getData();

        mToolbarTitle.setText(getString(R.string.alert));
        mIbSearch.setImageResource(R.mipmap.ic_add);
        mIbSearch.setVisibility(View.VISIBLE);

        adapter = new AlertListAdapter(this, R.layout.item_alert, list);
        mXlvAlert.setAdapter(adapter);
    }

    private void getData() {
        for (int i = 0; i < 10; i ++) {
            OptionalStockBean bean = new OptionalStockBean();
            bean.setName("证券" + i);
            bean.setCode("30010" + i);
            bean.setPrice(i + "0");
            bean.setUpDown(i + "%");

            list.add(bean);
        }
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mIbSearch.setOnClickListener(this);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // Create different menus depending on the view type
                createMenu1(menu);
            }

            private void createMenu1(SwipeMenu menu) {
                SwipeMenuItem item1 = new SwipeMenuItem(
                        getApplicationContext());
                item1.setBackground(new ColorDrawable(Color.rgb(0xff, 0xff,
                        0xff)));
                item1.setWidth(DensityUtil.dip2px(getApplicationContext(), 90));
                item1.setIcon(R.drawable.btn_delete);
                menu.addMenuItem(item1);
            }

        };
        // set creator
        mXlvAlert.setMenuCreator(creator);

        // step 2. listener item click event
        mXlvAlert.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                OptionalStockBean item = (OptionalStockBean) list.get(position);
                switch (index) {
                    case 0:
                        // open
                        list.remove(position);
                        adapter.notifyDataSetChanged();
                        break;
                }
                return false;
            }
        });

        mXlvAlert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == list.size()) return;
                if (0 == position) return;

                EventBus.getDefault().postSticky(new AnyEventType(list.get(position - 1)));
                startActivity(new Intent(getApplicationContext(), AlertSettingActivity.class));
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.ib_search:
                startActivity(new Intent(this, MarketSearchActivity.class));
                break;
        }
    }
}
