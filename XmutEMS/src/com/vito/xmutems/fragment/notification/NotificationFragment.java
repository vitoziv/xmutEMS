package com.vito.xmutems.fragment.notification;

import java.util.Iterator;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.vito.xmutems.R;
import com.vito.xmutems.adapter.NotificationListAdapter;
import com.vito.xmutems.domain.Notification;
import com.vito.xmutems.utils.HttpClientFactory;
import com.vito.xmutems.utils.L;

public class NotificationFragment extends Fragment {
	ListView list;
	NotificationListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.notification_layout, null);
		initView(view);
		initEvent();
		initData();
		return view;
	}
	
	private void initView(View view) {
		list = (ListView) view.findViewById(R.id.notificationList);
	}
	
	private void initEvent() {
		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				// TODO Auto-generated method stub
				if (adapter.getCount() > position) {
					Notification notification = adapter.getList().get(position);
					L.i("url:"+notification.getUrl());
					Intent intent = new Intent();
					intent.putExtra("notification", notification);
					intent.setClass(getActivity(), NotificationDetailActivity.class);
					startActivity(intent);
				}
			}
		});
		
	}
	
	public void initData() {
		adapter = new NotificationListAdapter(getActivity());
		list.setAdapter(adapter);
		new InitDataTask().execute();
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
		protected String doInBackground(String... arg0) {
			//获取通知
	        String notificationResponse = HttpClientFactory.get("content.aspx");
			Document notificationDoc = Jsoup.parse(notificationResponse);
			
			//获取所有通知记录
			Elements notificationTr = notificationDoc.select(".datelist tr");
			Iterator<Element> trIt = notificationTr.listIterator();
			while (trIt.hasNext()) {
				Element nextElement = trIt.next();
				if (nextElement.select("a").isEmpty()) continue;//标题栏的数据不需要
				
				//公告标题。<a>标签里面的内容
				Elements eleA = nextElement.select("a");
				String notificationTitle = eleA.text();
				
				//url
				String url = eleA.attr("onclick");
	        	int startOffset = url.indexOf("'") + 1;
        		url = url.substring(startOffset, url.indexOf("'", startOffset));

	        	if (url.length() == 55) {//某些链接最后的空格字符其实是：%26%23160%3B
	        		url = url.substring(0, url.length()-1) + "%26%23160%3B";
	        	}
	        	
	        	//发行单位
	        	Element publisher = nextElement.select("td").get(1);
	        	
	        	//发布时间
	        	Element publishDate = nextElement.select("td").get(2);
	        	
	        	//有效期限
	        	Element valid = nextElement.select("td").get(3);
	        	
	        	Notification notification = new Notification(notificationTitle,
	        			publisher.text(),
	        			publishDate.text(),
	        			valid.text(),
	        			url);
	        	adapter.getList().add(notification);
			}
			return "";
		}
		
		@Override
		protected void onPostExecute(String result) {
			adapter.notifyDataSetChanged();
			this.mpDialog.dismiss();
		}
		
	}
}
