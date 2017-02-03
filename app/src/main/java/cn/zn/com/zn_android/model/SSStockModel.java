package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.SSListAdapter;
import cn.zn.com.zn_android.adapter.SelfSelectAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.StocksBean;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.UpDownRankingActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/6/29 0029.
 */
public class SSStockModel {
    public StocksBean getBean() {
        return bean;
    }

    private StocksBean bean;
    private Context mContext;
    private final int RESID = R.layout.item_self_show;


    public SSStockModel(Context context, StocksBean bean) {
        this.bean = bean;
        this.mContext = context;
    }

    public void showSSData(SSListAdapter.ViewHolder viewHolder, int pos) {
        viewHolder.mTitle.setText(bean.getType());

        SelfSelectAdapter adapter = new SelfSelectAdapter(mContext, RESID, bean.getBeanList(), bean.getmStockType());
        if (2 == pos || 5 == pos) {
            adapter.setTurnVolume(true);
        } else {
            adapter.setTurnVolume(false);
        }
        viewHolder.mSllSs.setAdapter(adapter);
        viewHolder.mTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(bean.getType()));
                mContext.startActivity(new Intent(mContext, UpDownRankingActivity.class));
            }
        });

        viewHolder.mSllSs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                OptionalStockBean stockBean = (OptionalStockBean) bean.getBeanList().get(position);
                EventBus.getDefault().postSticky(new AnyEventType(stockBean));
                mContext.startActivity(new Intent(mContext, MarketDetailActivity.class));
            }
        });
    }
}
