package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.SSStockModel;
import cn.zn.com.zn_android.uiclass.ScrollListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/6/29 0029.
 */
public class SSListAdapter extends ArrayAdapter<SSStockModel> {
    private Context mContext;
    private int resId = R.layout.item_ss_list;
    private List<SSStockModel> list;

    public void setList(List<SSStockModel> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    public SSListAdapter(Context context, int resource) {
        super(context, resource);
        this.mContext = context;
        this.resId = resource;
    }

    public SSListAdapter(Context context, int resource, List<SSStockModel> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.resId = resource;
        this.list = objects;
    }

    @Override
    public int getCount() {
        if (null == list || list.size() == 0) return 0;
        return super.getCount();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder viewHolder;
        if (null == view) {
            view = LayoutInflater.from(mContext).inflate(resId, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        list.get(position).showSSData(viewHolder, position);

        return view;
    }


    public class ViewHolder {
        @Bind(R.id.sll_ss)
        public ScrollListView mSllSs;
        @Bind(android.R.id.title)
        public TextView mTitle;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
