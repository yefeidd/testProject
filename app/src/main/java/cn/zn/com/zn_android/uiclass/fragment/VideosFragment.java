package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.VideoListAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.VideoBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.activity.VideoDetailActivity;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VideosFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VideosFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideosFragment extends BaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1 = "";
    private String mParam2 = "";

//    private OnFragmentInteractionListener mListener;

    @Bind(R.id.lmlv_videos)
    ListView mLmlvVideos;

    private List<VideoBean> videoList = new ArrayList<>();
    private VideoListAdapter mAdapter;
    private JoDialog dialog;

    public VideosFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment VideosFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static VideosFragment newInstance(String param1, String param2) {
        VideosFragment fragment = new VideosFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_videos);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    protected void initView(View view) {
        mAdapter = new VideoListAdapter(_mApplication, videoList);
        mLmlvVideos.setAdapter(mAdapter);
        if (mParam1.equals("")) {
            dialog = new JoDialog.Builder(getActivity())
                    .setViewRes(R.layout.layout_loading)
                    .setBgRes(Color.TRANSPARENT)
                    .show(false);

        }
    }

    @Override
    protected void initEvent() {
        mLmlvVideos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            private VideoBean videoInfo;

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                videoInfo = videoList.get(position);
                //用EventBus传递数据,因为是新生成了一个activity，所以需要发送一个粘性的消息
                EventBus.getDefault().postSticky(new AnyEventType(videoInfo.getUrl()));
                Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("VideosFragment"); //统计页面，"MainScreen"为页面名称，可自定义
        queryVideoList();
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("VideosFragment");
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

    private void queryVideoList() {
        _apiManager.getService().queryVideoList("")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::resultVideoList, throwable -> {
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    Log.e(TAG, "queryVideoList: ", throwable);
                });
//        AppObservable.bindFragment(this, _apiManager.getService().queryVideoList(""))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(this::resultVideoList, throwable -> {
//                    if (dialog != null) {
//                        dialog.dismiss();
//                    }
//                    Log.e(TAG, "queryVideoList: ", throwable);
//                });
    }

    private void resultVideoList(ReturnListValue<VideoBean> returnValue) {
        if (dialog != null) {
            dialog.dismiss();
        }
        videoList = returnValue.getData();
        mAdapter.setData(videoList);

    }

    public void setVideoList(List<VideoBean> videoList) {
        this.videoList = videoList;
        mAdapter.setData(videoList);
    }
}
