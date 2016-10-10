package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;

import java.math.BigDecimal;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjs on 2016/6/28 0028.
 * email: m15267280642@163.com
 * explain:
 */
public class SelfSelectAdapter extends ArrayAdapter<MarketImp> {
    private List<MarketImp> selfDataList;
    private ViewHolder viewHolder;
    private Context mContext;
    private int RsId;
    private int stockType = 2;//0代表深交所，1代表港交所
    private boolean isTurnVolume = false;

    public void setData(List<MarketImp> selfDataList, int stockType) {
        this.stockType = stockType;
        this.selfDataList = selfDataList;
        this.notifyDataSetChanged();
    }

    public void setTurnVolume(boolean turnVolume) {
        isTurnVolume = turnVolume;
    }

    public SelfSelectAdapter(Context context, int resource, List<MarketImp> dataList) {
        super(context, resource, dataList);
        this.mContext = context;
        this.RsId = resource;
        this.selfDataList = dataList;
    }

    public SelfSelectAdapter(Context context, int resource, List<MarketImp> dataList, int stockType) {
        super(context, resource, dataList);
        this.mContext = context;
        this.RsId = resource;
        this.selfDataList = dataList;
        this.stockType = stockType;
    }

    @Override
    public int getCount() {
        if (null == selfDataList || selfDataList.size() == 0) return 0;
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(RsId, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        OptionalStockBean bean = (OptionalStockBean) selfDataList.get(position);
        if (bean.getName() != null) {
            viewHolder.mTvName.setText(bean.getName());
        } else if (null != bean.getShortNM()){
            viewHolder.mTvName.setText(bean.getShortNM());
        } else {
            viewHolder.mTvName.setText(bean.getCode_name());
        }
        if (bean.getTicker() == null) {
            viewHolder.mTvCode.setText(bean.getCode());
            if (null != bean.getCode()) {
                if (bean.getCode().length() == 5) {
                    viewHolder.mTvHk.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.mTvHk.setVisibility(View.INVISIBLE);
                }
            }
        } else {
            viewHolder.mTvCode.setText(bean.getTicker());
            if (bean.getTicker().length() == 5) {
                viewHolder.mTvHk.setVisibility(View.VISIBLE);
            } else {
                viewHolder.mTvHk.setVisibility(View.INVISIBLE);
            }
        }
        if (bean.getLastPrice() == null) {
            viewHolder.mTvPrice.setText(bean.getPrice());
        } else {
            viewHolder.mTvPrice.setText(bean.getLastPrice());
        }

        float rate = bean.getChangePct();
//        rate *= 100;
        BigDecimal r = new BigDecimal(rate);
        rate = r.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();

        if (2 != stockType) { // 不是自选股页面
            viewHolder.mTvUpDown.setBackgroundColor(Color.TRANSPARENT);
            viewHolder.mTvPrice.setTextColor(mContext.getResources().getColor(R.color.font_value_black));
//            viewHolder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
            if (isTurnVolume && stockType == Constants.HK) {
                viewHolder.mTvUpDown.setText(bean.getMoney());
                viewHolder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.font_value_black));
            } else if (rate > 0) {
                viewHolder.mTvUpDown.setText("+" + rate + "%");
                viewHolder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.app_bar_color));
            } else if (rate == 0) {
                viewHolder.mTvUpDown.setText(rate + "%");
                viewHolder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.font_value_black));
            } else if (rate + 100 == 0) {
                viewHolder.mTvUpDown.setText("停牌");
                viewHolder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.font_value_black));
            } else {
                viewHolder.mTvUpDown.setText(rate + "%");
                viewHolder.mTvUpDown.setTextColor(mContext.getResources().getColor(R.color.market_green));
            }


        } else { // 自选股页面
            if (rate > 0) {
                viewHolder.mTvUpDown.setText("+" + rate + "%");
                viewHolder.mTvUpDown.setBackgroundResource(R.drawable.sp_rect_orange);
            } else if (rate == 0) {
                viewHolder.mTvUpDown.setText(rate + "%");
                viewHolder.mTvUpDown.setBackgroundResource(R.drawable.sp_rect_corner_grey_light);
            } else if (rate + 100 == 0) {
                viewHolder.mTvUpDown.setText("停牌");
                viewHolder.mTvUpDown.setBackgroundResource(R.drawable.sp_rect_corner_grey_light);
            } else {
                viewHolder.mTvUpDown.setText(rate + "%");
                viewHolder.mTvUpDown.setBackgroundResource(R.drawable.sp_rect_green);
            }
        }
//        if (Constants.HK == bean.getmStockType()) {
//            viewHolder.mTvHk.setVisibility(View.VISIBLE);
//        } else {
//            viewHolder.mTvHk.setVisibility(View.INVISIBLE);
//        }
        return view;
    }


    static class ViewHolder {
        @Bind(R.id.tv_hk)
        TextView mTvHk;
        @Bind(R.id.tv_name)
        TextView mTvName;
        @Bind(R.id.tv_code)
        TextView mTvCode;
        @Bind(R.id.ll_name)
        LinearLayout mLlName;
        @Bind(R.id.tv_price)
        TextView mTvPrice;
        @Bind(R.id.tv_up_down)
        TextView mTvUpDown;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
