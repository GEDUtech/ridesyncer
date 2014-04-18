package com.gedutech.ridesyncer.api;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class UsersApi extends ApiBase {

	public UsersApi(String token) {
		super("users", token);
	}

	public ApiResult login(String username, String password) {
		List<NameValuePair> postData = new ArrayList<>(2);
		postData.add(new BasicNameValuePair("username", username));
		postData.add(new BasicNameValuePair("password", password));
		return this.execute(this.post("/login"), postData);
	}

}
