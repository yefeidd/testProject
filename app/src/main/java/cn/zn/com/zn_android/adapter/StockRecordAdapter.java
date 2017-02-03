package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.StockRecordItemBean;
import cn.zn.com.zn_android.uiclass.activity.MarketDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.TransactionDetailActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/9/12 0012.
 */
public class StockRecordAdapter extends ArrayAdapter<StockRecordItemBean> {
    private Context mContext;
    private List<StockRecordItemBean> data = new ArrayList<>();
    private int resId = R.layout.item_stock_record;

    public StockRecordAdapter(Context context, int resource, List<StockRecordItemBean> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_stock_record, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StockRecordItemBean bean = data.get(position);

        holder.mTvNameCode.setText(bean.getCode_name() + "（" + bean.getCode_id() + "）");
        holder.mTvNameCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OptionalStockBean stockBean = new OptionalStockBean();
                stockBean.setCode(bean.getCode_id());
                stockBean.setName(bean.getCode_name());
                EventBus.getDefault().postSticky(new AnyEventType(stockBean));
                mContext.startActivity(new Intent(mContext, MarketDetailActivity.class));
            }
        });

        holder.mTvProfitLoss.setText(bean.getWin_lose());
        holder.mTvGains.setText(bean.getSy() + "%");
        if (bean.getSy().startsWith("-")){
            holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.green_down));
            holder.mTvGains.setTextColor(mContext.getResources().getColor(R.color.green_down));
        } else {
            holder.mTvProfitLoss.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
            holder.mTvGains.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
        }
        holder.mTvBuyInAmount.setText(bean.getBuy_all_money());
        holder.mTvSellOutAmount.setText(bean.getSell_all_money());

        holder.mTvTransactionDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(
                        bean.getCode_id()).setTid(mContext.getString(R.string.transaction_detail)).setStockCode(bean.getCode_name()));
                mContext.startActivity(new Intent(mContext, TransactionDetailActivity.class));
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name_code)
        TextView mTvNameCode;
        @Bind(R.id.tv_profit_loss)
        TextView mTvProfitLoss;
        @Bind(R.id.tv_gains)
        TextView mTvGains;
        @Bind(R.id.tv_buy_in_amount)
        TextView mTvBuyInAmount;
        @Bind(R.id.tv_sell_out_amount)
        TextView mTvSellOutAmount;
        @Bind(R.id.tv_transaction_detail)
        TextView mTvTransactionDetail;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
