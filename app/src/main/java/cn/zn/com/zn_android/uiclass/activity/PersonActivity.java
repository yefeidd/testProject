package cn.zn.com.zn_android.uiclass.activity;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.uiclass.customerview.CusDownUpDialog;
import cn.zn.com.zn_android.uiclass.customerview.widget.NumericWheelAdapter;
import cn.zn.com.zn_android.uiclass.customerview.widget.OnWheelScrollListener;
import cn.zn.com.zn_android.uiclass.customerview.widget.WheelView;
import cn.zn.com.zn_android.utils.StorageUtil;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import com.facebook.drawee.view.SimpleDraweeView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 个人资料Activity
 */
public class PersonActivity extends BaseActivity implements View.OnClickListener {
    private String IMAGE_FILE_LOCATION = null;//head icon temp file
    Uri imageUri = null;//The Uri to store the big bitmap
    private static final int TAKE_SMALL_PICTURE = 1;//this is a requestCode for SMALL
    private static final int CROP_SMALL_PICTURE = 2;//this is a requestCode for crop SMALL
    private static final int CHOOSE_SMALL_PICTURE = 3;//this is a requestCode for choose SMALL
    private static final int MALE = 1;
    private static final int FEMALE = 2;
    private static final String TAG = "PersonActivity";
    private Dialog headIconDialog = null;
    private Dialog calendarDialog = null;
    private Dialog selectSexDialog = null;
    private final String IMAGE_TYPE = "image/*";
    private final int SIZE = 200;

