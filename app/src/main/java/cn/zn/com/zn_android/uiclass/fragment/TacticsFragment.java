package cn.zn.com.zn_android.uiclass.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.TacticsAdapter;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.model.bean.HotLiveBean;
import cn.zn.com.zn_android.model.bean.TacticsBean;
import cn.zn.com.zn_android.model.entity.ReturnListValue;
import cn.zn.com.zn_android.uiclass.activity.SpecialLectureActivity;
import cn.zn.com.zn_android.uiclass.activity.TacticsDetailActivity;
import cn.zn.com.zn_android.uiclass.activity.TeacherLiveActivity;
import cn.zn.com.zn_android.uiclass.customerview.JoDialog;
import de.greenrobot.event.EventBus;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TacticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TacticsFragment extends BaseFragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    @Bind(R.id.lv_my_tactics)
    ListView mLvMyTactics;
    @Bind(android.R.id.empty)
    TextView mEmpty;
    @Bind(R.id.ll_empty)
    LinearLayout mLlEmpty;

    private TacticsAdapter mAdapter;
    private JoDialog dialog;

    public TacticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.  就是tid
     * @param param2 Parameter 2.
     * @return A new instance of fragment TacticsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TacticsFragment newInstance(String param1, String param2) {
        TacticsFragment fragment = new TacticsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.fragment_tactics);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_tactics, container, false);
//    }

    @Override
    protected void initView(View view) {
        dialog = new JoDialog.Builder(getActivity())
                .setViewRes(R.layout.layout_loading)
                .setBgRes(Color.TRANSPARENT)
                .show(false);

        if (mParam1 == null || mParam1.equals("")) {
            queryUserTacticsList();
        } else {
            queryRoomTacticsList();
        }
        mEmpty.setText(String.format(getString(R.string.no_data), "策略"));
        mLvMyTactics.setEmptyView(mLlEmpty);
    }

    @Override
    protected void initEvent() {
        mLvMyTactics.setOnItemClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("TacticsFragment"); //统计页面，"MainScreen"为页面名称，可自定义
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("TacticsFragment");
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TacticsBean bean = (TacticsBean) mAdapter.getItem(position);

        if (mParam1.equals("")) {
            if (bean.getTid().equals("9898")) { // 特约讲堂
                startActivity(new Intent(getActivity(), SpecialLectureActivity.class));
            } else { // 老师直播
                HotLiveBean hotLiveBean = new HotLiveBean();
                hotLiveBean.setTid(bean.getTid());
                hotLiveBean.setOrdurl(bean.getOrdurl());
                hotLiveBean.setAvatars(bean.getAvatars());
                hotLiveBean.setTitle(bean.getTitle());
                hotLiveBean.setRoom_number(bean.getRoom_number());
                hotLiveBean.setCollect(bean.getCollect());
                hotLiveBean.setClick(bean.getClick());
                hotLiveBean.setPlacard(bean.getPlacard());
                hotLiveBean.setVipurl(bean.getVipurl());
                EventBus.getDefault().postSticky(new AnyEventType(hotLiveBean));
                startActivity(new Intent(getActivity(), TeacherLiveActivity.class));
            }
        } else {
            startActivity(new Intent(getActivity(), TacticsDetailActivity.class));
            EventBus.getDefault().postSticky(new AnyEventType(bean).setState(true));
        }
    }

    private void queryUserTacticsList() {
        _apiManager.getService().queryUserTacticsList(_mApplication.getUserInfo().getSessionID(), "")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultUserTactics, throwable -> {
                    dialog.dismiss();
                    Log.e(TAG, "queryUserTacticsList: ", throwable);
                });

//        AppObservable.bindFragment(this, _apiManager.getService().queryUserTacticsList(_mApplication.getUserInfo().getSessionID(), ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultUserTactics, throwable -> {
//                    dialog.dismiss();
//                    Log.e(TAG, "queryUserTacticsList: ", throwable);
//                });
    }

    private void resultUserTactics(ReturnListValue<TacticsBean> returnListValue) {
        dialog.dismiss();
        Log.i(TAG, "resultUserTactics: ");
        List<TacticsBean> list = returnListValue.getData();
        Iterator<TacticsBean> iter = list.iterator();
        while (iter.hasNext()) {
            TacticsBean bean = iter.next();
            if (bean.getTid().equals("9898")) {
                iter.remove();
            }
        }
        mAdapter = new TacticsAdapter(getActivity(), list);
        mLvMyTactics.setAdapter(mAdapter);
    }

    /**
     * 查询老师的策略列表
     */
    private void queryRoomTacticsList() {
        _apiManager.getService().queryRoomTacticsList(mParam1)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::resultUserTactics, throwable -> {
                    dialog.dismiss();
                    Log.e(TAG, "queryRoomTacticsList: ", throwable);
                });

//        AppObservable.bindFragment(this, _apiManager.getService().queryRoomTacticsList(mParam1))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(this::resultUserTactics, throwable -> {
//                    dialog.dismiss();
//                    Log.e(TAG, "queryRoomTacticsList: ", throwable);
//                });
    }

}
