package cn.zn.com.zn_android.presenter;

import android.app.Activity;
import android.util.Log;

import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.presenter.requestType.SimulativeBoardType;
import cn.zn.com.zn_android.viewfeatures.ImitateFryView;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * 模拟炒股
 * Created by Jolly on 2016/9/8 0008.
 */
public class ImitateFryPresenter extends BasePresenter<ImitateFryView> {
    private ImitateFryView imitateFryView;
    private Activity _activity;
    private ApiManager _apiManager;


    public ImitateFryPresenter(ImitateFryView imitateFryView, Activity _activity) {
        this.imitateFryView = imitateFryView;
        this._activity = _activity;
        this._apiManager = ApiManager.getInstance();
        this.msubscription = new CompositeSubscription();
    }

    /**
     * 模拟炒股用户资产页面中持股列表
     *
     * @param page    从0开始
     * @return
     */
    public void queryUserPosition(String sessionId, String page) {
        Subscription sub = _apiManager.getService().queryUserPosition(sessionId, page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    imitateFryView.onSuccess(SimulativeBoardType.USER_POSITION, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryUserPosition: " + throwable);
                    imitateFryView.onError(SimulativeBoardType.USER_POSITION, throwable);
                });
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryUserPosition(sessionId, page))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    imitateFryView.onSuccess(SimulativeBoardType.USER_POSITION, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryUserPosition: " + throwable);
//                    imitateFryView.onError(SimulativeBoardType.USER_POSITION, throwable);
//                });

    }

    public void queryImitateFry(String sessionId) {
        Subscription sub = _apiManager.getService().queryImitateFry(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    imitateFryView.onSuccess(SimulativeBoardType.QUERY_IMITATE_FRY, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryImitateFry: " + throwable);
                    imitateFryView.onError(SimulativeBoardType.QUERY_IMITATE_FRY, throwable);
                });
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryImitateFry(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    imitateFryView.onSuccess(SimulativeBoardType.QUERY_IMITATE_FRY, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryImitateFry: " + throwable);
//                    imitateFryView.onError(SimulativeBoardType.QUERY_IMITATE_FRY, throwable);
//                });
    }

    public void queryEntrusList(String sessionId) {
        Subscription sub = _apiManager.getService().queryEntrusList(sessionId, "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    imitateFryView.onSuccess(SimulativeBoardType.QUERY_ENTRUST_LIST, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "queryEntrusList: " + throwable);
                    imitateFryView.onError(SimulativeBoardType.QUERY_ENTRUST_LIST, throwable);
                });
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().queryEntrusList(sessionId, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    imitateFryView.onSuccess(SimulativeBoardType.QUERY_ENTRUST_LIST, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "queryEntrusList: " + throwable);
//                    imitateFryView.onError(SimulativeBoardType.QUERY_ENTRUST_LIST, throwable);
//                });
    }

    public void removeEnstuct(String sessionId, String id) {
        Subscription sub = _apiManager.getService().removeEnstuct(sessionId, id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(retValue -> {
                    imitateFryView.onSuccess(SimulativeBoardType.REMOVE_ENTRUST, retValue.getData());
                }, throwable -> {
                    Log.e(TAG, "removeEnstuct: " + throwable);
                    imitateFryView.onError(SimulativeBoardType.REMOVE_ENTRUST, throwable);
                });
        msubscription.add(sub);

//        AppObservable.bindActivity(_activity, _apiManager.getService().removeEnstuct(sessionId, id))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(retValue -> {
//                    imitateFryView.onSuccess(SimulativeBoardType.REMOVE_ENTRUST, retValue.getData());
//                }, throwable -> {
//                    Log.e(TAG, "removeEnstuct: " + throwable);
//                    imitateFryView.onError(SimulativeBoardType.REMOVE_ENTRUST, throwable);
//                });
    }

}
