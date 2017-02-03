package cn.zn.com.zn_android.model.chartBean;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.HsDayKLineBean;
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
public class HsDayKModel implements ChartDataMole {
    private ApiManager apiManager;
    private String tic_code;
    private String start_time;
    private String end_time;
    private Activity mActivity;
    private ChartParse chartParse;
    private List<HsDayKLineBean> kDatas = new ArrayList<>();


    public HsDayKModel(Activity activity, ChartParse chartParse, String ticCode, String startTime, String endTime) {
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
            apiManager.getService().queryHsDayQuota(tic_code, start_time, end_time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultHsDayKMsg, Throwable -> {
                        Throwable.printStackTrace();
                        chartParse.calcDayKLineData(kDatas);
                    });

//            AppObservable.bindActivity(mActivity, apiManager.getService().queryHsDayQuota(tic_code, start_time, end_time))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::resultHsDayKMsg, Throwable -> {
//                        Throwable.printStackTrace();
//                        chartParse.calcDayKLineData(kDatas);
//                    });
        } else if (5 == tic_code.length()) {
            apiManager.getService().queryGGDayQuota(tic_code, start_time, end_time)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this::resultHsDayKMsg, Throwable -> {
                        Throwable.printStackTrace();
                        chartParse.calcDayKLineData(kDatas);
                    });

//            AppObservable.bindActivity(mActivity, apiManager.getService().queryGGDayQuota(tic_code, start_time, end_time))
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(this::resultHsDayKMsg, Throwable -> {
//                        Throwable.printStackTrace();
//                        chartParse.calcDayKLineData(kDatas);
//                    });
        } else {
            switch (tic_code) {
                case "SZ":
                    apiManager.getService().querySZDayQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsDayKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcDayKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().querySZDayQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsDayKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcDayKLineData(kDatas);
//                            });
                    break;
                case "SZCZ":
                    apiManager.getService().querySZCZDayQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsDayKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcDayKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().querySZCZDayQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsDayKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcDayKLineData(kDatas);
//                            });
                    break;
                case "CY":
                    apiManager.getService().queryCYDayQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsDayKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcDayKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().queryCYDayQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsDayKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcDayKLineData(kDatas);
//                            });
                    break;
                case "HS":
                    apiManager.getService().queryHSDayQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsDayKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcDayKLineData(kDatas);
                            });

//                        AppObservable.bindActivity(mActivity, apiManager.getService().queryHSDayQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsDayKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcDayKLineData(kDatas);
//                            });
                    break;
                case "GQ":
                    apiManager.getService().queryGQDayQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsDayKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcDayKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().queryGQDayQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsDayKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcDayKLineData(kDatas);
//                            });
                    break;
                case "HC":
                    apiManager.getService().queryHCDayQuota(start_time, end_time)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(this::resultHsDayKMsg, Throwable -> {
                                Throwable.printStackTrace();
                                chartParse.calcDayKLineData(kDatas);
                            });

//                    AppObservable.bindActivity(mActivity, apiManager.getService().queryHCDayQuota(start_time, end_time))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(this::resultHsDayKMsg, Throwable -> {
//                                Throwable.printStackTrace();
//                                chartParse.calcDayKLineData(kDatas);
//                            });
                    break;
            }
        }
    }


    private void resultHsDayKMsg(ReturnListValue<HsDayKLineBean> returnListValue) {
        if (returnListValue != null && returnListValue.getMsg().equals(Constants.SUCCESS)) {
            if (kDatas != null) kDatas.clear();
            kDatas = returnListValue.getData();
            chartParse.calcDayKLineData(kDatas);
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
