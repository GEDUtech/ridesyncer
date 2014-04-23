package com.gedutech.ridesyncer.api;

import org.json.JSONException;
import org.json.JSONObject;

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

}
