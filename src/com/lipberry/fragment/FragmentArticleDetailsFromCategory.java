
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.LoginActivity;
import com.lipberry.R;
import com.lipberry.SplashActivity;
import com.lipberry.adapter.CustomAdapter;
import com.lipberry.model.Article;
import com.lipberry.model.ArticleDetails;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;



@SuppressLint("NewApi")
public class FragmentArticleDetailsFromCategory extends Fragment {
	LipberryApplication appInstance;
	ImageLoader imageLoader;
	ProgressDialog pd;
	ArticleDetails articledetails;
	ListView lst_imag;
	 boolean followstate=false;
	CategoryTabFragment parent;
	TextView text_user_name,text_date_other,txt_articl_ename,text_topic_text,txt_like,text_comment,txt_viewd;
	ImageView img_pro_pic,img_article,img_like,image_comments;
	Button btn_follow_her,btn_photo_album,btn_report;
	 JsonParser jsonParser;
	 ImageLoadingListener imll;
	 EditText et_comment;
	 String commentstext;
	@SuppressLint("NewApi")
	Article article;
	 public void setArticle(Article article){
		 this.article=article;
		 
	 }
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true).build();
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
						getActivity().getApplicationContext()).defaultDisplayImageOptions(
									defaultOptions).build();
				imageLoader = ImageLoader.getInstance();
				ImageLoader.getInstance().init(config);
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
	    		     Toast.makeText(getActivity(), getResources().getString(R.string.Toast_check_internet), 10000).show();
	    	  	}
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
						

							Log.d("rtes", response.getjObj().toString());
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
							if(articledetails.getFollow_flag()!=null){
								if(!articledetails.getFollow_flag().equals("Not a follower")){
									followstate=true;
								}
								else{
									followstate=false;
								}
							}
							
							setview();
							//Log.i("gallery", articledetails.getArticle_gallery().get);
							Log.i("comment", articledetails.getCommentlist_url());
							Log.i("comment count", articledetails.getComment_count());
						}
						else{
							String message=jobj.getString("message");
							Toast.makeText(getActivity(),message, 10000).show();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
    
			}
		}
	
	
	public void initview(ViewGroup v){
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
	}
	
	public void setview(){
		
		text_user_name.setText(article.getMember_username());
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
				    	pd=new ProgressDialog(getActivity());
						pd.setTitle("Image is  Loading");
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
				
				
				
			}
			
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				// TODO Auto-generated method stub
				
				getActivity().runOnUiThread(new Runnable(){
				    public void run(){
				    	if((pd.isShowing())&&(pd!=null)){
							pd.dismiss();
						}
				    }
				});
				
				
				Bitmap bitmap=loadedImage;
				
				int bitmapheight=bitmap.getHeight();
				int bitmapweight=bitmap.getWidth();
				int deviceheight=Utility.getDeviceHeight(getActivity());
				int devicewidth=Utility.getDeviceWidth(getActivity());
				float ratio=(float)devicewidth/(float)bitmapweight;
				int resizebitmapwidth=devicewidth;
				float a=(bitmapheight*ratio);
				int resizebitmaphight=(int)a ;
				
				Log.i("image size", bitmapheight+"  "+bitmapweight+"  "+deviceheight+" "+devicewidth+" "+ratio+" "+resizebitmapwidth);
				bitmap=Bitmap.createScaledBitmap(bitmap,resizebitmapwidth,resizebitmaphight, false);
				img_article.setImageBitmap(bitmap);
				
				if(articledetails.getArticle_gallery().size()>0){
					CustomAdapter adapter=new CustomAdapter(getActivity(), articledetails.getArticle_gallery());
					lst_imag.setAdapter(adapter);
					//updateListViewHeight(lst_imag);
					lst_imag.setOnTouchListener(new OnTouchListener() {
					    // Setting on Touch Listener for handling the touch inside ScrollView
					    @Override
					    public boolean onTouch(View v, MotionEvent event) {
					    // Disallow the touch request for parent scroll on touch of child view
					    v.getParent().requestDisallowInterceptTouchEvent(true);
					    return false;
					    }

					});
					lst_imag.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int position, long arg3) {
							// TODO Auto-generated method stub
							imageLoader.loadImage(articledetails.getArticle_gallery().get(position).getImage_src(), imll);
						}
					});
				}
			}
			
			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				// TODO Auto-generated method stub
				
				getActivity().runOnUiThread(new Runnable(){
				    public void run(){
				    	if((pd.isShowing())&&(pd!=null)){
							pd.dismiss();
						}
				    }
				});
			}
		};
		imageLoader.loadImage(articledetails.getPhoto(), imll);
		
		if(article.getUserAlreadylikeThis()!=null){
			if(!article.getUserAlreadylikeThis().equals("No")){
				img_like.setBackgroundResource(R.drawable.unlike);
			}
			else{
				img_like.setBackgroundResource(R.drawable.like);
			}
		}
		
		if(followstate){
			btn_follow_her.setText("unfollow");
		}
		else{
			btn_follow_her.setText("follow");
		}
		btn_follow_her.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				buttonfollowclicked();
			}
		});
		
		image_comments.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				showCustomDialog();
			}
		});
	 img_like.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			imgeviewlikeclicked();
		}
	 });
	 btn_report.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
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
			
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
			}
			
		}
			else{
				
					if(Constants.isOnline(getActivity())){
						pd=ProgressDialog.show(getActivity(), "Lipberry",
						    "Sending like", true);
						new AsyncTaskSeLike().execute();
					
					}
					else{
					
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
					}
			
			
		}
		

	}
	
	
	private class AsyncTaskSetDislike extends AsyncTask<Void, Void, ServerResponse> {
		@Override
					protected ServerResponse doInBackground(Void... params) {

					try {
							JSONObject loginObj = new JSONObject();
							loginObj.put("session_id", appInstance.getUserCred().getSession_id());
							String loginData = loginObj.toString();
							String url =articledetails.getDislike_url();
							Log.i("url", url);
							ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
									loginData, null);

							Log.i("rtes", response.getjObj().toString());
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
					
						Toast.makeText(getActivity(),description, 10000).show();
						img_like.setBackgroundResource(R.drawable.like);
						article.setUserAlreadylikeThis("No");
					}
					else{
						Toast.makeText(getActivity(),description, 10000).show();
						
						
							if(description.equals("You pressed dislike before")){
								img_like.setBackgroundResource(R.drawable.like);
								
								article.setUserAlreadylikeThis("No");
						}
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
							Log.i("rtes", response.getjObj().toString());
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
						Toast.makeText(getActivity(),description, 10000).show();
						img_like.setBackgroundResource(R.drawable.unlike);
						article.setUserAlreadylikeThis("Yes");
					}
				else{
						
						
					Toast.makeText(getActivity(),description, 10000).show();
					
							if(description.equals("You presed like before")){
								
								img_like.setBackgroundResource(R.drawable.unlike);
								article.setUserAlreadylikeThis("Yes");
						}
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
							Log.i("rtes", response.getjObj().toString());
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
			/*	try {
					String status= jobj.getString("status");
					String description=jobj.getString("description");
					if(status.equals("success")){
						Toast.makeText(getActivity(),description, 10000).show();
						img_like.setBackgroundResource(R.drawable.unlike);
						article.setUserAlreadylikeThis("Yes");
					}
				else{
						
						
					Toast.makeText(getActivity(),description, 10000).show();
					
							if(description.equals("You presed like before")){
								
								img_like.setBackgroundResource(R.drawable.unlike);
								article.setUserAlreadylikeThis("Yes");
						}
						
						
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
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

								Log.i("follow", response.getjObj().toString());
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
							
							Toast.makeText(getActivity(),description, 10000).show();
							btn_follow_her.setText("follow");
							followstate=false;
						}
						else{
							
							Toast.makeText(getActivity(),description, 10000).show();
							
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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

								Log.i("follow", response.getjObj().toString());
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
							
							Toast.makeText(getActivity(),description, 10000).show();
							btn_follow_her.setText("unfollow");
							followstate=true;
						}
						else{
							
							Toast.makeText(getActivity(),description, 10000).show();
							if(description.equals("Already followed")){
									btn_follow_her.setText("unfollow");
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
			
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
			}
			
		}
		else{
				
					if(Constants.isOnline(getActivity())){
						pd=ProgressDialog.show(getActivity(), "Lipberry",
						    "Please wait", true);
						new AsyncTaskSendUnFollowReq().execute();
					
					}
					else{
					
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
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
			
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
			}
			
		
			
	}
	
	public void showCustomDialog(){
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.custom_dilog);
		dialog.setTitle("Lipberry");

		// set the custom dialog components - text, image and button
		et_comment =  (EditText) dialog.findViewById(R.id.et_comment);
		Button  btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		Button  bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		// if button is clicked, close the custom dialog
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
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_enter_text), 10000).show();
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
	
	public void sendposttoserver(){
		if(Constants.isOnline(getActivity())){
			pd=ProgressDialog.show(getActivity(), "Lipberry",
			    "Posting comments", true);
			new AsyncTaskPostComments().execute();
		
		}
		else{
		
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet), 10000).show();
		}
	}
	
	private class AsyncTaskPostComments extends AsyncTask<Void, Void, ServerResponse> {
		@Override
					protected ServerResponse doInBackground(Void... params) {

					try {
							JSONObject loginObj = new JSONObject();
							loginObj.put("session_id", appInstance.getUserCred().getSession_id());
							loginObj.put("comment", commentstext);
							String loginData = loginObj.toString();
							String url =articledetails.getComment_url();
							ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
									loginData, null);

							Log.i("rtes", response.getjObj().toString());
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
					if(status.equals("success")){
						Toast.makeText(getActivity(),"You just commented! ", 10000).show();
					}
					else{
						String description=jobj.getString("description");
						Toast.makeText(getActivity(),description, 10000).show();
					}
					
					
						
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
	}


}

