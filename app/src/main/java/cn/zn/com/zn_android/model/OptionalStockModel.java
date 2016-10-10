package cn.zn.com.zn_android.model;

import cn.zn.com.zn_android.adapter.EditOpStockAdapter;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;

/**
 * Created by Jolly on 2016/6/28 0028.
 */
public class OptionalStockModel {
    private OptionalStockBean stockBean;

    public OptionalStockModel(OptionalStockBean stockBean) {
        this.stockBean = stockBean;
    }

    public void showOpStock(EditOpStockAdapter.ViewHolder viewHolder) {
        viewHolder.mTitle.setText(stockBean.getName());
        viewHolder.mText1.setText(stockBean.getCode());
    }

    public String getId() {
        return stockBean.getId();
    }
}
