package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.OrderAllHolder;
import cn.zn.com.zn_android.uiclass.activity.OrderConfirmActivity;

/**
 * Created by Jolly on 2016/12/7.
 */

public class OrderAllModel extends ListviewItemModel {
    private String question;
    private String price;
    private String type;
    private String time;
    private int pay;
    private String orderNum;

    public OrderAllModel(String question, String price, String type, String time, int pay, String orderNum) {
        this.question = question;
        this.price = price;
        this.type = type;
        this.time = time;
        this.pay = pay;
        this.orderNum = orderNum;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        OrderAllHolder holder = (OrderAllHolder) viewHolder;
        holder.mTvQuestion.setText(question);
        holder.mTvPrice.setText(price);
        holder.mTvTime.setText(time);
        holder.mTvType.setText(type);
        switch (pay) { // 1 待支付 2 已支付  3  取消支付
            case 1:
                holder.mTvPrice.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_money_red, 0, 0, 0);
                holder.mTvPrice.setTextColor(context.getResources().getColor(R.color.text_red));
                holder.mIbPay.setImageResource(R.mipmap.ic_order_pay_now);
                break;
            case 2:
                holder.mTvPrice.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_money_grey, 0, 0, 0);
                holder.mTvPrice.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.mIbPay.setImageResource(R.mipmap.ic_order_payed);
                break;
            case 3:
                holder.mTvPrice.setCompoundDrawablesWithIntrinsicBounds(R.mipmap.ic_money_grey, 0, 0, 0);
                holder.mTvPrice.setTextColor(context.getResources().getColor(R.color.text_gray));
                holder.mIbPay.setImageResource(R.mipmap.ic_order_cancel);
                break;
        }
        holder.mIbPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pay == 1) {
                    Intent intent = new Intent(context, OrderConfirmActivity.class);
                    intent.putExtra("orderNum", orderNum);
                    context.startActivity(intent);
//                    context.startActivity(new Intent(context, OrderConfirmActivity.class));
                }
            }
        });
    }
}
