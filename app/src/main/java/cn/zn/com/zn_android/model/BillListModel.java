package cn.zn.com.zn_android.model;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import java.util.ArrayList;
import java.util.List;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.viewHolder.BaseViewHolder;
import cn.zn.com.zn_android.adapter.viewHolder.BillListHolder;
import cn.zn.com.zn_android.adapter.viewHolder.ListViewAdapter;
import cn.zn.com.zn_android.uiclass.activity.BillDetailActivity;

/**
 * Created by Jolly on 2016/12/7.
 */

public class BillListModel extends ListviewItemModel {
    private String month;
    private List<BillModel> data = new ArrayList<>();

    public BillListModel(String month, List<BillModel> data) {
        this.month = month;
        this.data = data;
    }

    @Override
    public void showItem(BaseViewHolder viewHolder, Context context, int position) {
        BillListHolder holder = (BillListHolder) viewHolder;
        holder.mTitle.setText(month);
        ListViewAdapter adapter = new ListViewAdapter(context, R.layout.item_bill, data, "BillHolder");
        holder.mSllSs.setAdapter(adapter);
        holder.mSllSs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String billID = data.get(position).getBean().getId();  //获取账单id
                Intent intent = new Intent(context, BillDetailActivity.class);
                intent.putExtra("billID", billID);
                context.startActivity(intent);
            }
        });
    }

    public String getMonth() {
        return month;
    }
}
