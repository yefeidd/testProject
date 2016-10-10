package cn.zn.com.zn_android.uiclass.customerview;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.uiclass.NoScrollGridView;
import com.umeng.socialize.Config;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by zjs on 2016/9/19 0019.
 * email: m15267280642@163.com
 * explain:
 */
public class ShareDialogBuilder {
    private static ShareDialogBuilder instance = null;
    private Dialog dialog = null;
    private Activity _mAcitivity;
    private final int[] shareIcon = new int[]{R.drawable.wx, R.drawable.wechat, R.drawable.sina, R.drawable.qq, R.drawable.zone};
    private final String[] shareName = RnApplication.getInstance().getResources().getStringArray(R.array.share_name);
    private final SHARE_MEDIA[] actions = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.SINA, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE};
    private NoScrollGridView shareButton;
    private UMShareListener umShareListener;
    private String shareContent = Constants.taShareContent;
    private String shareTitle = Constants.taShareTitle;
    private String mUrl = Constants.taShareUrl;
    UMImage image;


    public static ShareDialogBuilder getInstance() {
        if (instance == null) {
            synchronized (ShareDialogBuilder.class) {
                if (null == instance) {
                    instance = new ShareDialogBuilder();
                }
            }
        }
        return instance;
    }

    private ShareDialogBuilder() {
        super();
    }


    public void setActivity(Activity activity) {
        if (activity.equals(_mAcitivity)) {
        } else {
            _mAcitivity = activity;
            creatDialog();
        }
        image = new UMImage(_mAcitivity, Constants.iconResourece);
    }

    private void creatDialog() {
        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(_mAcitivity);
        View view = View.inflate(_mAcitivity, R.layout.layout_share_action_detai, null);
        shareButton = (NoScrollGridView) view.findViewById(R.id.ngv_share_btn);
        initShareView();
        builder.setContentView(view);
        builder.setCancelable(true);
        builder.setBottomButton(false);
        dialog = builder.create();
    }

    /**
     * 设置三方跳转的时候的dialog
     */
    private void setDialog() {
        JoDialog dialog = new JoDialog.Builder(_mAcitivity)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);
        Config.dialog = dialog;
    }

    /**
     * 初始化分享的按钮
     */
    private void initShareView() {
        ShareAdapter shareAdapter = new ShareAdapter();
        shareButton.setAdapter(shareAdapter);
    }


    public Dialog getDialog() {
        return dialog;
    }


    /**
     * @param umShareListener
     */
    public void setUmShareListener(UMShareListener umShareListener) {
        this.umShareListener = umShareListener;
    }

    class ShareAdapter extends BaseAdapter {
        ViewHolder viewHolder;

        public ShareAdapter() {

        }

        @Override
        public int getCount() {
            return shareIcon.length;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (null == view) {
                view = View.inflate(_mAcitivity, R.layout.item_share_btn, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.mIvIcon.setImageResource(shareIcon[position]);
            viewHolder.mTvName.setText(shareName[position]);
            view.setOnClickListener(v -> {
                if (umShareListener != null) {
                    setDialog();
                    setShareAction(actions[position]);
                }
            });

            return view;
        }


        private void setShareAction(SHARE_MEDIA shareAction) {
            if (null == image) {
                image = new UMImage(_mAcitivity, BitmapFactory.decodeResource(_mAcitivity.getResources(), R.drawable.share_icon));
            }
            new ShareAction(_mAcitivity)
                    .setPlatform(shareAction)
                    .setCallback(umShareListener)
                    .withText(shareContent)
                    .withTargetUrl(mUrl)
                    .withMedia(image)
                    .withTitle(shareTitle)
                    .share();
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
            @Bind(R.id.iv_icon)
            ImageView mIvIcon;
            @Bind(R.id.tv_name)
            TextView mTvName;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
