package com.gedutech.ridesyncer.models;

import java.util.Date;
import java.util.ArrayList;

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

    public JSONObject toJSON() throws JSONException{
        JSONObject obj = new JSONObject();

        obj.put("Id", this.id);
//        obj.put("Created", this.createdAt);
        obj.put("SyncUsers", this.syncUsers);
        obj.put("SyncDays", this.syncDays);

        return obj;
    }

    public static Sync fromJSON(JSONObject obj) throws JSONException {
        Sync sync = new Sync();

        sync.id = obj.getLong("Id");
//        syncUsers.createdAt = obj.getDate("Created");
//        for(JSONObject syncUserObject : obj.getJSONArray("SyncUsers") {
//        	SyncUser.fromJSON(syncUserObject);
//        }
//        for(sync.syncDays = obj.getJSONArray(name)("SyncDays");

        return sync;
    }

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
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