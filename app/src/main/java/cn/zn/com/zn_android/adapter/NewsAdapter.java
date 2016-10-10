package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.StockNewsBean;
import cn.zn.com.zn_android.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 新闻公告适配器
 *
 * Created by Jolly on 2016/8/29 0029.
 */
public class NewsAdapter extends ArrayAdapter<StockNewsBean> {
    private Context mContext;
    private int resId = R.layout.item_stock_news;
    private List<StockNewsBean> data = new ArrayList<>();

    public void setData(List<StockNewsBean> list) {
        this.data = list;
        this.notifyDataSetChanged();
    }

    public NewsAdapter(Context context, int resource, List<StockNewsBean> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resId = resource;
        this.data = objects;
    }

    @Override
    public int getCount() {
        if (null == data || data.size() == 0) return 0;
        return data.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(resId, null);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (data.size() > position) {
            holder.mTvTitle.setText(data.get(position).getName());
            holder.mTvTime.setText(DateUtils.getStringDate(data.get(position).getTime() * 1000, "yyyy-MM-dd HH:mm"));
//            Log.d("NewsAdapter", "time: " + data.get(position).getTime() + "\nnow: " + System.currentTimeMillis());
        }

        return view;
    }

    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_time)
        TextView mTvTime;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
