package cn.zn.com.zn_android.presenter;

import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;
import cn.zn.com.zn_android.model.bean.UserInfoBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.fragment.PersonFragment;
import cn.zn.com.zn_android.utils.NetUtil;
import cn.zn.com.zn_android.viewfeatures.PersonView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.android.app.AppObservable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Jolly on 2016/6/27 0027.
 */
public class PersonPresenter extends BasePresenter<PersonView> {
    private static final String TAG = "PersonPresenter";
    private PersonView mPersonView;
    private ApiManager mApiManager;
    private PersonFragment mfragment;
    private RnApplication _mApplication = RnApplication.getInstance();

    public PersonPresenter(PersonView personView, PersonFragment fragment) {
        this.mPersonView = personView;
        this.mApiManager = ApiManager.getInstance();
        this.mfragment = fragment;
    }

    public void queryMemberInfo() {

        String sessionID = _mApplication.getUserInfo().getSessionID();

        mApiManager.getService().queryMemberInfo(sessionID, "", new Callback<ReturnValue<UserInfoBean>>() {
            @Override
            public void success(ReturnValue<UserInfoBean> returnValue, Response response) {
                mPersonView.queryUserInfoResult(returnValue);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i(TAG, error.toString());
                NetUtil.errorTip(error.getKind());
            }
        });
    }

    public void virReg(String sessionId, int flag) {
        AppObservable.bindFragment(mfragment, mApiManager.getService().virReg(sessionId, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    mPersonView.virReg(retValue, flag);
                }, throwable -> {
                    Log.e(TAG, "queryUserSign: ", throwable);
                });
    }
}
