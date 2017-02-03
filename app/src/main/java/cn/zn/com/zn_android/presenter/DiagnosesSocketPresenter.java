package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.FinishAnswerModel;
import cn.zn.com.zn_android.model.PreAnswerModel;
import cn.zn.com.zn_android.model.bean.MessageBean;
import cn.zn.com.zn_android.model.bean.PostAnswerBean;
import cn.zn.com.zn_android.model.bean.QuestionBean;
import cn.zn.com.zn_android.model.bean.QuestionDetailBean;
import cn.zn.com.zn_android.model.bean.RushAnswerBean;
import cn.zn.com.zn_android.model.bean.WaitAnswerBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.presenter.requestType.DiagnosedType;
import cn.zn.com.zn_android.uiclass.customerview.CountDown.CountDownTask;
import cn.zn.com.zn_android.uiclass.listener.CountDownListener;
import cn.zn.com.zn_android.utils.DateUtils;
import cn.zn.com.zn_android.utils.ToastUtil;
import cn.zn.com.zn_android.viewfeatures.DiagnosedStockView;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zjs on 2016/11/28 0028.
 * email: m15267280642@163.com
 * explain: 老师诊股中心的persenter
 */

public class DiagnosesSocketPresenter extends BasePresenter<DiagnosedStockView> {
    //当前的activity
    private Activity mContext;
    private ApiManager _apiManager = ApiManager.getInstance();
    private RnApplication _mApplication = RnApplication.getInstance();
    private Subscription timerSubscription;

    public DiagnosesSocketPresenter(Activity context, DiagnosedStockView mDSView) {
        this.mContext = context;
        attach(mDSView);
    }

