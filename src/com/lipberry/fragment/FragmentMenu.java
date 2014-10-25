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
		menuarray = getActivity().getResources().getStringArray(R.array.menuarray);

		// int layout=R.layout.custom_textview;
		ArrayList<String> list = new ArrayList<String>(Arrays.asList(menuarray));

		CustomAdapterForMenu adapter1 = new CustomAdapterForMenu(getActivity(), list);
		// ArrayAdapter adapter = new ArrayAdapter<String>(
		// getActivity(),
		// R.layout.custom_textview,
		// menuarray);
		//
		list_menu_item.setAdapter(adapter1);
		setlistviewonitemclick();
		if (Constants.MESSAGESETTINGSTATE) {
			parent.startFragmentSetting();
		}
		return v;
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

					// Intent emailIntent = new
					// Intent(android.content.Intent.ACTION_SEND);
					// emailIntent.setType("jpeg/image");
					// emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
					// new String[] { "" });
					//
					//
					// emailIntent.setType("text/plain");
					// emailIntent.putExtra(Intent.EXTRA_EMAIL , new
					// String[]{"contact@lipberry.com"});
					//
					// //emailIntent.putExtra(Intent.EXTRA_TEXT,
					// info.getText().toString());
					// startActivity(Intent.createChooser(emailIntent,
					// "Send mail..."));

					// Intent intent = new Intent(Intent.ACTION_SEND);
					// intent.setType("text/html");
					// intent.putExtra(Intent.EXTRA_EMAIL,
					// "contact@lipberry.com");
					// intent.putExtra(Intent.EXTRA_SUBJECT, "Lipberry");
					// intent.putExtra(Intent.EXTRA_TEXT,
					// "I'm email body. Email test");
					//
					// startActivity(Intent.createChooser(intent,
					// "Send Email"));
					// parent.startFragmentContactUs();
					break;

				default:
					break;
				}
			}
		});
	}

	public void onShareClick() {
		// Resources resources = getResources();
		Intent emailIntent = new Intent();
		emailIntent.setAction(Intent.ACTION_SEND);
		emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { "contact@lipberry.com" });
		emailIntent.setType("message/rfc822");
		PackageManager pm = getActivity().getPackageManager();
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
		// sendIntent.setType("text/plain");
		sendIntent.setType("message/rfc822");

		Intent openInChooser = Intent.createChooser(emailIntent,
				getActivity().getResources().getString(R.string.txt_send_mail));

		List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
		List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();
		for (int i = 0; i < resInfo.size(); i++) {
			// Extract the label, append it, and repackage it in a LabeledIntent
			ResolveInfo ri = resInfo.get(i);
			String packageName = ri.activityInfo.packageName;
			if (packageName.contains("android.email")) {
				emailIntent.setPackage(packageName);
			}
			// else if( packageName.contains("android.gm")) {
			// Intent intent = new Intent();
			// intent.setComponent(new ComponentName(packageName,
			// ri.activityInfo.name));
			// intent.setAction(Intent.ACTION_SEND);
			// intent.setType("text/plain");
			//
			//
			// intentList.add(new LabeledIntent(intent, packageName,
			// ri.loadLabel(pm), ri.icon));
			// }
		}

		// convert intentList to array
		LabeledIntent[] extraIntents = intentList.toArray(new LabeledIntent[intentList.size()]);

		openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
		startActivity(openInChooser);
	}

}
