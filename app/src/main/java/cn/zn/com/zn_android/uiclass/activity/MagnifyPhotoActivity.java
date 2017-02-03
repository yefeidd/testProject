package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.customerview.widget.SmoothImageView;
import cn.zn.com.zn_android.utils.ImageTools;

/**
 * Created by zjs on 2016/12/12 0012.
 * email: m15267280642@163.com
 * explain:
 */

public class MagnifyPhotoActivity extends BaseActivity {

    private ArrayList<String> mDatas;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    private SmoothImageView imageView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_magnify_photo);
    }

    @Override
    protected void initView() {
        mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");
        mPosition = getIntent().getIntExtra("position", 0);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        setContentView(imageView);
        File fileName = new File(mDatas.get(mPosition));
        ImageLoader.getInstance().displayImage(ImageTools.getImageContentUri(this, fileName).toString(), imageView);
//        int[] screenData = new int[2];
//        screenData = DensityUtil.getScreenWidth(_Activity);
//        ImageLoader.getInstance().display(mDatas.get(mPosition), imageView, screenData[0], screenData[1]);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    public void onBackPressed() {
        imageView.transformOut();
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }


}
