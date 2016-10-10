package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.GiftInfoBean;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Jolly on 2016/4/12 0012.
 */
public class ShowGiftGridAdapter extends BaseAdapter {
    private int[] giftImgRes = new int[] {R.mipmap.ic_gift1, R.mipmap.ic_gift2, R.mipmap.ic_gift3, R.mipmap.ic_gift4,
            R.mipmap.ic_gift5, R.mipmap.ic_gift6, R.mipmap.ic_gift7, R.mipmap.ic_gift8,
            R.mipmap.ic_gift9, R.mipmap.ic_gift10, R.mipmap.ic_gift11, R.mipmap.ic_gift12,
            R.mipmap.ic_gift13, R.mipmap.ic_gift14, R.mipmap.ic_gift15, R.mipmap.ic_gift16};
    private Context mContext;
    private List<GiftInfoBean> data = new ArrayList<>();

    public ShowGiftGridAdapter(Context mContext, List<GiftInfoBean> data) {
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
            convertView = UIUtil.inflate(R.layout.item_gift);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mIvGiftImg.setImageResource(giftImgRes[data.get(position).getGid() - 1]);
        holder.mIvGiftName.setText(data.get(position).getNickname());
        holder.mIvGiftWealth.setText(data.get(position).getTimes());
        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.iv_gift_img)
        ImageView mIvGiftImg;
        @Bind(R.id.iv_gift_name)
        TextView mIvGiftName;
        @Bind(R.id.iv_gift_wealth)
        TextView mIvGiftWealth;
        @Bind(R.id.ll_gift)
        LinearLayout mLlGift;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
