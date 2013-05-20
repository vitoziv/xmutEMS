package com.vito.xmutems;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class BaseFragment extends Fragment {
	private int layoutRes;
	
	public BaseFragment(int layoutRes) {
		this.layoutRes = layoutRes;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(layoutRes, null);	
		return view;
	}
}
