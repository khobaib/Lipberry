package com.lipberry.fragment;

import java.util.Stack;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.model.Article;
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

public class HomeTabFragment extends TabFragment{
	protected Stack<Fragment> backEndStack;
	protected Stack<Integer> trackcallhome;
	Bundle sBundle;
	int callstatefromtab=0;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		trackcallhome=new Stack<Integer>();
		backEndStack = new Stack<Fragment>();
		FragmentHomeHolder initialFragment = new FragmentHomeHolder();
		initialFragment.parent = this;
		trackcallhome.push(0);
		backEndStack.push(initialFragment);
		sBundle=savedInstanceState;
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.e("calling", "calling Tab");
		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.GONE);

		super.onPause();
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
		Constants.GOTABFROMWRITETOPIC=4;
		if(Constants.GOMEMBERSTATEFROMINTERACTION){
			startMemberFragment(2);
			Constants.GOMEMBERSTATEFROMINTERACTION=false;
		}
		else if(Constants.GOMEMBERSTATEFROMSETTING){
			startMemberFragment(5);
			Constants.GOMEMBERSTATEFROMSETTING=false;
		}
		else if(Constants.GOARTCLEPAGE){
			Constants.GOARTCLEPAGE=false;
			FragmentArticleDetailsFromInteraction(Constants.INTER_ARTICLE_ID);
		}
		else if(Constants.GOARTCLEPAGEFROMMEMBER){
			Constants.GOARTCLEPAGEFROMMEMBER=false;
			startFragmentArticleDetailsFromHome(Constants.ARTICLETOSEE,5);
		}
		trackcallhome.peek();
		Fragment fragment = backEndStack.peek();
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		super.onStart();
	}
	

	public void FragmentArticleDetailsFromInteraction(String article_id) {
		FragmentArticleDetailsFromInteraction newFragment = new FragmentArticleDetailsFromInteraction (article_id);
		newFragment.parent = this;
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackcallhome.push(2);
		fragmentTransaction.commitAllowingStateLoss();
	}

	public void StartFragmentSendMessageFormHome(String nickname,String username) {
		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.GONE);

		FragmentSendMessageFormHome newFragment = new FragmentSendMessageFormHome (nickname,username);
		newFragment.parent = this;
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackcallhome.push(0);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentArticleDetailsFromHome(Article article) {
		FragmentArticleDetailsFromHome newFragment = new FragmentArticleDetailsFromHome ();
		newFragment.setArticle(article);
		newFragment.parent = this;
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackcallhome.push(0);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentArticleDetailsFromHome(Article article, int state) {
		FragmentArticleDetailsFromHome newFragment = new FragmentArticleDetailsFromHome ();
		newFragment.setArticle(article);
		newFragment.parent = this;
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackcallhome.push(state);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startMenufragment() {
		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.GONE);

		FragmentHomeHolder newFragment = new FragmentHomeHolder ();
		newFragment.parent = this;
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackcallhome.push(0);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void startMemberFragment(int state) {
		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.GONE);

		FragmentMemberFromHome newFragment = new FragmentMemberFromHome(state, Constants.userid);
		newFragment.parent = this;
		FragmentManager fragmentManager = getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackcallhome.push(state);
		fragmentTransaction.commitAllowingStateLoss();
	}
	public void clearr(){
		trackcallhome.pop();
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
				int callstate=trackcallhome.pop();
				backEndStack.pop();
				if(callstate==0){
					Fragment frg = backEndStack.peek();
					FragmentManager fragmentManager = getChildFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
				}
				else{
					Fragment frg = backEndStack.peek();
					FragmentManager fragmentManager = getChildFragmentManager();
					FragmentTransaction fragmentTransaction = fragmentManager
							.beginTransaction();
					fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
					((HomeActivity)getActivity()).mTabHost.setCurrentTab(callstate);
				}
				
			}

		}
	}
}
