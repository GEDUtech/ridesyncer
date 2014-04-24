package com.gedutech.ridesyncer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONObject;

import com.gedutech.ridesyncer.models.User;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {

	SharedPreferences pref;

	User authUser;
	Session session;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		pref = getSharedPreferences("RideSyncer", Context.MODE_PRIVATE);

		if (!pref.getBoolean("isLoggedIn", false)) {
			startLoginActivity();
			return;
		}

		session = new Session(this);
		try {
			authUser = User.fromJSON(session.readJSON("authUser"));
		} catch (Exception e) {
			startLoginActivity();
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
