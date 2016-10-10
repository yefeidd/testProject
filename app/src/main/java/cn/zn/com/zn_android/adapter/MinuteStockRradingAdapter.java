package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.MinuteTradingBean;
import cn.zn.com.zn_android.utils.UnitUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjs on 2016/7/15 0015.
 * email: m15267280642@163.com
 * explain:
 */
public class MinuteStockRradingAdapter extends ArrayAdapter<MinuteTradingBean> {

    private int resId;
    private Context mContext;
    private ViewHolder viewHolder;
    private ArrayList<MinuteTradingBean> tradingDataList;
    private float itemHeight;

    public MinuteStockRradingAdapter(Context context, int resource, List<MinuteTradingBean> objects, float itemHeight) {
        super(context, resource, objects);
        this.resId = resource;
        this.mContext = context;
        this.tradingDataList = (ArrayList<MinuteTradingBean>) objects;
        this.itemHeight = itemHeight;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
//            Log.e("ChatAdapter", "convertView: " + convertView);
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
//            String str = viewHolder.mTvContent.getText().toString();
//            Log.e("getview", "getView: " + str);
        } else {
//            Log.e("ChatAdapter", "resId: " + resId);
            view = LayoutInflater.from(mContext).inflate(resId, new ListView(mContext), false);
            LinearLayout ll = (LinearLayout) view.findViewById(R.id.ll_item);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) ll.getLayoutParams();
            layoutParams.height = (int) Math.floor(itemHeight);
            ll.setLayoutParams(layoutParams);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        }
        MinuteTradingBean minutesBean = tradingDataList.get(position);
        viewHolder.mTvTitle.setText(minutesBean.getTitle());
        if (minutesBean.getTitle().contains("买")) {
            viewHolder.mTvPrice.setTextColor(mContext.getResources().getColor(R.color.market_red));
        } else {
            viewHolder.mTvPrice.setTextColor(mContext.getResources().getColor(R.color.market_green));
        }
        viewHolder.mTvPrice.setText(String.valueOf(minutesBean.getPrice()));
        String num = minutesBean.getNumber();
        int count;
        try {
            count = Integer.valueOf(num);
            String unit = UnitUtils.getVolUnit1(count);
            float number = UnitUtils.getVol(count);
            viewHolder.mTvNumber.setText(number + unit);
        } catch (Exception e) {
            viewHolder.mTvNumber.setText(num);
        }
        return view;
    }

    public static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_number)
        TextView mTvNumber;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
