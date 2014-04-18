package com.gedutech.ridesyncer.api;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.util.Log;

import java.io.IOException;
import java.util.List;

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

	public ApiResult execute(HttpEntityEnclosingRequestBase request, List<NameValuePair> data) {
		ApiResult result = null;

		try {
			Log.d("RideSyncer", "[" + request.getMethod() + "] " + request.getURI());

			request.setHeader("X-API-TOKEN", this.token);
			request.setEntity(new UrlEncodedFormEntity(data));

			HttpResponse response = client.execute(request);
			int statusCode = response.getStatusLine().getStatusCode();
			String raw = EntityUtils.toString(response.getEntity());
			String contentType = response.getEntity().getContentType().getValue();
			result = new ApiResult(statusCode, contentType, raw);
		} catch (ClientProtocolException e) {
			Log.d("RideSyncer", "ClientProtocolException: " + e.getMessage());
		} catch (IOException e) {
			Log.d("RideSyncer", "IOException: " + e.getMessage());
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