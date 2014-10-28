package com.lipberry.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.viewpagerindicator.TabPageIndicator;



@SuppressLint({ "NewApi", "DefaultLocale" })
public class FragmentHomeHolder extends Fragment {

	private static final String[] CONTENT = new String[] {"جديد الكل","جديد من أتابعهم"};
	HomeTabFragment parent;
	ViewGroup view;
	ViewPager pager;
	TabPageIndicator indicator;
	FragmentPagerAdapter adapter;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("onCreate", "homeonCreate");

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_home_holder,
				container, false);
		view=container;
		pager = (ViewPager) v.findViewById(R.id.pager);
		indicator = (TabPageIndicator)v.findViewById(R.id.indicator);
		 adapter = new PostRetreiveAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		indicator.setViewPager(pager,1);
		//pager.setCurrentItem(0);
		Log.e("current item", pager.getCurrentItem()+"");
		
	
		Log.e("onCreateView", "homeonCreateView");

		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
//		if(Constants.writetopicsuccess){
//			indicator.setViewPager(pager,0);
//			Constants.writetopicsuccess=false;
//		}
	
		
		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.GONE);
		( (HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
		( (HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.topbar_new_article));
		Log.e("onResume", "homeonResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e("onStart", "homeonStart");

	}
	@Override
	public void onPause() {
		super.onPause();
		
	}
	class PostRetreiveAdapter extends FragmentPagerAdapter {
		@Override
		public Parcelable saveState() {
			return super.saveState();
		}
		public PostRetreiveAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int position) {
			if (position == 0) {
				FragmentMyCountriesPost newfrag=new FragmentMyCountriesPost();
				FragmentMyCountriesPost.parent=parent;
				return newfrag;
			} else if (position == 1) {
				FragmentMyFollwerPost newfrag=new FragmentMyFollwerPost();
				FragmentMyFollwerPost.parent=parent;
				return newfrag;
			} else
				return null;

		}
		@Override
		public CharSequence getPageTitle(int position) {
			return CONTENT[position % CONTENT.length].toUpperCase();
		}
		@Override
		public int getCount() {
			Log.e("count", ""+CONTENT.length);
			return CONTENT.length;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}


}
