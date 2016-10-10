package cn.zn.com.zn_android.uiclass;

import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Jolly on 2016/4/27 0027.
 */
public class JoWebViewClient extends WebViewClient {
    @Override
    public void onPageCommitVisible(WebView view, String url) {
//        super.onPageCommitVisible(view, url);
        view.setVisibility(View.GONE);
        Log.i("ActivitiesFragment", "onPageCommitVisible: ");
    }
}
