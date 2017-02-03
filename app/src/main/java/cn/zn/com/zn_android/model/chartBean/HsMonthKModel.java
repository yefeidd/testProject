package cn.zn.com.zn_android.model.chartBean;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.HsWeekKLineBean;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.presenter.ChartParse;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/8/24 0024.
 * email: m15267280642@163.com
 * explain:
 */
public class HsMonthKModel implements ChartDataMole {
    private ApiManager apiManager;
    private String tic_code;
    private String start_time;
    private String end_time;
    private Activity mActivity;
    private ChartParse chartParse;
    private List<HsWeekKLineBean> kDatas = new ArrayList<>();


    public HsMonthKModel(Activity activity, ChartParse chartParse, String ticCode, String startTime, String endTime) {
        this.mActivity = activity;
        this.chartParse = chartParse;
        this.tic_code = ticCode;
        this.start_time = startTime;
        this.end_time = endTime;
        this.apiManager = ApiManager.getInstance();
    }

    @Override
    public void requestData() {

        if (6 == tic_code.length()) {
            apiManager.getService().queryHsMonthQuota(tic_code, start_time, end_time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultHsMonthKMsg, Throwable -> {
                        Throwable.printStackTrace();
                        chartParse.calcWeekKLineData(kDatas);
                    });

//            AppObservable.bindActivity(mActivity, apiManager.getService().queryHsMonthQuota(tic_code, start_time, end_time))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                        Throwable.printStackTrace();
//                        chartParse.calcWeekKLineData(kDatas);
//                    });
        } else if (5 == tic_code.length()) {
            apiManager.getService().queryGGMonthQuota(tic_code, start_time, end_time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultHsMonthKMsg, Throwable -> {
                        Throwable.printStackTrace();
                        chartParse.calcWeekKLineData(kDatas);
                    });

//            AppObservable.bindActivity(mActivity, apiManager.getService().queryGGMonthQuota(tic_code, start_time, end_time))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                        Throwable.printStackTrace();
//                        chartParse.calcWeekKLineData(kDatas);
//                    });
        } else {
            switch (tic_code) {
                case "SZ":
                    apiManager.getService().querySZMonthQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcWeekKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().querySZMonthQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcWeekKLineData(kDatas);
//                            });
                    break;
                case "SZCZ":
                    apiManager.getService().querySZCZMonthQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcWeekKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().querySZCZMonthQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcWeekKLineData(kDatas);
//                            });
                    break;
                case "CY":
                    apiManager.getService().queryCYMonthQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcWeekKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().queryCYMonthQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcWeekKLineData(kDatas);
//                            });
                    break;
                case "HS":
                    apiManager.getService().queryHSMonthQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcWeekKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().queryHSMonthQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcWeekKLineData(kDatas);
//                            });
                    break;
                case "GQ":
                    apiManager.getService().queryGQMonthQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcWeekKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().queryGQMonthQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcWeekKLineData(kDatas);
//                            });
                    break;
                case "HC":
                    apiManager.getService().queryHCMonthQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcWeekKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().queryHCMonthQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsMonthKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcWeekKLineData(kDatas);
//                            });
                    break;
            }
        }


    }


    private void resultHsMonthKMsg(ReturnListValue<HsWeekKLineBean> returnListValue) {
        if (returnListValue != null && returnListValue.getMsg().equals(Constants.SUCCESS)) {
            if (kDatas != null) kDatas.clear();
            kDatas = returnListValue.getData();
            chartParse.calcWeekKLineData(kDatas);
        } else {
            if (null == returnListValue) {
                ToastUtil.show(mActivity, mActivity.getResources().getString(R.string.no_net), Toast.LENGTH_SHORT);
            } else {
                ToastUtil.show(mActivity, returnListValue.getMsg(), Toast.LENGTH_SHORT);
            }
        }
    }


    @Override
    public ArrayList<MessageBean> getData() {
        return null;
    }


    public void setParameter(String tic_code, String start_time, String end_time) {
        this.tic_code = tic_code;
        this.start_time = start_time;
        this.end_time = end_time;
    }


}
