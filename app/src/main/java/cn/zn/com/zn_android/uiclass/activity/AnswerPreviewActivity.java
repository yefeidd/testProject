package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.PostAnswerBean;
import cn.zn.com.zn_android.presenter.DiagnosesSocketPresenter;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ImageLoader;
import cn.zn.com.zn_android.utils.StorageUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;
import de.greenrobot.event.EventBus;


/**
 * Created by zjs on 2016/12/8 0008.
 * email: m15267280642@163.com
 * explain:老师回答预览界面
 */

public class AnswerPreviewActivity extends BaseMVPActivity<DiagnosedStockView, DiagnosesSocketPresenter> implements DiagnosedStockView, View.OnClickListener {


    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.tv_question)
    TextView mTvQuestion;
    @Bind(R.id.iv_level)
    ImageView mIvLevel;
    @Bind(R.id.tv_user_name)
    TextView mTvUserName;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.tv_commit)
    TextView mTvCommit;
    @Bind(R.id.tv_short)
    TextView mTvShort;
    @Bind(R.id.tv_middle)
    TextView mTvMiddle;
    @Bind(R.id.tv_technology)
    TextView mTvTechnology;
    @Bind(R.id.tv_basic)
    TextView mTvBasic;
    @Bind(R.id.tv_supplement)
    TextView mTvSupplement;
    @Bind(R.id.gv_photo)
    GridView mGvPhoto;

    private List<String> mPhotoPathList;
    private PicAdapter adapter;
    private PostAnswerBean postAnswerBean;
    private String[] selectorStr = new String[]{"卖出", "减持", "中性", "增持", "买入"};
    private String token;
    private JoDialog dialog;
    private int flag = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        EventBus.getDefault().registerSticky(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    public DiagnosesSocketPresenter initPresenter() {
        return new DiagnosesSocketPresenter(this, this);
    }


    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof PostAnswerBean && null != event.getObject()) {
            postAnswerBean = (PostAnswerBean) event.getObject();
            EventBus.getDefault().cancelEventDelivery(this);
        }
    }


    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mTvCommit.setOnClickListener(this);
        mGvPhoto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ImageView imageView = (ImageView) view;
                Intent intent = new Intent(_Activity, MagnifyPhotoActivity.class);
                intent.putExtra("images", (ArrayList<String>) mPhotoPathList);//非必须
                intent.putExtra("position", position);
                int[] location = new int[2];
                imageView.getLocationOnScreen(location);
                intent.putExtra("locationX", location[0]);//必须
                intent.putExtra("locationY", location[1]);//必须
                intent.putExtra("width", imageView.getWidth());//必须
                intent.putExtra("height", imageView.getHeight());//必须
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
    }


    @Override
    protected void initView() {
        presenter.getQNToken();
        mToolbarTitle.setText(getString(R.string.answer_preview));
        mGvPhoto.setFocusable(false);
        adapter = new PicAdapter();
        mGvPhoto.setAdapter(adapter);
    }


    @Override
    protected void initData() {
        mTvQuestion.setText(postAnswerBean.getAsk());
        mTvTime.setText(postAnswerBean.getTime());
        setStarLevel(postAnswerBean.getStar(), mIvLevel);
        mTvUserName.setText(postAnswerBean.getNickName());
        String str0 = "短线趋势：" + selectorStr[postAnswerBean.getStrend() - 1];
        String str1 = "中线趋势：" + selectorStr[postAnswerBean.getMtrend() - 1];
        String str2 = "技术分析：" + postAnswerBean.getTranalysis();
        String str3 = "基本面分析：" + postAnswerBean.getFanalysis();
        String str4 = "补充：" + postAnswerBean.getSupplement();
        SpannableString shortString = StringUtil.setForeColorSpan(str0, 0, 5, getResources().getColor(R.color.text_red));
        SpannableString middleString = StringUtil.setForeColorSpan(str1, 0, 5, getResources().getColor(R.color.text_red));
        SpannableString technologyString = StringUtil.setForeColorSpan(str2, 0, 5, getResources().getColor(R.color.text_red));
        SpannableString basicString = StringUtil.setForeColorSpan(str3, 0, 6, getResources().getColor(R.color.text_red));
        SpannableString supplementString = StringUtil.setForeColorSpan(str4, 0, 3, getResources().getColor(R.color.text_red));
        mTvShort.setText(shortString);
        mTvMiddle.setText(middleString);
        mTvTechnology.setText(technologyString);
        mTvBasic.setText(basicString);
        mTvSupplement.setText(supplementString);
        if (null == mPhotoPathList || mPhotoPathList.size() == 0) {
            mPhotoPathList = new ArrayList<>();
        } else {
            mPhotoPathList.clear();
        }
        if (postAnswerBean.getImg1() != null && !postAnswerBean.getImg1().equals("")) {
            mPhotoPathList.add(postAnswerBean.getImg1());

        }
        if (postAnswerBean.getImg2() != null && !postAnswerBean.getImg2().equals("")) {
            mPhotoPathList.add(postAnswerBean.getImg2());
        }

        if (postAnswerBean.getImg3() != null && !postAnswerBean.getImg3().equals("")) {
            mPhotoPathList.add(postAnswerBean.getImg3());
        }

        adapter.notifyDataSetChanged();

    }


    @Override
    public int initResLayout() {
        return R.layout.activity_answer_preview;
    }


    @Override
    public void showLoading() {
        showLoadDialog();
        /**
         * 上传图片
         */
        if (null != postAnswerBean.getImg1() && !postAnswerBean.getImg1().equals("")) {
            flag += 1;
            String headIconName1 = postAnswerBean.getId() + "/" + StorageUtil.getFileName(postAnswerBean.getImg1());
            presenter.UploadImg(postAnswerBean.getImg1(), token, headIconName1);
            postAnswerBean.setImg1(headIconName1);
        } else {
            postAnswerBean.setImg1("");
        }

        if (null != postAnswerBean.getImg2() && !postAnswerBean.getImg2().equals("")) {
            flag += 1;
            String headIconName2 = postAnswerBean.getId() + "/" + StorageUtil.getFileName(postAnswerBean.getImg2());
            presenter.UploadImg(postAnswerBean.getImg2(), token, headIconName2);
            postAnswerBean.setImg2(headIconName2);


        } else {
            postAnswerBean.setImg2("");
        }

        if (null != postAnswerBean.getImg3() && !postAnswerBean.getImg3().equals("")) {
            flag += 1;
            String headIconName3 = postAnswerBean.getId() + "/" + StorageUtil.getFileName(postAnswerBean.getImg3());
            presenter.UploadImg(postAnswerBean.getImg3(), token, headIconName3);
            postAnswerBean.setImg3(headIconName3);

        } else {
            postAnswerBean.setImg3("");
        }

        flag += 1;
        presenter.commitTeacherAnswer(postAnswerBean);
    }


    @Override
    public void hideLoading() {
        ToastUtil.show(this, "提交成功", Toast.LENGTH_SHORT);
        startActivity(new Intent(this, DiagnoseSocketActivity.class));
        dialog.dismiss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.tv_commit:
                showSureDialog();
                break;
//            case :
//                break;
//            default:
//                break;
        }


    }


    private void showSureDialog() {
        new JoDialog.Builder(this).setStrTitle("\n是否确认提交?\n").setPositiveTextRes(R.string.confirm)
                .setNegativeTextRes(R.string.cancel).setCallback(new JoDialog.ButtonCallback() {
            @Override
            public void onPositive(JoDialog dialog) {
                showLoading();
                dialog.dismiss();
            }

            @Override
            public void onNegtive(JoDialog dialog) {
                dialog.dismiss();
            }
        }).show(true);
    }

    private void showLoadDialog() {
        dialog = new JoDialog.Builder(this)
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(true);
    }

    @Override
    public void onSuccess(DiagnosedType requestType, Object object) {
        switch (requestType) {
            case GET_TOKEN:
                token = (String) object;
                break;
            case UPLOAD_IMG:
                flag--;
                if (flag == 0) {
                    hideLoading();
                }
                break;
            case COMMIT_TEACHER_ANSWER:
                flag--;
                if (flag == 0) {
                    hideLoading();
                }
                break;
        }
    }

    @Override
    public void onError(DiagnosedType requestType, Throwable t) {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /**
     * 缩略图的显示adapter
     */
    class PicAdapter extends BaseAdapter {
        private int mColumnWidth = 75;
        private ImageView imageView;


        @Override
        public int getCount() {
            if (null == mPhotoPathList || mPhotoPathList.size() == 0) {
                return 0;
            } else
                return mPhotoPathList.size();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            ViewHolder holder;
//            if (null == convertView || null == convertView.getTag()) {
            imageView = new ImageView(_Activity);
            convertView = imageView;
//                convertView = LayoutInflater.from(_Activity).inflate(R.layout.item_image, null);
//                holder = new ViewHolder(convertView);
//                holder.mImageView = (ImageView) convertView.findViewById(R.id.imageView);
//                holder.mIvDelete = (ImageView) convertView.findViewById(R.id.iv_delete);
//                holder.mIvDelete.setVisibility(View.GONE);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//            ImageLoader.getInstance().display(getItem(position), holder.mImageView, DensityUtil.dip2px(_Activity, mColumnWidth), DensityUtil.dip2px(_Activity, mColumnWidth));
            GridView.LayoutParams layoutParams = new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT, 200);
            imageView.setLayoutParams(layoutParams);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().display(getItem(position), imageView, DensityUtil.dip2px(_Activity, mColumnWidth), DensityUtil.dip2px(_Activity, mColumnWidth));
            return convertView;
        }

        @Override
        public String getItem(int position) {
            return mPhotoPathList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
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

    /**
     * 设置星星
     *
     * @param star
     * @param view
     */
    private void setStarLevel(String star, ImageView view) {
        switch (star) {
            case "1":
                view.setImageResource(R.drawable.answer_star1);
                break;
            case "2":
                view.setImageResource(R.drawable.answer_star2);

                break;
            case "3":
                view.setImageResource(R.drawable.answer_star3);

                break;
            case "4":
                view.setImageResource(R.drawable.answer_star4);

                break;
            case "5":
                view.setImageResource(R.drawable.answer_star5);
                break;
        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("AboutUsActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("AboutUsActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }
}
