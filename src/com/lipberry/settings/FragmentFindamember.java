package com.lipberry.settings;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.FoundMemberListAdapter;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.model.MemberForSendMessage;
import com.lipberry.model.MemberListForSendMessage;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

@SuppressLint({ "NewApi", "ValidFragment" })
public class FragmentFindamember extends Fragment {
	int threadposition;
	public MenuTabFragment parent;
	private ProgressDialog pd;
	private MemberListForSendMessage memberListobject;
	private JsonParser jsonParser;
	private EditText et_username;
	private String username;
	private Button btn_go;
	String search_Key;
	// private Spinner spn_uname;
	private ListView lvFoundMembers;
	private FoundMemberListAdapter fmlAdapter;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		memberListobject = new MemberListForSendMessage();
		jsonParser = new JsonParser();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(false)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.find_a_member, container, false);
		et_username = (EditText) v.findViewById(R.id.et_username);
		et_username.setTypeface(Utility.getTypeface2(getActivity()));
		
		// spn_uname = (Spinner) v.findViewById(R.id.spn_uname);
		lvFoundMembers = (ListView) v.findViewById(R.id.lvFoundMemberList);
		fmlAdapter = new FoundMemberListAdapter(getActivity(), memberListobject.getMemberlistForSendMessage());
		lvFoundMembers.setAdapter(fmlAdapter);
		lvFoundMembers.setVisibility(View.VISIBLE);
		lvFoundMembers.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> pt, View view, int position, long id) {
				// TODO show single user
				if (position < memberListobject.getMemberlistForSendMessage().size()) {
					MemberForSendMessage m = (MemberForSendMessage) pt.getItemAtPosition(position);
					search_Key=et_username.getText().toString();
					Constants.userid = m.getId();
					parent.startFragmentSingleMember(m.getId());
				}
			}
		});
		// text_user_name.setTypeface(Utility.getTypeface1(getActivity()));
		btn_go = (Button) v.findViewById(R.id.btn_go);
		btn_go.setTypeface(Utility.getTypeface2(getActivity()));
		btn_go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				username = et_username.getText().toString();
				search_Key=username;
				if (Constants.isOnline(getActivity())) {
					pd = ProgressDialog.show(getActivity(),
							getActivity().getResources().getString(R.string.app_name_arabic), getActivity()
									.getResources().getString(R.string.txt_please_wait), false);
					new AsyncTaskGetMemberListWithName().execute();
				} else {
					Toast.makeText(getActivity(),
							getActivity().getResources().getString(R.string.Toast_check_internet), Toast.LENGTH_SHORT)
							.show();
				}
			}
		});

		return v;
	}

	@Override
	public void onResume() {
		super.onResume();
		((HomeActivity) getActivity()).welcome_title.setText(R.string.find_a_member);
		((HomeActivity) getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity) getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
		if((search_Key!=null)){
			if(search_Key.equals("")){
				et_username.setText("");
				et_username.setHint(R.string.txt_find_member);
			}
			else{
				et_username.setText(search_Key);
			}
			
		}
		else{
			et_username.setText("");
			et_username.setHint(R.string.txt_find_member);
		}
		
	}

	private class AsyncTaskGetMemberListWithName extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				// loginObj.put("session_id",
				// appInstance.getUserCred().getSession_id());
				// loginObj.put("startIndex",""+0);
				// loginObj.put("endIndex",""+1500);
				// loginObj.put("username", username);
				byte[] ba = username.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("searchtext", base64Str);
				String loginData = loginObj.toString();
				String url = Constants.baseurl + "account/searchMember/";// memberlist/";
				ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				return response;
			} catch (JSONException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			Constants.debugLog("result", result.getjObj().toString());
			if (pd.isShowing() && (pd != null)) {
				pd.dismiss();
			}
			if (result == null)
				return;
			JSONObject job = result.getjObj();
			// Log.i("FragFindMem_Touhid", "jObj: " + job.toString());

			try {
				String status = job.getString("status");
				if (status.equals("success")) {
					// memberListobject =
					// MemberListForSendMessage.getMemberlist(result.getjObj());
					memberListobject = new MemberListForSendMessage(status, MemberForSendMessage.parseMemberList(result
							.getjObj().getJSONArray("member_list")));
					// FIXME Search indiv. acc :: 0->1
					// if (memberListobject.getMemberlistForSendMessage().size()
					// > 1) {
					showFoundMemberList();
					// } else
					// showFoundMemberPopUpList();
					// Toast.makeText(getActivity(),""+memberListobject.getMemberlistForSendMessage().size(),
					// Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(),
							job.getString("description"), Toast.LENGTH_SHORT)
							.show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	public void showFoundMemberList() {
		// take adapter
		// set list
		// set on-click listener
		fmlAdapter.setData(memberListobject.getMemberlistForSendMessage());
		fmlAdapter.notifyDataSetChanged();
	}
}

// //////............................................///////////////////

// private void showFoundMemberPopUpList() {
// // memberListobject
// ArrayList<String> allMemberName = new ArrayList<String>();
// for (int i = 0; i <
// memberListobject.getMemberlistForSendMessage().size(); i++) {
// allMemberName.add(memberListobject.getMemberlistForSendMessage().get(i).getUserName());
// }
//
// ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
// R.layout.spinner_item, allMemberName);
// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// spn_uname.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
// R.drawable.contact_spinner_row_nothing_selected_country, getActivity()));
// spn_uname.performClick();
// // spn_uname.performItemClick(view, position, id)
//
// spn_uname.setOnItemSelectedListener(new OnItemSelectedListener() {
//
// public void onItemSelected(AdapterView<?> parent1, View arg1, int
// position, long arg3) {
// if (position > 0) {
// et_username.setText(memberListobject.getMemberlistForSendMessage().get(position
// - 1).getUserName());
// Constants.userid =
// memberListobject.getMemberlistForSendMessage().get(position - 1).getId();
// parent.startFragmentSingleMember(memberListobject.getMemberlistForSendMessage().get(position
// - 1)
// .getId());
// }
//
// }
//
// @Override
// public void onNothingSelected(AdapterView<?> arg0) {
// }
// });
//
// }

// private int startindex = 0;
// private int selectedpos = -1;
// private ThreadMessageList messagelist;
// private int endex = 10;
// private ArrayAdapter<String> mAdapter;
// private RelativeLayout re_sent_msz, re_new_msz, re_setting;
// private CustomAdapterForIInboxMessage adapter;
// private ArrayList<String> membername;
// private String messageid;
// private RelativeLayout re_holder;
// private SingleMember singleMember;
// private boolean oncreatecalledstate = false;
// private ListView actualListView;
// private int screenmessagestate = 0;
// private ImageLoader imageLoader;
// private LipberryApplication appInstance;
// private ArrayList<InboxMessage> inboxlist;
// private ImageView img_pro_pic;
// private TextView txt_bio, text_user_name;

// re_holder = (RelativeLayout) v.findViewById(R.id.re_holder);
// re_holder.setVisibility(View.GONE);
// img_pro_pic = (ImageView) v.findViewById(R.id.img_pro_pic);
// txt_bio = (TextView) v.findViewById(R.id.txt_bio);
// text_user_name = (TextView) v.findViewById(R.id.text_user_name);
// txt_bio.setTypeface(Utility.getTypeface2(getActivity()));
//
// if(memberListobject.getMemberlistForSendMessage().size()>0){
//
// }
// else{
// if(Constants.isOnline(getActivity())){
// pd=ProgressDialog.show(getActivity(),
// getActivity().getResources().getString(R.string.app_name_arabic),
// getActivity().getResources().getString(R.string.txt_please_wait),
// false);
// //new AsyncTaskGetMemberList().execute();
// }
// else{
// Toast.makeText(getActivity(),
// getActivity().getResources().getString(R.string.Toast_check_internet),
// Toast.LENGTH_SHORT).show();
// }
// }

// private void generateautocomplete(AutoCompleteTextView
// autextview,String[] arrayToSpinner){
// ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
// getActivity(), R.layout.my_autocomplete_text_style, arrayToSpinner);
// autextview.setAdapter(myAdapter);
//
// }
//
// public void loadAutoComplete(){
// membername.clear();
// for (int
// i=0;i<memberListobject.getMemberlistForSendMessage().size();i++){
// membername.add(memberListobject.getMemberlistForSendMessage().get(i).getNickname());
// }
// generateautocomplete(act_to, membername.toArray(new
// String[membername.size()]));
// act_to.setThreshold(1);
// act_to.setOnItemClickListener(new OnItemClickListener() {
//
// @Override
// public void onItemClick(AdapterView<?> parent, View v, int position, long
// id) {
// parent.getAdapter().getViewTypeCount();
// selectedpos=position;
// String selection = (String) parent.getItemAtPosition(position);
// int pos = -1;
//
// for (int i = 0; i < membername.size(); i++) {
// if (membername.get(i).equals(selection)) {
// pos = i;
// break;
// }
// }
// selectedpos=pos;
//
// Constants.userid=memberListobject.getMemberlistForSendMessage().get(selectedpos).getId();
// if(Constants.isOnline(getActivity())){
// pd=ProgressDialog.show(getActivity(),getActivity().getResources().getString(R.string.app_name_arabic),
// getActivity().getResources().getString(R.string.txt_please_wait),false);
// new AsyncTaskGetSinleMember().execute();
// }
// else{
// Toast.makeText(getActivity(),
// getActivity().getResources().getString(R.string.Toast_check_internet),
// Toast.LENGTH_SHORT).show();
// }
//
// }
// });
// }
//
// @SuppressWarnings("unused")
// private class AsyncTaskGetSinleMember extends AsyncTask<Void, Void,
// ServerResponse> {
// @Override
// protected ServerResponse doInBackground(Void... params) {
// String url = Constants.baseurl + "account/findmemberbyid/" +
// Constants.userid + "/";
// ServerResponse response =
// jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
// null, null);
// return response;
// }
//
// @Override
// protected void onPostExecute(ServerResponse result) {
// super.onPostExecute(result);
// if (pd != null) {
// if (pd.isShowing()) {
// pd.dismiss();
// }
//
// }
// setMemberObject(result.getjObj().toString());
// }
// }
//
// private void setMemberObject(String respnse) {
// try {
// Log.i("serverreponse", respnse);
// JSONObject jobj = new JSONObject(respnse);
// String status = jobj.getString("status");
// if (status.equals("success")) {
// singleMember = SingleMember.parseSingleMember(jobj);
// setmemberView();
// } else {
// Toast.makeText(getActivity(),
// getActivity().getResources().getString(R.string.Toast_member_found),
// Toast.LENGTH_SHORT).show();
// }
// } catch (JSONException e) {
// Toast.makeText(getActivity(),
// getActivity().getResources().getString(R.string.Toast_member_found),
// Toast.LENGTH_SHORT).show();
// e.printStackTrace();
// }
// }
//
// private void setmemberView() {
// re_holder.setVisibility(View.VISIBLE);
// text_user_name.setText(singleMember.getNickname());
// text_user_name.setTypeface(Utility.getTypeface1(getActivity()));
// imageLoader.displayImage(singleMember.getAvatar(), img_pro_pic);
// txt_bio.setText(singleMember.getBrief());
// if (singleMember.getBrief() != null) {
// txt_bio.setText(Html.fromHtml(singleMember.getBrief()));
// txt_bio.setMovementMethod(LinkMovementMethod.getInstance());
// ShowHtmlText showtext = new ShowHtmlText(txt_bio, getActivity());
// showtext.updateImages(true, singleMember.getBrief());
// txt_bio.setTypeface(Utility.getTypeface2(getActivity()));
// }
//
// text_user_name.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View arg0) {
// parent.startFragmentMyProfile();
// Constants.GOMEMBERSTATEFROMSETTING = true;
// ((HomeActivity) getActivity()).mTabHost.setCurrentTab(4);
//
// }
// });
// img_pro_pic.setOnClickListener(new OnClickListener() {
//
// @Override
// public void onClick(View arg0) {
// Constants.GOMEMBERSTATEFROMSETTING = true;
// ((HomeActivity) getActivity()).mTabHost.setCurrentTab(4);
//
// }
// });
// }