    /**
     * 查询老师回答问题列表(抢答部分)
     *
     * @param type  1抢答整股  2待回答  3已回答
     * @param stype 1自己抢答  2学生主动  3精彩回答
     * @param page  页码
     * @param num   每页显示的数量
     */
    public void queryTeaAnswerList(int type, int stype, int page, int num) {
        String[] answerType = {"preAnswer", "waitAnswer"};
        _apiManager.getService().queryTeacherQuestionList(_mApplication.getUserInfo().getSessionID(), type, stype, page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnListValue<QuestionBean>, Observable<List<PreAnswerModel>>>() {
                    @Override
                    public Observable<List<PreAnswerModel>> call(ReturnListValue<QuestionBean> returnListValue) {
                        if (null == returnListValue || null == returnListValue.getData()) {
                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
                        } else {
                            if (returnListValue.getCode().equals("4000")) {
                                return Observable.error(new Throwable(returnListValue.getMsg()));
                            } else {
                                List<PreAnswerModel> returnValue = new ArrayList<>();
                                for (QuestionBean bean : returnListValue.getData()) {
                                    returnValue.add(new PreAnswerModel(mContext, DiagnosesSocketPresenter.this).setBean(bean).setType(answerType[type - 1]));
                                }
                                return Observable.just(returnValue);
                            }
                        }
                    }
                })
                .subscribe(returnListValue -> {
                    if (null == returnListValue)
                        mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, new Throwable("得到一个null的对象"));
                    else {
                        mView.onSuccess(DiagnosedType.TEA_ANSWER_QUESTION, returnListValue);
                    }
                }, throwable -> {
                    mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().queryTeacherQuestionList(_mApplication.getUserInfo().getSessionID(), type, stype, page, num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnListValue<QuestionBean>, Observable<List<PreAnswerModel>>>() {
//                    @Override
//                    public Observable<List<PreAnswerModel>> call(ReturnListValue<QuestionBean> returnListValue) {
//                        if (null == returnListValue || null == returnListValue.getData()) {
//                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                        } else {
//                            if (returnListValue.getCode().equals("4000")) {
//                                return Observable.error(new Throwable(returnListValue.getMsg()));
//                            } else {
//                                List<PreAnswerModel> returnValue = new ArrayList<>();
//                                for (QuestionBean bean : returnListValue.getData()) {
//                                    returnValue.add(new PreAnswerModel(mContext, DiagnosesSocketPresenter.this).setBean(bean).setType(answerType[type - 1]));
//                                }
//                                return Observable.just(returnValue);
//                            }
//                        }
//                    }
//                })
//                .subscribe(returnListValue -> {
//                    if (null == returnListValue)
//                        mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, new Throwable("得到一个null的对象"));
//                    else {
//                        mView.onSuccess(DiagnosedType.TEA_ANSWER_QUESTION, returnListValue);
//                    }
//                }, throwable -> {
//                    mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, throwable);
//                });
    }


    /**
     * 查询老师回答问题列表(待回答部分)
     *
     * @param type  1抢答整股  2待回答  3已回答
     * @param stype 1自己抢答  2学生主动  3精彩回答
     * @param page  页码
     * @param num   每页显示的数量
     */
    public void queryWaitAnswerList(int type, int stype, int page, int num) {
        String[] answerType = {"preAnswer", "waitAnswer"};
        _apiManager.getService().queryTeacherQuestionList(_mApplication.getUserInfo().getSessionID(), type, stype, page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnListValue<QuestionBean>, Observable<List<WaitAnswerBean>>>() {
                    @Override
                    public Observable<List<WaitAnswerBean>> call(ReturnListValue<QuestionBean> returnListValue) {
                        if (null == returnListValue || null == returnListValue.getData()) {
                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
                        } else {
                            if (returnListValue.getCode().equals("4000")) {
                                return Observable.error(new Throwable(returnListValue.getMsg()));
                            } else {
                                List<WaitAnswerBean> returnValue = new ArrayList<>();
                                for (QuestionBean bean : returnListValue.getData()) {
                                    WaitAnswerBean answerBean = new WaitAnswerBean();
                                    answerBean.setQuestionBean(bean);
                                    try {
                                        answerBean.millis = CountDownTask.elapsedRealtime() + Long.valueOf(bean.getTime()) * 1000;
                                    } catch (Exception e) {
                                        Log.e(TAG, "call: " + "服务器传回的数据不是Long类型");
                                        answerBean.millis = 0;
                                    }
                                    answerBean.countDownInterval = 1000;
                                    returnValue.add(answerBean);
                                }
                                return Observable.just(returnValue);
                            }
                        }
                    }
                })
                .subscribe(returnListValue -> {
                    if (null == returnListValue)
                        mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, new Throwable("得到一个null的对象"));
                    else {
                        mView.onSuccess(DiagnosedType.TEA_ANSWER_QUESTION, returnListValue);
                    }
                }, throwable -> {
                    mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().queryTeacherQuestionList(_mApplication.getUserInfo().getSessionID(), type, stype, page, num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnListValue<QuestionBean>, Observable<List<WaitAnswerBean>>>() {
//                    @Override
//                    public Observable<List<WaitAnswerBean>> call(ReturnListValue<QuestionBean> returnListValue) {
//                        if (null == returnListValue || null == returnListValue.getData()) {
//                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                        } else {
//                            if (returnListValue.getCode().equals("4000")) {
//                                return Observable.error(new Throwable(returnListValue.getMsg()));
//                            } else {
//                                List<WaitAnswerBean> returnValue = new ArrayList<>();
//                                for (QuestionBean bean : returnListValue.getData()) {
//                                    WaitAnswerBean answerBean = new WaitAnswerBean();
//                                    answerBean.setQuestionBean(bean);
//                                    try {
//                                        answerBean.millis = CountDownTask.elapsedRealtime() + Long.valueOf(bean.getTime()) * 1000;
//                                    } catch (Exception e) {
//                                        Log.e(TAG, "call: " + "服务器传回的数据不是Long类型");
//                                        answerBean.millis = 0;
//                                    }
//                                    answerBean.countDownInterval = 1000;
//                                    returnValue.add(answerBean);
//                                }
//                                return Observable.just(returnValue);
//                            }
//                        }
//                    }
//                })
//                .subscribe(returnListValue -> {
//                    if (null == returnListValue)
//                        mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, new Throwable("得到一个null的对象"));
//                    else {
//                        mView.onSuccess(DiagnosedType.TEA_ANSWER_QUESTION, returnListValue);
//                    }
//                }, throwable -> {
//                    mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, throwable);
//                });
    }


    /**
     * 查询老师回答问题列表(只包含已回答部分)
     *
     * @param type  1抢答整股  2待回答  3已回答
     * @param stype 1自己抢答  2学生主动  3精彩回答
     * @param page  页码
     * @param num   每页显示的数量
     */
    public void queryFinishTeaAnswerList(int type, int stype, int page, int num, FinishAnswerModel.OnPerfectListener listener) {
        _apiManager.getService().queryTeacherQuestionList(_mApplication.getUserInfo().getSessionID(), type, stype, page, num)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnListValue<QuestionBean>, Observable<List<FinishAnswerModel>>>() {
                    @Override
                    public Observable<List<FinishAnswerModel>> call(ReturnListValue<QuestionBean> returnListValue) {
                        if (null == returnListValue || null == returnListValue.getData()) {
                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
                        } else {
                            if (returnListValue.getCode().equals("4000")) {
                                return Observable.error(new Throwable(returnListValue.getMsg()));
                            } else {
                                List<FinishAnswerModel> returnValue = new ArrayList<>();
                                for (QuestionBean bean : returnListValue.getData()) {
                                    returnValue.add(new FinishAnswerModel().setBean(bean).setType(stype - 1).setOnPerfectListener(listener));
                                }
                                return Observable.just(returnValue);
                            }
                        }
                    }
                })
                .subscribe(returnListValue -> {
                    if (null == returnListValue)
                        mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, new Throwable("得到一个null的对象"));
                    else {
                        mView.onSuccess(DiagnosedType.TEA_ANSWER_QUESTION, returnListValue);
                    }
                }, throwable -> {
                    mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().queryTeacherQuestionList(_mApplication.getUserInfo().getSessionID(), type, stype, page, num))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnListValue<QuestionBean>, Observable<List<FinishAnswerModel>>>() {
//                    @Override
//                    public Observable<List<FinishAnswerModel>> call(ReturnListValue<QuestionBean> returnListValue) {
//                        if (null == returnListValue || null == returnListValue.getData()) {
//                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                        } else {
//                            if (returnListValue.getCode().equals("4000")) {
//                                return Observable.error(new Throwable(returnListValue.getMsg()));
//                            } else {
//                                List<FinishAnswerModel> returnValue = new ArrayList<>();
//                                for (QuestionBean bean : returnListValue.getData()) {
//                                    returnValue.add(new FinishAnswerModel().setBean(bean).setType(stype - 1).setOnPerfectListener(listener));
//                                }
//                                return Observable.just(returnValue);
//                            }
//                        }
//                    }
//                })
//                .subscribe(returnListValue -> {
//                    if (null == returnListValue)
//                        mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, new Throwable("得到一个null的对象"));
//                    else {
//                        mView.onSuccess(DiagnosedType.TEA_ANSWER_QUESTION, returnListValue);
//                    }
//                }, throwable -> {
//                    mView.onError(DiagnosedType.TEA_ANSWER_QUESTION, throwable);
//                });
    }

    /**
     * 抢答问题
     *
     * @param id
     * @param view
     */
    public void rushAnswer(String id, DiagnosedStockView view) {
        _apiManager.getService().rushAnswer(_mApplication.getUserInfo().getSessionID(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnValue<RushAnswerBean>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ReturnValue<RushAnswerBean> returnValue) {
                        if (null == returnValue) {
                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
                        } else {
                            if (returnValue.getCode().equals("4000")) {
                                return Observable.error(new Throwable(returnValue.getData().getMessage()));
                            } else {
                                String str = returnValue.getData().getType();
                                return Observable.just(str);
                            }
                        }
                    }
                })
                .subscribe(ReturnValue -> {
                    if ("1".equals(ReturnValue)) view.onSuccess(DiagnosedType.RESPONDER,"1");
                }, throwable -> {
                    Log.e(TAG, "rushAnswer: " + throwable.getMessage());
                    ToastUtil.show(mContext, "抢答失败，" + throwable.getMessage(), Toast.LENGTH_SHORT);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().rushAnswer(_mApplication.getUserInfo().getSessionID(), id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnValue<RushAnswerBean>, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(ReturnValue<RushAnswerBean> returnValue) {
//                        if (null == returnValue) {
//                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                        } else {
//                            if (returnValue.getCode().equals("4000")) {
//                                return Observable.error(new Throwable(returnValue.getData().getMessage()));
//                            } else {
//                                String str = returnValue.getData().getType();
//                                return Observable.just(str);
//                            }
//                        }
//                    }
//                })
//                .subscribe(ReturnValue -> {
//                    if ("1".equals(ReturnValue)) view.onSuccess(DiagnosedType.RESPONDER,"1");
//                }, throwable -> {
//                    Log.e(TAG, "rushAnswer: " + throwable.getMessage());
//                    ToastUtil.show(mContext, "抢答失败，" + throwable.getMessage(), Toast.LENGTH_SHORT);
//                });

    }





    /**
     * 查询问详情
     *
     * @param id 问题ID
     */
    public void queryAskInfo(String id) {
        _apiManager.getService().queryAskInfo(_mApplication.getUserInfo().getSessionID(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnListValue<QuestionDetailBean>, Observable<QuestionDetailBean>>() {
                    @Override
                    public Observable<QuestionDetailBean> call(ReturnListValue<QuestionDetailBean> returnListValue) {
                        if (null == returnListValue || null == returnListValue.getData()) {
                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
                        } else {
                            if (returnListValue.getCode().equals("4000")) {
                                return Observable.error(new Throwable(returnListValue.getMsg()));
                            } else {
                                return Observable.just(returnListValue.getData().get(0));
                            }
                        }
                    }
                })
                .subscribe(returnListValue -> {
                    if (null == returnListValue)
                        mView.onError(DiagnosedType.QUESTION_DETAIL, new Throwable("得到一个null的对象"));
                    else {
                        mView.onSuccess(DiagnosedType.QUESTION_DETAIL, returnListValue);
                    }
                }, throwable -> {
                    mView.onError(DiagnosedType.QUESTION_DETAIL, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().queryAskInfo(_mApplication.getUserInfo().getSessionID(), id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnListValue<QuestionDetailBean>, Observable<QuestionDetailBean>>() {
//                    @Override
//                    public Observable<QuestionDetailBean> call(ReturnListValue<QuestionDetailBean> returnListValue) {
//                        if (null == returnListValue || null == returnListValue.getData()) {
//                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                        } else {
//                            if (returnListValue.getCode().equals("4000")) {
//                                return Observable.error(new Throwable(returnListValue.getMsg()));
//                            } else {
//                                return Observable.just(returnListValue.getData().get(0));
//                            }
//                        }
//                    }
//                })
//                .subscribe(returnListValue -> {
//                    if (null == returnListValue)
//                        mView.onError(DiagnosedType.QUESTION_DETAIL, new Throwable("得到一个null的对象"));
//                    else {
//                        mView.onSuccess(DiagnosedType.QUESTION_DETAIL, returnListValue);
//                    }
//                }, throwable -> {
//                    mView.onError(DiagnosedType.QUESTION_DETAIL, throwable);
//                });
    }


    /**
     * 老师回答页面的问题详情
     *
     * @param id
     */
    public void queryTeacherAnswer(String id) {
        _apiManager.getService().queryTeacherAnswer(_mApplication.getUserInfo().getSessionID(), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnValue<QuestionDetailBean>, Observable<QuestionDetailBean>>() {
                    @Override
                    public Observable<QuestionDetailBean> call(ReturnValue<QuestionDetailBean> returnValue) {
                        if (null == returnValue) {
                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
                        } else {
                            if (returnValue.getCode().equals("4000")) {
                                return Observable.error(new Throwable(returnValue.getData().getMessage()));
                            } else {
                                QuestionDetailBean bean = returnValue.getData();
                                return Observable.just(bean);
                            }
                        }
                    }
                })
                .subscribe(returnView -> {
                    mView.onSuccess(DiagnosedType.ANSWER_DETAIL, returnView);
                }, throwable -> {
                    Log.e(TAG, "rushAnswer: " + throwable.getMessage());
                    ToastUtil.show(mContext, "请求失败，" + throwable.getMessage(), Toast.LENGTH_SHORT);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().queryTeacherAnswer(_mApplication.getUserInfo().getSessionID(), id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnValue<QuestionDetailBean>, Observable<QuestionDetailBean>>() {
//                    @Override
//                    public Observable<QuestionDetailBean> call(ReturnValue<QuestionDetailBean> returnValue) {
//                        if (null == returnValue) {
//                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                        } else {
//                            if (returnValue.getCode().equals("4000")) {
//                                return Observable.error(new Throwable(returnValue.getData().getMessage()));
//                            } else {
//                                QuestionDetailBean bean = returnValue.getData();
//                                return Observable.just(bean);
//                            }
//                        }
//                    }
//                })
//                .subscribe(returnView -> {
//                    mView.onSuccess(DiagnosedType.ANSWER_DETAIL, returnView);
//                }, throwable -> {
//                    Log.e(TAG, "rushAnswer: " + throwable.getMessage());
//                    ToastUtil.show(mContext, "请求失败，" + throwable.getMessage(), Toast.LENGTH_SHORT);
//                });

    }


    /**
     * 查询七牛云的token
     */
    public void getQNToken() {
        _apiManager.getService().getUpLoadToken("")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
                        if (null == returnValue)
                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
                        else {
                            if (returnValue.getCode().equals("4000"))
                                return Observable.error(new Throwable(returnValue.getData().getMessage()));
                            else {
                                String token = returnValue.getData().getMessage();
                                return Observable.just(token);
                            }
                        }
                    }
                })
                .subscribe(returnValue -> {
                    mView.onSuccess(DiagnosedType.GET_TOKEN, returnValue);
                }, throwable -> {
                    mView.onError(DiagnosedType.GET_TOKEN, throwable);
                    Log.e(TAG, "getQNToken: 异常");
                });
//        AppObservable.bindActivity(mContext, _apiManager.getService().getUpLoadToken(""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
//                        if (null == returnValue)
//                            return Observable.error(new Throwable("服务器错误，返回了NULL"));
//                        else {
//                            if (returnValue.getCode().equals("4000"))
//                                return Observable.error(new Throwable(returnValue.getData().getMessage()));
//                            else {
//                                String token = returnValue.getData().getMessage();
//                                return Observable.just(token);
//                            }
//                        }
//                    }
//                })
//                .subscribe(returnValue -> {
//                    mView.onSuccess(DiagnosedType.GET_TOKEN, returnValue);
//                }, throwable -> {
//                    mView.onError(DiagnosedType.GET_TOKEN, throwable);
//                    Log.e(TAG, "getQNToken: 异常");
//                });
    }


    /**
     * 上传图片
     *
     * @param pathName
     * @param token
     */

    public void UploadImg(String pathName, String token, String headIconName) {
        List<String> datas = new ArrayList<>();
        UploadManager uploadManager = new UploadManager();
        uploadManager.put(pathName, headIconName, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, JSONObject response) {
                        Log.i("qiniu", info.toString());
                        datas.add(headIconName);
                        datas.add(info.toString());
                        mView.onSuccess(DiagnosedType.UPLOAD_IMG, datas);
                    }
                }, null);

    }

    /**
     * 提交问题答案
     *
     * @param postAnswerBean
     */

    public void commitTeacherAnswer(PostAnswerBean postAnswerBean) {
        _apiManager.getService().commitTeacherAnswer(_mApplication.getUserInfo().getSessionID(),
                postAnswerBean.getStrend(),
                postAnswerBean.getMtrend(),
                postAnswerBean.getTranalysis(),
                postAnswerBean.getFanalysis(),
                postAnswerBean.getSupplement(),
                postAnswerBean.getImg1(), postAnswerBean.getImg2(), postAnswerBean.getImg3(),
                postAnswerBean.getId(),
                postAnswerBean.getFrom())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> {
                    if (null != returnValue && null != returnValue.getData()) {
                        mView.onSuccess(DiagnosedType.COMMIT_TEACHER_ANSWER, returnValue.getData().getMessage());
                    }
                }, throwable -> {
                    mView.onError(DiagnosedType.COMMIT_TEACHER_ANSWER, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().commitTeacherAnswer(_mApplication.getUserInfo().getSessionID(),
//                postAnswerBean.getStrend(),
//                postAnswerBean.getMtrend(),
//                postAnswerBean.getTranalysis(),
//                postAnswerBean.getFanalysis(),
//                postAnswerBean.getSupplement(),
//                postAnswerBean.getImg1(), postAnswerBean.getImg2(), postAnswerBean.getImg3(),
//                postAnswerBean.getId(),
//                postAnswerBean.getFrom()))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(returnValue -> {
//                    if (null != returnValue && null != returnValue.getData()) {
//                        mView.onSuccess(DiagnosedType.COMMIT_TEACHER_ANSWER, returnValue.getData().getMessage());
//                    }
//                }, throwable -> {
//                    mView.onError(DiagnosedType.COMMIT_TEACHER_ANSWER, throwable);
//                })
//        ;

    }

    /**
     * 添加精彩
     *
     * @param id 问题ID
     */

    public void addGoodAnswer(String id) {
        _apiManager.getService().addGoodAnswer(_mApplication.getUserInfo().getSessionID(), id)
                .subscribeOn(Schedulers.io())           //在io线程订阅
                .observeOn(AndroidSchedulers.mainThread())  //观察者在主线程
                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
                        if (returnValue.getCode().equals("4000"))
                            return Observable.error(new Throwable(returnValue.getData().getMessage()));
                        else {
                            return Observable.just(returnValue.getData().getMessage());
                        }
                    }
                })
                .subscribe(returnValue -> {
                    mView.onSuccess(DiagnosedType.ADD_GOOD_ANSWER, returnValue);
                }, throwable -> {
                    mView.onError(DiagnosedType.ADD_GOOD_ANSWER, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().addGoodAnswer(_mApplication.getUserInfo().getSessionID(), id))
//                .subscribeOn(Schedulers.io())           //在io线程订阅
//                .observeOn(AndroidSchedulers.mainThread())  //观察者在主线程
//                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
//                        if (returnValue.getCode().equals("4000"))
//                            return Observable.error(new Throwable(returnValue.getData().getMessage()));
//                        else {
//                            return Observable.just(returnValue.getData().getMessage());
//                        }
//                    }
//                })
//                .subscribe(returnValue -> {
//                    mView.onSuccess(DiagnosedType.ADD_GOOD_ANSWER, returnValue);
//                }, throwable -> {
//                    mView.onError(DiagnosedType.ADD_GOOD_ANSWER, throwable);
//                });
    }

    /**
     * 取消精彩
     */
    public void cancelGoodAnswer(String id) {
        _apiManager.getService().delGoodAnswer(_mApplication.getUserInfo().getSessionID(), id)
                .subscribeOn(Schedulers.io())           //在io线程订阅
                .observeOn(AndroidSchedulers.mainThread())  //观察者在主线程
                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
                        if (returnValue.getCode().equals("4000"))
                            return Observable.error(new Throwable(returnValue.getData().getMessage()));
                        else {
                            return Observable.just(returnValue.getData().getMessage());
                        }
                    }
                })
                .subscribe(returnValue -> {
                    mView.onSuccess(DiagnosedType.CANCEL_GOOD_ANSWER, returnValue);
                }, throwable -> {
                    mView.onError(DiagnosedType.CANCEL_GOOD_ANSWER, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().delGoodAnswer(_mApplication.getUserInfo().getSessionID(), id))
//                .subscribeOn(Schedulers.io())           //在io线程订阅
//                .observeOn(AndroidSchedulers.mainThread())  //观察者在主线程
//                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
//                        if (returnValue.getCode().equals("4000"))
//                            return Observable.error(new Throwable(returnValue.getData().getMessage()));
//                        else {
//                            return Observable.just(returnValue.getData().getMessage());
//                        }
//                    }
//                })
//                .subscribe(returnValue -> {
//                    mView.onSuccess(DiagnosedType.CANCEL_GOOD_ANSWER, returnValue);
//                }, throwable -> {
//                    mView.onError(DiagnosedType.CANCEL_GOOD_ANSWER, throwable);
//                });
    }

    /**
     * 删除问题
     */
    public void delAnswer(String id) {
        _apiManager.getService().delAnswer(_mApplication.getUserInfo().getSessionID(), id)
                .subscribeOn(Schedulers.io())           //在io线程订阅
                .observeOn(AndroidSchedulers.mainThread())  //观察者在主线程
                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
                    @Override
                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
                        if (returnValue.getCode().equals("4000"))
                            return Observable.error(new Throwable(returnValue.getData().getMessage()));
                        else {
                            return Observable.just(returnValue.getData().getMessage());
                        }
                    }
                })
                .subscribe(returnValue -> {
                    mView.onSuccess(DiagnosedType.DEL_ANSWER, returnValue);
                }, throwable -> {
                    mView.onError(DiagnosedType.DEL_ANSWER, throwable);
                });

//        AppObservable.bindActivity(mContext, _apiManager.getService().delAnswer(_mApplication.getUserInfo().getSessionID(), id))
//                .subscribeOn(Schedulers.io())           //在io线程订阅
//                .observeOn(AndroidSchedulers.mainThread())  //观察者在主线程
//                .flatMap(new Func1<ReturnValue<MessageBean>, Observable<String>>() {
//                    @Override
//                    public Observable<String> call(ReturnValue<MessageBean> returnValue) {
//                        if (returnValue.getCode().equals("4000"))
//                            return Observable.error(new Throwable(returnValue.getData().getMessage()));
//                        else {
//                            return Observable.just(returnValue.getData().getMessage());
//                        }
//                    }
//                })
//                .subscribe(returnValue -> {
//                    mView.onSuccess(DiagnosedType.DEL_ANSWER, returnValue);
//                }, throwable -> {
//                    mView.onError(DiagnosedType.DEL_ANSWER, throwable);
//                });
    }


    /**
     * 小时/分/秒 倒计时
     */

    public void startCountDown(Long second, CountDownListener listener) {
        timerSubscription = Observable.timer(0, 1, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Func1<Long, Observable<Long[]>>() {
                    @Override
                    public Observable<Long[]> call(Long aLong) {
                        Long[] times = new Long[3];
                        if (aLong >= second) {
                            timerSubscription.unsubscribe();
                            times[0] = 0L;
                            times[1] = 0L;
                            times[2] = 0L;
                        } else {
                            times = DateUtils.second2HourStr1(second - aLong);
                        }

                        return Observable.just(times);
                    }
                })
                .subscribe(times -> {
                    listener.onStart(times);
                }, throwable -> {
                    listener.onFail(throwable);
                });
    }


    /**
     * 注销计时器
     */
    public void cancleCountDown() {
        if (timerSubscription != null && timerSubscription.isUnsubscribed())
            timerSubscription.unsubscribe();
    }

}
