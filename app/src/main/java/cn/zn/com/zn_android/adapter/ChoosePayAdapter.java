package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/4/6 0006.
 */
public class ChoosePayAdapter extends BaseAdapter {
    private Context mContext;
    private List<Map<String, String>> data = new ArrayList<>();

    public ChoosePayAdapter(Context mContext, List<Map<String, String>> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = UIUtil.inflate(R.layout.item_choose_pay);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mIvImg.setImageResource(Integer.valueOf(data.get(position).get(Constants.IMG)));
        String title = data.get(position).get(Constants.TITLE);
        holder.mTvTitle.setText(data.get(position).get(Constants.TITLE));
        holder.mTvInfo.setText(data.get(position).get(Constants.INFO));
        if (data.get(position).get(Constants.SELECT).equals(Constants.SELECT)) {
            holder.mIvSelect.setImageResource(R.mipmap.ic_selected);
        } else {
            holder.mIvSelect.setImageResource(R.mipmap.ic_select_no);
        }
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_img)
        ImageView mIvImg;
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_info)
        TextView mTvInfo;
        @Bind(R.id.iv_select)
        ImageView mIvSelect;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
