package com.gedutech.ridesyncer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.gedutech.ridesyncer.models.User;

public class MainActivity extends Activity {

	private User authUser;
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

		checkAuthorization();
	}

	protected void checkAuthorization() {
		if (authUser == null) {
			startLoginActivity();
		} else if (!authUser.isEmailVerified()) {
			startVerifyAccountActivity();
		}
	}

	protected void startVerifyAccountActivity() {
		startActivity(new Intent(this, VerifyAccountActivity.class));
		finish();
	}

	protected void startLoginActivity() {
		startActivity(new Intent(this, LoginActivity.class));
		finish();
	}
}
