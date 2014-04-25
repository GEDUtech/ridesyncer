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
				// TODO Auto-generated method stub
				attemptRegister();
			}
		});		
	}
	
	public void attemptRegister() {
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
		
		mRegisterTask = new UserRegisterTask();
		mRegisterTask.execute(user);
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
	
	public class UserRegisterTask extends AsyncTask<User, Void, ApiResult>{

		@Override
		protected ApiResult doInBackground(User... params) {
			ApiResult apiResult = null;
			try {
				apiResult = usersApi.register(params[0]);
			} catch (JSONException e) {
				e.printStackTrace();
				Toast.makeText(RegisterActivity.this, "Unexpected Problem" , Toast.LENGTH_LONG).show();
			}
			return apiResult;
		}
		
		// Add onPostExecute method
		@Override
		protected void onPostExecute(ApiResult result) {
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
				Toast.makeText(RegisterActivity.this, "Unexpected Problem" , Toast.LENGTH_LONG).show();
			}
		}
	}
}