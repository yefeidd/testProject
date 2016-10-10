package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.TenShBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/8/19 0019.
 */
public class ShareHoldersAdapter extends ArrayAdapter<TenShBean> {
    private Context mContext;
    private int resId = R.layout.item_ten_sh;
    private List<TenShBean> data = new ArrayList<>();

    public ShareHoldersAdapter(Context context, int resource, List<TenShBean> objects) {
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
        holder.mTvShName.setText(data.get(position).getSH_NAME());
        holder.mTvVol.setText(data.get(position).getHOLD_VOL());
        if (null != data.get(position).getHOLD_PCT()) {
            holder.mTvPct.setText(data.get(position).getHOLD_PCT());
        } else {
            holder.mTvPct.setText(data.get(position).getEND_DATE());
        }

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_sh_name)
        TextView mTvShName;
        @Bind(R.id.tv_vol)
        TextView mTvVol;
        @Bind(R.id.tv_pct)
        TextView mTvPct;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
