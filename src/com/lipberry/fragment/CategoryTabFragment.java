package com.lipberry.fragment;

import java.util.Stack;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.model.Article;
import com.lipberry.model.ArticleDetails;
import com.lipberry.model.ArticleList;
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

public class CategoryTabFragment extends TabFragment{
	protected Stack<Fragment> backEndStack;
	private static HomeActivity homeActivity;
	FragmentManager fragmentManager ;
	CategoryTabFragment cat_tabfragment;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		backEndStack = new Stack<Fragment>();
		FragmentCategories initialFragment = new FragmentCategories();
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
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
		cat_tabfragment=this;
		fragmentManager=cat_tabfragment.getChildFragmentManager();
	}
	public void onStart( ) {
		Constants.GOT_AB_FROM_WRITE_TOPIC=3;

		if(Constants.catgeory){
			startFragmentSubCategoriesList(Constants.caturl,
					Constants.caname,Constants.articlelist);
			Constants.catgeory=false;
		}
		Fragment fragment = backEndStack.peek();
		if(fragmentManager==null)
			fragmentManager=cat_tabfragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		super.onStart();
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {

		super.onActivityCreated(savedInstanceState);
		homeActivity=(HomeActivity) getActivity();
	}
	public void StartFragmentSendMessageFormHome(String nickname,String username) {
		FragmentSendMessageFormCategory newFragment = new FragmentSendMessageFormCategory ();
		newFragment.setFragmentSendMessageFormCategory(nickname,username);
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager=cat_tabfragment.getChildFragmentManager();	
		FragmentTransaction fragmentTransaction = fragmentManager
			.beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.addToBackStack(null);
			backEndStack.push(newFragment);
			fragmentTransaction.commitAllowingStateLoss();
	}

	public void startFragmentSubCategoriesList( String url,String catname,ArticleList article) {
		FragmentSubCategoriesList newFragment = new FragmentSubCategoriesList ();
		Constants.caturl=url;
		Constants.caname=catname;
		newFragment.setUrl(url,catname,article);
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager=cat_tabfragment.getChildFragmentManager();	
		FragmentTransaction fragmentTransaction = fragmentManager
			.beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.addToBackStack(null);

			backEndStack.push(newFragment);

			fragmentTransaction.commitAllowingStateLoss();
	}


	public void startFragmentArticleDetails(Article article,ArticleDetails articledetails) {
		FragmentArticleDetailsFromCategory newFragment = new FragmentArticleDetailsFromCategory ();
		newFragment.setArticle(article,articledetails);
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager=cat_tabfragment.getChildFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
			.beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.addToBackStack(null);
			backEndStack.push(newFragment);
			fragmentTransaction.commitAllowingStateLoss();
	}
	public void startFragmentMemberFromCategories() {
		FragmentMemberFromCategories newFragment = new FragmentMemberFromCategories ();
		newFragment.parent = this;
		if(fragmentManager==null)
			fragmentManager = cat_tabfragment.getChildFragmentManager();
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
			homeActivity.close();
		}
		else {
			if (backEndStack.size()==1) {
				homeActivity.close();
			} else {
				backEndStack.pop();
				Fragment frg = backEndStack.peek();
				if(fragmentManager==null)
					fragmentManager=cat_tabfragment.getChildFragmentManager();		
				FragmentTransaction fragmentTransaction = fragmentManager
					.beginTransaction();
					fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
			}

		}
	}
}
