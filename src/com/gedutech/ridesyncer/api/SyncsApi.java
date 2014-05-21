package com.gedutech.ridesyncer.api;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import com.gedutech.ridesyncer.models.Sync;

public class SyncsApi extends ApiBase {

	public SyncsApi(String token) {
		super("syncs", token);
	}

	public ApiResult create(List<Sync> syncs) throws JSONException {
		JSONArray syncsArray = new JSONArray();
		for (Sync sync : syncs) {
			syncsArray.put(sync.toJSON());
		}
		return execute(post("/create"), syncsArray);
	}

	public ApiResult getSyncs() throws JSONException {
		return execute(get("/index"));
	}

}
