/**
 * 
 */
package com.lipberry.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lipberry.R;
import com.lipberry.model.MemberForSendMessage;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @author Touhid
 * 
 */
public class FoundMemberListAdapter extends ArrayAdapter<MemberForSendMessage> {

	private static final String TAG = "FoundMemberListAdapter";

	private ArrayList<MemberForSendMessage> memberList;
	private LayoutInflater mInflater;
	private ImageLoader imgLoader;

	public FoundMemberListAdapter(Context context, ArrayList<MemberForSendMessage> memberList) {
		super(context, 0);
		this.memberList = memberList;
		this.memberList = memberList;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
//		.cacheInMemory(true).cacheOnDisc(true).build();
//		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
//				context.getApplicationContext()).defaultDisplayImageOptions(
//						defaultOptions).build();
		imgLoader = ImageLoader.getInstance();
//		ImageLoader.getInstance().init(config);
	}

	@Override
	public int getCount() {
		return memberList.size();
	}

	@Override
	public MemberForSendMessage getItem(int position) {
		return memberList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	private class ViewHolder {
		public TextView tvUName, tvNName;
		public ImageView ivAvatar;
	}

	@Override
	public View getView(int position, View cv, ViewGroup parent) {
		ViewHolder h = null;
		if (cv == null) {
			h = new ViewHolder();
			cv = mInflater.inflate(R.layout.found_member_row, parent, false);

			h.tvNName = (TextView) cv.findViewById(R.id.tvUserNickNameFoundMember);
			h.tvUName = (TextView) cv.findViewById(R.id.tvUserNameFoundMember);
			h.ivAvatar = (ImageView) cv.findViewById(R.id.ivAvatarFoundMember);

			cv.setTag(h);
		} else
			h = (ViewHolder) cv.getTag();

		MemberForSendMessage mem = (MemberForSendMessage) getItem(position);
		h.tvNName.setText(mem.getUserName());
		h.tvUName.setText(mem.getNickName());
		Log.i(TAG, "User pic url: " + mem.getAvatar());
		imgLoader.displayImage(mem.getAvatar(), h.ivAvatar);

		return cv;
	}

	public void setData(ArrayList<MemberForSendMessage> newMemberList) {
		clear();
		memberList.clear();
		if (newMemberList != null) {
			for (MemberForSendMessage memberForSendMessage : newMemberList) {
				Log.i(TAG, "Setting member: " + memberForSendMessage.getUserName());
				add(memberForSendMessage);
				memberList.add(memberForSendMessage);
			}
		}
	}

}
