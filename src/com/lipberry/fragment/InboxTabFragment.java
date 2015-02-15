package com.lipberry.fragment;

import java.util.Stack;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.model.ThreadMessageList;
import com.lipberry.utility.Constants;





import android.app.Activity;
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

public class InboxTabFragment extends TabFragment{

	protected Stack<Fragment> backEndStack;
	private  HomeActivity homeActivity;
	FragmentManager fragmentManager;
	InboxTabFragment inboxTabFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backEndStack = new Stack<Fragment>();
		FragmentInbox initialFragment = new FragmentInbox();
		initialFragment.parent = this;
		backEndStack.push(initialFragment);
		Log.e("tagR", "3");

	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		Log.e("tagR", "4");

		homeActivity=(HomeActivity) getActivity();
		inboxTabFragment=this;
		fragmentManager=inboxTabFragment.getChildFragmentManager();
		Log.e("tagR", "5");

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("tagR", "6");

		ViewParent parent = (ViewParent) container.getParent();
		if (parent instanceof View) {
			((TextView) ((View) parent).findViewById(R.id.welcome_title))
			.setText(this.getTag());
		}
		Log.e("tagR", "7");

		View v = inflater.inflate(R.layout.fragment_tab3, container, false);
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		homeActivity=(HomeActivity)getActivity();
	}
	public void onStart( ) {
		Log.e("tagR", "8");

		Constants.GOT_AB_FROM_WRITE_TOPIC=1;
		Log.e("tagR", "9");

		Fragment fragment = backEndStack.peek();
		Log.e("tagR", "10");

		if(fragmentManager==null){
			Log.e("tagR", "11");

			fragmentManager = inboxTabFragment.getChildFragmentManager();
			Log.e("tagR", "12");

		}
		Log.e("tagR", "13");
	
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Log.e("tagR", "14");

		fragmentTransaction.replace(R.id.tab3Content, fragment);
		Log.e("tagR", "15");

		fragmentTransaction.commitAllowingStateLoss();
		Log.e("tagR", "16");

		super.onStart();
	}
	
	public void StartFragmentMessageSettingFromInbo() {
		FragmentMessageSettingFromInbo newFragment = new FragmentMessageSettingFromInbo();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = inboxTabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startMessagefragment(ThreadMessageList messagelist,String messageid,boolean read_flag,int from) {
		FragmentMessage newFragment=new FragmentMessage();
		newFragment.SetFragmentMessage(messagelist, messageid, read_flag,from);
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = inboxTabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	public void startfragmentSendMessage() {
		FragmentSendMessage newFragment = new FragmentSendMessage();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager =inboxTabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}

	public void startInboxfragment() {
		FragmentInbox newFragment = new FragmentInbox();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = inboxTabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentNewMessage() {
		FragmentSentMessage newFragment = new FragmentSentMessage();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = inboxTabFragment.getChildFragmentManager();
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
		if (backEndStack.size()==1) {
			((HomeActivity) getActivity()).close();
		}
		else {
			if (backEndStack.size()==1) {
				((HomeActivity) getActivity()).close();
			} else {
				backEndStack.pop();
				Fragment frg = backEndStack.peek();
				if(fragmentManager==null)
					fragmentManager = inboxTabFragment.getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
			}

		}
	}
}
