package cn.zn.com.zn_android.uiclass.customerview;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.utils.DensityUtil;

/**
 * Created by Jolly on 2016/3/24 0024.
 */
public class JoDialog extends AlertDialog implements View.OnClickListener {
    private Builder b;

    private TextView title, content;
    private View img_line1;
    private ImageView icon;
    private TextView btn_Negative, btn_Positive;
    private LinearLayout buttonDefaultFrame, titleFrame, mLlLayout;
    private ScrollView contentScrollView;
    private FrameLayout customViewFrame;
    private ListView list_content;
    private View contentView;

    protected JoDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public JoDialog(Builder builder, int themeResId) {
        super(builder.context, themeResId);
        this.b = builder;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
    }

    /**
     *
     */
    private void initView() {
        View view = getLayoutInflater().inflate(R.layout.dialog_jo, null);

        title = (TextView) view.findViewById(R.id.title);
        img_line1 = view.findViewById(R.id.img_line1);
        btn_Negative = (Button) view.findViewById(R.id.btn_Negative);
        btn_Positive = (TextView) view.findViewById(R.id.btn_Positive);
        mLlLayout = (LinearLayout) view.findViewById(R.id.ll_layout);
        btn_Positive.setOnClickListener(this);
        btn_Negative.setOnClickListener(this);

        if (b.strTitle != null) {
            title.setText(b.strTitle);
        } else {
            titleFrame = (LinearLayout) view.findViewById(R.id.titleFrame);
            titleFrame.setVisibility(View.GONE);
        }

        if (b.iconRes != 0) {
            icon = (ImageView) view.findViewById(R.id.icon);
            icon.setImageResource(b.iconRes);
        }

        if (b.strContent != null) {
            contentScrollView = (ScrollView) view.findViewById(R.id.contentScrollView);
            contentScrollView.setVisibility(View.VISIBLE);
            content = (TextView) view.findViewById(R.id.content);
            content.setText(b.strContent);
            content.setTextColor(content.getResources().getColor(b.colorContent));
        }

//        if (b.items != null) {
//            list_content = (ListView) view.findViewById(R.id.list_content);
//            list_content.setVisibility(View.VISIBLE);
//            list_content.setOverScrollMode(View.OVER_SCROLL_NEVER);
//
//            buttonDefaultFrame = (LinearLayout) view.findViewById(R.id.buttonDefaultFrame);
//            buttonDefaultFrame.setVisibility(View.GONE);
//
//            YoDialogListAdapter adapter = new YoDialogListAdapter(getContext(), b.items);
//            list_content.setAdapter(adapter);
//
//            if (b.listCallback != null) {
//                list_content.setOnItemClickListener((parent, view, position, id) -> b.listCallback.onSelection(this, view, position, b.items[position]));
//            }
//        }

        if (b.positiveTextRes != 0) {
            btn_Positive.setText(b.positiveTextRes);
        } else {
            buttonDefaultFrame = (LinearLayout) view.findViewById(R.id.buttonDefaultFrame);
            buttonDefaultFrame.setVisibility(View.GONE);
        }

        if (b.negativeTextRes != 0) {
            btn_Negative.setText(b.negativeTextRes);
        } else {
            btn_Negative.setVisibility(View.GONE);
            img_line1.setVisibility(View.GONE);
        }

        if (b.viewRes != 0) {
            customViewFrame = (FrameLayout) view.findViewById(R.id.customViewFrame);
            customViewFrame.setVisibility(View.VISIBLE);

            View viewFrame = LayoutInflater.from(getContext()).inflate(b.viewRes, null);
            customViewFrame.addView(viewFrame);
        }

        if (null != b.contentView) {
            customViewFrame = (FrameLayout) view.findViewById(R.id.customViewFrame);
            customViewFrame.setVisibility(View.VISIBLE);
            customViewFrame.addView(b.contentView);
        }

        if (b.cancelableOut) {
            b.cancelable = true;
        }
        setCancelable(b.cancelable);
        setCanceledOnTouchOutside(b.cancelableOut);

        if (b.gravity != 0) {
            getWindow().setGravity(b.gravity);
        }

        if (b.isMatchParent) {
            addContentView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
            int maxWidth = DensityUtil.dip2px(getContext(), 480);
            int width = dm.widthPixels - DensityUtil.dip2px(getContext(), 28);
            if (width > maxWidth) {
                width = maxWidth;
            }

            addContentView(view, new ViewGroup.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

        if (b.bgRes >= 0) {
            mLlLayout.setBackgroundColor(b.bgRes);
        }
    }

    @Override
    public void onClick(View v) {
        if (b.callback == null) return;
        switch (v.getId()) {
            case R.id.btn_Negative:
                b.callback.onNegtive(this);
                break;
            case R.id.btn_Positive:
                b.callback.onPositive(this);
                break;
        }
    }

    public static class Builder {
        private Context context;
        private String strTitle;
        private int iconRes;
        private int viewRes;
        private int colorContent = R.color.app_bar_color;
        private String strContent;
        private String[] items;
        private int positiveTextRes;
        private int negativeTextRes;
        private boolean cancelableOut = true;
        private boolean cancelable = true;
        private ButtonCallback callback;
        private int gravity;
        private boolean isMatchParent = false; // ViewGroup.LayoutParams.MATCH_PARENT
        private int bgRes = -1;
        private View contentView;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        /**
         * dialog 宽度是否 MatchParent
         * @param isMatchParent
         * @return
         */
        public Builder setIsMatchParent(boolean isMatchParent) {
            this.isMatchParent = isMatchParent;
            return this;
        }

        public Builder setGravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder setStrTitle(int titleRes) {
            this.strTitle = context.getString(titleRes);
            return this;
        }

        public Builder setStrTitle(String strTitle) {
            this.strTitle = strTitle;
            return this;
        }

        public Builder setIconRes(int iconRes) {
            this.iconRes = iconRes;
            return this;
        }

        public Builder setViewRes(int viewRes) {
            this.viewRes = viewRes;
            return this;
        }

        public Builder setStrContent(String strContent) {
            this.strContent = strContent;
            return this;
        }

        public Builder setColorContent(int colorContent) {
            this.colorContent = colorContent;
            return this;
        }

        public Builder setItems(String[] items) {
            this.items = items;
            return this;
        }

        public Builder setPositiveTextRes(int positiveTextRes) {
            this.positiveTextRes = positiveTextRes;
            return this;
        }

        public Builder setNegativeTextRes(int negativeTextRes) {
            this.negativeTextRes = negativeTextRes;
            return this;
        }

        public Builder setCancelableOut(boolean cancelableOut) {
            this.cancelableOut = cancelableOut;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            return this;
        }

        public Builder setCallback(ButtonCallback callback) {
            this.callback = callback;
            return this;
        }

        public Builder setBgRes(int bgRes) {
            this.bgRes = bgRes;
            return this;
        }

        public Builder setContentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public JoDialog build(boolean fromBottom) {
            if (fromBottom) {
                return new JoDialog(this, R.style.Theme_Dialog_From_Bottom);
            } else {
                return new JoDialog(this, R.style.Theme_Dialog_Normal);
            }
        }

        public JoDialog show(boolean fromBottom) {
            JoDialog dialog = build(fromBottom);
            dialog.show();
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            return dialog;
        }


    }

    @Override
    public void show() {
        if (Looper.myLooper() != Looper.getMainLooper())
            throw new IllegalStateException("Dialogs can only be shown from the UI thread.");
        super.show();
    }

    public interface ButtonCallback {
        void onPositive(JoDialog dialog);
        void onNegtive(JoDialog dialog);
    }

    public TextView getContent() {
        return content;
    }
}
