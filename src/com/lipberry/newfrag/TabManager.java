package com.lipberry.newfrag;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

import com.lipberry.R;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

public class TabManager implements TabHost.OnTabChangeListener {
	private final FragmentActivity mActivity;
	private final TabHost mTabHost;
	private final int mContainerId;
	private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
	private final HashMap<String, Stack<TabInfo>> mTagMap = new HashMap<String, Stack<TabInfo>>();
	public TabInfo mLastTab;
	String mCurrentTab;

	class DummyTabFactory implements TabHost.TabContentFactory {
		private final Context mContext;

		public DummyTabFactory(Context context) {
			mContext = context;
		}

		@Override
		public View createTabContent(String tag) {
			View v = new View(mContext);
			v.setMinimumWidth(0);
			v.setMinimumHeight(0);
			return v;
		}
	}

	public TabManager(FragmentActivity activity, TabHost tabHost,
			int containerId) {
		mActivity = activity;
		mTabHost = tabHost;
		mContainerId = containerId;
		mTabHost.setOnTabChangedListener(this);
	}

	public void addTab(String tag, String initFrg, int res_id) {
		TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tag);
		tabSpec.setContent(new DummyTabFactory(mActivity));
		// TabInfo info = new TabInfo(tag, clss, args);
		View indicator = LayoutInflater.from(mActivity).inflate(
				R.layout.indicator_view, null);
		TextView tv = (TextView) indicator.findViewById(R.id.tv);
		ImageView iv = (ImageView) indicator.findViewById(R.id.iv);
		tv.setText(tag);
		iv.setImageResource(res_id);
		tabSpec.setIndicator(indicator);
		Stack<TabInfo> stack = new Stack<TabInfo>();
		stack.push(new TabInfo(initFrg, mTabs.get(initFrg).clss, mTabs.get(initFrg).args));
		mTagMap.put(tag, stack);
		mTabHost.addTab(tabSpec);
	}

	public void changeTab(String tab, String initFrg) {
		while (mActivity.getSupportFragmentManager().getBackStackEntryCount() > 0) {
			mActivity.getSupportFragmentManager().popBackStackImmediate();
		}
		Stack<TabInfo> stack = mTagMap.get(tab);
		for (TabInfo tag : stack) {
			mTabs.get(tag.tag).fragment = null;
			tag.fragment = null;
		}
		stack.clear();
		stack.push(new TabInfo(initFrg, mTabs.get(initFrg).clss, mTabs.get(initFrg).args));
//		switchFragment(mLastTab,false);
	}

	public void addTab(String _tag, Class<?> _class, Bundle _args) {
		mTabs.put(_tag, new TabInfo(_tag, _class, _args));
	}

	public void switchFragment(TabInfo newTab, boolean stack) {
		mTabs.put(newTab.tag, newTab);
		FragmentTransaction ft = mActivity.getSupportFragmentManager()
				.beginTransaction();
		if (newTab != null) {
//			if (newTab.fragment == null || newTab.fragment.isDetached()) {
//				newTab.fragment = Fragment.instantiate(mActivity,
//						newTab.clss.getName(), newTab.args);
//				ft.replace(mContainerId, newTab.fragment, newTab.tag);
//			} else {
//				ft.replace(mContainerId, newTab.fragment, newTab.tag);
//			}
			newTab.fragment = Fragment.instantiate(mActivity,
					newTab.clss.getName(), newTab.args);
			ft.replace(mContainerId, newTab.fragment, newTab.tag);
		}
		if (stack) {
			ft.addToBackStack(newTab.tag);
		}
		if (!mTagMap.get(mCurrentTab).lastElement().tag.equals(newTab.tag)) {
			mTagMap.get(mCurrentTab).push(newTab.copy());
		}else{
			mTagMap.get(mCurrentTab).pop();
			mTagMap.get(mCurrentTab).push(newTab.copy());
		}
		mLastTab = newTab;
		ft.commit();
	}

	@Override
	public void onTabChanged(String tabId) {
		TabInfo info = mTagMap.get(tabId).lastElement();
//		TabInfo newTab = mTabs.get(info.tag);
		mCurrentTab = tabId;
		switchFragment(info, false);
	}

	public void save(Bundle savedInstanceState) {
		if (savedInstanceState == null)
			return;
		Collection<TabInfo> values = mTabs.values();
		Iterator<TabInfo> iter = values.iterator();
		while (iter.hasNext()) {
			TabInfo ti = iter.next();
			savedInstanceState.putParcelable(ti.tag, ti);
		}
		if (mLastTab != null && mLastTab.tag != null) {
			savedInstanceState.putString("current_frg", mLastTab.tag);
		}
		savedInstanceState
				.putString("current_tab", mTabHost.getCurrentTabTag());
	}

	public void load(Bundle savedInstanceState) {
		if (savedInstanceState == null)
			return;
		Set<String> keys = savedInstanceState.keySet();
		Iterator<String> iter_key = keys.iterator();
		while (iter_key.hasNext()) {
			String key = iter_key.next();
			Object obj = savedInstanceState.getParcelable(key);
			if (obj != null && obj instanceof TabInfo) {
				mTabs.put(key, (TabInfo) obj);
			}
		}
		String currentFrg = savedInstanceState.getString("current_frg");
		String currentTab = savedInstanceState.getString("current_tab");
		if (currentTab != null) {
			mTabHost.setCurrentTabByTag(currentTab);
		}
		if (currentFrg != null) {
			switchFragment(mTabs.get(currentFrg), false);
		}
	}

	public TabInfo getTab(String key) {
		return mTagMap.get(key).lastElement();
	}

	public TabInfo getTabFromTag(String key) {
		return mTabs.get(key);
	}

	public void pop() {
		mTagMap.get(mCurrentTab).pop();
	}

}