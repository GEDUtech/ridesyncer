package com.gedutech.ridesyncer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.gedutech.ridesyncer.models.User;

public class Session {

	private static final String PREFERENCES_NAME = "RideSyncer";
	private static final String AUTH_USER_KEY = "auth_user";
	private static final String IS_LOGGED_IN_KEY = "is_logged_in";

	private Context context;
	private SharedPreferences pref;

	public Session(Context context) {
		this.context = context;

		pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGGED_IN_KEY, false);
	}

	public User getAuthUser() {
		User user = null;
		try {
			user = User.fromJSON(readJSON(AUTH_USER_KEY));
		} catch (Exception e) {
			Log.d("RideSyncer", "Failed to read auth user");
		}
		return user;
	}

	public boolean setAuthUser(User user) {
		try {
			write(AUTH_USER_KEY, user.toJSON());
		} catch (Exception e) {
			Log.d("RideSyncer", "Failed to write auth user: " + e.getMessage());
			return false;
		}

		Editor editor = pref.edit();

		editor.putBoolean(IS_LOGGED_IN_KEY, true);
		editor.putString("token", user.getToken());
		editor.commit();
		return true;
	}

	public void logout() {
		pref.edit().remove(IS_LOGGED_IN_KEY).commit();
	}

	public JSONObject readJSON(String key) throws IOException, JSONException {
		return new JSONObject(read(key));
	}

	public String read(String key) throws IOException {
		StringBuilder stringBuilder = new StringBuilder();

		InputStream inputStream = context.openFileInput(key);
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String buffer;
		while ((buffer = bufferedReader.readLine()) != null) {
			stringBuilder.append(buffer);
		}

		return stringBuilder.toString();
	}

	public boolean write(String key, String data) {
		try {
			FileOutputStream outputStream;
			outputStream = context.openFileOutput(key, Context.MODE_PRIVATE);
			outputStream.write(data.getBytes());
			outputStream.close();
		} catch (Exception e) {
			Log.d("RideSyncer", "Failed to write key (" + key + "): " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean write(String key, JSONObject data) {
		return write(key, data.toString());
	}

}
