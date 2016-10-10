package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;

/**
 * 底部按钮
 *
 * Created by WJL on 2016/3/10 0010 16:04.
 */
public class BottomTabButton extends FrameLayout implements View.OnClickListener {
    private Context mContext;
    private ImageView mImage; // 图标
    private TextView mTitle; // 标题

    private int index;

    private Drawable mSelectedImg; // 选中图标
    private Drawable mUnselectedImg; // 未选中图标

    private OnBottomClickListener onBottomClick;
    public interface OnBottomClickListener {
        void bottomClick(int index);
    }

    public BottomTabButton(Context context) {
        this(context, null);
    }

    public BottomTabButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BottomTabButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.widget_button_tab, this, true);

        mImage = (ImageView) findViewById(R.id.tab_img);
        mTitle = (TextView) findViewById(R.id.tab_btn_title);

        setOnClickListener(this);

    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setOnBottomClick(OnBottomClickListener clickListener) {
        this.onBottomClick = clickListener;
    }

    public void setUnselectedImage(Drawable img) {
        this.mUnselectedImg = img;
    }

    public void setSelectedImage(Drawable img) {
        this.mSelectedImg = img;
    }

    private void setSelectedColor(Boolean selected) {
        if (selected) {
            mTitle.setTextColor(getResources().getColor(R.color.white));
        } else {
            mTitle.setTextColor(getResources().getColor(R.color.tab_unselected));
        }
    }

    public void setSelectedButton(Boolean selected) {
        setSelectedColor(selected);
        if (selected) {
            mImage.setImageDrawable(mSelectedImg);
        } else {
            mImage.setImageDrawable(mUnselectedImg);
        }
    }

    /**
     * 标题
     *
     * @param title
     */
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public String getTitle() {
        return mTitle.getText().toString();
    }

    @Override
    public void onClick(View view) {
        onBottomClick.bottomClick(index);
    }
}
