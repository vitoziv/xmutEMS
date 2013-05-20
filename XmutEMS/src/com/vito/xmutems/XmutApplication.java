package com.vito.xmutems;

import org.apache.http.client.CookieStore;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;

import android.app.Application;
import cn.jpush.android.api.JPushInterface;

import com.vito.xmutems.utils.CacheProvider;
import com.vito.xmutems.utils.Constant;
import com.vito.xmutems.utils.HttpClientFactory;

public class XmutApplication extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		HttpClientFactory.context = this;
		
		JPushInterface.setDebugMode(false); //设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        
		if (CacheProvider.get(getApplicationContext(), Constant.USER_COOKIE) != null) {
			//System.out.println("has cookie");
			CookieStore cookieStore = new BasicCookieStore();
			cookieStore.addCookie((Cookie) CacheProvider.get(getApplicationContext(), Constant.USER_COOKIE));
			HttpClientFactory.getHttpClient().setCookieStore(cookieStore);
		}
	}
	
	
}
