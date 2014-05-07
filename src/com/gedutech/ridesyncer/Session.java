package com.gedutech.ridesyncer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.gedutech.ridesyncer.models.Sync;
import com.gedutech.ridesyncer.models.User;

public class Session {

	private static Session instance;

	private static final String PREFERENCES_NAME = "RideSyncer";
	private static final String AUTH_USER_KEY = "auth_user";
	private static final String IS_LOGGED_IN_KEY = "is_logged_in";
	private static final String SYNCS_KEY = "syncs";

	private Context context;
	private SharedPreferences pref;

	private User authUser;
	private List<Sync> syncs;
	private List<User> matches;

	private Session(Context context) {
		this.context = context;
		pref = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		matches = new ArrayList<>();
		syncs = new ArrayList<>();
	}

	public static Session getInstance(Context context) {
		if (instance == null) {
			instance = new Session(context);
		}
		return instance;
	}

	public boolean isLoggedIn() {
		return pref.getBoolean(IS_LOGGED_IN_KEY, false);
	}

	public User getAuthUser() {
		if (authUser == null) {
			try {
				authUser = User.fromJSON(readJSON(AUTH_USER_KEY));
			} catch (Exception e) {
				Log.d("RideSyncer", "Failed to read auth user");
				Log.d("RideSyncer", e.getMessage());
			}
		}
		return authUser;
	}

	public boolean login(User user) {
		authUser = user;

		if (!saveAuthUser()) {
			authUser = null;
			return false;
		}

		Editor editor = pref.edit();
		editor.putBoolean(IS_LOGGED_IN_KEY, true);
		editor.putString("token", user.getToken());
		editor.commit();
		return true;
	}

	public boolean saveAuthUser() {
		try {
			// Log.d("RideSyncer", authUser.toJSON().toString(4));
			write(AUTH_USER_KEY, authUser.toJSON());
		} catch (Exception e) {
			Log.d("RideSyncer", "Failed to write auth user: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean saveSyncs() throws JSONException {
		JSONArray data = new JSONArray();
		for (Sync sync : syncs) {
			data.put(sync.toJSON());
		}
		JSONObject json = new JSONObject();
		json.put("syncs", data);
		return write(SYNCS_KEY, json);
	}

	public List<Sync> getSyncs() {
		return syncs;
	}

	public void logout() {
		pref.edit().remove(IS_LOGGED_IN_KEY).commit();
		matches.clear();
		syncs.clear();
		authUser = null;
	}

	public void setMatches(List<User> matches) {
		this.matches = matches;
	}

	public List<User> getMatches() {
		return matches;
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

	public boolean write(String key, Object data) {
		return write(key, data.toString());
	}

}
