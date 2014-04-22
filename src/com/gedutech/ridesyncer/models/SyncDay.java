package com.gedutech.ridesyncer.models;

import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class SyncDay {

	long id;
	long driverId;
	long passengerId;
	long syncId;
	int weekday;
	int status;
	int order;
	Date createdAt;

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("DriverId", this.driverId);
		obj.put("PassengerId", this.passengerId);
		obj.put("SyncId", this.syncId);
		obj.put("Weekday", this.weekday);
		obj.put("Status", this.status);
		obj.put("Order", this.order);

		return obj;
	}

	public static SyncDay fromJSON(JSONObject obj) throws JSONException {
		SyncDay syncDay = new SyncDay();

		syncDay.id = obj.getLong("Id");
		syncDay.driverId = obj.getLong("DriverId");
		syncDay.passengerId = obj.getLong("PassengerId");
		syncDay.syncId = obj.getLong("SyncId");
		syncDay.weekday = obj.getInt("Weekday");
		syncDay.status = obj.getInt("Status");
		syncDay.order = obj.getInt("Order");

		return syncDay;
	}

	public long getId() {
		return id;
	}

	public long getDriverId() {
		return driverId;
	}

	public void setDriverId(long driverId) {
		this.driverId = driverId;
	}

	public long getPassengerId() {
		return passengerId;
	}

	public void setPassengerId(long passengerId) {
		this.passengerId = passengerId;
	}

	public long getSyncId() {
		return syncId;
	}

	public void setSyncId(long syncId) {
		this.syncId = syncId;
	}

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
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