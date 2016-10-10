package cn.zn.com.zn_android.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.BaseContribution;
import cn.zn.com.zn_android.utils.UIUtil;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * this is the list adapter for the contribution
 */
public class RankingListAdapter extends BaseAdapter {
    private ViewHolder holder = null;
    private BaseContribution contribution;

    public void setmContentList(List<?> mContentList) {
        this.mContentList = mContentList;
    }

    private List<?> mContentList;
    private Context mContext;

    public RankingListAdapter(Context mContext, List<?> list_content) {
        this.mContext = mContext;
        this.mContentList = list_content;
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
            view = UIUtil.inflate(R.layout.item_contribution);
            holder = new ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        switch (position) {
            case 0:
                holder.mTvIcon.setBackgroundResource(R.drawable.contribution_1);
                break;
            case 1:
                holder.mTvIcon.setBackgroundResource(R.drawable.contribution_2);
                break;
            case 2:
                holder.mTvIcon.setBackgroundResource(R.drawable.contribution_3);
                break;
            default:
                holder.mTvIcon.setText(String.valueOf(position+1));
                holder.mTvIcon.setBackgroundResource(R.drawable.contribution_else);
                break;
        }

        contribution = (BaseContribution) mContentList.get(position);
        holder.mTvNickname.setText(contribution.getNickname());
        String content = "(" + contribution.getFraction() + ")";
        holder.mTvContribution.setText(content);
        return view;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    class ViewHolder {
        @Bind(R.id.tv_icon)
        TextView mTvIcon;
        @Bind(R.id.tv_nickname)
        TextView mTvNickname;
        @Bind(R.id.tv_contribution)
        TextView mTvContribution;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
