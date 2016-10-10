package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.MyCollectVideoAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.VideoBean;
import cn.zn.com.zn_android.uiclass.activity.VideoDetailActivity;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionVideoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionVideoFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.gv_collect_videos)
    GridView mGvCollectVideos;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private MyCollectVideoAdapter mAdapter;
    private List<VideoBean> videoList = new ArrayList<>();

    public CollectionVideoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollectionVideoFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionVideoFragment newInstance() {
        CollectionVideoFragment fragment = new CollectionVideoFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        _setLayoutRes(R.layout.fragment_collection_video);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_collection_video, container, false);
//        ButterKnife.bind(this, view);
//        return view;
//    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {
        mGvCollectVideos.setOnItemClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CollectionVideoFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CollectionVideoFragment");
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //用EventBus传递数据,因为是新生成了一个activity，所以需要发送一个粘性的消息
        EventBus.getDefault().postSticky(new AnyEventType(videoList.get(position).getUrl()));
        Intent intent = new Intent(getActivity(), VideoDetailActivity.class);
        startActivity(intent);
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

    public void setVideoList(List<VideoBean> videoList) {
        this.videoList = videoList;
        mAdapter = new MyCollectVideoAdapter(getActivity(), videoList);
        mGvCollectVideos.setAdapter(mAdapter);
    }

}
