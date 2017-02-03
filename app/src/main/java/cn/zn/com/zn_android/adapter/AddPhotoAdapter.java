package cn.zn.com.zn_android.adapter;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.customerview.widget.PhotoPickerActivity;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ImageLoader;

/**
 * Created by zjs on 2016/12/5 0005.
 * email: m15267280642@163.com
 * explain: 添加图片的adapter
 */

public class AddPhotoAdapter extends BaseAdapter {
    private List<String> pathList;
    private List<String> truePathList;
    private Activity mContxt;
    private int mColumnWidth = 70;
    private static final int PICK_PHOTO = 1;
    private boolean showCamera = true;
    private int selectedMode = PhotoPickerActivity.MODE_MULTI;
    public static int maxNum = 3;
    private String ADD_IMG = "addImage";
    private String ADD_TIPS = "addTips";
    private View.OnClickListener addPhotoOnclickListener;
    private View.OnClickListener deletePhoneOnclickListener;

    public AddPhotoAdapter(List<String> listUrls, Activity context) {
        this.truePathList = listUrls;
        mContxt = context;
        initDatas();
        initEvent();
    }

    @Override
    public int getCount() {
        return pathList.size();
    }

    @Override
    public String getItem(int position) {
        return pathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 初始化点击事件
     */
    private void initEvent() {
        addPhotoOnclickListener = v -> {
            if (Build.VERSION.SDK_INT >= 23) {
                int checkCallPhonePermission = ContextCompat.checkSelfPermission(mContxt, Manifest.permission.READ_EXTERNAL_STORAGE);
                if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(mContxt, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 123);
                    return;
                } else {
                    startPhotoSelecActivity();
                }
            } else {
                startPhotoSelecActivity();
            }
        };
    }


    /**
     * 开启选择照片的也卖弄
     */
    public void startPhotoSelecActivity() {
        Intent intent = new Intent(mContxt, PhotoPickerActivity.class);
        intent.putExtra(PhotoPickerActivity.EXTRA_SHOW_CAMERA, showCamera);
        intent.putExtra(PhotoPickerActivity.EXTRA_SELECT_MODE, selectedMode);
        intent.putExtra(PhotoPickerActivity.EXTRA_MAX_MUN, maxNum - truePathList.size());
        mContxt.startActivityForResult(intent, PICK_PHOTO);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (pathList.get(position).equals(ADD_IMG)) {
            convertView = mContxt.getLayoutInflater().inflate(
                    R.layout.item_add_photo, null);
            convertView.setOnClickListener(addPhotoOnclickListener);
            convertView.setTag(null);
        } else if (pathList.get(position).equals(ADD_TIPS)) {
            convertView = LayoutInflater.from(mContxt).inflate(
                    R.layout.item_add_pic_tips, null);
            convertView.setTag(null);
        } else {
            ViewHolder holder;
            if (null == convertView || null == convertView.getTag()) {
                convertView = LayoutInflater.from(mContxt).inflate(R.layout.item_image, null);
                holder = new ViewHolder(convertView);
                holder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
                holder.mIvDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
                holder.mIvDelete.setOnClickListener(v -> {
                    truePathList.remove(position);
                    setPathList(truePathList);
                });
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ImageLoader.getInstance().display(getItem(position), holder.mImageView, DensityUtil.dip2px(mContxt, mColumnWidth), DensityUtil.dip2px(mContxt, mColumnWidth));
        }
        return convertView;
    }


    /**
     * 设置路径
     *
     * @param pathList
     */
    public void setPathList(List<String> pathList) {
        this.truePathList = pathList;
        initDatas();
        notifyDataSetChanged();
    }


    /**
     * 初始化显示的数据
     */
    private void initDatas() {
        if (null == pathList) {
            pathList = new ArrayList<>();
        } else {
            pathList.clear();
        }
        for (int i = 0; i < truePathList.size(); i++) {
            pathList.add("");
        }
        Collections.copy(pathList, truePathList);

        if (pathList.size() == 0) {
            pathList.add(ADD_IMG);
            pathList.add(ADD_TIPS);
        } else if (pathList.size() < 3 && !pathList.get(pathList.size() - 1).equals(ADD_IMG)) {
            pathList.add(ADD_IMG);
        }
    }


    public class ViewHolder {
        @Bind(R.id.imageView)
        ImageView mImageView;
        @Bind(R.id.iv_delete)
        ImageView mIvDelete;
        @Bind(R.id.wrap_layout)
        FrameLayout mWrapLayout;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
