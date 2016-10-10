package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.TeacherExplainHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.VideoBean;
import cn.zn.com.zn_android.uiclass.activity.VideoDetailActivity;
import cn.zn.com.zn_android.utils.UnitUtils;

import de.greenrobot.event.EventBus;

/**
 * Created by zjs on 2016/9/14 0014.
 * email: m15267280642@163.com
 * explain:
 */
public class TeacherExplainModel extends ListviewItemModel {
    private VideoBean bean;
    private Context mContext;

    public TeacherExplainModel(Context context,VideoBean bean) {
        this.bean = bean;
        mContext = context;
    }


    @Override
    public void showItem(BaseViewHolder viewHolder, Context context) {
        TeacherExplainHolder explainHolder = (TeacherExplainHolder) viewHolder;
        explainHolder.mSdvExplain.setImageURI(Uri.parse(bean.getImage()));
        explainHolder.mTvTitle.setText(bean.getTitle());
        explainHolder.mRlItem.setOnClickListener(v -> {
            EventBus.getDefault().postSticky(new AnyEventType(bean.getVideo_link()));
            mContext.startActivity(new Intent(mContext, VideoDetailActivity.class));
        });
        try {
            long num = Long.valueOf(bean.getClick_num());
            StringBuilder sb = new StringBuilder();
            String unit = UnitUtils.getVolUnit1(num);
            float vol = UnitUtils.getVol(num);
            sb.append(vol).append(unit);
            explainHolder.mTvNumber.setText(sb.toString() + "人在看");
        } catch (Exception e) {
            explainHolder.mTvNumber.setText("0人在看");
        }
    }
}
