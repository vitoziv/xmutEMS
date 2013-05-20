package com.vito.xmutems;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.vito.xmutems.utils.CacheProvider;
import com.vito.xmutems.utils.Constant;
import com.vito.xmutems.utils.HttpClientFactory;
import com.vito.xmutems.utils.ImageDownloader;
import com.vito.xmutems.utils.SerializableCookie;

public class LoginActivity extends Activity {
	EditText stuNO;
	EditText password;
	ImageView checkCodeImage;
	EditText checkCode;
	Button submitBtn;
	
	ImageDownloader imageDownloader;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
		                      WindowManager.LayoutParams. FLAG_FULLSCREEN);
		
		setContentView(R.layout.activity_login);
		if (CacheProvider.get(getApplicationContext(), Constant.USER_COOKIE) != null) {
			//使用用户cookie登录网站
			CookieStore cookieStore = new BasicCookieStore();
			cookieStore.addCookie((Cookie) CacheProvider.get(getApplicationContext(), Constant.USER_COOKIE));
			HttpClientFactory.getHttpClient().setCookieStore(cookieStore);
			
			//System.out.println(cookieStore.getCookies().get(0).getName() +"=" + cookieStore.getCookies().get(0).getValue());
			redirect();
		} else {
			initView();
			initEvent();
			initData();
		}
	}

	private void initView() {
		stuNO 			= (EditText) findViewById(R.id.stuNO);
		password 		= (EditText) findViewById(R.id.password);
		checkCode		= (EditText) findViewById(R.id.checkCode);
		checkCodeImage 	= (ImageView) findViewById(R.id.checkCodeImage);
		submitBtn 		= (Button) findViewById(R.id.submitBtn);
	}
	
	private void initEvent() {
		submitBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				hiddenKeyboard();
				new InitDataTask().execute();
			}
		});
		checkCodeImage.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				resetCheckCodeImage();
			}
		});
		
	}
	
	private void initData() {
		imageDownloader = new ImageDownloader(LoginActivity.this);
		resetCheckCodeImage();
	}
	
	private void hiddenKeyboard() {
		InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(stuNO.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(password.getWindowToken(), 0);
		imm.hideSoftInputFromWindow(checkCode.getWindowToken(), 0);
	}
	
	private void resetCheckCodeImage() {
		imageDownloader.download("http://jxgl.xmut.edu.cn/CheckCode.aspx", checkCodeImage);
	}
	
	class InitDataTask extends AsyncTask<String, String, Boolean> {
		private String pMsg = "登录中...";
		ProgressDialog mpDialog;
		@Override
		protected void onPreExecute() {
			// 加载进度对话框
			mpDialog = new ProgressDialog(
					LoginActivity.this);
			mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
			mpDialog.setMessage(pMsg);
			mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
			mpDialog.setCancelable(false);
			mpDialog.show();
			super.onPreExecute();
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			String stuNoStr = stuNO.getText().toString();
			String passwordStr = password.getText().toString();
			String checkCodeStr = checkCode.getText().toString();
			
			String loginUrl = "http://jxgl.xmut.edu.cn/default2.aspx?__VIEWSTATE=dDwtMTg3MTM5OTI5MTs7Pl7Fe0AMecK6q7I4zUwetu8qhVpI" +
					"&TextBox1=" + stuNoStr +
					"&TextBox2=" + passwordStr +
					"&TextBox3=" + checkCodeStr +
					"&RadioButtonList1=%E5%AD%A6%E7%94%9F&Button1=%E7%99%BB%E5%BD%95";
			Boolean result = false;
			try {
				//创建一个默认的HttpClient
				DefaultHttpClient httpclient = HttpClientFactory.getHttpClient();
				//创建一个GET请求
		        HttpGet request = new HttpGet(loginUrl);
		        //发送GET请求，并将响应内容转换成字符串
		        String response = httpclient.execute(request, new BasicResponseHandler());
		        
		        Document doc = Jsoup.parse(response);
		        Elements nav = doc.select(".nav");
		        if (!nav.isEmpty()) {
		        	result = true;
			        //保存cookie
		        	CacheProvider.put(LoginActivity.this, Constant.USER_COOKIE,
		        			new SerializableCookie(httpclient.getCookieStore().getCookies().get(0)));
		        	HttpClientFactory.cookieStore = httpclient.getCookieStore();
		        	CacheProvider.put(LoginActivity.this, "stuNo", stuNoStr);
		        	
		        	Elements stuName = doc.select("#xhxm");
		        	String stuNameStr = stuName.text().substring(stuName.text().length()-4, stuName.text().length()-2);
		        	CacheProvider.put(LoginActivity.this, "stuName", stuNameStr);
		        }
		        
		    } catch (ClientProtocolException e) {
		        e.printStackTrace();
		    } catch (IOException e) {
		        e.printStackTrace();
		    }
			
			return result;
		}
		
		@Override
		protected void onPostExecute(Boolean result) {
	        this.mpDialog.dismiss();
	        if (result) {//登录成功
	        	redirect();
	        } else {
	        	resetCheckCodeImage();
	        }
		}
	}
	
	private void redirect() {
		
		Intent intent = new Intent();
    	intent.setClass(LoginActivity.this, MainActivity.class);
    	startActivity(intent);
    	
    	//finish() destroys the current Activity and therefore removes it from the Stack.
    	this.finish();
	}
	
}
