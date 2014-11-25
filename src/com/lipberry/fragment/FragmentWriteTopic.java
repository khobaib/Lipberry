package com.lipberry.fragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.CustomAdaptergrid;
import com.lipberry.adapter.NothingSelectedSpinnerAdapter;
import com.lipberry.model.ArticleDetails;
import com.lipberry.model.Categories;
import com.lipberry.model.ImageScale;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Base64;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
import com.lipberry.utility.Utility;

@SuppressLint({ "NewApi", "DefaultLocale" })
public class FragmentWriteTopic extends Fragment {

	private final String TAG_T = "FragmentWriteTopic_Touhid";

	// btn_add_more_photo
	EditText txt_topic, txt_text, txt_tag,txt_vedio;
	Button btn_select_photo, btn_go;
	WriteTopicTabFragment parent;
	int pos = 1;
	Spinner spinner_category;
	int selectedSpinnerPos = 0;
	Activity activity;
	boolean writetopicsuccess = false;
	Bitmap scaledBmp;
	String catagoryid;
	ArrayList<String> galarylist = new ArrayList<String>();
	Bitmap bitmap;
	String article_id;
	ProgressDialog pd;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	ArrayList<String> catnamelist;
	ArrayList<Categories> categorylist;
	ImageScale bitmapimage;
	GridView grid_image;
	String title, category_id, category_prefix, body, photo, tags,string_txt_vedio;

	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = getActivity();
		jsonParser = new JsonParser();
		categorylist = new ArrayList<Categories>();
		catnamelist = new ArrayList<String>();

	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		activity = getActivity();
		jsonParser = new JsonParser();
		categorylist = new ArrayList<Categories>();
		catnamelist = new ArrayList<String>();
	}

	@Override
	public void onPause() {
		selectedSpinnerPos = 0;
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		loadGridview();
		((HomeActivity) activity).backbuttonoftab.setVisibility(View.VISIBLE);
		((HomeActivity) activity).backbuttonoftab.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				parent.onBackPressed();
			}
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		appInstance = (LipberryApplication) activity.getApplication();
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_write_topic, container, false);
		grid_image = (GridView) v.findViewById(R.id.grid_image);
		txt_topic = (EditText) v.findViewById(R.id.txt_topic);
		txt_text = (EditText) v.findViewById(R.id.txt_text);
		txt_tag = (EditText) v.findViewById(R.id.txt_tag);
		btn_go = (Button) v.findViewById(R.id.btn_go);
		txt_vedio=(EditText) v.findViewById(R.id.txt_vedio);
		txt_topic.setTypeface(Utility.getTypeface2(getActivity()));
		txt_text.setTypeface(Utility.getTypeface2(getActivity()));
		txt_tag.setTypeface(Utility.getTypeface2(getActivity()));
		btn_go.setTypeface(Utility.getTypeface1(getActivity()));
		btn_go.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (galarylist.size() > 0) {
					String filepath = galarylist.get(0);
					filepath = filepath.replace("/Lipberrythumb", "/Lipberryfinal1");
					File file=new File(filepath);
					bitmap=decodeFile(file, 500, 500);
					
				}
				startwritetopic();
			}
		});
		btn_select_photo = (Button) v.findViewById(R.id.btn_select_photo);
		btn_select_photo.setTypeface(Utility.getTypeface1(getActivity()));
		btn_select_photo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loadphoto();
			}
		});
		spinner_category = (Spinner) v.findViewById(R.id.spinner_category);
		((HomeActivity) activity).welcome_title.setText(R.string.txt_write_topic);
		if (Constants.isOnline(activity)) {
			pd = ProgressDialog.show(activity, getActivity().getResources().getString(R.string.app_name_arabic),
					getActivity().getResources().getString(R.string.txt_retreiving_category), false);

			new AsyncTaskgetCategories().execute();
		} else {
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}
		return v;
	}

	public void loadGridview() {

		File file = new File(Environment.getExternalStorageDirectory().toString() + "/Lipberrythumb");
		galarylist = getList(file);

		if (galarylist.size() > 0) {
			btn_select_photo.setText(getActivity().getResources().getString(R.string.txt_add_more_phto));
			grid_image.setVisibility(View.VISIBLE);
			// Bitmap bitmap =
			// BitmapFactory.decodeFile(Environment.getExternalStorageDirectory().toString()+
			// "/Lipberryfinal/"+galarylist.get(0));
			CustomAdaptergrid adapter = new CustomAdaptergrid(activity, galarylist);
			grid_image.setAdapter(adapter);
			grid_image.setOnTouchListener(new OnTouchListener() {
				@SuppressLint("ClickableViewAccessibility")
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					return false;
				}

			});
			grid_image.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parentView, View v, int pos, long id) {
					Log.e("name", galarylist.get(pos));

				}
			});
		} else {
			btn_select_photo.setText(getActivity().getResources().getString(R.string.txt_select_photo));
			grid_image.setVisibility(View.GONE);
		}

	}

	public void loadphoto() {
		((HomeActivity) activity).captureimage(false);
	}

	private class AsyncTaskgetCategories extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url = Constants.baseurl + "category/categorylist";
				ServerResponse response = jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				return response;
			} catch (JSONException e) {
				return null;
			}
		}

		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if (pd != null) {
				if ((pd.isShowing())) {
					pd.dismiss();
				}
			}
			JSONObject res = result.getjObj();
			try {
				String status = res.getString("status");
				if (status.equals("success")) {
					JSONArray jarraArray = res.getJSONArray("categorylist");
					categorylist.clear();
					for (int i = 0; i < jarraArray.length(); i++) {
						JSONObject job = jarraArray.getJSONObject(i);
						Categories cat = new Categories();
						cat = Categories.parsecaCategories(job);
						categorylist.add(cat);
					}
					if (categorylist.size() > 0) {
						catnamelist.clear();
						for (int i = 0; i < categorylist.size(); i++) {
							catnamelist.add(categorylist.get(i).getName());
						}
						generateSpinner();
					} else {
						Toast.makeText(activity, activity.getResources().getString(R.string.Toast_nocat_found),
								Toast.LENGTH_SHORT).show();
					}
				} else {
					Toast.makeText(activity, activity.getResources().getString(R.string.Toast_nocat_found),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(activity, activity.getResources().getString(R.string.Toast_nocat_found),
						Toast.LENGTH_SHORT).show();
			}
		}
	}

	/*
	 * ((TextView) parent.getChildAt(0)).setTextColor(Color.BLUE); ((TextView)
	 * parent.getChildAt(0)).setTextSize(5); private void generateSpinner() {
	 * 
	 * ArrayAdapter< String>adapter = new ArrayAdapter<String>(activity,
	 * R.layout.spinner_item, catnamelist);
	 * 
	 * spinner_category.setAdapter(adapter); spinner_category.setAdapter( new
	 * NothingSelectedSpinnerAdapter( adapter,
	 * R.drawable.contact_spinner_row_nothing_selected_category,
	 * getActivity())); //
	 * adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item
	 * ); spinner_category.setOnItemSelectedListener(new
	 * OnItemSelectedListener(){
	 * 
	 * public void onItemSelected(AdapterView<?> arg0, View arg1, int position,
	 * long arg3){ selsectedspinnerposition=position; }
	 * 
	 * @Override public void onNothingSelected(AdapterView<?> arg0) {
	 * selsectedspinnerposition=0; } }); }
	 */
	private void generateSpinner() {

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_item, catnamelist);
		spinner_category.setAdapter(adapter);
		spinner_category.setAdapter(new NothingSelectedSpinnerAdapter(adapter,
				R.drawable.contact_spinner_row_nothing_selected_category, getActivity()));
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_category.setOnItemSelectedListener(new OnItemSelectedListener() {
			public void onItemSelected(AdapterView<?> parent, View arg1, int position, long arg3) {
				((TextView) parent.getChildAt(0)).setTextColor(Color.parseColor("#777777"));
				((TextView) parent.getChildAt(0)).setTypeface(Utility.getTypeface2(getActivity()));
				((TextView) parent.getChildAt(0)).setGravity(Gravity.CENTER);
				selectedSpinnerPos = position - 1;
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				selectedSpinnerPos = 0;
			}
		});
	}

	private void startwritetopic() {
		// String title,category_id,category_prefix,body,photo,video;
	
		title = txt_topic.getText().toString();
		body = txt_text.getText().toString();
		tags = txt_tag.getText().toString();
		string_txt_vedio=txt_vedio.getText().toString();
		if (title.trim().equals("")) {
			Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.txt_please_enter_title),
					Toast.LENGTH_SHORT).show();
		}

		else if (spinner_category.getSelectedItemPosition()==0) {
			Toast.makeText(activity, getActivity().getResources().getString(R.string.txt_please_select_category),
					Toast.LENGTH_SHORT).show();
		}

		else if ((!body.equals("")) || (tags.equals("")) || (bitmap != null)) {

			if (Constants.isOnline(getActivity())) {
				pd = new ProgressDialog(getActivity());
				pd.setMessage(getActivity().getResources().getString(R.string.txt_writing_topic));
				pd.show();
				new AsyncTaskWriteTopic().execute();
			} else {
				Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}

		} else {
			Toast.makeText(getActivity(), getResources().getString(R.string.txt_please_required_field),
					Toast.LENGTH_SHORT).show();
		}
	}

	private class AsyncTaskWriteTopic extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject articleObj = new JSONObject();
				articleObj.put("session_id", appInstance.getUserCred().getSession_id());
				int s_pos=spinner_category.getSelectedItemPosition()-1;
				articleObj.put("category_id", categorylist.get(s_pos).getId());
				articleObj.put("category_prefix", categorylist.get(s_pos).getPrefix());
				byte[] ba1;
				String base64StrString;
				if (!body.equals("")) {
					ba1 = body.getBytes();
					base64StrString = Base64.encodeBytes(ba1);
					articleObj.put("body", base64StrString);
				}
				ba1 = title.getBytes();
				base64StrString = Base64.encodeBytes(ba1);
				articleObj.put("title", base64StrString);
				String filepath;
				if (galarylist.size() > 0) {
					filepath = galarylist.get(0);
					if (filepath != null) {
						filepath = filepath.replace("/Lipberrythumb", "/Lipberryfinal1");
						bitmap = BitmapFactory.decodeFile(filepath);
						
					}
				}
				if (bitmap != null) {
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 100, bao);
					byte[] ba = bao.toByteArray();
					String base64Str = Base64.encodeBytes(ba);
					articleObj.put("photo", base64Str);
					
				}
				articleObj.put("video", "");
				if(string_txt_vedio!=null){
					articleObj.put("video", string_txt_vedio);
				}
				// FIXME !!: video was here!
				articleObj.put("tags", getTagArray(tags));// FIXME !!
				String loginData = articleObj.toString();
				Log.e("request", loginData);
				String url = Constants.baseurl + "article/addarticle/";
				jsonParser=new JsonParser();
				
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
			JSONObject jobj = result.getjObj();
		

			try {
				String status = jobj.getString("status");
				bitmap = null;
				if (status.equals("success")) {

					FragmentMyCountriesPost.oncreatecallsate = true;
					FragmentMyFollwerPost.oncreatecallsate1 = true;
					Constants.writetopicsuccess = true;

					String article_info = jobj.getString("article_info");
					article_info = article_info.replace("{", "");
					article_info = article_info.replace("}", "");
					catagoryid = article_info.substring(article_info.indexOf(":") + 1);
					article_id = catagoryid;
					if (galarylist.size() > 1) {
						addGalaryToServer();
					} else {
						if (Constants.isOnline(getActivity())) {
							new AsyncTaskgetArticleDetails(0).execute();
						} else {
							Toast.makeText(getActivity(),
									getActivity().getResources().getString(R.string.Toast_check_internet),
									Toast.LENGTH_SHORT).show();
						}
						Toast.makeText(getActivity(), getResources().getString(R.string.txt_write_topic_success),
								Toast.LENGTH_SHORT).show();
					}

				} else {
					if (pd != null) {
						if ((pd.isShowing())) {
							pd.dismiss();
						}
					}
					Toast.makeText(getActivity(), getResources().getString(R.string.txt_write_topic_failure),
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				if (pd != null) {
					if ((pd.isShowing())) {
						pd.dismiss();
					}
				}
			}
		}
	}

	private class AsyncTaskAddGalaryImage extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				loginObj.put("category_prefix", categorylist.get(selectedSpinnerPos).getPrefix());

				if (bitmap != null) {
					ByteArrayOutputStream bao = new ByteArrayOutputStream();
					bitmap.compress(CompressFormat.JPEG, 60, bao);
					byte[] ba = bao.toByteArray();
					String base64Str = Base64.encodeBytes(ba);
					loginObj.put("pic_data", base64Str);
				} else {
				}
				String loginData = loginObj.toString();
				String url = Constants.baseurl + "article/addgallery/" + catagoryid;
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
			JSONObject jobj = result.getjObj();
			try {
				String status = jobj.getString("status");
				String description = jobj.getString("description");
				if (status.equals("success")) {
					if (pos < galarylist.size()) {
						if (Constants.isOnline(activity)) {
							String filepath = galarylist.get(pos);
							filepath = filepath.replace("/Lipberrythumb", "/Lipberryfinal1");
							File file=new File(filepath);
							bitmap=decodeFile(file, 500, 500);
							new AsyncTaskAddGalaryImage().execute();
							pos++;
						} else {
							Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
									Toast.LENGTH_SHORT).show();
							if (Constants.isOnline(getActivity())) {
								new AsyncTaskgetArticleDetails(0).execute();
							} else {
								Toast.makeText(getActivity(),
										getActivity().getResources().getString(R.string.Toast_check_internet),
										Toast.LENGTH_SHORT).show();
							}
						}
					} else {
						Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
						Constants.writetopicsuccess = false;
						if (Constants.isOnline(getActivity())) {
							new AsyncTaskgetArticleDetails(0).execute();
						} else {
							Toast.makeText(getActivity(),
									getActivity().getResources().getString(R.string.Toast_check_internet),
									Toast.LENGTH_SHORT).show();
						}
						String newFolder = "/Lipberryfinal1";
						String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
						String drectory = extStorageDirectory + newFolder;

						File myNewFolder = new File(drectory);

						String thumb = extStorageDirectory + "/Lipberrythumb";
						File thumbFolder = new File(thumb);
						deleteDirectory(thumbFolder);
						deleteDirectory(myNewFolder);
						createfolder();
					}
				} else {
					
					Constants.writetopicsuccess = false;
					if (Constants.isOnline(getActivity())) {
						new AsyncTaskgetArticleDetails(0).execute();
					} else {
						Toast.makeText(getActivity(),
								getActivity().getResources().getString(R.string.Toast_check_internet),
								Toast.LENGTH_SHORT).show();
					}
					Toast.makeText(activity, description, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
			}
		}
	}

	private ArrayList<String> getList(File parentDir) {

		ArrayList<String> inFiles = new ArrayList<String>();
		try {
			String[] fileNames = parentDir.list();

			for (String fileName : fileNames) {

				if ((fileName.toLowerCase().endsWith(".jpg")) || (fileName.toLowerCase().endsWith(".png"))) {
					inFiles.add(Environment.getExternalStorageDirectory().toString() + "/Lipberrythumb/" + fileName);

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return inFiles;
	}

	public JSONArray getTagArray(String tagString) {
		JSONArray tagArray = new JSONArray();

		// StringTokenizer tokens = new StringTokenizer(tagString, " ");
		// while (tokens.hasMoreTokens()) {
		try {
			// String tag = tokens.nextToken();
			JSONObject jt = new JSONObject();
			jt.put("tag_name", Base64.encodeBytes(tagString.getBytes()));
			tagArray.put(jt);
		} catch (JSONException je) {
			je.printStackTrace();
		}
		// }
	return tagArray;
	}

	private void addGalaryToServer() {
		pos = 0;
		if (Constants.isOnline(activity)) {
			String filepath = galarylist.get(pos);
			filepath = filepath.replace("/Lipberrythumb", "/Lipberryfinal1");
			File file=new File(filepath);
			bitmap=decodeFile(file, 500, 500);
			new AsyncTaskAddGalaryImage().execute();

			pos++;
		} else {
			Toast.makeText(activity, activity.getResources().getString(R.string.Toast_check_internet),
					Toast.LENGTH_SHORT).show();
		}

	}

	public static boolean deleteDirectory(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					deleteDirectory(files[i]);
				} else {
					files[i].delete();
				}
			}
		}
		return (path.delete());
	}

	public void createfolder() {
		String newFolder = "/Lipberryfinal1";
		String thumb = "/Lipberrythumb";

		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		String drectory = extStorageDirectory + newFolder;
		String drectorythumb = extStorageDirectory + thumb;
		File myNewFolder = new File(drectory);
		myNewFolder.mkdir();
		File myNewFolderthumb = new File(drectorythumb);
		myNewFolderthumb.mkdir();
	}

	private class AsyncTaskgetArticleDetails extends AsyncTask<Void, Void, ServerResponse> {
		@SuppressWarnings("unused")
		int position;

		public AsyncTaskgetArticleDetails(int position) {
			this.position = position;
		}

		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url = Constants.baseurl + "article/findarticlebyid/" + article_id;
				
				int s_pos=spinner_category.getSelectedItemPosition()-1;
				if(s_pos==5){
					 url = Constants.baseurl + "experience/experiencePage/" + article_id;
				}
				Log.e("Url", url);
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
			Log.e("result", result.getjObj().toString());
			if ((pd != null) && (pd.isShowing())) {
				pd.dismiss();
			}
			JSONObject jobj = result.getjObj();
			try {
				String status = jobj.getString("status");
				if (status.equals("success")) {
					txt_topic.setText("");
					txt_topic.setHint(R.string.txt_title);
					txt_text.setText("");
					txt_text.setHint(R.string.txt_article_body);
					txt_tag.setText("");
					txt_tag.setHint(R.string.txt_video_url);
					String newFolder = "/Lipberryfinal1";
					String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
					String drectory = extStorageDirectory + newFolder;
					File myNewFolder = new File(drectory);
					String thumb = extStorageDirectory + "/Lipberrythumb";
					File thumbFolder = new File(thumb);
					deleteDirectory(thumbFolder);
					deleteDirectory(myNewFolder);
					generateSpinner();
					ArticleDetails articledetails = ArticleDetails.getArticleDetails(jobj);
					Constants.GO_ARTCLE_PAGE_FROM_MEMBER = true;
					Constants.articledetails = articledetails;
					Constants.from = 10;
					((HomeActivity) getActivity()).mTabHost.setCurrentTab(4);
				} else {
					String message = jobj.getString("description");
					Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		String newFolder = "/Lipberryfinal1";
		String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
		String drectory = extStorageDirectory + newFolder;
		File myNewFolder = new File(drectory);
		String thumb = extStorageDirectory + "/Lipberrythumb";
		File thumbFolder = new File(thumb);
		deleteDirectory(thumbFolder);
		deleteDirectory(myNewFolder);
		createfolder();
	}
	 public static Bitmap decodeFile(File f,int WIDTH,int HIGHT){
		 try {
		     //Decode image size
		     BitmapFactory.Options o = new BitmapFactory.Options();
		     o.inJustDecodeBounds = true;
		     BitmapFactory.decodeStream(new FileInputStream(f),null,o);

		     //The new size we want to scale to
		     final int REQUIRED_WIDTH=WIDTH;
		     final int REQUIRED_HIGHT=HIGHT;
		     //Find the correct scale value. It should be the power of 2.
		     int scale=1;
		     while(o.outWidth/scale/2>=REQUIRED_WIDTH && o.outHeight/scale/2>=REQUIRED_HIGHT)
		         scale*=2;

		     //Decode with inSampleSize
		     BitmapFactory.Options o2 = new BitmapFactory.Options();
		     o2.inSampleSize=scale;
		     return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		 } catch (FileNotFoundException e) {}
		 return null;
		}
}
