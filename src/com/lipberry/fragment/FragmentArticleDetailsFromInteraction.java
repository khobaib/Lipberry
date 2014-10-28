package com.lipberry.fragment;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.ShowHtmlText;
import com.lipberry.adapter.CustomAdapter;
import com.lipberry.adapter.CustomAdapterForComment;
import com.lipberry.customalertdilog.LisAlertDialog;
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

@SuppressLint({ "NewApi", "ResourceAsColor", "ValidFragment" })
public class FragmentArticleDetailsFromInteraction extends Fragment {
	LipberryApplication appInstance;
	ImageLoader imageLoader;
	ProgressDialog pd;
	private static ArticleDetails articledetails;
	ScrollView scrollView1;
	ListView list_comment;
	ListView lst_imag;
	int state = 0;
	CustomAdapterForComment adapter1;
	boolean followstate = false;
	public HomeTabFragment parent;
	TextView text_user_name, text_date_other, txt_articl_ename, text_topic_text, txt_like, text_comment, txt_viewd;
	ImageView img_pro_pic, img_article, img_like, image_comments, image_share;
	Button btn_follow_her, visit, btn_report;// btn_photo_album
	JsonParser jsonParser;
	ImageLoadingListener imll;
	EditText et_comment;
	String commentstext;
	ImageView play_vedio;
	Commentslist commentslist;
	static Activity activity;
	LinearLayout vedio_view_holder;
	View view_gap_list, view_gap_list2;
	String member_id;
	WebView web_view;
	String memberid;
	private static int from;

