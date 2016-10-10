package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjs on 2016/6/29 0029.
 * email: m15267280642@163.com
 * explain:
 */
public class SearchMarketAdapter extends BaseAdapter {
    private List<MarketImp> marketList;
    private Context mContext;
    private ViewHolder viewHolder;
//    public interface AddSelfStockListener {
//        void addSelfStock(View view, OptionalStockBean bean);
//    }
//
//    public void setAddSelfStockListener(AddSelfStockListener addSelfStockListener) {
//        this.addSelfStockListener = addSelfStockListener;
//    }
//
//    private AddSelfStockListener addSelfStockListener;


    public void setMarketList(List<MarketImp> marketList) {
        this.marketList = marketList;
        this.notifyDataSetChanged();
    }

    public SearchMarketAdapter(Context context) {
        this.mContext = context;

    }

    public SearchMarketAdapter(Context context, List<MarketImp> marketList) {
        this.mContext = context;
        this.marketList = marketList;

    }


    @Override
    public int getCount() {
        if (marketList == null) return 0;
        return marketList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_search_market, new ListView(mContext), false);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        OptionalStockBean bean = (OptionalStockBean) marketList.get(position);
        StringBuilder sb = new StringBuilder();
        String name;
        if (bean.getName() != null) {
            name = bean.getName();
        } else {
            name = bean.getCode_name();
            bean.setName(bean.getCode_name());
        }
        sb.append(name).append("  (").append(bean.getCode()).append(")");
        viewHolder.mTvName.setText(sb.toString());
//        view.setOnClickListener(v -> {
//
//        });
//        viewHolder.mIvAddSelf.setOnClickListener(v -> {
//            addSelfStockListener.addSelfStock(v, bean);
////            ((ImageView) v).setImageResource(R.drawable.already_add);
////            ToastUtil.show(mContext, "已添加为自选", Toast.LENGTH_SHORT);
//        });
        return view;
    }

    @Override
    public Object getItem(int positlistviewion) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    static class ViewHolder {
        @Bind(R.id.tv_name)
        TextView mTvName;
//        @Bind(R.id.iv_add_self)
//        ImageView mIvAddSelf;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
