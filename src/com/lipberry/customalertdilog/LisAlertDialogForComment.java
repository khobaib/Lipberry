package com.lipberry.customalertdilog;
import java.util.ArrayList;
import com.lipberry.utility.Constants;
import com.lipberry.utility.Utility;
import com.lipberry.R;
import com.lipberry.adapter.CustomAdapterForCommentFromDialog;
import com.lipberry.fragment.HomeTabFragment;
import com.lipberry.fragment.CategoryTabFragment;
import com.lipberry.model.Comments;
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
public class LisAlertDialogForComment {
	CategoryTabFragment parent3;
	Context context;
	Activity activity;
	HomeTabFragment parent;
	ArrayList<Comments>comments;
	String url;
	public LisAlertDialogForComment(Context context,ArrayList<Comments>comments,Activity activity,
			HomeTabFragment parent, CategoryTabFragment parent3,String url){
		this.url=url;
		this.parent3=parent3;
		this.parent=parent;
		this.context=context;	
		this.comments=comments;
		this.activity=activity;
	}
	public void show_alert() {
		ListView list_alert1;
		Button cancel_button;
		final Dialog dia = new Dialog(context,R.style.CustomDialog);
		dia.setContentView(R.layout.list_alert);
		cancel_button=(Button) dia.findViewById(R.id.cancel_button);
		dia.setTitle(activity.getResources().getString(R.string.app_name_arabic));
		dia.setCancelable(true);
		list_alert1 = (ListView) dia.findViewById(R.id.alert_list);
		CustomAdapterForCommentFromDialog adapter=new CustomAdapterForCommentFromDialog(activity, comments, url, parent, parent3);
		cancel_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dia.cancel();
			}
		});
		list_alert1.setAdapter(adapter); 
		list_alert1.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				if(parent!=null){
					Constants.userid=comments.get(position).getMember_id();
					parent.startMemberFragment(0);
				}
				else{
					Constants.userid=comments.get(position).getMember_id();
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
