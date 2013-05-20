package com.vito.xmutems;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vito.xmutems.utils.CacheProvider;
import com.vito.xmutems.utils.ImageDownloader;

public class MenuFragment extends Fragment {
	ImageView userPortrait;
	TextView stuName;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.menu_layout, null);
		stuName = (TextView) view.findViewById(R.id.stuName);
		String stuNameText = CacheProvider.get(getActivity(), "stuName").toString();
		stuName.setText(stuNameText);

		userPortrait = (ImageView) view.findViewById(R.id.headPortrait);
		ImageDownloader imageDownloader = new ImageDownloader(getActivity());
		String stuNo = CacheProvider.get(getActivity(), "stuNo").toString();
		imageDownloader.download("http://jxgl.xmut.edu.cn/readimagexs.aspx?xh=" + stuNo, userPortrait);
		
		return view;
	}
	
}
