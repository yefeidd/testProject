package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.ImitateFryModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/9/8 0008.
 */
public class ImitateFryAdapter extends ArrayAdapter<ImitateFryModel> {
    private Context mContext;
    private int resId = R.layout.item_imitate_fry;
    private List<ImitateFryModel> data = new ArrayList<>();

    public ImitateFryAdapter(Context context, int resource, List<ImitateFryModel> objects) {
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
        ImitateFryModel model = data.get(position);
        model.updateUi(holder);

        return convertView;
    }

    public class ViewHolder {
        @Bind(R.id.tv_head)
        public TextView mTvHead;
        @Bind(R.id.tv_current_price)
        public TextView mTvCurrentPrice;
        @Bind(R.id.tv_gains)
        public TextView mTvGains;
        @Bind(R.id.tv_last_market_value)
        public TextView mTvLastMarketValue;
        @Bind(R.id.tv_avarage_cost)
        public TextView mTvAvarageCost;
        @Bind(R.id.tv_position_num)
        public TextView mTvPositionNum;
        @Bind(R.id.tv_sell_num)
        public TextView mTvSellNum;
        @Bind(R.id.tv_transaction_detail)
        public TextView mTvTransactionDetail;
        @Bind(R.id.tv_buy_in)
        public TextView mTvBuyIn;
        @Bind(R.id.tv_sell_out)
        public TextView mTvSellOut;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
