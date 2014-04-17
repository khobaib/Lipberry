package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.viewpagerindicator.TabPageIndicator;



@SuppressLint("NewApi")
public class FragmentHomeHolder extends Fragment {

	private static final String[] CONTENT = new String[] {"جديد الكل","جديد من أتابعهم"};
	HomeTabFragment parent;
	ViewGroup view;
	ViewPager pager;
	TabPageIndicator indicator;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.e("onCreate", "onCreate");

	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_home_holder,
				container, false);
		view=container;
		pager = (ViewPager) v.findViewById(R.id.pager);
		indicator = (TabPageIndicator)v.findViewById(R.id.indicator);
		FragmentPagerAdapter adapter = new PostRetreiveAdapter(getChildFragmentManager());
		pager.setAdapter(adapter);
		indicator.setViewPager(pager);
		Log.e("onCreateView", "onCreateView");

		return v;
	}
	@Override
	public void onResume() {
		super.onResume();
		( (HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
		( (HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.topbar_new_article));
		Log.e("onResume", "onResume");
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.e("onStart", "onStart");

	}
	@Override
	public void onPause() {
		super.onPause();
		Log.e("onPause", "onPause");
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
				newfrag.parent=parent;
				return newfrag;
			} else if (position == 1) {
				FragmentMyFollwerPost newfrag=new FragmentMyFollwerPost();
				newfrag.parent=parent;
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
			return CONTENT.length;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}


}
