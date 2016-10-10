package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public abstract class BaseViewHolder {

    BaseViewHolder(View view) {
        ButterKnife.bind(this, view);
    }

}
