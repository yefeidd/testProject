package cn.zn.com.zn_android.uiclass.customerview;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.GiftRVAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.GiftBean;
import cn.zn.com.zn_android.uiclass.DividerGridItemDecoration;
import cn.zn.com.zn_android.utils.LogUtils;
import cn.zn.com.zn_android.utils.StringUtil;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.ChatVipView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Jolly on 2016/6/13 0013.
 */
public class ChatVIPInput extends ChatInput {
    private ChatVipView mChatVIPView;

    public ChatVIPInput(Context context) {
        this(context, null);
    }

    public ChatVIPInput(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatVIPInput(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    @Override
    protected void initView() {
        super.initView();
        mIbFen.setBackgroundResource(R.drawable.private_talk);
        mIbRanking.setVisibility(GONE);
        mIbVip.setVisibility(GONE);
    }

    @Override
    public void onClick(View v) {
//        if (v.getId() == R.id.ib_fan) {
//            showSendPrivateTalkDialog();
//        } else {
//            super.onClick(v);
//        }

        switch (v.getId()) {
            case R.id.tv_send:
                if (mEtMsg.getText().length() > 0) {
                    mChatVIPView.sendMsg();
                }
                break;
            case R.id.ib_fan:
                showSendPrivateTalkDialog();
                break;
            case R.id.ib_gift:
                startGiftDialog();
                break;
            default:
                super.onClick(v);
                break;
        }
    }

    public void setmChatView(ChatVipView mChatView) {
        mChatVIPView = mChatView;
    }

    /**
     * 发送悄悄话的dialog
     */
    Dialog privateTalkDialog = null;

    private void showSendPrivateTalkDialog() {
        CusDownUpDialog.Builder builder = new CusDownUpDialog.Builder(_activity);
        View layout = LayoutInflater.from(_activity).inflate(R.layout.layout_send_private_talk, new ListView(_activity), false);
        EditText priMsg = (EditText) layout.findViewById(R.id.et_private_msg);
        TextView msgSend = (TextView) layout.findViewById(R.id.tv_send_msg);
        msgSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = priMsg.getText().toString();
                if (StringUtil.isEmpty(msg)) {
                    ToastUtil.showShort(_activity, _activity.getString(R.string.msg_not_empty));
                    return;
                }
                mChatVIPView.sendPriConversion(msg);
                privateTalkDialog.dismiss();
                LogUtils.i("发送消息");
            }
        });
        builder.setLayoutView(layout);
        builder.setCancelable(true);
        privateTalkDialog = builder.create();
        privateTalkDialog.show();
    }

    /**
     * 选择礼物的Dialog
     */
    int index = 0;
    Dialog dialog = null;
    private List<GiftBean> giftList = new ArrayList<>();

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
        giftAdapter.setItemClickListener(((itemView, position) -> {
            String num = numStrArray[spinner.getSelectedItemPosition()];
            tvWealth.setText(String.format(_activity.getString(R.string.wealth_total), new Object[]{giftList.get(position).getGiftWealth() * Integer.valueOf(num)}));
            if (index >= 0) {
                giftList.get(index).setIsSelected(false);
            }
            giftList.get(position).setIsSelected(true);
            index = position;
            giftAdapter.notifyDataSetChanged();
        }));
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
                mChatVIPView.showVip();
            }
        });

        // 跳到充值页面
        Button btnRecharge = (Button) contentView.findViewById(R.id.btn_recharge);
        btnRecharge.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mChatVIPView.showRecharge();//充值
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
                        mChatVIPView.sendGift((index + 1) + "", num);
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

}
