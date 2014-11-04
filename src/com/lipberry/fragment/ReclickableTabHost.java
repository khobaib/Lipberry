package com.lipberry.fragment;

import com.lipberry.HomeActivity;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.widget.TabHost;
import android.widget.Toast;

public class ReclickableTabHost extends FragmentTabHost {
	HomeTabFragment homeactivity;
    public ReclickableTabHost(Context context) {
        super(context);
       
    }
    public void AttachHomeFragment(HomeTabFragment homeactivity){
    	 this.homeactivity=homeactivity;
    }

    public ReclickableTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentTab(int index) {
        if (index == getCurrentTab()) {
            if(index==4){
            	if(homeactivity!=null){
            		homeactivity.restasrtTab();
            	}
            }
        } else {
            super.setCurrentTab(index);
        }
    }
}
