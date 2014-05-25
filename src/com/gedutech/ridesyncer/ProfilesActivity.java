package com.gedutech.ridesyncer;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.gedutech.ridesyncer.api.ApiResult;
import com.gedutech.ridesyncer.api.UsersApi;
import com.gedutech.ridesyncer.models.User;

public class ProfilesActivity extends Activity {
	
	protected UsersApi usersApi;
	protected Session session;
	protected User authUser;
	protected long userId;
	
	protected TextView txtUsername, txtCityState, txtName, txtGender, txtGenre, txtMajor, txtSmoker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profiles);
		
		txtName = (TextView) findViewById(R.id.txt_name);
		txtUsername = (TextView) findViewById(R.id.txt_username);
		txtCityState = (TextView) findViewById(R.id.txt_city_state);
		txtGender = (TextView) findViewById(R.id.txt_gender);
		txtMajor  = (TextView) findViewById(R.id.txt_major);
		txtGenre = (TextView) findViewById(R.id.txt_genre);
		txtSmoker = (TextView) findViewById(R.id.txt_smoker);
		
		session = Session.getInstance(this);
		authUser = session.getAuthUser();
		usersApi = new UsersApi(authUser.getToken());

		userId = getIntent().getExtras().getLong("user_id");
		
		new FetchProfileTask().execute();
	}

	class FetchProfileTask extends AsyncTask<Void, Void, ApiResult> {

		@Override
		protected ApiResult doInBackground(Void... args) {
			ApiResult result = null;
			try {
				result = usersApi.profile(userId);
			} catch (Exception e) {
				
			}
			return result;
		}
		
		@Override
		protected void onPostExecute(ApiResult result) {
			if (result.isSuccess()) {
				try {
					User user = User.fromJSON(result.getData());
					txtName.setText(user.getFirstName() + " " + user.getLastName());
					txtUsername.setText(user.getUsername());
					txtGender.setText(user.getGender());
					txtGenre.setText(user.getFavoriteGenre());
					txtMajor.setText(user.getMajor());
					txtCityState.setText(user.getCity() + ", " + user.getState());
					txtSmoker.setText(user.isSmoker() ? "Yes" : "No");
				} catch (Exception e) {
					
				}
			}
		}

	}
}
