package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.TransDetailListBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/9/9 0009.
 */
public class TransactionDetailAdapter extends ArrayAdapter<TransDetailListBean> {
    private Context mContext;
    private int resId = R.layout.item_transaction_detail;
    private List<TransDetailListBean> data = new ArrayList<>();

    public TransactionDetailAdapter(Context context, int resource, List<TransDetailListBean> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resId = resource;
        this.data = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(resId, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TransDetailListBean bean = data.get(position);
        holder.mTvDealPrice.setText(bean.getPrice());
        holder.mTvDealNum.setText(bean.getNum());
        holder.mTvDealAmount.setText(bean.getMoney());
        holder.mTvExchangeFee.setText(bean.getTax());
        holder.mTvTime.setText(bean.getTime());

        if (bean.getTrade_type().equals("1")) { // 买入
//            holder.mTvTime.setCompoundDrawables(null, null,
//                    mContext.getResources().getDrawable(R.mipmap.icon_buy_in), null);
            holder.mTvTime.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getResources().getDrawable(R.mipmap.icon_buy_in), null);
        } else { // 卖出
//            holder.mTvTime.setCompoundDrawables(null, null,
//                    mContext.getResources().getDrawable(R.mipmap.icon_sell_out), null);
            holder.mTvTime.setCompoundDrawablesWithIntrinsicBounds(null, null,
                    mContext.getResources().getDrawable(R.mipmap.icon_sell_out), null);
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_time)
        TextView mTvTime;
        @Bind(R.id.tv_deal_price)
        TextView mTvDealPrice;
        @Bind(R.id.tv_deal_num)
        TextView mTvDealNum;
        @Bind(R.id.tv_deal_amount)
        TextView mTvDealAmount;
        @Bind(R.id.tv_exchange_fee)
        TextView mTvExchangeFee;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
