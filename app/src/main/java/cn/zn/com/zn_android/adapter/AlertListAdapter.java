package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.MyBonusesBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;

/**
 * Created by Jolly on 2016/7/4 0004.
 */
public class AlertListAdapter extends ArrayAdapter<MarketImp> {
    private List<MarketImp> marketList;
    private Context mContext;
    private int resId = R.layout.item_alert;

    public AlertListAdapter(Context context, int resource, List<MarketImp> objects) {
        super(context, resource, objects);
        this.marketList = objects;
        this.mContext = context;
        this.resId = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;

        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_alert, null, false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        if (marketList.get(position).getClass().equals(OptionalStockBean.class)) {
            // 智能预警
            OptionalStockBean bean = (OptionalStockBean) marketList.get(position);
            StringBuilder sb = new StringBuilder(bean.getName());
            sb.append("  (").append(bean.getCode()).append(")");
            viewHolder.mTvStock.setText(sb.toString());
            viewHolder.mTvStock.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null);
        } else {
            // 我的奖励  添加drawableRight
            MyBonusesBean bean = (MyBonusesBean) marketList.get(position);
            viewHolder.mTvStock.setText(bean.getName());
            viewHolder.mTvStock.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, mContext.getResources().getDrawable(R.drawable.right_arrows), null);
        }
        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_stock)
        TextView mTvStock;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
