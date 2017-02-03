package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.activity.CourseActivity;
import cn.zn.com.zn_android.uiclass.activity.LoginActivity;
import cn.zn.com.zn_android.uiclass.activity.MemberAreaActivity;
import cn.zn.com.zn_android.uiclass.activity.NewestTacticsActivity;
import cn.zn.com.zn_android.uiclass.activity.NoticeActivity;
import cn.zn.com.zn_android.uiclass.activity.RankingListActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherArticleListActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.VideosActivity;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import de.greenrobot.event.EventBus;

/**
 * 聊天Fragment
 * <p>
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WealthToolsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WealthToolsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private final int[] ICON_RES = {R.mipmap.ic_wealth_member_area2, R.mipmap.ic_wealth_videos2, R.mipmap.ic_wealth_rank2,
            R.mipmap.ic_wealth_course2, R.mipmap.ic_wealth_notice};
    private final int[] ROOM_ICON_RES = {R.mipmap.ic_new_tactics, R.mipmap.ic_wealth_member_area2, R.mipmap.ic_wealth_article, R.mipmap.ic_wealth_videos2,
            R.mipmap.ic_wealth_rank2, R.mipmap.ic_wealth_notice2, R.mipmap.ic_wealth_notice};

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    @Bind(R.id.gv_wealth_tool)
    GridView mGvWealthTool;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private List<Map<String, Object>> data = new ArrayList<>();

//    private OnFragmentInteractionListener mListener;

    public WealthToolsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WealthToolsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WealthToolsFragment newInstance(String param1, String param2) {
        WealthToolsFragment fragment = new WealthToolsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        _setLayoutRes(R.layout.fragment_wealth_tools);
        ButterKnife.bind(this, super.onCreateView(inflater, container, savedInstanceState));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected void initView(View view) {
        if (mParam1.equals("")) {
            getData(getResources().getStringArray(R.array.wealth_tool), ICON_RES);
        } else {
            getData(getResources().getStringArray(R.array.wealth_tool_room), ROOM_ICON_RES);
        }
        mGvWealthTool.setAdapter(new SimpleAdapter(_mApplication, data, R.layout.item_wealth_tools, new String[]{"img", "name"}, new int[]{R.id.iv_img, R.id.tv_name}));

    }

    @Override
    protected void initEvent() {
        mGvWealthTool.setOnItemClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("WealthToolsFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("WealthToolsFragment");
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
        if (mParam1.equals("")) {
            switch (position) { //特约讲堂财富工具
                case 0:
                    if (_mApplication.getUserInfo().getIsLogin() == Constants.IS_LOGIN) {
                        startActivity(new Intent(getActivity(), MemberAreaActivity.class));
                        EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    } else {
                        startLoginDialog();
                    }
                    break;
                case 1:
                    startActivity(new Intent(getActivity(), VideosActivity.class));
                    break;
                case 2:
                    EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    startActivity(new Intent(getActivity(), RankingListActivity.class));
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), CourseActivity.class));
                    break;
                case 4:
                    startActivity(new Intent(getActivity(), NoticeActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(""));
                    break;
                case 5:
                    startActivity(new Intent(getActivity(), NoticeActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    break;
            }
        } else {
            switch (position) {  //老师房间财富工具
                case 0:
                    Intent intent = new Intent(getActivity(), NewestTacticsActivity.class);
                    intent.putExtra("teacherId", mParam1);
                    startActivity(intent);
                    break;
                case 1:
                    if (_mApplication.getUserInfo().getIsLogin() == Constants.IS_LOGIN) {
                        startActivity(new Intent(getActivity(), MemberAreaActivity.class));
                        EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    } else {
                        startLoginDialog();
                    }
                    break;
                case 2:
                    startActivity(new Intent(getActivity(), TeacherArticleListActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    break;
                case 3:
                    startActivity(new Intent(getActivity(), VideosActivity.class));
                    break;
                case 4:
                    EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    startActivity(new Intent(getActivity(), RankingListActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    break;
                case 5:
                    startActivity(new Intent(getActivity(), TeacherDetailActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    break;
                case 6:
                    startActivity(new Intent(getActivity(), NoticeActivity.class));
                    EventBus.getDefault().postSticky(new AnyEventType(mParam1));
                    break;
            }
        }

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

    private void getData(String[] titiles, int[] iconRes) {
        data = new ArrayList<>();

        for (int i = 0; i < titiles.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("img", iconRes[i]);
            map.put("name", titiles[i]);
            data.add(map);
        }

    }

    private void startLoginDialog() {
        new JoDialog.Builder(getContext())
                .setStrTitle(R.string.tips)
                .setStrContent(getString(R.string.please_login))
                .setPositiveTextRes(R.string.confirm)
                .setNegativeTextRes(R.string.cancel)
                .setCallback(new JoDialog.ButtonCallback() {
                    @Override
                    public void onPositive(JoDialog dialog) {
                        dialog.dismiss();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    }

                    @Override
                    public void onNegtive(JoDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show(true);
    }
}
