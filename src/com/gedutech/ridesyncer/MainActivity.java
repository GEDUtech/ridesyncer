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

		session = Session.getInstance(this);

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
				return "My Schedule";
			case 1:
				return "Search";
			}
			return null;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new SchedulesFragment();
			case 1:
				return new SearchFragment();
			}

			return null;
		}

		@Override
		public int getCount() {
			return 2;
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
