package com.gedutech.ridesyncer.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ApiResult {

	protected String contentType;

	protected int statusCode;

	protected String raw;

	protected JSONObject data;

	public ApiResult(int statusCode) {
		this.statusCode = statusCode;
		this.raw = "";
	}

	public ApiResult(int statusCode, String contentType, String raw) {
		this.statusCode = statusCode;
		this.contentType = contentType;
		this.raw = raw;

		if (contentType.equals("application/json")) {
			try {
				this.data = new JSONObject(raw);
			} catch (JSONException e) {
				Log.d("RideSyncer", "JSONException" + e.getMessage());
			}
		}
	}

	public int getStatusCode() {
		return this.statusCode;
	}

	public String getRaw() {
		return this.raw;
	}

	public JSONObject getData() {
		return this.data;
	}

	public boolean isSuccess() {
		return this.statusCode == 200;
	}

	public boolean isUnauthorized() {
		return this.statusCode == 401;
	}

	public boolean isServerError() {
		return statusCode >= 500;
	}
}
