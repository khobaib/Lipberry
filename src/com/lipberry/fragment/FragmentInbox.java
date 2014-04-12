
package com.lipberry.fragment;

import java.util.ArrayList;
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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.bugsense.trace.Utils;
import com.lipberry.HomeActivity;
import com.lipberry.R;
@SuppressLint("NewApi")
public class FragmentInbox extends Fragment {
	InboxTabFragment parent;
	Button camera;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_inbox,
				container, false);
		camera=(Button) v.findViewById(R.id.camera);
		camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				showDiloag();
			}
		});
		return v;
	}
	
	
	public void showDiloag(){
	    Dialog dialog = new Dialog(getActivity());
	    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
	    builder.setTitle("Choose Image Source");
	    builder.setItems(new CharSequence[] { "Gallery", "Camera" },
	            new DialogInterface.OnClickListener() {

	                @Override
	                public void onClick(DialogInterface dialog,
	                        int which) {
	                    switch (which) {
	                    case 0:
	                        Intent intent = new Intent(
	                                Intent.ACTION_GET_CONTENT);
	                        intent.setType("image/*");

	                        Intent chooser = Intent
	                                .createChooser(
	                                        intent,
	                                        "Choose a Picture");
	                        Log.e("gallry","gallery");
	                     getActivity().startActivityForResult(
	                                chooser,
	                                0);

	                        break;

	                    case 1:
	                        Intent cameraIntent = new Intent(
	                                android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	                        Log.e("camera","camera");
	                        startActivityForResult(
	                                cameraIntent,
	                                1);

	                        break;

	                    default:
	                        break;
	                    }
	                }
	            });

	    builder.show();
	    dialog.dismiss();
	}
	
	
	 @Override
	 public void onActivityResult(int requestCode, int resultCode, Intent data) {
	     super.onActivityResult(requestCode, resultCode, data);
	   Log.e("OnActivityResult","OnActivityResult");
	     if (resultCode == getActivity().RESULT_OK) {
	         if (requestCode == 0) {
	             
	         } else if (requestCode == 1 ){
	             Bitmap photo = (Bitmap) data.getExtras()
	                     .get("data");
	            // imgJuice.setImageBitmap(photo);
	         }
	     }
	 }
}

