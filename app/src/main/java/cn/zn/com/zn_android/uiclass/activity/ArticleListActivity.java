package cn.zn.com.zn_android.uiclass.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.manage.Constants;
import cn.zn.com.zn_android.model.bean.AnyEventType;
import cn.zn.com.zn_android.uiclass.fragment.ArticalListFragment;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * 名家博文列表
 * Created by zjs on 2016/3/25 0025.
 */
public class ArticleListActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        EventBus.getDefault().registerSticky(this);
        _setLightSystemBarTheme(false);
        super.onCreate(savedInstanceState);
        _setLayoutRes(R.layout.activity_article_list);
    }


    public void onEventMainThread(AnyEventType event) {
        if (event.getObject() instanceof String && !"".equals(event.getObject())) {
        }
    }

    @Override
    protected void initView() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Bundle bundle = new Bundle();
        bundle.putString(Constants.FROM, Constants.ARTICLE);
        ArticalListFragment fragment = new ArticalListFragment();
        fragment.setArguments(bundle);
        transaction.replace(R.id.fl_art_list, fragment);
        transaction.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onPageStart("ArticleListActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
        MobclickAgent.onResume(this);          //统计时长

    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("ArticleListActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
        MobclickAgent.onPause(this);
    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
