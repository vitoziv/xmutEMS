package com.vito.xmutems.course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.vito.xmutems.R;
import com.vito.xmutems.domain.Course;
import com.vito.xmutems.utils.DialogUtil;
import com.vito.xmutems.utils.HttpClientFactory;
import com.vito.xmutems.utils.L;

public class CourseFragment extends Fragment {
	Spinner yearPicker;
	Spinner semesterPicker;
	Button searchBtn;
	RelativeLayout courseContainer;
	
	TextView monday, tuesday, wednesday, thursday, friday, saturday, sunday;
	
	
	final String[][] urls = {
								{"http://vit0.com/xmutEDSData/course1.json",
								"http://vit0.com/xmutEDSData/course3.json",
								"http://vit0.com/xmutEDSData/course5.json",
								"http://vit0.com/xmutEDSData/course7.json"},
								{"http://vit0.com/xmutEDSData/course2.json",
								"http://vit0.com/xmutEDSData/course4.json",
								"http://vit0.com/xmutEDSData/course6.json",
								""}
								};
	final String[] years={"2009-2010","2010-2011","2011-2012","2012-2013"};
	final String[] semesters={"1","2"};
	String yearUrl;
	Integer semesterPosition;
	Integer yearlPosition;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.course_layout, null);
		initView(view);
		initEvent();
		initData();
		return view;
	}
	
	void initView(View view) {
		courseContainer = (RelativeLayout) view.findViewById(R.id.courseContainer);
		yearPicker = (Spinner) view.findViewById(R.id.yearPicker);
		semesterPicker = (Spinner) view.findViewById(R.id.semesterPicker);
		searchBtn = (Button) view.findViewById(R.id.searchBtn);
		
		monday = (TextView) view.findViewById(R.id.monday);
		tuesday = (TextView) view.findViewById(R.id.tuesday);
		wednesday = (TextView) view.findViewById(R.id.wednesday);
		thursday = (TextView) view.findViewById(R.id.thursday);
		friday = (TextView) view.findViewById(R.id.friday);
		saturday = (TextView) view.findViewById(R.id.saturday);
		sunday = (TextView) view.findViewById(R.id.sunday);
	}
	
	void initEvent() {
		yearPicker.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				yearlPosition = position;
				yearUrl = urls[semesterPosition][yearlPosition];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		semesterPicker.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				semesterPosition = arg2;
				yearUrl = urls[semesterPosition][yearlPosition];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});
		
		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new InitDataTask().execute();
			}
		});
	}
	
	@SuppressLint("NewApi")
	void initData() {
		Display display = getActivity().getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		int windowWidth = size.x;
		
		int _20dp = dpToPx(getActivity(), 20);
		int width = (windowWidth-_20dp) / 7;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, _20dp);
		monday.setLayoutParams(params);
		tuesday.setLayoutParams(params);
		wednesday.setLayoutParams(params);
		thursday.setLayoutParams(params);
		friday.setLayoutParams(params);
		saturday.setLayoutParams(params);
		sunday.setLayoutParams(params);
		
		semesterPosition = 0;
		yearlPosition = 0;
		yearUrl = urls[semesterPosition][yearlPosition];
		
		ArrayAdapter<String> yearPickerAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,years);
		yearPickerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		yearPicker.setAdapter(yearPickerAdapter);
		
		ArrayAdapter<String> semesterPickerAdapter = new ArrayAdapter<String>(getActivity(),R.layout.spinner_item,semesters);
		semesterPickerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		semesterPicker.setAdapter(semesterPickerAdapter);
		
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
		protected String doInBackground(String... params) {
			//String url = "http://vit0.com/xmutEDSData/course1.json";
			String json = HttpClientFactory.normalGet(yearUrl);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			processJson(result);
			this.mpDialog.dismiss();
		}
		
	}
	
	public int processJson(String json) {
		courseContainer.removeAllViews();
		int len = 0;
		try {
			L.d("解析所有课程", json);
			JSONArray jsonArray;
			jsonArray = new JSONArray(json);
			len = jsonArray.length();
			
			for (int i=0; i<len; i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Course course = new Course(jsonObject);
				
				//将dp转换成px，因为设置RelativeLayout.LayoutParams 的margin只能传入px
				int _60dp = dpToPx(getActivity(), 60);
				int marginDp = dpToPx(getActivity(), 1.5f);
				
				int colWidth = courseContainer.getWidth() / 7;
				int xStartOffset = (Integer.valueOf(course.getLessonsBegin())- 1) * (_60dp+marginDp);
				int xEndOffset = (Integer.valueOf(course.getLessonsEnd())- 1) * (_60dp+marginDp);
				int yStartOffset = (Integer.valueOf(course.getDayOfWeek()) - 1) * colWidth;
				
				LinearLayout ll = (LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.layout_onecourse, null);
				
				TextView courseName = (TextView) ll.findViewById(R.id.courseName);
//				TextView detail = (TextView) ll.findViewById(R.id.detail);
//				TextView teacher = (TextView) ll.findViewById(R.id.teacher);
				TextView room = (TextView) ll.findViewById(R.id.room);
				
				courseName.setText(course.getCourseName());
				//detail.setText(course.getDetail());
				//teacher.setText(course.getTeacher());
				room.setText(course.getRoom());
				
				RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(colWidth,xEndOffset-xStartOffset+_60dp);
				params.leftMargin = yStartOffset;
				params.topMargin = xStartOffset;
				
				courseContainer.addView(ll, params);
				
				ll.setOnClickListener(new CourseOnClickListener(course));
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return len;
	}
	
	class CourseOnClickListener implements OnClickListener {
		Course course;
		public CourseOnClickListener(Course course) {
			this.course = course;
		}
		
		@Override
		public void onClick(View v) {
			String content = "课程名："+course.getCourseName() + "\n" +
					"上课时间：" + course.getDetail() + "\n" +
					"授课教师：" + course.getTeacher() + "\n" +
					"地点：" + course.getRoom();
					DialogUtil.showMsg(getActivity(), content);
		}
		
	}
	
	public int dpToPx(Context context, float dp) {
		return (int) TypedValue.applyDimension(
		        TypedValue.COMPLEX_UNIT_DIP,
		        dp, 
		        context.getResources().getDisplayMetrics()
		);
	}
}
