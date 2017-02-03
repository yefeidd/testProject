package cn.zn.com.zn_android.uiclass.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.UMShareAPI;

import butterknife.Bind;
import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.adapter.BannerAdapter;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.fragment.ArticalListFragment;
import cn.zn.com.zn_android.uiclass.fragment.BaseFragment;
import cn.zn.com.zn_android.uiclass.fragment.MainFragment;
import cn.zn.com.zn_android.uiclass.fragment.MarketFragment;
import cn.zn.com.zn_android.uiclass.fragment.PersonFragment;
import cn.zn.com.zn_android.uiclass.fragment.SimulateContestHomeFragment;
import cn.zn.com.zn_android.uiclass.fragment.TeacherFragment;
import cn.zn.com.zn_android.utils.ToastUtil;
import de.greenrobot.event.EventBus;

public class MainActivity extends BaseActivity implements BannerAdapter.pageOnlickListener {
    private final int POS_HOME = 0;
    private final int POS_TEACHER = 1;
    private final int POS_MARKET = 2;
    private final int POS_ARTICLE = 3;
    public static final int POS_PERSON = 4;
    private final int POS_CONTEST = 5;
    private final String CURRENT_INDEX = "currentIndex";
    // 再点一次退出程序时间设置
    long WAIT_TIME = 2000;
    long TOUCH_TIME = 0;

    private final int[] LEFT_MENU_ICON = new int[]{R.mipmap.ic_left_recharge, R.mipmap.ic_left_history, R.mipmap.ic_left_collect,
            R.mipmap.ic_left_myfocus, R.mipmap.ic_left_strategy};
    private final int[] TAB_NAMES = {R.string.home, R.string.teacher, R.string.market, R.string.news, R.string.person, R.string.forum};
    private final int[] TAB_ICONS = new int[]{R.drawable.tab_home, R.drawable.tab_teacher,
            R.drawable.tab_market, R.drawable.tab_news, R.drawable.tab_person, R.drawable.tab_person};

    @Bind(android.R.id.tabhost)
    FragmentTabHost mTabHost;

    private MainFragment mainFragment;
    private TeacherFragment teacherFragment;
    private MarketFragment marketFragment;
    //    private ActivitiesFragment activitiesFragment;
    private ArticalListFragment articalListFragment;
    private PersonFragment personFragment;
    private SimulateContestHomeFragment simulateContestHomeFragment;
    private BaseFragment[] fragments;