    @Bind(R.id.iv_leftmenu)
    ImageView mIvLeftmenu;
    @Bind(R.id.toolbar_title)
    TextView mToolbarTitle;
    @Bind(R.id.sdv_avatar)
    SimpleDraweeView mSdvAvatar; // 头像
    @Bind(R.id.rl_avatar)
    RelativeLayout mRlAvatar; // 头像layout
    @Bind(R.id.tv_nick)
    TextView mTvNick; // 昵称Txt
    @Bind(R.id.rl_nick)
    RelativeLayout mRlNick; // 昵称layout
    @Bind(R.id.tv_sex)
    TextView mTvSex; // 性别Txt
    @Bind(R.id.rl_sex)
    RelativeLayout mRlSex; // 性别layout
    @Bind(R.id.tv_birth)
    TextView mTvBirth; // 生日Txt
    @Bind(R.id.rl_birth)
    RelativeLayout mRlBirth; // 生日layout
    @Bind(R.id.tv_location)
    TextView mTvLocation; // 位置Txt
    @Bind(R.id.rl_location)
    RelativeLayout mRlLocation; // 位置layout
    @Bind(R.id.tv_signature)
    TextView mTvSignature; // 个性签名Txt
    @Bind(R.id.rl_signature)
    RelativeLayout mRlSignature; // 个性签名layout
    @Bind(R.id.tv_phone)
    TextView mTvPhone; // 手机号Txt
    @Bind(R.id.rl_phone)
    RelativeLayout mRlPhone; // 手机号layout
    @Bind(R.id.rl_pw)
    RelativeLayout mRlPw; // 登录密码layout
    private File pathName;
    private String dateStr;
    private int sex;
    private String headIconName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_person);
        inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected void initView() {
        mToolbarTitle.setText(getString(R.string.person_info));
        mTvNick.setText(_mApplication.getUserInfo().getName());
        if (_mApplication.getUserInfo().getSex() != null && !_mApplication.getUserInfo().getSex().equals("")) {
            switch (_mApplication.getUserInfo().getSex()) {
                case "0":
                    mTvSex.setText("保密");
                    break;
                case "1":
                    mTvSex.setText("男");
                    break;
                case "2":
                    mTvSex.setText("女");
                    break;
            }
        }
        mTvBirth.setText(_mApplication.getUserInfo().getBirthday());
        mTvLocation.setText(_mApplication.getUserInfo().getProvince());
        mTvPhone.setText(_mApplication.getUserInfo().getPhone());
        if (_mApplication.getUserInfo().getAvatars() != null) {
            mSdvAvatar.setImageURI(Uri.parse(_mApplication.getUserInfo().getAvatars()));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("PersonActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

        if (!_mApplication.getUserInfo().getName().equals(mTvNick.getText().toString())) {
            mTvNick.setText(_mApplication.getUserInfo().getName());
        }
        if (_mApplication.getUserInfo().getProvince() != null) {
            mTvLocation.setText(_mApplication.getUserInfo().getProvince());
            if (_mApplication.getUserInfo().getCity() != null) {
                mTvLocation.append(_mApplication.getUserInfo().getCity());
            }
        } else if (_mApplication.getUserInfo().getCity() != null) {
            mTvLocation.setText(_mApplication.getUserInfo().getCity());
        }

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("PersonActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initEvent() {
        mIvLeftmenu.setOnClickListener(this);
        mRlAvatar.setOnClickListener(this);
        mRlNick.setOnClickListener(this);
        mRlSex.setOnClickListener(this);
        mRlBirth.setOnClickListener(this);
        mRlLocation.setOnClickListener(this);
        mRlPhone.setOnClickListener(this);
        mRlPw.setOnClickListener(this);
        mRlSignature.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_leftmenu:
                finish();
                break;
            case R.id.rl_avatar: // 头像
                showDialog();
                break;
            case R.id.rl_nick: // 昵称
                startActivity(new Intent(_mApplication, ModifyNickActivity.class));
                break;
            case R.id.rl_sex: // 性别
                showSexDialog();
                break;
            case R.id.rl_birth: // 生日
//                ToastUtil.showShort(_mApplication, getString(R.string.person_birth));
                showCalendarDialog();
                break;
            case R.id.rl_location: // 所在地
                startActivity(new Intent(_mApplication, LocationActivity.class));
                break;
//            case R.id.rl_phone: // 手机号
//                ToastUtil.showShort(_mApplication, getString(R.string.phone_num));
//                break;
            case R.id.rl_pw: // 登录密码
                startActivity(new Intent(_mApplication, ModifyPWActivity.class));
                break;
            case R.id.rl_signature: // 个性签名
                Intent signI = new Intent(_mApplication, SignatureActivity.class);
                signI.putExtra("signature", _mApplication.getUserInfo().getSignature());
                startActivity(signI);
                break;

        }
    }


    /**
     * 弹出选择照片的dialog
     */
    public void showDialog() {
        //拿到缓存目录，有sd卡/sdcard/Android/data/包名/cache/
        //没有sd卡:data/data/包名/cache
        IMAGE_FILE_LOCATION = StorageUtil.getCachePath(getApplication());
        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(this);
//        builder.setTitle("提示");
        builder.setCancelable(true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_camera_dialog, new ListView(this), false);
        contentView.findViewById(R.id.tv_camera).setOnClickListener(new View.OnClickListener() {
            @Override
            //打开相机并返回一个数据
            public void onClick(View v) {
                //创建缓存目录，如果没有就创建一个，有的话直接拿到该路径
                FileIsExisi();
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//action is capture
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, TAKE_SMALL_PICTURE);//or TAKE_SMALL_PICTURE
                headIconDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.tv_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //创建缓存目录，如果没有就创建一个，有的话直接拿到该路径
                FileIsExisi();
                doPickPhotoFromGallery();
                headIconDialog.dismiss();
            }
        });
        builder.setContentView(contentView);
        builder.setBottomButton("取消", new CusDownUpDialog.CusDialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        headIconDialog = builder.create();
        headIconDialog.show();

    }

    /**
     * 从相册获取图片
     **/
    private void doPickPhotoFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType(IMAGE_TYPE);  // 开启Pictures画面Type设定为image
//        intent.setAction(Intent.ACTION_GET_CONTENT); //使用Intent.ACTION_GET_CONTENT这个Action
        startActivityForResult(intent, CHOOSE_SMALL_PICTURE); //取得相片后返回到本画面
    }


    /**
     * 判断该路径是否存在，不存在就创建
     */
    private void FileIsExisi() {
        pathName = new File(IMAGE_FILE_LOCATION);
        pathName.mkdirs();
        headIconName = StorageUtil.getFileName();
        pathName = new File(pathName, headIconName + ".jpg");
        imageUri = Uri.fromFile(pathName);//The Uri to store the big bitmap
        Log.i(TAG, "FileIsExisi: " + imageUri.toString());
    }

    private void getQNToken() {
        AppObservable.bindActivity(this, _apiManager.getService().getUpLoadToken(""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::uploadImg, throwable -> {
                    Log.e(TAG, "getQNToken: 异常");
                });
    }

    private void uploadImg(ReturnValue<MessageBean> returnValue) {
        Map<String, String> map = new HashMap<>();
//        map.put("returnUrl", )
        String token = returnValue.getData().getMessage();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(pathName, headIconName, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        Log.i("qiniu", info.toString());
                    }
                }, null);
    }

    /**
     * 拿到返回的结果
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case TAKE_SMALL_PICTURE:
                Log.i(TAG, "TAKE_SMALL_PICTURE: data = " + data);
                //TODO sent to crop
                //根据返回码判断是否取消拍照
                if (resultCode != 0) {
                    cropImageUri(imageUri, SIZE, SIZE, CROP_SMALL_PICTURE);
                } else {
                    return;
                }
                break;
            case CROP_SMALL_PICTURE:
                if (data != null) {
                    getQNToken();
                    modifyAvatars();
                }
                break;
            case CHOOSE_SMALL_PICTURE:
                ContentResolver resolver = getContentResolver();
                if (data != null) {
                    Uri originalUri = data.getData();
                    if (originalUri != null) {
                        try {
                            //使用ContentProvider通过URI获取原始图片
                            Bitmap photo = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                            //写一个方法将此文件保存到本应用下面啦
                            savePicture(pathName.getAbsolutePath(), photo);//pathname和imageUri指向同一个图片
                            if (photo != null) {
                                cropImageUri(imageUri, SIZE, SIZE, CROP_SMALL_PICTURE);
                            }
                            //abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789

//                        if (photo != null) {
//                            //为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
//                            Bitmap smallBitmap = ImageTools.zoomBitmap(photo, photo.getWidth() / 5, photo.getHeight() / 5);
//                            savePicture(pathName.getAbsolutePath(), smallBitmap);
//
//                        }
//		                iv_temp.setImageURI(originalUri);   //在界面上显示图片
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        return;
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 裁剪照片
     *
     * @param uri
     * @param outputX
     * @param outputY
     * @param requestCode
     */
    private void cropImageUri(Uri uri, int outputX, int outputY, int requestCode) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, IMAGE_TYPE);
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", outputX);
        intent.putExtra("outputY", outputY);
        intent.putExtra("scale", true);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, requestCode);

    }


    /**
     * 保存图片到本应用下
     **/
    private void savePicture(String fileName, Bitmap bitmap) {

        FileOutputStream fos = null;
        try {//直接写入名称即可，没有会被自动创建；私有：只有本应用才能访问，重新内容写入会被覆盖
            fos = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);// 把图片写入指定文件夹中

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fos) {
                    fos.close();
                    fos = null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private LayoutInflater inflater = null;
    private WheelView year;
    private WheelView month;
    private WheelView day;

    /**
     * 弹出设置日历的dialog
     */
    private void showCalendarDialog() {
        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(this);
        //  builder.setTitle("提示");
        builder.setCancelable(true);
        builder.setBottomButton(false);
        builder.setContentView(getDataPick());
        calendarDialog = builder.create();
        calendarDialog.show();
    }


    /**
     * @return 返回日历view
     */
    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        View view = inflater.inflate(R.layout.datapick, null);
        year = (WheelView) view.findViewById(R.id.year);
        year.setAdapter(new NumericWheelAdapter(1950, curYear));
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        month.setAdapter(new NumericWheelAdapter(1, 12));
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        day.setCyclic(true);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        Button bt = (Button) view.findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateStr = (year.getCurrentItem() + 1950) + "-" + StringUtil.int2DoubleString((month.getCurrentItem() + 1), 2) + "-" + StringUtil.int2DoubleString((day.getCurrentItem() + 1), 2);
                modifyBirthday();
                calendarDialog.dismiss();
            }
        });
        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendarDialog.dismiss();
            }
        });
        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
            int n_year = year.getCurrentItem() + 1950;//
            int n_month = month.getCurrentItem() + 1;//
            initDay(n_year, n_month);
        }
    };


    /**
     * 为日历设置数据
     *
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    /**
     * 初始化日历
     */
    private void initDay(int arg1, int arg2) {
        day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
    }

    /**
     * 弹出一个选择性别的dialog
     */
    private void showSexDialog() {
        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(this);
        builder.setTitle("修改性别");
        builder.setCancelable(true);
        View contentView = LayoutInflater.from(this).inflate(R.layout.item_sex_dialog, new ListView(this), false);
        contentView.findViewById(R.id.tv_male).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = MALE;
                modifySex();
                selectSexDialog.dismiss();
            }
        });
        contentView.findViewById(R.id.tv_female).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sex = FEMALE;
                modifySex();
                selectSexDialog.dismiss();
            }
        });
        builder.setContentView(contentView);
        builder.setBottomButton("取消", new CusDownUpDialog.CusDialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        selectSexDialog = builder.create();
        selectSexDialog.show();
    }


    /**
     * 修改头像
     */
    public void modifyAvatars() {
        AppObservable.bindActivity(this, _apiManager.getService().modifyMemberAvatars(_mApplication.getUserInfo().getSessionID(),
                headIconName))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::modifyMemberAvatars, throwable -> {
                    Log.i(TAG, "modifyMemberAvatars: 异常");
                    ToastUtil.showShort(this, getString(R.string.modify_fail));
                });

    }

    private void modifyMemberAvatars(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            ToastUtil.showShort(this, getString(R.string.modify_success));
            if (imageUri != null) {
                mSdvAvatar.setImageURI(imageUri, this);
            }
        } else {
            ToastUtil.showShort(this, getString(R.string.modify_fail));
            if (_mApplication.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
            }
        }
    }

    /**
     * 修改生日
     */
    public void modifyBirthday() {
        AppObservable.bindActivity(this, _apiManager.getService().modifyMemberBirthday(_mApplication.getUserInfo().getSessionID(),
                dateStr))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::modifyMemberBirthday, throwable -> {
                    Log.i(TAG, "modifyMemberBirthday: 异常");
                    ToastUtil.showShort(this, getString(R.string.modify_fail));
                });

    }

    /**
     * 处理修改日期后的返回消息
     */
    private void modifyMemberBirthday(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            _mApplication.getUserInfo().setBirthday(dateStr);
            mTvBirth.setText(dateStr);
            ToastUtil.showShort(this, getString(R.string.modify_success));
        } else {
            ToastUtil.showShort(this, getString(R.string.modify_fail));
            if (_mApplication.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
            }
        }
    }

    /**
     * 修改性别
     */
    public void modifySex() {
        AppObservable.bindActivity(this, _apiManager.getService().modifyMemberSex(_mApplication.getUserInfo().getSessionID(),
                sex + ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::modifyMemberSex, throwable -> {
                    Log.i(TAG, "modifyMemberSex: 异常");
                    ToastUtil.showShort(this, getString(R.string.modify_fail));
                });
    }

    private void modifyMemberSex(ReturnValue<MessageBean> returnValue) {
        if (returnValue != null && returnValue.getMsg().equals(Constants.SUCCESS)) {
            _mApplication.getUserInfo().setBirthday(dateStr);
            mTvSex.setText(sex == 1 ? getString(R.string.male) : getString(R.string.female));
            ToastUtil.showShort(this, getString(R.string.modify_success));
        } else {
            ToastUtil.showShort(this, getString(R.string.modify_fail));
            if (_mApplication.getResources().getString(R.string.please_login).equals(returnValue.getData().getMessage())) {
                _mApplication.getUserInfo().setIsLogin(Constants.NOT_LOGIN);
            }
        }
    }
}
