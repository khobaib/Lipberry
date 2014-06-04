
package com.lipberry.settings;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bugsense.trace.Utils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.adapter.CustomAdapterForIInboxMessage;
import com.lipberry.adapter.CustomAdapterMessage;
import com.lipberry.adapter.NothingSelectedSpinnerAdapter;
import com.lipberry.fragment.MenuTabFragment;
import com.lipberry.model.Article;
import com.lipberry.model.InboxMessage;
import com.lipberry.model.InboxMessgaeList;
import com.lipberry.model.MemberList;
import com.lipberry.model.MemberListForSendMessage;
import com.lipberry.model.NotificationList;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.SingleMember;
import com.lipberry.model.ThreadMessageList;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
@SuppressLint({ "NewApi", "ValidFragment" })
public class FragmentFindamember extends Fragment{
	int threadposition;
	public MenuTabFragment parent;
	ProgressDialog pd;
	int startindex=0;
	int selectedpos=-1;
	MemberListForSendMessage memberListobject;
	ThreadMessageList messagelist;
	int endex=10;
	private ArrayAdapter<String> mAdapter;
	RelativeLayout re_sent_msz,re_new_msz,re_setting;
	CustomAdapterForIInboxMessage adapter;
	ArrayList<String>membername;
	String messageid;
	RelativeLayout re_holder;
	SingleMember singleMember;
	boolean oncreatecalledstate=false;
	JsonParser jsonParser;
	ListView actualListView;
	int screenmessagestate=0;
	ImageLoader imageLoader;
	LipberryApplication appInstance;
	ArrayList<InboxMessage>inboxlist;
	EditText et_username;
	ImageView img_pro_pic;
	TextView txt_bio,text_user_name;
	String username;
	Button btn_go;
	Spinner spn_uname;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		memberListobject=new MemberListForSendMessage();
		oncreatecalledstate=true;
		inboxlist=new ArrayList<InboxMessage>();
		jsonParser=new JsonParser();
		appInstance = (LipberryApplication)getActivity().getApplication();
		membername=new ArrayList<String>();
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(false).cacheOnDisc(false).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.find_a_member,
				container, false);
		re_holder=(RelativeLayout) v.findViewById(R.id.re_holder);
		re_holder.setVisibility(View.GONE);
		et_username=(EditText) v.findViewById(R.id.et_username);
		img_pro_pic=(ImageView) v.findViewById(R.id.img_pro_pic);
		txt_bio=(TextView) v.findViewById(R.id.txt_bio);
		text_user_name=(TextView) v.findViewById(R.id.text_user_name);
		txt_bio.setTypeface(Utility.getTypeface2(getActivity()));
		et_username.setTypeface(Utility.getTypeface2(getActivity()));
		btn_go=(Button) v.findViewById(R.id.btn_go);
		spn_uname=(Spinner) v.findViewById(R.id.spn_uname);
		text_user_name.setTypeface(Utility.getTypeface1(getActivity()));
		btn_go.setTypeface(Utility.getTypeface2(getActivity()));
		btn_go.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				username=et_username.getText().toString();
				if(Constants.isOnline(getActivity())){
					pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
							getActivity().getResources().getString(R.string.txt_please_wait), false);
					new AsyncTaskGetMemberListWithName().execute();
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
							Toast.LENGTH_SHORT).show();
				}
			}
		});
		//		
		//		if(memberListobject.getMemberlistForSendMessage().size()>0){
		//			
		//		}
		//		else{
		//			if(Constants.isOnline(getActivity())){
		//				pd=ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
		//						getActivity().getResources().getString(R.string.txt_please_wait), false);
		//				//new AsyncTaskGetMemberList().execute();
		//			}
		//			else{
		//				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
		//						Toast.LENGTH_SHORT).show();
		//			}
		//		}



		return v;
	}
	
	private void setcountry(){
		//memberListobject
		ArrayList<String> allcountryname=new ArrayList<String>();
		for(int i=0;i<memberListobject.getMemberlistForSendMessage().size();i++){
			allcountryname.add(memberListobject.getMemberlistForSendMessage().get(i).getUsername());
		}

		ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_item, allcountryname);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spn_uname.setAdapter(
				new NothingSelectedSpinnerAdapter(
						adapter,
						R.drawable.contact_spinner_row_nothing_selected_country,
						getActivity()));
		spn_uname.performClick();
		//spn_uname.performItemClick(view, position, id)
		
		spn_uname.setOnItemSelectedListener(new OnItemSelectedListener(){

			public void onItemSelected(AdapterView<?> parent1, View arg1, int position, 
					long arg3){
				if(position>0){
					et_username.setText(memberListobject.getMemberlistForSendMessage().get(position-1).getUsername());
					Constants.userid=memberListobject.getMemberlistForSendMessage().get(position-1).getId();
					parent.startFragmentSingleMember(memberListobject.getMemberlistForSendMessage().get(position-1).getId());
				}
								
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		((HomeActivity)getActivity()).welcome_title.setText(R.string.find_a_member);
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}
	private class AsyncTaskGetMemberListWithName extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
//				loginObj.put("startIndex",""+0);
//				loginObj.put("endIndex",""+1500);
				byte[] ba = username.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("username",base64Str);
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/memberlist/";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
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
			Log.e("res", result.getjObj().toString());
			if(pd.isShowing()&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject job=result.getjObj();

			try {
				String status=job.getString("status");
				if(status.equals("success")){
					memberListobject=MemberListForSendMessage.getMemberlist(result.getjObj());
					if(memberListobject.getMemberlistForSendMessage().size()>0){
						setcountry();
					}
				//	Toast.makeText(getActivity(),""+memberListobject.getMemberlistForSendMessage().size(), Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(getActivity(),getActivity().getResources().getString(R.string.txt_no_member_found2), 
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	//	private void generateautocomplete(AutoCompleteTextView autextview,String[] arrayToSpinner){
	//		ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(
	//				getActivity(),  R.layout.my_autocomplete_text_style, arrayToSpinner);
	//		autextview.setAdapter(myAdapter);
	//
	//	}
	//
	//	public void loadAutoComplete(){
	//		membername.clear();
	//		for (int i=0;i<memberListobject.getMemberlistForSendMessage().size();i++){
	//			membername.add(memberListobject.getMemberlistForSendMessage().get(i).getNickname());
	//		}
	//		generateautocomplete(act_to, membername.toArray(new String[membername.size()]));
	//		act_to.setThreshold(1);
	//		act_to.setOnItemClickListener(new OnItemClickListener() {
	//
	//			@Override
	//			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
	//				parent.getAdapter().getViewTypeCount();
	//				selectedpos=position;
	//				 String selection = (String) parent.getItemAtPosition(position);
	//			        int pos = -1;
	//
	//			        for (int i = 0; i < membername.size(); i++) {
	//			            if (membername.get(i).equals(selection)) {
	//			                pos = i;
	//			                break;
	//			            }
	//			        }
	//			        selectedpos=pos;
	//			        
	//				Constants.userid=memberListobject.getMemberlistForSendMessage().get(selectedpos).getId();
	//				if(Constants.isOnline(getActivity())){
	//					pd=ProgressDialog.show(getActivity(),getActivity().getResources().getString(R.string.app_name_arabic),
	//							getActivity().getResources().getString(R.string.txt_please_wait),false);
	//					new AsyncTaskGetSinleMember().execute();
	//				}
	//				else{
	//					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
	//							Toast.LENGTH_SHORT).show();
	//				}
	//
	//			}
	//		});
	//	}

	private class AsyncTaskGetSinleMember extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"account/findmemberbyid/"+Constants.userid+"/";
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_GET, url, null,
					null, null);
			return response;
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if(pd!=null){
				if(pd.isShowing()){
					pd.dismiss();
				}

			}
			setMemberObject(result.getjObj().toString());
		}
	}

	public void setMemberObject(String  respnse){
		try {
			Log.i("serverreponse", respnse);
			JSONObject jobj=new JSONObject(respnse);
			String  status=jobj.getString("status");
			if(status.equals("success")){
				singleMember = SingleMember.parseSingleMember(jobj);
				setmemberView();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_member_found),
						Toast.LENGTH_SHORT).show();
			}
		} catch (JSONException e) {
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_member_found),
					Toast.LENGTH_SHORT).show();
			e.printStackTrace();
		}
	}

	public void setmemberView(){
		re_holder.setVisibility(View.VISIBLE);
		text_user_name.setText(singleMember.getNickname());	
		text_user_name.setTypeface(Utility.getTypeface1(getActivity()));
		imageLoader.displayImage(singleMember.getAvatar(), img_pro_pic);
		txt_bio.setText(singleMember.getBrief());
		if(singleMember.getBrief()!=null){
			txt_bio.setText(Html.fromHtml(singleMember.getBrief()));
			txt_bio.setMovementMethod(LinkMovementMethod.getInstance());
			ShowHtmlText showtext=new ShowHtmlText(txt_bio, getActivity());
			showtext.updateImages(true,singleMember.getBrief());
			txt_bio.setTypeface(Utility.getTypeface2(getActivity()));
		}
		
		text_user_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				parent.startFragmentMyProfile();
				Constants.GOMEMBERSTATEFROMSETTING=true;
				((HomeActivity)getActivity()).mTabHost.setCurrentTab(4);

			}
		});
		img_pro_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Constants.GOMEMBERSTATEFROMSETTING=true;
				((HomeActivity)getActivity()).mTabHost.setCurrentTab(4);

			}
		});
	}
}


