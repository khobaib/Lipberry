package com.lipberry.newfrag;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.v4.app.Fragment;


public class TabInfo implements Parcelable {
	public final String tag;
	public final Class<?> clss;
	public Bundle args;
	public Fragment fragment;

	public TabInfo(String _tag, Class<?> _class, Bundle _args) {
		tag = _tag;
		clss = _class;
		args = _args;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	public TabInfo copy(){
		TabInfo info = new TabInfo(tag, clss, args);
//		info.fragment = fragment;
		return info;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(tag);
		dest.writeString(clss.getName());
	}

	public static final Creator<TabInfo> CREATOR = new Creator<TabInfo>() {

		@Override
		public TabInfo createFromParcel(Parcel source) {
			try {
				String tag = source.readString();
				// String package_name = source.readString();
				String clss_name = source.readString();
				Class<?> clss = Class.forName(clss_name);
				TabInfo ti = new TabInfo(tag, clss, null);
				return ti;
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		}

		@Override
		public TabInfo[] newArray(int size) {
			return null;
		}
	};
}
