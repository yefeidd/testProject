package cn.zn.com.zn_android.model.chartBean;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.presenter.ChartParse;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/9/6 0006.
 * email: m15267280642@163.com
 * explain:
 */
public class MinutesModel implements ChartDataMole {
    private ApiManager apiManager;
    private String tic_code;
    private Activity mActivity;
    private ChartParse chartParse;
    private List<MinutesBean> datas = new ArrayList<>();


    public MinutesModel(Activity activity, ChartParse chartParse, String ticCode) {
        this.mActivity = activity;
        this.chartParse = chartParse;
        this.tic_code = ticCode;
        this.apiManager = ApiManager.getInstance();
    }


    @Override
    public void requestData() {
        if (tic_code.length() == 6) {
            apiManager.getService().queryHsMinQuota(tic_code)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultHsMinMsg, Throwable -> {
                        Throwable.printStackTrace();
                        chartParse.calcMinData(datas);
                    });

//            AppObservable.bindActivity(mActivity, apiManager.getService().queryHsMinQuota(tic_code))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::resultHsMinMsg, Throwable -> {
//                        Throwable.printStackTrace();
//                        chartParse.calcMinData(datas);
//                    });
        } else {
            String zs_type = "1";
            switch (tic_code) {
                case "SZ":
                    zs_type = "1";
                    break;
                case "SZCZ":
                    zs_type = "2";
                    break;
                case "CY":
                    zs_type = "3";
                    break;
            }
            apiManager.getService().queryZSMinQuota(zs_type)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultHsMinMsg, Throwable -> {
                        Throwable.printStackTrace();
                        chartParse.calcMinData(datas);
                    });

//            AppObservable.bindActivity(mActivity, apiManager.getService().queryZSMinQuota(zs_type))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::resultHsMinMsg, Throwable -> {
//                        Throwable.printStackTrace();
//                        chartParse.calcMinData(datas);
//                    });
        }
    }

    private void resultHsMinMsg(ReturnListValue<MinutesBean> returnListValue) {
        if (returnListValue != null && returnListValue.getMsg().equals(Constants.SUCCESS)) {
            if (datas != null) datas.clear();
            datas = returnListValue.getData();
            chartParse.calcMinData(datas);
        } else {
            if (null == returnListValue) {
                ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.no_net), Toast.LENGTH_SHORT);
            } else {
                ToastUtil.show(mActivity, returnListValue.getMsg(), Toast.LENGTH_SHORT);
            }
            chartParse.calcMinData(datas);

        }
    }

    @Override
    public ArrayList<MessageBean> getData() {
        return null;
    }

    public void setParameter(String tic_code) {
        this.tic_code = tic_code;
    }
}
