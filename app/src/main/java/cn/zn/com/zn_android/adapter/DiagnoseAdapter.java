package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.DiagnoseBean;
import cn.zn.com.zn_android.model.bean.OrderInfoBean;
import cn.zn.com.zn_android.utils.ToastUtil;

/**
 * Created by Jolly on 2016/11/30.
 */

public class DiagnoseAdapter extends BaseAdapter {
    private Context mContext;
    private List<OrderInfoBean.TicListBean> data = new ArrayList<>();

    private AppCompatCheckBox mCbUseZnb = null;

    public void setmCbUseZnb(AppCompatCheckBox mCbUseZnb) {
        this.mCbUseZnb = mCbUseZnb;
    }

    public List<OrderInfoBean.TicListBean> getData() {
        return data;
    }

    public DiagnoseAdapter(Context mContext, List<OrderInfoBean.TicListBean> data) {
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_use_diagnose, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mCbUseZnb.setChecked(data.get(position).isChecked());
        holder.mTvValue.setText(String.valueOf(data.get(position).getFace_value()));

        holder.mCbUseZnb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                data.get(position).setChecked(isChecked);

                if (isChecked) {
                    if (null != mCbUseZnb && mCbUseZnb.isChecked()) {
                        mCbUseZnb.setChecked(false);
                    }
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.tv_value)
        TextView mTvValue;
        @Bind(R.id.cb_use_znb)
        AppCompatCheckBox mCbUseZnb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
