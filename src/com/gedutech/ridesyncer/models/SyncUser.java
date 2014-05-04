package com.gedutech.ridesyncer.models;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncUser {

	long id;
	long userId;
	long syncId;
	int status;
	int order;
	Date createdAt;

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("UserId", this.userId);
		obj.put("SyncId", this.syncId);
		obj.put("Status", this.status);
		obj.put("Order", this.order);

		return obj;
	}

	public static SyncUser fromJSON(JSONObject obj) throws JSONException {
		SyncUser syncDay = new SyncUser();

		syncDay.id = obj.getLong("Id");
		syncDay.userId = obj.getLong("UserId");
		syncDay.syncId = obj.getLong("SyncId");
		syncDay.status = obj.getInt("Status");
		syncDay.order = obj.getInt("Order");

		return syncDay;
	}

	public long getId() {
		return id;
	}

	public long getUserId() {
		return userId;
	}

	public void setuserId(long userId) {
		this.userId = userId;
	}

	public long getSyncId() {
		return syncId;
	}

	public void setSyncId(long syncId) {
		this.syncId = syncId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

}