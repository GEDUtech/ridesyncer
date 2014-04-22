package com.gedutech.ridesyncer.models;

import java.util.Date;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sync {

	long id;
	Date createdAt;
	ArrayList<SyncUser> syncUsers;
	ArrayList<SyncDay> syncDays;

	public Sync() {
		syncUsers = new ArrayList<SyncUser>();
		syncDays = new ArrayList<SyncDay>();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("SyncUsers", this.syncUsers);
		obj.put("SyncDays", this.syncDays);

		return obj;
	}

	public static Sync fromJSON(JSONObject obj) throws JSONException {
		Sync sync = new Sync();

		sync.id = obj.getLong("Id");

		JSONArray syncDaysArray = obj.getJSONArray("SyncDays");
		JSONArray syncUsersArray = obj.getJSONArray("SyncUsers");

		for (int i = 0; i < syncDaysArray.length(); i++) {
			sync.syncDays.add(SyncDay.fromJSON(syncDaysArray.getJSONObject(i)));
		}

		for (int i = 0; i < syncUsersArray.length(); i++) {
			sync.syncUsers.add(SyncUser.fromJSON(syncUsersArray.getJSONObject(i)));
		}

		return sync;
	}

	public long getId() {
		return id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public ArrayList<SyncUser> getSyncUsers() {
		return syncUsers;
	}

	public void setSyncUsers(ArrayList<SyncUser> syncUsers) {
		this.syncUsers = syncUsers;
	}

	public ArrayList<SyncDay> getSyncDays() {
		return syncDays;
	}

	public void setSyncDays(ArrayList<SyncDay> syncDays) {
		this.syncDays = syncDays;
	}
}