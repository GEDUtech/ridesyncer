package com.gedutech.ridesyncer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.TextView;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.SyncsApi;
import com.gedutech.ridesyncer.api.UsersApi;
import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.SyncUser;
import com.gedutech.ridesyncer.models.User;
import com.gedutech.ridesyncer.utils.GcmUtil;

public class RideSyncer extends FragmentActivity {

	private static User authUser;
	private static Session session;

	private UsersApi usersApi;
	private ViewPager pager;
	private GcmUtil gcmUtil;
	private MenuItem newSyncRequestsMenuItem;
	private TextView txtNumSyncRequests;

	private static RideSyncer instance;

	public static void refreshSyncs() {
		new FetchSyncsTask().execute();
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		instance = this;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		session = Session.getInstance(getApplicationContext());

		if (!session.isLoggedIn()) {
			startLoginActivity();
			return;
		}

		authUser = session.getAuthUser();
		if (authUser == null) {
			startLoginActivity();
			return;
		}

		if (!checkAuthorization()) {
			return;
		}

		usersApi = new UsersApi(authUser.getToken());
		gcmUtil = new GcmUtil(this);
		try {
			if (gcmUtil.checkPlayServices()) {
				String regid = gcmUtil.getRegistrationId(this);
				Log.d("RideSyncer", "GCMID: " + regid);
				if (regid.isEmpty()) {
					new GcmRegisterTask().execute();
				}
			} else {
				Log.i("RideSyncer", "No valid Google Play Services APK found.");
			}
		} catch (Exception e) {
			Log.d("RideSyncer", e.getMessage());
		}

		setupViewPager();

		ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		ActionBar.TabListener tabListener = new ActionBar.TabListener() {
			@Override
			public void onTabUnselected(Tab tab, FragmentTransaction ft) {

			}

			@Override
			public void onTabSelected(Tab tab, FragmentTransaction ft) {
				pager.setCurrentItem(tab.getPosition(), false);
			}

			@Override
			public void onTabReselected(Tab tab, FragmentTransaction ft) {

			}
		};

		for (int i = 0; i < 3; i++) {
			actionBar
					.addTab(actionBar.newTab().setText(pager.getAdapter().getPageTitle(i)).setTabListener(tabListener));
		}

		try {
			ViewConfiguration config = ViewConfiguration.get(this);
			Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
			if (menuKeyField != null) {
				menuKeyField.setAccessible(true);
				menuKeyField.setBoolean(config, false);
			}
		} catch (Exception ex) {
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		new FetchSyncsTask().execute();
	}

	protected void setupViewPager() {
		pager = (ViewPager) findViewById(R.id.pager);
		pager.setAdapter(new MainPagerAdapter(getSupportFragmentManager()));
		pager.setPageTransformer(false, new ViewPager.PageTransformer() {
			/*
			 * Class from
			 * http://developer.android.com/training/animation/screen-slide.html
			 */

			private static final float MIN_SCALE = 0.85f;
			private static final float MIN_ALPHA = 0.5f;

			public void transformPage(View view, float position) {
				int pageWidth = view.getWidth();
				int pageHeight = view.getHeight();

				if (position < -1) { // [-Infinity,-1)
					// This page is way off-screen to the left.
					view.setAlpha(0);

				} else if (position <= 1) { // [-1,1]
					// Modify the default slide transition to shrink the page as
					// well
					float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
					float vertMargin = pageHeight * (1 - scaleFactor) / 2;
					float horzMargin = pageWidth * (1 - scaleFactor) / 2;
					if (position < 0) {
						view.setTranslationX(horzMargin - vertMargin / 2);
					} else {
						view.setTranslationX(-horzMargin + vertMargin / 2);
					}

					// Scale the page down (between MIN_SCALE and 1)
					view.setScaleX(scaleFactor);
					view.setScaleY(scaleFactor);

					// Fade the page relative to its size.
					view.setAlpha(MIN_ALPHA + (scaleFactor - MIN_SCALE) / (1 - MIN_SCALE) * (1 - MIN_ALPHA));

				} else { // (1,+Infinity]
					// This page is way off-screen to the right.
					view.setAlpha(0);
				}
			}
		});
		pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				getActionBar().setSelectedNavigationItem(position);
			}

			@Override
			public void onPageScrolled(int position, float offset, int offsetPixels) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}

