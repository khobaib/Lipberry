
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
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.adapter.CustomAdapterFormemberPost;
import com.lipberry.model.ArticleList;
import com.lipberry.model.ServerResponse;
import com.lipberry.model.SingleMember;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
@SuppressLint("NewApi")
public class FragmentMyProfile extends Fragment {
	ImageLoader imageLoader;
	LipberryApplication appInstance;
	SingleMember singleMember;
	JsonParser jsonParser;
	WebView webview_member;
	ArticleList  articlelistinstance;
	MenuTabFragment parent;
	TextView txt_num_seen,txt_num_following,txt_num_follower,txt_name,txt_nick_name,txt_bio,txt_seen_text,txt_following_text,txt_follower_text;
	ImageView img_profile;
	ProgressDialog pd;
	Button btn_change_photo;
	boolean followstate=false;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		appInstance = (LipberryApplication) getActivity().getApplication();
		jsonParser=new JsonParser();
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
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_my_profile,
				container, false);
		txt_seen_text=(TextView) v.findViewById(R.id.txt_seen_text);
		txt_following_text=(TextView) v.findViewById(R.id.txt_following_text);
		txt_follower_text=(TextView) v.findViewById(R.id.txt_follower_text);
		txt_num_seen=(TextView) v.findViewById(R.id.txt_num_seen);
		btn_change_photo=(Button) v.findViewById(R.id.btn_change_photo);
		txt_num_following=(TextView) v.findViewById(R.id.txt_num_following);
		txt_num_follower=(TextView) v.findViewById(R.id.txt_num_follower);
		txt_name=(TextView) v.findViewById(R.id.txt_name);
		txt_nick_name=(TextView) v.findViewById(R.id.txt_nick_name);
		txt_bio=(TextView) v.findViewById(R.id.txt_bio);
		img_profile=(ImageView) v.findViewById(R.id.img_profile);
		btn_change_photo.setTypeface(Utility.getTypeface2(getActivity()));
		webview_member=(WebView) v.findViewById(R.id.webview_member);
		txt_seen_text.setTypeface(Utility.getTypeface2(getActivity()));
		txt_following_text.setTypeface(Utility.getTypeface2(getActivity()));
		txt_follower_text.setTypeface(Utility.getTypeface2(getActivity()));
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(),getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskGetSinleMember().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		btn_change_photo.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.startFragmentProfileSetting();
			}
		});
		img_profile.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				parent.startFragmentProfileSetting();
			}
		});
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();


		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}
	private class AsyncTaskGetSinleMember extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			String url =Constants.baseurl+"account/findmemberbyid/"+appInstance.getUserCred().getId()+"/";
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
				singleMember  =com.lipberry.model.SingleMember.parseSingleMember(jobj);
				setUserInterface();
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
	public void setUserInterface(){
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).welcome_title.setText(singleMember.getName());
		txt_name.setText(singleMember.getName());
		txt_nick_name.setText(singleMember.getNickname());
		if(singleMember.getBrief()!=null){
			txt_bio.setText(Html.fromHtml(singleMember.getBrief()));
		}
		txt_nick_name.setTypeface(Utility.getTypeface2(getActivity()));
		txt_name.setTypeface(Utility.getTypeface1(getActivity()));
		txt_bio.setTypeface(Utility.getTypeface2(getActivity()));
		txt_bio.setText(singleMember.getBrief());
		txt_bio.setMovementMethod(LinkMovementMethod.getInstance());
		ShowHtmlText showtext=new ShowHtmlText(txt_bio, getActivity());
		txt_bio.setMovementMethod(LinkMovementMethod.getInstance());
		showtext.updateImages(true,singleMember.getBrief());
		txt_num_follower.setText(singleMember.getNumber_of_followers());
		txt_num_following.setText(singleMember.getNumber_of_following());
		txt_num_seen.setText(singleMember.getPublicpage_visit());
		imageLoader.displayImage(singleMember.getAvatar(), img_profile);
	}
	
}