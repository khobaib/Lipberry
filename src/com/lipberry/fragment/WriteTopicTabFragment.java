package com.lipberry.fragment;

import java.io.File;
import java.util.Stack;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.utility.Constants;





import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
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
	private static HomeActivity homeActivity;
	FragmentManager fragmentManager;
	WriteTopicTabFragment writeTopicTabFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backEndStack = new Stack<Fragment>();
		FragmentWriteTopic initialFragment = new FragmentWriteTopic();
		initialFragment.parent = this;
		backEndStack.push(initialFragment);
		homeActivity=(HomeActivity)getActivity();
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
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		homeActivity=(HomeActivity)getActivity();
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		homeActivity=(HomeActivity) getActivity();
		writeTopicTabFragment=this;
		fragmentManager=writeTopicTabFragment.getChildFragmentManager();
	}
	public void onStart( ) {
//		Log.e("Calling", "Calling");
//		File file = new File(Environment.getExternalStorageDirectory().toString() + "/Lipberrythumb");
//		if(file.exists()){
//			deleteDirectory(file);
//		}
//		file = new File(Environment.getExternalStorageDirectory().toString() + "/Lipberryfinal");
//		if(file.exists()){
//			deleteDirectory(file);
//		}
//		backEndStack.clear();
//		backEndStack = new Stack<Fragment>();
//		FragmentWriteTopic initialFragment = new FragmentWriteTopic();
//		initialFragment.parent = this;
//		backEndStack.push(initialFragment);
		Fragment fragment = backEndStack.peek();
		if(fragmentManager==null)
			fragmentManager = writeTopicTabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		super.onStart();
	}

	public void startfragmenthome() {
		FragmentWriteTopic newFragment = new FragmentWriteTopic ();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = writeTopicTabFragment.getChildFragmentManager();
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
	public void startWriteFragment() {
		backEndStack.clear();
		backEndStack = new Stack<Fragment>();
		FragmentWriteTopic initialFragment = new FragmentWriteTopic();
		initialFragment.parent = this;
		backEndStack.push(initialFragment);
	}
	@Override
	public void onBackPressed() {
		homeActivity.mTabHost.setCurrentTab(Constants.GOT_AB_FROM_WRITE_TOPIC);
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
	public static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
}
