package com.vito.xmutems.adapter;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vito.xmutems.R;
import com.vito.xmutems.domain.Notification;
import com.vito.xmutems.utils.L;

public class NotificationListAdapter extends BaseAdapter {
	private List<Notification> list = new ArrayList<Notification>();
	private Context context;
	
	public NotificationListAdapter(Context context) {
		this.context = context;
	}
	
	public List<Notification> getList() {
		return this.list;
	}
	public void setList(List<Notification> list) {
		this.list = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_notification_row, null);
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.publisher = (TextView) convertView.findViewById(R.id.publisher);
			holder.valid = (TextView) convertView.findViewById(R.id.valid);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		Notification notification = list.get(position);
		
		holder.title.setText(notification.getTitle());
		holder.publisher.setText(notification.getPublisher());
		holder.valid.setText(notification.getValid());

		return convertView;
	}
	
	public int processJson(String json) throws JSONException {
		L.d("通知信息", json);
		JSONArray jsonArray = new JSONArray(json);
		for (int i = 0,len = jsonArray.length(); i < len; i++) {
			JSONObject jsonObj = jsonArray.getJSONObject(i);
			Notification notification = new Notification(jsonObj);
			list.add(notification);
		}
		
		notifyDataSetChanged();
		return 0;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	
	static class ViewHolder
    {
        public TextView title;
        public TextView publisher;
        public TextView valid;
    }

}