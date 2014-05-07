package com.gedutech.ridesyncer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.gedutech.ridesyncer.models.User;

public class MainActivity extends FragmentActivity {

	private User authUser;
	private ViewPager pager;
	private Session session;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
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

		setupViewPager();
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
				getActionBar().setTitle(pager.getAdapter().getPageTitle(position));

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

	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.logout:
			session.logout();
			startLoginActivity();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
