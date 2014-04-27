package com.gedutech.ridesyncer;

import org.json.JSONException;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.UsersApi;
import com.gedutech.ridesyncer.models.User;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VerifyAccountActivity extends Activity {

	private UserVerifyTask mVerifyTask = null;

	// UI References
	private EditText mVerify;
	private Button btnVerify;

	private UsersApi usersApi;
	private Session session;
	private User authUser;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		session = Session.getInstance(this);
		authUser = session.getAuthUser();
		usersApi = new UsersApi(authUser.getToken());
		setContentView(R.layout.activity_verify_account);

		mVerify = (EditText) findViewById(R.id.etfVerify);
		btnVerify = (Button) findViewById(R.id.btnVerify);
		btnVerify.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptVerify();
			}
		});
	}

	public void attemptVerify() {
		if (mVerifyTask != null) {
			return;
		}

		boolean cancel = false;
		View focusView = null;

		String verificationCode = mVerify.getText().toString();
		if (verificationCode.isEmpty()) {
			cancel = true;
			focusView = mVerify;
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			mVerifyTask = new UserVerifyTask();
			mVerifyTask.execute(verificationCode);
		}
	}

	public class UserVerifyTask extends AsyncTask<String, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(String... params) {
			ApiResult apiResult = null;
			try {
				apiResult = usersApi.verify(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(VerifyAccountActivity.this, "Unexpected Problem", Toast.LENGTH_LONG).show();
			}
			return apiResult;
		}

		// Add onPostExecute method
		@Override
		protected void onPostExecute(ApiResult result) {
			mVerifyTask = null;

			if (result.isSuccess()) {
				try {
					Log.d("RideSyncer", "Success");
					authUser.setEmailVerified(true);
					session.saveAuthUser();
					startActivity(new Intent(VerifyAccountActivity.this, MainActivity.class));
					finish();
				} catch (Exception e) {
					Toast.makeText(VerifyAccountActivity.this, "Unexpected Problem", Toast.LENGTH_LONG).show();
					Log.d("RideSyncer", e.getMessage());
					Log.d("RideSyncer", result.getRaw());
				}
			} else if (result.hasValidationErrors()) {
				// Do something
				Log.d("RideSyncer", result.getRaw());
				Toast.makeText(VerifyAccountActivity.this, "Unexpected Problem", Toast.LENGTH_LONG).show();
			} else {
				Log.d("RideSyncer", result.getRaw());
				Toast.makeText(VerifyAccountActivity.this, "Unexpected Problem", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mVerifyTask = null;
		}

	}
}
