package cn.zn.com.zn_android.adapter.viewHolder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.ListviewItemModel;

import java.util.List;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class ListViewAdapter extends BaseAdapter {
    private int resId = R.layout.item_chat;
    private BaseViewHolder viewHolder;
    private Context mContext;
    private List<? extends ListviewItemModel> dataList;
    private String viewHolderName;

    public ListViewAdapter(Context context, int resource, List<? extends ListviewItemModel> objects, String name) {
        this.resId = resource;
        this.mContext = context;
        this.dataList = objects;
        this.viewHolderName = name;
    }

    public void setDataList(List<? extends ListviewItemModel> objects) {
        this.dataList = objects;
        this.notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        if (dataList != null) {
            return dataList.size();
        } else {
            return 0;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView != null) {
//            Log.e("ChatAdapter", "convertView: " + convertView);
            view = convertView;
            viewHolder = (BaseViewHolder) view.getTag();
//            viewHolder = ViewHolderFactory.createViewHoler(viewHolderName, view);
//            String str = viewHolder.mTvContent.getText().toString();
//            Log.e("getview", "getView: " + str);
        } else {
//            Log.e("ChatAdapter", "resId: " + resId);
            view = LayoutInflater.from(mContext).inflate(resId, null);
            viewHolder = ViewHolderFactory.createViewHoler(viewHolderName, view);
            view.setTag(viewHolder);
        }

        final ListviewItemModel listviewItemModel = dataList.get(position);
        listviewItemModel.showItem(viewHolder, mContext);

        return view;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

}
