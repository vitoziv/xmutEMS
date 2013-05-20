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
import com.vito.xmutems.domain.Exam;
import com.vito.xmutems.utils.L;

public class ExamListAdapter extends BaseAdapter{
	private List<Exam> list = new ArrayList<Exam>();
	private Context context;
	
	public ExamListAdapter(Context context) {
		this.context = context;
	}
	
	public List<Exam> getList() {
		return this.list;
	}
	public void setList(List<Exam> list) {
		this.list = list;
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.exam_row, null);
			holder.courseName = (TextView) convertView.findViewById(R.id.courseName);
			holder.examDate = (TextView) convertView.findViewById(R.id.examDate);
			holder.location = (TextView) convertView.findViewById(R.id.location);
			holder.sitNumber = (TextView) convertView.findViewById(R.id.sitNumber);
			holder.school = (TextView) convertView.findViewById(R.id.school);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder)convertView.getTag();
		}
		Exam exam = list.get(position);
		
		holder.courseName.setText(exam.getCourseName());
		holder.examDate.setText(exam.getExamDate());
		holder.location.setText(exam.getLocation());
		holder.sitNumber.setText("座位号"+exam.getSitNumber());
		holder.school.setText(exam.getSchool());
		
		return convertView;
	}
	
	public int processJson(String json) {
		try {
			L.d("考试信息", json);
			list.clear();
			JSONArray jsonArray = new JSONArray(json);
			for (int i = 0,len = jsonArray.length(); i < len; i++) {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				Exam exam = new Exam(jsonObj);
				list.add(exam);
			}
			
			notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		return 0;
	}

	static class ViewHolder
    {
        public TextView courseName;
        public TextView examDate;
        public TextView location;
        public TextView sitNumber;
        public TextView school;
    }
}
