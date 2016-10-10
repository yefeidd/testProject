package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.ArticleBean;
import cn.zn.com.zn_android.uiclass.activity.ArticleDetailActivity;
import cn.zn.com.zn_android.utils.DateUtils;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CollectionArtFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CollectionArtFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.lv_art)
    ListView mLvArt;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    private SimpleAdapter mAdapter;
    private List<Map<String, String>> artList = new ArrayList<>();
    private List<ArticleBean> articleBeans = new ArrayList<>();

    public CollectionArtFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment CollectionArtFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CollectionArtFragment newInstance() {
        CollectionArtFragment fragment = new CollectionArtFragment();
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
        _setLayoutRes(R.layout.fragment_collection_art);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_collection_art, container, false);
//        ButterKnife.bind(this, view);
//        return view;
//    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initEvent() {
        mLvArt.setOnItemClickListener(this);
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("CollectionArtFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("CollectionArtFragment");
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
        EventBus.getDefault().postSticky(new AnyEventType(articleBeans.get(position)));
        Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
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


    public void setArtList(List<ArticleBean> articleBeans) {
        this.articleBeans = articleBeans;
        getArtData(articleBeans);
        mAdapter = new SimpleAdapter(getActivity(), artList, R.layout.item_list_art, new String[] {Constants.TITLE, Constants.INFO, Constants.TIME}, new int[] {R.id.tv_art_title, R.id.tv_art_info, R.id.tv_art_time});
        mLvArt.setAdapter(mAdapter);


    }

    private void getArtData(List<ArticleBean> list) {
        for (ArticleBean b:list) {
            Map<String, String> map = new HashMap<>();
            map.put(Constants.TITLE, b.getTitle());
            map.put(Constants.INFO, b.getSource());
            String month = b.getTimes().substring(0, 7);
            String currentMonth = DateUtils.getCurrentYearMonth("yyyy-MM");
            if (!month.equals(currentMonth)) {
                map.put(Constants.TIME, b.getTimes().substring(0, 10));
            } else {
                String day = b.getTimes().substring(8, 10);
                int currentDay = DateUtils.getCurrentDay();
                if (currentDay - Integer.valueOf(day) > 3) {
                    map.put(Constants.TIME, b.getTimes().substring(0, 10));
                } else if (currentDay - Integer.valueOf(day) == 0){
                    map.put(Constants.TIME, "今天");
                } else {
                    map.put(Constants.TIME, (currentDay - Integer.valueOf(day)) + "天前");
                }
            }
            map.put(Constants.URL, b.getUrl());
            artList.add(map);
        }
    }

}
