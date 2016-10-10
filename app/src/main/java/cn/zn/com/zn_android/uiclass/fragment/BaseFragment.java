package cn.zn.com.zn_android.uiclass.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cn.zn.com.zn_android.helper.SpfHelper;
import cn.zn.com.zn_android.manage.ApiManager;
import cn.zn.com.zn_android.manage.RnApplication;

import butterknife.ButterKnife;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2016/3/9 0009.
 */
public abstract class BaseFragment extends Fragment {
    protected final String TAG = this.getClass().getSimpleName();

    protected Context _mContext;
    protected Activity _mActivity;
    protected RnApplication _mApplication = RnApplication.getInstance();
    protected SpfHelper _spfHelper;
    protected ApiManager _apiManager;
    protected CompositeSubscription _mSubscriptions = new CompositeSubscription();
    protected LayoutInflater inflater;
    protected ViewGroup container;
    protected View currentView;

    protected int layoutRes;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _mContext = context;
        _apiManager = ApiManager.getInstance();
        _spfHelper = _mApplication.getSpfHelper();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /**
         * 避免oncreateView执行多次
         *
         */
        if (null != currentView) {
            ViewGroup parent = (ViewGroup) currentView.getParent();
            if (null != parent) {
                parent.removeView(currentView);
            }
        } else {
        currentView = inflater.inflate(layoutRes, container, false);
        ButterKnife.bind(this, currentView);
        initView(currentView);
        initEvent();
        }

        this.inflater = inflater;
        this.container = container;
        return currentView;
    }

    protected abstract void initView(View view);

    protected abstract void initEvent();

    protected void _setLayoutRes(int res) {
//        View view = inflater.inflate(res, container, false);
//        ButterKnife.bind(this, view);
//        initView(view);
//        initEvent();
//        return view;
        layoutRes = res;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void setData(Object data) {
    }

}