	protected boolean checkAuthorization() {
		if (authUser == null) {
			startLoginActivity();
			return false;
		}
		if (!authUser.isEmailVerified()) {
			startVerifyAccountActivity();
			return false;
		}
		return true;
	}

	protected void startVerifyAccountActivity() {
		startActivity(new Intent(this, VerifyAccountActivity.class));
		finish();
	}

	protected void startLoginActivity() {
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		newSyncRequestsMenuItem = menu.findItem(R.id.new_sync_requests);
		newSyncRequestsMenuItem.getActionView().setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(), SyncReviewerActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
				startActivity(intent);
			}
		});
		txtNumSyncRequests = (TextView) newSyncRequestsMenuItem.getActionView()
				.findViewById(R.id.txt_num_sync_requests);
		updateSyncRequestNotification();

		return true;
	}

	protected void updateSyncRequestNotification() {
		if (!session.isLoggedIn()) {
			return;
		}

		int num = 0;
		for (Sync sync : authUser.getSyncs()) {
			for (SyncUser syncUser : sync.getSyncUsers()) {
				if (syncUser.getUserId() == authUser.getId() && syncUser.getStatus() == 0) {
					num++;
				}
			}
		}

		txtNumSyncRequests.setText(num > 9 ? "9+" : num + "");
		newSyncRequestsMenuItem.setVisible(num > 0);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.edit_syncs:
			Intent intent = new Intent(getApplicationContext(), SyncEditorActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.putExtra("edit", true);
			startActivity(intent);
			break;
		case R.id.logout:
			session.logout();
			startLoginActivity();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private class MainPagerAdapter extends FragmentPagerAdapter {

		public MainPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
			case 0:
				return "Syncs";
			case 1:
				return "Schedules";
			case 2:
				return "Find Matches";
			}
			return null;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new SyncsFragment();
			case 1:
				return new SchedulesFragment();
			case 2:
				return new SearchFragment();
			}

			return null;
		}

		@Override
		public int getCount() {
			return 3;
		}

	}

	private static class FetchSyncsTask extends AsyncTask<Void, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(Void... arg0) {
			ApiResult result = null;
			try {
				SyncsApi api = new SyncsApi(authUser.getToken());
				return api.getSyncs();
			} catch (Exception e) {
				Log.d("RideSyncer", "FetchSyncsTask Error: " + e.getMessage());
			}
			return result;
		}

		@Override
		protected void onPostExecute(ApiResult result) {
			if (result.isSuccess()) {
				Log.d("RideSyncer", result.getRaw());
				List<Sync> syncs = new ArrayList<>();
				JSONArray syncsArr;
				try {
					syncsArr = result.getData().getJSONArray("results");
					for (int i = 0; i < syncsArr.length(); i++) {
						Log.d("RideSyncer", syncsArr.getJSONObject(i).toString(4));
						syncs.add(Sync.fromJSON(syncsArr.getJSONObject(i)));
					}
					authUser.getSyncs().clear();
					authUser.getSyncs().addAll(syncs);
					session.saveAuthUser();
					if (instance != null) {
						instance.updateSyncRequestNotification();
					}
				} catch (Exception e) {

				}
			}
		}
	}

	private class GcmRegisterTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... arg0) {
			String regid = "";
			try {
				regid = gcmUtil.register();
			} catch (Exception e) {
				Log.d("RideSyncer", "Error: " + e.getMessage());
			}
			return regid;
		}

		@Override
		protected void onPostExecute(String result) {
			if (result.isEmpty()) {
				return;
			}
			new SendGcmRegIdTask().execute();
		}
	}

	private class SendGcmRegIdTask extends AsyncTask<Void, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(Void... params) {
			try {
				return usersApi.registerGcm(gcmUtil.getRegId());
			} catch (JSONException e) {
				Log.d("RideSyncer", e.getMessage());
			}
			return null;
		}

		@Override
		protected void onPostExecute(ApiResult result) {
			if (!result.isSuccess()) {
				Log.d("RideSyncer", result.toString());
			}
		}

	}
}
