
package com.lipberry.fragment;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebView.FindListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.lipberry.HomeActivity;
import com.lipberry.LoginActivity;
import com.lipberry.R;
import com.lipberry.Splash2Activity;
import com.lipberry.adapter.CustomAdapter;
import com.lipberry.adapter.CustomAdapterForComment;
import com.lipberry.customalertdilog.LisAlertDialog;
import com.lipberry.model.Article;
import com.lipberry.model.ArticleDetails;
import com.lipberry.model.Commentslist;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
@SuppressLint({ "NewApi", "ResourceAsColor" })
public class FragmentArticleDetailsFromCategory extends Fragment {
	LipberryApplication appInstance;
	ImageLoader imageLoader;
	ProgressDialog pd;
	ArticleDetails articledetails;
	Commentslist commentslist;
	ListView list_comment;
	ListView lst_imag;
	boolean followstate=false;
	CategoryTabFragment parent;
	TextView text_user_name,text_date_other,txt_articl_ename,text_topic_text,txt_like,text_comment,txt_viewd;
	ImageView img_pro_pic,img_article,img_like,image_comments,play_vedio,image_share;
	Button btn_photo_album,btn_follow_her,back,btn_report;
	LinearLayout vedioholder;
	VideoView video_view;
	JsonParser jsonParser;
	CustomAdapterForComment adapter1;
	ImageLoadingListener imll;
	EditText et_comment;
	String commentstext;
	@SuppressLint("NewApi")
	Article article;
	public void setArticle(Article article){
		this.article=article;
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
		.cacheInMemory(false).cacheOnDisc(false).build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getActivity().getApplicationContext()).defaultDisplayImageOptions(
						defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
	}


	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


