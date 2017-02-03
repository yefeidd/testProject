package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.ArticleListAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.model.bean.CollectVideoArtBean;
import cn.zn.com.zn_android.model.entity.ReturnValue;
import cn.zn.com.zn_android.uiclass.xlistview.XListView;
import cn.zn.com.zn_android.utils.ToastUtil;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 文章 Fragment
 *
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ArticlesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ArticlesFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.lv_article)
    XListView mLvArticle;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private int page = 1;
    private final int pcount = 10;
    private ArticleListAdapter articleAdapter;
    private List<ArticleBean> data = new ArrayList<>();

//    private OnFragmentInteractionListener mListener;

    public ArticlesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ArticlesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ArticlesFragment newInstance(String param1, String param2) {
        ArticlesFragment fragment = new ArticlesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_articles);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView(View view) {
//        queryStockSchool();
        mLvArticle.setHeaderDividersEnabled(false);
        articleAdapter = new ArticleListAdapter(getActivity(), data);
        mLvArticle.setAdapter(articleAdapter);
    }

    @Override
    protected void initEvent() {
        mLvArticle.setPullLoadEnable(true);
        mLvArticle.setPullRefreshEnable(true);
        mLvArticle.setLoadMoreEnable(true);
        mLvArticle.setXListViewListener(new XListView.IXListViewListener() {
            @Override
            public void onRefresh() {
                mLvArticle.setPullLoadEnable(true);
                mLvArticle.setLoadMoreEnable(true);
                page = 1;
                data = new ArrayList<>();
                queryStockSchool(page, pcount);
            }

            @Override
            public void onLoadMore() {
                ++page;
                queryStockSchool(1, page * pcount);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("ArticlesFragment"); //统计页面，"MainScreen"为页面名称，可自定义
        queryStockSchool(1, page * pcount);
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ArticlesFragment");
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        mListener = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void queryStockSchool(int page, int pcount) {
        _apiManager.getService().queryStockSchool(page, pcount)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultStockSchool, throwable -> {
                    Log.e(TAG, "queryStockSchool: ", throwable);
                    mLvArticle.stopRefresh();
                    mLvArticle.stopLoadMore();
                });

//        AppObservable.bindFragment(this, _apiManager.getService().queryStockSchool(page, pcount))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultStockSchool, throwable -> {
//                    Log.e(TAG, "queryStockSchool: ", throwable);
//                    mLvArticle.stopRefresh();
//                    mLvArticle.stopLoadMore();
//                });
    }

    private void resultStockSchool(ReturnValue<CollectVideoArtBean> returnValue) {
        mLvArticle.stopRefresh();
        mLvArticle.stopLoadMore();
        if (returnValue != null && !returnValue.getMsg().equals(Constants.ERROR) && returnValue.getData() != null) {
            if (returnValue.getData().getArt().size() > 0) {
                data.clear();
                data.addAll(returnValue.getData().getArt());
                articleAdapter.setmContentList(data);
            } else {
                mLvArticle.setLoadMoreEnable(false);
                mLvArticle.setPullLoadEnable(false);
            }
        } else {
            ToastUtil.showShort(_mApplication, returnValue.getMsg());
        }
    }

    public void setData(List<ArticleBean> data) {
        this.data = data;
        articleAdapter.setmContentList(data);
    }
}
