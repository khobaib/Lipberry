package com.lipberry.fragment;

import java.util.Stack;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.utility.Constants;





import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewParent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;
import android.widget.Toast;

public class WriteTopicTabFragment extends TabFragment{
	protected Stack<Fragment> backEndStack;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backEndStack = new Stack<Fragment>();
		FragmentWriteTopic initialFragment = new FragmentWriteTopic();
		initialFragment.parent = this;
		backEndStack.push(initialFragment);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewParent parent = (ViewParent) container.getParent();
		if (parent instanceof View) {
			((TextView) ((View) parent).findViewById(R.id.welcome_title))
			.setText(this.getTag());
		}
		View v = inflater.inflate(R.layout.fragment_tab3, container, false);
		return v;
	}

	public void onStart( ) {
		Fragment fragment = backEndStack.peek();
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		super.onStart();
	}

	public void startfragmenthome() {
		FragmentWriteTopic newFragment = new FragmentWriteTopic ();
		newFragment.parent = this;
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void clearr(){
		backEndStack.pop();
	}
	@Override
	public void onBackPressed() {
		((HomeActivity) getActivity()).mTabHost.setCurrentTab(Constants.GOTABFROMWRITETOPIC);
//		if (backEndStack.size()==1) {
//			((HomeActivity) getActivity()).close();
//		}
//		else {
//			if (backEndStack.size()==1) {
//				((HomeActivity) getActivity()).close();
//			} else {
//				backEndStack.pop();
//				Fragment frg = backEndStack.peek();
//				Log.d("1", "4");
//				FragmentManager fragmentManager = getChildFragmentManager();
//				FragmentTransaction fragmentTransaction = fragmentManager
//						.beginTransaction();
//				fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
//			}
//		}
	}
}
