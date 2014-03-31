package com.lipberry.customalertdilog;

import java.util.ArrayList;
import com.lipberry.utility.Constants;
import com.lipberry.R;
import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.fragment.CategoryTabFragment;
import com.lipberry.model.LikeMember;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class LisAlertDialog {
	CategoryTabFragment parent3;
	Context context;
	ArrayList< String>aList;
	Activity activity;
	HomeTabFragment parent;
	ArrayList<LikeMember>likememberlist;
	 
	public LisAlertDialog(Context context,ArrayList< LikeMember>likememberlist,Activity activity,HomeTabFragment parent,CategoryTabFragment parent3){
			
			this.parent3=parent3;
			this.parent=parent;
			this.context=context;	
			this.likememberlist=likememberlist;
			aList=new ArrayList<String>();
		
			for(int i=0;i<this.likememberlist.size();i++){
				aList.add(likememberlist.get(i).getNickname());
			}
			//this.aList=aList;
			this.activity=activity;
			
			
			
	}
	
	
	public void show_alert() {
      ListView list_alert1;
      Button cancel_button;
		 final Dialog dia = new Dialog(context);
		 dia.setContentView(R.layout.list_alert);
		 cancel_button=(Button) dia.findViewById(R.id.cancel_button);
		 dia.setTitle("Select File to import");
		 dia.setCancelable(true);
		 list_alert1 = (ListView) dia.findViewById(R.id.alert_list);
		  ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
	                 activity, 
	                 android.R.layout.simple_list_item_1,
	                 aList );
		  cancel_button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dia.cancel();
				
			}
		});

		 list_alert1.setAdapter(arrayAdapter); 
		 
		 list_alert1.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				
				if(parent!=null){
					Constants.userid=likememberlist.get(position).getLikemember_id();
					parent.startMemberFragment();
				}
				else{
					Constants.userid=likememberlist.get(position).getLikemember_id();
					parent3.startFragmentMemberFromCategories();
				}
				dia.cancel();
				
			}
		});
		 
		 
		 
		 dia.show();
       
    }
	
	public void gomemberpage(int position){
		
	}
	
}
