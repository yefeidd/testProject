package cn.zn.com.zn_android.uiclass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants_api;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.List;

/**
 * 客服pop弹窗
 * Created by Jolly on 2016/5/13 0013.
 */
public class CSPopWindow extends PopupWindow {
    private Context mContext;
    private UMShareAPI mShareAPI;

    public CSPopWindow(Context context) {
        this(context, null);
    }

    public CSPopWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CSPopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //获取UMShareAPI
        mShareAPI = UMShareAPI.get(context);
    }

    public void showPopWind(Activity activity, View baseView, List<String> qqList) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_cs_popwind, null);

        ListView mLvCs = (ListView) contentView.findViewById(R.id.lv_cs);
        ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.item_cs, mContext.getResources().getStringArray(R.array.cus_services));
        mLvCs.setAdapter(adapter);

        mLvCs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mShareAPI.isInstall(activity, SHARE_MEDIA.QQ)) {
                    String url= Constants_api.QQ_URL + qqList.get(position);
                    mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    dismiss();
                } else {
                    ToastUtil.showShort(mContext, mContext.getString(R.string.uninstall_qq));
                }
            }
        });

        this.setFocusable(true);
        this.setContentView(contentView);
        this.setWidth(DensityUtil.dip2px(mContext, 80));
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sp_rect_gray));
        this.showAsDropDown(baseView, DensityUtil.dip2px(mContext, 30), DensityUtil.dip2px(mContext, -60));
    }
}
