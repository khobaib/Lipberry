package com.lipberry.fragment;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.model.SingleMember;
import com.lipberry.settings.FragmentContactUs;
import com.lipberry.settings.FragmentFindamember;
import com.lipberry.settings.FragmentImageSetting;
import com.lipberry.settings.FragmentMessageSetting;
import com.lipberry.settings.FragmentProfileSetting;
import com.lipberry.settings.FragmentSendMessageFormMenuTab;
import com.lipberry.settings.FragmentSingleMember;
import com.lipberry.utility.Constants;
public class MenuTabFragment extends TabFragment{
	protected Stack<Fragment> backEndStack;
//	private static HomeActivity homeActivity;
	FragmentManager fragmentManager;
	MenuTabFragment menutabFragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backEndStack = new Stack<Fragment>();
		FragmentMenu initialFragment = new FragmentMenu();
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
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		((HomeActivity)getActivity()).mTabHost.AttachMenuFragment(menutabFragment);
	}
	public void restasrtTab(){
		backEndStack = new Stack<Fragment>();
		backEndStack.clear();
		startMenufragment();
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
//		homeActivity=(HomeActivity) getActivity();
		menutabFragment=this;
		fragmentManager=menutabFragment.getChildFragmentManager();
		
	}

	public void onStart( ) {
		Constants.STATECALLPDFORMENU=true;
		if(!Constants.IMAGEPAGECALLED){
			backEndStack.clear();
			startMenufragment();
		}
		
		else{
			Constants.IMAGEPAGECALLED=false;
		}
		Constants.GOT_AB_FROM_WRITE_TOPIC=5;
		Fragment fragment = backEndStack.peek();
		if(fragmentManager==null){
			fragmentManager = menutabFragment.getChildFragmentManager();;
		}
			
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		super.onStart();
	}
	
	public void StartFragmentSendMessageFormMenu(String nickname,String username) {
		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.GONE);

		FragmentSendMessageFormMenuTab newFragment = new FragmentSendMessageFormMenuTab ();
		newFragment.setFragmentSendMessageFormMenuTab(nickname,username);
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	
	//FragmentContactUs
	public void startFragmentContactUs() {
		FragmentContactUs newFragment = new FragmentContactUs();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();;
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentFindamember() {
		FragmentFindamember newFragment = new FragmentFindamember();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();;
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentMessageSetting() {
		FragmentMessageSetting newFragment = new FragmentMessageSetting();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentMyProfile() {
		FragmentMyProfile newFragment = new FragmentMyProfile();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentSingleMember(String member_id) {
		FragmentSingleMember newFragment = new FragmentSingleMember( member_id);
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager =menutabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	//FragmentSingleMember
	
	public void startFragmentImageSetting(SingleMember singleMember,FragmentProfileSetting lisenar) {
		FragmentImageSetting newFragment = new FragmentImageSetting();
		newFragment.setFragmentImageSetting(singleMember,lisenar);
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	public void startFragmentProfileSetting() {
		
		FragmentProfileSetting newFragment = new FragmentProfileSetting();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	
	public void startMenufragment() {
		FragmentMenu newFragment = new FragmentMenu();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		fragmentTransaction.commitAllowingStateLoss();
	}
	
	public void startFragmentSetting() {
		FragmentSetting newFragment = new FragmentSetting();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = menutabFragment.getChildFragmentManager();
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
			
		}
		else {
			if (backEndStack.size()==1) {
				getActivity().finish();
			} else {
				backEndStack.pop();
				Fragment frg = backEndStack.peek();
				if(fragmentManager==null)
					fragmentManager =menutabFragment.getChildFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager
						.beginTransaction();
				fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
			}

		}
	}
}
