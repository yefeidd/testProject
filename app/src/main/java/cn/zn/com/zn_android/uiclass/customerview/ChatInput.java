package cn.zn.com.zn_android.uiclass.customerview;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.FaceGVAdapter;
import cn.zn.com.zn_android.adapter.FaceVPAdapter;
import cn.zn.com.zn_android.adapter.GiftRVAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.GiftBean;
import cn.zn.com.zn_android.uiclass.CSPopWindow;
import cn.zn.com.zn_android.uiclass.DividerGridItemDecoration;
import cn.zn.com.zn_android.uiclass.NoScrollGridView;
import cn.zn.com.zn_android.uiclass.customerview.emoji.EmotionKeyboard;
import cn.zn.com.zn_android.uiclass.customerview.vpindicator.CirclePageIndicator;
import cn.zn.com.zn_android.utils.DensityUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.utils.UIUtil;
import cn.zn.com.zn_android.viewfeatures.ChatView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zjs on 2016/6/6 0006.
 * email: m15267280642@163.com
 * explain: 自定义的发送消息的按钮
 */
public class ChatInput extends LinearLayout implements View.OnClickListener, TextWatcher {

    private static final String TAG = "ChatInput";
    private List<String> qqList = new ArrayList<String>() {
        {
            add("103487016");
            add("407803757");
        }
    };
    protected Context mContext;
    protected Activity _activity;
    private List<GiftBean> giftList = new ArrayList<>();
    protected final int[] GIFT_RES = {R.mipmap.ic_gift1, R.mipmap.ic_gift2, R.mipmap.ic_gift3, R.mipmap.ic_gift4,
            R.mipmap.ic_gift5, R.mipmap.ic_gift6, R.mipmap.ic_gift7, R.mipmap.ic_gift8,
            R.mipmap.ic_gift9, R.mipmap.ic_gift10, R.mipmap.ic_gift11, R.mipmap.ic_gift12,
            R.mipmap.ic_gift13, R.mipmap.ic_gift14, R.mipmap.ic_gift15, R.mipmap.ic_gift16};
    protected ChatView mChatView;
    protected ImageButton mIbCs;
    protected ImageButton mIbFen;
    protected ImageButton mIbRanking;
    protected ImageButton mIbGift;
    protected ImageButton mIbVip;
    protected ImageButton mIbFace;
    public EditText mEtMsg;
    protected TextView mTvSend;
    protected ViewPager mVpFace;
    protected CirclePageIndicator mIndicator;
    protected LinearLayout mLlChatFaceContainer;

    protected boolean isSendVisible;
    protected boolean showFaceContainer = false;

    // 7列4行
    private int columns = 7;
    private int rows = 4;
    private List<View> views = new ArrayList<View>();
    private List<String> staticFacesList;
    private InputMode inputMode = InputMode.NONE;

    private EmotionKeyboard mEmotionKeyboard;
    private View contentView;   //需要绑定的内容view
    private boolean isBindToBarEditText = true; //是否绑定当前Bar的编辑框,默认true,即绑定。
                                                //false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
    private boolean isHidenBarEditTextAndBtn = false;   //是否隐藏bar上的编辑框和发生按钮,默认不隐藏

    public ChatInput(Context context) {
        this(context, null);
    }

    public ChatInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.chat_input, this);
        this.mContext = context;
        initView();