	public static void setListViewHeightBasedOnChildrenImage(ListView listView,int height) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			// pre-condition
			return;
		}

		int totalHeight = height;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			View listItem = listAdapter.getView(i, null, listView);
			if (listItem instanceof ViewGroup) {
				listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			listItem.measure(0, 0);
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}


	public void setmemberlist(){
		adapter1=new CustomAdapterForComment(getActivity(), commentslist.getCommentslist(),Constants.baseurl+"article/commentlist/"+article.getArticle_id());

		list_comment.setAdapter(adapter1);
		setListViewHeightBasedOnChildren(list_comment);
		//		list_comment.setOnTouchListener(new OnTouchListener() {
		//			@Override
		//			public boolean onTouch(View v, MotionEvent event) {
		//				v.getParent().requestDisallowInterceptTouchEvent(true);
		//				return false;
		//			}
		//		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		appInstance = (LipberryApplication) getActivity().getApplication();
		jsonParser=new JsonParser();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_article_details,
				container, false);
		initview(v);
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
					"Retreving article details", true);
			new AsyncTaskgetArticleDetails().execute();
		}
		else{
			Toast.makeText(getActivity(), getResources().getString(R.string.Toast_check_internet), 
					Toast.LENGTH_SHORT).show();
		}

		TextView tv = new TextView(getActivity());
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		params.leftMargin = 107;
		params.topMargin=100;

		return v;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(article.getCategory_name()!=null){
			((HomeActivity)getActivity()).welcome_title.setText(article.getCategory_name());

		}
		else{
			if(article.getcategory().equals("1")){
				((HomeActivity)getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat1));
			}
			if(article.getcategory().equals("2")){
				if(article.getArticle_category_url().contains("shexp")){
					((HomeActivity)getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat2_shpx));
				}
				else{
					((HomeActivity)getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat2));

				}
			}
			if(article.getcategory().equals("3")){
				((HomeActivity)getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat3));
			}
			if(article.getcategory().equals("5")){
				((HomeActivity)getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat5));
			}
			if(article.getcategory().equals("8")){
				((HomeActivity)getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat8));
			}
		}




		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.VISIBLE);



		if(article.getcategory().equals("2")){
			if(article.getArticle_category_url().contains("shexp")){
				int id = getActivity().getResources().getIdentifier("l"+article.getcategory(), "drawable", getActivity().getPackageName());
				((HomeActivity)getActivity()).img_cat_icon.setImageResource(id);
			}
			else{
				int id = getActivity().getResources().getIdentifier("bl"+article.getcategory(), "drawable", getActivity().getPackageName());
				((HomeActivity)getActivity()).img_cat_icon.setImageResource(id);
			}
		}else{
			int id = getActivity().getResources().getIdentifier("l"+article.getcategory(), "drawable", getActivity().getPackageName());
			((HomeActivity)getActivity()).img_cat_icon.setImageResource(id);
		}
		((HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity)getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				parent.onBackPressed();
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

		((HomeActivity)getActivity()).img_cat_icon.setVisibility(View.GONE);

		super.onPause();
	}
	private class AsyncTaskgetArticleDetails extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"article/findarticlebyid/"+article.getArticle_id();
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status=jobj.getString("status");
				if(status.equals("success")){
					articledetails=ArticleDetails.getArticleDetails(jobj);
					new AsyncTaskGetComments().execute();
					articledetails=ArticleDetails.getArticleDetails(jobj);
					if(articledetails.getFollow_flag()!=null){
						if(!articledetails.getFollow_flag().equals("Not a follower")){
							followstate=true;
						}
						else{
							followstate=false;
						}
					}
					setview();
				}
				else{
					//					if((pd.isShowing())&&(pd!=null)){
					//						pd.dismiss();
					//					}
					String message=jobj.getString("message");
					Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
				}
			} 
			catch (JSONException e) {
				//				if((pd.isShowing())&&(pd!=null)){
				//					pd.dismiss();
				//				}
			}
		}
	}
	public void initview(ViewGroup v){
		//play_vedio,back,video_view,vedioholder
		play_vedio=(ImageView) v.findViewById(R.id.play_vedio);
		back=(Button) v.findViewById(R.id.back);
		video_view=(VideoView) v.findViewById(R.id.video_view);
		vedioholder=(LinearLayout) v.findViewById(R.id.vedio_view_holder);
		list_comment=(ListView) v.findViewById(R.id.list_comment);
		lst_imag=(ListView) v.findViewById(R.id.lst_imag);
		text_user_name=(TextView) v.findViewById(R.id.text_user_name);
		text_date_other=(TextView) v.findViewById(R.id.text_date_other);
		txt_articl_ename=(TextView) v.findViewById(R.id.txt_articl_ename);
		text_topic_text=(TextView) v.findViewById(R.id.text_topic_text);
		txt_like=(TextView) v.findViewById(R.id.txt_like);
		text_comment=(TextView) v.findViewById(R.id.text_comment);
		txt_viewd=(TextView) v.findViewById(R.id.txt_viewd);
		img_pro_pic=(ImageView) v.findViewById(R.id.img_pro_pic);
		image_comments=(ImageView) v.findViewById(R.id.image_comments);
		img_like=(ImageView) v.findViewById(R.id.img_like);
		img_article=(ImageView) v.findViewById(R.id.img_article);
		btn_follow_her=(Button) v.findViewById(R.id.btn_follow_her);
		btn_photo_album=(Button) v.findViewById(R.id.btn_photo_album);
		btn_report=(Button) v.findViewById(R.id.btn_report);
		image_share=(ImageView) v.findViewById(R.id.image_share);
	}

	public void setview(){
		image_share.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = articledetails.getShort_url();
				
				sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Lipberry");
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent, "Share via"));
				
			}
		} );
		txt_like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LisAlertDialog alert;
				if(article.getLikedmemberlist().size()>0){
					alert=new LisAlertDialog(getActivity(), article.getLikedmemberlist(),getActivity(),null,parent);
					alert.show_alert();
				}
				else{

				}
			}
		});
		Log.e("abuseflag", "1 " +articledetails.getAbuseFlag());
		if(articledetails.getAbuseFlag().equals("false")){
			btn_report.setVisibility(View.VISIBLE);
		}
		else{
			btn_report.setVisibility(View.GONE);
		}
		play_vedio.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				vedioholder.setVisibility(View.VISIBLE);
				if(articledetails.getVideo().equals("")){

				}
				else{
					startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(articledetails.getVideo())));

				}
			}
		});
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				vedioholder.setVisibility(View.GONE);
			}
		});
		text_user_name.setText(article.getMember_nickname());
		text_date_other.setText(articledetails.getCreated_at());
		txt_articl_ename.setText(articledetails.getTitle());
		if(articledetails.getVisit_counter().equals("")){
			txt_viewd.setText("");
		}
		else{
			txt_viewd.setText(""+Long.parseLong(articledetails.getVisit_counter()));
		}
		text_topic_text.setText(article.getArticle_description());
		txt_like.setText(articledetails.getLikemember_text());
		text_comment.setText(articledetails.getComment_count()+ " "+getResources().
				getString(R.string.txt_comments));
		imageLoader.displayImage(articledetails.getMember_avatar(), img_pro_pic);
		imll=new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				getActivity().runOnUiThread(new Runnable(){
					public void run(){
						getActivity().runOnUiThread(new Runnable(){
							public void run(){
								pd=ProgressDialog.show(getActivity(), "Lipberry",
										"Image is loading", true);
							}
						});
					}
				});
			}
			@Override
			public void onLoadingFailed(String imageUri, View view,
					FailReason failReason) {
				getActivity().runOnUiThread(new Runnable(){
					public void run(){
						if((pd.isShowing())&&(pd!=null)){
							pd.dismiss();
						}
					}
				});

				if(articledetails.getArticle_gallery().size()>0){
					CustomAdapter adapter=new CustomAdapter(getActivity(), articledetails.getArticle_gallery());
					lst_imag.setAdapter(adapter);
					lst_imag.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							v.getParent().requestDisallowInterceptTouchEvent(true);
							return false;
						}

					});
					lst_imag.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							imageLoader.loadImage(articledetails.getArticle_gallery().get(position).getImage_src(), imll);
						}
					});
				}
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				getActivity().runOnUiThread(new Runnable(){
					public void run(){

						if((pd.isShowing())&&(pd!=null)){
							pd.dismiss();
							Log.e("pd", "3");
						}
					}
				});
				int imageheight=Utility.getDeviceHeight(getActivity())/8;
				Bitmap bitmap=loadedImage;
				if(bitmap!=null){
					int bitmapheight=bitmap.getHeight();
					int bitmapweight=bitmap.getWidth();
					int deviceheight=Utility.getDeviceHeight(getActivity());
					int devicewidth=Utility.getDeviceWidth(getActivity());
					float ratio=(float)devicewidth/(float)bitmapweight;
					int resizebitmapwidth=devicewidth;
					float a=(bitmapheight*ratio);
					int resizebitmaphight=(int)a ;
					imageheight=resizebitmaphight-00;
					bitmap=Bitmap.createScaledBitmap(bitmap,resizebitmapwidth,resizebitmaphight, false);
					img_article.setImageBitmap(bitmap);
					int x=img_article.getLeft();
					int y=img_article.getTop();
					int h=img_article.getHeight();
					int w=img_article.getWidth();


				}



				if(articledetails.getArticle_gallery().size()>0){
					CustomAdapter adapter=new CustomAdapter(getActivity(), articledetails.getArticle_gallery());
					lst_imag.setAdapter(adapter);
					setListViewHeightBasedOnChildrenImage(lst_imag, imageheight);
					lst_imag.setOnTouchListener(new OnTouchListener() {
						@Override
						public boolean onTouch(View v, MotionEvent event) {
							v.getParent().requestDisallowInterceptTouchEvent(true);
							return false;
						}

					});

					lst_imag.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							imageLoader.loadImage(articledetails.getArticle_gallery().get(position).getImage_src(), imll);
						}
					});
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				getActivity().runOnUiThread(new Runnable(){
					public void run(){
						if((pd.isShowing())&&(pd!=null)){
							pd.dismiss();
						}

						if(articledetails.getArticle_gallery().size()>0){
							CustomAdapter adapter=new CustomAdapter(getActivity(), articledetails.getArticle_gallery());
							lst_imag.setAdapter(adapter);
							lst_imag.setOnTouchListener(new OnTouchListener() {
								@Override
								public boolean onTouch(View v, MotionEvent event) {
									v.getParent().requestDisallowInterceptTouchEvent(true);
									return false;
								}

							});
							lst_imag.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0, View arg1,
										int position, long arg3) {
									imageLoader.loadImage(articledetails.getArticle_gallery().get(position).getImage_src(), imll);
								}
							});
						}
					}
				});
			}
		};
		if((articledetails.getPhoto()!=null)||(!articledetails.getPhoto().equals(""))){
			imageLoader.loadImage(articledetails.getPhoto(), imll);
		}


		if(article.getUserAlreadylikeThis()!=null){
			if(!article.getUserAlreadylikeThis().equals("No")){
				img_like.setBackgroundResource(R.drawable.unlike);
			}
			else{
				img_like.setBackgroundResource(R.drawable.like);
			}
		}

		if(followstate){
			btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_following));

		}
		else{
			btn_follow_her.setBackgroundResource(R.drawable.lbtn_follow);
		}
		btn_follow_her.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonfollowclicked();
			}
		});

		image_comments.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showCustomDialog();
			}
		});
		img_like.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				imgeviewlikeclicked();
			}
		});
		btn_report.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				CallReoprt();
			}
		});
	}

	public void imgeviewlikeclicked(){
		if(!article.getUserAlreadylikeThis().equals("No")){
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(getActivity(), "Lipberry",
						"Start dislike", true);
				new AsyncTaskSetDislike().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
		else{
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(getActivity(), "Lipberry",
						"Sending like", true);
				new AsyncTaskSeLike().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	private int getRelativeTop(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getTop();
		else
			return myView.getTop() + getRelativeTop((View) myView.getParent());
	}

	private int getRelativeBottom(View myView) {
		if (myView.getParent() == myView.getRootView())
			return myView.getBottom();
		else
			return myView.getBottom() + getRelativeBottom((View) myView.getParent());
	}

	private class AsyncTaskSetDislike extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =articledetails.getDislike_url();
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					img_like.setBackgroundResource(R.drawable.like);
					article.setUserAlreadylikeThis("No");
				}
				else{
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					if(description.equals("You pressed dislike before")){
						img_like.setBackgroundResource(R.drawable.like);
						article.setUserAlreadylikeThis("No");
					}
				}
			} 
			catch (JSONException e) {
			}
		}
	}

	private class AsyncTaskSeLike extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =articledetails.getLike_url();
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				return response;
			} 
			catch (JSONException e) {                
				e.printStackTrace();
				return null;
			}
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					img_like.setBackgroundResource(R.drawable.unlike);
					article.setUserAlreadylikeThis("Yes");
				}
				else{
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					if(description.equals("You presed like before")){
						img_like.setBackgroundResource(R.drawable.unlike);
						article.setUserAlreadylikeThis("Yes");
					}
				}
			} 
			catch (JSONException e) {
			}
		}
	}


	private class AsyncTaskCallReoprt extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =articledetails.getAbuse_url();
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String description=jobj.getString("description");
				Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
				btn_report.setVisibility(View.GONE);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}


	private class AsyncTaskSendUnFollowReq extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {

			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/cancelFollowmember/"+article.getMember_id()+"/";
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_follower));
					followstate=false;
				}
				else{
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
			}
		}
	}
	private class AsyncTaskSendFollowReq extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"account/followmember/"+article.getMember_id()+"/";
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
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				String description=jobj.getString("description");
				if(status.equals("success")){
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_following));
					btn_follow_her.setBackgroundColor(android.R.color.transparent);
					followstate=true;
				}
				else{
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
					if(description.equals("Already followed")){
						btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_following));
						btn_follow_her.setBackgroundColor(android.R.color.transparent);
						followstate=true;
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public void buttonfollowclicked(){
		if(!followstate){
			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(getActivity(), "Lipberry",
						"Please wait", true);
				new AsyncTaskSendFollowReq().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 
						10000).show();
			}
		}
	}
	public void CallReoprt(){
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
					"Start Report", true);
			new AsyncTaskCallReoprt().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					10000).show();
		}
	}


	public void sendposttoserver(){
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
					"Posting comments", true);
			new AsyncTaskPostComments().execute();
		}
		else{
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
	}
	private class AsyncTaskPostComments extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				byte[] ba = commentstext.getBytes();
				String base64Str = Base64.encodeBytes(ba);
				loginObj.put("comment", base64Str);
				String loginData = loginObj.toString();
				String url =articledetails.getComment_url();
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
			//			if((pd.isShowing())&&(pd!=null)){
			//				pd.dismiss();
			//			}
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				if(status.equals("success")){
					articledetails.setCommentcount((Integer.parseInt(articledetails.getComment_count())+1)+"");
					article.setCommentcount((Integer.parseInt(article.getComment_count())+1)+"");
					text_comment.setText(articledetails.getComment_count()+ " "+getResources().
							getString(R.string.txt_comments));
					if(Constants.isOnline(getActivity())){

						new AsyncTaskGetComments().execute();
						Toast.makeText(getActivity(),"You just commented! ", Toast.LENGTH_SHORT).show();
					}
					else{
						Toast.makeText(getActivity(), getResources().getString(R.string.Toast_check_internet), 
								Toast.LENGTH_SHORT).show();
					}

				}
				else{
					if((pd.isShowing())&&(pd!=null)){
						pd.dismiss();
					}
					String description=jobj.getString("description");
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	private class AsyncTaskGetComments extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				int endindex;
				Log.i("error",  "a "+article.getComment_count());

				if((article.getComment_count()!=null)&&(!article.getComment_count().equals(""))){
					endindex=Integer.parseInt(article.getComment_count())+1;
				}
				else{
					endindex=20;
				}

				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex", "0");
				loginObj.put("endIndex", ""+endindex);

				String loginData = loginObj.toString();
				String url =Constants.baseurl+"article/commentlist/"+article.getArticle_id();
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
			JSONObject jobj=result.getjObj();
			//			if((pd.isShowing())&&(pd!=null)){
			//				pd.dismiss();
			////			}

			if(pd!=null){
				if(pd.isShowing()){
					pd.dismiss();
				}
			}
			new AsyncTaskUpdatePageVisit().execute();

			try {
				String status= jobj.getString("status");
				if(status.equals("success")){
					commentslist=Commentslist.getCommentsListInstance(jobj);
					if(commentslist.getCommentslist().size()>0){
						setmemberlist();
					}
				}
				else{
					String description=jobj.getString("message");
					Toast.makeText(getActivity(),description, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
			}
		}
	}

	public void showCustomDialog(){
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.custom_dilog);
		dialog.setTitle("Lipberry");
		et_comment =  (EditText) dialog.findViewById(R.id.et_comment);
		Button  btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		Button  bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String comments=et_comment.getText().toString();
				if(comments.equals("")){
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_enter_text),
							Toast.LENGTH_SHORT).show();
				}
				else{
					dialog.dismiss();
					commentstext=comments;
					sendposttoserver();
				}
			}
		});

		dialog.show();
	}

	private class AsyncTaskUpdatePageVisit extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			JSONObject loginObj = new JSONObject();
			String loginData = loginObj.toString();
			String url =articledetails.getUpdate_visitcounter_url();
			ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
					loginData, null);
			return response;
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			JSONObject jobj=result.getjObj();
			try {
				String status= jobj.getString("status");
				if(status.equals("success")){
				}
				else{
					String description=jobj.getString("message");
				}
			} catch (JSONException e) {
			}
		}
	}
}

