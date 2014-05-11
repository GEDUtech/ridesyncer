package com.gedutech.ridesyncer.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class ApiResult extends Object {

	protected String contentType;

	protected String charset;

	protected int statusCode;

	protected String raw;

	protected JSONObject data;

	public ApiResult(int statusCode) {
		this.statusCode = statusCode;
		this.raw = "";
	}

	public ApiResult(int statusCode, String contentTypeHeader, String raw) {
		this.statusCode = statusCode;
		this.raw = raw;

		String[] contentTypeParts = contentTypeHeader.split(";");
		if (contentTypeParts.length > 0) {
			this.contentType = contentTypeParts[0];
		}
		if (contentTypeParts.length > 1) {
			this.charset = contentTypeParts[1].replace(" charset=", "");
		}

		if (contentType.equals("application/json")) {
			try {
				this.data = new JSONObject(raw);
			} catch (JSONException e) {
				Log.d("RideSyncer", "JSONException" + e.getMessage());
			}
		}
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getRaw() {
		return raw;
	}

	public JSONObject getData() {
		return data;
	}

	public boolean isSuccess() {
		return statusCode == 200;
	}

	public boolean hasValidationErrors() {
		return statusCode == 400;
	}

	public boolean isUnauthorized() {
		return statusCode == 401;
	}

	public boolean isServerError() {
		return statusCode >= 500;
	}

	public boolean isNetworkError() {
		return statusCode == 0;
	}

	public String toString() {
		return String.format("[StatusCode: %d]\n[Content-Type: %s]\n[Begin Body]%s[\\EndBody]", statusCode,
				contentType, raw);
	}
}
