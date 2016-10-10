package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.HotArticleContestHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.uiclass.activity.ArticleDetailActivity;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/9/12 0012.
 * email: m15267280642@163.com
 * explain:
 */
public class HotAritcleContestModel extends ListviewItemModel {

    private int order;
    private ArticleBean bean;
    private Context context;

    public HotAritcleContestModel(Context context, ArticleBean bean) {
        this.bean = bean;
        this.context = context;
    }


    public HotAritcleContestModel setOrder(int order) {
        this.order = order;
        return this;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        HotArticleContestHolder hotArticleContestHolder = (HotArticleContestHolder) viewHolder;
        if (order > 2) {
            hotArticleContestHolder.mTvRank.setBackgroundResource(R.color.bar_bg_gray);
            hotArticleContestHolder.mTvRank.setTextColor(context.getResources().getColor(R.color.bar_text_title_gray));
        } else {
            hotArticleContestHolder.mTvRank.setBackgroundResource(R.color.text_red);
            hotArticleContestHolder.mTvRank.setTextColor(context.getResources().getColor(R.color.white));
        }
        hotArticleContestHolder.mTvRank.setText(String.valueOf(order + 1));
        hotArticleContestHolder.mTvTitle.setText(bean.getTitle());
        hotArticleContestHolder.mHotItem.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType(bean).setState(true));
            context.startActivity(new Intent(context, ArticleDetailActivity.class));
        });
    }
}
