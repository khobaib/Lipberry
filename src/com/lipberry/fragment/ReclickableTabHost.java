package com.lipberry.fragment;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.AttributeSet;
import android.util.Log;

public class ReclickableTabHost extends FragmentTabHost {
	HomeTabFragment homeactivity;
	MenuTabFragment menu_fragment;
    public ReclickableTabHost(Context context) {
        super(context);
       
    }
    public void AttachHomeFragment(HomeTabFragment homeactivity){
    	 this.homeactivity=homeactivity;
    }
    public void AttachMenuFragment(MenuTabFragment menu_fragment){
   	 this.menu_fragment=menu_fragment;
   }

    public ReclickableTabHost(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setCurrentTab(int index) {
    	try {
    		if (index == getCurrentTab()) {
    			if(index==4){
    				if(homeactivity!=null){
    					homeactivity.restasrtTab();
    				}
    			}
    			else if(index==5){
    				if(menu_fragment!=null){
    					menu_fragment.restasrtTab();
    				}
    			}
    		} else {
    			super.setCurrentTab(index);
    		}
    	} catch (IllegalStateException e) {
			Log.e("20150311", "state loss - IllegalStateException");
		}
    }
}
