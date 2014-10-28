package com.lipberry.fragment;

import java.util.Stack;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.TextView;

import com.lipberry.HomeActivity;
import com.lipberry.R;
import com.lipberry.model.Article;
import com.lipberry.model.ArticleDetails;
import com.lipberry.utility.Constants;

public class HomeTabFragment extends TabFragment {
	private static final String TAG = "HomeTabFragment";

	protected Stack<Fragment> backEndStack;
	protected Stack<Integer> trackCallHome;
	// private Bundle sBundle;
	int callstatefromtab = 0;

	private static HomeActivity homeActivity;

	private FragmentManager fragmentManager;
	private FragmentTransaction fragmentTransaction;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initHomeTab();
		// sBundle = savedInstanceState;
	}

	private void initHomeTab() {
		trackCallHome = new Stack<Integer>();
		backEndStack = new Stack<Fragment>();
		FragmentHomeHolder initialFragment = new FragmentHomeHolder();
		initialFragment.parent = this;
		trackCallHome.push(0);
		backEndStack.push(initialFragment);

		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
	}

	public void resetHome() {
		// while(trackcallhome!=null && trackcallhome.size()>0)
		// trackcallhome.pop();
		// while(backEndStack!=null && backEndStack.size()>0)
		// backEndStack.pop();
		trackCallHome = new Stack<Integer>();
		backEndStack = new Stack<Fragment>();
		FragmentHomeHolder initialFragment = new FragmentHomeHolder();
		initialFragment.parent = this;
		trackCallHome.push(0);
		backEndStack.push(initialFragment);
		fragmentManager = getChildFragmentManager();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, initialFragment);
		try {
			fragmentTransaction.commit();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, initialFragment);
			fragmentTransaction.commit();
		}
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		// Log.e("calling", "calling Tab");
		// ((HomeActivity) getActivity()).img_cat_icon.setVisibility(View.GONE);
		homeActivity.img_cat_icon.setVisibility(View.GONE);

		super.onPause();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewParent parent = (ViewParent) container.getParent();
		if (parent instanceof View) {
			((TextView) ((View) parent).findViewById(R.id.welcome_title)).setText(this.getTag());
		}
		View v = inflater.inflate(R.layout.fragment_tab3, container, false);
		return v;
	}

	public void onStart() {
		Constants.GOT_AB_FROM_WRITE_TOPIC = 4;

		if (Constants.GO_MEMBER_STATE_FROM_INTERACTION) {
			startMemberFragment(2);
			Constants.GO_MEMBER_STATE_FROM_INTERACTION = false;
		} else if (Constants.GO_MEMBER_STATE_FROM_IMESSAGE) {
			startMemberFragment(1);
			Constants.GO_MEMBER_STATE_FROM_IMESSAGE = false;
		} else if (Constants.GO_MEMBER_STATE_FROM_SETTING) {
			startMemberFragment(5);
			Constants.GO_MEMBER_STATE_FROM_SETTING = false;
		} else if (Constants.GO_ARTCLE_PAGE) {
			Log.e(TAG, "GO_ARTCLE_PAGE" + " " + Constants.from);
			Constants.GO_ARTCLE_PAGE = false;
			FragmentArticleDetailsFromInteraction(Constants.articledetails, Constants.from);
		} else if (Constants.GO_ARTCLE_PAGE_FROM_MEMBER) {
			Log.e(TAG, "GO_ARTCLE_PAGE_FROM_MEMBER");
			Constants.GO_ARTCLE_PAGE_FROM_MEMBER = false;
			startFragmentArticleDetailsFromHome(Constants.ARTICLETOSEE, 5);
		}

		trackCallHome.peek();
		Fragment fragment = backEndStack.peek();
		// FragmentManager fragmentManager = getChildFragmentManager();
		// FragmentTransaction fragmentTransaction =
		// fragmentManager.beginTransaction();
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, fragment);
		fragmentTransaction.commitAllowingStateLoss();
		super.onStart();
	}

	public void FragmentArticleDetailsFromInteraction(ArticleDetails articledetails, int from) {
		FragmentArticleDetailsFromInteraction newFragment = new FragmentArticleDetailsFromInteraction(articledetails,
				from);
		newFragment.parent = this;
		// FragmentManager fragmentManager = getChildFragmentManager();
		// FragmentTransaction
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackCallHome.push(from);
		try {
			fragmentTransaction.commit();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.commit();
		}
	}

	public void StartFragmentSendMessageFormHome(String nickname, String username) {
		// ((HomeActivity) getActivity()).img_cat_icon.setVisibility(View.GONE);
		homeActivity.img_cat_icon.setVisibility(View.GONE);

		FragmentSendMessageFormHome newFragment = new FragmentSendMessageFormHome(nickname, username);
		newFragment.parent = this;
		// FragmentManager fragmentManager = getChildFragmentManager();
		// FragmentTransaction
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackCallHome.push(0);
		try {
			fragmentTransaction.commit();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.commit();
		}
	}

	public void startFragmentArticleDetailsFromHome(Article article, ArticleDetails articleDetails) {
		FragmentArticleDetailsFromHome newFragment = new FragmentArticleDetailsFromHome();
		newFragment.setArticle(article, articleDetails);
		newFragment.parent = this;
		// FragmentManager fragmentManager = getChildFragmentManager();
		// FragmentTransaction
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackCallHome.push(0);
		try {
			fragmentTransaction.commit();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.commit();
		}
	}

	public void startFragmentArticleDetailsFromHome(Article article, int state) {
		FragmentArticleDetailsFromHome newFragment = new FragmentArticleDetailsFromHome();
		newFragment.setArticle(article, Constants.articledetails);
		newFragment.parent = this;
		// FragmentManager fragmentManager = getChildFragmentManager();
		// FragmentTransaction
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackCallHome.push(state);
		try {
			fragmentTransaction.commit();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.commit();
		}
	}

	public void startMenufragment() {
		// ((HomeActivity) getActivity()).img_cat_icon.setVisibility(View.GONE);
		homeActivity.img_cat_icon.setVisibility(View.GONE);

		FragmentHomeHolder newFragment = new FragmentHomeHolder();
		newFragment.parent = this;
		// FragmentManager fragmentManager = getChildFragmentManager();
		// FragmentTransaction
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackCallHome.push(0);
		try {
			fragmentTransaction.commit();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.commit();
		}
	}

	public void startMemberFragment(int state) {
		// ((HomeActivity) getActivity()).img_cat_icon.setVisibility(View.GONE);
		homeActivity.img_cat_icon.setVisibility(View.GONE);

		FragmentMemberFromHome newFragment = new FragmentMemberFromHome(state, Constants.userid);
		newFragment.parent = this;
		// FragmentManager fragmentManager = getChildFragmentManager();
		// FragmentTransaction
		fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.tab3Content, newFragment);
		fragmentTransaction.addToBackStack(null);
		backEndStack.push(newFragment);
		trackCallHome.push(state);
		try {
			fragmentTransaction.commit();
		} catch (IllegalStateException ise) {
			ise.printStackTrace();
			fragmentTransaction = homeActivity.getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tab3Content, newFragment);
			fragmentTransaction.commit();
		}
	}

	public void clearr() {
		trackCallHome.pop();
		backEndStack.pop();
	}

	@Override
	public void onBackPressed() {
		if (backEndStack.size() == 1) {
			// ((HomeActivity) getActivity()).close();
			homeActivity.close();
		} else {
			if (backEndStack.size() == 1) {
				// ((HomeActivity) getActivity()).close();
				homeActivity.close();
			} else {
				int callstate = trackCallHome.pop();
				backEndStack.pop();
				if (callstate == 0) {
					Fragment frg = backEndStack.peek();
					// FragmentManager fragmentManager =
					// getChildFragmentManager();
					// FragmentTransaction
					fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
				} else {
					Fragment frg = backEndStack.peek();
					// FragmentManager fragmentManager =
					// getChildFragmentManager();
					// FragmentTransaction
					fragmentTransaction = fragmentManager.beginTransaction();
					fragmentTransaction.replace(R.id.tab3Content, frg).commitAllowingStateLoss();
					if (callstate == 10) {
						// ((HomeActivity)
						// getActivity()).mTabHost.setCurrentTab(0);
						homeActivity.mTabHost.setCurrentTab(0);
					} else {
						// ((HomeActivity)
						// getActivity()).mTabHost.setCurrentTab(callstate);
						homeActivity.mTabHost.setCurrentTab(callstate);
					}

				}

			}

		}
	}

	public static HomeTabFragment newInstance(HomeActivity hActivity) {
		homeActivity = hActivity;
		return new HomeTabFragment();
	}
}
