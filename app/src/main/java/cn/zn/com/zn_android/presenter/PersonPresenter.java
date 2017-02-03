package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.app.Fragment;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.uiclass.fragment.PersonFragment;
import cn.zn.com.zn_android.viewfeatures.PersonView;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Jolly on 2016/6/27 0027.
 */
public class PersonPresenter extends BasePresenter<PersonView> {
    private static final String TAG = "PersonPresenter";
    private PersonView mPersonView;
    private ApiManager mApiManager;
    private Activity mActivity;
    private RnApplication _mApplication = RnApplication.getInstance();

    public PersonPresenter(PersonView personView, PersonFragment mfragment) {
        this.mPersonView = personView;
        this.mApiManager = ApiManager.getInstance();
        this.msubscription = new CompositeSubscription();
        this.mActivity = mfragment.getActivity();
    }

    public void queryMemberInfo() {

        String sessionID = _mApplication.getUserInfo().getSessionID();
        Subscription sub = mApiManager.getService().queryMemberInfo(sessionID, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(returnValue -> mPersonView.queryUserInfoResult(returnValue), throwable -> {
                    Log.i(TAG, "queryMemberInfo: 异常");
                });
        msubscription.add(sub);

//        mApiManager.getService().queryMemberInfo(sessionID, "", new Callback<ReturnValue<UserInfoBean>>() {
//            @Override
//            public void success(ReturnValue<UserInfoBean> returnValue, Response response) {
//                mPersonView.queryUserInfoResult(returnValue);
//            }
//
//            @Override
//            public void failure(RetrofitError error) {
//                Log.i(TAG, error.toString());
//                NetUtil.errorTip(error.getKind());
//            }
//        });
    }

    public void virReg(String sessionId, int flag) {
        Subscription sub = mApiManager.getService().virReg(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mPersonView.virReg(retValue, flag);
                }, throwable -> {
                    Log.e(TAG, "queryUserSign: ", throwable);
                });
        msubscription.add(sub);

//        AppObservable.bindFragment(mfragment, mApiManager.getService().virReg(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    mPersonView.virReg(retValue, flag);
//                }, throwable -> {
//                    Log.e(TAG, "queryUserSign: ", throwable);
//                });
    }
}
