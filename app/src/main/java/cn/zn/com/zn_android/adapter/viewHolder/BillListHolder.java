package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.TextView;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.ScrollListView;

/**
 * Created by Jolly on 2016/12/7.
 */

public class BillListHolder extends BaseViewHolder {
    @Bind(android.R.id.title)
    public TextView mTitle;
    @Bind(R.id.sll_ss)
    public ScrollListView mSllSs;

    BillListHolder(View view) {
        super(view);
    }
}
