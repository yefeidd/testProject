package cn.zn.com.zn_android.model;

import android.content.Context;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:所有Item显示
 */
public abstract class ListviewItemModel {

    private final String TAG = this.getClass().getSimpleName();

    public abstract void showItem(BaseViewHolder viewHolder, Context context, int position);
}
