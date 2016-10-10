package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.uiclass.activity.ArticleDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.BaseActivity;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * this is the list adapter for the contribution
 */
public class ArticleListAdapter extends BaseAdapter {

    private ViewHolder holder;
    private final int CONTENTLENGTH = 40;
    private ApiManager _apiManager;
    private String art_id;

    private String type = Constants.ARTICLE;

    public void setType(String type) {
        this.type = type;
    }

    public void setmContentList(List<?> mContentList) {
        this.mContentList = mContentList;
        notifyDataSetChanged();
    }

    private List<?> mContentList;
    private Context mContext;

    public ArticleListAdapter(Context mContext, List<?> list_content) {
        this.mContext = mContext;
        this.mContentList = list_content;
        this._apiManager = ((BaseActivity) mContext).get_apiManager();
    }

    @Override
    public int getCount() {
        if (mContentList == null) {
            return 0;
        }
        return mContentList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = UIUtil.inflate(R.layout.item_article);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        //不能设置为全局变量，不然传递的数据就是最后一条的
        ArticleBean articleInfo = (ArticleBean) mContentList.get(position);
        String describe = StringUtil.subString(articleInfo.getContent(), CONTENTLENGTH, "......");
        StringBuilder praise = new StringBuilder();
        praise.append("阅 (");
        praise.append(articleInfo.getClick());
        praise.append(")");
        StringBuilder like = new StringBuilder();
        like.append("点赞 (");
        like.append(articleInfo.getLikes());
        like.append(")");
        holder.mTvContent.setText(describe);
//        holder.mTvContent.setText(articleInfo.getUrl());
        holder.mTvTitle.setText(articleInfo.getTitle());
        holder.mTvPraise.setText(praise.toString());
        holder.mTvLike.setText(like.toString());
        holder.mTvTimes.setText(articleInfo.getTimes());
        /**
         * 设置条目的点击事件
         */
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                art_id = articleInfo.getId();
//                postArtClick(art_id);
                //是否是博文
                boolean isArticle = true;
                if (type.equals(Constants.ARTICLE)) {
                    isArticle = true;
                } else {
                    isArticle = false;
                }
                EventBus.getDefault().postSticky(new AnyEventType(articleInfo).setState(isArticle));
                mContext.startActivity(new Intent(mContext, ArticleDetailActivity.class));
            }
        });
        return view;
    }

    @Override
    public Object getItem(int position) {
        return mContentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    static class ViewHolder {
        @Bind(R.id.tv_title)
        TextView mTvTitle;
        @Bind(R.id.tv_content)
        TextView mTvContent;
        @Bind(R.id.tv_praise)
        TextView mTvPraise;
        @Bind(R.id.tv_like)
        TextView mTvLike;
        @Bind(R.id.tv_times)
        TextView mTvTimes;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

//    public void postArtClick(String art_id) {
//        AppObservable.bindActivity((BaseActivity) mContext, _apiManager.getService().postArtClick(art_id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultArtClick, Throwable -> {
//                    Throwable.printStackTrace();
//                    ToastUtil.showShort(mContext, mContext.getString(R.string.no_net));
//                });
//    }
//
//
//    private void resultArtClick(ReturnValue<MessageBean> returnValue) {
//        if (returnValue != null && returnValue.getMsg().equals("success")) {
//            LogUtils.i("ArticleListAdapter:" + "请求成功");
//        } else {
//            ToastUtil.showShort(mContext, mContext.getString(R.string.no_net));
//        }
//    }
}
