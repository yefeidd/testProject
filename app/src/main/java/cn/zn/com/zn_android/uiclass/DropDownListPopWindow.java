package cn.zn.com.zn_android.uiclass;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;

import java.util.List;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.utils.DensityUtil;

/**
 * Created by Jolly on 2016/11/25.
 */

public class DropDownListPopWindow extends PopupWindow {
    private Context mContext;
    public interface onItemClick {
        void itemClick(String stockname);
    }
    public onItemClick itemClick;

    public void setItemClick(onItemClick itemClick) {
        this.itemClick = itemClick;
    }

    public DropDownListPopWindow(Context context) {
        this(context, null);
    }

    public DropDownListPopWindow(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropDownListPopWindow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
    }

    public void showPopWind(View baseView, List<String> stockList) {
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.layout_drop_down_list, null);

        ListView mLvCs = (ListView) contentView.findViewById(R.id.lv_drop_down);
        ArrayAdapter adapter = new ArrayAdapter(mContext, R.layout.item_drop_down, stockList);
        mLvCs.setAdapter(adapter);

        mLvCs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (itemClick != null) {
                    itemClick.itemClick(stockList.get(position));
                    dismiss();
                }
            }
        });

        this.setFocusable(true);
        this.setContentView(contentView);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.sp_rect_gray));
//        this.showAsDropDown(baseView, DensityUtil.dip2px(mContext, 30), DensityUtil.dip2px(mContext, -60));
        if (!isShowing()) {
            this.showAsDropDown(baseView, 0, DensityUtil.dip2px(mContext, 12));
        }
    }
}
