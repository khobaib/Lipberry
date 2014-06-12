package com.lipberry.fragment;

import java.util.Stack;

import com.lipberry.HomeActivity;

import android.support.v4.app.Fragment;

public abstract class TabFragment extends Fragment {
	@Override
	public void onResume(){
		((HomeActivity)getActivity()).activeFragment=this;
		super.onResume();
	}
	public abstract void onBackPressed();
}