//        //创建全局监听
//        GlobalOnItemClickManagerUtils globalOnItemClickManager = GlobalOnItemClickManagerUtils.getInstance(getActivity());
//
//        if (isBindToBarEditText) {
//            //绑定当前Bar的编辑框
//            globalOnItemClickManager.attachToEditText(bar_edit_text);
//
//        } else {
//            // false,则表示绑定contentView,此时外部提供的contentView必定也是EditText
//            globalOnItemClickManager.attachToEditText((EditText) contentView);
//            mEmotionKeyboard.bindToEditText((EditText) contentView);
//        }
    }

    /**
     * 初始化布局
     */
    protected void initView() {
        mIbCs = (ImageButton) findViewById(R.id.ib_cs);
        mIbFen = (ImageButton) findViewById(R.id.ib_fan);
        mIbRanking = (ImageButton) findViewById(R.id.ib_ranking);
        mIbGift = (ImageButton) findViewById(R.id.ib_gift);
        mIbVip = (ImageButton) findViewById(R.id.ib_vip);
        mVpFace = (ViewPager) findViewById(R.id.vp_face);
        mIndicator = (CirclePageIndicator) findViewById(R.id.dot_indicator);
        mLlChatFaceContainer = (LinearLayout) findViewById(R.id.chat_face_container);
        mIbFace = (ImageButton) findViewById(R.id.ib_face);
        mIbFace.setOnClickListener(this);
        mEtMsg = (EditText) findViewById(R.id.et_msg);
        mEtMsg.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    updateView(InputMode.TEXT);
                }
            }
        });
        mEtMsg.addTextChangedListener(this);
        mTvSend = (TextView) findViewById(R.id.tv_send);
        mTvSend.setOnClickListener(this);
        mIbCs.setOnClickListener(this);
        mIbFen.setOnClickListener(this);
        mIbRanking.setOnClickListener(this);
        mIbGift.setOnClickListener(this);
        mIbVip.setOnClickListener(this);
        initStaticFaces();
        InitViewPager();
    }

    public void updateView(InputMode mode) {
        if (mode == InputMode.TEXT && mode == inputMode) return;
        leavingCurrentState();
        switch (inputMode = mode){
            case TEXT:
                showFaceContainer = false;
                if (mEtMsg.requestFocus()){
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(mEtMsg, InputMethodManager.SHOW_IMPLICIT);
                }
                break;
            case EMOTICON:
//                if (!isEmoticonReady) {
//                    prepareEmoticon();
//                }
                mEmotionKeyboard.hideEmotionLayout(false);
                showFaceContainer = !showFaceContainer;
                if (showFaceContainer) {
//                    mLlChatFaceContainer.setVisibility(VISIBLE);
                } else {
//                    mLlChatFaceContainer.setVisibility(GONE);
                }
                break;
            case NONE:
                showFaceContainer = false;
                mEmotionKeyboard.hideEmotionLayout(false);
                break;
        }
    }

    private void leavingCurrentState(){
        switch (inputMode){
            case TEXT:
                View view = ((Activity) getContext()).getCurrentFocus();
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                mEtMsg.clearFocus();
                break;
            case EMOTICON:
//                mLlChatFaceContainer.setVisibility(GONE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_cs:
                showCSPopWindow();
                break;
            //榜单的按钮
            case R.id.ib_ranking:
                mChatView.showRanking();
                break;
            case R.id.tv_send:
                if (mEtMsg.getText().length() > 0) {
                    mChatView.sendMsg();
                }

                break;
            case R.id.ib_gift:
                startGiftDialog();
                break;
            case R.id.ib_fan:
                mChatView.addCollectRoom();
                break;
            case R.id.ib_vip:
                mChatView.showVip();
                break;
            case R.id.ib_face:
//                updateView(inputMode == InputMode.EMOTICON?InputMode.TEXT:InputMode.EMOTICON);
                updateView(InputMode.EMOTICON);

                break;

//            default:
//                break;
        }
    }

    /**
     * 设置一个activity的上下文
     *
     * @param _activity
     */

    public void set_activity(Activity _activity) {
        this._activity = _activity;
    }

    public ChatInput setContentView(View contentView) {
        this.contentView = contentView;
        return this;
    }

    public ChatInput initEmotionKeyBoard() {

        mEmotionKeyboard = EmotionKeyboard.with(_activity)
                .setEmotionView(findViewById(R.id.chat_face_container))//绑定表情面板
                .bindToContent(contentView)//绑定内容view
                .bindToEditText(!isBindToBarEditText ? ((EditText) contentView) : ((EditText) findViewById(R.id.et_msg)))//判断绑定那种EditView
                .bindToEmotionButton(findViewById(R.id.ib_face))//绑定表情按钮
                .build();
        return this;
    }

    /**
     * 绑定发送消息的界面
     *
     * @param mChatView
     */
    public void setmChatView(ChatView mChatView) {
        this.mChatView = mChatView;
    }

    /**
     * 设置聊天的QQ客服列表
     *
     * @param qqList
     */
    public void setQqList(List<String> qqList) {
        this.qqList = qqList;
    }

    public void hideCsBtn() {
        mIbCs.setVisibility(GONE);
    }

    /**
     * 显示客服qq弹窗
     */
    private void showCSPopWindow() {
        CSPopWindow popWind = new CSPopWindow(_activity);
        popWind.showPopWind(_activity, mIbCs, qqList);
    }


    /**
     * 关注的dialog
     *
     * @param content
     */
    public void showFanDialog(String content) {

        new JoDialog.Builder(_activity)
                .setGravity(Gravity.CENTER)
                .setPositiveTextRes(R.string.confirm)
                .setStrTitle(R.string.tips)
                .setStrContent(content)
                .setCallback(new JoDialog.ButtonCallback() {
                    @Override
                    public void onPositive(JoDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {

                    }
                })
                .show(true);
    }


    /**
     * 监听edittxt中值的改变
     *
     * @param s
     * @param start
     * @param count
     * @param after
     */

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        isSendVisible = s != null && s.length() > 0;
        setSendBtn();
    }

    /**
     * 设置按钮状态
     */
    private void setSendBtn() {
        if (isSendVisible) {
            mTvSend.setBackgroundResource(R.drawable.sp_rect_orange);
            mTvSend.setClickable(true);
        } else {
            mTvSend.setBackgroundResource(R.drawable.sp_rect_corner_grey_light);
            mTvSend.setClickable(false);
        }
    }


    /**
     * 选择礼物的Dialog
     */
    int index = 0;
    Dialog dialog = null;

    private void startGiftDialog() {
        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(_activity);
        View contentView = LayoutInflater.from(_activity).inflate(R.layout.layout_give_gift, new ListView(_activity), false);
        builder.setBottomButton(false).setCancelable(true).setIsWidthFull(true);

        AppCompatSpinner spinner = (AppCompatSpinner) contentView.findViewById(R.id.sp_num);
        TextView tvWealth = (TextView) contentView.findViewById(R.id.tv_wealth_total);
        RecyclerView mRVGift = (RecyclerView) contentView.findViewById(R.id.rv_gift);

        tvWealth.setText(String.format(_activity.getString(R.string.wealth_total), new Object[]{0}));
        getData();
        String[] numStrArray = getResources().getStringArray(R.array.num);
        GiftRVAdapter giftAdapter = new GiftRVAdapter(_activity, giftList);
        giftAdapter.setItemClickListener(new GiftRVAdapter.OnItemClickListener() {
            @Override
            public void onClick(View itemView, int position) {
                String num = numStrArray[spinner.getSelectedItemPosition()];
                tvWealth.setText(String.format(_activity.getString(R.string.wealth_total), new Object[]{giftList.get(position).getGiftWealth() * Integer.valueOf(num)}));
                if (index >= 0) {
                    giftList.get(index).setIsSelected(false);
                }
                giftList.get(position).setIsSelected(true);
                index = position;
                giftAdapter.notifyDataSetChanged();
            }
        });
        mRVGift.setAdapter(giftAdapter);
        mRVGift.addItemDecoration(new DividerGridItemDecoration(_activity));
        mRVGift.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        String[] numArray = getResources().getStringArray(R.array.num);
        List<Map<String, String>> spinnerData = new ArrayList<>();
        for (String ss : numArray) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.TITLE, ss);
            spinnerData.add(map);
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(_activity, R.layout.item_spinner_text, numArray);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (index >= 0) {
                    String num = numStrArray[position];
                    tvWealth.setText(String.format(_activity.getString(R.string.wealth_total), new Object[]{giftList.get(index).getGiftWealth() * Integer.valueOf(num)}));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // 送礼物按钮
        Button btnGive = (Button) contentView.findViewById(R.id.btn_give);
        btnGive.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (index >= 0) {
                    showGiveConfirmDialog(giftList.get(index).getGiftName(), giftList.get(index).getGiftWealth(), numStrArray[spinner.getSelectedItemPosition()]);
                    dialog.dismiss();
                    giftList.get(index).setIsSelected(false);
//                    ToastUtil.showShort(_mApplication, giftList.get(index).getGiftName() + "," + giftList.get(index).getGiftWealth() + "*" + numStrArray[spinner.getSelectedItemPosition()]);
                } else {
                    ToastUtil.showShort(_activity, "请先选择礼物");
                }
            }
        });

        // 跳转VIP页面
        Button btnVip = (Button) contentView.findViewById(R.id.btn_vip);
        btnVip.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatView.showVip();
            }
        });

        // 跳到充值页面
        Button btnRecharge = (Button) contentView.findViewById(R.id.btn_recharge);
        btnRecharge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatView.showRecharge();//充值
            }
        });
        builder.setContentView(contentView);
        dialog = builder.create();
        dialog.show();

    }


    /**
     * 初始化礼物
     */
    private void getData() {
        giftList.clear();
        String[] giftName = getResources().getStringArray(R.array.gift_names);
        int[] giftwealth = getResources().getIntArray(R.array.gift_wealth);
        for (int i = 0; i < giftName.length; i++) {
            GiftBean bean = new GiftBean();
            if (i == 0) {
                bean.setIsSelected(true);
            } else {
                bean.setIsSelected(false);
            }
            bean.setGiftName(giftName[i]);
            bean.setGiftWealth(giftwealth[i]);
            bean.setImgRes(GIFT_RES[i]);
            giftList.add(bean);
        }
    }

    /**
     * 提示确认送礼物的Dialog
     *
     * @param giftName
     * @param wealth
     * @param num
     */
    private void showGiveConfirmDialog(String giftName, int wealth, String num) {
        new JoDialog.Builder(_activity)
                .setStrTitle(R.string.tips)
                .setGravity(Gravity.CENTER)
                .setStrContent(_activity.getString(R.string.give_confirm))
                .setPositiveTextRes(R.string.confirm)
                .setNegativeTextRes(R.string.cancel)
                .setCallback(new JoDialog.ButtonCallback() {
                    @Override
                    public void onPositive(JoDialog dialog) {
                        dialog.dismiss();
                        mChatView.sendGift((index + 1) + "", num);
                        giftList.get(index).setIsSelected(false);
                        index = 0;
//                        ToastUtil.showShort(_mApplication, giftName + "," + wealth);
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {
                        giftList.get(index).setIsSelected(false);
                        index = 0;
                        dialog.dismiss();
                    }
                })
                .setCancelableOut(false)
                .show(true);

    }

    public void clearEdit() {
        mEtMsg.setText("");

    }

    public void hideKeyBoard() {
        //隐藏软键盘
        View view = _activity.getCurrentFocus();
        UIUtil.hidekeyBoard(view);
    }

    public Editable getMsg() {
        return mEtMsg.getText();
    }

    private int getPagerCount() {
        int count = staticFacesList.size();
        return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
                : count / (columns * rows - 1) + 1;
    }

    private void initStaticFaces() {
        try {
            staticFacesList = new ArrayList<String>();
            String[] faces = mContext.getAssets().list("face/png");
            for (int i = 0; i < faces.length; i++) {
                staticFacesList.add(faces[i]);
            }
            staticFacesList.remove("emotion_del_normal.png");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 初始表情
     */
    private void InitViewPager() {
        // 获取页数
        for (int i = 0; i < getPagerCount(); i++) {
            views.add(viewPagerItem(i));
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(16, 16);
            // LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
            // LayoutParams.WRAP_CONTENT);
        }
        FaceVPAdapter mVpAdapter = new FaceVPAdapter(views);
        mVpFace.setAdapter(mVpAdapter);
        mIndicator.setViewPager(mVpFace);

    }

    private View viewPagerItem(int position) {
//        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        View layout = inflater.inflate(R.layout.layout_gv_face, null);
//        GridView gridview = (GridView) layout.findViewById(R.id.gv_chart_face);
        NoScrollGridView gridview = new NoScrollGridView(mContext);
        /**
         * 注：因为每一页末尾都有一个删除图标，所以每一页的实际表情columns *　rows - 1; 空出最后一个位置给删除图标
         * */
        List<String> subList = new ArrayList<String>();
        subList.addAll(staticFacesList
                .subList(position * (columns * rows - 1),
                        (columns * rows - 1) * (position + 1) > staticFacesList
                                .size() ? staticFacesList.size() : (columns
                                * rows - 1)
                                * (position + 1)));
        // 0-20 20-40 40-60 60-80

        // 获取屏幕宽度
        int screenWidth = DensityUtil.getScreenWidth(mContext)[0];
        // item的间距
        int spacing = DensityUtil.dip2px(mContext, 12);
        // 动态计算item的宽度和高度
        int itemWidth = (screenWidth - spacing * 8) / 7;
        //动态计算gridview的总高度
        int gvHeight = itemWidth * 3 + spacing * 6;

        /**
         * 末尾添加删除图标
         * */
        subList.add("emotion_del_normal.png");
        FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, mContext);
        gridview.setAdapter(mGvAdapter);
        gridview.setNumColumns(columns);
        gridview.setPadding(spacing, spacing, spacing, spacing);
        gridview.setHorizontalSpacing(spacing);
        gridview.setVerticalSpacing(spacing * 2);
        //设置GridView的宽高
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(screenWidth, gvHeight);
        Log.d(TAG, "gvHeight: " + gvHeight);
        gridview.setLayoutParams(params);
        // 单击表情执行的操作
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                try {
                    String png = ((TextView) ((LinearLayout) view)
                            .getChildAt(1)).getText().toString();
                    if (!png.contains("emotion_del_normal")) {// 如果不是删除图标
                        // input.setText(sb);
                        insert(getFace(png));
                    } else {
                        delete();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return gridview;
    }

    private SpannableStringBuilder getFace(String png) {
        SpannableStringBuilder sb = new SpannableStringBuilder();
        try {
            /**
             * 经过测试，虽然这里tempText被替换为png显示，但是但我单击发送按钮时，获取到輸入框的内容是tempText的值而不是png
             * 所以这里对这个tempText值做特殊处理
             * 格式：#[face/png/f_static_000.png]#，以方便判斷當前圖片是哪一個
             * */
            String tempText = "#[" + png + "]#";
            sb.append(tempText);
            sb.setSpan(
                    new ImageSpan(mContext, BitmapFactory
                            .decodeStream(mContext.getAssets().open(png))), sb.length()
                            - tempText.length(), sb.length(),
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return sb;
    }

    /**
     * 向输入框里添加表情
     * */
    private void insert(CharSequence text) {
        int iCursorStart = Selection.getSelectionStart((mEtMsg.getText()));
        int iCursorEnd = Selection.getSelectionEnd((mEtMsg.getText()));
        if (iCursorStart != iCursorEnd) {
            ((Editable) mEtMsg.getText()).replace(iCursorStart, iCursorEnd, "");
        }
        int iCursor = Selection.getSelectionEnd((mEtMsg.getText()));
        ((Editable) mEtMsg.getText()).insert(iCursor, text);
    }

    /**
     * 删除图标执行事件
     * 注：如果删除的是表情，在删除时实际删除的是tempText即图片占位的字符串，所以必需一次性删除掉tempText，才能将图片删除
     * */
    private void delete() {
        if (mEtMsg.getText().length() != 0) {
            int iCursorEnd = Selection.getSelectionEnd(mEtMsg.getText());
            int iCursorStart = Selection.getSelectionStart(mEtMsg.getText());
            if (iCursorEnd > 0) {
                if (iCursorEnd == iCursorStart) {
                    if (isDeletePng(iCursorEnd)) {
                        String st = "#[face/png/f_static_000.png]#";
                        ((Editable) mEtMsg.getText()).delete(
                                iCursorEnd - st.length(), iCursorEnd);
                    } else {
                        ((Editable) mEtMsg.getText()).delete(iCursorEnd - 1,
                                iCursorEnd);
                    }
                } else {
                    ((Editable) mEtMsg.getText()).delete(iCursorStart,
                            iCursorEnd);
                }
            }
        }
    }

    /**
     * 判断即将删除的字符串是否是图片占位字符串tempText 如果是：则讲删除整个tempText
     * **/
    private boolean isDeletePng(int cursor) {
        String st = "#[face/png/f_static_000.png]#";
        String content = mEtMsg.getText().toString().substring(0, cursor);
        if (content.length() >= st.length()) {
            String checkStr = content.substring(content.length() - st.length(),
                    content.length());
            String regex = "(\\#\\[face/png/f_static_)\\d{3}(.png\\]\\#)";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(checkStr);
            return m.matches();
        }
        return false;
    }

    public enum InputMode{
        TEXT,
        VOICE,
        EMOTICON,
        MORE,
        VIDEO,
        NONE,
    }

}
