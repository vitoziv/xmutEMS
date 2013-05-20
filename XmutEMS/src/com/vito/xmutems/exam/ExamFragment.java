package com.vito.xmutems.exam;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.huewu.pla.lib.MultiColumnListView;
import com.vito.xmutems.R;
import com.vito.xmutems.adapter.ExamListAdapter;
import com.vito.xmutems.utils.HttpClientFactory;

public class ExamFragment extends Fragment {
	Spinner yearPicker;
	Spinner semesterPicker;
	Button searchBtn;
	//GridView examGridView;
	ExamListAdapter adapter;
	MultiColumnListView listView;
	
	final String[][] urls = {
			{"http://vit0.com/xmutEDSData/exam2009-2010-1.json",
			"http://vit0.com/xmutEDSData/exam2010-2011-1.json",
			"http://vit0.com/xmutEDSData/exam2011-2012-1.json",
			"http://vit0.com/xmutEDSData/exam2012-2013-1.json"},
			{"http://vit0.com/xmutEDSData/exam2009-2010-2.json",
			"http://vit0.com/xmutEDSData/exam2010-2011-2.json",
			"http://vit0.com/xmutEDSData/exam2011-2012-2.json",
			"http://vit0.com/xmutEDSData/exam2012-2013-2.json"}
			};
	
	final String[] years={"2009-2010","2010-2011","2011-2012","2012-2013"};
	final String[] semesters={"1","2"};
	String yearUrl;
	Integer semesterPosition;
	Integer yearPosition;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.exam_layout, null);
		initView(view);
		initEvent();
		initData();
		return view;
	}
	
	void initView(View view) {
		yearPicker = (Spinner) view.findViewById(R.id.yearPicker);
		semesterPicker = (Spinner) view.findViewById(R.id.semesterPicker);
		searchBtn = (Button) view.findViewById(R.id.searchBtn);
		listView = (MultiColumnListView) view.findViewById(R.id.mListView);
		//examGridView = (GridView) view.findViewById(R.id.examGridView);
	}
	
	void initEvent() {
		yearPicker.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				yearPosition = position;
				yearUrl = urls[semesterPosition][yearPosition];
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
				yearUrl = urls[semesterPosition][yearPosition];
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
	
	void initData() {
		adapter = new ExamListAdapter(getActivity());
		//examGridView.setAdapter(adapter);
		listView.setAdapter(adapter);
		yearPosition = 0;
		semesterPosition = 0;
		yearUrl = urls[semesterPosition][yearPosition];
		
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
			String json = HttpClientFactory.normalGet(yearUrl);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			adapter.processJson(result);
			this.mpDialog.dismiss();
		}
		
	}
	
}
