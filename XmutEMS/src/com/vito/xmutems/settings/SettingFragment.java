package com.vito.xmutems.settings;

import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.vito.xmutems.R;
import com.vito.xmutems.utils.CacheProvider;
import com.vito.xmutems.utils.DialogUtil;
import com.vito.xmutems.utils.HttpClientFactory;

import de.ankri.views.Switch;

public class SettingFragment extends Fragment {
	private static final String PREF_NAME = "Settings";
	private static final String PREF_THEME = "isDark";
	private String errorMsg;
	Switch reciveNoticeSwitch;
	TextView oldPassword;
	TextView newPassword;
	TextView confirmNewPassword;
	Button submit;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// Load the settings
		SharedPreferences preferences = getActivity().getSharedPreferences(PREF_NAME, 0);
		boolean isDark = preferences.getBoolean(PREF_THEME, false);

		// set the theme according to the setting
		if (isDark)
			getActivity().setTheme(R.style.AppThemeDark);
		else
			getActivity().setTheme(R.style.AppThemeLight);
		
		View view = inflater.inflate(R.layout.setting_layout, null);
		initView(view);
		initEvent();
		initData();
		return view;
	}
	
	void initView(View view) {
		reciveNoticeSwitch = (Switch) view.findViewById(R.id.reciveNoticeSwitch);
		oldPassword = (TextView) view.findViewById(R.id.oldPassword);
		newPassword = (TextView) view.findViewById(R.id.newPassword);
		confirmNewPassword = (TextView) view.findViewById(R.id.reTypeNewPassword);
		submit = (Button) view.findViewById(R.id.submit);
	}
	
	void initEvent() {
		reciveNoticeSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {  
            
            @Override  
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {  
                if(isChecked) {    
                	JPushInterface.resumePush(getActivity()); 
                	CacheProvider.put(getActivity(), "noticeRecive", true);
                } else {   
                    JPushInterface.stopPush(getActivity());
                	CacheProvider.remove(getActivity(), "noticeRecive");
                }  
            }  
        });
		
		submit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (checkPassword()) {
					new InitDataTask().execute();
				} else {
					DialogUtil.showMsg(getActivity(), errorMsg);
				}
			}
		});
	}
	
	private boolean checkPassword() {
		if (newPassword.getText().length() < 6) {
			errorMsg = "新的密码不能小于6位";
			return false;
		}
		if (!newPassword.getText().toString().equals(confirmNewPassword.getText().toString())) {
			errorMsg = "两次输入的新密码不同";
			return false;
		}
		
		return true;
	}
	
	void initData() {
		if (CacheProvider.get(getActivity(), "noticeRecive") == null) {
			reciveNoticeSwitch.setChecked(false);
		} else {
			reciveNoticeSwitch.setChecked(true);
		}
	}

	class InitDataTask extends AsyncTask<String, String, String> {
		ProgressDialog mpDialog;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// 加载进度对话框
			mpDialog = new ProgressDialog(
					getActivity());
			mpDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);// 设置风格为圆形进度条
			mpDialog.setMessage("加载中...");
			mpDialog.setIndeterminate(false);// 设置进度条是否为不明确
			mpDialog.setCancelable(true);
			mpDialog.show();
		}
		
		@Override
		protected String doInBackground(String... params) {
//			String url = "mmxg.aspx?" +
//					"xh=0901012120" +
//					"&gnmkdm=N121502" +
//					"&__VIEWSTATE=dDwtMzY1OTU0OTYwOzs%2B5q1iRgLYp7KKCMRXMZZBrqhG%2FAk%3D" +
//					"&TextBox2="+oldPassword.getText() +
//					"&TextBox3="+newPassword.getText() +
//					"&Textbox4="+confirmNewPassword.getText();

//			String response = HttpClientFactory.post(url);
			

			String stuNo = CacheProvider.get(getActivity(), "stuNo").toString();
			String url = "mmxg.aspx?xh="+stuNo+"&gnmkdm=N121502";
			Map<String, String> postParams = new HashMap<String, String>();
//			postParams.put("xh", Constant.STU_NO);
//			postParams.put("gnmkdm", "N121502");
			postParams.put("__VIEWSTATE", "dDwtMzY1OTU0OTYwOzs+5q1iRgLYp7KKCMRXMZZBrqhG/Ak=");
			postParams.put("TextBox2", oldPassword.getText().toString());
			postParams.put("TextBox3", newPassword.getText().toString());
			postParams.put("Textbox4", confirmNewPassword.getText().toString());
			
			String response = HttpClientFactory.post(url, postParams, getActivity());
			Document doc = Jsoup.parse(response);
			System.out.println(doc);
			
			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			//adapter.processJson(result);
			this.mpDialog.dismiss();
			
			oldPassword.setText("");
			newPassword.setText("");
			confirmNewPassword.setText("");
		}
		
	}
	
}
