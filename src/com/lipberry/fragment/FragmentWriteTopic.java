
package com.lipberry.fragment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.adapter.NothingSelectedSpinnerAdapter;
import com.lipberry.model.Categories;
import com.lipberry.model.ImageScale;
import com.lipberry.model.ServerResponse;
import com.lipberry.parser.JsonParser;
import com.lipberry.utility.Constants;
import com.lipberry.utility.LipberryApplication;
@SuppressLint("NewApi")
public class FragmentWriteTopic extends Fragment {
    TextView txt_topic,txt_text,txt_tag;
	Button btn_select_photo,btn_go;
	WriteTopicTabFragment parent;
	ArrayList< Categories>categorylist;
	Spinner spinner_category;
	int selsectedspinnerposition=-1;
	ProgressDialog pd;
	LipberryApplication appInstance;
	JsonParser jsonParser;
	ArrayList<String>catnamelist;
	ImageScale bitmapimage;
	public  String photofromcamera;
	public  String drectory;
	String title,category_id,category_prefix,body,photo,video;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jsonParser=new JsonParser();
		categorylist=new ArrayList<Categories>();
		catnamelist=new ArrayList<String>();
		appInstance = (LipberryApplication) getActivity().getApplication();
	}
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		selsectedspinnerposition=-1;
		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
			ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_write_topic,
				container, false);
			btn_go=(Button) v.findViewById(R.id.btn_go);
			btn_go.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					startwritetopic();
				}
			});
			btn_select_photo=(Button) v.findViewById(R.id.btn_select_photo);
			btn_select_photo.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					captureimage();
				}
			});
			spinner_category=(Spinner) v.findViewById(R.id.spinner_category);
			((HomeActivity)getActivity()).welcome_title.setText(R.string.txt_write_topic);

			if(Constants.isOnline(getActivity())){
				pd=ProgressDialog.show(getActivity(), "Lipberry",
						"Retreving categories", true);
				new AsyncTaskgetCategories().execute();
			}
			else{
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_check_internet),
						Toast.LENGTH_SHORT).show();
			}
			
		return v;
	}
	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
	}
	
	private class AsyncTaskgetCategories extends AsyncTask<Void, Void, ServerResponse> {
		@Override
		protected ServerResponse doInBackground(Void... params) {
			try {
				JSONObject loginObj = new JSONObject();
				loginObj.put("session_id", appInstance.getUserCred().getSession_id());
				String loginData = loginObj.toString();
				String url =Constants.baseurl+"category/categorylist";
				ServerResponse response =jsonParser.retrieveServerData(Constants.REQUEST_TYPE_POST, url, null,
						loginData, null);
				return response;
			} catch (JSONException e) { 
				if(pd!=null){
					if((pd.isShowing())){
						pd.dismiss();
					}
				}
				return null;
			}
		}
		@Override
		protected void onPostExecute(ServerResponse result) {
			super.onPostExecute(result);
			if((pd.isShowing())&&(pd!=null)){
				pd.dismiss();
			}
			JSONObject res=result.getjObj();
			try {
				String status=res.getString("status");
				if(status.equals("success")){
					JSONArray jarraArray=res.getJSONArray("categorylist");
					categorylist.clear();
					for(int i=0;i<jarraArray.length();i++){
						JSONObject job=jarraArray.getJSONObject(i);
						Categories cat=new Categories();
						cat=cat.parsecaCategories(job);
						categorylist.add(cat);
					}

					if(categorylist.size()>0){
						catnamelist.clear();
						for(int i=0;i<categorylist.size();i++){
							catnamelist.add(categorylist.get(i).getName());
						}
						generateSpinner();
					}
					else{
						Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
								Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
							Toast.LENGTH_SHORT).show();
				}
			} catch (JSONException e) {
				Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.Toast_nocat_found),
						Toast.LENGTH_SHORT).show();
			}
		}
	}
	
	private void generateSpinner() {
			ArrayAdapter< String>adapter = new ArrayAdapter<String>(getActivity(),
					android.R.layout.simple_spinner_dropdown_item, catnamelist);
			spinner_category.setAdapter(adapter);
			adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
	  }
	  
		public void captureimage(){
			 
			//	
				final CharSequence[] options = { "Take Photo", "Choose from Gallery","Cancel" };
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("Add Photo!");
				builder.setItems(options, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int item) {
					createfolder();
                if (options[item].equals("Take Photo")){
                		photofromcamera=System.currentTimeMillis()+".jpg";
                		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                		File f = new File(drectory, photofromcamera);
                		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                		startActivityForResult(intent, 1);
                	}
                else if (options[item].equals("Choose from Gallery"))
                {
                		photofromcamera=System.currentTimeMillis()+".jpg";
                    	Intent intent = new   Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.
                    			Media.EXTERNAL_CONTENT_URI);
                    	startActivityForResult(intent, 3);
                }
                else if (options[item].equals("Cancel")) {
                		dialog.dismiss();
                }
            }
        });
        builder.show();
	
	  
  }
		
		public void createfolder(){
			String newFolder = "/Lipberry";
			String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
			drectory= extStorageDirectory + newFolder;
			File myNewFolder = new File(drectory);
			myNewFolder.mkdir();
	}

	public static boolean deleteDirectory(File path) {
		if( path.exists() ) {
			File[] files = path.listFiles();
			if (files == null) {
				return true;
			}
			for(int i=0; i<files.length; i++) {
				if(files[i].isDirectory()) {
					deleteDirectory(files[i]);
				}
				else {
					files[i].delete();
				}
			}
		}
		return( path.delete() );
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
       // Log.i("msz", ""+requestCode+"  "+RESULT_OK+" "+resultCode);
       if (resultCode == getActivity().RESULT_OK) {
        	
        	if (requestCode == 1) {
            			try{
            					String filepath = drectory+"/"+photofromcamera;
            					bitmapimage =new ImageScale();
            					Bitmap bitmap=bitmapimage.decodeImage(filepath);
            					String mBaseFolderPath =drectory+ "/";
            					String mFilePath = drectory+"/"+photofromcamera;
            					FileOutputStream stream = new FileOutputStream(mFilePath);
            					bitmap.compress(CompressFormat.JPEG,60, stream);
            					stream.flush();
            					stream.close();
            			}
            			catch(Exception e) {
            					 Log.e("Could not save", e.toString());
            			}
            	}
            							
           else if (requestCode == 3) {
            			try {
            				   File sd = Environment.getExternalStorageDirectory();
            				   File data1 = Environment.getDataDirectory();
            				   if (sd.canWrite()) {
            									Uri selectedImage = data.getData();
            									String[] filePath = { MediaStore.Images.Media.DATA };
            									Cursor c = getActivity().getContentResolver().query(selectedImage,filePath, 
            											null, null, null);
            									c.moveToFirst();
            									int columnIndex = c.getColumnIndex(filePath[0]);
            									String picturePath = c.getString(columnIndex);
            									c.close();
            									File dn= new File(drectory);
            									File source= new File(picturePath);
            									photofromcamera=String.valueOf(System.currentTimeMillis()) +".jpg";
            									File destination= new File(dn,photofromcamera);
            									if (source.exists()) {
            											FileChannel src = new FileInputStream(source).getChannel();
            											FileChannel dst = new FileOutputStream(destination).getChannel();
            											dst.transferFrom(src, 0, src.size());
            											src.close();
            											dst.close();
            											String filepath = drectory+"/"+photofromcamera;
            			        						bitmapimage =new ImageScale();
            			        						Bitmap bitmap=bitmapimage.decodeImage(filepath);
            			        						String mBaseFolderPath = drectory + "/";
            			        						String mFilePath = drectory+"/"+photofromcamera;
            			        						FileOutputStream stream = new FileOutputStream(mFilePath);
            			        						bitmap.compress(CompressFormat.JPEG, 60, stream);
            			        						stream.flush();
            			        						stream.close();
            										}
            							} 
            	    
            				}
            			  catch (Exception e) {
            					
            				}
            }
      }
	} 
	
	public void startwritetopic(){
		
	}

}

