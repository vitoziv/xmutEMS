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
import com.vito.xmutems.domain.Score;
import com.vito.xmutems.utils.L;

public class ScoreListAdapter extends BaseAdapter {
	private List<Score> list = new ArrayList<Score>();
	private Context context;
	
	public ScoreListAdapter(Context context) {
		this.context = context;
	}
	
	public List<Score> getList() {
		return this.list;
	}
	public void setList(List<Score> list) {
		this.list = list;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.score_row, null);
			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.score = (TextView) convertView.findViewById(R.id.score);
			holder.credits = (TextView) convertView.findViewById(R.id.credits);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		Score score = list.get(position);
		
		holder.name.setText(score.getName());
		holder.score.setText(score.getScore());
		holder.credits.setText(score.getCredits());
		return convertView;
	}
	
	public int processJson(String json, String semester) {
		try {
			L.d("成绩信息", json);
			list.clear();
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0,len = jsonArray.length(); i < len; i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				if (jsonObj.optString("semester").equals(semester)) {
					Score score = new Score(jsonObj);
					list.add(score);
				}
			}
			
			notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
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
        public TextView name;
        public TextView score;
        public TextView credits;
    }

}