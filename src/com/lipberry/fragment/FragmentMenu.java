package com.lipberry.fragment;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.viewpagerindicator.TabPageIndicator;



@SuppressLint("NewApi")
public class FragmentMenu extends Fragment {
	
	 private static final String[] CONTENT = new String[] {"جديد الكل","جديد من أتابعهم"};
	 LayoutInflater inflater1 ;
	FragmentTab1 parent;
	ViewGroup view;
	 ViewPager pager;
	 TabPageIndicator indicator;
	@SuppressLint("NewApi")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
	
	
		
		
		
		inflater1=inflater;
		

		ViewGroup v = (ViewGroup) inflater.inflate(R.layout.fragment_menu,
				container, false);
		view=container;
		//PagerAdapter
		 // FragmentPagerAdapter adapter = new PagerAdapter(getActivity().getSupportFragmentManager(), fragments);
		 pager = (ViewPager) v.findViewById(R.id.pager);
		 indicator = (TabPageIndicator)v.findViewById(R.id.indicator);

			List<Fragment> fragments=new ArrayList<Fragment>();
			Log.i("crash", "2");
			FragmentMyCountriesPost.parent=parent;
			FragmentMyCountriesPost frag=new FragmentMyCountriesPost();
			
			Fragment newfrag=frag;
			;
			 fragments.add(newfrag);
			 FragmentMyFollwerPost.parent=parent;
			 FragmentMyFollwerPost frag2=new FragmentMyFollwerPost();
			
			 newfrag=frag2;
				Log.i("crash", "5");
			 fragments.add(newfrag);
				Log.i("crash", "6");
			 FragmentPagerAdapter adapter = new GoogleMusicAdapter(getChildFragmentManager());
				Log.i("crash", "7");
			 pager.setAdapter(adapter);
				Log.i("crash", "8");
		     indicator.setViewPager(pager);
		     
		
		return v;
	}
	
	
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//topbar_new_article
		( (HomeActivity)getActivity()).backbuttonoftab.setVisibility(View.GONE);
		( (HomeActivity)getActivity()).welcome_title.setText(getActivity().getResources().getString(R.string.topbar_new_article));
	}

	@Override
	public void onStart() {
		
		// TODO Auto-generated method stub
		super.onStart();
		

		Log.i("crash", "1");
		
		
		Log.i("onStartm", "onStart");
	}
	

	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		  
		
		//fragmentTransaction.remove(yourfragment).commit()
	}
	
	class GoogleMusicAdapter extends FragmentPagerAdapter {
       
		@Override
		public Parcelable saveState() {
			// TODO Auto-generated method stub
			return super.saveState();
		}
		public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }
        @Override
      
        public Fragment getItem(int position) {
        	
        
        	
        	 if (position == 0) {
        		 Fragment newfrag=new FragmentMyCountriesPost();
              	return newfrag;
             } else if (position == 1) {
            	 Fragment newfrag=new FragmentMyFollwerPost();
             	return newfrag;
             } else
                 return null;
        	
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return CONTENT[position % CONTENT.length].toUpperCase();
        }

        @Override
        public int getCount() {
            return CONTENT.length;
        }
    }
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.i("onDestroy", "onDestroy");
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		Log.i("onDestroyView", "onDestroyView");
	}
	
	
	public class PagerAdapter extends FragmentPagerAdapter {
		 
	    private List<Fragment> fragments;
	    /**
	     * @param fm
	     * @param fragments
	     */
	    public PagerAdapter(FragmentManager fm, List<Fragment> fragments) {
	        super(fm);
	        this.fragments = fragments;
	    }
	    /* (non-Javadoc)
	     * @see android.support.v4.app.FragmentPagerAdapter#getItem(int)
	     */
	    @Override
	    public Fragment getItem(int position) {
	        return this.fragments.get(position);
	    }
	 
	    /* (non-Javadoc)
	     * @see android.support.v4.view.PagerAdapter#getCount()
	     */
	    @Override
	    public int getCount() {
	        return this.fragments.size();
	    }
	}
	
	
	
	
	/*public class MyAdapter extends FragmentPagerAdapter
	{
	    static final int NUM_ITEMS = 2;
	    private final FragmentManager mFragmentManager;
	    private Fragment mFragmentAtPos0;

	    public MyAdapter(FragmentManager fm)
	    {
	        super(fm);
	        mFragmentManager = fm;
	    }

	    @Override
	    public Fragment getItem(int position)
	    {
	    	 if (position == 0)
	         {
	             if (mFragmentAtPos0 == null)
	             {
	                 mFragmentAtPos0 = FragmentMyCountriesPost.newInstance(new FirstPageFragmentListener()
	                 {
	                     public void onSwitchToNextFragment()
	                     {
	                         mFragmentManager.beginTransaction().remove(mFragmentAtPos0).commit();
	                         mFragmentAtPos0 = NextFragment.newInstance();
	                         notifyDataSetChanged();
	                     }
	                 });
	             }
	             return mFragmentAtPos0;
	         }
	        else{

            	Fragment newfrag=new FragmentMyFollwerPost();
            	return newfrag;
	        }
	           // return SecondPageFragment.newInstance();
	    }

	    @Override
	    public int getCount()
	    {
	        return NUM_ITEMS;
	    }

	    @Override
	    public int getItemPosition(Object object)
	    {
	        if (object instanceof FragmentMyCountriesPost && mFragmentAtPos0 instanceof NextFragment)
	            return POSITION_NONE;
	        return POSITION_UNCHANGED;
	    }
	}

	
	public interface FirstPageFragmentListener
	{
	    void onSwitchToNextFragment();
	}*/
	

}
