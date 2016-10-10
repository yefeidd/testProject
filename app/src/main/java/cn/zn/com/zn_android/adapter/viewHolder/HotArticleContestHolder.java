package cn.zn.com.zn_android.adapter.viewHolder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;

import butterknife.Bind;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class HotArticleContestHolder extends BaseViewHolder {


    @Bind(R.id.tv_rank)
    public TextView mTvRank;
    @Bind(R.id.tv_title)
    public TextView mTvTitle;
    @Bind(R.id.ll_hot_article_item)
    public LinearLayout mHotItem;

    HotArticleContestHolder(View view) {
        super(view);
    }

}
