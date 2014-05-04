package com.gedutech.ridesyncer.models;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sync {

	long id;
	Date createdAt;
	int weekday;
	ArrayList<SyncUser> syncUsers;

	public Sync() {
		syncUsers = new ArrayList<SyncUser>();
	}

	public JSONObject toJSON() throws JSONException {
		JSONObject obj = new JSONObject();

		obj.put("Id", this.id);
		obj.put("Weekday", this.weekday);

		JSONArray syncUsersArray = new JSONArray();
		for (SyncUser syncUser : syncUsers) {
			syncUsersArray.put(syncUser.toJSON());
		}
		obj.put("SyncUsers", syncUsersArray);

		return obj;
	}

	public static Sync fromJSON(JSONObject obj) throws JSONException {
		Sync sync = new Sync();

		sync.id = obj.getLong("Id");
		sync.weekday = obj.getInt("Weekday");

		JSONArray syncUsersArray = obj.getJSONArray("SyncUsers");
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

	public int getWeekday() {
		return weekday;
	}

	public void setWeekday(int weekday) {
		this.weekday = weekday;
	}

}