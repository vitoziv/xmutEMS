package com.vito.xmutems.fragment.notification;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.vito.xmutems.R;
import com.vito.xmutems.domain.Notification;
import com.vito.xmutems.utils.L;

public class NotificationWebViewActivity extends Activity {
	private Notification notification;
	WebView mWebView;
	ProgressBar loadBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notification_webview_layout);
		initView();
		initData();
	}
	
	private void initView() {
		mWebView = (WebView) findViewById(R.id.mWebView);
		loadBar = (ProgressBar) findViewById(R.id.loadBar);
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	private void initData() {
		notification = (Notification) getIntent().getSerializableExtra("notification");
		
		mWebView.getSettings().setJavaScriptEnabled(true);//可用JS
		mWebView.setScrollBarStyle(0);//滚动条风格，为0就是不给滚动条留空间，滚动条覆盖在网页上
		mWebView.setWebViewClient(new WebViewClient(){   
            public boolean shouldOverrideUrlLoading(final WebView view, final String url) {
            	loadurl(view,url);//载入网页
                return true;   
            }
            
            //网页加载完成后调用
            public void onPageFinished(WebView view, String url) {
            	loadBar.setVisibility(ProgressBar.GONE);
            }
        });
		L.i("initData() url:" + notification.getUrl());
		if(notification.getUrl()!=null) {
			loadurl(mWebView,notification.getUrl());
		}
	}
	
	public void loadurl(final WebView view,final String url){
    	new InitDataTask(view).execute(url);
    }
	
	class InitDataTask extends AsyncTask<String, String, String> {
		private WebView webview;
		
		public InitDataTask(WebView webview) {
			this.webview = webview;
		}
		
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			webview.loadUrl(params[0]);
			return null;
		}
		
	}
}