	public static FragmentArticleDetailsFromInteraction newInstance(ArticleDetails articledetails, int from) {
		FragmentArticleDetailsFromInteraction.articledetails = articledetails;
		FragmentArticleDetailsFromInteraction.from = from;
		return new FragmentArticleDetailsFromInteraction();
	}

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(false).cacheOnDisc(false)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity().getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions).build();
		imageLoader = ImageLoader.getInstance();
		ImageLoader.getInstance().init(config);
		activity = getActivity();
		Log.e("details", "onCreate");
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null)
			return;
		if (listAdapter.getCount() <= 0)
			return;
		int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(Utility.getDeviceWidth(activity),
				View.MeasureSpec.AT_MOST);
		int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		int totalHeight = 0;
		View view = null;
		for (int i = 0; i < listAdapter.getCount(); i++) {
			view = listAdapter.getView(i, view, listView);
			if (view instanceof ViewGroup) {
				view.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			}
			view.measure(widthMeasureSpec, heightMeasureSpec);
			totalHeight += view.getMeasuredHeight();
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public static void setListViewHeightBasedOnChildrenImage(ListView listView, int height) {
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
		params.height = totalHeight - 5;
		listView.setLayoutParams(params);
	}

	public void setmemberlist1() {
		list_comment.setFocusable(false);
		if (commentslist.getCommentslist().size() > 0) {
			view_gap_list.setVisibility(View.VISIBLE);
			view_gap_list2.setVisibility(View.VISIBLE);
			adapter1 = new CustomAdapterForComment(getActivity(), commentslist.getCommentslist(), Constants.baseurl
					+ "article/commentlist/" + articledetails.getId(), 0, null, null, this);
			list_comment.setAdapter(adapter1);
			setListViewHeightBasedOnChildren(list_comment);
			scrollView1.scrollTo(0, 0);
			scrollView1.fullScroll(View.FOCUS_UP);

		} else {
			view_gap_list.setVisibility(View.GONE);
			view_gap_list2.setVisibility(View.GONE);
			scrollView1.scrollTo(0, 0);

		}
		img_pro_pic.requestFocus();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Log.e("details", "onCreateView");

		appInstance = (LipberryApplication) getActivity().getApplication();
		jsonParser = new JsonParser();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_article_details, container, false);
		initview(v);
		if (Constants.isOnline(getActivity())) {

			new AsyncTaskGetComments(0).execute();
			if (articledetails.getFollow_flag() != null) {
				if (!articledetails.getFollow_flag().equals("Not a follower")) {
					followstate = true;
				} else {
					followstate = false;
				}
			}
			setview();
			// new AsyncTaskgetArticleDetails().execute();
		} else {
			Toast.makeText(getActivity(), getResources().getString(R.string.Toast_check_internet), Toast.LENGTH_SHORT)
					.show();
		}
		return v;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.e("details", "onResume");

		((HomeActivity) getActivity()).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity) getActivity()).backbuttonoftab.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (state == 0) {
					parent.onBackPressed();
					if (from == 10) {
						((HomeActivity) getActivity()).mTabHost.setCurrentTab(0);
					} else {
						((HomeActivity) getActivity()).mTabHost.setCurrentTab(from);
					}

				} else {
					vedio_view_holder.setVisibility(View.GONE);
					state = 0;
				}
			}
		});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		Log.e("details", "onPause");

		((HomeActivity) getActivity()).img_cat_icon.setVisibility(View.GONE);

		super.onPause();
	}

	// private class AsyncTaskgetArticleDetails extends AsyncTask<Void, Void,
	// ServerResponse> {
	// @Override
	// protected ServerResponse doInBackground(Void... params) {
	// try {
	// JSONObject loginObj = new JSONObject();
	// loginObj.put("session_id", appInstance.getUserCred().getSession_id());
	// String loginData = loginObj.toString();
	// String url =Constants.baseurl+"article/findarticlebyid/"+article_id;
	// ServerResponse response
	// =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
	// loginData, null);
	// return response;
	// } catch (JSONException e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	// @Override
	// protected void onPostExecute(ServerResponse result) {
	// super.onPostExecute(result);
	// if((pd!=null)&&(pd.isShowing())){
	// pd.dismiss();
	// }
	// JSONObject jobj=result.getjObj();
	// try {
	// String status=jobj.getString("status");
	// if(status.equals("success")){
	// new AsyncTaskGetComments(0).execute();
	// articledetails=ArticleDetails.getArticleDetails(jobj);
	// if(articledetails.getFollow_flag()!=null){
	// if(!articledetails.getFollow_flag().equals("Not a follower")){
	// followstate=true;
	// }
	// else{
	// followstate=false;
	// }
	// }
	// setview();
	// }
	// else{
	// String message=jobj.getString("message");
	// Toast.makeText(getActivity(),message, Toast.LENGTH_SHORT).show();
	// }
	// } catch (JSONException e) {
	// e.printStackTrace();
	// }
	// }
	// }
	public void initview(ViewGroup v) {
		scrollView1 = (ScrollView) v.findViewById(R.id.scrollView1);
		visit = (Button) v.findViewById(R.id.visit);
		list_comment = (ListView) v.findViewById(R.id.list_comment);
		lst_imag = (ListView) v.findViewById(R.id.lst_imag);
		text_user_name = (TextView) v.findViewById(R.id.text_user_name);
		text_date_other = (TextView) v.findViewById(R.id.text_date_other);
		txt_articl_ename = (TextView) v.findViewById(R.id.txt_articl_ename);
		text_topic_text = (TextView) v.findViewById(R.id.text_topic_text);
		txt_like = (TextView) v.findViewById(R.id.txt_like);
		text_comment = (TextView) v.findViewById(R.id.text_comment);
		txt_viewd = (TextView) v.findViewById(R.id.txt_viewd);
		img_pro_pic = (ImageView) v.findViewById(R.id.img_pro_pic);
		image_comments = (ImageView) v.findViewById(R.id.image_comments);
		img_like = (ImageView) v.findViewById(R.id.img_like);
		img_article = (ImageView) v.findViewById(R.id.img_article);
		btn_follow_her = (Button) v.findViewById(R.id.btn_follow_her);
		// btn_photo_album=(Button) v.findViewById(R.id.btn_photo_album);
		btn_report = (Button) v.findViewById(R.id.btn_report);
		image_share = (ImageView) v.findViewById(R.id.image_share);
		play_vedio = (ImageView) v.findViewById(R.id.play_vedio);
		vedio_view_holder = (LinearLayout) v.findViewById(R.id.vedio_view_holder);
		web_view = (WebView) v.findViewById(R.id.web_view);
		view_gap_list = v.findViewById(R.id.view_gap_list);
		view_gap_list2 = v.findViewById(R.id.view_gap_list2);
		text_user_name.setTypeface(Utility.getTypeface1(getActivity()));
		txt_articl_ename.setTypeface(Utility.getTypeface2(getActivity()));
		txt_like.setTypeface(Utility.getTypeface2(getActivity()));
		text_comment.setTypeface(Utility.getTypeface2(getActivity()));
		txt_viewd.setTypeface(Utility.getTypeface2(getActivity()));
		btn_report.setTypeface(Utility.getTypeface2(getActivity()));
		scrollView1.scrollTo(0, 0);

	}

	public void setCategorytitle() {

		if (articledetails.getCategory_title() != null) {
			((HomeActivity) getActivity()).welcome_title.setText(articledetails.getCategory_title());

		} else {
			if (articledetails.getCategory().equals("1")) {
				((HomeActivity) getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat1));
			}
			if (articledetails.getCategory().equals("2")) {
				// if(articledetails.getca.contains("shexp")){
				// ((HomeActivity)getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat2_shpx));
				// }
				// else{
				((HomeActivity) getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat2));

				// }
			}
			if (articledetails.getCategory().equals("3")) {
				((HomeActivity) getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat3));
			}
			if (articledetails.getCategory().equals("5")) {
				((HomeActivity) getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat5));
			}
			if (articledetails.getCategory().equals("8")) {
				((HomeActivity) getActivity()).welcome_title.setText(getResources().getString(R.string.txt_cat8));
			}
		}
		((HomeActivity) getActivity()).img_cat_icon.setVisibility(View.VISIBLE);

		if (articledetails.getCategory().equals("2")) {

			int id = getActivity().getResources().getIdentifier("catbl" + articledetails.getCategory(), "drawable",
					getActivity().getPackageName());
			((HomeActivity) getActivity()).img_cat_icon.setImageResource(id);

		} else {
			int id = getActivity().getResources().getIdentifier("catl" + articledetails.getCategory(), "drawable",
					getActivity().getPackageName());
			((HomeActivity) getActivity()).img_cat_icon.setImageResource(id);
		}
	}

	private class Callback extends WebViewClient { // HERE IS THE MAIN CHANGE.
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			return (false);
		}
	}

	public void setview() {
		// FIXME Activate the "Photo-Album" button?
		// if(articledetails.getArticle_gallery().size()<1){
		// btn_photo_album.setVisibility(View.GONE);
		// }
		// else{
		// btn_photo_album.setVisibility(View.VISIBLE);
		// }
		if ((articledetails.getVideo().equals("")) || (articledetails.getVideo() == null)) {
			play_vedio.setVisibility(View.GONE);
		}
		play_vedio.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				state = 1;
				vedio_view_holder.setVisibility(View.VISIBLE);
				web_view.setVisibility(View.VISIBLE);
				WebSettings webSettings = web_view.getSettings();
				webSettings.setJavaScriptEnabled(true);
				web_view.setWebViewClient(new Callback());
				web_view.loadUrl(articledetails.getVideo());
			}
		});
		setCategorytitle();
		image_share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
				sharingIntent.setType("text/plain");
				String shareBody = articledetails.getTitle() + "\n" + articledetails.getShort_url();
				sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
				startActivity(Intent.createChooser(sharingIntent,
						getActivity().getResources().getString(R.string.txt_shared_via)));
			}
		});
		txt_like.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				LisAlertDialog alert;
				if (articledetails.getLikedmemberlist().size() > 0) {
					alert = new LisAlertDialog(getActivity(), articledetails.getLikedmemberlist(), getActivity(),
							parent, null);
					alert.show_alert();
				} else {

				}
			}
		});
		if (articledetails.getAbuseFlag().equals("false")) {
			btn_report.setVisibility(View.VISIBLE);
		} else {
			btn_report.setVisibility(View.GONE);
		}
		String text = articledetails.getBody();
		text = text.replaceAll("\n", "<br />");
		text_topic_text.setText(Html.fromHtml(text));
		text_topic_text.setMovementMethod(LinkMovementMethod.getInstance());
		ShowHtmlText showtext = new ShowHtmlText(text_topic_text, getActivity());
		showtext.updateImages(true, text);
		text_user_name.setText(articledetails.getMember_name());
		text_date_other.setText(articledetails.getMember_username());
		text_user_name.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Constants.userid = articledetails.getMember();
				parent.startMemberFragment(0);

			}
		});
		img_pro_pic.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Constants.userid = articledetails.getMember();
				parent.startMemberFragment(0);

			}
		});

		txt_articl_ename.setText(articledetails.getTitle());
		if (articledetails.getVisit_counter().equals("")) {
			txt_viewd.setText("");
		} else {
			txt_viewd.setText("" + Long.parseLong(articledetails.getVisit_counter()));
		}
		txt_like.setText(articledetails.getLikemember_text());
		text_comment.setText(articledetails.getComment_count() + " " + getResources().getString(R.string.txt_comments));
		imageLoader.displayImage(articledetails.getMember_avatar(), img_pro_pic);
		imll = new ImageLoadingListener() {
			@Override
			public void onLoadingStarted(String imageUri, View view) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						getActivity().runOnUiThread(new Runnable() {
							public void run() {
								pd = ProgressDialog.show(getActivity(),
										getActivity().getResources().getString(R.string.app_name_arabic), getActivity()
												.getResources().getString(R.string.txt_please_wait), false);
							}
						});
					}
				});
			}

			@Override
			public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if ((pd.isShowing()) && (pd != null)) {
							pd.dismiss();
						}
					}
				});

				if (articledetails.getArticle_gallery().size() > 0) {
					CustomAdapter adapter = new CustomAdapter(getActivity(), articledetails.getArticle_gallery());
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
						public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
							imageLoader.loadImage(articledetails.getArticle_gallery().get(position).getImage_src(),
									imll);
						}
					});
				}
			}

			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {

						if ((pd.isShowing()) && (pd != null)) {
							pd.dismiss();
							Log.e("pd", "3");
						}
					}
				});
				int imageheight = Utility.getDeviceHeight(getActivity()) / 8;
				Bitmap bitmap = loadedImage;
				if (bitmap != null) {
					int bitmapheight = bitmap.getHeight();
					int bitmapweight = bitmap.getWidth();
					int deviceheight = Utility.getDeviceHeight(getActivity());
					int devicewidth = Utility.getDeviceWidth(getActivity());
					float ratio = (float) devicewidth / (float) bitmapweight;
					int resizebitmapwidth = devicewidth;
					float a = (bitmapheight * ratio);
					int resizebitmaphight = (int) a;
					imageheight = resizebitmaphight - 00;
					bitmap = Bitmap.createScaledBitmap(bitmap, resizebitmapwidth, resizebitmaphight, false);
					img_article.setImageBitmap(bitmap);
					int x = img_article.getLeft();
					int y = img_article.getTop();
					int h = img_article.getHeight();
					int w = img_article.getWidth();
				}
				if (articledetails.getArticle_gallery().size() > 0) {
					CustomAdapter adapter = new CustomAdapter(getActivity(), articledetails.getArticle_gallery());
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
						public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
							imageLoader.loadImage(articledetails.getArticle_gallery().get(position).getImage_src(),
									imll);
						}
					});
				}
			}

			@Override
			public void onLoadingCancelled(String imageUri, View view) {
				getActivity().runOnUiThread(new Runnable() {
					public void run() {
						if ((pd.isShowing()) && (pd != null)) {
							pd.dismiss();
						}

						if (articledetails.getArticle_gallery().size() > 0) {
							CustomAdapter adapter = new CustomAdapter(getActivity(), articledetails
									.getArticle_gallery());
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
								public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
									imageLoader.loadImage(articledetails.getArticle_gallery().get(position)
											.getImage_src(), imll);
								}
							});
						}
					}
				});
			}
		};
		if ((articledetails.getPhoto().equals("") || (articledetails.getPhoto() == null))) {
			img_article.setVisibility(View.GONE);
		} else {
			img_article.setVisibility(View.VISIBLE);

			imageLoader.loadImage(articledetails.getPhoto(), imll);
		}

		if (articledetails.getUserAlreadylikeThis() != null) {
			if (!articledetails.getUserAlreadylikeThis().equals("No")) {
				img_like.setBackgroundResource(R.drawable.unlike);
			} else {
				img_like.setBackgroundResource(R.drawable.like);
			}
		}
		if (articledetails.getMember_id().equals(appInstance.getUserCred().getId())) {
			btn_follow_her.setBackgroundResource(R.drawable.lfollowher_button);
			btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_show_self_follow));

		} else {
			if (followstate) {
				btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_following));
				btn_follow_her.setBackgroundResource(R.drawable.lfollowher_button);
				btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_already_following));

			} else {
				btn_follow_her.setBackgroundResource(R.drawable.lbtn_follow);
			}
		}

		btn_follow_her.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (articledetails.getMember_id().equals(appInstance.getUserCred().getId())) {

				} else {
					buttonfollowclicked();

				}
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
		// scrollView1.fullScroll(View.FOCUS_UP);
		scrollView1.fullScroll(View.FOCUS_UP);
	}

	public void imgeviewlikeclicked() {
		if (!articledetails.getUserAlreadylikeThis().equals("No")) {
			if (Constants.isOnline(getActivity())) {
				pd = ProgressDialog.show(getActivity(), getActivity().getResources()
						.getString(R.string.app_name_arabic),
						getActivity().getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskSetDislike().execute();
			} else {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		} else {

			if (Constants.isOnline(getActivity())) {
				pd = ProgressDialog.show(getActivity(), getActivity().getResources()
						.getString(R.string.app_name_arabic),
						getActivity().getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskSeLike().execute();
			} else {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
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
				String url = articledetails.getDislike_url();
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
			if ((pd.isShowing()) && (pd != null)) {
				pd.dismiss();
			}
			JSONObject jobj = result.getjObj();
			try {
				String status = jobj.getString("status");
				String description = jobj.getString("description");
				if (status.equals("success")) {
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
					img_like.setBackgroundResource(R.drawable.like);
					articledetails.setUserAlreadylikeThis("No");
				} else {
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
					if (description.equals("You pressed dislike before")) {
						img_like.setBackgroundResource(R.drawable.like);
						articledetails.setUserAlreadylikeThis("No");
					}
				}
			} catch (JSONException e) {
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
				String url = articledetails.getLike_url();
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
			if ((pd.isShowing()) && (pd != null)) {
				pd.dismiss();
			}
			JSONObject jobj = result.getjObj();
			try {
				String status = jobj.getString("status");
				String description = jobj.getString("description");
				if (status.equals("success")) {
					Toast.makeText(activity, activity.getResources().getString(R.string.txt_like_success),
							Toast.LENGTH_SHORT).show();
					img_like.setBackgroundResource(R.drawable.unlike);
					articledetails.setUserAlreadylikeThis("Yes");
				} else {
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
					if (description.equals("You presed like before")) {
						img_like.setBackgroundResource(R.drawable.unlike);
						articledetails.setUserAlreadylikeThis("Yes");
					}
				}
			} catch (JSONException e) {
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
				String url = articledetails.getAbuse_url();
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
			if ((pd.isShowing()) && (pd != null)) {
				pd.dismiss();
			}
			JSONObject jobj = result.getjObj();
			try {
				String description = jobj.getString("description");
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
				String url = Constants.baseurl + "account/cancelFollowmember/" + articledetails.getMember_id() + "/";
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
			if ((pd.isShowing()) && (pd != null)) {
				pd.dismiss();
			}
			JSONObject jobj = result.getjObj();
			try {
				String status = jobj.getString("status");
				String description = jobj.getString("description");
				if (status.equals("success")) {
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
					btn_follow_her.setText("follow");
					followstate = false;
				} else {
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
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
				String url = Constants.baseurl + "account/followmember/" + articledetails.getMember_id() + "/";
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
			if ((pd.isShowing()) && (pd != null)) {
				pd.dismiss();
			}
			JSONObject jobj = result.getjObj();
			try {
				String status = jobj.getString("status");
				String description = jobj.getString("description");
				if (status.equals("success")) {
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
					btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_following));
					btn_follow_her.setBackgroundColor(android.R.color.transparent);
					followstate = true;
				} else {
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
					if (description.equals("Already followed")) {
						btn_follow_her.setText(getActivity().getResources().getString(R.string.txt_following));
						btn_follow_her.setBackgroundColor(android.R.color.transparent);
						followstate = true;
					}
				}
			} catch (JSONException e) {
			}
		}
	}

	public void buttonfollowclicked() {
		if (!followstate) {
			if (Constants.isOnline(getActivity())) {
				pd = ProgressDialog.show(getActivity(), getActivity().getResources()
						.getString(R.string.app_name_arabic),
						getActivity().getResources().getString(R.string.txt_please_wait), false);
				new AsyncTaskSendFollowReq().execute();
			} else {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	public void CallReoprt() {
		if (Constants.isOnline(getActivity())) {
			pd = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskCallReoprt().execute();
		} else {
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
	}

	public void showCustomDialog() {
		final Dialog dialog = new Dialog(activity, R.style.CustomDialog);
		dialog.setTitle(activity.getResources().getString(R.string.app_name_arabic));

		dialog.setContentView(R.layout.custom_dilog);
		et_comment = (EditText) dialog.findViewById(R.id.et_comment);
		Button btn_cancel = (Button) dialog.findViewById(R.id.btn_cancel);
		Button bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
		btn_cancel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String comments = et_comment.getText().toString();
				if (comments.equals("")) {
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_enter_text),
							Toast.LENGTH_SHORT).show();
				} else {
					dialog.dismiss();
					commentstext = comments;
					sendposttoserver();
				}
			}
		});
		dialog.show();
	}

	public void sendposttoserver() {
		if (Constants.isOnline(getActivity())) {
			pd = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			new AsyncTaskPostComments().execute();
		} else {
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
				String url = articledetails.getComment_url();
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
			// if((pd.isShowing())&&(pd!=null)){
			// pd.dismiss();
			// }
			JSONObject jobj = result.getjObj();
			try {
				String status = jobj.getString("status");
				if (status.equals("success")) {
					if (status.equals("success")) {
						articledetails.setCommentcount((Integer.parseInt(articledetails.getComment_count()) + 1) + "");
						articledetails.setCommentcount((Integer.parseInt(articledetails.getComment_count()) + 1) + "");
						text_comment.setText(articledetails.getComment_count() + " "
								+ getResources().getString(R.string.txt_comments));
						if (Constants.isOnline(getActivity())) {

							if (commentslist != null) {
								new AsyncTaskGetComments(1).execute();
							} else {
								new AsyncTaskGetComments(0).execute();

							}
							Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_comment),
									Toast.LENGTH_SHORT).show();
						} else {
							if ((pd.isShowing()) && (pd != null)) {
								pd.dismiss();
							}
							Toast.makeText(getActivity(), getResources().getString(R.string.Toast_check_internet),
									Toast.LENGTH_SHORT).show();
						}

					}
				} else {
					String description = jobj.getString("description");
					Toast.makeText(getActivity(), description, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

	private class AsyncTaskGetComments extends AsyncTask<Void, Void, ServerResponse> {
		int a;
		ProgressDialog pd2;

		public AsyncTaskGetComments(int a) {
			this.a = a;
			;
			pd2 = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), true);
		}

		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				int endindex;
				endindex = 20;
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("startIndex", "0");
				loginObj.put("endIndex", "" + endindex);
				String loginData = loginObj.toString();
				String url = Constants.baseurl + "article/commentlist/" + articledetails.getId();
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
			if ((pd2 != null) && (pd2.isShowing())) {
				pd2.dismiss();
			}
			view_gap_list.setVisibility(View.GONE);
			view_gap_list2.setVisibility(View.GONE);
			JSONObject jobj = result.getjObj();
			new AsyncTaskUpdatePageVisit().execute();
			try {
				String status = jobj.getString("status");
				if (status.equals("success")) {
					commentslist = Commentslist.getCommentsListInstance(jobj);

					if (commentslist.getCommentslist().size() > 0) {
						Log.e("comment", "8");
						if (a == 0) {
							setmemberlist1();
						} else {
							setmemberlist1();
						}

					}
				} else {
					String description = jobj.getString("message");
					// Toast.makeText(getActivity(),description,
					// Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
			}
		}
	}

	private class AsyncTaskUpdatePageVisit extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			JSONObject loginObj = new JSONObject();
			String loginData = loginObj.toString();
			String url = articledetails.getUpdate_visitcounter_url();
			ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null, loginData,
					null);
			return response;

		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if (pd != null) {
				if (pd.isShowing()) {
					pd.dismiss();
				}
			}

		}
	}

	public void callback() {
		if (Constants.isOnline(getActivity())) {
			pd = ProgressDialog.show(getActivity(), getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_please_wait), false);
			if (commentslist != null) {
				new AsyncTaskGetComments(1).execute();
			} else {
				new AsyncTaskGetComments(0).execute();
			}
		} else {
			Toast.makeText(getActivity(), getResources().getString(R.string.Toast_check_internet), Toast.LENGTH_SHORT)
					.show();
		}
	}
}
