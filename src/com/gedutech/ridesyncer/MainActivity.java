package com.gedutech.ridesyncer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

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

		session = new Session(this);

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
			}
			return null;
		}

		@Override
		public Fragment getItem(int position) {
			switch (position) {
			case 0:
				return new SchedulesFragment(authUser);
			}

			return null;
		}

		@Override
		public int getCount() {
			return 1;
		}

	}
}