    private int currentFragIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_main);
        if (null != savedInstanceState) {
            // 系统内存不够，重启应用时
            mainFragment = (MainFragment) getSupportFragmentManager()
                    .findFragmentByTag(MainFragment.class.getSimpleName());
            teacherFragment = (TeacherFragment) getSupportFragmentManager()
                    .findFragmentByTag(TeacherFragment.class.getSimpleName());
            marketFragment = (MarketFragment) getSupportFragmentManager()
                    .findFragmentByTag(MarketFragment.class.getSimpleName());
            articalListFragment = (ArticalListFragment) getSupportFragmentManager()
                    .findFragmentByTag(ArticalListFragment.class.getSimpleName());
            personFragment = (PersonFragment) getSupportFragmentManager()
                    .findFragmentByTag(PersonFragment.class.getSimpleName());
            currentFragIndex = savedInstanceState.getInt(CURRENT_INDEX);

//            setFragmentIndicator(currentFragIndex);
        }

    }

    public void onEventMainThread(AnyEventType event) {
        if (event.getType() != null) {
            currentFragIndex = event.getType();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        switchFragment(currentFragIndex);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CURRENT_INDEX, mTabHost.getCurrentTab());
    }

    @Override
    public void onBackPressed() {
        if (mainFragment.isHidden()) {
//            setFragmentIndicator(POS_HOME);
        } else {
            if (System.currentTimeMillis() - TOUCH_TIME < WAIT_TIME) {
                finish();
            } else {
                TOUCH_TIME = System.currentTimeMillis();
                ToastUtil.showShort(_mApplication, R.string.press_again_exit);
            }
        }
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(this);       //统计时长
        super.onResume();
        //viewPager设置页面
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        currentFragIndex = mTabHost.getCurrentTab();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initView() {

        // 根据下标判断离开前是哪个Fragment在显示,
        // 解决Fragment重叠问题
        initFragment();
//        initBottomTab();
        _mApplication.addActivity(this);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.contentPanel);
        int fragmentCount = fragments.length;
        String[] tabName = getResources().getStringArray(R.array.tab_name);

        if (mTabHost.getChildCount() > 0) {
            mTabHost.clearAllTabs();
        }
        for (int i = 0; i < fragmentCount; ++i) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabName[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragments[i].getClass(), null);
            mTabHost.getTabWidget().setDividerDrawable(null);

        }
        mTabHost.getTabWidget().getChildTabViewAt(POS_CONTEST).setVisibility(View.GONE);
    }

    @Override
    protected void initEvent() {
        mTabHost.getTabWidget().getChildTabViewAt(POS_HOME).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabHost.setCurrentTab(POS_HOME);
            }
        });
        mTabHost.getTabWidget().getChildTabViewAt(POS_TEACHER).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabHost.setCurrentTab(POS_TEACHER);
            }
        });
        mTabHost.getTabWidget().getChildTabViewAt(POS_MARKET).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabHost.setCurrentTab(POS_MARKET);
            }
        });
        mTabHost.getTabWidget().getChildTabViewAt(POS_ARTICLE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabHost.setCurrentTab(POS_ARTICLE);
//                startActivity(new Intent(getApplicationContext(), ArticleListActivity.class));
            }
        });
        mTabHost.getTabWidget().getChildTabViewAt(POS_PERSON).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTabHost.setCurrentTab(POS_PERSON);
            }
        });
    }

    private View getTabItemView(int index) {
        View view = LayoutInflater.from(this).inflate(R.layout.layout_tab, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageResource(TAB_ICONS[index]);
        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(TAB_NAMES[index]);
        return view;
    }

    /**
     * 初始化各 Fragment
     */
    private void initFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        fragments = new BaseFragment[6];

        if (mainFragment == null) {
            mainFragment = new MainFragment();
            transaction.add(R.id.contentPanel, mainFragment, mainFragment.getClass().getSimpleName());
        }
        fragments[POS_HOME] = mainFragment;
        transaction.show(mainFragment);

        if (teacherFragment == null) {
            teacherFragment = new TeacherFragment();
            transaction.add(R.id.contentPanel, teacherFragment, teacherFragment.getClass().getSimpleName());
        }
        fragments[POS_TEACHER] = teacherFragment;
        transaction.hide(teacherFragment);

        if (marketFragment == null) {
            marketFragment = new MarketFragment();
            transaction.add(R.id.contentPanel, marketFragment, marketFragment.getClass().getSimpleName());
        }
        fragments[POS_MARKET] = marketFragment;
        transaction.hide(marketFragment);

        if (articalListFragment == null) {
            articalListFragment = new ArticalListFragment();
            Bundle bundle = new Bundle();
            bundle.putString(Constants.FROM, Constants.NEWS);
            articalListFragment.setArguments(bundle);
            transaction.add(R.id.contentPanel, articalListFragment, articalListFragment.getClass().getSimpleName());
        }
        fragments[POS_ARTICLE] = articalListFragment;
        transaction.hide(articalListFragment);

        if (null == personFragment) {
            personFragment = new PersonFragment();
            transaction.add(R.id.contentPanel, personFragment, personFragment.getClass().getSimpleName());
        }
        fragments[POS_PERSON] = personFragment;
        transaction.hide(personFragment);

        //模拟大赛
        if (null == simulateContestHomeFragment) {
            simulateContestHomeFragment = new SimulateContestHomeFragment();
            transaction.add(R.id.contentPanel, simulateContestHomeFragment, simulateContestHomeFragment.getClass().getSimpleName());
        }
        fragments[POS_CONTEST] = simulateContestHomeFragment;
        transaction.hide(simulateContestHomeFragment);

        if (!transaction.isEmpty()) {
            transaction.commit();
        }
    }
//
//    private void setFragmentIndicator(int index) {
//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        if (currentFragIndex != index) {
//            transaction.hide(fragments[currentFragIndex]).show(fragments[index]);
//        }
//        currentFragIndex = index;
//        transaction.commit();
//    }

    @Override
    public void clickPage(int position) {
        switchFragment(POS_CONTEST);
    }


    /**
     * 重写activityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     *
     * @param postion
     */
    public void switchFragment(int postion) {
        mTabHost.setCurrentTab(postion);
    }
}
