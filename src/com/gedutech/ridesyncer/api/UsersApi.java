package com.gedutech.ridesyncer.api;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.gedutech.ridesyncer.models.User;

public class UsersApi extends ApiBase {

	public UsersApi() {
		this("");
	}

	public UsersApi(String token) {
		super("users", token);
	}

	public ApiResult login(String username, String password) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("Username", username);
		data.put("Password", password);
		return this.execute(this.post("/login"), data);
	}

	public ApiResult register(User user) throws JSONException {
		return this.execute(this.post("/register"), user.toJSON());
	}

	public ApiResult verify(String verificationCode) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("VerificationCode", verificationCode);
		return this.execute(this.post("/verify"), data);
	}

	public ApiResult registerGcm(String regid) throws JSONException {
		JSONObject data = new JSONObject();
		data.put("GcmRegid", regid);
		Log.d("RideSyncer", "RegId: " + regid);
		Log.d("RideSyncer", "Data: " + data.toString(4));
		return execute(post("/register_gcm"), data);
	}

	public ApiResult search() {
		return execute(get("/search"));
	}
	
	public ApiResult profile(long id) {
		return execute(get("/profile/" + id));
	}

}
