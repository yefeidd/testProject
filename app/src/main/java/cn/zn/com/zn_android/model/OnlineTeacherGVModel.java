package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.OnlineTeacherGVHolder;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.uiclass.activity.QuickQaActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherLiveActivity;
import cn.zn.com.zn_android.utils.UIUtil;
import de.greenrobot.event.EventBus;

/**
 * Created by Jolly on 2016/11/24.
 */

public class OnlineTeacherGVModel extends ListviewItemModel {
    private String url;
    private String name;
    private String tid;
    private int star_num;
    private int num;
    private Context mContext;

    public OnlineTeacherGVModel(Context context, String url, String name, int num, String tid, int starNum) {
        this.url = url;
        this.name = name;
        this.num = num;
        this.mContext = context;
        this.tid = tid;
        this.star_num = starNum;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        OnlineTeacherGVHolder gvHolder = (OnlineTeacherGVHolder) viewHolder;
        gvHolder.tvName.setText(name);
        String answerNum = String.format(context.getString(R.string.answer_num), num);
        SpannableString ss = new SpannableString(answerNum);
        ss.setSpan(new ForegroundColorSpan(UIUtil.getResources().getColor(R.color.app_bar_color)),
                5, 5 + String.valueOf(num).length(), SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);
        gvHolder.tvAnswerNum.setText(ss);
        if (url != null && !TextUtils.isEmpty(url)) {
            gvHolder.sdvAvatar.setImageURI(Uri.parse(url));
        }

        String resName = "ic_teacher_star" + star_num;
        gvHolder.mIvStar.setImageBitmap(BitmapFactory.decodeResource(context.getResources(),
                context.getResources().getIdentifier(resName, "drawable", context.getPackageName())));

        gvHolder.ibAskHim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().postSticky(new AnyEventType(name).setTid(tid).setType(star_num));
                mContext.startActivity(new Intent(mContext, QuickQaActivity.class));
            }
        });
        gvHolder.tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotLiveBean hotLiveBean = new HotLiveBean();
                hotLiveBean.setTid(tid);
//                hotLiveBean.setOrdurl(roomBeanList.get(position - 1).getOrdurl());
                hotLiveBean.setAvatars(url);
                hotLiveBean.setTitle(name);
                hotLiveBean.setRoom_number("");
                hotLiveBean.setCollect("");
                hotLiveBean.setClick("");
                hotLiveBean.setPlacard("");
                hotLiveBean.setCurrentItem(2);
                EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
                context.startActivity(new Intent(context, TeacherLiveActivity.class));
            }
        });
        gvHolder.sdvAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HotLiveBean hotLiveBean = new HotLiveBean();
                hotLiveBean.setTid(tid);
//                hotLiveBean.setOrdurl(roomBeanList.get(position - 1).getOrdurl());
                hotLiveBean.setAvatars(url);
                hotLiveBean.setTitle(name);
                hotLiveBean.setRoom_number("");
                hotLiveBean.setCollect("");
                hotLiveBean.setClick("");
                hotLiveBean.setPlacard("");
                hotLiveBean.setCurrentItem(2);
                EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
                context.startActivity(new Intent(context, TeacherLiveActivity.class));
            }
        });
    }
}
