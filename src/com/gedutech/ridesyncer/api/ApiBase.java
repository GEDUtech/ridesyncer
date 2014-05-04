package com.gedutech.ridesyncer.api;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

public class ApiBase {

	static final String BASE_URL = "http://ridesyncer.com/api";

	protected HttpClient client;

	protected String context;

	protected String token;

	public ApiBase(String context, String token) {
		this.client = new DefaultHttpClient();
		this.context = context;
		this.token = token;
	}

	public ApiResult execute(HttpEntityEnclosingRequestBase request, Object data) {
		ApiResult result = null;

		try {
			Log.d("RideSyncer", "[" + request.getMethod() + "] " + request.getURI());

			request.setHeader("X-API-TOKEN", this.token);
			request.setEntity(new StringEntity(data.toString()));

			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			String raw = EntityUtils.toString(response.getEntity());
			String contentType = response.getEntity().getContentType().getValue();
			result = new ApiResult(statusCode, contentType, raw);
		} catch (IOException e) {
			Log.d("RideSyncer", "IOException: " + e.getMessage());
			result = new ApiResult(0);
		}

		return result;
	}

	public HttpPost post(String path) {
		return new HttpPost(this.getFullUrl(path));
	}

	protected String getFullUrl(String path) {
		return BASE_URL + "/" + context + path;
	}

}