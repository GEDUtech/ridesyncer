package com.gedutech.ridesyncer.models;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncUser {

	long id;
	long syncId;
	long userId;

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("SyncId", this.syncId);
		obj.put("UserId", this.userId);

		return obj;
	}

	public static SyncUser fromJSON(JSONObject obj) throws JSONException {
		SyncUser syncUser = new SyncUser();

		syncUser.id = obj.getLong("Id");
		syncUser.syncId = obj.getLong("SyncId");
		syncUser.userId = obj.getLong("UserId");

		return syncUser;
	}

	public long getId() {
		return id;
	}

	public long getSyncId() {
		return syncId;
	}

	public void setSyncId(long syncId) {
		this.syncId = syncId;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}
}
