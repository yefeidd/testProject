package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;

/**
 * Created by Jolly on 2016/12/5.
 */

public class ImageGVAdapter extends BaseAdapter {
    private Context mContext;
    private List<String> data;

    public ImageGVAdapter(Context mContext, List<String> urlList) {
        this.mContext = mContext;
        this.data = urlList;
    }

    @Override
    public int getCount() {
        if (data.size() > 3) {
            return 3;
        }
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
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_answer_img, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mSdvAvatar.setImageURI(Uri.parse(data.get(position)));

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.sdv_avatar)
        SimpleDraweeView mSdvAvatar;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
