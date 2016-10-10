package cn.zn.com.zn_android.model.factory;

import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.DfbBean;
import cn.zn.com.zn_android.model.bean.GjgbBean;
import cn.zn.com.zn_android.model.bean.HKRankListBean;
import cn.zn.com.zn_android.model.bean.HotHyBean;
import cn.zn.com.zn_android.model.bean.HslBean;
import cn.zn.com.zn_android.model.bean.OptionalStockBean;
import cn.zn.com.zn_android.model.bean.SelfSelectStockBean;
import cn.zn.com.zn_android.model.bean.ZfbBean;
import cn.zn.com.zn_android.model.bean.ZflBean;
import cn.zn.com.zn_android.model.modelMole.MarketImp;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jolly on 2016/8/16 0016.
 */
public class StockRankingFactory {

    public static List<MarketImp> ZfbToMarketImp(List<ZfbBean> beanList) {
        List<MarketImp> dataList = new ArrayList<>();
        for (ZfbBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getShortNM());
            stockBean.setCode(bean.getTicker());
            stockBean.setPrice(bean.getLastPrice() + "");
            stockBean.setChangePct(Float.valueOf(bean.getChangePct() + ""));
            stockBean.setmStockType(Constants.SH);
            dataList.add(stockBean);
        }
        return dataList;
    }

    public static List<MarketImp> DfbToMarketImp(List<DfbBean> beanList) {
        List<MarketImp> dataList = new ArrayList<>();
        for (DfbBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getShortNM());
            stockBean.setCode(bean.getTicker());
            stockBean.setPrice(bean.getLastPrice() + "");
            stockBean.setChangePct(Float.valueOf(bean.getChangePct() + ""));
            stockBean.setmStockType(Constants.SH);
            dataList.add(stockBean);
        }
        return dataList;
    }

    public static List<MarketImp> HslToMarketImp(List<HslBean> beanList) {
        List<MarketImp> dataList = new ArrayList<>();
        for (HslBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getSEC_SHORT_NAME());
            stockBean.setCode(bean.getTICKER_SYMBOL());
            stockBean.setPrice(bean.getLastPrice() + "");
            stockBean.setChangePct(Float.valueOf(bean.getHsl() + ""));
            stockBean.setmStockType(Constants.SH);
            dataList.add(stockBean);
        }
        return dataList;
    }

    public static List<MarketImp> ZfToMarketImp(List<ZflBean> beanList) {
        List<MarketImp> dataList = new ArrayList<>();
        for (ZflBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getSecurityName());
            stockBean.setCode(bean.getSecurityID());
            stockBean.setPrice(bean.getLastPrice() + "");
            try {
                if (null != bean.getZhenful()) {
                    stockBean.setChangePct(Float.valueOf(bean.getZhenful() + ""));
                } else {
                    stockBean.setChangePct(Float.valueOf(bean.getZfl() + ""));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            stockBean.setmStockType(Constants.SH);
            dataList.add(stockBean);
        }
        return dataList;
    }

    public static List<MarketImp> GjgbToMarketImp(List<GjgbBean> beanList) {
        List<MarketImp> dataList = new ArrayList<>();
        for (GjgbBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getSecurityName());
            stockBean.setCode(bean.getSecurityID());
            stockBean.setPrice(bean.getLastPrice() + "");
            try {
                if (null != bean.getZhangful()) {
                    stockBean.setChangePct(Float.valueOf(bean.getZhangful()));
                } else {
                    stockBean.setChangePct(Float.valueOf(bean.getZfl()));
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            stockBean.setmStockType(Constants.SH);
            dataList.add(stockBean);
        }
        return dataList;
    }

    public static List<MarketImp> SelfStockToMarketImp(List<SelfSelectStockBean> beanList) {
        List<MarketImp> dataList = new ArrayList<>();
        for (SelfSelectStockBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getStock_name());
            stockBean.setCode(bean.getTicker());
            stockBean.setPrice(bean.getCprice() + "");
            stockBean.setChangePct(Float.valueOf(bean.getChange_rate() + ""));
            stockBean.setmStockType(Constants.SH);
            stockBean.setId(bean.getId());
            dataList.add(stockBean);
        }
        return dataList;
    }

    public static List<MarketImp> HotHyToMarketImp(List<HotHyBean> beanList) {
        List<MarketImp> dataList = new ArrayList<>();
        for (HotHyBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getName());
            stockBean.setCode(bean.getCode());
            stockBean.setPrice(bean.getPrice() + "");
            if (null != bean.getChange()) {
                stockBean.setChangePct(Float.valueOf(bean.getChange()));
            }
            stockBean.setmStockType(Constants.SH);
            dataList.add(stockBean);
        }
        return dataList;
    }

    public static List<MarketImp> HKToMarketImp(List<HKRankListBean> beanList, int shOrHk) {
        List<MarketImp> dataList = new ArrayList<>();
        for (HKRankListBean bean : beanList) {
            OptionalStockBean stockBean = new OptionalStockBean();
            stockBean.setName(bean.getName());
            stockBean.setCode(bean.getCode());
            stockBean.setPrice(bean.getPrice() + "");
            if (null != bean.getChange()) {
                stockBean.setChangePct(Float.valueOf(bean.getChange()));
            }
            stockBean.setmStockType(shOrHk);
            stockBean.setMoney(bean.getMoney());
            dataList.add(stockBean);
        }
        return dataList;
    }

}
