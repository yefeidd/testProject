package cn.zn.com.zn_android.uiclass.customerview;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;


/**
 * Created by Scorpion on 2016/3/19.
 * 　　　___ __
 * 　　 {___{__}\
 * 　 {_}      `\)
 * 　{_}        `            _.-''''--.._
 * 　{_}                    //'.--.  \___`.
 * 　{ }__,_.--~~~-~~~-~~-::.---. `-.\  `.)
 * 　`-.{_{_{_{_{_{_{_{_//  -- 8;=- `
 * 　　`-:,_.:,_:,_:,.`\\._ ..'=- ,
 * 　　　　　// // // //`-.`\`   .-'/
 * 　　　　　<< << << <<    \ `--'  /----)
 * 　　　　　^  ^  ^  ^     `-.....--'''
 * Email:m15267280642@163.com
 * synopsis: this is a dialog from down to up
 */
public class CusDownUpDialog extends Dialog {
    private static Builder b;

    public CusDownUpDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initValues();
    }

    /**
     * 设置dialog显示的位置和宽度
     */
    private void initValues() {
        // 不能写在init()中
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        lp.width = dm.widthPixels;//让dialog的宽占满屏幕的宽
        lp.gravity = Gravity.BOTTOM;//出现在底部
        window.setAttributes(lp);

        window.getDecorView().setPadding(0, 0, 0, 0);
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);

    }

    //dialog工厂
    public static class Builder {
        private Context context;
        private String title;
        private String message;
        private String bottomButtonText;
        private View contentView;
        private View childrenView;
        private boolean bottomFlag = true;
        private boolean flag = true;
        private boolean isWidthFull = flag;
        private CusDialogInterface.OnClickListener bottomButtonClickListene;
        private View layout;

        public Builder(Context context) {
            this.context = context;
            b = this;
        }

        public Builder setMessage(String message) {
            this.message = message;
            return this;
        }

        /**
         * Set the Dialog message from resource
         *
         * @return
         */
        public Builder setMessage(int message) {
            this.message = (String) context.getText(message);
            return this;
        }

        public Builder setIsWidthFull(boolean isWidthFull) {
            this.isWidthFull = isWidthFull;
            return this;
        }

        /**
         * Set the Dialog title from resource
         *
         * @param title
         * @return
         */
        public Builder setTitle(int title) {
            this.title = (String) context.getText(title);
            return this;
        }

        /**
         * Set the Dialog title from String
         *
         * @param title
         * @return
         */

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        /**
         * Set the content from view
         *
         * @param v
         * @return
         */
        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        /**
         * Set the children for the content
         */
        public Builder setChildrenView(View v) {
            this.childrenView = v;
            return this;
        }

        /**
         * set the layout
         *
         * @param v
         * @return
         */
        public Builder setLayoutView(View v) {
            this.layout = v;
            return this;
        }

        /**
         * Set the bottomBuntton
         *
         * @param bottomButtonText
         * @param listener
         * @return
         */
        public Builder setBottomButton(String bottomButtonText,
                                       CusDialogInterface.OnClickListener listener) {
            this.bottomButtonText = bottomButtonText;
            this.bottomButtonClickListene = listener;
            return this;
        }

        /**
         * Set the Cancelable
         *
         * @param flag
         * @return
         */
        public Builder setCancelable(boolean flag) {
            this.flag = flag;
            return this;
        }

        /**
         * set bottom is visible or gone
         *
         * @param bottomFlag
         * @return
         */
        public Builder setBottomButton(boolean bottomFlag) {
            this.bottomFlag = bottomFlag;
            return this;
        }

        /**
         * 生产弹窗的工厂
         *
         * @return
         */
        public CusDownUpDialog create() {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // instantiate the dialog with the custom Theme
            final CusDownUpDialog dialog;
            //如果是全屏且layout布局为空的话
            if (isWidthFull && layout == null) {
                dialog = new CusDownUpDialog(context, R.style.Dialog_From_Bottom_Width_Full);
            } else {
                dialog = new CusDownUpDialog(context, R.style.Theme_Dialog_From_Bottom);
            }
            if (layout == null) {
                layout = inflater.inflate(R.layout.layout_cus_dialog, null);
                if (!bottomFlag) {
                    layout.findViewById(R.id.tv_bottom).setVisibility(View.GONE);
                } else {
                    layout.findViewById(R.id.tv_bottom).setVisibility(View.VISIBLE);
                }
                // set the dialog title
                if (title != null) {
                    layout.findViewById(R.id.tv_title).setVisibility(View.VISIBLE);
                    ((TextView) layout.findViewById(R.id.tv_title)).setText(title);
                }
                // set the bottom button
                if (bottomButtonText != null) {

                    ((TextView) layout.findViewById(R.id.tv_bottom))
                            .setText(bottomButtonText);
                    if (bottomButtonClickListene != null) {
                        layout.findViewById(R.id.tv_bottom)
                                .setOnClickListener(new View.OnClickListener() {
                                    public void onClick(View v) {
                                        bottomButtonClickListene.onClick(dialog, CusDialogInterface.BUTTON_BOTTOM);
                                    }
                                });
                    }
                } else {
                    layout.findViewById(R.id.tv_bottom)
                            .setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });
                }
                // set the content message
                if (message != null) {
                    layout.findViewById(R.id.tv_message).setVisibility(View.VISIBLE);
                    ((TextView) layout.findViewById(R.id.tv_message)).setText(message);
                } else if (contentView != null) {
                    // if no message set
                    // add the contentView to the dialog body
                    ((FrameLayout) layout.findViewById(R.id.lv_chat))
                            .removeAllViews();
                    ((FrameLayout) layout.findViewById(R.id.lv_chat))
                            .addView(contentView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
                }
            }
            dialog.setContentView(layout);
            dialog.setCancelable(flag);
            dialog.setCanceledOnTouchOutside(flag);
            return dialog;
        }
    }

    //点击的接口
    public interface CusDialogInterface {
        int BUTTON_BOTTOM = -1;

        interface OnClickListener {
            /**
             * This method will be invoked when a button in the dialog is clicked.
             *
             * @param dialog The dialog that received the click.
             * @param which  The button that was clicked (e.g.
             *               {@link DialogInterface#BUTTON1}) or the position
             *               of the item clicked.
             */
            void onClick(DialogInterface dialog, int which);
        }
    }

}
