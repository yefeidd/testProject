package cn.zn.com.zn_android.uiclass.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import cn.zn.com.zn_android.R;
import cn.zn.com.zn_android.uiclass.x5webview.X5WebView;
import com.umeng.analytics.MobclickAgent;

public class RefreshActivity extends Activity {
	X5WebView webView;
	TextView title;
	
	/**
	 * 此类实现了下拉刷新，
	 * 使用extension interface将会准确回去overScroll的时机
	 * 
	 */
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_refresh);
		webView=(X5WebView)findViewById(R.id.web_filechooser);
		title = (TextView) findViewById(R.id.refreshText);
		webView.setTitle(title);
		webView.loadUrl("http://app.html5.qq.com/navi/index");
		this.initBtn();
	}
	
	private void initBtn(){
		Button btnFlush=(Button) findViewById(R.id.bt_filechooser_flush);
		btnFlush.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				webView.reload();
				webView.setDayOrNight(false);
			}
		});
		
		Button btnBackForward=(Button) findViewById(R.id.bt_filechooser_back);
		btnBackForward.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.goBack();
			}
		});
		
		Button btnHome=(Button) findViewById(R.id.bt_filechooser_home);
		btnHome.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				webView.loadUrl("http://app.html5.qq.com/navi/index");
			}
		});
	}

	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("RefreshActivity"); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
		MobclickAgent.onPause(this);
	}

	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("RefreshActivity"); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
		MobclickAgent.onResume(this);          //统计时长
	}
}
