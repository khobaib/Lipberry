package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.CustomAdapterForMenu;
import com.lipberry.utility.Constants;

@SuppressLint("NewApi")
public class FragmentMenu extends Fragment {
	MenuTabFragment parent;
	String[] menuarray;
	ListView list_menu_item;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_menu, container, false);
		list_menu_item = (ListView) v.findViewById(R.id.list_menu_item);
		
		return v;
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		menuarray = getActivity().getResources().getStringArray(R.array.menuarray);
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(menuarray));
		CustomAdapterForMenu adapter1 = new CustomAdapterForMenu(getActivity(), list);
		list_menu_item.setAdapter(adapter1);
		setlistviewonitemclick();
		if (Constants.MESSAGESETTINGSTATE) {
			parent.startFragmentSetting();
		}
		
	}

	@Override
	public void onPause() {
		super.onPause();
		((HomeActivity) getActivity()).topBar.setVisibility(View.VISIBLE);
	}

	@Override
	public void onResume() {
		super.onResume();
		((HomeActivity) getActivity()).topBar.setVisibility(View.GONE);
		((HomeActivity) getActivity()).backbuttonoftab.setVisibility(View.GONE);
		((HomeActivity) getActivity()).welcome_title.setText(getResources().getString(R.string.txt_menu));
	}

	public void setlistviewonitemclick() {
		list_menu_item.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				switch (position) {
				case 0:
					((HomeActivity) getActivity()).mTabHost.setCurrentTab(4);
					break;
				case 1:
					parent.startFragmentSetting();
					break;
				case 2:
					parent.startFragmentFindamember();
					break;
				case 3:
					((HomeActivity) getActivity()).mTabHost.setCurrentTab(0);
					break;
				case 4:
					parent.startFragmentMyProfile();
					break;
				case 5:
					onShareClick();
					break;

				default:
					break;
				}
			}
		});
	}

	public void onShareClick() {
		Intent emailIntent = new Intent();
		emailIntent.setAction(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "contact@lipberry.com" });
		emailIntent.setType("message/rfc822");
		PackageManager pm = getActivity().getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		sendIntent.setType("message/rfc822");
		Intent openInChooser = Intent.createChooser(emailIntent,
				getActivity().getResources().getString(R.string.txt_send_mail));

		List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
		for (int i = 0; i < resInfo.size(); i++) {
			ResolveInfo ri = resInfo.get(i);
			String packageName = ri.activityInfo.packageName;
			if (packageName.contains("android.email")) {
				emailIntent.setPackage(packageName);
			}
			
		}
		LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);
		openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		startActivity(openInChooser);
	}

}
