package com.vito.xmutems.fragment.score;

import java.util.List;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.vito.xmutems.R;
import com.vito.xmutems.adapter.ScoreListAdapter;
import com.vito.xmutems.domain.Score;
import com.vito.xmutems.utils.DialogUtil;
import com.vito.xmutems.utils.HttpClientFactory;


public class ScoreFragment extends Fragment {
	Spinner yearPicker;
	Spinner semesterPicker;
	Button searchBtn;
	ListView scoreList;
	ScoreListAdapter adapter;
	
	String[] urls = {"http://vit0.com/xmutEDSData/score1.json",
			"http://vit0.com/xmutEDSData/score2.json",
			"http://vit0.com/xmutEDSData/score3.json",
			"http://vit0.com/xmutEDSData/score4.json"};
	String[] years={"2009-2010","2010-2011","2011-2012","2012-2013"};
	String[] semesters={"1","2"};
	String yearUrl;
	String semester;
	
	List<Score> scores;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.score_layout, null);
		initView(view);
		initEvent();
		initData();
		return view;
	}
	
	private void initView(View view) {
		yearPicker = (Spinner) view.findViewById(R.id.yearPicker);
		semesterPicker = (Spinner) view.findViewById(R.id.semesterPicker);
		searchBtn = (Button) view.findViewById(R.id.searchBtn);
		scoreList = (ListView) view.findViewById(R.id.scoreList);
	}
	
	private void initEvent() {
		yearPicker.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View v,
					int position, long id) {
				yearUrl = urls[position];
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
			
		});
		
		semesterPicker.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				semester = semesters[arg2];
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
		
		scoreList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View v, int position,
					long id) {
				Score score = (Score) scoreList.getAdapter().getItem(position);
				String content = "学年："+score.getYear()+ "\n" +
						"学期："+score.getSemester()+ "\n" +
						"课程名："+score.getName() + "\n" +
						"分数：" + score.getScore() + "\n" +
						"学分：" + score.getCredits() + "\n" +
						"绩点：" + score.getGradePoint() + "\n" +
						"课程性质：" + score.getType();
						DialogUtil.showMsg(getActivity(), content);
			}
		});
	}
	
	private void initData() {
		adapter = new ScoreListAdapter(getActivity());
		scoreList.setAdapter(adapter);
		
		yearUrl = urls[0];
		semester = semesters[0];
		
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
			adapter.processJson(result, semester);
			this.mpDialog.dismiss();
		}
		
	}
}
