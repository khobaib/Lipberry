
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
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.utility.Constants;
@SuppressLint("NewApi")
public class FragmentMenu extends Fragment {
	MenuTabFragment parent;
	String[]menuarray;
	ListView list_menu_item;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.e("activity", "name  "+getActivity());
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_menu,
				container, false);
		list_menu_item=(ListView) v.findViewById(R.id.list_menu_item);
		menuarray = getActivity().getResources().getStringArray(R.array.menuarray);
		ArrayAdapter adapter = new ArrayAdapter<String>(
				getActivity(),
				android.R.layout.simple_list_item_1,
				menuarray);
		list_menu_item.setAdapter(adapter);
		setlistviewonitemclick();
		if(	Constants.MESSAGESETTINGSTATE){
			parent.startFragmentSetting();
		}
		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
	}

	public void setlistviewonitemclick(){
		list_menu_item.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				switch (position) {
				case 0:
						((HomeActivity)getActivity()).mTabHost.setCurrentTab(4);
					break;
				case 1:
					parent.startFragmentSetting();
					break;
				case 2:
					parent.startFragmentFindamember();
					break;
				case 3:
					((HomeActivity)getActivity()).mTabHost.setCurrentTab(0);
					break;
				case 4:
					parent.startFragmentMyProfile();
					break;
				case 5:
					
					break;

				default:
					break;
				}
			}
		});
	}
}

