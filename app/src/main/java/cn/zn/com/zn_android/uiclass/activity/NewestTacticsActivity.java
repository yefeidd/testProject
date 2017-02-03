package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.TacticsAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.TacticsBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.umeng.socialize.Config.dialog;

/**
 * Created by zjs on 2016/12/27 0027.
 * email: m15267280642@163.com
 * explain: 最新策略页面
 */

public class NewestTacticsActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.lv_my_tactics)
    ListView mLvMyTactics;
    @Bind(android.R.id.empty)
    TextView mEmpty;
    private String tid;
    private TacticsAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_newest_tactics);
    }


    @Override
    protected void initView() {
        mToolbarTitle.setText(R.string.new_tactics);
        tid = getIntent().getStringExtra("teacherId");
        dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        queryRoomTacticsList();
        mLvMyTactics.setEmptyView(mEmpty);
    }

    @Override
    protected void initEvent() {
        mLvMyTactics.setOnItemClickListener(this);
        mIvLeftmenu.setOnClickListener(this);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(TAG);
        MobclickAgent.onResume(this);       //统计时长
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(TAG);
        MobclickAgent.onPause(this);       //统计时长
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu: //返回前一个页面
                finish();
                break;
            default:
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TacticsBean bean = (TacticsBean) mAdapter.getItem(position);
        startActivity(new Intent(this, TacticsDetailActivity.class));
        EventBus.getDefault().postSticky(new AnyEventType(bean).setState(true));
    }

    private void resultUserTactics(ReturnListValue<TacticsBean> returnListValue) {
        dialog.dismiss();
        Log.i(TAG, "resultUserTactics: ");
        List<TacticsBean> list = returnListValue.getData();
        Iterator<TacticsBean> iter = list.iterator();
        while (iter.hasNext()) {
            TacticsBean bean = iter.next();
            if (bean.getTid().equals("9898")) {
                iter.remove();
            }
        }
        mAdapter = new TacticsAdapter(this, list);
        mLvMyTactics.setAdapter(mAdapter);
    }

    /**
     * 查询老师的策略列表
     */
    private void queryRoomTacticsList() {
        _apiManager.getService().queryRoomTacticsList(tid)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultUserTactics, throwable -> {
                    dialog.dismiss();
                    Log.e(TAG, "queryRoomTacticsList: ", throwable);
                });

//        AppObservable.bindActivity(this, _apiManager.getService().queryRoomTacticsList(tid))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultUserTactics, throwable -> {
//                    dialog.dismiss();
//                    Log.e(TAG, "queryRoomTacticsList: ", throwable);
//                });
    }


}
