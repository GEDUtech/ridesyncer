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

public class RegisterActivity extends Activity {

	private UserRegisterTask mRegisterTask = null;

	// UI References
	private EditText mFirstNameView;
	private EditText mLastNameView;
	private EditText mEmailView;
	private EditText mUsernameView;
	private EditText mPasswordView;
	private EditText mRepeatPasswordView;
	private EditText mAddressView;
	private EditText mCityView;
	private EditText mStateView;
	private EditText mZipView;
	private EditText mRideView;
	private Button btnRegister;

	private UsersApi usersApi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		usersApi = new UsersApi();
		setContentView(R.layout.activity_register);

		mFirstNameView = (EditText) findViewById(R.id.etfFName);
		mLastNameView = (EditText) findViewById(R.id.etfLName);
		mEmailView = (EditText) findViewById(R.id.etfEmailAddress);
		mUsernameView = (EditText) findViewById(R.id.etfUsername);
		mPasswordView = (EditText) findViewById(R.id.pwtfPassword);
		mRepeatPasswordView = (EditText) findViewById(R.id.pwtfRepeatPassword);

		// Placeholder for address
		mCityView = (EditText) findViewById(R.id.etfCity);
		mStateView = (EditText) findViewById(R.id.etfState);
		mZipView = (EditText) findViewById(R.id.etfZip);
		mRideView = (EditText) findViewById(R.id.etfRide);

		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnRegister.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				attemptRegister();
			}
		});
	}

	public void attemptRegister() {
		if (mRegisterTask != null) {
			return;
		}

		// Reset Errors
		mFirstNameView.setError(null);
		mLastNameView.setError(null);
		mEmailView.setError(null);
		mUsernameView.setError(null);
		mPasswordView.setError(null);
		mRepeatPasswordView.setError(null);
		mCityView.setError(null);
		mStateView.setError(null);
		mZipView.setError(null);
		mRideView.setError(null);

		// Insert validation here
		User user = new User();
		user.setFirstName(mFirstNameView.getText().toString());
		user.setLastName(mLastNameView.getText().toString());
		user.setEmail(mEmailView.getText().toString());
		user.setUsername(mUsernameView.getText().toString());
		user.setPassword(mPasswordView.getText().toString());
		user.setRepeatPassword(mRepeatPasswordView.getText().toString());
		// Placeholder for address
		user.setCity(mCityView.getText().toString());
		user.setState(mStateView.getText().toString());
		user.setZip(mZipView.getText().toString());
		user.setRide(mRideView.getText().toString());

		boolean cancel = false;
		View focusView = null;

		if (user.getFirstName().isEmpty()) {
			mFirstNameView.setError(getString(R.string.error_field_required));
		}

		if (user.getLastName().isEmpty()) {
			mLastNameView.setError(getString(R.string.error_field_required));
		}

		if (user.getEmail().isEmpty()) {
			mEmailView.setError(getString(R.string.error_field_required));
		}

		if (!android.util.Patterns.EMAIL_ADDRESS.matcher(user.getEmail()).matches()
				|| user.getEmail().substring(user.getEmail().length() - 4).equals(".edu")) {
			mEmailView.setError(getString(R.string.InvalidEmail));
		}

		if (user.getUsername().length() < 6) {
			mUsernameView.setError(getString(R.string.InvalidUsername));
		}

		if (user.getPassword().length() < 6) {
			mPasswordView.setError(getString(R.string.InvalidPassword));
		}

		if (!user.getRepeatPassword().equals(user.getPassword())) {
			mRepeatPasswordView.setError(getString(R.string.PasswordsDontMatch));
		}

		if (user.getCity().isEmpty()) {
			mCityView.setError(getString(R.string.error_field_required));
		}

		if (user.getState().isEmpty()) {
			mStateView.setError(getString(R.string.error_field_required));
		}

		if (user.getZip().length() < 5 || !user.getZip().matches("[0-9]+")) {
			mZipView.setError(getString(R.string.InvalidZip));
		}

		if (user.getRide().isEmpty()) {
			mRideView.setError(getString(R.string.error_field_required));
		}

		if (cancel) {
			focusView.requestFocus();
		} else {
			mRegisterTask = new UserRegisterTask();
			mRegisterTask.execute(user);
		}
	}

	private void register(User user) {
		Session session = new Session(this);

		if (!session.setAuthUser(user)) {
			return;
		}

		Intent i = new Intent(RegisterActivity.this, VerifyAccountActivity.class);
		startActivity(i);
		finish();
	}

	public class UserRegisterTask extends AsyncTask<User, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(User... params) {
			ApiResult apiResult = null;
			try {
				apiResult = usersApi.register(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(RegisterActivity.this, "Unexpected Problem", Toast.LENGTH_LONG).show();
			}
			return apiResult;
		}

		// Add onPostExecute method
		@Override
		protected void onPostExecute(ApiResult result) {
			mRegisterTask = null;

			Log.d("RideSyncer", result.getRaw());
			Log.d("RideSyncer", "" + result.getStatusCode());
			if (result.isSuccess()) {
				try {
					User user = User.fromJSON(result.getData());
					register(user);
				} catch (Exception e) {
					Toast.makeText(RegisterActivity.this, "Unexpected Problem", Toast.LENGTH_LONG).show();
					Log.d("RideSyncer", e.getMessage());
				}
			} else if (result.hasValidationErrors()) {
				// Do something
			} else {
				Toast.makeText(RegisterActivity.this, "Unexpected Problem", Toast.LENGTH_LONG).show();
			}
		}

		@Override
		protected void onCancelled() {
			mRegisterTask = null;
		}
	}
}