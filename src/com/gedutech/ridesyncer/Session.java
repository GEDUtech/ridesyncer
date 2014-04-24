package com.gedutech.ridesyncer;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

public class Session {

	private Context context;
	
	public Session(Context context) {
		this.context = context;
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
			Log.d("RideSyncer", "SessionWrite: " + e.getMessage());
			return false;
		}
		return true;
	}

	public boolean write(String key, JSONObject data) {
		return write(key, data.toString());
	}

}
